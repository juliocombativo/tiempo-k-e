package com.tiempodevelopment.kanban.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.FileNotFoundException;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.tiempodevelopment.kanban.data.KanbanProject;
import com.tiempodevelopment.kanban.data.WorkStation;
import com.tiempodevelopment.kanban.service.KanbanProjectService;
import com.tiempodevelopment.kanban.service.KanbanService;
import com.tiempodevelopment.kanban.test.infra.Deployments;

@RunWith(Arquillian.class)
public class KanbanServiceProjectTestCase {
	@Inject
	private KanbanService service;
	@Inject
	private KanbanProjectService projectService;
	
	@Deployment
	public static WebArchive archive() throws IllegalArgumentException, FileNotFoundException {
		return Deployments.warDeployment();
	}
	
	@Test
	public void createProject() {
		assertThat("Service is not present!", service, is(not(nullValue())));
		assertThat("Cannot create project!", service.createProject("Robots"), is(true));
		
		KanbanProject project = projectService.byName("Robots");
		assertThat("Cannot found project!", project, is(not(nullValue())));
		
		WorkStation ws = projectService.firstStation(project);
		assertThat("Cannot locate first state!", ws, is(not(nullValue())));
		assertThat("First state is not the expectes!", 
				ws.getName(), is("Backlog"));
	}
	
	@Test
	public void createDuplicatedProject() {
		assertThat("Cannot create project!", service.createProject("Empire"), is(true));
		assertThat("Can create duplicated project!", service.createProject("Empire"), is(false));
	}
}
