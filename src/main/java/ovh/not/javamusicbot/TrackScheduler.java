package ovh.not.javamusicbot;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;
    private final TextChannel textChannel;
    public final Queue<AudioTrack> queue;

    TrackScheduler(AudioPlayer player, TextChannel textChannel) {
        this.player = player;
        this.textChannel = textChannel;
        this.queue = new LinkedBlockingQueue<>();
    }

    public void queue(AudioTrack track) {
        if (!player.startTrack(track, true)) {
            queue.offer(track);
        }
    }

    public void next() {
        player.startTrack(queue.poll(), false);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            next();
        }
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        textChannel.sendMessage(String.format("Now playing **%s** by **%s** `[%d]`", track.getInfo().title,
                track.getInfo().author, track.getDuration())).complete();
    }
}
