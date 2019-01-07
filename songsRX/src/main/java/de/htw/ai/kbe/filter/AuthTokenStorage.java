package de.htw.ai.kbe.filter;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.htw.ai.kbe.bean.User;
import de.htw.ai.kbe.storage.IUser;

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
		user = userStorage.getUser(userId);
		//userStorage.getAllUsers().forEach(u -> System.out.println(u)); //Test - enthaelt nur 2 User
		if(user != null) {
			String token = generateToken();
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

}
