package ovh.not.javamusicbot.impl;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.audio.AudioSendHandler;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.managers.AudioManager;
import org.json.JSONObject;
import ovh.not.javamusicbot.Database;
import ovh.not.javamusicbot.Statement;
import ovh.not.javamusicbot.UserManager;
import ovh.not.javamusicbot.lib.AlreadyConnectedException;
import ovh.not.javamusicbot.lib.PermissionException;
import ovh.not.javamusicbot.lib.server.Server;
import ovh.not.javamusicbot.lib.server.ServerProperty;
import ovh.not.javamusicbot.lib.song.QueueSong;
import ovh.not.javamusicbot.lib.song.SongQueue;
import ovh.not.javamusicbot.lib.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class DiscordServer extends AudioEventAdapter implements Server {
    private final SongQueue songQueue = new DiscordSongQueue(this);
    private final Collection<ServerProperty> serverProperties = new ArrayList<>();
    private final Database database;
    private final UserManager userManager;
    private final Guild guild;
    private final AudioPlayerManager audioPlayerManager;
    private final AudioPlayer audioPlayer;
    private final User owner;
    private boolean playing = false;
    private boolean paused = false;
    public VoiceChannel voiceChannel = null;

    public DiscordServer(Database database, UserManager userManager, Guild guild, AudioPlayerManager audioPlayerManager) throws SQLException {
        this.database = database;
        this.userManager = userManager;
        this.guild = guild;
        this.audioPlayerManager = audioPlayerManager;
        audioPlayer = audioPlayerManager.createPlayer();
        audioPlayer.addListener(this);
        AudioSendHandler audioSendHandler = new AudioPlayerSendHandler(audioPlayer);
        guild.getAudioManager().setSendingHandler(audioSendHandler);
        owner = userManager.get(guild.getOwner().getUser());
        init();
        initProperties();
    }

    private void init() throws SQLException {
        try (Connection connection = database.dataSource.getConnection()) {
            PreparedStatement statement = database.prepare(connection, Statement.SERVER_EXISTS);
            statement.setString(1, getId());
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.isBeforeFirst()) {
                statement = database.prepare(connection, Statement.SERVER_INSERT);
                statement.setString(1, getId());
                statement.setString(2, owner.getId());
                statement.execute();
            }
            resultSet.close();
        }
    }

    private void initProperties() throws SQLException {
        try (Connection connection = database.dataSource.getConnection()) {
            PreparedStatement statement = database.prepare(connection, Statement.SERVER_PROPERTIES_SELECT_ALL);
            statement.setString(1, getId());
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.isBeforeFirst()) {
                return;
            }
            while (resultSet.next()) {
                String property = resultSet.getString(1);
                String value = resultSet.getString(2);
                serverProperties.add(new ServerProperty(property, new JSONObject(value)));
            }
            resultSet.close();
        }
    }

    @Override
    public void play(QueueSong song) {
        if (audioPlayer.startTrack(((DiscordQueueSong) song).audioTrack, true)) {
            ((DiscordSongQueue) songQueue).current = song;
        } else {
            songQueue.add(song);
        }
    }

    @Override
    public void load(String song, User addedBy, Date dateAdded) {
        DiscordResultHandler resultHandler = new DiscordResultHandler(this, addedBy, dateAdded);
        audioPlayerManager.loadItem(song, resultHandler);
    }

    @Override
    public void stop() {
        audioPlayer.stopTrack();
        audioPlayer.destroy();
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
        DiscordSong song = (DiscordSong) songQueue.next();
        if (song == null) {
            stop();
            disconnect();
        } else {
            AudioTrack track = song.audioTrack;
            audioPlayer.startTrack(track, false);
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
    public QueueSong getCurrentSong() {
        return songQueue.getCurrentSong();
    }

    @Override
    public void connect(VoiceChannel voiceChannel) throws AlreadyConnectedException, PermissionException {
        if (guild.getAudioManager().isConnected()) {
            throw new AlreadyConnectedException();
        }
        if (!guild.getSelfMember().hasPermission(voiceChannel, Permission.VOICE_CONNECT)) {
            throw new PermissionException();
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
        playing = true;
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

    @Override
    public String getId() {
        return guild.getId();
    }

    @Override
    public User getOwner() {
        return owner;
    }
}
