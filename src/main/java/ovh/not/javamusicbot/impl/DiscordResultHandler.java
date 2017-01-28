package ovh.not.javamusicbot.impl;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import ovh.not.javamusicbot.lib.server.Server;
import ovh.not.javamusicbot.lib.song.Song;
import ovh.not.javamusicbot.lib.user.User;

import java.util.Date;

class DiscordResultHandler implements AudioLoadResultHandler {
    private final Server server;
    private final User addedBy;
    private final Date dateAdded;

    DiscordResultHandler(Server server, User addedBy, Date dateAdded) {
        this.server = server;
        this.addedBy = addedBy;
        this.dateAdded = dateAdded;
    }

    @Override
    public void trackLoaded(AudioTrack audioTrack) {
        DiscordQueueSong song = new DiscordQueueSong(audioTrack, server, addedBy, dateAdded);
        server.play(song);
    }

    @Override
    public void playlistLoaded(AudioPlaylist audioPlaylist) {
        if (audioPlaylist.getSelectedTrack() != null) {
            trackLoaded(audioPlaylist.getSelectedTrack());
        } else if (audioPlaylist.isSearchResult()) {
            // TODO
        } else {
            audioPlaylist.getTracks().forEach(track -> {
                server.play(new DiscordQueueSong(track, server, addedBy, dateAdded));
            });
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
