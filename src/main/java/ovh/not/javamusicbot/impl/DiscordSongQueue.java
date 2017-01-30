package ovh.not.javamusicbot.impl;

import ovh.not.javamusicbot.Database;
import ovh.not.javamusicbot.Statement;
import ovh.not.javamusicbot.UserManager;
import ovh.not.javamusicbot.lib.server.Server;
import ovh.not.javamusicbot.lib.song.QueueSong;
import ovh.not.javamusicbot.lib.song.SongQueue;

import java.sql.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

class DiscordSongQueue implements SongQueue {
    private final Queue<QueueSong> queue = new LinkedList<>();
    private final Database database;
    private final Server server;
    private final UserManager userManager;
    private QueueSong current = null;

    DiscordSongQueue(Database database, Server server, UserManager userManager) throws SQLException {
        this.database = database;
        this.server = server;
        this.userManager = userManager;
        init();
    }

    private void init() throws SQLException {
        try (Connection connection = database.dataSource.getConnection()) {
            PreparedStatement statement = database.prepare(connection, Statement.QUEUE_SELECT);
            statement.setString(1, server.getId());
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.isBeforeFirst()) {
                statement = database.prepare(connection, Statement.QUEUE_INSERT);
                statement.setString(1, server.getId());
                statement.execute();
            } else {
                resultSet.next();
                Array array = resultSet.getArray(1);
                if (array != null) {
                    Long[] songArray = (Long[]) array.getArray();
                    current = new DiscordQueueSong(database, server, userManager, songArray[0]);
                    LinkedList<QueueSong> queue = (LinkedList<QueueSong>) this.queue;
                    for (int i = 1; i < songArray.length; i++) {
                        queue.add(new DiscordQueueSong(database, server, userManager, songArray[i]));
                    }
                    server.play(current);
                }
            }
            resultSet.close();
        }
    }

    private void update() throws SQLException {
        try (Connection connection = database.dataSource.getConnection()) {
            PreparedStatement statement = database.prepare(connection, Statement.QUEUE_UPDATE);
            Array array = connection.createArrayOf("BIGINT", serialize());
            statement.setArray(1, array);
            statement.setString(2, server.getId());
            statement.execute();
        }
    }

    private Object[] serialize() {
        if (current == null) {
            return new Object[0];
        }
        Object[] array = new Object[queue.size() + 1];
        array[0] = current.getId();
        int index = 1;
        for (QueueSong queueSong : queue) {
            array[index] = queueSong.getId();
            index++;
        }
        return array;
    }

    void setCurrent(QueueSong current) {
        this.current = current;
        try {
            update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Collection<QueueSong> get() {
        return queue;
    }

    @Override
    public void add(QueueSong song) {
        queue.add(song);
        try {
            update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public QueueSong next() {
        if (queue.isEmpty()) {
            if (current != null) {
                try {
                    ((DiscordQueueSong) current).delete();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                setCurrent(null);
            }
            return null;
        }
        try {
            ((DiscordQueueSong) current).delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        current = queue.poll();
        try {
            update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return current;
    }

    private void deleteAll() throws SQLException {
        try (Connection connection = database.dataSource.getConnection()) {
            PreparedStatement statement = database.prepare(connection, Statement.QUEUE_SONGS_DELETE_ALL);
            statement.setString(1, server.getId());
            statement.execute();
        }
    }

    @Override
    public void clear() {
        try {
            deleteAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        queue.clear();
        try {
            update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
