package de.htw.ai.kbe.database.interfaces;

import java.util.List;
import java.util.NoSuchElementException;

import de.htw.ai.kbe.bean.SongList;
import de.htw.ai.kbe.bean.User;

public interface ISongList {


    public List<SongList> getAllListsOfUser(User user);
    public SongList getListByIdAndUser(int listId, User user) throws NoSuchElementException;
    public void deleteSongList(SongList list) throws NoSuchElementException;
    public void saveSongList(SongList list);

}
