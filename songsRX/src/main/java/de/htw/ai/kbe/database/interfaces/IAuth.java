package de.htw.ai.kbe.database.interfaces;

import java.util.List;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;

import de.htw.ai.kbe.bean.Token;
import de.htw.ai.kbe.bean.User;

public interface IAuth {
	//Adapted methods from beleg3 
	//generiert anhand der userId einen Token
	public String authenticate(String userId) throws NotAuthorizedException;
	
	//prueft ob Token gueltig ist
	public boolean isValid(String token) throws NotAuthorizedException;
	
	//New methods for Beleg 4
	
	//This method finds the token corresponding to the user
	public Token findTokenByUser(User user) throws NotFoundException;
	
	public User findUserByToken(String token);
	
	//From token string 
	public Token findToken(String token);
	
	//THis method persists the token in the DB
	public void saveToken(Token token);
	
	//This method updates an existing token in the DB
	public void updateToken(Token token);
	
	public List<Token> getAll();
	
	public String verify (String token);
	
	
	

}
