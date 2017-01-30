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
import java.util.function.Consumer;

class DiscordResultHandler implements AudioLoadResultHandler {
    private final Database database;
    private final Server server;
    private final User addedBy;
    private final Date dateAdded;
    private final Consumer<AudioTrack> callback;

    DiscordResultHandler(Database database, Server server, User addedBy, Date dateAdded) {
        this.database = database;
        this.server = server;
        this.addedBy = addedBy;
        this.dateAdded = dateAdded;
        this.callback = null;
    }

    DiscordResultHandler(Database database, Server server, Consumer<AudioTrack> callback) {
        this.database = database;
        this.server = server;
        this.addedBy = null;
        this.dateAdded = null;
        this.callback = callback;
    }

    @Override
    public void trackLoaded(AudioTrack audioTrack) {
        if (callback == null) {
            DiscordQueueSong song;
            try {
                song = new DiscordQueueSong(audioTrack, database, server, addedBy, dateAdded);
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }
            server.play(song);
        } else {
            callback.accept(audioTrack);
        }
    }

    @Override
    public void playlistLoaded(AudioPlaylist audioPlaylist) {
        if (audioPlaylist.getSelectedTrack() != null) {
            trackLoaded(audioPlaylist.getSelectedTrack());
        } else if (audioPlaylist.isSearchResult()) {
            // TODO
        } else {
            audioPlaylist.getTracks().forEach(this::trackLoaded);
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
