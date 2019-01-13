package de.htw.ai.kbe.filter;

import de.htw.ai.kbe.bean.User;

public interface IAuth {
	//Neuer Ansatz:
	
	//generiert anhand der userId einen Token
	public String authenticate(String userId);
	
	//prueft ob Token gueltig ist
	public boolean isValid(String token);

}
