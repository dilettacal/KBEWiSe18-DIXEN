package de.htw.ai.kbe.database.interfaces;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import de.htw.ai.kbe.bean.SongList;
import de.htw.ai.kbe.bean.User;

/**
 * Interface fuer SongList Entitaeten
 * @author dc
 *
 */
public interface ISongList {


    public List<SongList> getAllListsOfUser(User user);
    public SongList getListByIdAndUser(int listId, User user) throws NoSuchElementException;
    public boolean deleteSongList(SongList list) throws NoSuchElementException;
    public boolean saveSongList(SongList list);
    public SongList getSongListByID(int id);
    public Collection<SongList> findSongListByAccessType(User user, boolean isPublic);

}
