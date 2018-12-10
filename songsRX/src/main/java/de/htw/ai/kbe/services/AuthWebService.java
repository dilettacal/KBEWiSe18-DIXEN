package de.htw.ai.kbe.services;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import de.htw.ai.kbe.storage.IUser;

//URL fuer diesen Service ist: http://localhost:8080/contactsJAXRS/rest/auth 
@Path("/auth")
public class AuthWebService {

	// Referenz auf InMemory-DB
	private IUser userStorage;

	// Konstruktor bekommt Verweis auf DB-Instanz
	@Inject
	public AuthWebService(IUser userStorage) {
		this.userStorage = userStorage;
	}
	
	@GET
	@Path("/{userId}")
	public Response getUser(@PathParam("userId") String userID) {
		return null;
	}

}
