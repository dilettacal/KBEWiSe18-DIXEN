package de.htw.ai.kbe.filter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.inject.Singleton;

public class AuthTokenStorage implements IAuth {

	private static Map<String,String> storage;
	
	public AuthTokenStorage() {
		storage = new ConcurrentHashMap<String,String>(); //Token,userID
	}

	@Override
	public boolean authenticate(String authKey) {
		if(storage.containsKey(authKey)) {
			return true;
		}
		return false;
	}
	
	@Override
	public String getUserIdByToken(String token) {
		return storage.get(token);
	}
	
	@Override
	public String setUserIdByToken(String token,String userId) {
		return storage.put(token,userId);
	}

	@Override
	public boolean containsVal(String userId) {		
		return storage.containsValue(userId);
	}

	

}
