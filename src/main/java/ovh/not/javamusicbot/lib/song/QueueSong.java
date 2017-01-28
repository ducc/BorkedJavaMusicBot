package ovh.not.javamusicbot.lib.song;

import ovh.not.javamusicbot.lib.user.User;

import java.util.Date;

public interface QueueSong extends Song {
    SongQueue getSongQueue();

    User getAddedBy();

    Date getDateAdded();
}
