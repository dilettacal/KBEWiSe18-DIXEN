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

	@Override
	public void init(ServletConfig config) throws ServletException {

	}

	@Override
	public void init() throws ServletException {

	}

	//http://localhost:8080/songsServlet?all mit Accept-Header: * oder application/json oder ohne Accept-Header soll alle gespeicherten Songs in JSON-Format zurücksenden
	//http://localhost:8080/songsServlet?songId=6 mit Accept-Header: * oder application/json oder ohne Accept-Header soll den Song 6 in JSON-Format zurücksenden
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

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
