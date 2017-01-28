INSERT INTO playlists (owner_id, name, date_created) VALUES ($1, $2, $3);
SELECT currval(pg_get_serial_sequence('playlists', 'id'));