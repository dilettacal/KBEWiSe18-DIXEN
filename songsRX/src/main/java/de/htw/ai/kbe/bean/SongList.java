package de.htw.ai.kbe.bean;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "songlists")
@XmlRootElement(name = "songList") 
@XmlAccessorType(XmlAccessType.FIELD)
public class SongList {
	/*
	 * Unrecognized field "songList" (class de.htw.ai.kbe.bean.SongList), not marked as ignorable (4 known properties: "id", "songs", "isPublic", "owner"])
 at [Source: org.glassfish.jersey.message.internal.ReaderInterceptorExecutor$UnCloseableInputStream@325091c8; line: 2, column: 18] (through reference chain: de.htw.ai.kbe.bean.SongList["songList"])
 	Solution: @XmlRootElement(name = "songList") 
	 */
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty("id")
    private int id;

    @Column(name = "public", nullable = false)
    @JsonProperty("isPublic")
    private boolean isPublic = false;

    @ManyToOne
    @JsonProperty("owner")
    private User owner;

    @ManyToMany(fetch = FetchType.EAGER) //solves problem of lazy list creation
    @JoinTable(name = "listSongRelation", joinColumns = { @JoinColumn(name = "list_id") }, inverseJoinColumns = {
            @JoinColumn(name = "song_id") })
    @XmlElementWrapper(name = "songs")
    @XmlElement(name = "song")
    @JsonProperty(value = "songs")
    private List<Song> songs;

    public SongList() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

}
