	package de.htw.ai.kbe.filter;

import java.io.IOException;
import java.util.UUID;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

@Provider
public class AuthenticationFilter implements ContainerResponseFilter {
	
	private static final String AUTHENTICATION_HEADER = "Authorization";
	// better injected
	private IAuth authenticationService = new AuthTokenStorage();
	
	private static AuthenticationFilter filter = new AuthenticationFilter();
	
	private String userID = null;
	
	
	public AuthenticationFilter() {			
	}
	
	public static AuthenticationFilter getInstance() {
		return filter;
	}
	
	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException{

		String authKey = requestContext.getHeaderString(AUTHENTICATION_HEADER);
		
		if(authKey == null || authKey.isEmpty())
			authKey = keyGenerator();

		boolean authenticationStatus = authenticationService.authenticate(this.userID, authKey);
		//String user = authenticationService.getUserIdByToken(authCredentials);		

		if (!authenticationStatus) {
			responseContext.setStatusInfo(Status.UNAUTHORIZED);
		}
		else {
			responseContext.getHeaders().add(AUTHENTICATION_HEADER, authKey);
		}

	}
	
	private String keyGenerator() {
		String key = UUID.randomUUID().toString();
		key = key.replaceAll("-", "");
		return key;
	}

}
