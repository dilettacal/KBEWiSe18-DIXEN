package de.htw.ai.kbe.filter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import de.htw.ai.kbe.bean.User;
import de.htw.ai.kbe.storage.ISongs;
import de.htw.ai.kbe.storage.IUser;
import de.htw.ai.kbe.storage.UserStorage;


public class AuthTokenStorage implements IAuth {
	
	private static Map<String, String> userTokenMapping; //String=userID, String=Token
	private static Map<Integer, User> storage;

	
	public AuthTokenStorage() {
		userTokenMapping = new ConcurrentHashMap<String, String>();		
		storage = UserStorage.getStorage();
	}

	@Override
	public synchronized boolean authenticate(String userID, String token) {
		//1. Existiert der User (ist in der Map) --> Token wird durch den neuen ersetzt
		//2. User ist identifiziert? 
		if(userExists(userID)) {
			userTokenMapping.put(userID, token);
			return true; 
		}
		else
			return false;
	}

	/*checks wether key exists*/
	@Override
	public synchronized boolean identify(String token) {
		if(userTokenMapping.containsValue(token))
			return true;
		else
			return false;
	}

	/*checks wether user is in userDB or not --> allows requests when he exists*/
	@Override
	public boolean userExists(String userId) {
		User user = ((IUser) storage).getUserByUsername(userId);
		if(user != null) {
			return true;
		} else 
			return false;
	}

	
}
