package ovh.not.javamusicbot.impl;

import ovh.not.javamusicbot.lib.Song;
import ovh.not.javamusicbot.lib.SongQueue;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

class DiscordSongQueue implements SongQueue {
    private final Queue<Song> queue = new LinkedList<>();

    @Override
    public Collection<Song> get() {
        return queue;
    }

    @Override
    public void add(Song song) {
        queue.add(song);
    }

    @Override
    public Song next() {
        if (queue.isEmpty()) {
            return null;
        }
        return queue.poll();
    }

    @Override
    public void clear() {
        queue.clear();
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
