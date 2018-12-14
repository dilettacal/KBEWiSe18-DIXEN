package de.htw.ai.kbe.services;

import static org.junit.Assert.*;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import de.htw.ai.kbe.filter.AuthStorageForTesting;
import de.htw.ai.kbe.filter.AuthTokenStorage;
import de.htw.ai.kbe.filter.IAuth;
import de.htw.ai.kbe.storage.IUser;
import de.htw.ai.kbe.storage.UserStorageForTesting;

public class AuthWebServiceTest extends JerseyTest {
	
	private final String AUTH = "/auth/";

	@Override
    protected Application configure() {
        return new ResourceConfig(AuthWebService.class).register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(AuthStorageForTesting.getInstance()).to(IAuth.class);
            }
        });
    }

	@Test
    public void testGetWithExistingUserShouldReturnToken() {
        Response response = target(AUTH).queryParam("userId", "programmer").request().get();
        
        assertEquals(200, response.getStatus());
        String token = response.readEntity(String.class);
        assertNotNull(token);
        System.out.println(token);        
    }

    @Test
    public void testGetWithNonExistingUserShouldReturnUnauthorized() {
        Response response = target(AUTH).queryParam("userId", "argh").request().get();
        
        assertEquals(401, response.getStatus());
        String token = response.readEntity(String.class);
        assertTrue(token == null || token.isEmpty());
    }

    @Test
    public void testGetWithoutUserIdShouldReturnBadRequest() {
        Response response = target(AUTH).request().get();
        
        assertEquals(400, response.getStatus());
        String token = response.readEntity(String.class);
        assertTrue(token == null || token.isEmpty());
    }
    
    @Test
    public void testGetWithEmptyUserIdShouldReturnBadRequest() {
        Response response = target(AUTH).queryParam("userId", "").request().get();
        
        assertEquals(400, response.getStatus());
        String token = response.readEntity(String.class);
        assertTrue(token == null || token.isEmpty());
    }

}
