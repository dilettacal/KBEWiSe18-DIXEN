package de.htw.ai.kbe.songsServlet;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * @author dixen
 * In_memory Database
 *
 */
public class Songs {
	/**
	 * Stores the new songs in the in memory database
	 */
	private static Map<Integer, Song> storage;
	
	
	/**
	 * The DB instance
	 */
	private static Songs instance = null;
	
	private Songs() {
		storage = new ConcurrentHashMap<Integer,Song>();
	}
	
	//Singleton
	public synchronized static Songs getInstance(List<Song> songsFromJsonFile) {
		if(instance == null) {
			instance = new Songs();
			initSongsDB(songsFromJsonFile);
		} 
		return instance;
	}
	
	private static void initSongsDB(List<Song> songsFromJsonFile) {
		//Reads songs from songs.json and stores them in the hashmap
		for (Song s: songsFromJsonFile) {
			instance.addSong(s);
		}
		
	}
	
	/**
	 * returns the song with songId
	 * @param songId
	 * @return
	 */
	public Song getSong(Integer songId) {
		return storage.get(songId);
	}
	
	public Collection<Song> getAllSongs(){
		return storage.values();
	}
	
	public Integer addSong(Song song) {
		//TODO: Check if Titel da ist
		String title = song.getTitle().replaceAll("\\s","");
		if(song.getTitle() == null || song.getTitle().equals("") || title.equals("")) {
			System.out.println("Der Song mit der ID "+song.getId()+" konnte nicht hinzugefuegt werden. Der Titel hat gefehlt.");
			return null;
		} else {
			Integer id = checkSongAlreadyInDB(song);
			if(id == null) {
				//Add song in the Hashmap
				song.setId((int) storage.keySet().stream().count()+1);
				storage.putIfAbsent(song.getId(), song);
				//System.out.println("Song added mit ID: " + song.getId());
				return song.getId();
			}
			else {
				return id;
			}
			
		}
	}
	
	public Integer checkSongAlreadyInDB(Song song) {
		//System.out.println(song.toString());
		for(Song s: storage.values()) {
			//System.out.println(s.toString());
			if(s.getTitle().equals(song.getTitle()) && s.getAlbum().equals(song.getAlbum()) && s.getArtist().equals(song.getArtist()) && s.getReleased() == song.getReleased()) {
				song.setId(s.getId());
				return song.getId();
			}			
		}
		return null;
	}
	
	public Integer getLastID() {
		return (int) (storage.keySet().stream().count()+1);
	}
}
