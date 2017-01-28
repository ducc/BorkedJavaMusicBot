package ovh.not.javamusicbot.lib.server;

import ovh.not.javamusicbot.lib.song.SongQueue;

public interface Server extends ServerInfo, ConnectionController, MusicController, ServerPropertyController {
    SongQueue getSongQueue();
}
