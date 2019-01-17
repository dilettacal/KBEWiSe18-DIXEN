package de.htw.ai.kbe.database.interfaces;

import javax.ws.rs.NotFoundException;

import de.htw.ai.kbe.bean.Token;
import de.htw.ai.kbe.bean.User;

public interface IAuth {
	//Neuer Ansatz:
	
	//generiert anhand der userId einen Token
	public String authenticate(String userId);
	
	//prueft ob Token gueltig ist
	public boolean isValid(String token);
	
	//Beleg 4
	
	//This method finds the token corresponding to the user
	public Token findTokenByUser(User user) throws NotFoundException;
	
	//THis method persists the token in the DB
	public void saveToken(Token token);
	
	//This method updates an existing token in the DB
	public void updateToken(Token token);

}
