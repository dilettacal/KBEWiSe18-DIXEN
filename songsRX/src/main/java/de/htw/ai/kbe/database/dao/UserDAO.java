package de.htw.ai.kbe.database.dao;

import java.util.Collection;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.NotFoundException;


import de.htw.ai.kbe.bean.User;
import de.htw.ai.kbe.database.interfaces.IUser;

public class UserDAO implements IUser{

	@Inject
	private EntityManagerFactory emf;
	
	@Override
	public User getUserByStringID(String userID) {
		EntityManager em = emf.createEntityManager();
		try {
			User u = em.find(User.class, userID);
			if (u == null) {
				throw new NotFoundException("No such user '" + userID + "'");
			}
			return u;
		} finally {
			em.close();
}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<User> getAllUsers() {
		EntityManager em = emf.createEntityManager();
		try {
			javax.persistence.Query query = em.createQuery("SELECT u FROM User u");
			return query.getResultList();
		} finally {
			em.close();
}
	}

}
