package ovh.not.javamusicbot.lib.song;

interface SongInfo {
    String getId();

    String getSource();

    String getIdentifier();

    String getTitle();

    String getAuthor();

    long getDuration();
}
