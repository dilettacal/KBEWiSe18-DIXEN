package de.htw.ai.kbe.services;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.htw.ai.kbe.bean.Song;
import de.htw.ai.kbe.storage.ISongs;
import de.htw.ai.kbe.storage.SongsStorage;

import static org.junit.Assert.assertEquals;

import javax.inject.Singleton;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class SongsWebServiceTest extends JerseyTest {

	// DATEN

	private Song onlyTitle;
	private Song songNoTitle;
	private Song validPOSTSong;
	private Song validPUTSong;

	@Override
	protected Application configure() {
		return new ResourceConfig(SongsWebService.class)
				.register(new AbstractBinder() {
			@Override
			protected void configure() {
				bind(SongsStorage.class).to(ISongs.class).in(Singleton.class);
			}
		}).packages("de.htw.ai.kbe.services");
	}

	@Before
	public void setUp() throws Exception {
		onlyTitle = new Song();
		onlyTitle.setTitle("Ein Titel");
		

		songNoTitle = new Song();
		songNoTitle.setId(1);
		songNoTitle.setTitle(null);
		songNoTitle.setAlbum("Ein Album");
		songNoTitle.setArtist("Ein Artist");

		validPUTSong = new Song();
		validPUTSong.setTitle("Ein PUT Titel");
		validPUTSong.setAlbum("Ein PUT Album");
		validPUTSong.setArtist("Ein PUT Artist");
		validPUTSong.setReleased(1900);
	}

	// ============= PUT Tests ================= //

	// VALID PAYLOAD

	@Test
	public void updateSongWithValidJSONPayloadShouldReturn204AndUpdateSongWithPutSong() {
		// JSON changes ID 0
		validPUTSong.setId(0);
		String path = "rest/songs/0";
		
		//Song vor dem PUT
		// the original song
		Song songGetTest = target(path).request(MediaType.APPLICATION_JSON).get(Song.class);
		isProcessedSongValid(songGetTest, 0, "Valid artist", "Valid album", "Ein valid Song 1", 2018);
		
		Response response = target(path).request().put(Entity.json(validPUTSong));
		Assert.assertEquals(Status.NO_CONTENT, response.getStatus());
		
		songGetTest = target(path).request(MediaType.APPLICATION_JSON).get(Song.class);
		isProcessedSongValid(songGetTest, 0, "Ein PUT Artist", "Ein PUT Album", "Ein PUT Titel", 1900);
		
		
	}

	private void isProcessedSongValid(Song songGetTest, int id, String artist, String album, String title, Integer released) {
		assertEquals(artist, songGetTest.getArtist());
        assertEquals(album, songGetTest.getAlbum());
        assertEquals(id, songGetTest.getId().intValue());
        assertEquals(title, songGetTest.getTitle());
        int year = songGetTest.getReleased();
        assertEquals(released, Integer.valueOf(year));
	}

	@Test
	public void updateSongWithValidXMLPayloadShouldReturn204AndUpdateSongWithPutSong() {
		// JSON changes ID 0
		validPUTSong.setId(1);
		String path = "/songs/1";
		
		//Song vor dem PUT
		// the original song
		Song songGetTest = target(path).request(MediaType.APPLICATION_XML).get(Song.class);
		isProcessedSongValid(songGetTest, 1, "Valid artist 2", "Valid album 2", "Ein valid Song 2", 2017);
		
		Response response = target(path).request().put(Entity.xml(validPUTSong));
		Assert.assertEquals(Status.NO_CONTENT, response.getStatus());
		
		songGetTest = target(path).request(MediaType.APPLICATION_JSON).get(Song.class);
		isProcessedSongValid(songGetTest, 1, "Ein PUT Artist", "Ein PUT Album", "Ein PUT Titel", 1900);
		
		
	}

		
		// ===== WRONG PAYLOAD =====

		// XML
		@Test
		public void updateSongXmlWithWrongPathIDShouldReturn404() {
			validPUTSong.setId(1);
			String path = "/songs/22";
			Response response = target(path).request().put(Entity.xml(validPUTSong));
			Assert.assertEquals(404, response.getStatus());
		}

		// JSON
		@Test
		public void updateSongJSONWithWrongPathIDShouldReturn404() {
			validPUTSong.setId(0);
			String path = "/songs/22";
			Response response = target(path).request().put(Entity.json(validPUTSong));
			Assert.assertEquals(404, response.getStatus());
		}

		
	// JSON
	@Test
	public void updateSongXMLWithDifferentIdInBodyShouldReturn400() {
		validPUTSong.setId(2); //id body
		String path = "/songs/1";
		Response response = target(path).request().put(Entity.xml(validPUTSong));
		Assert.assertEquals(400, response.getStatus());
	}

	// XML
	@Test
	public void updateSongJSONWithDifferentIdInBodyShouldReturn400() {
		validPUTSong.setId(2); //id in body
		String path = "/songs/1";
		Response response = target(path).request().put(Entity.xml(validPUTSong));
		Assert.assertEquals(400, response.getStatus());
	}

	

	@Test
	public void updateSongWithWrongParamaterShouldReturn404() {
		validPUTSong.setId(0);
		Response response = target("/songs/asdfaet").request().put(Entity.xml(validPOSTSong));
		Assert.assertEquals(404, response.getStatus());
	}

	@Test
	public void updateSongWithWrongPayloadShouldReturn400() {
		Response response = target("/songs/1").request().put(Entity.xml(songNoTitle));
		Assert.assertEquals(400, response.getStatus());
	}


}
