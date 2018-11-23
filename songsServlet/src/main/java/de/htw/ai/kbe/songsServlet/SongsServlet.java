package de.htw.ai.kbe.songsServlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.List;

//import org.json.JSONObject;

/**
 * Manages HTTP requests and responses (and JSON parsing)
 * 
 * @author dixen 
 *
 */
public class SongsServlet extends HttpServlet {
	
	//Parameters for requests and responses
	private final String APP_JSON = "application/json"; //accept header
	private final String ALL_PARAM = "all";
	private final String SONGID = "songId";
	private final String RESPONSE_TYPE = "text/plain";
	
	//Objects needed
	private Songs database;
	
	//Path to file
	private final String pathToFile = "songs.json"; //Das muss waehrend der Abgabe angepasst werden, da die Datei sich im Projekt nicht befindet!
	
	private String jsonFilePath = null;


	@Override
	public void init(ServletConfig config) throws ServletException {
		this.jsonFilePath = config.getInitParameter("jsonFilePathComponent");
		
		//1. Read songs from json file - List<Song>
		boolean readSuccessful = false;
		List<Song> songsFromJSON = null;
		
		try {
			songsFromJSON= SongsServlet.readJSONToSongs(this.jsonFilePath);
			readSuccessful = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(readSuccessful){
			//Jetzt wurde eine Liste von Song erstellt und sie muss der in-Memory DB hinzugefuegt werden
			database = Songs.getInstance(songsFromJSON);
		} else {
			//TODO: Problem melden
			System.out.println("Ein Fehler ist aufgetreten.");
		}
			

	}

	@Override
	public void init() throws ServletException {

	}

	//http://localhost:8080/songsServlet?all mit Accept-Header: * oder application/json oder ohne Accept-Header soll alle gespeicherten Songs in JSON-Format zuruecksenden
	//http://localhost:8080/songsServlet?songId=6 mit Accept-Header: * oder application/json oder ohne Accept-Header soll den Song 6 in JSON-Format zuruecksenden
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		
		//1. Header "Accept" auslesen
		String acceptRequest = req.getHeader("Accept");
		
		//2. Check ob Header APP_JSON enthaelt - Wir behandeln nur JSON Requests
		//Fall "Accept"-Header == application/json
		if(acceptRequest == null || acceptRequest.contains(APP_JSON) || acceptRequest.contains("*")){
			//3. Aus einer Request koennen sowohl Parameternamen (all, songId), 
			//als auch Parameterwerte - Methode: getParameterNames()
			Enumeration<String> paramsNames = req.getParameterNames();
			String actualParam = "";
			//Iterieren ueber die Parameternamen
			while(paramsNames.hasMoreElements()){
				//Rufe aktuell iteriertes Element auf:
				actualParam = paramsNames.nextElement();
				
				//Auslesen der Parameterwerte all
				if(actualParam.equals(ALL_PARAM)){
					String response = ""; //Vorbereitung der Antwort
					//Typ der Antwort festlegen
					resp.setContentType(APP_JSON);
					resp.setCharacterEncoding("UTF8");
					
					
					System.out.println("ALL gelesen");
					database.getAllSongs().forEach(s -> System.out.println(s));
					System.out.println("**************************************");
					System.out.println("JSON string:");
										
					ObjectMapper objectMapper = new ObjectMapper();
					String arrayToJson = objectMapper.writeValueAsString(database.getAllSongs());
					System.out.println(arrayToJson);
					try (PrintWriter out = resp.getWriter()){
						out.println(arrayToJson);
					}

					//Iterieren durch alle Songs in der DB und sie als JSON zueruckgeben
				}
				//songId
				else if(actualParam.equals(SONGID)){
					//SongID Wert aus Request auslesen
					Integer songID = Integer.valueOf(req.getParameter(SONGID));
					Song song = database.getSong(songID);
					
					//Typ der Antwort festlegen
					resp.setContentType(APP_JSON);
					resp.setCharacterEncoding("UTF8");
					
					ObjectMapper objectMapper = new ObjectMapper();
					String objToJson = objectMapper.writeValueAsString(song);
					System.out.println("JSON - Song mit Object Mapper:");
					System.out.println(objToJson);
					try (PrintWriter out = resp.getWriter()){
						out.println(objToJson);
					}
					
					//TODO: Moegliche Faelle abdecken:
					//1. Song existiert und wird zurueckgegeben (PrintWriter) - ERLEDIGT
					
					//2. Song existiert nicht oder Request ist schlecht aufgebaut
					//Request-Analyse in einer separaten Methode eventuell behandeln
					//Kein Titel, Leere Autoren usw.
				}
			}	

		}
		//TODO: Was passiert hier, wenn es keinen Accept-Header gibt?
		else {
			resp.sendError(400, "Bad Request" );
		}
		
	}

	//http://localhost:8080/songsServlet mit Payload soll eine neue ID fuer den neuen Song generieren und den Song in der DB speichern
	//neue ID an den Client als Wert des "Location"-Header: http://localhost:8080/songsServlet?songId=newId der Response zurueckschicken (Body ist leer)
	//akzeptiert nur JSON payloads
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//Hinweis aus Folien:
		/* Bei der Abarbeitung von POST und PUT mit request.getContentType() 
		den MIME-Typ des Request-Bodys checken */
		
		resp.setContentType("text/plain");
		ServletInputStream inputStream = req.getInputStream();
		byte[] inBytes = IOUtils.toByteArray(inputStream);
		String s = new String(inBytes);
		System.out.println("String aus Bytearray: "+s);
		
		ObjectMapper objectMapper = new ObjectMapper();
		Song song = null;
		try (PrintWriter out = resp.getWriter()) {
			//Hier ID Ausgabe
			//out.println(new String(inBytes));
			song = objectMapper.readValue(s, Song.class);
			database.addSong(song);		
			//TODO: Was wenn der Titel null ist? momentan wird einfach null zurueckgegeben, man koennte auch ein HTTP Status Code ausgeben
			//response.sendError(400, "Bad Request" );
			//Ausgabe fuer Client
			out.println(song.getId());
		}
	}

	@Override
	public void destroy() {
		//TODO: Content from Hashmap should be transferred to songs.json
		List<Song> allSongs = (List<Song>) database.getAllSongs();
		//1. Auf Datei songs.json zugreifen
		try {
			SongsServlet.writeSongsToJSON(allSongs, "output.json");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//2. Datei von Hashmap zu  songs.json schreiben

	}

	
	//Methoden aus jaxJacksonExample
	// Reads a list of songs from a JSON-file into List<Song>
		@SuppressWarnings("unchecked")
		static List<Song> readJSONToSongs(String filename) throws FileNotFoundException, IOException {
			//TODO: Wir bekommen ein ServletInputStream!
			ObjectMapper objectMapper = new ObjectMapper();
			try (InputStream is = new BufferedInputStream(new FileInputStream(filename))) {
				return (List<Song>) objectMapper.readValue(is, new TypeReference<List<Song>>(){});
			}
		}

		// Write a List<Song> to a JSON-file
		static void writeSongsToJSON(List<Song> songs, String filename) throws FileNotFoundException, IOException {
			ObjectMapper objectMapper = new ObjectMapper();
			//TODO: Wir haben ein ServletOutputStream!
			try (OutputStream os = new BufferedOutputStream(new FileOutputStream(filename))) {
				objectMapper.writeValue(os, songs);
			}
		}
		
		protected String getJsonFilePath () {
			return this.jsonFilePath;
		}
}
