package ovh.not.javamusicbot;

import java.io.File;

public enum Statement {
    CREATE_TABLES("create_tables"),

    SERVER_INSERT("server/insert"),
    SERVER_DELETE("server/delete"),
    SERVER_UPDATE("server/update"),
    SERVER_UPDATE_OWNER("server/update_owner"),
    SERVER_UPDATE_VOICE_CHANNEL("server/update_voice_channel"),
    SERVER_SELECT("server/select"),

    SERVER_PERMISSIONS_INSERT("server/permissions/insert"),
    SERVER_PERMISSIONS_DELETE("server/permissions/delete"),
    SERVER_PERMISSIONS_UPDATE("server/permissions/update"),

    SERVER_PROPERTIES_INSERT("server/properties/insert"),
    SERVER_PROPERTIES_DELETE("server/properties/delete"),
    SERVER_PROPERTIES_UPDATE("server/properties/update"),
    SERVER_PROPERTIES_SELECT_ALL("server/properties/select_all"),

    USER_INSERT("user/insert"),
    USER_EXISTS("user/exists"),
    USER_DELETE("user/delete"),

    SONG_INSERT("song/insert"),
    SONG_DELETE("song/delete"),
    SONG_UPDATE("song/update"),
    SONG_SELECT_BY_ID("song/select_by_id"),
    SONG_SELECT_BY_IDENTIFIER("song/select_by_identifier"),

    QUEUE_INSERT("queue/insert"),
    QUEUE_DELETE("queue/delete"),
    QUEUE_UPDATE("queue/update"),
    QUEUE_SELECT("queue/select"),

    QUEUE_SONGS_INSERT("queue/song/insert"),
    QUEUE_SONGS_EXISTS("queue/song/exists"),
    QUEUE_SONGS_DELETE("queue/song/delete"),
    QUEUE_SONGS_DELETE_ALL("queue/song/delete_all"),
    QUEUE_SONGS_SELECT("queue/song/select"),

    PLAYLIST_INSERT("playlist/insert"),
    PLAYLIST_DELETE("playlist/delete"),
    PLAYLIST_UPDATE("playlist/update"),

    PLAYLIST_SONGS_INSERT("playlist/song/insert"),
    PLAYLIST_SONGS_DELETE("playlist/song/delete");

    private final String path;

    Statement(String path) {
        this.path = path;
    }

    private String getPath() {
        return "sql/" + path + ".sql";
    }

    public File getFile() {
        return new File(getPath());
    }
}
