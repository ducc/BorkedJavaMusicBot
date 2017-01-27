package ovh.not.javamusicbot.impl;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.core.audio.AudioSendHandler;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.managers.AudioManager;
import ovh.not.javamusicbot.lib.*;

import java.util.ArrayList;
import java.util.Collection;

public class DiscordServer extends AudioEventAdapter implements Server {
    private final SongQueue songQueue = new DiscordSongQueue();
    private final Collection<ServerProperty> serverProperties = new ArrayList<>();
    private final Guild guild;
    private final AudioPlayerManager audioPlayerManager;
    private final AudioPlayer audioPlayer;
    private final DiscordResultHandler resultHandler;
    private boolean playing = false;
    private boolean paused = false;
    private Song currentSong = null;
    public VoiceChannel voiceChannel = null;

    public DiscordServer(Guild guild, AudioPlayerManager audioPlayerManager) {
        this.guild = guild;
        this.audioPlayerManager = audioPlayerManager;
        audioPlayer = audioPlayerManager.createPlayer();
        audioPlayer.addListener(this);
        AudioSendHandler audioSendHandler = new AudioPlayerSendHandler(audioPlayer);
        guild.getAudioManager().setSendingHandler(audioSendHandler);
        resultHandler = new DiscordResultHandler(this);
    }

    @Override
    public void play(Song song) {
        if (audioPlayer.startTrack(((DiscordSong) song).audioTrack, true)) {
            songQueue.add(song);
        }
        currentSong = song;
        playing = true;
    }

    @Override
    public void load(String song) {
        audioPlayerManager.loadItem(song, resultHandler);
    }

    @Override
    public void stop() {
        audioPlayer.stopTrack();
        currentSong = null;
        playing = false;
        paused = false;
    }

    @Override
    public void pause() {
        audioPlayer.setPaused(true);
    }

    @Override
    public void resume() {
        audioPlayer.setPaused(false);
    }

    @Override
    public void next() {
        if (!audioPlayer.startTrack(((DiscordSong) songQueue.next()).audioTrack, false)) {
            stop();
            disconnect();
        }
    }

    @Override
    public boolean isPlaying() {
        return isConnected() && playing;
    }

    @Override
    public boolean isPaused() {
        return paused;
    }

    @Override
    public Song getCurrentSong() {
        return currentSong;
    }

    @Override
    public void connect(VoiceChannel voiceChannel) throws AlreadyConnectedException, PermissionException {
        if (guild.getAudioManager().isConnected()) {
            throw new AlreadyConnectedException();
        }
        try {
            AudioManager audioManager = guild.getAudioManager();
            audioManager.openAudioConnection(voiceChannel);
            audioManager.setSelfDeafened(true);
            this.voiceChannel = voiceChannel;
        } catch (net.dv8tion.jda.core.exceptions.PermissionException e) {
            throw new PermissionException();
        }
    }

    @Override
    public void disconnect() {
        guild.getAudioManager().closeAudioConnection();
        this.voiceChannel = null;
    }

    @Override
    public boolean isConnected() {
        return guild.getAudioManager().isConnected();
    }

    @Override
    public Collection<ServerProperty> getProperties() {
        return serverProperties;
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        super.onTrackStart(player, track);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            next();
        }
    }

    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
        super.onTrackException(player, track, exception);
    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
        super.onTrackStuck(player, track, thresholdMs);
    }

    @Override
    public void onPlayerPause(AudioPlayer player) {
        paused = true;
    }

    @Override
    public void onPlayerResume(AudioPlayer player) {
        paused = false;
    }

    @Override
    public SongQueue getSongQueue() {
        return songQueue;
    }
}
