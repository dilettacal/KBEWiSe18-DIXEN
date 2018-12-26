package de.htw.ai.kbe.songsServlet;

/**
 * POJO - represents a song in the Database
 */
public class Song {

	//Titel MUSS kommen
	private String title;
	//restliche Attribute nicht wichtig, Bsp. Volkslieder
	private String artist;
	private String album;
	private int released;
	private Integer id;

	public Song(String title, String artist, String album, int released, Integer id) {
		if(title != null) {
			this.title = title;
			this.artist = artist;
			this.album = album;
			this.released = released;
			this.id = id;
		}
		//TODO: Sich was ausdenken fuer den Fall, dass title eigenltich null ist
		//Ansatz: eigene Exception werfen
	}

	public Song() {

	}

	public Integer getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getArtist() {
		return artist;
	}

	public String getAlbum() {
		return album;
	}

	public int getReleased() {
		return released;
	}

	@Override
	public String toString() {
		return "Song [id=" + id + ", title=" + title + ", artist=" + artist + ", album=" + album + ", released="
				+ released + "]";
	}

	public void setId(Integer id) {
		this.id = id;
	}

}