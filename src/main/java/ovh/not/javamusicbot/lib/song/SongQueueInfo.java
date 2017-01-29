package ovh.not.javamusicbot.lib.song;

import ovh.not.javamusicbot.lib.server.Server;

interface SongQueueInfo {
    Server getServer();

    QueueSong getCurrentSong();
}
