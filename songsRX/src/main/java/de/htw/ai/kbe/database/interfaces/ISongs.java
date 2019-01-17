package de.htw.ai.kbe.database.interfaces;

import java.util.List;
import java.util.NoSuchElementException;

import de.htw.ai.kbe.bean.Song;

public interface ISongs {

	public Song getSongById(int id) throws NoSuchElementException; //method further specified as we can need a method getSongByTitle
	public List<Song> getAllSongs();
	public int addSong(Song song); //Primitive data types 
	public boolean updateLocalSong(Integer id,Song song);
	public void deleteSong(int id); //Primitive data types 
	public void updateSong(Song song);
	Song getSongByTitle(String title) throws NoSuchElementException;
}
