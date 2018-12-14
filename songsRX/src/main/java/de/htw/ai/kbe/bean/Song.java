package de.htw.ai.kbe.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO - represents a song in the Database
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "song")
public class Song implements Comparable<Song>  {

	// Titel MUSS kommen
	private String title;
	// restliche Attribute nicht wichtig, Bsp. Volkslieder
	private String artist;
	private String album;
	private int released;
	private Integer id;
	
	public Song() {
		
	}

	public static class Builder {
		private String builderTitle;
		private String builderArtist;
		private String builderAlbum;
		private int builderReleased;
		private Integer builderId;

		public Builder(String title) {
			this.builderTitle = title;
		}

		public Builder artist(String val) {
			builderArtist = val;
			return this;
		}

		public Builder album(String val) {
			builderAlbum = val;
			return this;
		}

		public Builder released(int val) {
			builderReleased = val;
			return this;
		}

		public Builder id(Integer val) {
			builderId = val;
			return this;
		}
		
		public Song build() {
			return new Song(this);
		}

	}

	private Song(Builder builder) {
		this.title = builder.builderTitle;
		this.artist = builder.builderArtist;
		this.album = builder.builderAlbum;
		this.released = builder.builderReleased;
		this.id = builder.builderId;
	}

	@XmlElement
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@XmlElement
	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	@XmlElement
	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	@XmlElement
	public int getReleased() {
		return released;
	}

	public void setReleased(int released) {
		this.released = released;
	}

	@XmlElement
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Song [id=" + id + ", title=" + title + ", artist=" + artist + ", album=" + album + ", released="
				+ released + "]";
	}

	@Override
	public int compareTo(Song o) {
		return this.getId().compareTo(o.getId());
	}

}