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
		if(user != null) {
			String token = generateToken();
			userToken.put(userId, token);
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

//	@Override
//	public boolean authenticate(String authKey) {
//		if(storage.containsKey(authKey)) {
//			return true;
//		}
//		return false;
//	}
//	
//	@Override
//	public String getUserIdByToken(String token) {
//		return storage.get(token);
//	}
//	
//	@Override
//	public String setUserIdByToken(String token,String userId) {
//		return storage.put(token,userId);
//	}
//
//	@Override
//	public boolean containsVal(String userId) {		
//		return storage.containsValue(userId);
//	}

	

}
