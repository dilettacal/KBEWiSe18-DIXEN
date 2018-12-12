package de.htw.ai.kbe.filter;

import de.htw.ai.kbe.bean.User;

public interface IAuth {
	
	public boolean authenticate(String userID, String token);
	
	public boolean identify(String token);
	
	public boolean userExists(String userId);
	

}
