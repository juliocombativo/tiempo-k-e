package com.tiempodevelopment.kanban.test.rest;

import javax.ejb.EJB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.tiempodevelopment.kanban.data.KanbanProject;
import com.tiempodevelopment.kanban.service.KanbanProjectService;
import com.tiempodevelopment.kanban.service.KanbanService;
import com.tiempodevelopment.kanban.test.infra.Deployments;

@RunWith(Arquillian.class)
public class XmlSerialization {
	@EJB
	KanbanProjectService projects;
	
	@EJB
	KanbanService service;
	
	@Deployment
	public static WebArchive deploy() {
		return Deployments.warDeployment();
	}
	
	@Test
	public void serialize() throws Exception {
		service.createProject("Simple Project");
		KanbanProject p = projects.byName("Simple Project");
		
		Marshaller marshaller = JAXBContext.newInstance(KanbanProject.class).createMarshaller();
		marshaller.marshal(p, System.out);
	}
}
