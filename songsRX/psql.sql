-- phpMyAdmin SQL Dump
-- version 4.4.15.6
-- http://www.phpmyadmin.net
--
-- Host: db.f4.htw-berlin.de:3306
-- Erstellungszeit: 18. Jan 2019 um 15:43
-- Server-Version: 5.6.39
-- PHP-Version: 5.6.33-0+deb8u1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Datenbank: `_s0559842__songsrx`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `listSongRelation`
--

CREATE TABLE IF NOT EXISTS listSongRelation (
  list_id INT NOT NULL,
  song_id INT NOT NULL
);

--
-- Daten für Tabelle `listSongRelation`
--

INSERT INTO listSongRelation (list_id, song_id) VALUES
(1, 1),
(1, 2),
(2, 3),
(2, 6),
(3, 7),
(3, 10),
(4, 4),
(4, 10);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `songlists`
--

CREATE TABLE IF NOT EXISTS songlists (
 id serial PRIMARY KEY,
  public BOOLEAN NOT NULL,
  owner_id varchar(100) NOT NULL REFERENCES users(id)
);

--
-- Daten für Tabelle `songlists`
--

INSERT INTO songlists (public, owner_id) VALUES
(TRUE, 'mmuster'),
(FALSE, 'mmuster'),
(TRUE, 'eschuler'),
(FALSE, 'eschuler');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `songs`
--

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

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `tokens`
--

CREATE TABLE IF NOT EXISTS tokens (
  id serial PRIMARY KEY,
  token varchar(100) NOT NULL,
  user_id varchar(100) NOT NULL
);

--
-- Daten für Tabelle `tokens`
--

INSERT INTO tokens (token, user_id) VALUES
('4c23bf6cdfda498b8db40f6d228a1829', 'eschuler'),
('9f18406b4b0f42fc9d97fa9ddd192c3b', 'mmuster');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `users`
--

CREATE TABLE IF NOT EXISTS users (
  id varchar(100) NOT NULL,
  firstName varchar(100) NOT NULL,
  lastName varchar(100) NOT NULL
);

--
-- Daten für Tabelle `users`
--

INSERT INTO users (id, firstName, lastName) VALUES
('eschuler', 'Elena', 'Schuler'),
('mmuster', 'Maxime', 'Muster');

ALTER TABLE listSongRelation
ADD CONSTRAINT list_id_fk
FOREIGN KEY (list_id)
REFERENCES songlists (id)
ON DELETE CASCADE;