package de.htw.ai.kbe.storage;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Before;
import org.junit.Test;

import de.htw.ai.kbe.bean.Song;

public class SongsStorageTest implements ISongs {

	private ISongs songsStorage;
	// Little inmemory DB
	private Song[] testSongs = {
			new Song.Builder("Man on the moon").id(1).artist("R.E.M.").album("R.E.M. Collection").released(1998)
					.build(),
			new Song.Builder("Nothing else matters").id(1).artist("Metallica").album("Metallica").released(1990)
					.build(),

	};

	

	@Override
	public Song getSong(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Song> getAllSongs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer addSong(Song song) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateSong(Integer id, Song song) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Song deleteSong(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

}
