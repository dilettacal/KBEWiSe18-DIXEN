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
	     
	        System.out.println("XML Payload:" + System.getProperty("line.separator"));	         
	        //Method which uses JAXB to convert object to XML
	        jaxbObjectToXML(list);
	        
	        jaxbObjectToJSON(list);
	    }
	 
	 //XML Payload wird in der Console ausgegeben
	    private static void jaxbObjectToXML(SongList list)
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
	    private static void jaxbObjectToJSON(SongList list)
	    {
	        try
	        {
	        	JAXBContext jaxbContext = JAXBContext.newInstance(SongList.class);
	            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	 
	            // To format JSON
	            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	             
	            //Set JSON type
	            jaxbMarshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
	            jaxbMarshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, true);
	 
	            //Print JSON String to Console
	            StringWriter sw = new StringWriter();
	            jaxbMarshaller.marshal(list, sw);
	            System.out.println(sw.toString());
	 
	        } catch (JAXBException e) {
	            e.printStackTrace();
	        }
	    }

}
