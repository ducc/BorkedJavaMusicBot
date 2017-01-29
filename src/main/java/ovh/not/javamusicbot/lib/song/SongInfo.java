package ovh.not.javamusicbot.lib.song;

interface SongInfo {
    int getId();

    String getSource();

    String getIdentifier();

    String getTitle();

    String getAuthor();

    long getDuration();
}
