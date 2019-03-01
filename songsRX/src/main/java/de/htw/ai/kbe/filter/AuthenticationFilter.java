	package de.htw.ai.kbe.filter;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

@Provider
public class AuthenticationFilter implements ContainerRequestFilter {
	

private static final String AUTHENTICATION_HEADER = "Authorization";

	@Inject
	private IAuth authContainer;

	@Override
	public void filter(ContainerRequestContext containerRequest) throws WebApplicationException {
		System.out.println("Ich bin im Filter");
		
		String path = containerRequest.getUriInfo().getPath();
		if(path.contains("auth")) {
			return;
		}
		String authToken = containerRequest.getHeaderString(AUTHENTICATION_HEADER);
		System.out.println("Authorization Header: " + authToken);
		if(authToken == null) {
			//angezeigt im Postman als: The request has not been applied because it lacks valid authentication credentials for the target resource.
			throw new WebApplicationException(Status.UNAUTHORIZED);
		}
		String valid = authContainer.getUserIdFromToken(authToken);
		System.out.println("UserID: " + valid);
		
		if(valid==null) {
			//angezeigt im Postman als: The request has not been applied because it lacks valid authentication credentials for the target resource.
			throw new WebApplicationException(Status.UNAUTHORIZED);
		}
	}
	
}
