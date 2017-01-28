package ovh.not.javamusicbot.impl;

import ovh.not.javamusicbot.lib.server.Server;
import ovh.not.javamusicbot.lib.song.QueueSong;
import ovh.not.javamusicbot.lib.song.SongQueue;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

class DiscordSongQueue implements SongQueue {
    private final Queue<QueueSong> queue = new LinkedList<>();
    private final String id;
    private final Server server;

    QueueSong current = null;

    DiscordSongQueue(Server server) {
        this.server = server;
        id = null; // TODO sql insert
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
    public String getId() {
        return id;
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
