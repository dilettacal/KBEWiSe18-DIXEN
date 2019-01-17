package de.htw.ai.kbe.database.interfaces;

import java.util.Collection;
import java.util.List;

import de.htw.ai.kbe.bean.SongList;
import de.htw.ai.kbe.bean.User;

public interface ISongList {

	public List<SongList> getListByUser(User user);

	public SongList getListByIdAndUser(int listId, User user);

	public boolean deleteSongList(SongList list);

	public boolean saveSongList(SongList list);

	public Collection<SongList> findSongListByAccessType(User user, boolean isPublic);

	public SongList getSongListByID(int id);

}
