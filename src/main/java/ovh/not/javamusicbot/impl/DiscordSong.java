package ovh.not.javamusicbot.impl;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import ovh.not.javamusicbot.Database;
import ovh.not.javamusicbot.Statement;
import ovh.not.javamusicbot.lib.server.Server;
import ovh.not.javamusicbot.lib.song.Song;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

abstract class DiscordSong implements Song {
    private final Database database;
    private final Server server;
    AudioTrack audioTrack = null;
    private long id = 0;
    private String source = null;
    private String identifier = null;
    private String title = null;
    private String author = null;
    private long duration = 0;

    DiscordSong(Database database, Server server, AudioTrack audioTrack) throws SQLException {
        this.database = database;
        this.server = server;
        this.audioTrack = audioTrack;
        this.source = audioTrack.getSourceManager().getSourceName();
        this.identifier = audioTrack.getIdentifier();
        this.title = audioTrack.getInfo().title;
        this.author = audioTrack.getInfo().author;
        this.duration = audioTrack.getDuration();
        initIdentifier();
    }

    DiscordSong(Database database, Server server, long id) throws SQLException {
        this.database = database;
        this.server = server;
        this.id = id;
        initId();
    }

    private void initIdentifier() throws SQLException {
        try (Connection connection = database.dataSource.getConnection()) {
            PreparedStatement statement = database.prepare(connection, Statement.SONG_SELECT_BY_IDENTIFIER);
            statement.setString(1, getSource());
            statement.setString(2, getIdentifier());
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.isBeforeFirst()) {
                resultSet.close();
                statement = database.prepare(connection, Statement.SONG_INSERT);
                statement.setString(1, getSource());
                statement.setString(2, getIdentifier());
                statement.setString(3, getTitle());
                statement.setString(4, getAuthor());
                statement.setLong(5, getDuration());
                resultSet = statement.executeQuery();
                resultSet.next();
                this.id = resultSet.getInt(1);
            } else {
                resultSet.next();
                this.id = resultSet.getInt(1);
                String title = resultSet.getString(2);
                String author = resultSet.getString(3);
                long duration = resultSet.getLong(4);
                if (!this.title.equals(title) || !this.author.equals(author) || this.duration != duration) {
                    statement = database.prepare(connection, Statement.SONG_UPDATE);
                    statement.setString(1, getTitle());
                    statement.setString(2, getAuthor());
                    statement.setLong(3, getDuration());
                }
            }
            resultSet.close();
        }
    }

    private void initId() throws SQLException {
        try (Connection connection = database.dataSource.getConnection()) {
            PreparedStatement statement = database.prepare(connection, Statement.SONG_SELECT_BY_ID);
            statement.setLong(1, getId());
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.isBeforeFirst()) {
                return;
            } else {
                resultSet.next();
                this.source = resultSet.getString(1);
                this.identifier = resultSet.getString(2);
                this.title = resultSet.getString(3);
                this.author = resultSet.getString(4);
                this.duration = resultSet.getLong(5);
                server.load(identifier, track -> {
                    audioTrack = track;
                });
            }
            resultSet.close();
        }
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getSource() {
        return source;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public long getPosition() {
        return audioTrack.getPosition();
    }

    @Override
    public void setPosition(long position) {
        audioTrack.setPosition(position);
    }

    @Override
    public long getDuration() {
        return duration;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getAuthor() {
        return author;
    }
}
