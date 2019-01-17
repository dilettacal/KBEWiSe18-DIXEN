package de.htw.ai.kbe.database.interfaces;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import de.htw.ai.kbe.bean.Song;

public interface ISongs {

//	public Song getSong(Integer id);
//	public Collection<Song> getAllSongs();
//	public Integer addSong(Song song);
//	public boolean updateSong(Integer id,Song song);
//	public Song deleteSong(Integer id);
	
	//Query Methoden meistens void - Interface angepasst
	
    List<Song> getAll();
    Song getSongById(int id) throws NoSuchElementException;
    void deleteSong(int id) throws NoSuchElementException;
    int addSong(Song s);
    void updateSong(Song s) throws NoSuchElementException;
}
