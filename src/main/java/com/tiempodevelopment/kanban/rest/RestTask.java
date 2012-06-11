package com.tiempodevelopment.kanban.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.tiempodevelopment.kanban.data.KanbanProject;
import com.tiempodevelopment.kanban.data.KanbanTask;
import com.tiempodevelopment.kanban.rest.RestProject.ServiceLocator;
import com.tiempodevelopment.kanban.service.KanbanProjectService;
import com.tiempodevelopment.kanban.service.KanbanTaskService;

public class RestTask {
	private ServiceLocator locator;
	
	private long taskId;
	private long projectId;
	
	public RestTask(ServiceLocator locator, long projectId, long taskId) {
		this.locator = locator;
		this.projectId = projectId;
		this.taskId = taskId;
	}
	
	@GET
	public KanbanTask taskInfo() {
		KanbanProject project = project();
		if(project != null) {
			return task(taskId);
		}
		return null;
	}
	
	@POST @Path("/assign/{userId}")
	public KanbanTask assignTask() {
		return null;
	}
	
	@POST @Path("/task/{taskId}/status/{fromLane}/to/{toLane}")
	public KanbanTask updateLane(@PathParam("taskId") long taskId, @PathParam("fromLane") long fromLane,
			@PathParam("toLane") long toLane) {
		KanbanProject project = project();
		if(project != null) {
			return tasks().byProjectAndId(project, taskId);
		}
		return null;
	}
	
	private KanbanProject project() {
		return projects().byId(projectId);
	}
	
	private KanbanTask task(long taskId) {
		return tasks().byProjectAndId(project(), taskId);
	}
	
	public KanbanProjectService projects() {
		return locator.projects();
	}
	
	public KanbanTaskService tasks() {
		return locator.tasks();
	}
}
