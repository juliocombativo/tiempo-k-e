package com.tiempodevelopment.kanban.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.sun.jersey.api.core.ResourceContext;
import com.tiempodevelopment.kanban.data.KanbanProject;
import com.tiempodevelopment.kanban.data.KanbanTask;
import com.tiempodevelopment.kanban.data.WorkStation;
import com.tiempodevelopment.kanban.service.KanbanProjectService;
import com.tiempodevelopment.kanban.service.KanbanService;
import com.tiempodevelopment.kanban.service.KanbanTaskService;
import com.tiempodevelopment.kanban.service.KanbanWorkstationService;

@Stateless
@Path("/project/{projectId}")
@Produces({"application/xml", "application/json"})
public class RestProject {
	class ServiceLocator {
		public KanbanWorkstationService lanes() {
			return workstationService;
		}
		
		public KanbanProjectService projects() {
			return projectService;
		}
		
		public KanbanTaskService tasks() {
			return taskService;
		}
		
		public KanbanService kanban() {
			return service;
		}
	}
	
	@EJB
	private KanbanProjectService projectService;
	@EJB
	private KanbanWorkstationService workstationService;
	@EJB
	private KanbanTaskService taskService;
	@EJB
	private KanbanService service;
	
	@Context private ResourceContext context;
	
	private ServiceLocator locator;
	
	@PostConstruct
	public void createContext() {
		locator = new ServiceLocator();
	}
	
	@GET
	public Response byId(@PathParam("projectId") long projectId) {
		KanbanProject project = project(projectId);
		if(project == null) {
			return Response.noContent().build();
		}
		return Response.ok(project).build();
	}
		
	@GET @Path("/dashboard")
	public List<WorkStation> dashboard(@PathParam("projectId") long projectId) {
		KanbanProject project = project(projectId);
		if(project != null) {
			return locator.lanes().byProject(project);
		}
		return null;
	}
	
	@POST @Path("/tasks/add")
	@Consumes("application/x-www-form-urlencoded")
	public Response createTask(@PathParam("projectId") long projectId, @FormParam("name") String taskName,
			@FormParam("estimate") int estimate) throws URISyntaxException {
		KanbanProject project = project(projectId);
		if(project != null) {
			boolean created = service.createTask(project, taskName, estimate);
			KanbanTask task = locator.tasks().byProjectAndName(project, taskName);
			if(created) { 
				return Response.seeOther(new URI("/project/" + projectId + "/task/" + task.getId())).build();
			}
		}
		return Response.noContent().build();
	}
	
	@POST @Path("/users/add")
	@Consumes("application/x-www-form-urlencoded")
	public Response addUser(@PathParam("projectId") long projectId, @FormParam("username") String userName) {
		return Response.noContent().build();
	}
	
	@POST @Path("/users/admin/add")
	@Consumes("application/x-www-form-urlencoded")
	public Response addAdmin(@PathParam("projectId") long projectId, @FormParam("username") String userName) {
		return Response.noContent().build();
	}
	
	@Path("/task/{taskId}")
	public RestTask task(@PathParam("projectId") long projectId, @PathParam("taskId") long taskId) {
		return new RestTask(locator, projectId, taskId);
	}
	
	@Path("/lane/{laneId}")
	public RestLane lane(@PathParam("projectId") long projectId, @PathParam("laneId") long laneId) {
		return new RestLane(locator, projectId, laneId);
	}
	
	private KanbanProject project(long projectId) {
		return locator.projects().byId(projectId);
	}
}
