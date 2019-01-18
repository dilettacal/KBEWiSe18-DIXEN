package de.htw.ai.kbe.database.interfaces;

import java.util.Collection;

import de.htw.ai.kbe.bean.User;

public interface IUser {

	public User getUserByStringID(String userID);
	public Collection<User> getAllUsers();
}
