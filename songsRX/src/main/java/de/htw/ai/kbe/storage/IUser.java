package de.htw.ai.kbe.storage;

import java.util.Collection;

import de.htw.ai.kbe.bean.User;

public interface IUser {
	
	public User getUser(Integer id);
	public Collection<User> getAllUsers();
	public User getUserByUsername(String userId);
	//public boolean setAuthetificationKey();

}
