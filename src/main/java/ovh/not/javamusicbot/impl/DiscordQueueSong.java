package ovh.not.javamusicbot.impl;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import ovh.not.javamusicbot.Database;
import ovh.not.javamusicbot.Statement;
import ovh.not.javamusicbot.lib.server.Server;
import ovh.not.javamusicbot.lib.song.QueueSong;
import ovh.not.javamusicbot.lib.song.SongQueue;
import ovh.not.javamusicbot.lib.user.User;

import java.sql.*;
import java.util.Date;

class DiscordQueueSong extends DiscordSong implements QueueSong {
    private final Database database;
    private final Server server;
    private final User addedBy;
    private final Date dateAdded;

    DiscordQueueSong(AudioTrack audioTrack, Database database, Server server, User addedBy, Date dateAdded) throws SQLException {
        super(database, audioTrack);
        this.database = database;
        this.server = server;
        this.addedBy = addedBy;
        this.dateAdded = dateAdded;
        init();
    }

    private void init() throws SQLException {
        try (Connection connection = database.dataSource.getConnection()) {
            PreparedStatement statement = database.prepare(connection, Statement.QUEUE_SONGS_EXISTS);
            statement.setString(1, server.getId());
            statement.setInt(2, getId());
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.isBeforeFirst()) {
                statement = database.prepare(connection, Statement.QUEUE_SONGS_INSERT);
                statement.setString(1, server.getId());
                statement.setInt(2, getId());
                statement.setString(3, addedBy.getId());
                statement.setTimestamp(4, new Timestamp(dateAdded.getTime()));
                statement.execute();
            }
            resultSet.close();
        }
    }

    void delete() throws SQLException {
        try (Connection connection = database.dataSource.getConnection()) {
            PreparedStatement statement = database.prepare(connection, Statement.QUEUE_SONGS_DELETE);
            statement.setString(1, server.getId());
            statement.setInt(2, getId());
            statement.execute();
        }
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
