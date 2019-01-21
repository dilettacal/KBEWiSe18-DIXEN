package de.htw.ai.kbe.database.dao;

import java.util.List;
import java.util.NoSuchElementException;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;


import de.htw.ai.kbe.bean.Song;
import de.htw.ai.kbe.bean.SongList;
import de.htw.ai.kbe.database.interfaces.ISongs;

public class SongDAO implements ISongs {

	@Inject
	private EntityManagerFactory emf;

	@Override
	public List<Song> getAll() {
		EntityManager em = emf.createEntityManager();
		try {
			TypedQuery<Song> query = em.createQuery("SELECT s FROM Song s ORDER BY s.id", Song.class);
			return query.getResultList();
		} finally {
			em.close();
		}
	}

	@Override
	public Song getSongById(int id) throws NoSuchElementException {
		EntityManager em = emf.createEntityManager();
		Song s = null;
		try {
			s = em.find(Song.class, id);
			if (s == null) {
				throw new NoSuchElementException("No song with id " + id);
			}
		} finally {
			em.close();
		}

		return s;
	}

	@Override
	public void deleteSong(int id) throws NoSuchElementException {
		//Diese Methode wird nicht ausgefuehrt, da Loeschen von Songs unerlaubt ist
		//Song Loeschen wird am Endpunkt verweigert
		
//		EntityManager em = emf.createEntityManager();
//		Song s = getSongById(id);
//		try {
//			em.getTransaction().begin();
//			// delete song from lists
//			List<SongList> allListsWhereSongIsContainedIn = s.getLists();
//			
//			for(SongList sl: allListsWhereSongIsContainedIn) {
//				sl.getSongs().remove(s);
//				em.merge(sl);
//			}
//			em.remove(s);
//			em.getTransaction().commit();
//		} catch (Exception e) {
//			em.getTransaction().rollback();
//			throw new PersistenceException("Could not persist entity: " + e.toString());
//		} finally {
//			em.close();
//		}
		
		return;
	}

	@Override
	public int addSong(Song s) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction transaction = em.getTransaction();
		try {
			transaction.begin();
			em.persist(s);
			transaction.commit();
			return s.getId();
		} catch (Exception e) {
			transaction.rollback();
			throw new PersistenceException("Could not persist entity: " + e.toString());
		} finally {
			em.close();
		}
	}

	@Override
	public void updateSong(Song s) throws NoSuchElementException {
		EntityManager em = emf.createEntityManager();
		EntityTransaction transaction = em.getTransaction();
		if (s.getId() == null) {
			throw new NoSuchElementException();
		}

		// check if song exists in database
		getSongById(s.getId());
		try {
			transaction.begin();
			Song song = em.find(Song.class, s.getId());
			if(song != null) {
				song.updateSong(s);
			}
			//em.merge(s);
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			transaction.rollback();
			throw new PersistenceException("Problem while updating this entity: " + e.toString());
		} finally {
			em.close();
		}
	}

}
