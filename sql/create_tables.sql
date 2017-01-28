CREATE TABLE users (
  id TEXT PRIMARY KEY NOT NULL
);

CREATE TABLE servers (
  id TEXT PRIMARY KEY NOT NULL,
  owner_id TEXT NOT NULL,
  FOREIGN KEY (owner_id) REFERENCES users(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE TABLE server_properties (
  server_id TEXT NOT NULL,
  property  TEXT NOT NULL,
  value     TEXT DEFAULT NULL,
  FOREIGN KEY (server_id) REFERENCES servers(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE TABLE server_permissions (
  server_id   TEXT NOT NULL,
  role        TEXT NOT NULL,
  permissions BIT VARYING,
  FOREIGN KEY (server_id) REFERENCES servers(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE TABLE songs (
  id       BIGSERIAL PRIMARY KEY NOT NULL,
  source   TEXT NOT NULL,
  title    TEXT NOT NULL,
  author   TEXT NOT NULL,
  duration BIGINT
);

CREATE TABLE queues (
  id            BIGSERIAL PRIMARY KEY NOT NULL,
  server_id     TEXT NOT NULL,
  voice_channel TEXT NOT NULL,
  current_song  BIGSERIAL NOT NULL,
  FOREIGN KEY (server_id) REFERENCES servers(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
  FOREIGN KEY (current_song) REFERENCES songs(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE TABLE queue_songs (
  queue_id   BIGSERIAL NOT NULL,
  song_id    BIGSERIAL NOT NULL,
  added_by   TEXT NOT NULL,
  date_added TIMESTAMP NOT NULL,
  FOREIGN KEY (queue_id) REFERENCES queues(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
  FOREIGN KEY (song_id) REFERENCES songs(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
  FOREIGN KEY (added_by) REFERENCES users(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE TABLE playlists (
  id           BIGSERIAL PRIMARY KEY NOT NULL,
  owner_id     TEXT NOT NULL,
  name         TEXT NOT NULL,
  description  TEXT DEFAULT NULL,
  date_created TIMESTAMP NOT NULL,
  FOREIGN KEY (owner_id) REFERENCES users(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE TABLE playlist_songs (
  playlist_id BIGSERIAL NOT NULL,
  song_id     BIGSERIAL NOT NULL,
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