package com.tiempodevelopment.kanban.test.infra;

import java.io.File;

import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;

public class Deployments {
	private static final String WEBAPP_SRC = "src/main/webapp";
	
	public static WebArchive warDeployment() {
		return ShrinkWrap.create(WebArchive.class)
			.addPackages(true, "com.tiempodevelopment.kanban")
			.addAsWebInfResource("test-persistence.xml", "classes/META-INF/persistence.xml")
			.addAsWebInfResource("beans.xml", "beans.xml")
			.setWebXML(new File(WEBAPP_SRC + "/WEB-INF/web.xml"));
	}
	
	public static WebArchive webApp() {
		WebArchive war = warDeployment()
			.merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class)
				.importDirectory(WEBAPP_SRC).as(GenericArchive.class), "/")
			.addClass(SampleDataStartupBean.class);
		
		return war;
	}
}
