package ovh.not.javamusicbot.impl;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import ovh.not.javamusicbot.Database;
import ovh.not.javamusicbot.Statement;
import ovh.not.javamusicbot.lib.song.Song;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

abstract class DiscordSong implements Song {
    private final Database database;
    final AudioTrack audioTrack;
    private int id = 0;
    private final String title;
    private final String author;
    private final long duration;

    DiscordSong(Database database, AudioTrack audioTrack) throws SQLException {
        this.database = database;
        this.audioTrack = audioTrack;
        this.title = audioTrack.getInfo().title;
        this.author = audioTrack.getInfo().author;
        this.duration = audioTrack.getDuration();
        init();
    }

    private void init() throws SQLException {
        try (Connection connection = database.dataSource.getConnection()) {
            PreparedStatement statement = database.prepare(connection, Statement.SONG_SELECT);
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

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getSource() {
        return audioTrack.getSourceManager().getSourceName();
    }

    @Override
    public String getIdentifier() {
        return audioTrack.getIdentifier();
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
