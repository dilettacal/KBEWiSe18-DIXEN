package de.htw.ai.kbe.database.interfaces;

import de.htw.ai.kbe.bean.Token;
import de.htw.ai.kbe.bean.User;

public interface IToken {
	
	//Useful methods if token persisted in db
		public Token findTokenByUser(User user);
		public Token findTokenByString(String token);
		public void saveToken(Token token);
		public void updateToken(Token token);


}
