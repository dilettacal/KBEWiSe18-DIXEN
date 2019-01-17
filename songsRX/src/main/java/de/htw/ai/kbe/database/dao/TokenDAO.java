package de.htw.ai.kbe.database.dao;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.ws.rs.NotAuthorizedException;
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
		System.out.println("Persist token");
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
		System.out.println("Update token");
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
		System.out.println("DB Authentication");
		User u = new User();
		u.setId(userId);
		Token t = findTokenByUser(u);
		String token = generateToken();
		//userStorage.getAllUsers().forEach(u -> System.out.println(u)); //Test - enthaelt nur 2 User
		if(t == null) {			
			saveToken(t);
			
		} else {			
			updateToken(t);
		}
		return token;
		
	}



	@Override
	public boolean isValid(String token) {
		EntityManager em = emf.createEntityManager();
		try {
			Query q = em.createQuery("SELECT t FROM Token t WHERE t.token = :token");
			q.setParameter("token", token);
			Token t = (Token) q.getSingleResult();
			return (t != null)? true: false;
		} finally {
			em.close();
		}
	}


	@Override
	public Token findToken(String token) {
		EntityManager em = emf.createEntityManager();
		try {
			Query q = em.createQuery("SELECT t FROM Token t WHERE t.token = :token");
			q.setParameter("token", token);
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
			Query q = em.createQuery("SELECT t.user FROM Token t WHERE t.token = :token");
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
	
	
	private String generateToken() {
		String key = UUID.randomUUID().toString();
		key = key.replaceAll("-", "");
		return key;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Token> getAll() {
		EntityManager em = emf.createEntityManager();
		try {
			Query query = em.createQuery("SELECT t FROM Token t");
			return query.getResultList();
		} finally {
			em.close();
		}
	}

	@Override
	public String verify(String token) {
		Token t = findToken(token);
        if(t == null || t.getUser() == null) {
            throw new NotAuthorizedException("Token invalid");
        }
        return t.getUser().getId();
	}

}
