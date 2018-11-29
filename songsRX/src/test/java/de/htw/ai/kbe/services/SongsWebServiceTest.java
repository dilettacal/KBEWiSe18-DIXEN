package de.htw.ai.kbe.services;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;

import de.htw.ai.kbe.storage.ISongs;
import de.htw.ai.kbe.storage.SongsStorageTest;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class SongsWebServiceTest {

	 protected Application configure() {
	        return new ResourceConfig(SongsWebService.class).register(new AbstractBinder() {
				
				@Override
				protected void configure() {
					bind(SongsStorageTest.class).to(ISongs.class);					
				}
			});
	 }

//	    @Test
//	    public void getContactDefaultContentTypeShouldBeXML() {
//	        String response = target("/contacts/1").request().get(String.class);
//	        System.out.println(response);
//	        Assert.assertTrue(response.startsWith("<?xml"));
//	    }
	//    
//	    @Test
//	    public void getContactWithValidIdShouldReturnContact() {
//	        Contact contact = target("/contacts/1")
//	        		.request(MediaType.APPLICATION_JSON)
//	        		.get(Contact.class);
//	        System.out.println(contact);
//	        Assert.assertEquals(1, contact.getId().intValue());
//	    }
	//    
//	    @Test
//	    public void getContactWithNonExistingIdShouldReturn404() {
//	        Response response = target("/contacts/22").request().get();
//	        Assert.assertEquals(204, response.getStatus());
//	    }
	//    
//	    @Test
//	    public void getContactWithStringIdShouldReturn404() {
//	        Response response = target("/contacts/ksksksk").request().get();
//	        Assert.assertEquals(404, response.getStatus());
//	    }
	//    
//	    @Test
//	    public void createContactShouldReturn201AndID() {
//	    		Contact bob = new Contact();
//	    		bob.setFirstName("Bob");
//	    		bob.setLastName("MUELLER");
//	    		bob.setMobile("+4917687654321");			
//	        Response response = target("/contacts").request().post(Entity.xml(bob));
//	        Assert.assertEquals(201, response.getStatus());
//	        //Assert.assertEquals("3", response.readEntity(String.class));
//	        Assert.assertTrue(response.getLocation().toString().endsWith("3"));
//	    }
	    
}
