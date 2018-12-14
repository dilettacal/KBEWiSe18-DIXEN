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
import javax.ws.rs.core.Response.Status;
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
	@Inject
	private ISongs songsStorage;
	
	@Context
	UriInfo uriInfo;

	// Konstruktor bekommt Verweis auf DB-Instanz
	public SongsWebService() {
		
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
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Collection<Song> getAllSongs() {
		System.out.println("getAllSongs()...");
		//songsStorage.getAllSongs().forEach(s -> System.out.println(s)); //Test - OK
		return songsStorage.getAllSongs();
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

		Song song = songsStorage.getSong(id);
		if(song == null) {
			return Response.status(Response.Status.NOT_FOUND).entity(Response.Status.NOT_FOUND + ": No song found with id " + id).build();
		}			
		else {
			return Response.ok(song).build();
		}
		
	}

// POST http://localhost:8080/songsRX/rest/songs with contact in payload
// Temp. solution returns: 
//   Status Code 200 and the new id of the contact in the payload 
// diese methode nimmt json oder xml object und fragt ob es möglich ist diese auf contact pojos abzubilden. wenn ja dann macht er
	// das automatisch

	@POST
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Produces(MediaType.TEXT_PLAIN) // TODO: brauchen wir das? POST schickt nur Location Header zurueck, siehe PUT
									// und DELETE -> da gibt es auch kein Consumes
	public Response createSong(Song song, @HeaderParam("Authorization") String key) {

		Integer newID = songsStorage.addSong(song);
		
		if(newID == null) {
			return Response.status(Response.Status.NOT_FOUND)
					.entity(Response.Status.NOT_FOUND + ": " + ": Adding new song failed for some reason.").build();
		}
		else {
			UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
	        uriBuilder.path(newID.toString());
	        return Response.created(uriBuilder.build()).status(Status.CREATED).entity(Status.CREATED + ": Song added (new id: " + newID + ")").build();
		}

	}


	@PUT
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/{id}")
	public Response updateSong(@PathParam("id") Integer id, Song song, @HeaderParam("Authorization") String key) {

		//Dieser Check ist notwendig:
		//PUT Request --> ...../rest/songs/7
		//Body: 
		/*
		 * {
			  "id" : 7,
			  "title" : "Man on the Moon",
			  "artist" : "REM",
			  "album" : "REM Collection",
			  "released" : 1998
			}
}
		 */
		if(!song.getId().equals(id) || song == null) {
			return  Response.status(Response.Status.BAD_REQUEST)
					.entity("Id does not correspond to Id in payload ").build();
		}
		
		//Ein Song mit gueltigem ID wurde uebergeben. Titelcheck:
		if(song.getTitle() == null || (song.getTitle().trim()).isEmpty()) { 
			//README: Ich wuerde eher BAD Request zurueckschicke, wenn Song-Format ungueltig
			return  Response.status(Response.Status.BAD_REQUEST)
					.entity(Response.Status.BAD_REQUEST + ": Fail to updated Song").build();
		}
		
		boolean updated = songsStorage.updateSong(id, song);
		if(!updated) {
			return Response.status(Response.Status.NOT_FOUND)
					.entity(Response.Status.NOT_FOUND + ": Fail to updated Song").build();		
		}
		else {
			return Response.status(Response.Status.NO_CONTENT).entity(Response.Status.NO_CONTENT + ": Update successful.").build();
		}

	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response delete(@PathParam("id") Integer id, @HeaderParam("Authorization") String key) {
		Song song = songsStorage.deleteSong(id);
		if(song == null) {
			return Response.status(Response.Status.NOT_FOUND)
					.entity(Response.Status.NOT_FOUND + ": Song ID was not found or not successfully deleted.").build();
		}
		else {
			return Response.status(Response.Status.NO_CONTENT).entity(Response.Status.NO_CONTENT + ": Delete successful.").build();
		}
	}
}