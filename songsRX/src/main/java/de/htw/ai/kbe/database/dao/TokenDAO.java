package de.htw.ai.kbe.database.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import de.htw.ai.kbe.bean.Token;
import de.htw.ai.kbe.bean.User;
import de.htw.ai.kbe.database.interfaces.IToken;

public class TokenDAO implements IToken {
	
	@Inject
	private EntityManagerFactory emf;


	@Override
	public Token findTokenByUser(User user) {
		EntityManager em = emf.createEntityManager();
		try {
			javax.persistence.Query q = em.createQuery("SELECT t FROM Token t WHERE t.user = :user");
			q.setParameter("user", user);
			try {
				return (Token) q.getSingleResult();
			} catch (NoResultException e) {
				return null;
			}
		} finally {
			em.close();
}
	}

	@Override
	public User findUserByToken(String token) {
		EntityManager em = emf.createEntityManager();
		try {
			javax.persistence.Query q = em.createQuery("SELECT t.user FROM Token t WHERE t.token = :token");
			q.setParameter("token", token);
			try {
				return (User) q.getSingleResult();
			} catch (NoResultException e) {
				return null;
			}
		} finally {
			em.close();
}
	}

	@Override
	public void saveToken(Token token) {
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			em.persist(token);
			em.getTransaction().commit();
		} finally {
			em.close();
}
	}

	@Override
	public void updateToken(Token token) {
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			em.merge(token);
			em.getTransaction().commit();
		} finally {
			em.close();
}
	}

}
