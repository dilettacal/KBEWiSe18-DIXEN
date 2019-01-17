	package de.htw.ai.kbe.filter;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import de.htw.ai.kbe.database.interfaces.IAuth;

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
		System.out.println(authToken);
		if(authToken == null) {
			//angezeigt im Postman als: The request has not been applied because it lacks valid authentication credentials for the target resource.
			throw new WebApplicationException(Status.UNAUTHORIZED);
		}
		boolean valid = authContainer.isValid(authToken);
		System.out.println("Valid: " + valid);
		
		if(!valid) {
			//angezeigt im Postman als: The request has not been applied because it lacks valid authentication credentials for the target resource.
			throw new WebApplicationException(Status.UNAUTHORIZED);
		}
	}
	
}
