package ovh.not.javamusicbot.impl;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import ovh.not.javamusicbot.lib.Server;
import ovh.not.javamusicbot.lib.Song;

class DiscordResultHandler implements AudioLoadResultHandler {
    private final Server server;

    DiscordResultHandler(Server server) {
        this.server = server;
    }

    @Override
    public void trackLoaded(AudioTrack audioTrack) {
        Song song = new DiscordSong(audioTrack);
        server.play(song);
    }

    @Override
    public void playlistLoaded(AudioPlaylist audioPlaylist) {
        if (audioPlaylist.getSelectedTrack() != null) {
            trackLoaded(audioPlaylist.getSelectedTrack());
        } else if (audioPlaylist.isSearchResult()) {
            // TODO
        } else {
            audioPlaylist.getTracks().forEach(track -> server.play(new DiscordSong(track)));
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
