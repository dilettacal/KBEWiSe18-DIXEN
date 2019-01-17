package de.htw.ai.kbe.database.dao;

import java.util.List;
import java.util.NoSuchElementException;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import de.htw.ai.kbe.bean.SongList;
import de.htw.ai.kbe.bean.User;
import de.htw.ai.kbe.database.interfaces.ISongList;

public class SongListDAO implements ISongList {

	@Inject
	private EntityManagerFactory emf;
	
	@Override
	public List<SongList> getAllListsOfUser(User user) {
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
	public SongList getListByIdAndUser(int listId, User user) throws NoSuchElementException {
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
	public void deleteSongList(SongList list) throws NoSuchElementException {
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			if(em.contains(list)) {
				em.remove(list);
			} else {
				em.remove(em.merge(list));
			}
			em.getTransaction().commit();
			
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new NoSuchElementException("Could not persist entity: " + e.toString());
		} finally {
			em.close();
}
		
	}

	@Override
	public void saveSongList(SongList list) {
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			em.persist(list);
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new PersistenceException("Could not persist entity: " + e.toString());
		} finally {
			em.close();
}
	}

}
