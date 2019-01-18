package de.htw.ai.kbe.filter;

import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;

import de.htw.ai.kbe.bean.Token;
import de.htw.ai.kbe.bean.User;
import de.htw.ai.kbe.database.interfaces.IToken;
import de.htw.ai.kbe.database.interfaces.IUser;

public class AuthTokenDB implements IAuth {

	@Inject
	private IUser userPersistence;

	@Inject
	private IToken tokenPersistence;



	private String generateToken() {
		String key = UUID.randomUUID().toString();
		key = key.replaceAll("-", "");
		return key;
	}


	@Override
	public String authenticate(String userId) {
		  User user;
	        try {
	            user = userPersistence.getUserByStringID(userId);
	        } catch (NotFoundException e) {
	            throw new NotAuthorizedException("User does not exist");
	        }

	        Token t = tokenPersistence.findTokenByUser(user);
	        
	        String token = generateToken();

	        if(t != null) {
	            t.setToken(token);
	            tokenPersistence.updateToken(t);
	        } else {
	            t = new Token();
	            t.setToken(token);
	            t.setUser(user);
	            
	            tokenPersistence.saveToken(t);
	        }

	        return token;
	}



	@Override
	public String getUserIdFromToken(String token) {
		Token t = tokenPersistence.findTokenByTokenString(token);
        if(t == null || t.getUser() == null) {
            throw new NotAuthorizedException("Token invalid");
        }
        return t.getUser().getId();
	}

}
