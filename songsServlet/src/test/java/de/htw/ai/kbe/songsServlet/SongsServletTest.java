package de.htw.ai.kbe.songsServlet;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;

public class SongsServletTest {

	private SongsServlet servlet;
    private MockServletConfig config;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    private final static String JSON_PATH = "songs.json";
    private final static String INIT_PARAM = "jsonFilePathComponent";
    
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
    public void initShouldSetDBComponentURI() {
    		assertEquals(JSON_PATH, servlet.getJsonFilePath());
    }

    @Test
    public void doGetAllShouldEchoParameters() throws ServletException, IOException {
    	request.addHeader("Accept", "application/json");
        request.addParameter("all", "");
       // request.addParameter("password", "tiger");
        
        servlet.doGet(request, response);

        String jsonResp = "[{\"title\":\"7 Years\",\"artist\":\"Lukas Graham\",\"album\":\"Lukas Graham (Blue Album)\",\"released\":2015,\"id\":1},{\"title\":\"Private Show\",\"artist\":\"Britney Spears\",\"album\":\"Glory\",\"released\":2016,\"id\":2},{\"title\":\"No\",\"artist\":\"Meghan Trainor\",\"album\":\"Thank You\",\"released\":2016,\"id\":3},{\"title\":\"i hate u, i love u\",\"artist\":\"Gnash\",\"album\":\"Top Hits 2017\",\"released\":2017,\"id\":4},{\"title\":\"I Took a Pill in Ibiza\",\"artist\":\"Mike Posner\",\"album\":\"At Night, Alone.\",\"released\":2016,\"id\":5},{\"title\":\"Bad Things\",\"artist\":\"Camila Cabello, Machine Gun Kelly\",\"album\":\"Bloom\",\"released\":2017,\"id\":6},{\"title\":\"Ghostbusters (I'm not a fraid)\",\"artist\":\"Fall Out Boy, Missy Elliott\",\"album\":\"Ghostbusters\",\"released\":2016,\"id\":7},{\"title\":\"Team\",\"artist\":\"Iggy Azalea\",\"album\":null,\"released\":2016,\"id\":8},{\"title\":\"Mom\",\"artist\":\"Meghan Trainor, Kelli Trainor\",\"album\":\"Thank You\",\"released\":2016,\"id\":9},{\"title\":\"Can't Stop the Feeling\",\"artist\":\"Justin Timberlake\",\"album\":\"Trolls\",\"released\":2016,\"id\":10}]";
       // String jsonResp ="[{\"title\":\"7 Years\",\"artist\":\"Lukas Graham\",\"album\":\"Lukas Graham (Blue Album)\",\"released\":2015,\"id\":1}]";
        assertEquals("application/json", response.getContentType());
        System.out.println("Ausgabe vom Testfall: ");
        System.out.println(response.getContentAsString());
        System.out.println(response.getContentAsString().contains(jsonResp));
        assertTrue(response.getContentAsString().contains((jsonResp)));
       // assertTrue(response.getContentAsString().contains("password=tiger"));
       // assertTrue(response.getContentAsString().contains(JSON_PATH));        
    }
    
    @Test
    public void doPostShouldEchoBody() throws ServletException, IOException {
        request.setContent("blablablabla".getBytes());
        servlet.doPost(request, response);
        assertEquals("text/plain", response.getContentType());
        assertTrue(response.getContentAsString().contains("blablablabla"));        
    }

}
