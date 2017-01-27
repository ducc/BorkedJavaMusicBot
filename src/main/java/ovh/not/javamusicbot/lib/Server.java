package ovh.not.javamusicbot.lib;

public interface Server extends ConnectionController, MusicController, ServerPropertyController {
    SongQueue getSongQueue();
}
