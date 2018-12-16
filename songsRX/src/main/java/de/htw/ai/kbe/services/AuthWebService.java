package de.htw.ai.kbe.services;


import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.htw.ai.kbe.filter.IAuth;

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
	//@Path("/")
	@Produces({ MediaType.TEXT_PLAIN })
	public Response getToken(@QueryParam("userId") String userID) {
		System.out.println("USERID: " + userID); //liefert eschuler
		System.out.println(userID==null);
		if(userID == null || userID.trim().isEmpty() || userID.equals("")) {
			//Laesst sich besser testen
			throw new BadRequestException("No userId"); //400 and token is null
		}
		
		String token = tokenStorage.authenticate(userID);
		if(token == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity(
					Response.Status.UNAUTHORIZED + ": Unauthorized user!")
					.build();
		}
		else {
			return Response.status(Response.Status.OK).header(AUTHENTICATION_HEADER, token).entity(token).build();
		}

	}

}
