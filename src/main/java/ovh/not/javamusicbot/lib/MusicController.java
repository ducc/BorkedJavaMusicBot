package ovh.not.javamusicbot.lib;

interface MusicController extends ServerController {
    void play(Song song);

    void load(String song);

    void stop();

    void pause();

    void resume();

    void next();

    boolean isPlaying();

    boolean isPaused();

    Song getCurrentSong();
}
