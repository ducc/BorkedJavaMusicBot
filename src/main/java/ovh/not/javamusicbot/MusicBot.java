package ovh.not.javamusicbot;

import com.google.gson.Gson;
import com.moandjiezana.toml.Toml;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public final class MusicBot {
    private static final String CONFIG_PATH = "config.toml";
    private static final String CONSTANTS_PATH = "constants.toml";
    private static final String HIKARI_PATH = "hikari.properties";
    public static final String USER_AGENT = "dabBot (https://github.com/sponges/JavaMusicBot)";
    public static final Gson GSON = new Gson();

    @SuppressWarnings("WeakerAccess")
    public static final Collection<JDA> JDA_INSTANCES = new ArrayList<>(); // only used in eval

    public static void main(String[] args) {
        Config config = new Toml().read(new File(CONFIG_PATH)).to(Config.class);
        Constants constants = new Toml().read(new File(CONSTANTS_PATH))
                .to(Constants.class);
        HikariConfig hikariConfig = new HikariConfig(HIKARI_PATH);
        HikariDataSource dataSource = new HikariDataSource(hikariConfig);
        StatementManager statementManager = new StatementManager();
        Database database = new Database(statementManager, dataSource);
        try {
            createTables(statementManager, dataSource);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        if (args.length == 0) {
            JDA jda = setup(config, constants, database, false, 0, 0);
            JDA_INSTANCES.add(jda);
            return;
        }
        int shardCount = Integer.parseInt(args[0]);
        int minShard = Integer.parseInt(args[1]);
        int maxShard = Integer.parseInt(args[2]);
        for (int shard = minShard; shard < maxShard + 1;) {
            System.out.println("Starting shard " + shard + "...");
            JDA jda = setup(config, constants, database, true, shard, shardCount);
            JDA_INSTANCES.add(jda);
            shard++;
        }
    }

    private static JDA setup(Config config, Constants constants, Database database, boolean sharding, int shard, int shardCount) {
        AudioPlayerManager audioPlayerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(audioPlayerManager);
        CommandManager commandManager = new CommandManager(config, constants);
        UserManager userManager = new UserManager(database);
        ServerManager serverManager = new ServerManager(database, userManager, audioPlayerManager);
        JDA jda;
        try {
            JDABuilder builder = new JDABuilder(AccountType.BOT)
                    .setToken(config.token)
                    .setAudioEnabled(true)
                    .addListener(new Listener(config, commandManager, serverManager, userManager));
            if (sharding) {
                builder.useSharding(shard, shardCount);
            }
            jda = builder.buildBlocking();
        } catch (LoginException | InterruptedException | RateLimitedException e) {
            e.printStackTrace();
            return null;
        }
        jda.getPresence().setGame(Game.of(config.game));
        return jda;
    }

    private static void createTables(StatementManager statementManager, HikariDataSource dataSource) throws SQLException {
        String statement = statementManager.statements.get(Statement.CREATE_TABLES);
        try (Connection connection = dataSource.getConnection()) {
            connection.createStatement().execute(statement);
        }
    }
}
