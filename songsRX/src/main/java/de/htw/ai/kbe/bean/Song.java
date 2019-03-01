package de.htw.ai.kbe.bean;

import javax.xml.bind.annotation.XmlElement;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO - represents a song in the Database
 */
@Entity
@Table(name = "songs")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="song")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Song implements Comparable<Song> {
	
	//Attribute nach Beleg 4 angepasst

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty("id")
	private int id;

	@NotNull
	@JsonProperty("title")
	private String title;
	@JsonProperty("artist")
	private String artist;
	@JsonProperty("album")
	private String album;
	@JsonProperty("released")
	private Integer released;

	@JsonIgnore
    @XmlTransient
    @ManyToMany(fetch = FetchType.EAGER, mappedBy="songs")
    private List<SongList> lists;

	
	public Song() {

	}

	private Song(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.artist = builder.artist;
        this.album = builder.album;
        this.released = builder.released;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public Integer getReleased() {
        return released;
    }

    public void setReleased(Integer released) {
        this.released = released;
    }
    
    public List<SongList> getLists() {
        return lists;
    }

    @Override
    public String toString() {
        return "Song [id=" + id + ", title=" + title + ", artist=" + artist + ", album=" + album + ", released="
                + released + "]";
    }

    public static class Builder {
        private Integer id;
        private String title;
        private String artist;
        private String album;
        private Integer released;

        public Builder(String title) {
            this.title = title;
        }

        public Builder id(Integer val) {
            id = val;
            return this;
        }
        
        public Builder artist(String val) {
            artist = val;
            return this;
        }

        public Builder album(String val) {
            album = val;
            return this;
        }

        public Builder released(Integer val) {
            released = val;
            return this;
        }

        public Song build() {
            return new Song(this);
        }
    }

	@Override
	public int compareTo(Song arg0) {
		return this.title.compareTo(arg0.title);
	}
	
	public void updateSong(Song newSong) {
		this.album = newSong.getAlbum();
		this.artist = newSong.getArtist();
		this.title = newSong.getTitle();
		this.released = newSong.getReleased();
	}

}