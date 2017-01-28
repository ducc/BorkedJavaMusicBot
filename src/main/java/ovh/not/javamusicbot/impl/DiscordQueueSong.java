package ovh.not.javamusicbot.impl;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import ovh.not.javamusicbot.lib.server.Server;
import ovh.not.javamusicbot.lib.song.QueueSong;
import ovh.not.javamusicbot.lib.song.SongQueue;
import ovh.not.javamusicbot.lib.user.User;

import java.util.Date;

class DiscordQueueSong extends DiscordSong implements QueueSong {
    private final Server server;
    private final User addedBy;
    private final Date dateAdded;

    DiscordQueueSong(AudioTrack audioTrack, Server server, User addedBy, Date dateAdded) {
        super(audioTrack);
        this.server = server;
        this.addedBy = addedBy;
        this.dateAdded = dateAdded;
    }

    @Override
    public SongQueue getSongQueue() {
        return server.getSongQueue();
    }

    @Override
    public User getAddedBy() {
        return addedBy;
    }

    @Override
    public Date getDateAdded() {
        return dateAdded;
    }
}
