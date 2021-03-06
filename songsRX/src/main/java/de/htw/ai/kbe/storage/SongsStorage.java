package de.htw.ai.kbe.storage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.ws.rs.NotFoundException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.htw.ai.kbe.bean.Song;
import de.htw.ai.kbe.database.interfaces.ISongs;

/**
 * Lokale InMemory-DB fuer Songs
 *
 */
public class SongsStorage implements ISongs{

	private static Map<Integer, Song> storage;
	private static SongsStorage instance = null;
	private AtomicInteger lastID;

	public SongsStorage() {
		storage = new ConcurrentHashMap<Integer, Song>();
		List<Song> songs = null;
		try {
			songs = readJSONToSongs("songs.json");
			//JSON File: First song has id 10
			//Collections.reverse(songs); //Reverse so that first song in our in memory storage has the id 1
		} catch (IOException e) {
			e.printStackTrace();
		}
		boolean initFromFileOK = initSongsFromFile(songs);
		if(initFromFileOK == false) {
			initSomeSongs();
		}
	}

	public static SongsStorage getInstance() {
		if(instance == null)
			instance = new SongsStorage();
		return instance;
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
	public List<Song> getAll() {
		return storage.values().stream().collect(Collectors.toList());
	}

	@Override
	public Song getSongById(int id) throws NoSuchElementException {
		return storage.get(id);
	}

	@Override
	public synchronized void deleteSong(int id) throws NoSuchElementException {
		if (storage.remove(id) == null) {
            throw new NotFoundException("No song with id '" + id + "'!");
        }
	}

	@Override
	public synchronized int addSong(Song s) {
		int id = lastID.incrementAndGet();
		s.setId(id);
        storage.put(s.getId(), s);
        return s.getId();
	}

	@Override
	public void updateSong(Song s) throws NoSuchElementException {
		if (s.getId() == null || !storage.containsKey(s.getId())) {
            throw new NotFoundException("No song with id '" + s.getId() + "'!");
        }
        storage.put(s.getId(), s);
	}



}
