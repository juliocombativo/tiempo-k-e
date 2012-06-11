package com.tiempodevelopment.kanban.rest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/api")
public class RestApp extends Application {
	@SuppressWarnings("unchecked")
	public Set<Class<?>> getClasses() {
		return new HashSet<Class<?>>(
			Arrays.asList(RestProjects.class, RestProject.class, RestUser.class)
		);
	}
}
