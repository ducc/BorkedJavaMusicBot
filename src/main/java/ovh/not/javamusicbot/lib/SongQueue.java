package ovh.not.javamusicbot.lib;

import java.util.Collection;

public interface SongQueue {
    Collection<Song> get();

    void add(Song song);

    Song next();

    void clear();

    boolean isEmpty();
}
