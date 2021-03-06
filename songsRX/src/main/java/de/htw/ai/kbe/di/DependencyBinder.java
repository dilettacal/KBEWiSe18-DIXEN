package de.htw.ai.kbe.di;

import javax.inject.Singleton;
import javax.persistence.Persistence;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import javax.persistence.EntityManagerFactory;

import de.htw.ai.kbe.database.dao.SongDAO;
import de.htw.ai.kbe.database.dao.SongListDAO;
import de.htw.ai.kbe.database.dao.TokenDAO;
import de.htw.ai.kbe.database.dao.UserDAO;
import de.htw.ai.kbe.database.interfaces.ISongList;
import de.htw.ai.kbe.database.interfaces.ISongs;
import de.htw.ai.kbe.database.interfaces.IToken;
import de.htw.ai.kbe.database.interfaces.IUser;
import de.htw.ai.kbe.filter.AuthTokenDB;
import de.htw.ai.kbe.filter.AuthTokenStorage;
import de.htw.ai.kbe.filter.AuthenticationFilter;
import de.htw.ai.kbe.filter.IAuth;
import de.htw.ai.kbe.storage.SongsStorage;
import de.htw.ai.kbe.storage.UserStorage;

public class DependencyBinder extends AbstractBinder {

	@Override
	protected void configure() {
		/*
		 * Stand 18.01
		 * Tokens koennen sowohl lokal, als auch in der DB verwaltet werden.
		 * AuthStorage (lokale Inmemory) funktioniert
		 */

		//Beleg3
//		bind(SongsStorage.class).to(ISongs.class).in(Singleton.class);
//		bind(UserStorage.class).to(IUser.class); //.in(Singleton.class);
	
		

		// Binding angepasst nach Beleg 4 (DAO statt lokale InMemory-DBs
		bind(Persistence.createEntityManagerFactory("song-persistence")).to(EntityManagerFactory.class);
		
		// === TOKENS ==== //
		
		//XXX: Diese Zeile aktiviert die LOKALE Tokenverwaltung
		//bind(AuthTokenStorage.class).to(IAuth.class).in(Singleton.class);
		
		//XXX: Diese Zeilen aktivieren die DB-Tokenverwaltung
		bind(AuthTokenDB.class).to(IAuth.class).in(Singleton.class);
		bind(TokenDAO.class).to(IToken.class).in(Singleton.class);		
		
		// === SONGS === //
		bind(SongDAO.class).to(ISongs.class).in(Singleton.class);
		
		// === USERS === //
		bind(UserDAO.class).to(IUser.class).in(Singleton.class);
		
		// ==== SONGLISTS === //
		bind(SongListDAO.class).to(ISongList.class).in(Singleton.class);
		
		// === AUTH-FILTER === ///
		bind(AuthenticationFilter.class).to(IAuth.class);
	}

}
