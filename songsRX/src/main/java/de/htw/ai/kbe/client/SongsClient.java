package de.htw.ai.kbe.client;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.htw.ai.kbe.bean.Song;

public class SongsClient {
	
	private static Client client = ClientBuilder.newClient();
	private static WebTarget baseTarget = client.target("http://localhost:8080/songsRX/rest/songs");

	public static void main(String[] args) {
		//listSongs();
	}
	
//	private static void listSongs() {
//		System.out.println("------- Getting all songs:");
//		
//		// Antwort als JSON-String 
//		String response = baseTarget.request(MediaType.APPLICATION_JSON)
//				.get(String.class);
//		System.out.println("JSON-Contacts received: " + response);
//				
//		// Antwort gleich in eine Liste von Contact-POJOs einlesen lassen
//		List<Song> contacts = baseTarget.request()
//				.get(new GenericType<List<Song>>() {});
//		contacts.forEach(System.out::println);
//	}
//
//	private static Song getSong(long id) {
//		System.out.printf("------- Getting song with id: %s\n", id);
//		
//		// Antwort als XML-String 
//		String response = baseTarget
//				.path(Long.toString(id))
//				.request(MediaType.APPLICATION_XML)
//				.get(String.class);
//		System.out.println("XML-Contact received: " + response);
//		
//		// Antwort gleich in das Contact-POJO einlesen lassen
//		Song song = baseTarget
//				.path(Long.toString(id))
//				.request(MediaType.APPLICATION_XML)
//				.get(Contact.class);
//		System.out.println("Contact retrieved: " + song);
//		return song;
//	}
//
//	private static void createSongs() {
//		System.out.println("------- Creating or posting songs:");
//		for (int i = 1; i <= 5; i++) {
//			Song song = new Song();
//			song.setTitle("FirstName " + i);
//			song.setArtist("LastName " + i);
//			Response response;
//			Entity<Contact> entity;
//			if (i%2 == 0) {
//				entity = Entity.xml(song);
//			} else {
//				entity = Entity.json(song);
//			}
//			System.out.println("Posting new contact: " + 
//					entity.toString());
//			
//			response = baseTarget.request().post(entity);
//			System.out.println("Created contact: " 
//					+ response.getStatus() + " id =" 
//					+ response.readEntity(String.class));
//		}
//	}

//	private static void updateContact(Contact contact) {
//		System.out.printf("------- Updating (putting) a contact with id: %s\n", contact.getId());
//		Response response = baseTarget.path(Long.toString(contact.getId())).request().put(Entity.xml(contact));
//		System.out.println("Updated contact: " + response.getStatus());
//	}
//
//	private static void deleteContactById(int... ids) {
//		System.out.printf("------- Deleting contacts: %s\n", Arrays.toString(ids));
//		for (int id : ids) {
//			WebTarget target = baseTarget.path(Long.toString(id));
//			Response response = target.request().delete();
//			System.out.println("Deleted contact: " + response.getStatus());
//		}
//	}
}