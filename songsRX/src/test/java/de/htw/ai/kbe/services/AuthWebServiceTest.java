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

import de.htw.ai.kbe.filter.AuthTokenStorage;
import de.htw.ai.kbe.filter.IAuth;
import de.htw.ai.kbe.storage.ISongs;
import de.htw.ai.kbe.storage.SongsStorage;

public class AuthWebServiceTest extends JerseyTest {

	@Override
	protected Application configure() {
		return new ResourceConfig(AuthWebService.class).register(new AbstractBinder() {
			@Override
			protected void configure() {
				bind(AuthTokenStorage.class).to(IAuth.class);
				bind(SongsStorage.class).to(ISongs.class).in(Singleton.class);

			}
		});
	}

	@Test
	public void validUserIdToAuthShouldReturn200() {
		Response response = target("/auth").queryParam("userId", "mmuster").request().get();
		System.out.println("Valid UserId Request: " + response.getStatus());
		Assert.assertEquals(200, response.getStatus());
	}

	@Test
	public void NoValidUserIdToAuthShouldReturn403() {
		Response response = target("/auth").queryParam("userId", "muster").request().get();
		System.out.println("Non Valid UserId Request: " + response.getStatus());
		Assert.assertEquals(403, response.getStatus());
	}

	@Test
	public void NoUserIdToAuthShouldReturn403() {
		Response response = target("/auth").request().get();
		System.out.println("Non Valid UserId Request: " + response.getStatus());
		Assert.assertEquals(403, response.getStatus());
	}

	@Test
	public void testGetWithEmptyUserIdShouldReturnBadRequest() {
		Response response = target("/auth").queryParam("userId", "").request().get();

		assertEquals(400, response.getStatus());
		String token = response.readEntity(String.class);
		assertTrue(token == null || token.isEmpty());
	}

}
