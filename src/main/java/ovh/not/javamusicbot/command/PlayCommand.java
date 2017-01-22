package ovh.not.javamusicbot.command;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.core.entities.VoiceChannel;
import ovh.not.javamusicbot.Command;
import ovh.not.javamusicbot.CommandManager;
import ovh.not.javamusicbot.GuildMusicManager;
import ovh.not.javamusicbot.LoadResultHandler;

import java.util.Set;

public class PlayCommand extends Command {
    private final CommandManager commandManager;
    private final AudioPlayerManager playerManager;

    public PlayCommand(CommandManager commandManager, AudioPlayerManager playerManager) {
        super("play", "p");
        this.commandManager = commandManager;
        this.playerManager = playerManager;
    }

    @Override
    public void on(Context context) {
        if (context.args.length == 0) {
            context.reply("Usage: `!!!p <link>` - plays a song\n" +
                    "To search youtube, use `!!!search: <your search term>`\n" +
                    "To add as first in queue, use `!!!p <link> -first`");
            return;
        }
        VoiceChannel channel = context.event.getMember().getVoiceState().getChannel();
        if (channel == null) {
            context.reply("You must be in a voice channel!");
            return;
        }
        GuildMusicManager musicManager = GuildMusicManager.getOrCreate(context.event.getGuild(),
                context.event.getTextChannel(), playerManager);
        LoadResultHandler handler = new LoadResultHandler(commandManager, musicManager, context);
        Set<String> flags = context.parseFlags();
        if (flags.contains("first") || flags.contains("f")) {
            handler.setFirstInQueue = true;
        }
        playerManager.loadItem(String.join(" ", context.args), handler);
        if (!musicManager.open) {
            musicManager.open(channel);
        }
    }
}
