package de.htw.ai.kbe.database.dao;

import java.util.List;
import java.util.NoSuchElementException;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;


import de.htw.ai.kbe.bean.Song;
import de.htw.ai.kbe.database.interfaces.ISongs;

public class SongDAO implements ISongs {

	@Inject
	private EntityManagerFactory emf;

	@SuppressWarnings("unchecked")
	@Override
	public List<Song> getAll() {
		EntityManager em = emf.createEntityManager();
		try {
			javax.persistence.Query query = em.createQuery("SELECT s FROM Song s");
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
		EntityManager em = emf.createEntityManager();
		Song s = getSongById(id);
		try {
			em.getTransaction().begin();
			// delete song from lists
			s.getLists().stream().forEach(l -> {
				l.getSongs().remove(s);
				em.merge(l);
			});
			em.remove(s);
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new PersistenceException("Could not persist entity: " + e.toString());
		} finally {
			em.close();
		}
	}

	@Override
	public int addSong(Song s) {
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			em.persist(s);
			em.getTransaction().commit();
			return s.getId();
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new PersistenceException("Could not persist entity: " + e.toString());
		} finally {
			em.close();
		}
	}

	@Override
	public void updateSong(Song s) throws NoSuchElementException {
		EntityManager em = emf.createEntityManager();
		if (s.getId() == null) {
			throw new NoSuchElementException();
		}

		// check if song exists in database
		getSongById(s.getId());
		try {
			em.getTransaction().begin();
			em.merge(s);
			em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback();
			throw new PersistenceException("Problem while updating this entity: " + e.toString());
		} finally {
			em.close();
		}
	}

}
