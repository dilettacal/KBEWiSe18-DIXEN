package de.htw.ai.kbe.songsServlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

//import org.json.JSONObject;

/**
 * Manages HTTP requests and responses (and JSON parsing)
 * 
 * @author dixen 
 *
 */
public class SongsServlet extends HttpServlet {
	
	//Parameters for requests and responses
	private static final String APP_JSON = "application/json"; //accept header
	private static final String ALL_PARAM = "all";
	private static final String SONGID = "songId";
	private static final String TEXT_PLAIN = "text/plain";
	//Responses to Client
	private static final String WRONG_PARAMS = "Eingegebene Parameter nicht gueltig. Bitte entweder ?all oder ?songId=1";
	private static final String SONG_NOT_AVAILABLE = "Achtung, Song mit Id %d nicht vorhanden";
	private static final String EMPTY_VALUE = "Achtung, Sie heben keinen Wert fuer Parameter %s eingegeben. Ueberpruefen Sie bitte Ihre Eingabe";
	private static final String WRONG_FORMAT = "Format des Parameters %s ist moeglicherweise falsch. Die Anfrage konnte nicht verarbeitet werden";
	
	private static final int NOT_FOUND = 404;
	private static final int NOT_ACCEPTABLE = 406;
	private static final int BAD_REQUEST = 400;
	private static final String WRONG_HEADER = "Bad Request. Akzeptiert werden nur Anfragen mit folgenden Header: \"application/json\" oder *, sowie Anfragen ohne Header";
	//Objects needed
	private Songs database;
	
	//Path to file
	private final String pathToFile = "songs.json"; //Das muss waehrend der Abgabe angepasst werden, da die Datei sich im Projekt nicht befindet!
	
	private String jsonFilePath = null;
	
	

	public Songs getDatabase() {
		return database;
	}

	
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
		//0. Check header Accept
		String acceptRequest = req.getHeader("Accept");
		
		//1. Response-Typ: Nur json Format erlaubt
		resp.setContentType(APP_JSON); 
		if(acceptRequest == null || acceptRequest.contains(APP_JSON) || acceptRequest.contains("*")) {
			//2. Anfragetyp: -1 == all, Zahl == bestimmter Song-ID
			Integer id  = -1;
			try (PrintWriter out = resp.getWriter()) {
				if (req.getParameter("songId") != null) {
					try {
						id = Integer.parseInt(req.getParameter("songId"));
						responseToClient(out, id, req, resp);
					} catch(NumberFormatException e ) {
						//NumberFormatException tritt auf, wenn Wert keine Zahl ist, z.B. String 'ahahah', leere Zeichenkette...
						//Kein ID uebergeben
						if (req.getParameter("songId").isEmpty()) {
							resp.sendError(BAD_REQUEST,String.format(EMPTY_VALUE, "songId") );
							//out.println(String.format(EMPTY_VALUE, "songId"));
						}
						//Falsches Format oder andere moegliche Ursachen
						else {
							resp.sendError(BAD_REQUEST,String.format(WRONG_FORMAT, "songId") );
							//out.println(String.format(WRONG_FORMAT, "songId"));
						}				
					}
					
					
				}			
				else if (req.getParameter("all") != null) {
					responseToClient(out,id, req, resp);
				}
				else {
					out.println(WRONG_PARAMS);
				}
					
			}
		} else {
			//Zum Beispiel wenn Accept Header xml fordert oder nicht gueltig ist
			resp.sendError(BAD_REQUEST, WRONG_HEADER);
		}
		
	}
		

	
	private void responseToClient(PrintWriter out, Integer id, HttpServletRequest req, HttpServletResponse resp) throws JsonProcessingException {
		ObjectMapper objMap = new ObjectMapper();
		if(id == -1) {
			//Ausgabe aller Songs
			out.println(objMap.writeValueAsString(database.getAllSongs()));
		} else if(database.isSongStored(id)){
			//Suche nach einem bestimmten Song + Fehlermeldung im Fall, dass der Song nicht in DB
			out.println(objMap.writeValueAsString(database.getSong(id)));
		} else {
			try {
				resp.sendError(NOT_FOUND, String.format(SONG_NOT_AVAILABLE, id));
				//out.println(String.format(SONG_NOT_AVAILABLE, id));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	//http://localhost:8080/songsServlet mit Payload soll eine neue ID fuer den neuen Song generieren und den Song in der DB speichern
	//neue ID an den Client als Wert des "Location"-Header: http://localhost:8080/songsServlet?songId=newId der Response zurueckschicken (Body ist leer)
	//akzeptiert nur JSON payloads
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//Hinweis aus Folien:
		String contentType = req.getContentType();
		
		//TODO: POST akzeptiert nur JSON payloads - ERLEDIGT
		if(contentType != null && contentType.equals(APP_JSON)) {

			//resp.setContentType(TEXT_PLAIN);
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
				String title = song.getTitle();
				if(title == null || title.isEmpty()) {
					//TODO: Problem mit Titel - ERLEDIGT
					//response.sendError(400, "Bad Request" );
					resp.sendError(BAD_REQUEST, "Ein Titel ist notwendig. Bitte geben Sie den Titel ein");
				} else {
					database.addSong(song);					
					//TODO: 
					/*
					 *  Die neue Id des Songs soll an den Client als Wert des “Location”-Header: http://localhost:8080/songsServlet?songId=newId der Response zurückschicken. 
					 *  Der ResponseBody ist leer. 
					 */
					//ERLEDIGT
					
					//Zum Testen in der Console:
					//System.out.println("Song in der DB gespeichert mit ID: " + song.getId());
					
					
					/* TODO: Codeteil verursacht fehlerhafte URL in Testmethode
					 * 
					//produziert Ausgabe: http://localhost
					String reqURL = req.getRequestURL().toString();//http://localhost:8080/songServlet/
					//produziert Ausgabe: http://localhos
					String respURL = reqURL.substring(0, reqURL.length()-1); //http://localhost:8080/songServlet
					//produziert Ausgabe: http://localhos?songId=11
					String location = respURL+ "?" + SONGID + "=" +song.getId();
					System.out.println(location);
					resp.setHeader("Location", location); //Header: Location wird gesetzt --> In Postman nachvollziehbar
					//System.out.println(location);
					*/
					resp.setHeader("Location", "http://localhost:8080/songsServlet?songId="+song.getId());
				}
				
			}
		} else {
			resp.sendError(BAD_REQUEST, "Nur JSON Payload ist akzeptiert.");
		}
		
	}

	@Override
	public void destroy() {
		//TODO: Content from Hashmap should be transferred to songs.json
		Collection<Song> values = database.getAllSongs();
		List<Song> allSongs = new ArrayList<Song>(values);
		//1. Auf Datei songs.json zugreifen
		try {
			//2. Datei von Hashmap zu  songs.json schreiben
			SongsServlet.writeSongsToJSON(allSongs, this.jsonFilePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
			try (OutputStream os = new BufferedOutputStream(new FileOutputStream(filename))) {
				objectMapper.writeValue(os, songs);
				System.out.println("Completed!");
			}
		}
		
		protected String getJsonFilePath () {
			return this.jsonFilePath;
		}
}
