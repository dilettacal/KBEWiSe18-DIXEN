package de.htw.ai.kbe.storage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.htw.ai.kbe.bean.Song;

public class SongsStorage implements ISongs{
	
	private static Map<Integer, Song> storage;
	
	public SongsStorage() {
		storage = new ConcurrentHashMap<Integer, Song>();
		List<Song> songs = null;
		try {
			songs = readJSONToSongs("songs.json");
		} catch (IOException e) {
			e.printStackTrace();
		}
		initSongsFromFile(songs);
	}


	private void initSongsFromFile(List<Song> songsFromJsonFile) {
		if(songsFromJsonFile == null){
			//TODO: Songs anderswie initialisieren ODER Programm abbrechen
			System.out.println("Es sind keine Songs in der Datei vorhanden.");
		}
		else {
			for (Song s: songsFromJsonFile) {
				addSong(s);
			}
		}	
	}
	

	//TODO: Evtl. erweitern, damit DB nicht null bleibt
	private static void initSomeSongs() {
		Song song1 = new Song.Builder("A love song").album("A love album").artist("An empty artist").released(2008).build();
		storage.put(1, song1);
		
		Song song2 = new Song.Builder("Song 2").album("Keine Ahnung").artist("A singer").released(1900).build();
		storage.put(2, song2);	
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
	public Song getSong(Integer id) {
		return storage.get(id);
	}

	@Override
	public Collection<Song> getAllSongs() {
		return storage.values();
	}

	@Override
	public Integer addSong(Song song) {
		song.setId((int) storage.keySet().stream().count()+1);
		storage.put(song.getId(), song);
		//liefert neu vergebene ID zurueck
		return song.getId();
	}

	@Override
	public boolean updateSong(Integer id, Song song) {
		Song oldSong = storage.get(id);
		song.setId(oldSong.getId());
		return storage.replace(oldSong.getId(), oldSong, song);
	}

	@Override
	public Song deleteSong(Integer id) {
		return storage.remove(id);
	}

}
