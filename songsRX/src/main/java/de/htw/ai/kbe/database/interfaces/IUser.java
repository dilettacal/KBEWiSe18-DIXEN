package de.htw.ai.kbe.database.interfaces;

import java.util.Collection;

import de.htw.ai.kbe.bean.User;

public interface IUser {
	
	public User getUser(String userID);
	public Collection<User> getAllUsers();
	//public boolean setAuthetificationKey();

}
