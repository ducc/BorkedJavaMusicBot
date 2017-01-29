CREATE TABLE IF NOT EXISTS users (
  id TEXT PRIMARY KEY NOT NULL
);

CREATE TABLE IF NOT EXISTS servers (
  id TEXT PRIMARY KEY NOT NULL,
  owner_id TEXT NOT NULL,
  voice_channel TEXT DEFAULT NULL,
  FOREIGN KEY (owner_id) REFERENCES users(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS server_properties (
  server_id TEXT NOT NULL,
  property  TEXT NOT NULL,
  value     TEXT DEFAULT NULL,
  FOREIGN KEY (server_id) REFERENCES servers(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS server_permissions (
  server_id   TEXT NOT NULL,
  role        TEXT NOT NULL,
  permissions BIT VARYING,
  FOREIGN KEY (server_id) REFERENCES servers(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS songs (
  id         BIGSERIAL PRIMARY KEY NOT NULL,
  source     TEXT NOT NULL,
  identifier TEXT NOT NULL,
  title      TEXT NOT NULL,
  author     TEXT NOT NULL,
  duration   BIGINT
);

CREATE TABLE IF NOT EXISTS queues (
  server_id     TEXT NOT NULL,
  songs         BIGINT ARRAY DEFAULT NULL,
  FOREIGN KEY (server_id) REFERENCES servers(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS queue_songs (
  server_id  TEXT NOT NULL,
  song_id    BIGINT NOT NULL,
  added_by   TEXT NOT NULL,
  date_added TIMESTAMP NOT NULL,
  FOREIGN KEY (server_id) REFERENCES servers(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
  FOREIGN KEY (song_id) REFERENCES songs(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
  FOREIGN KEY (added_by) REFERENCES users(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS playlists (
  id           BIGSERIAL PRIMARY KEY NOT NULL,
  owner_id     TEXT NOT NULL,
  name         TEXT NOT NULL,
  date_created TIMESTAMP NOT NULL,
  FOREIGN KEY (owner_id) REFERENCES users(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS playlist_songs (
  playlist_id BIGINT NOT NULL,
  song_id     BIGINT NOT NULL,
  added_by    TEXT NOT NULL,
  date_added  TIMESTAMP NOT NULL,
  FOREIGN KEY (playlist_id) REFERENCES playlists(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
  FOREIGN KEY (song_id) REFERENCES songs(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
  FOREIGN KEY (added_by) REFERENCES users(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);