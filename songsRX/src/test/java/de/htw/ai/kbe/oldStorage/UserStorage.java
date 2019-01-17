package de.htw.ai.kbe.oldStorage;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import de.htw.ai.kbe.bean.User;
import de.htw.ai.kbe.database.interfaces.IUser;

public class UserStorage implements IUser{
	
	public static Map<Integer, User> storage;
	private static UserStorage instance = null;
	
	public UserStorage() {
		storage = new ConcurrentHashMap<Integer, User>();
		initUserDB();
	}

	private void initUserDB() {
		User user1 = new User.Builder("mmuster").firstName("Maxime").lastName("Muster").build();
		user1.setLocalID(1);
		storage.put(user1.getLocalID(), user1);
		User user2 = new User.Builder("eschuler").firstName("Elena").lastName("Schuler").build();
		user2.setLocalID(2);
		storage.put(user1.getLocalID(), user2);
		
	}
	
	public static UserStorage getInstance() {
		if(instance == null)
			instance = new UserStorage();
		return instance;
	}

	@Override
	public User getUser(String userID) {
		for(User u: storage.values()) {
			if(u.getId().equals(userID))
				return u;
		}
		return null;
	}

	@Override
	public Collection<User> getAllUsers() {
		return storage.values();
	}

	public static Map<Integer, User> getStorage() {
		return storage;
	}

}
