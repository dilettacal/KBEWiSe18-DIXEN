
CREATE TABLE IF NOT EXISTS listSongRelation (
  list_id INT NOT NULL,
  song_id INT NOT NULL
);


INSERT INTO listSongRelation (list_id, song_id) VALUES
(1, 1),
(1, 2),
(2, 3),
(2, 6),
(3, 7),
(3, 10),
(4, 4),
(4, 10);


CREATE TABLE IF NOT EXISTS songlists (
 id serial PRIMARY KEY,
  public BOOLEAN NOT NULL,
  owner_id varchar(100) NOT NULL REFERENCES users(id)
);

INSERT INTO songlists (public, owner_id) VALUES
(TRUE, 'mmuster'),
(FALSE, 'mmuster'),
(TRUE, 'eschuler'),
(FALSE, 'eschuler');


CREATE TABLE IF NOT EXISTS songs (
  id serial PRIMARY KEY,
  album varchar(100) DEFAULT NULL,
  artist varchar(100) DEFAULT NULL,
  released INT DEFAULT NULL,
  title varchar(100) NOT NULL
);

--
-- Daten für Tabelle `songs`
--

INSERT INTO songs (album, artist, released, title) VALUES
('Trolls', 'Justin Timberlake', 2016, 'Can''t Stop the Feeling'),
('Thank You', 'Meghan Trainor, Kelli Trainor', 2016, 'Mom'),
( NULL, 'Iggy Azalea', 2016, 'Team'),
('Ghostbusters', 'Fall Out Boy, Missy Elliott', 2016, 'Ghostbusters (I''m not a fraid)'),
('Bloom', 'Camila Cabello, Machine Gun Kelly', 2017, 'Bad Things'),
('At Night, Alone.', 'Mike Posner', 2016, 'I Took a Pill in Ibiza'),
('Top Hits 2017', 'Gnash', 2017, 'i hate u, i love u'),
('Thank You', 'Meghan Trainor', 2016, 'No'),
('Glory', 'Britney Spears', 2016, 'Private Show'),
('Lukas Graham (Blue Album)', 'Lukas Graham', 2015, '7 Years');


CREATE TABLE IF NOT EXISTS tokens (
  id serial PRIMARY KEY,
  token varchar(100) NOT NULL,
  user_id varchar(100) NOT NULL
);


CREATE TABLE IF NOT EXISTS users (
  id varchar(100) NOT NULL,
  firstName varchar(100) NOT NULL,
  lastName varchar(100) NOT NULL
);


INSERT INTO users (id, firstName, lastName) VALUES
('eschuler', 'Elena', 'Schuler'),
('mmuster', 'Maxime', 'Muster');


