package de.htw.ai.kbe.filter;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.htw.ai.kbe.bean.User;
import de.htw.ai.kbe.storage.UserStorageForTesting;

public class AuthStorageForTesting implements IAuth{
	
	private static AuthStorageForTesting instance;
	private Map<String, String> miniAuthTokenStorage;
	public static final String TOKEN1 = "123abcd";
	public static final String TOKEN2 = "456efgh";
	
	
	AuthStorageForTesting() {
		miniAuthTokenStorage = new HashMap<>();
	}
	

	public static AuthStorageForTesting getInstance() {
        if(instance==null) {
        	instance = new AuthStorageForTesting();
        }
        return instance;
    }

	@Override
	public String authenticate(String userId) {
		
		switch(userId){
		case UserStorageForTesting.USERID1:
			miniAuthTokenStorage.put(UserStorageForTesting.USERID1, TOKEN1);
			return TOKEN1;
		case UserStorageForTesting.USERID2:
			miniAuthTokenStorage.put(UserStorageForTesting.USERID2,TOKEN2);
			return TOKEN2;
		default:
			return "TOKEN";		
		}
	}

	@Override
	public boolean isValid(String token) {
		return miniAuthTokenStorage.containsValue(token);
	}
	
	
	

	
}
