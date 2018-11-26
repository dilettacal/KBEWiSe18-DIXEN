package de.htw.ai.kbe.services;

import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
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

import de.htw.ai.kbe.bean.Contact;
import de.htw.ai.kbe.storage.AddressBook;

// URL fuer diesen Service ist: http://localhost:8080/contactsJAXRS/rest/contacts 
@Path("/contacts")
public class ContactsWebService {

	// GET http://localhost:8080/contactsJAXRS/rest/contacts
	// Returns all contacts
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Collection<Contact> getAllContacts() {
		System.out.println("getAllContacts: Returning all contacts!");
		return AddressBook.getInstance().getAllContacts();
	}

	 //GET http://localhost:8080/contactsJAXRS/rest/contacts/1
	 //Returns: 200 and contact with id 1
	 //Returns: 404 on provided id not found
	@GET
	@Path("/{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Contact getContact(@PathParam("id") Integer id) {
		Contact contact = AddressBook.getInstance().getContact(id);
		if (contact != null) {
			System.out.println("getContact: Returning contact for id " + id);
			return contact;  //Response.ok(contact).build();
		} else {
			return contact; //Response.status(Response.Status.NOT_FOUND).entity("No contact found with id " + id).build();
		}
	}

// POST http://localhost:8080/contactsJAXRS/rest/contacts with contact in payload
// Temp. solution returns: 
//   Status Code 200 and the new id of the contact in the payload 
// diese methode nimmt json oder xml object und fragt ob es möglich ist diese auf contact pojos abzubilden. wenn ja dann macht er
	//das automatisch

	@Context
	UriInfo uriInfo;
	
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces(MediaType.TEXT_PLAIN)
	public Response createContact(Contact contact) {
		 int newId = AddressBook.getInstance().addContact(contact);
	     UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
	     uriBuilder.path(Integer.toString(newId));
	     return Response.created(uriBuilder.build()).build();

	}

//     Besser: 
//         Status Code 201 und URI fuer den neuen Eintrag im http-header 'Location' zurueckschicken, also:
//         Location: /contactsJAXRS/rest/contacts/neueID
//    
//     Dafuer: 
//     @Context UriInfo uriInfo; // Dependency Injection (spaeter)
//    
//  @Context UriInfo uriInfo;	

	@PUT
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/{id}")
	public Response updateSong(@PathParam("id") Integer id, Contact contact) {
		return Response.status(Response.Status.METHOD_NOT_ALLOWED).entity("PUT not implemented").build();
	}

	@DELETE
	@Path("/{id}")
	public Response delete(@PathParam("id") Integer id) {
		return Response.status(Response.Status.METHOD_NOT_ALLOWED).entity("DELETE not implemented").build();
	}
}