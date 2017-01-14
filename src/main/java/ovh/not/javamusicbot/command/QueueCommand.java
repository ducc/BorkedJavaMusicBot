package ovh.not.javamusicbot.command;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import ovh.not.javamusicbot.Command;
import ovh.not.javamusicbot.GuildMusicManager;

import java.util.Queue;

public class QueueCommand extends Command {
    private static final String QUEUE_LINE = "\n`%02d`. %s by %s `[%s]`";

    public QueueCommand() {
        super("queue", "list", "q");
    }

    @Override
    public void on(Context context) {
        GuildMusicManager musicManager = GuildMusicManager.get(context.event.getGuild());
        if (musicManager == null || musicManager.player.getPlayingTrack() == null) {
            context.reply("No music is queued or playing on this guild!");
            return;
        }
        AudioTrack playing = musicManager.player.getPlayingTrack();
        Queue<AudioTrack> queue = musicManager.scheduler.queue;
        StringBuilder builder = new StringBuilder("Song queue:");
        builder.append(String.format(QUEUE_LINE, 0, "**" + playing.getInfo().title + "**", "**"
                + playing.getInfo().author + "**", formatDuration(playing.getPosition()) + "/"
                + formatDuration(playing.getDuration())));
        int index = 1;
        for (AudioTrack track : queue) {
            builder.append(String.format(QUEUE_LINE, index, track.getInfo().title, track.getInfo().author,
                    formatDuration(track.getDuration())));
            index++;
        }
        context.reply(builder.toString());
    }
}
