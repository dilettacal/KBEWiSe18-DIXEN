package de.htw.ai.kbe.filter;

import java.io.IOException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

@Provider
public class AuthenticationFilter implements ContainerRequestFilter {
	
	private static final String AUTHENTICATION_HEADER = "Authorization";
	// better injected
	private IAuth authenticationService = new AuthTokenStorage();

	@Override
	public void filter(ContainerRequestContext containerRequest) throws IOException {

		String authCredentials = containerRequest.getHeaderString(AUTHENTICATION_HEADER);
		String userID = null;

		boolean authenticationStatus = authenticationService.authenticate(userID, authCredentials);
		//String user = authenticationService.getUserIdByToken(authCredentials);		

		if (!authenticationStatus) {
			throw new WebApplicationException(Status.UNAUTHORIZED);
		}

	}

}
