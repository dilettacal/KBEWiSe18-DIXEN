package de.htw.ai.kbe.services;

import java.util.Collection;
import java.util.NoSuchElementException;

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
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import de.htw.ai.kbe.bean.Song;
import de.htw.ai.kbe.database.interfaces.ISongs;

/**
 * 
 * @author dilet
 *
 */
// URL fuer diesen Service ist: http://localhost:8080/songsRX/rest/songs 
@Path("/songs")
public class SongsWebService {

	// Referenz auf InMemory-DB
	@Inject
	private ISongs songsStorage;
	
	@Context
	UriInfo uriInfo;

	// Konstruktor bekommt Verweis auf DB-Instanz
	public SongsWebService() {
		
	}

	/* METHODEN DES WEBSERVICE */

	// GET http://localhost:8080/songsRX/rest/songs
	// Returns all contacts
	
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Collection<Song> getAllSongs( @HeaderParam("Authorization") String key) {
		System.out.println("getAllSongs()...");
		//songsStorage.getAllSongs().forEach(s -> System.out.println(s)); //Test - OK
		return songsStorage.getAll();
	}

	// GET http://localhost:8080/songsRX/rest/songs/1
	// Returns: 200 and contact with id 1
	// Returns: 404 on provided id not found
	@GET
	@Path("/{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getSong(@PathParam("id") Integer id, @HeaderParam("Authorization") String key) {
		Song song = songsStorage.getSongById(id);
		if(song == null) {
			return Response.status(Response.Status.NOT_FOUND).entity(Response.Status.NOT_FOUND + ": No song found with id " + id).build();
		}			
		else {
			return Response.ok(song).build();
		}
		
	}


	@POST
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces(MediaType.TEXT_PLAIN)
	public Response createSong(Song song, @HeaderParam("Authorization") String key) {

		if(!(song instanceof Song)) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(Response.Status.BAD_REQUEST + "Payload is malformed. Please provide a valid song").build();
		}
		
		int newID = songsStorage.addSong(song);
		
		if(newID >0) {
			return Response.status(Response.Status.NOT_FOUND)
					.entity(Response.Status.NOT_FOUND + ": " + ": Adding new song failed for some reason.").build();
		}
		else {
			UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
	        uriBuilder.path(String.valueOf(newID));
	        return Response.created(uriBuilder.build()).status(Status.CREATED).entity(Status.CREATED + ": Song added (new id: " + newID + ")").build();
		}

	}


	@PUT
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/{id}")
	public Response updateSong(@PathParam("id") Integer id, Song song, @HeaderParam("Authorization") String key) {

		//System.out.println("Update with song: " + song);
		
		if(!(song instanceof Song)) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(Response.Status.BAD_REQUEST + "Payload is malformed. Please provide a valid song").build();
		}
		
		/*
		 * Fall: Beide IDs sind uebergeben, aber sie stimmen nicht ueberein
		 * Ueberpruefung: !id.equals(song.getId()) && song.getId() != null
		 * deckt den Fall ab, dass PUT-Anfrage gueltig (rest/songs/1) 
		 * aber Benutzer hat ID im Body gesetzt (z.B. 2) [song.getId() != null]
		 */
		//Beleg 3: !id.equals(song.getId()) && song.getId() != null
		if (!id.equals(song.getId()) && song.getId() != 0) { 
			System.out.println("Different IDs");
			return  Response.status(Response.Status.BAD_REQUEST)
					.entity("ID does not correspond to ID in payload ").build();
		}
		
		//Ein Song mit gueltigem ID wurde uebergeben. Pruefe ob Song gueltig (mit Titel ist):
		if(song.getTitle() == null || (song.getTitle().trim()).isEmpty()) { 
			System.out.println("Not valid prerequisite (title) for song");
			return  Response.status(Response.Status.BAD_REQUEST)
					.entity(Response.Status.BAD_REQUEST + ": Fail to updated Song").build();
		}
		
		/*
		 * Fall: Alles passt aber ID im Payload wurde vergessen
		 */
		if(song.getId() == null) {
			song.setId(id); // Song braucht eine ID, sonst wird er mit der folgenden Anweisung mit ID==null gespeichert
			System.out.println("Update successful");			
			return Response.status(Response.Status.NO_CONTENT).entity(Response.Status.NO_CONTENT + ": Update successful.").build();
		}		
		try{
			songsStorage.updateSong(song);
			return Response.status(Response.Status.NO_CONTENT).entity(Response.Status.NO_CONTENT + ": Update successful.").build();
		} catch(NoSuchElementException e) {
			return Response.status(Response.Status.NOT_FOUND)
					.entity(Response.Status.NOT_FOUND + ": Fail to updated Song").build();		
		}

	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response delete(@PathParam("id") Integer id, @HeaderParam("Authorization") String key) {
		//Beleg 4 - Diese Funktionalitaet soll nicht implementiert werden
		
//		try{
//			songsStorage.deleteSong(id);
//			return Response.status(Response.Status.NO_CONTENT).entity(Response.Status.NO_CONTENT + ": Delete successful.").build();
//		} catch (NoSuchElementException e) {
//			return Response.status(Response.Status.NOT_FOUND)
//					.entity(Response.Status.NOT_FOUND + ": Song ID was not found or not successfully deleted.").build();
//		}
		//Beleg 4: Nutzer koennen neue Songs anlegen und updaten, aber duerfen keine Songs mehr loeschen
		return Response.status(Response.Status.UNAUTHORIZED).entity(Response.Status.UNAUTHORIZED + ": You are not allowed to delete any song!").build();
	}
}
