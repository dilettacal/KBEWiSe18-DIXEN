package de.htw.ai.kbe.services;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import de.htw.ai.kbe.bean.User;
import de.htw.ai.kbe.filter.AuthenticationFilter;
import de.htw.ai.kbe.storage.IUser;

//URL fuer diesen Service ist: http://localhost:8080/contactsJAXRS/rest/auth?userId=schueler --> ghahoeho4
@Path("/auth")
public class AuthWebService {

	// Referenz auf InMemory-DB
	private IUser userStorage;
	
	private AuthenticationFilter filter = AuthenticationFilter.getInstance();

	// Konstruktor bekommt Verweis auf DB-Instanz
	@Inject
	public AuthWebService(IUser userStorage) {
		this.userStorage = userStorage;
	}
	
	@GET
	@Path("/{userId}")
	public Response getUser(@PathParam("userId") String userID) {
		User userExists = userStorage.getUserByUsername(userID);
		if(userExists != null) {
			//TODO: Generate token here !!!!!!!!!
			//String key = filter.filter(containerRequest);
			return Response.status(Response.Status.OK).entity("Authorized").build();
		} else {
			return Response.status(Response.Status.UNAUTHORIZED).entity("No existing user found").build();
		}
	}

}
