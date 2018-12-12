	package de.htw.ai.kbe.filter;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

@Provider
public class AuthenticationFilter implements ContainerRequestFilter {
	

public static final String AUTHENTICATION_HEADER = "Authorization";

	
	private IAuth authContainer;
	
	@Inject
	public AuthenticationFilter(IAuth authContainer) {
		this.authContainer = authContainer;
	}

	@Override
	public void filter(ContainerRequestContext containerRequest) throws WebApplicationException {

		String authToken = containerRequest.getHeaderString(AUTHENTICATION_HEADER);

		if (authToken == null) {
			// etwas zu einfach: wenn "auth" in der URL, dann durchlassen:
			if (!containerRequest.getUriInfo().getPath().contains("auth")) { //kein "auth"
				System.out.print("token ist null bei filter");
				throw new WebApplicationException(Status.UNAUTHORIZED);
			}
		} else {
			if (!authContainer.authenticate(authToken)) { // Service kennt den Token nicht
				System.out.print("token ist unathorized bei filter");
				throw new WebApplicationException(Status.UNAUTHORIZED);
			}
		}
	}
}
