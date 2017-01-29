package ovh.not.javamusicbot.impl;

import ovh.not.javamusicbot.Database;
import ovh.not.javamusicbot.Statement;
import ovh.not.javamusicbot.lib.server.Server;
import ovh.not.javamusicbot.lib.song.QueueSong;
import ovh.not.javamusicbot.lib.song.SongQueue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

class DiscordSongQueue implements SongQueue {
    private final Queue<QueueSong> queue = new LinkedList<>();
    private final Database database;
    private final Server server;

    QueueSong current = null;

    DiscordSongQueue(Database database, Server server) throws SQLException {
        this.database = database;
        this.server = server;
        init();
    }

    private void init() throws SQLException {
        try (Connection connection = database.dataSource.getConnection()) {
            PreparedStatement statement = database.prepare(connection, Statement.QUEUE_EXISTS);
            statement.setString(1, server.getId());
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.isBeforeFirst()) {
                statement = database.prepare(connection, Statement.QUEUE_INSERT);
                statement.setString(1, server.getId());
                statement.execute();
            }
            resultSet.close();
        }
    }

    @Override
    public Collection<QueueSong> get() {
        return queue;
    }

    @Override
    public void add(QueueSong song) {
        queue.add(song);
    }

    @Override
    public QueueSong next() {
        if (queue.isEmpty()) {
            return null;
        }
        current = queue.poll();
        return current;
    }

    @Override
    public void clear() {
        queue.clear();
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    @Override
    public Server getServer() {
        return server;
    }

    @Override
    public QueueSong getCurrentSong() {
        return current;
    }
}
