package de.htw.ai.kbe.database.dao;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import de.htw.ai.kbe.bean.Song;
import de.htw.ai.kbe.bean.SongList;
import de.htw.ai.kbe.bean.User;
import de.htw.ai.kbe.database.interfaces.ISongList;

public class SongListDAO implements ISongList {

	@Inject
	private EntityManagerFactory emf;

	@SuppressWarnings("unchecked")
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
			System.out.println("Retrieved this songlist from DB: " + sl);
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
	public void saveSongList(SongList list) throws PersistenceException{
		System.out.println("SongListDAO - saveSongList...");
		EntityManager em = emf.createEntityManager();
		EntityTransaction transaction = em.getTransaction();
		// ===== DEBUG ====== // 
		//If these infos are displayed, payload has been correctly transferred :-)
		User owner = list.getOwner();
		System.out.println("List for user: " + owner);
		List<Song> songInList = list.getSongs();
		System.out.println("Song in the list: " + owner);
		songInList.stream().forEach(s -> System.out.println(s));
		System.out.println("List infos: ");
		System.out.println(list.getId());
		System.out.println(list.isPublic());
		//  === END DEBUG ==== //
		try {
			System.out.println("Begin transaction...");
			transaction.begin();
			System.out.println("Trying to persist entity...");
			//====== BELEG 4 --- Probleme hier:
			//TODO: Post haengt hier! geht nicht weiter. Folgender Aufruf liefert 500!
			//XXX: In Eclipse: MessageBodyWriter not found for media type=text/plain, type=class javax.ws.rs.core.Response$Status, genericType=class javax.ws.rs.core.Response$Status.
			em.persist(list);
			
			//Hier wird das nicht ausgefuehrt
			System.out.println("Commit transaction...");
			transaction.commit();
			System.out.println("Transaction completed...");
		} catch (Exception e) {
			transaction.rollback();
			throw new PersistenceException("Could not persist entity: " + e.getCause().getMessage());
		} finally {
			em.close();
}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<SongList> findSongListByAccessType(User user, boolean isPublic) {
		//OLD METHOD not used
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
