package de.htw.ai.kbe.di;

import javax.inject.Singleton;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import de.htw.ai.kbe.database.dao.SongDAO;
import de.htw.ai.kbe.database.dao.TokenDAO;
import de.htw.ai.kbe.database.dao.UserDAO;
import de.htw.ai.kbe.database.interfaces.IAuth;
import de.htw.ai.kbe.database.interfaces.ISongs;
import de.htw.ai.kbe.database.interfaces.IUser;

public class DependencyBinder extends AbstractBinder {

	@Override
	protected void configure() {
		
		//Binding angepasst nach Beleg 4 (DAO statt lokale InMemory-DBs
		bind(SongDAO.class).to(ISongs.class).in(Singleton.class);
		bind(UserDAO.class).to(IUser.class); //.in(Singleton.class);
		bind(TokenDAO.class).to(IAuth.class).in(Singleton.class);
		
	}

}
