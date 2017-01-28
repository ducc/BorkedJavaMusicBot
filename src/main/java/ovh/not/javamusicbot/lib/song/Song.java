package ovh.not.javamusicbot.lib.song;

public interface Song extends SongInfo {
    long getPosition();

    void setPosition(long position);
}
