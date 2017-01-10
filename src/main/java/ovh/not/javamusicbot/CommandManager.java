package ovh.not.javamusicbot;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import ovh.not.javamusicbot.command.PlayCommand;
import ovh.not.javamusicbot.command.QueueCommand;
import ovh.not.javamusicbot.command.SkipCommand;
import ovh.not.javamusicbot.command.StopCommand;

import java.util.HashMap;
import java.util.Map;

class CommandManager {
    private final Map<String, Command> commands = new HashMap<>();

    CommandManager() {
        AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        register(
                new PlayCommand(playerManager),
                new QueueCommand(),
                new SkipCommand(),
                new StopCommand()
        );
    }

    private void register(Command... commands) {
        for (Command command : commands) {
            for (String name : command.names) {
                if (this.commands.containsKey(name)) {
                    throw new RuntimeException(String.format("Command name collision %s in %s!", name,
                            command.getClass().getName()));
                }
                this.commands.put(name, command);
            }
        }
    }

    Command getCommand(String name) {
        return commands.get(name);
    }
}
