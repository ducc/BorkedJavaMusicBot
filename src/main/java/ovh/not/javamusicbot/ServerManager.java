package ovh.not.javamusicbot;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.core.entities.Guild;
import ovh.not.javamusicbot.impl.DiscordServer;
import ovh.not.javamusicbot.lib.server.Server;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

class ServerManager {
    final Map<Guild, Server> servers = new HashMap<>();
    private final Database database;
    private final UserManager userManager;
    private final AudioPlayerManager audioPlayerManager;

    ServerManager(Database database, UserManager userManager, AudioPlayerManager audioPlayerManager) {
        this.database = database;
        this.userManager = userManager;
        this.audioPlayerManager = audioPlayerManager;
    }

    Server get(Guild guild) {
        if (!servers.containsKey(guild)) {
            Server server;
            try {
                server = new DiscordServer(database, userManager, guild, audioPlayerManager);
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
            servers.put(guild, server);
            return server;
        }
        return servers.get(guild);
    }
}
