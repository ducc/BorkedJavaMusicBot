package ovh.not.javamusicbot.lib.song;

interface SongInfo {
    long getId();

    String getSource();

    String getIdentifier();

    String getTitle();

    String getAuthor();

    long getDuration();
}
