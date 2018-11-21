package de.htw.ai.kbe.songsServlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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


	@Override
	public void init(ServletConfig config) throws ServletException {
		//1. Read songs from json file - List<Song>
		boolean readSuccessful = false;
		List<Song> songsFromJSON = null;
		
		try {
			//TODO: Das habe ich nicht getestet!
			songsFromJSON= SongsServlet.readJSONToSongs("songs.json");
			//Test- Ausgabe TODO: Remove before last commit to repo
			songsFromJSON.forEach(s -> System.out.println(s));
			readSuccessful = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(readSuccessful){
			//Jetzt wurde eine Liste von Song erstellt und sie muss der in-Memory DB hinzugefuegt werden
			database = Songs.getInstance(songsFromJSON);
		}
			

	}

	@Override
	public void init() throws ServletException {

	}

	//http://localhost:8080/songsServlet?all mit Accept-Header: * oder application/json oder ohne Accept-Header soll alle gespeicherten Songs in JSON-Format zurücksenden
	//http://localhost:8080/songsServlet?songId=6 mit Accept-Header: * oder application/json oder ohne Accept-Header soll den Song 6 in JSON-Format zurücksenden
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		//1. Header "Accept" auslesen
		String acceptRequest = req.getHeader("Accept");
		
		//2. Check ob Header APP_JSON enthaelt - Wir behandeln nur JSON Requests
		//Fall "Accept"-Header == application/json
		if(acceptRequest.contains("application/json")){
			//3. Aus einer Request koennen sowohl Parameternamen (all, songId), 
			//als auch Parameterwerte - Methode: getParameterNames()
			Enumeration<String> paramsNames = req.getParameterNames();
			String actualParam = "";
			//Iterieren ueber die Parameternamen
			while(paramsNames.hasMoreElements()){
				//Rufe aktuell iteriertes Element auf:
				actualParam = paramsNames.nextElement();
				
				//Auslesen der Parameterwerte
				if(actualParam.equals(ALL_PARAM)){
					String response = ""; //Vorbereitung der Antwort
					//Typ der Antwort festlegen
					resp.setContentType(APP_JSON);

					//Iterieren durch alle Songs in der DB und sie als JSON zueruckgeben
				}
				else if(actualParam.equals(SONGID)){
					//Song ID auslesen
					Integer songID = Integer.valueOf(req.getParameter(SONGID));

					//TODO: Song mit songID aus DB aufrufen und sie als JSON zurueckgeben
					
					//TODO: Moegliche Faelle abdecken:
					//1. Song existiert und wird zurueckgegeben
					//TODO: Klaeren, wie das ausgegeben werden soll: PrintWriter oder Syso?
					
					//2. Song existiert nicht oder Request ist schlecht aufgebaut
					//Request-Analyse in einer separaten Methode eventuell behandeln
					//Kein Titel, Leere Autoren usw.
				}
				
			}	
			
			
		}	
		
		
		
		

	}

	//http://localhost:8080/songsServlet mit Payload soll eine neue ID fuer den neuen Song generieren und den Song in der DB speichern
	//neue ID an den Client als Wert des "Location"-Header: http://localhost:8080/songsServlet?songId=newId der Response zurückschicken (Body ist leer)
	//akzeptiert nur JSON payloads
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	}

	@Override
	public void destroy() {
		//TODO: Content from Hashmap should be transferred to songs.json

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
}
