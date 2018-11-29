package de.htw.ai.kbe.di;

import javax.inject.Singleton;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import de.htw.ai.kbe.storage.ISongs;
import de.htw.ai.kbe.storage.SongsStorage;

public class DependencyBinder extends AbstractBinder {

	@Override
	protected void configure() {
		bind(SongsStorage.class).to(ISongs.class).in(Singleton.class);		// erzeugt nur eine Instanz (Singleton-Muster!)
		//bind(SongsStorage.class).to(ISongs.class);
	}

}
