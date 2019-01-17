package de.htw.ai.kbe.oldStorage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.htw.ai.kbe.bean.Song;
import de.htw.ai.kbe.database.interfaces.ISongs;

public class SongsStorage implements ISongs{
	
	private Map<Integer, Song> storage;
	private AtomicInteger lastID;
	
	public SongsStorage() {
		storage = new ConcurrentHashMap<Integer, Song>();
		List<Song> songs = null;
		try {
			songs = readJSONToSongs("songs.json");
			System.out.println("Read from json file complete!");
			//JSON File: First song has id 10 --> Reverse
			Collections.reverse(songs); //Reverse so that first song in our in memory storage has the id 1
		} catch (IOException e) {
			e.printStackTrace();
		}
		boolean initFromFileOK = initSongsFromFile(songs);
		if(initFromFileOK == false) {
			initSomeSongs();
		}
	}

	private boolean initSongsFromFile(List<Song> songsFromJsonFile) {
		if(songsFromJsonFile == null){
			return false;
		}
		else {
			for (Song s: songsFromJsonFile) {
				if(s.getTitle() != null && !(s.getTitle().trim()).isEmpty())
					//addSong(song) only for POST-Requests, here song is added directly
					storage.put(s.getId(), s);
			}
			//Keeps track of the number of songs in the DB
			int valuesDB = (int) storage.keySet().stream().count();
			//initializes lastID as AtomicInteger
			lastID = new AtomicInteger(valuesDB);
			return true;
		}	
	}
	
	private void initSomeSongs() {
		Song song1 = new Song.Builder("A love song").album("A love album").artist("An empty artist").released(2008).build();
		storage.put(1, song1);
		
		Song song2 = new Song.Builder("Song 2").album("Keine Ahnung").artist("A singer").released(1900).build();
		storage.put(2, song2);	
		
		//Keeps track of the number of songs in DB
		int valuesDB = (int) storage.keySet().stream().count();
		//initializes lastID as AtomicInteger
		lastID = new AtomicInteger(valuesDB);
	}
	
	@SuppressWarnings("unchecked")
	private List<Song> readJSONToSongs(String filename) throws FileNotFoundException, IOException {
		//Read file
		ClassLoader classLoader = SongsStorage.class.getClassLoader();
		File file = new File(classLoader.getResource(filename).getFile());

		ObjectMapper objectMapper = new ObjectMapper();
		try (InputStream is = new BufferedInputStream(new FileInputStream(file))) {
			return (List<Song>) objectMapper.readValue(is, new TypeReference<List<Song>>(){});
		}
}


	@Override
	public synchronized Song getSongById(int id) {
		return storage.get(id);
	}

	@Override
	public synchronized List<Song> getAllSongs() {
		return storage.values().stream().collect(Collectors.toList());
	}

	@Override
	public synchronized int addSong(Song song) {
		if(song.getTitle() != null && !(song.getTitle().trim()).isEmpty()) {
			int newID = lastID.incrementAndGet();
			System.out.println("Neues ID: " + newID);
			song.setId(newID);
			storage.put(song.getId(), song);
			//liefert neu vergebene ID zurueck
			return song.getId();
		}
		else
			return -1;
	}

	@Override
	public synchronized boolean updateLocalSong(Integer id, Song song) {
		Song oldSong = storage.get(id);
		if(oldSong != null && song.getTitle() != null && !(song.getTitle().trim()).isEmpty()) {
			song.setId(oldSong.getId());
			return storage.replace(oldSong.getId(), oldSong, song);
		}
		else
			return false;
	}

	@Override
	public synchronized void deleteSong(int id) {
		storage.remove(id);
	}

	@Override
	public void updateSong(Song song) {
		return;
	}

	@Override
	public Song getSongByTitle(String title) throws NoSuchElementException {
		return (Song) storage.values().stream().filter(s -> s.getTitle().equals(title)).toArray()[0];
	}

}
