package de.htw.ai.kbe.database.dao;

import java.util.List;
import java.util.NoSuchElementException;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.hibernate.cfg.NotYetImplementedException;

import de.htw.ai.kbe.bean.Song;
import de.htw.ai.kbe.database.interfaces.ISongs;
@Singleton
public class SongDAO implements ISongs {

//	@Inject
//    protected EntityManager em;

	@Inject
	private EntityManagerFactory emf;

	@Override
	public List<Song> getAllSongs() {
		EntityManager em = emf.createEntityManager();
		try {
			Query query = em.createQuery("SELECT s FROM Song s");
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
	public int addSong(Song song) {
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			em.persist(song);
			em.getTransaction().commit();
			return song.getId();
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new PersistenceException("Could not persist entity: " + e.toString());
		} finally {
			em.close();
		}
	}

	@Override
	public void updateSong(Song song) throws NoSuchElementException {
		EntityManager em = emf.createEntityManager();
		if (song.getId() == null) {
			throw new NoSuchElementException();
		}

		// check if song exists in database
		getSongById(song.getId());
		try {
			em.getTransaction().begin();
			em.merge(song);
			em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback();
			throw new PersistenceException("Problem while updating this entity: " + e.toString());
		} finally {
			em.close();
}
		
	}

	@Override
	public Song getSongByTitle(String title) throws NoSuchElementException {
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("SELECT s FROM Song s WHERE s.title = :title");
		query.setParameter("title", title);
		Song song = (Song) query.getSingleResult();
		if (song != null) {
			return song;
		} else
			throw new NoSuchElementException("Song not found");

	}

	@Override
	public boolean updateLocalSong(Integer id, Song song) {
		throw new NotYetImplementedException();
	}

	@Override
	public void deleteSong(int id) {
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

}
