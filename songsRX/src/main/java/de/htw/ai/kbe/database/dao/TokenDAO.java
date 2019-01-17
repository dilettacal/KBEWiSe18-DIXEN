package de.htw.ai.kbe.database.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.ws.rs.NotFoundException;

import javax.persistence.Query;

import de.htw.ai.kbe.bean.Token;
import de.htw.ai.kbe.bean.User;
import de.htw.ai.kbe.database.interfaces.IAuth;

public class TokenDAO implements IAuth{

	@Inject
	private EntityManagerFactory emf;
	

	// ==== Beleg 4

	@Override
	public Token findTokenByUser(User user) throws NotFoundException {
		EntityManager em = emf.createEntityManager();
		try {
			Query q = em.createQuery("SELECT t FROM Token t WHERE t.user = :user");
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
	
	
	// Beleg 3 ====
	@Override
	public String authenticate(String userId) {
		return "";
	}



	@Override
	public boolean isValid(String token) {
		EntityManager em = emf.createEntityManager();
		try {
			Token q = em.find(Token.class, token);
			return (q != null)? true: false;
		} finally {
			em.close();
		}
	}
}
