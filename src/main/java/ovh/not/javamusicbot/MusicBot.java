package ovh.not.javamusicbot;

import com.moandjiezana.toml.Toml;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

import javax.security.auth.login.LoginException;
import java.io.File;

public final class MusicBot {
    private static final String CONFIG_PATH = "config.toml";

    public static void main(String[] args) {
        Config config = new Toml().read(new File(CONFIG_PATH)).to(Config.class);
        CommandManager commandManager = new CommandManager();
        JDA jda;
        try {
            jda = new JDABuilder(AccountType.BOT)
                    .setToken(config.token)
                    .addListener(new Listener(commandManager))
                    .buildBlocking();
        } catch (LoginException | InterruptedException | RateLimitedException e) {
            e.printStackTrace();
        }
    }
}
