package ovh.not.javamusicbot.lib.song;

import java.util.Collection;

public interface SongQueue extends SongQueueInfo {
    Collection<QueueSong> get();

    void add(QueueSong song);

    QueueSong next();

    void clear();

    boolean isEmpty();
}
