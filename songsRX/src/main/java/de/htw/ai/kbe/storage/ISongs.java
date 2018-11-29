package de.htw.ai.kbe.storage;

import java.util.Collection;

import de.htw.ai.kbe.bean.Song;

public interface ISongs {

	public Song getSong(Integer id);
	public Collection<Song> getAllSongs();
	public Integer addSong(Song song);
	public boolean updateSong(Integer id,Song song);
	public Song deleteSong(Integer id);
}
