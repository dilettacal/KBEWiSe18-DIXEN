package de.htw.ai.kbe.filter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;

import de.htw.ai.kbe.bean.Token;
import de.htw.ai.kbe.bean.User;
import de.htw.ai.kbe.database.interfaces.IAuth;
import de.htw.ai.kbe.database.interfaces.IUser;

public class AuthTokenStorage implements IAuth {

	//Neuer Ansatz:
	
	@Inject
	private IUser userStorage;
	private static Map<String,String> userToken;
	
	public AuthTokenStorage() {
		userToken = new ConcurrentHashMap<String,String>(); //Token,userID
	}

	@Override
	public synchronized String authenticate(String userId) {
		System.out.println("Local authentication");
		User user = null;
		user = userStorage.getUserByUserId(userId);
		//userStorage.getAllUsers().forEach(u -> System.out.println(u)); //Test - enthaelt nur 2 User
		if(user != null) {
			String token = generateToken();
			System.out.println("Locally generated token: " + token);
			userToken.put(userId, token);
			//System.out.println(userToken.get(userId)); //Als Test, dass Token mit entsprechendem userId gespeichert wurde
			return token;
		}
		return null;
	}

	@Override
	public boolean isValid(String token) {
		return userToken.containsValue(token);
	}
	
	private String generateToken() {
		String key = UUID.randomUUID().toString();
		key = key.replaceAll("-", "");
		return key;
	}

	@Override
	public Token findTokenByUser(User user) throws NotFoundException {
		throw new NotFoundException();
	}

	@Override
	public void saveToken(Token token) {
		return;
	}

	@Override
	public void updateToken(Token token) {
		return;		
	}

	@Override
	public User findUserByToken(String token) {
		String userId = userToken.get(token);
		User u = new User();
		u.setId(userId);
		return u;
	}

	@Override
	public Token findToken(String token) {
		return null;
	}

	@Override
	public List<Token> getAll() {
		List<Token> allToks = Collections.emptyList();
		allToks = userToken.values().stream().map(val -> {
			Token t = new Token();
			t.setToken(val);
			return t;
		}).collect(Collectors.toList());
		return allToks;
	}

	@Override
	public String verify(String token) throws NotAuthorizedException {
		return null;
	}



}
