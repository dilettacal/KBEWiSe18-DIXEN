package de.htw.ai.kbe.filter;

import de.htw.ai.kbe.bean.User;

public interface IAuth {
	/**
	 * 
	 * @param authToken
	 * @return true when authToken in tokenMap
	 */
	public boolean authenticate(String authToken);
	
	/**
	 * 
	 * @param userID
	 * @return userId given token
	 */
	public String getUserIdByToken(String userID);
	
	/**
	 * put token in tokenMap
	 * @param token
	 * @param userId
	 * @return
	 */
	public String setUserIdByToken(String token,String userId);
	
	
	public boolean containsVal(String userId);

	

}
