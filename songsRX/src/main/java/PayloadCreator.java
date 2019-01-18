import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.eclipse.persistence.jaxb.MarshallerProperties;

import de.htw.ai.kbe.bean.Song;
import de.htw.ai.kbe.bean.SongList;
import de.htw.ai.kbe.bean.User;
import de.htw.ai.kbe.storage.SongsStorage;

/**
 * Auxiliary Class to create valid payload from java objects
 * 
 * Needs the file: jaxb.properties in de.htw.ai.kbe.bean
 * @author dilet
 *
 */
public class PayloadCreator {
	
	public static SongsStorage storage = SongsStorage.getInstance();
	
	 public static void main(String[] args)
	    {
		 	List<Song> availableSongs = storage.getAll();
		 	//WavailableSongs.forEach(s -> System.out.println(s));
		 	System.out.println("Random choosen Songs:");
		 	Random randomizer = new Random();
		 	Song random = availableSongs.get(randomizer.nextInt(availableSongs.size()));
		 	System.out.println(random);
		 	
		 	Song random1 = availableSongs.get(randomizer.nextInt(availableSongs.size()));
		 	System.out.println(random1);
		 	List<Song> songToList = new ArrayList<Song>();
		 	songToList.add(random);
		 	songToList.add(random1);
		 	//Java object. We will convert it to XML.
		 	SongList list = new SongList();
		 	list.setId(7);
		 	User owner = new User();
		 	owner.setId("mmuster");
		 	owner.setFirstName("Maxime");
		 	owner.setLastName("Muster");
		 	list.setOwner(owner);
		 	list.setPublic(true);
		 	list.setSongs(songToList);
		 	
		 	System.setProperty("javax.xml.bind.context.factory","org.eclipse.persistence.jaxb.JAXBContextFactory");
	     
	        System.out.println("XML Payload:" + System.getProperty("line.separator"));	         
	        //Method which uses JAXB to convert object to XML
	        jaxbObjectToXML(null, null, null, list);
	        System.out.println(System.getProperty("line.separator") +"JSON Payload: ");
	        jaxbObjectToJSON(owner, random1, songToList, list);
	        System.out.println("End Payload");
	    }
	 
	 
	 
	 
	 //XML Payload wird in der Console ausgegeben
	    private static void jaxbObjectToXML(User user, Song song, List<Song> songlist, SongList list)
	    {
	        try
	        {
	            //Create JAXB Context
	            JAXBContext jaxbContext = JAXBContext.newInstance(SongList.class);
	             
	            //Create Marshaller
	            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	 
	            //Required formatting??
	            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	 
	            //Print XML String to Console
	            StringWriter sw = new StringWriter();
	             
	            //Write XML to StringWriter
	            jaxbMarshaller.marshal(list, sw);
	             
	            //Verify XML Content
	            String xmlContent = sw.toString();
	            System.out.println( xmlContent );
	 
	        } catch (JAXBException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    //https://howtodoinjava.com/jaxb/convert-object-to-json-moxy/ 	
	    private static void jaxbObjectToJSON(User user, Song song, List<Song> songlist, SongList list)
	    {
	        try
	        {
	        	JAXBContext jaxbContextSongList = JAXBContext.newInstance(SongList.class);
	            Marshaller jaxbMarshallerSongList = jaxbContextSongList.createMarshaller();
	 
	            // To format JSON
	            jaxbMarshallerSongList.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	             
	            //Set JSON type
	            jaxbMarshallerSongList.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
	            jaxbMarshallerSongList.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, true);
	 
	            //Print JSON String to Console
	            StringWriter sw = new StringWriter();
	            jaxbMarshallerSongList.marshal(list, sw);
	            System.out.println("Ganze Liste");
	            System.out.println(sw.toString());
	            System.out.println("===========================================");
	            
	            //===================
	            
	            JAXBContext jaxbContextSong = JAXBContext.newInstance(Song.class);
	            Marshaller jaxbMarshallerSong = jaxbContextSong.createMarshaller();
	 
	            // To format JSON
	            jaxbMarshallerSong.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	             
	            //Set JSON type
	            jaxbMarshallerSong.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
	            jaxbMarshallerSong.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, true);
	 
	            //Print JSON String to Console
	            StringWriter sw2= new StringWriter();
	            jaxbMarshallerSong.marshal(song, sw2);
	            System.out.println("Song");
	            System.out.println(sw2.toString());
	            System.out.println("===========================================");
	            
     //===================
	            
	            JAXBContext jaxbContextUser = JAXBContext.newInstance(User.class);
	            Marshaller jaxbMarshallerUser = jaxbContextUser.createMarshaller();
	 
	            // To format JSON
	            jaxbMarshallerUser.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	             
	            //Set JSON type
	            jaxbMarshallerUser.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
	            jaxbMarshallerUser.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, true);
	 
	            //Print JSON String to Console
	            StringWriter sw3 = new StringWriter();
	      
	            jaxbMarshallerUser.marshal(user, sw3);
	            System.out.println("User");
	            System.out.println(sw3.toString());
	            System.out.println("===========================================");
	 
	        } catch (JAXBException e) {
	            e.printStackTrace();
	        }
	    }

}
