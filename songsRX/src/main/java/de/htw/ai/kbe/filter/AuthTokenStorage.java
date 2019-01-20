package de.htw.ai.kbe.filter;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.htw.ai.kbe.bean.User;
import de.htw.ai.kbe.database.interfaces.IUser;

/**
 * InMemory Tokenverwaltung 
 * (Beleg 3 und Tests)
 *
 */
public class AuthTokenStorage implements IAuth {

	// Neuer Ansatz:

	@Inject
	private IUser userStorage;
	private static Map<String, String> userToken;

	public AuthTokenStorage() {
		userToken = new ConcurrentHashMap<String, String>(); // Token,userID
	}

	@Override
	public synchronized String authenticate(String userId) {
		User user = null;
		user = userStorage.getUserByStringID(userId);
		// userStorage.getAllUsers().forEach(u -> System.out.println(u)); //Test -
		// enthaelt nur 2 User
		if (user != null) {
			String token = generateToken();
			userToken.put(userId, token);
			System.out.println(userToken.get(userId)); // Als Test, dass Token mit entsprechendem userId gespeichert
														// wurde
			return token;
		}
		return null;
	}

	@Override
	public String getUserIdFromToken(String token) {
		for (String key : userToken.keySet()) {
			if (userToken.get(key).equals(token)) {
				return key;
			}
		}
		return null;

	}

	private String generateToken() {
		System.out.println("Token generation for local user...");
		String key = UUID.randomUUID().toString();
		key = key.replaceAll("-", "");
		return key;
	}

}
