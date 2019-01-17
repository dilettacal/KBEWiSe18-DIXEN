package de.htw.ai.kbe.oldStorage;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;
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
		User user = null;
		user = userStorage.getUserByUserId(userId);
		//userStorage.getAllUsers().forEach(u -> System.out.println(u)); //Test - enthaelt nur 2 User
		if(user != null) {
			String token = generateToken();
			System.out.println("Generated token: " + token);
			userToken.put(userId, token);
			System.out.println(userToken.get(userId)); //Als Test, dass Token mit entsprechendem userId gespeichert wurde
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



}
