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
	private final String AUTHENTICATION_HEADER = "Authentication";

	//Konstruktor INjection hat nicht funktioniert -> Field Injection
	@Inject
	private IAuth tokenStorage;
	
	public AuthWebService() {
		
	}
	
	@GET
	@Path("/")
	@Produces({ MediaType.TEXT_PLAIN })
	public Response getToken(@QueryParam("userId") String userID) {
		System.out.println("USERID: " + userID); //liefert eschuler
		System.out.println(userID.trim()); //liefert eschuler
		if(userID == null || (userID.isEmpty())) {
			return Response.status(Response.Status.BAD_REQUEST).entity(
					Response.Status.BAD_REQUEST + ": No username given!")
					.build();
		}
		
		String token = tokenStorage.authenticate(userID);
		if(token == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity(
					Response.Status.UNAUTHORIZED + ": Unauthorized user!")
					.build();
		}
		else {
			//FIXME: Anfragen mit curl zeigen keinen Token an
			return Response.status(Response.Status.OK).header(AUTHENTICATION_HEADER, token).entity(token).build();
		}

	}

}
