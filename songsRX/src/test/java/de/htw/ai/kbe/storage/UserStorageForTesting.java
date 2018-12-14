package de.htw.ai.kbe.storage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.NotFoundException;

import de.htw.ai.kbe.bean.User;

@Deprecated
public class UserStorageForTesting implements IUser {
	
	private static UserStorageForTesting instance;
	private Map<Integer, User> miniUserDB;
	
	public final static String USERID1 = "programmer";
	public final static String USERID2 = "faultier";
	
	public static UserStorageForTesting getInstance() {
        if(instance==null) {
        	instance = new UserStorageForTesting();
        }
        return (UserStorageForTesting) instance;
    }
	
	 UserStorageForTesting() {
		miniUserDB = new HashMap<Integer,User>();
        initSomeTestUserInDB();
    }

	private void initSomeTestUserInDB() {
		User user1 = new User.Builder(USERID1).firstName("Uwe").lastName("Krupp").build();
		user1.setId(1);
		miniUserDB.put(user1.getId(), user1);
		User user2 = new User.Builder(USERID2).firstName("Faultier").lastName("Faulenzen").build();
		user2.setId(2);
		miniUserDB.put(user2.getId(), user2);
		
	}

	@Override
	public User getUser(String userID) {
		 User user = miniUserDB.get(userID);
	     if(user == null) throw new NotFoundException("user not found " + userID);
	     return user;
	}

	@Override
	public Collection<User> getAllUsers() {
		return miniUserDB.values();
	}

}
