package ovh.not.javamusicbot;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.core.entities.Guild;
import ovh.not.javamusicbot.impl.DiscordServer;
import ovh.not.javamusicbot.lib.server.Server;

import java.util.HashMap;
import java.util.Map;

class ServerManager {
    final Map<Guild, Server> servers = new HashMap<>();
    private final AudioPlayerManager audioPlayerManager;

    ServerManager(AudioPlayerManager audioPlayerManager) {
        this.audioPlayerManager = audioPlayerManager;
    }

    Server get(Guild guild) {
        if (!servers.containsKey(guild)) {
            Server server = new DiscordServer(guild, audioPlayerManager);
            servers.put(guild, server);
            return server;
        }
        return servers.get(guild);
    }
}
