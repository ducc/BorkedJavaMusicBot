package ovh.not.javamusicbot;

import com.google.gson.Gson;
import com.moandjiezana.toml.Toml;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

import javax.security.auth.login.LoginException;
import java.io.File;

public final class MusicBot {
    private static final String CONFIG_PATH = "config.toml";
    public static final String USER_AGENT = "dabBot (https://github.com/sponges/JavaMusicBot)";
    public static final Gson GSON = new Gson();

    public static void main(String[] args) {
        Config config = new Toml().read(new File(CONFIG_PATH)).to(Config.class);
        CommandManager commandManager = new CommandManager(config);
        JDA jda;
        try {
            jda = new JDABuilder(AccountType.BOT)
                    .setToken(config.token)
                    .addListener(new Listener(config, commandManager))
                    .buildBlocking();
        } catch (LoginException | InterruptedException | RateLimitedException e) {
            e.printStackTrace();
            return;
        }
        jda.getPresence().setGame(Game.of(config.game));
    }
}
