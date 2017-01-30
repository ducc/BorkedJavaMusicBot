package ovh.not.javamusicbot.lib.server;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import ovh.not.javamusicbot.lib.song.QueueSong;
import ovh.not.javamusicbot.lib.user.User;

import java.util.Date;
import java.util.function.Consumer;

interface MusicController extends ServerController {
    void play(QueueSong song);

    void load(String song, User addedBy, Date dateAdded);

    void load(String song, Consumer<AudioTrack> callback);

    default void load(String song, User addedBy) {
        load(song, addedBy, new Date());
    }

    void stop();

    void pause();

    void resume();

    void next();

    boolean isPlaying();

    boolean isPaused();

    QueueSong getCurrentSong();
}
