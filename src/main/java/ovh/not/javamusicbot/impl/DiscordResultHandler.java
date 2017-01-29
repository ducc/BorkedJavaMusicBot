package ovh.not.javamusicbot.impl;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import ovh.not.javamusicbot.Database;
import ovh.not.javamusicbot.lib.server.Server;
import ovh.not.javamusicbot.lib.user.User;

import java.sql.SQLException;
import java.util.Date;

class DiscordResultHandler implements AudioLoadResultHandler {
    private final Database database;
    private final Server server;
    private final User addedBy;
    private final Date dateAdded;

    DiscordResultHandler(Database database, Server server, User addedBy, Date dateAdded) {
        this.database = database;
        this.server = server;
        this.addedBy = addedBy;
        this.dateAdded = dateAdded;
    }

    @Override
    public void trackLoaded(AudioTrack audioTrack) {
        DiscordQueueSong song;
        try {
            song = new DiscordQueueSong(audioTrack, database, server, addedBy, dateAdded);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        server.play(song);
    }

    @Override
    public void playlistLoaded(AudioPlaylist audioPlaylist) {
        if (audioPlaylist.getSelectedTrack() != null) {
            trackLoaded(audioPlaylist.getSelectedTrack());
        } else if (audioPlaylist.isSearchResult()) {
            // TODO
        } else {
            for (AudioTrack track : audioPlaylist.getTracks()) {
                try {
                    server.play(new DiscordQueueSong(track, database, server, addedBy, dateAdded));
                } catch (SQLException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    @Override
    public void noMatches() {
    }

    @Override
    public void loadFailed(FriendlyException e) {
        e.printStackTrace();
    }
}
