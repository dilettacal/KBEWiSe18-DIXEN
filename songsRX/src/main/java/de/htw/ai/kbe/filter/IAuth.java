package de.htw.ai.kbe.filter;

import de.htw.ai.kbe.bean.User;

public interface IAuth {
	//Neuer Ansatz:
	
	//generiert anhand der userId einen Token
	public String authenticate(String userId);
	
	//prueft ob Token gueltig ist
	public boolean isValid(String token);
	
	
	/**
	 * 
	 * @param authToken
	 * @return true when authToken in tokenMap
	 */
	//public boolean authenticate(String authToken);
	
	/**
	 * 
	 * @param userID
	 * @return userId given token
	 */
	//public String getUserIdByToken(String userID);
	
	/**
	 * put token in tokenMap
	 * @param token
	 * @param userId
	 * @return
	 */
//	public String setUserIdByToken(String token,String userId);
//	
//	
//	public boolean containsVal(String userId);

	

}
