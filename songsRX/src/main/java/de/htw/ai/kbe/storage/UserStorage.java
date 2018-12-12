package de.htw.ai.kbe.storage;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import de.htw.ai.kbe.bean.User;

public class UserStorage implements IUser{
	
	public static Map<Integer, User> storage;
	
	public UserStorage() {
		storage = new ConcurrentHashMap<Integer, User>();
		initUserDB();
	}

	private void initUserDB() {
		User user1 = new User.Builder("mmuster").firstName("Maxime").lastName("Muster").build();
		user1.setId(1);
		storage.put(user1.getId(), user1);
		User user2 = new User.Builder("eschuler").firstName("Elena").lastName("Schuler").build();
		user2.setId(2);
		storage.put(user2.getId(), user2);
		
	}

	@Override
	public User getUser(String userID) {
		return storage.get(userID);
	}

	@Override
	public Collection<User> getAllUsers() {
		return storage.values();
	}

	public static Map<Integer, User> getStorage() {
		return storage;
	}

	@Override
	public User getUserByUsername(String userId) {
		Collection<User> allUsers = getAllUsers();
		for (User u: allUsers) {
			if(u.getUserID().equals(userId)) {
				return u;
			} 
		}
		return null;
	}

	
	

}
