package de.htw.ai.kbe.filter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AuthTokenStorage implements IAuth {

	private static Map<String,String> storage = new ConcurrentHashMap<String,String>(); //Key,userID
	

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
