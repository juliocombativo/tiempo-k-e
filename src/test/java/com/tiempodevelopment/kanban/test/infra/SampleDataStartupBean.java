package com.tiempodevelopment.kanban.test.infra;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tiempodevelopment.kanban.data.KanbanProject;
import com.tiempodevelopment.kanban.service.KanbanProjectService;
import com.tiempodevelopment.kanban.service.KanbanService;

@SuppressWarnings("serial")
@WebServlet(name="sampleData", urlPatterns="/sample_data")
public class SampleDataStartupBean extends HttpServlet {
	@EJB
	KanbanService service;
	@EJB
	KanbanProjectService projects;
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		service.createProject("Firefly");
		service.createProject("Grimm");
		
		KanbanProject p = projects.byName("Grimm");
		service.addLane(p, "Pre Blacklog", -1, -1);
	}
}
