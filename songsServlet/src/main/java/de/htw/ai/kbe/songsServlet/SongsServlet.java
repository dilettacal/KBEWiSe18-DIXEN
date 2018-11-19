package de.htw.ai.kbe.songsServlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

}
