package de.htw.ai.kbe.storage;


import java.util.Collection;
import java.util.Map;
import de.htw.ai.kbe.bean.Song;

@Deprecated
public class SongStorageForTesting implements ISongs {

	private Map<Integer, Song> miniSongDB;
		
	public SongStorageForTesting() {
		Song song1, song2;
		song1 = new Song.Builder("Man on the moon").id(1).artist("R.E.M.").album("R.E.M. Collection").released(1998)
				.build();
		song2 = new Song.Builder("Nothing else matters").id(2).artist("Metallica").album("Metallica").released(1990)
				.build();
		
		miniSongDB.put(song1.getId(), song1);
		miniSongDB.put(song2.getId(), song2);
	}

	@Override
	public Song getSong(Integer id) {
		return miniSongDB.get(id);
	}

	@Override
	public Collection<Song> getAllSongs() {
		return miniSongDB.values();
	}

	@Override
	public Integer addSong(Song song) {
		if(song.getTitle() != null && !(song.getTitle().trim()).isEmpty()) {
			song.setId((int) miniSongDB.keySet().stream().count()+1);
			miniSongDB.put(song.getId(), song);
			//liefert neu vergebene ID zurueck
			return song.getId();
		}
		else
			return null;
	}

	@Override
	public boolean updateSong(Integer id, Song song) {
		Song oldSong = miniSongDB.get(id);
		if(oldSong != null && song.getTitle() != null && !(song.getTitle().trim()).isEmpty()) {
			song.setId(oldSong.getId());
			return miniSongDB.replace(oldSong.getId(), oldSong, song);
		}
		else
			return false;
	}

	@Override
	public Song deleteSong(Integer id) {
		return miniSongDB.remove(id);
	}

}
