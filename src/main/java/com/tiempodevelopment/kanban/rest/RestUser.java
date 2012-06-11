package com.tiempodevelopment.kanban.rest;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.tiempodevelopment.kanban.data.KanbanProject;
import com.tiempodevelopment.kanban.data.KanbanUser;
import com.tiempodevelopment.kanban.service.KanbanProjectService;
import com.tiempodevelopment.kanban.service.KanbanService;
import com.tiempodevelopment.kanban.service.KanbanUserService;

@Stateless
@Path("/user/{userId}")
@Produces({"application/xml", "application/json"})
public class RestUser {
	@EJB
	KanbanService service;
	@EJB
	KanbanProjectService projectService;
	@EJB
	KanbanUserService userService;
	
	@GET
	public KanbanUser userInfo(@PathParam("userId") long userId) {
		return user(userId);
	}
	
	@GET
	@Path("/projects") 
	public List<KanbanProject> projects(@PathParam("userId") long userId) {
		KanbanUser user = user(userId);
		return projects().byUser(user.getLogin());
	}
	
	private KanbanUser user(long userId) {
		return users().byId(userId);
	}
	
	private KanbanUserService users() {
		return userService;
	}
	
	private KanbanProjectService projects() {
		return projectService;
	}
}
