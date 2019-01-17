package de.htw.ai.kbe.di;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;

import de.htw.ai.kbe.database.dao.SongDAO;
import de.htw.ai.kbe.database.dao.SongListDAO;
import de.htw.ai.kbe.database.dao.TokenDAO;
import de.htw.ai.kbe.database.dao.UserDAO;
import de.htw.ai.kbe.database.interfaces.IAuth;
import de.htw.ai.kbe.database.interfaces.ISongList;
import de.htw.ai.kbe.database.interfaces.ISongs;
import de.htw.ai.kbe.database.interfaces.IUser;
import de.htw.ai.kbe.filter.AuthTokenStorage;

public class DependencyBinder extends AbstractBinder {

	@Override
	protected void configure() {
		
		//Binding angepasst nach Beleg 4 (DAO statt lokale InMemory-DBs
		bind (Persistence.createEntityManagerFactory("song-persistence")).to(EntityManagerFactory.class);
		bind(SongDAO.class).to(ISongs.class).in(Singleton.class);
        bind(UserDAO.class).to(IUser.class).in(Singleton.class);
       bind(TokenDAO.class).to(IAuth.class).in(Singleton.class);
      //  bind(AuthTokenStorage.class).to(IAuth.class).in(Singleton.class);
        bind(SongListDAO.class).to(ISongList.class).in(Singleton.class);
		
	}

}
