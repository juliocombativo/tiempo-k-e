package com.tiempodevelopment.kanban.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.tiempodevelopment.kanban.data.KanbanProject;
import com.tiempodevelopment.kanban.data.KanbanUser;
import com.tiempodevelopment.kanban.service.KanbanProjectService;
import com.tiempodevelopment.kanban.service.KanbanService;
import com.tiempodevelopment.kanban.service.KanbanUserService;

@Stateless
@Path("/projects")
@Produces({"application/xml", "application/json"})
public class RestProjects {
	@EJB
	KanbanService service;
	@EJB
	KanbanProjectService projectService;
	@EJB
	KanbanUserService userService;
	
	@GET
	@Path("/search/user/{userId}")
	public List<KanbanProject> mine(@QueryParam("userId") long userId) {
		KanbanUser user = userService.byId(userId);
		return projects().byUser(user.getName());
	}
	
	@POST
	@Path("/add")
	@Consumes("application/x-www-form-urlencoded")
	public Response add(@FormParam("projectName") String projectName) throws URISyntaxException {
		if(service.createProject(projectName)) {
			KanbanProject project = projectService.byName(projectName);
			if(project == null) {
				return Response.serverError().build();
			} else {
				return Response.seeOther(new URI("/project/" + project.getId())).build();
			}
		} else {
			return Response.noContent().build();
		}
	}
	
	private KanbanProjectService projects() {
		return projectService;
	}
}
