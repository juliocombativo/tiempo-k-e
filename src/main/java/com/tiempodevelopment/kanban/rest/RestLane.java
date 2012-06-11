package com.tiempodevelopment.kanban.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.tiempodevelopment.kanban.data.KanbanProject;
import com.tiempodevelopment.kanban.data.KanbanTask;
import com.tiempodevelopment.kanban.data.WorkStation;
import com.tiempodevelopment.kanban.rest.RestProject.ServiceLocator;
import com.tiempodevelopment.kanban.service.KanbanProjectService;
import com.tiempodevelopment.kanban.service.KanbanTaskService;
import com.tiempodevelopment.kanban.service.KanbanWorkstationService;

public class RestLane {
	private ServiceLocator locator;
	
	private long laneId;
	private long projectId;
	
	public RestLane(ServiceLocator locator, long projectId, long laneId) {
		this.locator = locator;
		this.projectId = projectId;
		this.laneId = laneId;
	}
	
	@GET
	public WorkStation laneInfo() {
		KanbanProject project = project();
		if(project != null) {
			return lane(laneId);
		}
		return null;
	}
	
	@GET @Path("/tasks")
	public List<KanbanTask> taskList() {
		KanbanProject project = project();
		if(project != null) {
			return tasks().byProject(project);
		}
		return null;
	}
	
	private KanbanProject project() {
		return projects().byId(projectId);
	}
	
	private WorkStation lane(long laneId) {
		return lanes().byProjectAndId(project(), laneId);
	}
	
	public KanbanProjectService projects() {
		return locator.projects();
	}
	
	public KanbanWorkstationService lanes() {
		return locator.lanes(); 
	}
	
	public KanbanTaskService tasks() {
		return locator.tasks();
	}
}
