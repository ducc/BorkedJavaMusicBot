INSERT INTO songs (source, identifier, title, author, duration) VALUES ($1, $2, $3, $4, $5);
SELECT currval(pg_get_serial_sequence('songs', 'id'));