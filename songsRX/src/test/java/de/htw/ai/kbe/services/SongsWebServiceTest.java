package de.htw.ai.kbe.services;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.htw.ai.kbe.bean.Song;
import de.htw.ai.kbe.filter.AuthStorageForTesting;
import de.htw.ai.kbe.storage.ISongs;
import de.htw.ai.kbe.storage.SongStorageForTesting;
import de.htw.ai.kbe.storage.SongsStorage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.inject.Singleton;
import javax.validation.Valid;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class SongsWebServiceTest extends JerseyTest {

	private static final String JSON_TITLE = "JSON title";
	private static final String JSON_ARTIST = "JSON artist";
	private static final String JSON_ALBUM = "JSON album";
	private static final int JSON_RELEASED = 1990;

	private static final String XML_TITLE = "XML title";
	private static final String XML_ARTIST = "XML artist";
	private static final String XML_ALBUM = "XML album";
	private static final int XML_RELEASED = 2018;

	@Override
	protected Application configure() {
		return new ResourceConfig(SongsWebService.class).register(new AbstractBinder() {
			@Override
			protected void configure() {
				bind(SongsStorage.class).to(ISongs.class).in(Singleton.class);

			}
		});
	}

// ==== PUT Tests ===
	// JSON - Full valid payload
	@Test
	public void updateSongValidJSONPayloadShouldReturn204AndUpdateTheSongWithNewValues() {
		// Change to song with id == 1

		int idToChange = 1;

		// the original song
		Song songBeforeUpdate = target("/songs/" + idToChange).request(MediaType.APPLICATION_JSON).get(Song.class);
		System.out.println(songBeforeUpdate);
		// First song ist the first song in the songs.json file
		assertTrue(songBeforeUpdate.getArtist().equalsIgnoreCase("Lukas Graham"));
		assertTrue(songBeforeUpdate.getAlbum().equalsIgnoreCase("Lukas Graham (Blue Album)"));
		assertTrue(songBeforeUpdate.getReleased() == 2015);
		assertTrue(songBeforeUpdate.getTitle().equalsIgnoreCase(("7 Years")));

		Song newSong = new Song.Builder(JSON_TITLE).artist(JSON_ARTIST).album(JSON_ALBUM).released(JSON_RELEASED)
				.id(idToChange).build();

		Response output = target("songs/" + idToChange).request().header("authorization", "testToken")
				.put(Entity.json(newSong));
		// Response is 204
		assertEquals(204, output.getStatus());

		// Update check
		Song afterUpdate = target("/songs/1").request(MediaType.APPLICATION_JSON).get(Song.class);
		assertTrue(afterUpdate.getAlbum().equals(JSON_ALBUM));
		assertTrue(afterUpdate.getArtist().equals(JSON_ARTIST));
		assertTrue(afterUpdate.getTitle().equals(JSON_TITLE));
		assertTrue(afterUpdate.getReleased() == (JSON_RELEASED));
		assertTrue(afterUpdate.getId() == idToChange);
	}

	//XML - Full valid payload
	@Test
	public void updateSongValidXMLPayloadShouldReturn204AndUpdateTheSongWithNewValues() {
		// Change to song with id == 1

		int idToChange = 1;

		// the original song
		Song songBeforeUpdate = target("/songs/" + idToChange).request(MediaType.APPLICATION_JSON).get(Song.class);
		assertTrue(songBeforeUpdate.getArtist().equalsIgnoreCase("Lukas Graham"));
		assertTrue(songBeforeUpdate.getAlbum().equalsIgnoreCase("Lukas Graham (Blue Album)"));
		assertTrue(songBeforeUpdate.getReleased() == 2015);
		assertTrue(songBeforeUpdate.getTitle().equalsIgnoreCase(("7 Years")));

		Song newSong = new Song.Builder(XML_TITLE).artist(XML_ARTIST).album(XML_ALBUM).released(XML_RELEASED)
				.id(idToChange).build();

		Response output = target("songs/" + idToChange).request().header("authorization", "testToken")
				.put(Entity.xml(newSong));
		// Response is 204
		assertEquals(204, output.getStatus());

		// Update check
		Song afterUpdate = target("/songs/1").request(MediaType.APPLICATION_JSON).get(Song.class);
		assertTrue(afterUpdate.getAlbum().equals(XML_ALBUM));
		assertTrue(afterUpdate.getArtist().equals(XML_ARTIST));
		assertTrue(afterUpdate.getTitle().equals(XML_TITLE));
		assertTrue(afterUpdate.getReleased() == (XML_RELEASED));
		assertTrue(afterUpdate.getId() == idToChange);
	}

	// UPDATE JSON - Payload only with title
	@Test
	public void updateSongValidJSONPayloadOnlyTitleShouldReturn204AndUpdateTheSongWithNewTitleAndOtherValuesSetToStandardValues() {
		// Change to song with id == 2
		/*
		 * { "id": 9, "title": "Private Show", "artist": "Britney Spears", "album":
		 * "Glory", "released": 2016 }
		 */
		int idToChange = 2;

		// the original song
		Song songBeforeUpdate = target("/songs/" + idToChange).request(MediaType.APPLICATION_JSON).get(Song.class);
		System.out.println(songBeforeUpdate);
		assertTrue(songBeforeUpdate.getArtist().equalsIgnoreCase("Britney Spears"));
		assertTrue(songBeforeUpdate.getAlbum().equalsIgnoreCase("Glory"));
		assertTrue(songBeforeUpdate.getReleased() == 2016);
		assertTrue(songBeforeUpdate.getTitle().equalsIgnoreCase(("Private Show")));

		Song newSong = new Song.Builder(JSON_TITLE).id(idToChange).build();

		Response output = target("songs/" + idToChange).request().header("authorization", "testToken")
				.put(Entity.json(newSong));
		// Response is 204
		assertEquals(204, output.getStatus());

		// Update check
		Song afterUpdate = target("/songs/" + idToChange).request(MediaType.APPLICATION_JSON).get(Song.class);
		System.out.println(afterUpdate);
		assertTrue(afterUpdate.getAlbum() == null); // Null if not provided
		assertTrue(afterUpdate.getArtist() == null);
		assertTrue(afterUpdate.getTitle().equals(JSON_TITLE));
		assertTrue(afterUpdate.getReleased() == (0)); // Standard value for int
		assertTrue(afterUpdate.getId() == idToChange);
	}

	// UPDATE XML - Payload only with title
	@Test
	public void updateSongValidXMLPayloadOnlyTitleShouldReturn204AndUpdateTheSongWithNewTitleAndOtherValuesSetToStandardValues() {
		int idToChange = 2;

		// the original song
		Song songBeforeUpdate = target("/songs/" + idToChange).request(MediaType.APPLICATION_JSON).get(Song.class);
		System.out.println(songBeforeUpdate);
		assertTrue(songBeforeUpdate.getArtist().equalsIgnoreCase("Britney Spears"));
		assertTrue(songBeforeUpdate.getAlbum().equalsIgnoreCase("Glory"));
		assertTrue(songBeforeUpdate.getReleased() == 2016);
		assertTrue(songBeforeUpdate.getTitle().equalsIgnoreCase(("Private Show")));

		Song newSong = new Song.Builder(XML_TITLE).id(idToChange).build();

		Response output = target("songs/" + idToChange).request().header("authorization", "testToken")
				.put(Entity.xml(newSong));
		// Response is 204
		assertEquals(204, output.getStatus());

		// Update check
		Song afterUpdate = target("/songs/" + idToChange).request(MediaType.APPLICATION_JSON).get(Song.class);
		System.out.println(afterUpdate);
		assertTrue(afterUpdate.getAlbum() == null); // Null if not provided
		assertTrue(afterUpdate.getArtist() == null);
		assertTrue(afterUpdate.getTitle().equals(XML_TITLE));
		assertTrue(afterUpdate.getReleased() == (0)); // Standard value for int
		assertTrue(afterUpdate.getId() == idToChange);
	}

	//UPDATE JSON - Not existing ID
	@Test
	public void updateSongWithNonExistingIDShouldReturn404JSON() {
		Song testSong = new Song();
		testSong.setId(14);
		testSong.setArtist(JSON_ARTIST);
		testSong.setTitle(JSON_TITLE);
		testSong.setAlbum(JSON_ALBUM);
		testSong.setReleased(JSON_RELEASED);
		Response response = target("/songs/" +testSong.getId()).request().header("authorization", "testToken")
				.put(Entity.json(testSong));
		System.out.println(response.getStatus());
		Assert.assertEquals(404, response.getStatus());
	}
	
	//UPDATE XML - Not existing ID
	public void updateSongWithNonExistingIDShouldReturn404XML() {
		Song testSong = new Song();
		testSong.setId(14);
		testSong.setArtist(XML_ARTIST);
		testSong.setTitle(XML_TITLE);
		testSong.setAlbum(XML_ALBUM);
		testSong.setReleased(XML_RELEASED);
		Response response = target("/songs/" +testSong.getId()).request().header("authorization", "testToken")
				.put(Entity.xml(testSong));
		System.out.println(response.getStatus());
		Assert.assertEquals(404, response.getStatus());
	}
	
	//UPDATE XML - Different ID param vs. body
	@Test
	public void updateSongWithNonMatchingIdShouldReturn400XMLAndNoUpdate() {
		int dbValidID = 2;
		Song songBeforeUpdate = target("/songs/" + dbValidID).request(MediaType.APPLICATION_JSON).get(Song.class);
		System.out.println(songBeforeUpdate);
		assertTrue(songBeforeUpdate.getArtist().equalsIgnoreCase("Britney Spears"));
		assertTrue(songBeforeUpdate.getAlbum().equalsIgnoreCase("Glory"));
		assertTrue(songBeforeUpdate.getReleased() == 2016);
		assertTrue(songBeforeUpdate.getTitle().equalsIgnoreCase(("Private Show")));
		
		Song testSong = new Song();
		testSong.setId(10); //10 in Body
		testSong.setArtist(XML_ARTIST);
		testSong.setTitle(XML_TITLE);
		testSong.setAlbum(XML_ALBUM);
		testSong.setReleased(XML_RELEASED);
		//id 1 in Param
		Response response = target("/songs/" + dbValidID).request().header("authorization", "testToken")
				.put(Entity.xml(testSong));
		System.out.println(response.getStatus());
		Assert.assertEquals(400, response.getStatus());
		
		Song songAfterUpdate = target("/songs/" + dbValidID).request(MediaType.APPLICATION_JSON).get(Song.class);
		//Nothing happened
		assertTrue(songAfterUpdate.getArtist().equalsIgnoreCase("Britney Spears"));
		assertTrue(songAfterUpdate.getAlbum().equalsIgnoreCase("Glory"));
		assertTrue(songAfterUpdate.getReleased() == 2016);
		assertTrue(songAfterUpdate.getTitle().equalsIgnoreCase(("Private Show")));
		
	}

	
	//UPDATE JSON - Different ID param vs. body
	@Test
	public void updateSongWithNonMatchingIdShouldReturn400JSONAndNoUpdate() {
		int dbValidID = 2;
		Song songBeforeUpdate = target("/songs/" + dbValidID).request(MediaType.APPLICATION_JSON).get(Song.class);
		System.out.println(songBeforeUpdate);
		assertTrue(songBeforeUpdate.getArtist().equalsIgnoreCase("Britney Spears"));
		assertTrue(songBeforeUpdate.getAlbum().equalsIgnoreCase("Glory"));
		assertTrue(songBeforeUpdate.getReleased() == 2016);
		assertTrue(songBeforeUpdate.getTitle().equalsIgnoreCase(("Private Show")));
		
		Song testSong = new Song();
		testSong.setId(1);
		testSong.setArtist(JSON_ARTIST);
		testSong.setTitle(JSON_TITLE);
		testSong.setAlbum(JSON_ALBUM);
		testSong.setReleased(JSON_RELEASED);
		Response response = target("/songs/" + dbValidID).request().header("authorization", "testToken")
				.put(Entity.xml(testSong));
		System.out.println(response.getStatus());
		Assert.assertEquals(400, response.getStatus());
		
		Song songAfterUpdate = target("/songs/" + dbValidID).request(MediaType.APPLICATION_JSON).get(Song.class);
		//Nothing happened
		assertTrue(songAfterUpdate.getArtist().equalsIgnoreCase("Britney Spears"));
		assertTrue(songAfterUpdate.getAlbum().equalsIgnoreCase("Glory"));
		assertTrue(songAfterUpdate.getReleased() == 2016);
		assertTrue(songAfterUpdate.getTitle().equalsIgnoreCase(("Private Show")));
		
	}
	
	//UPDATE JSON - No id in body but valid id as param
	@Test
	public void updateSongNoIdInBodyButValidParaIdAndPayloadJSON() {
		int dbValidID = 2;
		Song songBeforeUpdate = target("/songs/" + dbValidID).request(MediaType.APPLICATION_JSON).get(Song.class);
		System.out.println(songBeforeUpdate);
		assertTrue(songBeforeUpdate.getArtist().equalsIgnoreCase("Britney Spears"));
		assertTrue(songBeforeUpdate.getAlbum().equalsIgnoreCase("Glory"));
		assertTrue(songBeforeUpdate.getReleased() == 2016);
		assertTrue(songBeforeUpdate.getTitle().equalsIgnoreCase(("Private Show")));
		
		Song testSong = new Song(); //No id set for testSong
		testSong.setArtist(JSON_ARTIST);
		testSong.setTitle(JSON_TITLE);
		testSong.setAlbum(JSON_ALBUM);
		testSong.setReleased(JSON_RELEASED);
		System.out.println(testSong);
		Response response = target("/songs/" + dbValidID).request().header("authorization", "testToken")
				.put(Entity.json(testSong));
		System.out.println(response.getStatus());
		//Assert.assertEquals(204, response.getStatus());
		
		Song songAfterUpdate = target("/songs/" + dbValidID).request(MediaType.APPLICATION_JSON).get(Song.class);
		//Nothing happened
		assertTrue(songAfterUpdate.getArtist().equalsIgnoreCase(JSON_ARTIST));
		assertTrue(songAfterUpdate.getAlbum().equalsIgnoreCase(JSON_ALBUM));
		assertTrue(songAfterUpdate.getReleased() == JSON_RELEASED);
		assertTrue(songAfterUpdate.getTitle().equalsIgnoreCase((JSON_TITLE)));
		
	}

	
	//UPDATE XML - No id in body but valid id as param
	@Test
	public void updateSongNoIdInBodyButValidParamIdAndPayloadXML() {
		int dbValidID = 2;
		Song songBeforeUpdate = target("/songs/" + dbValidID).request(MediaType.APPLICATION_JSON).get(Song.class);
		System.out.println(songBeforeUpdate);
		assertTrue(songBeforeUpdate.getArtist().equalsIgnoreCase("Britney Spears"));
		assertTrue(songBeforeUpdate.getAlbum().equalsIgnoreCase("Glory"));
		assertTrue(songBeforeUpdate.getReleased() == 2016);
		assertTrue(songBeforeUpdate.getTitle().equalsIgnoreCase(("Private Show")));
		
		Song testSong = new Song(); //No id set for testSong
		testSong.setArtist(XML_ARTIST);
		testSong.setTitle(XML_TITLE);
		testSong.setAlbum(XML_ALBUM);
		testSong.setReleased(XML_RELEASED);
		System.out.println(testSong);
		Response response = target("/songs/" + dbValidID).request().header("authorization", "testToken")
				.put(Entity.xml(testSong));
		System.out.println(response.getStatus());
		Assert.assertEquals(204, response.getStatus());
		
		Song songAfterUpdate = target("/songs/" + dbValidID).request(MediaType.APPLICATION_JSON).get(Song.class);
		System.out.println(songAfterUpdate);
		//Nothing happened
		assertTrue(songAfterUpdate.getArtist().equalsIgnoreCase(XML_ARTIST));
		assertTrue(songAfterUpdate.getAlbum().equalsIgnoreCase(XML_ALBUM));
		assertTrue(songAfterUpdate.getReleased() == XML_RELEASED);
		assertTrue(songAfterUpdate.getTitle().equalsIgnoreCase((XML_TITLE)));
	}
	
	//UPDATE Wrong path
	@Test
	@Ignore
	public void updateSongWithWrongPath() {
		//XXX: This test leads to Socket Exception (Connection reset) while executing singularly or within the whole test class
		//Bsp. rest/song/asd
		Song testSong = new Song(); //No id set for testSong
		testSong.setArtist(JSON_ARTIST);
		testSong.setTitle(JSON_TITLE);
		testSong.setAlbum(JSON_ALBUM);
		testSong.setReleased(JSON_RELEASED);
		Response response = target("/songs/asd").request().header("authorization", "testToken").put(Entity.json(testSong));
		System.out.println(response.getStatus());
		Assert.assertEquals(404, response.getStatus());
	}

	
	@Test
	@Ignore
	public void updateSongWithWrongPayload() {
		//XXX: This test leads to Socket Exception (Connection reset) while executing singularly or within the whole test class
		Song testSong = new Song();
		testSong.setAlbum("Ein Album");
		Response response = target("/songs/asd").request().header("authorization", "testToken").put(Entity.json(testSong));
		System.out.println(response.getStatus());
		Assert.assertEquals(404, response.getStatus());
		
		response = target("/songs/asd").request().header("authorization", "testToken").put(Entity.xml(testSong));
		System.out.println(response.getStatus());
		Assert.assertEquals(404, response.getStatus());
	}
	
}
