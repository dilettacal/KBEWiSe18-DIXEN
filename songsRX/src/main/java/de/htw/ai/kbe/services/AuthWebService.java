package de.htw.ai.kbe.services;

import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.htw.ai.kbe.bean.User;
import de.htw.ai.kbe.filter.AuthenticationFilter;
import de.htw.ai.kbe.filter.IAuth;
import de.htw.ai.kbe.storage.IUser;
import de.htw.ai.kbe.storage.UserStorage;

//URL fuer diesen Service ist: http://localhost:8080/songsRX/rest/auth?userId=schueler --> ghahoeho4
@Path("/auth")
public class AuthWebService {

	// Referenz auf InMemory-DB
	private IUser userStorage;
	private IAuth tokenStorage;

	// Konstruktor bekommt Verweis auf DB-Instanz
	@Inject
	public AuthWebService(IUser userStorage, IAuth tokenStorage) {
		this.userStorage = userStorage;
		this.tokenStorage = tokenStorage;
	}
	
	@GET
	@Path("/")
	@Produces({ MediaType.TEXT_PLAIN })
	public Response getToken(@QueryParam("userId") String userID) {
		User user = ((IUser) UserStorage.storage).getUser(userID);
		if (user != null) {
			if (tokenStorage.containsVal(userID)) {
				return Response.status(Response.Status.FOUND).entity(
						Response.Status.FORBIDDEN + ": You allready logged in, your token is: " + tokenStorage.getUserIdByToken(userID.toString()))
						.build();
			}
			String token = keyGenerator();
			System.out.println("Session token ist: "  +token);
			tokenStorage.setUserIdByToken(token, userID);
			return Response.status(Response.Status.OK).entity("Token: " + token).build();
		} else {
			return Response.status(Response.Status.FORBIDDEN)
					.entity(Response.Status.FORBIDDEN + ": Not Auth, No User found with id " + userID).build();
		}

	}
	
	//Alternativer Token: Session ID?
	private String keyGenerator() {
		String key = UUID.randomUUID().toString();
		key = key.replaceAll("-", "");
		return key;
	}

}
