package com.tiempodevelopment.kanban.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.tiempodevelopment.kanban.data.KanbanProject;
import com.tiempodevelopment.kanban.data.KanbanRole;
import com.tiempodevelopment.kanban.data.KanbanTask;
import com.tiempodevelopment.kanban.data.KanbanUser;
import com.tiempodevelopment.kanban.data.ProjectAssignment;
import com.tiempodevelopment.kanban.data.WorkStation;

/**
 * Common Kanban taks
 */
@Stateless
public class KanbanService {
	@EJB
	KanbanProjectService projectService;
	@EJB
	KanbanWorkstationService workstationService;
	@EJB
	KanbanTaskService taskService;
	@EJB
	KanbanUserService userService;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public boolean createProject(String projectName) {
		if(projects().byName(projectName) != null)
			return false;
		
		KanbanProject project = new KanbanProject();
		project.setActive(true);
		project.setName(projectName);
		
		projects().add(project);
		addLane(project, "Backlog", -1, 1);
		addLane(project, "In Progress", -1, 2);
		addLane(project, "Done", -1, 3);
		return true;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void addLane(KanbanProject project, String lane, long capacity, long order) {
		if(workstationService.byProjectAndName(project, lane) != null)
			return;
		
		WorkStation workstation = new WorkStation();
		workstation.setName(lane);
		workstation.setWip(capacity);
		workstation.setProject(project);
		workstation.setOrder(order);
		
		workstations().add(workstation);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void activateProject(KanbanProject project, boolean activate) {
		KanbanProject p = projects().byName(project.getName());
		p.setActive(activate);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public boolean canMove(KanbanTask task, WorkStation to, KanbanUser by) {
		boolean canMove = false;
		
		// Is user in this project?
		KanbanTask kanbanTask = taskService.byProjectAndName(task.getProject(), task.getName());
		KanbanUser user = userService.byNameAndProject(by.getLogin(), kanbanTask.getProject());
		if(user != null) {
			ProjectAssignment role = userService.roleByProject(user, kanbanTask.getProject());
			if(role.getRole() == KanbanRole.ADMIN || (kanbanTask.getAsignee() != null && role.getUser().getId() == kanbanTask.getAsignee().getId())) 
				canMove = true;
		}
		
		return canMove;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void moveTask(KanbanTask task, WorkStation to, KanbanUser by) {
		if(canMove(task, to, by)) {
			KanbanTask editableTask = taskService.byProjectAndName(task.getProject(), task.getName());
			workstationService.moveTask(editableTask, to, by);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public boolean createTask(KanbanProject project, String name, int estimate) {
		if(tasks().byProjectAndName(project, name) != null)
			return false;
		
		KanbanTask task = new KanbanTask();
		task.setProject(project);
		task.setStation(projects().firstStation(project));
		task.setName(name);
		task.setEstimate(estimate);
		
		tasks().add(task);
		return true;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public boolean assignTask(KanbanTask task, KanbanUser user) {
		task = taskService.byProjectAndName(task.getProject(), task.getName());
		KanbanProject p = projectService.byName(task.getProject().getName());
		
		if(user == null) {
			task.setAsignee(null);
		} else {
			KanbanUser u = userService.byNameAndProject(user.getLogin(), p); 
			if(u == null)
				return false;
		
			task.setAsignee(u);
		}
		return true;
	}
	
	private KanbanProjectService projects() {
		return projectService;
	}
	
	private KanbanWorkstationService workstations() {
		return workstationService;
	}
	
	private KanbanTaskService tasks() {
		return taskService;
	}
}
