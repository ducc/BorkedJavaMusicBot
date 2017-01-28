package ovh.not.javamusicbot.lib.server;

import ovh.not.javamusicbot.lib.song.QueueSong;
import ovh.not.javamusicbot.lib.user.User;

import java.util.Date;

interface MusicController extends ServerController {
    void play(QueueSong song);

    void load(String song, User addedBy, Date dateAdded);

    void stop();

    void pause();

    void resume();

    void next();

    boolean isPlaying();

    boolean isPaused();

    QueueSong getCurrentSong();
}
