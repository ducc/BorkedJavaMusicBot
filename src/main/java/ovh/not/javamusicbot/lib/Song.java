package ovh.not.javamusicbot.lib;

public interface Song {
    String getSource();

    String getIdentifier();

    long getPosition();

    void setPosition(long position);

    long getDuration();

    String getTitle();

    String getAuthor();
}
