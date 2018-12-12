package de.htw.ai.kbe.services;

import java.util.Collection;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import de.htw.ai.kbe.bean.Song;
import de.htw.ai.kbe.filter.AuthenticationFilter;
import de.htw.ai.kbe.filter.IAuth;
import de.htw.ai.kbe.storage.ISongs;

// URL fuer diesen Service ist: http://localhost:8080/songsRX/rest/songs 
@Path("/songs")
public class SongsWebService {

	// Referenz auf InMemory-DB
	private ISongs songsStorage;
	private IAuth authStorage;

	// Konstruktor bekommt Verweis auf DB-Instanz
	@Inject
	public SongsWebService(ISongs songStorage, IAuth authStorage) {
		this.songsStorage = songStorage;
		this.authStorage = authStorage;
	}

	/* METHODEN DES WEBSERVICE */

	/*
	 * Der WebService muss folgende Anfragen vearbeiten:
	 * 
	 * ##### GET ##### GET Accept:application/json
	 * http://localhost:8080/songsRX/rest/songs - schickt alle Songs in JSON zurück
	 * GET Accept:application/xml http://localhost:8080/songsRX/rest/songs/7 -
	 * schickt Song 7 in XML zurück #### POST #### POST Content-Type:application/xml
	 * http://localhost:8080/songsRX/rest/songs (mit einer XML-Payload) - trägt den
	 * Song in der DB ein. Falls erfolgreich, dann Status-Code 201 und liefert die
	 * URL für den neuen Song im “Location”-Header an den Client zurück. POST
	 * Content-Type:application/json http://localhost:8080/songsRX/rest/songs (mit
	 * einer JSON-Payload) - trägt den Song in der DB ein. Falls erfolgreich, dann
	 * Status-Code 201 und liefert die URL für den neuen Song im “Location”-Header
	 * an den Client zurück. #### PUT ##### PUT Content-Type:application/xml
	 * http://localhost:8080/songsRX/rest/songs/7 (mit einer XML-Payload) - erneuert
	 * den Eintrag für Id 7 in der DB und schickt “on Success” den HTTP-Statuscode
	 * 204. PUT Content-Type:application/json
	 * http://localhost:8080/songsRX/rest/songs/7 (mit einer JSON-Payload) -
	 * erneuert den Eintrag für Id 7 in der DB und schickt “on Success” den
	 * HTTP-Statuscode 204. #### DELETE #### DELETE
	 * http://localhost:8080/songsRX/rest/songs/7 - loescht den Eintrag Id 7 in der
	 * DB und schickt on Success einen HTTP-Statuscode 204.
	 * 
	 */

	// GET http://localhost:8080/songsRX/rest/songs
	// Returns all contacts
	// TODO: fuer xml klappt das mit return-Response noch nicht
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getAllSongs(@HeaderParam("Authorization") String key) {
		String userIdAuthToken = authStorage.getUserIdByToken(key);
		if (key != null && userIdAuthToken != null) {
			System.out.println("getAllSongs: Returning all songs!");
			Collection<Song> allSongs = songsStorage.getAllSongs();
			return Response.status(Response.Status.OK).entity(allSongs).build();
		} else
			return Response.status(Response.Status.UNAUTHORIZED).entity("No valid key.").build();

	}

	// GET http://localhost:8080/songsRX/rest/songs/1
	// Returns: 200 and contact with id 1
	// Returns: 404 on provided id not found
	@GET
	@Path("/{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getSong(@PathParam("id") Integer id, @HeaderParam("Authorization") String key) {
		// TODO || FIXME: Test mit "OLD", id ist schon als Integer, sollte ID lieber als
		// String gespeichert werden? und hier zieht man den Wert mit
		// Integer.parseInt(id)?
		String userIdAuthToken = authStorage.getUserIdByToken(key);
		if (key != null && userIdAuthToken != null) {
			Song song = songsStorage.getSong(id);
			if (song != null) {
				System.out.println("getSong: Returning song for id " + id);
				return Response.ok(song).build();
			} else {
				return Response.status(Response.Status.NOT_FOUND).entity("No song found with id " + id).build();
			}
		} else {
			return Response.status(Response.Status.UNAUTHORIZED).entity("No valid key.").build();
		}

	}

// POST http://localhost:8080/songsRX/rest/songs with contact in payload
// Temp. solution returns: 
//   Status Code 200 and the new id of the contact in the payload 
// diese methode nimmt json oder xml object und fragt ob es möglich ist diese auf contact pojos abzubilden. wenn ja dann macht er
	// das automatisch

	@Context
	UriInfo uriInfo;

	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces(MediaType.TEXT_PLAIN) // TODO: brauchen wir das? POST schickt nur Location Header zurueck, siehe PUT
									// und DELETE -> da gibt es auch kein Consumes
	public Response createSong(Song song, @HeaderParam("Authorization") String key) {
		String userIdAuthToken = authStorage.getUserIdByToken(key);
		if (key == null || userIdAuthToken == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("You don't have any permission").build();
		} else {
			if (song != null && song.getTitle() != null) { // Bedingungen aus Beleg2!
				Integer newId = songsStorage.addSong(song);
				if (newId == null) {
					return Response.status(Response.Status.BAD_REQUEST)
							.entity("Adding new song failed for some reason.").build();
				} else {
					return Response.status(Response.Status.CREATED).entity(Response.Status.CREATED + ": "
							+ "Album - Name: " + song.getAlbum() + "Album - ID: " + song.getId()).build();
				}
			} else {
				return Response.status(Response.Status.NOT_FOUND)
						.entity(Response.Status.NOT_FOUND + ": " + "Song not found ").build();
			}
		}

	}

//     Besser: 
//         Status Code 201 und URI fuer den neuen Eintrag im http-header 'Location' zurueckschicken, also:
//         Location: /songsRX/rest/songs/neueID
//    
//     Dafuer: 
//     @Context UriInfo uriInfo; // Dependency Injection (spaeter)
//    
//  @Context UriInfo uriInfo;	

	@PUT
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/{id}")
	public Response updateSong(@PathParam("id") Integer id, Song song, @HeaderParam("Authorization") String key) {
		String userIdAuthToken = authStorage.getUserIdByToken(key);
		if (key == null || userIdAuthToken == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("No valid key.").build();
		} else {
			boolean updateSuccessful = false;
			if (song != null && song.getTitle() != null) {
				if (song.getId() != null) {
					updateSuccessful = songsStorage.updateSong(id, song);
					if (updateSuccessful)
						return Response.status(Response.Status.NO_CONTENT).entity("Update successful.").build();
					else
						return Response.status(Response.Status.BAD_REQUEST)
								.entity("Wrong content type or song ID was not found or not successfully updated.")
								.build();
				} else {
					if (songsStorage.getSong(id) != null) {
						updateSuccessful = songsStorage.updateSong(id, song);
						return Response.status(Response.Status.NO_CONTENT).entity("Succes to updated Song").build();

					} else {
						return Response.status(Response.Status.NOT_FOUND)
								.entity(Response.Status.NOT_FOUND + "Fail to updated Song").build();
					}
				}
			} else {
				return Response.status(Response.Status.CONFLICT)
						.entity(Response.Status.CONFLICT + "Fail to updated Song, because of a Bad Payload").build();
			}
		}
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response delete(@PathParam("id") Integer id, @HeaderParam("Authorization") String key) {
		String userIdAuthToken = authStorage.getUserIdByToken(key);
		if (key == null || userIdAuthToken == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("Key not valid.").build();
		}
		Song song = songsStorage.deleteSong(id);
		if (song == null)
			return Response.status(Response.Status.NOT_FOUND)
					.entity("Song ID was not found or not successfully deleted.").build();
		else
			return Response.status(Response.Status.NO_CONTENT).entity("Delete successful.").build();

	}
}