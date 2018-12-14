package de.htw.ai.kbe.services;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.htw.ai.kbe.bean.Song;
import de.htw.ai.kbe.filter.AuthStorageForTesting;
import de.htw.ai.kbe.storage.ISongs;
import de.htw.ai.kbe.storage.SongStorageForTesting;
import de.htw.ai.kbe.storage.SongsStorage;

import static org.junit.Assert.assertEquals;

import javax.inject.Singleton;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class SongsWebServiceTest extends JerseyTest {

	@Override
	protected Application configure() {
		return new ResourceConfig(SongsWebService.class).register(new AbstractBinder() {
			@Override
			protected void configure() {
				bind(SongsStorage.class).to(ISongs.class).in(Singleton.class);

			}
		});
	}
	

    @Test
public void updateSongWithNonExistingIDShouldReturn404 () {
	Song testSong = new Song();
	testSong.setId(14);
	testSong.setArtist("Test");
	testSong.setTitle("TestTitle");
	testSong.setAlbum("TestAlbum");
	testSong.setReleased(2015);
	Response response = target("/songs/14").request().header("authorization", "testToken").put(Entity.json(testSong));
	System.out.println("Update with no provided ID: " + response.getStatus());
	Assert.assertEquals(404, response.getStatus());
}

    @Test 
	public void updateSongWithExistingIdShouldReturn204XML () {
    	//FIXME: 500 internal server error
    	Song testSong = new Song();
    	testSong.setId(14);
    	testSong.setArtist("Test");
    	testSong.setTitle("TestTitle");
    	testSong.setAlbum("TestAlbum");
    	testSong.setReleased(2015);
		Response response = target("/songs/10").request().header("authorization", "testToken").put(Entity.xml(testSong));
		System.out.println("Update with matching ID: " + response.getStatus());
		Assert.assertEquals(204, response.getStatus());
	}
    
    @Test
	public void updateSongWithNonMatchingIdShouldReturn400JSON () {
    	Song testSong = new Song();
    	testSong.setId(1);
    	testSong.setArtist("Test");
    	testSong.setTitle("TestTitle");
    	testSong.setAlbum("TestAlbum");
    	testSong.setReleased(2015);
		Response response = target("/songs/14").request().header("authorization", "testToken").put(Entity.json(testSong));
		System.out.println("Update with non matching ID: " + response.getStatus());
		Assert.assertEquals(400, response.getStatus());
	}	
    
    @Test
 	public void updateSongWithNonMatchingIdShouldReturn400XML() {
    	//FIXME: 500 Internal server error
     	Song testSong = new Song();
     	testSong.setId(1);
     	testSong.setArtist("Test");
     	testSong.setTitle("TestTitle");
     	testSong.setAlbum("TestAlbum");
     	testSong.setReleased(2015);
 		Response response = target("/songs/14").request().header("authorization", "testToken").put(Entity.xml(testSong));
 		System.out.println("Update with non matching ID: " + response.getStatus());
 		Assert.assertEquals(400, response.getStatus());
 	}	
    
	@Test
	public void updateSongValidJSONPayloadShouldReturn204() {
		Song song = new Song.Builder("Ein neuer Titel JSON").artist("Ein neuer Artist JSON").album("JSON Album")
				.released(2005).id(1).build();

		Response output = target("songs/" + song.getId()).request().header("authorization", "testToken")
				.put(Entity.json(song));
		assertEquals("Should return status 204", 204, output.getStatus());
	}

	@Test
	public void updateSongValidXMLPayloadShouldReturn204() {
		//FIXME: 500 Internal server error
		Song song = new Song.Builder("Ein neuer Titel XML").artist("Ein neuer Artist XML").album("XML Album")
				.released(2005).id(1).build();

		Response output = target("songs/" + song.getId()).request().header("authorization", "testToken").
				put(Entity.xml(song));
		assertEquals("Song" + song.getId() + " succesfully updated", 204, output.getStatus());
	}

	@Test
	public void updateSongJsonWithUnknownIDShouldReturn() { //TODO: Should return?
		//FIXME: SocketException???
		Song song = new Song.Builder("Ein neuer Titel JSON").artist("Ein neuer Artist JSON").album("JSON Album")
				.released(2005).build();

		Response output = target("songs/" + song.getId()).request().header("authorization", "testToken")
				.put(Entity.json(song));
		assertEquals("Should return status 404", 404, output.getStatus());
	}

	@Test
	public void updateSongXMLWithUnknownIDShouldReturn() { //TODO: Should return?
		Song song = new Song.Builder("Ein neuer Titel XML").artist("Ein neuer Artist XML").album("XML Album")
				.released(2005).build();

		Response output = target("songs/" + song.getId()).request().header("authorization", "testToken").put(Entity.xml(song));
		assertEquals("Should return status 404", 404, output.getStatus()); //FIXME: 500 Error XML!
	}

	@Test
	public void updateSongwithoutIDShouldReturn404() {
		Song song = new Song();
		song.setArtist("Ein Title");
		song.setAlbum("Ein Album");
		song.setReleased(1900);
		Response output = target("songs/5").request().header("authorization", "testToken").put(Entity.json(song));
		assertEquals(404, output.getStatus()); //FIXME: 500
	}

	@Test
	public void updateSongwithDifferentIdTargetSongShouldReturn400() {
		Song song = new Song.Builder("Ein neuer Titel").artist("Ein neuer Artist").album("Album")
				.released(2005).id(5).build();

		Response output = target("songs/9").request().put(Entity.json(song));
		assertEquals(400, output.getStatus());
	}

}
