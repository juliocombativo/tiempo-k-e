package com.tiempodevelopment.kanban.servlet;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import com.tiempodevelopment.kanban.data.KanbanProject;
import com.tiempodevelopment.kanban.service.KanbanService;

@Named("userBean")
@SessionScoped
public class UserInformation {
	@EJB
	KanbanService service;
	
	private List<KanbanProject> projects;
	
	public List<KanbanProject> getProjects() {
		return projects;
	}
}
