package de.htw.ai.kbe.services;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.htw.ai.kbe.bean.Song;
import de.htw.ai.kbe.bean.SongList;
import de.htw.ai.kbe.bean.User;
import de.htw.ai.kbe.database.interfaces.ISongList;
import de.htw.ai.kbe.database.interfaces.ISongs;
import de.htw.ai.kbe.database.interfaces.IUser;
import de.htw.ai.kbe.filter.IAuth;

/**
 * Servlet to read and write Songlists into a simple kind of database
 *
 * @version 0.1
 */
@Path("/songLists")
public class SongListWebService {

	@Inject
	private IUser userDB;

	@Inject
	private ISongList songListDB;

	@Inject
	private ISongs songsDB;

	@Inject
	private IAuth tokenDB;

	@Context
	private UriInfo uriInfo;


	// =========== GET Requests ========== //

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getListByUserId(@QueryParam("userId") String userID, @HeaderParam("Authorization") String token) {
		System.out.println("GET - List by UserId (?userId=xxx)");
		/*
		 * Query-Beispiele: 1. GET /songsRX/rest/songLists?userId=mmuster
		 */

		/*
		 * Algorithmus: Entscheidend sind hier der Token im Authorization Header und der
		 * userID als Parameter
		 * 
		 * Fall 1: UserID als Param entspricht dem authorizierten Benutzer (ermittelbar
		 * nach dem Token im Authorization Header) 1. Pruefung dass der Token-Besitzer
		 * (userId in AuthStorage) == userID liefert true --> Er will all seine
		 * Songlisten angezeigt
		 * 
		 * Fall 2: UserID als Param entspricht nicht dem authorizierten Benutzer
		 * (ermittelbar nach dem Token im Authorization Header) 1. Pruefung dass der
		 * Token-Besitzer (userId in AuthStorage) == userID liefert false --> Er will
		 * die SongListen vom Nutzer userId --> Er kriegt nur die public SongListen
		 *
		 */


		String idFromToken = tokenDB.getUserIdFromToken(token);
		System.out.println("ID from Token: " + idFromToken);

		if (idFromToken.equals(userID)) {
			System.out.println("All SongLists for User: " + userID);
			User u = userDB.getUserByStringID(userID);
			System.out.println("Found this user: " + u);
			List<SongList> lists = songListDB.getAllListsOfUser(u);
			GenericEntity<List<SongList>> generic = new GenericEntity<List<SongList>>(lists) {
			};
			return Response.ok(generic).build();
		} else {
			System.out.println("Public SongLists for User: " + userID);
			// Der Authentifizierte Benutzer fragt nach den Listen von userID
			User u = userDB.getUserByStringID(userID);

			List<SongList> lists = songListDB.getAllListsOfUser(u);
			System.out.println("Found some song lists: " + !lists.isEmpty());
			List<SongList> publicLists = new ArrayList<SongList>();
			for (SongList sl : lists) {
				if (sl.isPublic()) {
					publicLists.add(sl);
				}
			}

			GenericEntity<List<SongList>> generic = new GenericEntity<List<SongList>>(publicLists) {
			};
			return Response.ok(generic).build();
		}
	}

	@GET
	@Path("/{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getListByListID(@PathParam("id") Integer id, @HeaderParam("Authorization") String token) {
		System.out.println("GET - List by ListID (rest/songsLists/1)");
		/*
		 * Algorithmus: 1. Rufe den UserID, der dem Token entspricht 2. Suche in der DB
		 * nach der SongList Nr: {id} 2.1 Wenn SongListe existiert: 2.2. Wenn Songliste
		 * dem userId gehoert --> Liefern 2.3. Wenn nicht 2.3.1 Wenn public liefern
		 * 2.3.2 Wenn private 403 *
		 * 
		 */
		SongList list = null;
		try {
			list = songListDB.getSongListByID(id);
		} catch (NoResultException e) {
			return Response.status(Status.NOT_FOUND).build();
		}

		if (list == null) {
			return Response.status(Status.NOT_FOUND).build();
		} else if (list.getOwner().getId().equals(tokenDB.getUserIdFromToken(token)) || list.isPublic()) {
			return Response.ok(list).build();
		} else if (!list.isPublic()) {
			return Response.status(Status.FORBIDDEN).build();
		}
		// wenn etwas schief geht
		return Response.status(Status.BAD_REQUEST).build();

	}

	// ======== POST====== //

	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces(MediaType.TEXT_PLAIN) 
	//@JsonIgnoreProperties(ignoreUnknown = true)
	public Response addSongList(@HeaderParam("Authorization") String token, SongList songList) {

		String idFromToken = tokenDB.getUserIdFromToken(token);
		if (idFromToken != "" || idFromToken != null) {
			User user;
			try {
				// User der uber AuthT-Token authentifiziert ist
				user = userDB.getUserByStringID(idFromToken);
				User owner = songList.getOwner();
				if (owner != null) {
					if (owner.getId().equals(user.getId())) {
						songList.setOwner(user);
						List<Song> songsFromPayload = songList.getSongs();
						if (songsFromPayload == null || songsFromPayload.isEmpty()) {
							return Response.status(Status.BAD_REQUEST).entity("No songs provided!").build();
						} else {
							System.out.println("Number of songs contained in Payload: " + songsFromPayload.size());
							// Check if all songs are in the DB - Check only by IDs, otherwise we get "false"
							List<Integer> allSongsIDs = songsDB.getAll().stream().map(s -> s.getId()).collect(Collectors.toList());
							allSongsIDs.forEach(s -> System.out.println(s));
							System.out.println("Songs from payload:");
							songsFromPayload.forEach(s -> System.out.println(s.getId()));
							List<Integer> idsFromPayload = songsFromPayload.stream().map(s -> s.getId()).collect(Collectors.toList());
							//Wenn true, dann persist wird gestartet
							System.out.println(allSongsIDs.containsAll(idsFromPayload));
							
							if(allSongsIDs.containsAll(idsFromPayload)) {
								System.out.println("Persisting song list....");
								try {
									boolean successful = songListDB.saveSongList(songList); 
									if(successful == false) {
										return Response.status(Status.BAD_REQUEST)
												.entity(Status.BAD_REQUEST + " Something went wrong!").build();
									}
									System.out.println("SongList persisted");
									UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
									uriBuilder.path(Integer.toString(songList.getId()));
									return Response.created(uriBuilder.build())
											.entity("List added (new id: " + songList.getId() + ")").build();
									
								} catch(PersistenceException e) {
									return Response.status(Status.BAD_REQUEST)
											.entity(Status.BAD_REQUEST).build();
								}
							}
							else {
								return Response.status(Status.BAD_REQUEST)
										.entity("All songs in the list must be already stored in the DB!").build();
							}
							
							

						}
					} else {
						return Response.status(Status.UNAUTHORIZED).entity("POST is only for own lists allowed!")
								.build();
					}
				}
			} catch (NotFoundException e) {
				return Response.status(Status.UNAUTHORIZED)
						.entity("Not existing user is not allowed to perform operation!").build();
			}

		}
		//If something unexpected happens 
		return Response.status(Status.BAD_REQUEST).entity("Something went wrong! Please try again").build();

	}

	// ======= DELETE ====== //

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response delete(@PathParam("id") Integer id, @HeaderParam("Authorization") String token) {
		// String userFromToken = tokenDB.getUserIdByToken(token);

		SongList list = songListDB.getSongListByID(id);
		if (list == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		String idFromToken = tokenDB.getUserIdFromToken(token);

		if (list.getOwner().getId().equals(idFromToken)) {
			boolean successful = songListDB.deleteSongList(list);
			if(successful == false) {
				return Response.status(Status.BAD_REQUEST).entity("Unable to delete songlist.").build();
			}
			return Response.status(Response.Status.OK).build();
		}
		// wenn etwas schief geht
		else
			return Response.status(Status.FORBIDDEN).build();
	}

}
