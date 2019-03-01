package de.htw.ai.kbe.services;

import static org.junit.Assert.*;

import javax.inject.Singleton;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;

import de.htw.ai.kbe.database.interfaces.ISongs;
import de.htw.ai.kbe.database.interfaces.IUser;
import de.htw.ai.kbe.filter.AuthTokenStorage;
import de.htw.ai.kbe.filter.IAuth;
import de.htw.ai.kbe.storage.SongsStorage;
import de.htw.ai.kbe.storage.UserStorage;

/**
 * Test class for AuthWebService (Beleg 3)
 * Uses InMemory-DBs from Assignment 3
 * @author dc
 *
 */
public class AuthWebServiceTest extends JerseyTest {

	//Works on linux
	
	@Override
	protected Application configure() {
		return new ResourceConfig(AuthWebService.class).register(new AbstractBinder() {
			@Override
			protected void configure() {
				bind(SongsStorage.class).to(ISongs.class).in(Singleton.class);
				bind(UserStorage.class).to(IUser.class); // .in(Singleton.class);
				bind(AuthTokenStorage.class).to(IAuth.class).in(Singleton.class);

			}
		});
	}

	// ==== AUTH GET WITH VALID USERID ==== //
	//rest/auth?userId=mmuster --> 200 and token
	@Test
	public void getWithvalidUserIdToAuthShouldReturn200AndAToken() {
		Response response = target("/auth").queryParam("userId", "mmuster").request().get();
		System.out.println(response.getStatus());
		Assert.assertEquals(200, response.getStatus());
		String token = response.readEntity(String.class);
		assertNotNull(token);
		System.out.println(token);
	}

	
	// === AUTH GET WITH NOT VALID USERID ===
	// not existing userId --> Unauthorized
	@Test
	public void getWithNoValidUserIdToAuthShouldReturn401UnAuthorized() {
		Response response = target("/auth").queryParam("userId", "muster").request().get();
		String resp = response.readEntity(String.class);
		assertEquals(401, response.getStatus());		
        assertTrue(resp.contains("Unauthorized"));		
	}

	//emtpy userId --> Bad Request
	@Test
	public void getWithEmptyUserIdToAuthShouldReturn400BadRequest() {
		Response response = target("/auth").queryParam("userId", "").request().get();
		System.out.println(response.getStatus());
		Assert.assertEquals(400, response.getStatus());
		String token = response.readEntity(String.class);
        assertTrue(token == null || token.isEmpty());		
	}

	//request without params --
	@Test
	public void NoUserIdToAuthShouldReturn400BadRequest() {
		// Request ohne Parameter: rest/auth
		Response response = target("/auth").request().get();
		//userId is null
		System.out.println(response.getStatus());
		Assert.assertEquals(400, response.getStatus());
	}

	

}
