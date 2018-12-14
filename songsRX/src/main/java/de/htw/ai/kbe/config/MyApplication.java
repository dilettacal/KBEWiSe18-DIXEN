package de.htw.ai.kbe.config;

import java.io.File;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.model.Resource;

import de.htw.ai.kbe.di.DependencyBinder;
import de.htw.ai.kbe.filter.AuthenticationFilter;

@ApplicationPath("/rest")
public class MyApplication extends ResourceConfig {
	
	public MyApplication() {
		//Einbinden 
		register(new DependencyBinder());
		packages("de.htw.ai.kbe.services");
		//AuthenticationFilter muss registriert werden, kein packages(..)
       register(AuthenticationFilter.class); //packages("de.htw.ai.kbe.filter");
	}

}
