package de.htw.ai.kbe.database.dao;

import java.util.Collection;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import de.htw.ai.kbe.bean.SongList;
import de.htw.ai.kbe.bean.User;
import de.htw.ai.kbe.database.interfaces.ISongList;

@Singleton
public class SongListDAO implements ISongList {

	@Inject
	private EntityManagerFactory emf;

	@SuppressWarnings("unchecked")
	@Override
	public List<SongList> getListByUser(User user) {
		EntityManager em = emf.createEntityManager();
		try {
			Query q = em.createQuery("SELECT l FROM SongList l WHERE l.owner = :user");
			q.setParameter("user", user);
			return q.getResultList();
		} finally {
			em.close();
		}
	}

	@Override
	public SongList getListByIdAndUser(int listId, User user) {
		// throws NoSuchElementException;
		EntityManager em = emf.createEntityManager();
		try {
			Query q = em.createQuery("SELECT l FROM SongList l WHERE l.id = :id AND l.owner = :user");
			q.setParameter("id", listId);
			q.setParameter("user", user);
			SongList sl = (SongList) q.getSingleResult();
			return sl;
		} finally {
			em.close();
		}
	}

	@Override
	public boolean deleteSongList(SongList list) {
		// throws NoSuchElementException;
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			if(em.contains(list)) {
				em.remove(list);
			} else {
				em.remove(em.merge(list));
			}
			em.getTransaction().commit();
			return true;
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new PersistenceException("Could not persist entity: " + e.toString());
		} finally {
			em.close();
		}
	}

	@Override
	public boolean saveSongList(SongList list) {
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			em.persist(list);
			em.getTransaction().commit();
			return true;
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new PersistenceException("Could not persist entity: " + e.toString());
		} finally {
			em.close();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<SongList> findSongListByAccessType(User user, boolean isPublic) {
		EntityManager em = emf.createEntityManager();
		boolean isUserNull = user == null;
		if (!isUserNull) {
			try { // Listen von user
				Query q = em.createQuery("SELECT l FROM SongList l WHERE l.owner = :user AND l.isPublic = :isPublic");
				q.setParameter("user", user);
				q.setParameter("isPublic", isPublic);
				return q.getResultList();
			} finally {
				em.close();
			}
		} else { // nur public Listen
			try {
				Query q = em.createQuery("SELECT l FROM SongList l WHERE l.isPublic = :isPublic");
				q.setParameter("isPublic", isPublic);
				return q.getResultList();
			} finally {
				em.close();
			}

		}
	}

	@Override
	public SongList getSongListByID(int id) {
		EntityManager em = emf.createEntityManager();
		SongList songList = null;
		try {
			// Ansatz mit Query
			Query q = em.createQuery("SELECT l FROM SongList l WHERE l.id = :id");
			q.setParameter("id", id);
			songList = (SongList) q.getSingleResult();
		} finally {
			em.close();
		}
		return songList;
	}

	

}
