package de.htw.ai.kbe.di;

import javax.inject.Singleton;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import de.htw.ai.kbe.filter.AuthTokenStorage;
import de.htw.ai.kbe.filter.IAuth;
import de.htw.ai.kbe.storage.ISongs;
import de.htw.ai.kbe.storage.IUser;
import de.htw.ai.kbe.storage.SongsStorage;
import de.htw.ai.kbe.storage.UserStorage;

public class DependencyBinder extends AbstractBinder {

	@Override
	protected void configure() {
		//FIXME: Nach Auth-Implementierung geht DI schief		
		bind(SongsStorage.class).to(ISongs.class).in(Singleton.class);
		bind(UserStorage.class).to(IUser.class); //.in(Singleton.class);
		bind(AuthTokenStorage.class).to(IAuth.class).in(Singleton.class);
		
	}

}
