package de.htw.ai.kbe.songsServlet;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;

public class SongsServletTest {

	private SongsServlet servlet;
    private MockServletConfig config;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    private final static String JSON_PATH = "testsongs.json";
    private final static String INIT_PARAM = "jsonFilePathComponent";
    //Dasselbe WRONG_HEADER wie in SongsServlet.java
    private static final String WRONG_HEADER = "Bad Request. Akzeptiert werden nur Anfragen mit folgenden Header: \"application/json\" oder *, sowie Anfragen ohne Header";
    
    private static final String HOME_URL = "http://localhost:8080/songServlet";
    
    @Before
    public void setUp() throws ServletException {
    	servlet = new SongsServlet();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        config = new MockServletConfig();
        config.addInitParameter(INIT_PARAM, JSON_PATH);
        servlet.init(config); //throws ServletException
    }
    
    @Test
    public void initShouldSetJSONFilePath() {
    		assertEquals(JSON_PATH, servlet.getJsonFilePath());
    }

    @Test 
    public void doGetAllWithAcceptHeaderApplicationJSONShouldEchoParameters() throws ServletException, IOException {
    	request.addHeader("Accept", "application/json");
        request.addParameter("all", "");
        
        servlet.doGet(request, response);

        String jsonResp = "[{\"title\":\"7 Years\",\"artist\":\"Lukas Graham\",\"album\":\"Lukas Graham (Blue Album)\",\"released\":2015,\"id\":1},"
        		+ "{\"title\":\"Private Show\",\"artist\":\"Britney Spears\",\"album\":\"Glory\",\"released\":2016,\"id\":2},"
        		+ "{\"title\":\"No\",\"artist\":\"Meghan Trainor\",\"album\":\"Thank You\",\"released\":2016,\"id\":3},"
        		+ "{\"title\":\"i hate u, i love u\",\"artist\":\"Gnash\",\"album\":\"Top Hits 2017\",\"released\":2017,\"id\":4},"
        		+ "{\"title\":\"I Took a Pill in Ibiza\",\"artist\":\"Mike Posner\",\"album\":\"At Night, Alone.\",\"released\":2016,\"id\":5},"
        		+ "{\"title\":\"Bad Things\",\"artist\":\"Camila Cabello, Machine Gun Kelly\",\"album\":\"Bloom\",\"released\":2017,\"id\":6},"
        		+ "{\"title\":\"Ghostbusters (I'm not a fraid)\",\"artist\":\"Fall Out Boy, Missy Elliott\",\"album\":\"Ghostbusters\",\"released\":2016,\"id\":7},"
        		+ "{\"title\":\"Team\",\"artist\":\"Iggy Azalea\",\"album\":null,\"released\":2016,\"id\":8},"
        		+ "{\"title\":\"Mom\",\"artist\":\"Meghan Trainor, Kelli Trainor\",\"album\":\"Thank You\",\"released\":2016,\"id\":9},"
        		+ "{\"title\":\"Can't Stop the Feeling\",\"artist\":\"Justin Timberlake\",\"album\":\"Trolls\",\"released\":2016,\"id\":10}]";
        System.out.println("String: \n" + jsonResp);
        
        assertEquals("application/json", response.getContentType());
        
        System.out.println("Ausgabe vom Testfall: ");
        System.out.println(response.getContentAsString());
       // System.out.println(response.getContentAsString().contains(jsonResp));
        String databaseContentBeforePost = response.getContentAsString().substring(0, jsonResp.length()-1); //jsonResp.length()-1 so wird das letzte Komma entfernt
        databaseContentBeforePost += "]";
       // System.out.println("Substring:\n"+databaseContentBeforePost);
        assertTrue(databaseContentBeforePost.contains((jsonResp)));
    }
    
    @Test
    public void doGetAllWithAcceptHeaderStarShouldEchoParameters() throws ServletException, IOException {
    	request.addHeader("Accept", "*");
        request.addParameter("all", "");
        
        servlet.doGet(request, response);

        String jsonResp = "[{\"title\":\"7 Years\",\"artist\":\"Lukas Graham\",\"album\":\"Lukas Graham (Blue Album)\",\"released\":2015,\"id\":1},"
        		+ "{\"title\":\"Private Show\",\"artist\":\"Britney Spears\",\"album\":\"Glory\",\"released\":2016,\"id\":2},"
        		+ "{\"title\":\"No\",\"artist\":\"Meghan Trainor\",\"album\":\"Thank You\",\"released\":2016,\"id\":3},"
        		+ "{\"title\":\"i hate u, i love u\",\"artist\":\"Gnash\",\"album\":\"Top Hits 2017\",\"released\":2017,\"id\":4},"
        		+ "{\"title\":\"I Took a Pill in Ibiza\",\"artist\":\"Mike Posner\",\"album\":\"At Night, Alone.\",\"released\":2016,\"id\":5},"
        		+ "{\"title\":\"Bad Things\",\"artist\":\"Camila Cabello, Machine Gun Kelly\",\"album\":\"Bloom\",\"released\":2017,\"id\":6},"
        		+ "{\"title\":\"Ghostbusters (I'm not a fraid)\",\"artist\":\"Fall Out Boy, Missy Elliott\",\"album\":\"Ghostbusters\",\"released\":2016,\"id\":7},"
        		+ "{\"title\":\"Team\",\"artist\":\"Iggy Azalea\",\"album\":null,\"released\":2016,\"id\":8},"
        		+ "{\"title\":\"Mom\",\"artist\":\"Meghan Trainor, Kelli Trainor\",\"album\":\"Thank You\",\"released\":2016,\"id\":9},"
        		+ "{\"title\":\"Can't Stop the Feeling\",\"artist\":\"Justin Timberlake\",\"album\":\"Trolls\",\"released\":2016,\"id\":10}]";
        assertEquals("application/json", response.getContentType());
        System.out.println("Ausgabe vom Testfall: ");
        System.out.println(response.getContentAsString());
        System.out.println(response.getContentAsString().contains(jsonResp));
        assertTrue(response.getContentAsString().contains((jsonResp)));
    }
    
    @Test
    public void doGetAllWithAcceptHeaderShouldNotEchoParametersWithHTTPStatusCode() throws ServletException, IOException {
    	request.addHeader("Accept", "bla");
        request.addParameter("all", "");
        
        servlet.doGet(request, response);
        
        assertEquals(response.getStatus(), 400);
        assertEquals(response.getErrorMessage(), WRONG_HEADER);
    }
    
    @Test
    public void doGetAllWithoutAcceptHeaderShouldEchoParameters() throws ServletException, IOException {
    	//request.addHeader("Accept", "*");
        request.addParameter("all", "");
        
        servlet.doGet(request, response);

        String jsonResp = "[{\"title\":\"7 Years\",\"artist\":\"Lukas Graham\",\"album\":\"Lukas Graham (Blue Album)\",\"released\":2015,\"id\":1},"
        		+ "{\"title\":\"Private Show\",\"artist\":\"Britney Spears\",\"album\":\"Glory\",\"released\":2016,\"id\":2},"
        		+ "{\"title\":\"No\",\"artist\":\"Meghan Trainor\",\"album\":\"Thank You\",\"released\":2016,\"id\":3},"
        		+ "{\"title\":\"i hate u, i love u\",\"artist\":\"Gnash\",\"album\":\"Top Hits 2017\",\"released\":2017,\"id\":4},"
        		+ "{\"title\":\"I Took a Pill in Ibiza\",\"artist\":\"Mike Posner\",\"album\":\"At Night, Alone.\",\"released\":2016,\"id\":5},"
        		+ "{\"title\":\"Bad Things\",\"artist\":\"Camila Cabello, Machine Gun Kelly\",\"album\":\"Bloom\",\"released\":2017,\"id\":6},"
        		+ "{\"title\":\"Ghostbusters (I'm not a fraid)\",\"artist\":\"Fall Out Boy, Missy Elliott\",\"album\":\"Ghostbusters\",\"released\":2016,\"id\":7},"
        		+ "{\"title\":\"Team\",\"artist\":\"Iggy Azalea\",\"album\":null,\"released\":2016,\"id\":8},"
        		+ "{\"title\":\"Mom\",\"artist\":\"Meghan Trainor, Kelli Trainor\",\"album\":\"Thank You\",\"released\":2016,\"id\":9},"
        		+ "{\"title\":\"Can't Stop the Feeling\",\"artist\":\"Justin Timberlake\",\"album\":\"Trolls\",\"released\":2016,\"id\":10}]";
        assertEquals("application/json", response.getContentType());
        System.out.println("Ausgabe vom Testfall: ");
        System.out.println(response.getContentAsString());
        System.out.println(response.getContentAsString().contains(jsonResp));
        assertTrue(response.getContentAsString().contains((jsonResp)));
    }
    
    @Test
    public void doGetSongWithSongIDWithAcceptHeaderApplicationJSONShouldEchoSong() throws ServletException, IOException {
    	request.addHeader("Accept", "application/json");
        request.addParameter("songId", String.valueOf(1));
       // request.addParameter("password", "tiger");
        
        servlet.doGet(request, response);

       // String jsonResp = "[{\"title\":\"7 Years\",\"artist\":\"Lukas Graham\",\"album\":\"Lukas Graham (Blue Album)\",\"released\":2015,\"id\":1},{\"title\":\"Private Show\",\"artist\":\"Britney Spears\",\"album\":\"Glory\",\"released\":2016,\"id\":2},{\"title\":\"No\",\"artist\":\"Meghan Trainor\",\"album\":\"Thank You\",\"released\":2016,\"id\":3},{\"title\":\"i hate u, i love u\",\"artist\":\"Gnash\",\"album\":\"Top Hits 2017\",\"released\":2017,\"id\":4},{\"title\":\"I Took a Pill in Ibiza\",\"artist\":\"Mike Posner\",\"album\":\"At Night, Alone.\",\"released\":2016,\"id\":5},{\"title\":\"Bad Things\",\"artist\":\"Camila Cabello, Machine Gun Kelly\",\"album\":\"Bloom\",\"released\":2017,\"id\":6},{\"title\":\"Ghostbusters (I'm not a fraid)\",\"artist\":\"Fall Out Boy, Missy Elliott\",\"album\":\"Ghostbusters\",\"released\":2016,\"id\":7},{\"title\":\"Team\",\"artist\":\"Iggy Azalea\",\"album\":null,\"released\":2016,\"id\":8},{\"title\":\"Mom\",\"artist\":\"Meghan Trainor, Kelli Trainor\",\"album\":\"Thank You\",\"released\":2016,\"id\":9},{\"title\":\"Can't Stop the Feeling\",\"artist\":\"Justin Timberlake\",\"album\":\"Trolls\",\"released\":2016,\"id\":10}]";
        String jsonResp ="{\"title\":\"7 Years\",\"artist\":\"Lukas Graham\",\"album\":\"Lukas Graham (Blue Album)\",\"released\":2015,\"id\":1}";
        System.out.println("JSON Resp:");
        System.out.println(jsonResp);
        assertEquals("application/json", response.getContentType());
        assertTrue(response.getContentAsString().contains((jsonResp)));
       // assertTrue(response.getContentAsString().contains("password=tiger"));
       // assertTrue(response.getContentAsString().contains(JSON_PATH));        
    }
    
    @Test
    public void doGetSongWithIDWithAcceptHeaderStarShouldEchoSong() throws ServletException, IOException {
    	request.addHeader("Accept", "*");
        request.addParameter("songId", String.valueOf(10));
        
        servlet.doGet(request, response);
        
        String jsonResp ="{\"title\":\"Can't Stop the Feeling\",\"artist\":\"Justin Timberlake\",\"album\":\"Trolls\",\"released\":2016,\"id\":10}";
        System.out.println("JSON Resp:");
        System.out.println(jsonResp);
        assertEquals("application/json", response.getContentType());
        assertTrue(response.getContentAsString().contains((jsonResp)));
    }
    
    @Test
    public void doGetSongWithIDWithAcceptHeaderShouldNotEchoSongWithHTTPStatusCode() throws ServletException, IOException {
    	request.addHeader("Accept", "bla");
        request.addParameter("songId", String.valueOf(9));
        
        servlet.doGet(request, response);
        
        assertEquals(response.getStatus(), 400);
        assertEquals(response.getErrorMessage(), WRONG_HEADER);
    }
    
    @Test
    public void doGetSongWithIDWithoutAcceptHeaderShouldEchoSong() throws ServletException, IOException {
    	//request.addHeader("Accept", "*");
        request.addParameter("songId", String.valueOf(5));
        
        servlet.doGet(request, response);
        
        String jsonResp ="{\"title\":\"I Took a Pill in Ibiza\",\"artist\":\"Mike Posner\",\"album\":\"At Night, Alone.\",\"released\":2016,\"id\":5}";
        System.out.println("JSON Resp:");
        System.out.println(jsonResp);
        assertEquals("application/json", response.getContentType());
        assertTrue(response.getContentAsString().contains((jsonResp)));
    }
    
    //*** Anforderungen fuer POST
/*
 * Soll eine neue Id fuer den neuen Song generieren und den Song in der “DB” speichern. 
 * Die neue Id des Songs soll an den Client als Wert des “Location”-Header: http://localhost:8080/songsServlet?songId=newId der Response zurückschicken. 
 * Der ResponseBody ist leer ==> keine Plaintext-Antwort
 * POST akzeptiert nur JSON payloads (== Body)
 * 
 * Aus SongServlet.java - Aufbau locationURL:
 * String reqURL = req.getRequestURL().toString();//http://localhost:8080/songServlet/
	String respURL = reqURL.substring(0, reqURL.length()-1); //http://localhost:8080/songServlet
	String location = respURL+ "?" + SONGID + "=" +song.getId();
 */
   //doPostShouldEchoSongID() --> doPostNewSongShouldSetNewLocationInHeader 
    
    //in diesem Test kam die fehlerhafte URL zurueck
    @Test
    public void doPostNewSongShouldSetNewLocationInHeader() throws ServletException, IOException {
    	//TODO: Check 'Location'-Header
    	request.addHeader("Accept", "application/json");
    	request.setContentType("application/json");
    	String testContent ="{\"title\":\"Heroes\",\"artist\":\"David Bowie\",\"album\":\"Heroes\",\"released\":1977}";
        request.setContent(testContent.getBytes());
        servlet.doPost(request, response);
        System.out.println("Print: " + response.getContentAsString());
        assertNull(response.getContentType());
        assertTrue(servlet.getDatabase().isSongStored(11));
        System.out.println(response.getHeader("Location"));
        assertEquals(response.getHeader("Location"), "http://localhost:8080/songsServlet?songId=11");
        //assertTrue(response.getContentAsString().contains("11"));
        //assertTrue(response.getContentAsString().contains(testContent));    
        
    }
    
    //schlaegt fehlt wenn gesamte Testklasse ausgeführt, weil vorher neuer Song hinzugefuegt wurde und dementsprechen Song mit ID 11 existiert
    @Test
    public void doPostShouldNotEchoSongIDNoSongTitle() throws ServletException, IOException {
    	request.addHeader("Accept", "application/json");
    	request.setContentType("application/json");
    	String testContent ="{\"title\":\"\",\"artist\":\"Lukas Graham\",\"album\":\"Lukas Graham (Blue Album)\",\"released\":2015}";
        request.setContent(testContent.getBytes());
        servlet.doPost(request, response);
        //System.out.println("HIER: " + servlet.getDatabase().getSong(11).getTitle());
        assertEquals(response.getStatus(), 400);
        assertNull(response.getContentType());
        //assertFalse(servlet.getDatabase().isSongStored(11));
        if(servlet.getDatabase().isSongStored(11)) {
        	assertFalse(servlet.getDatabase().getSong(11).getTitle().equals(""));
        	assertFalse(servlet.getDatabase().getSong(11).getArtist().equals("Lukas Graham"));
        }
        if(servlet.getDatabase().isSongStored(12)) {
        	assertFalse(servlet.getDatabase().getSong(12).getTitle().equals(""));
        	assertFalse(servlet.getDatabase().getSong(12).getArtist().equals("Lukas Graham"));
        }
        
        //assertEquals("text/plain", response.getContentType());
        //assertTrue(response.getContentAsString().contains("null"));
    }
    
    @Test
    public void doPostShouldNotEchoSongIDNoContentType() throws ServletException, IOException {
    	request.addHeader("Accept", "application/json");
    	String testContent ="{\"title\":\"Money\",\"artist\":\"Pink Floyd\",\"album\":\"Dark Side of the Moon\",\"released\":1973}";
        request.setContent(testContent.getBytes());
        servlet.doPost(request, response);
        System.out.println("Print: " + response.getContentAsString());
        assertEquals(response.getStatus(), 400);
        assertNull(response.getContentType());
        assertFalse(servlet.getDatabase().isSongStored(11));
        //assertEquals("text/plain", response.getContentType());
        //assertTrue(response.getContentAsString().contains("null"));
    }
    
    @Test
    public void doPostShouldNotEchoSongIDSongAlreadyInDB() throws ServletException, IOException {
    	request.addHeader("Accept", "application/json");
    	request.setContentType("application/json");
    	String testContent ="{\"title\":\"7 Years\",\"artist\":\"Lukas Graham\",\"album\":\"Lukas Graham (Blue Album)\",\"released\":2015}";
        request.setContent(testContent.getBytes());
        servlet.doPost(request, response);
        System.out.println("Print: " + response.getContentAsString());
        assertNull(response.getContentType());
        assertFalse(servlet.getDatabase().isSongStored(11));
        assertEquals(servlet.getDatabase().getSong(1).getTitle(), "7 Years");
        assertEquals(servlet.getDatabase().getSong(1).getArtist(), "Lukas Graham");
        assertEquals(servlet.getDatabase().getSong(1).getAlbum(), "Lukas Graham (Blue Album)");
    }
    
    @Ignore
    @Test 
    //Aufruf der Methode - Das ist nur fuer uns
    public void destroy() throws ServletException, IOException {
    	servlet.destroy();
    }

}
