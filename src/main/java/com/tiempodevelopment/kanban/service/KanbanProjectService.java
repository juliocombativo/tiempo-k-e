package com.tiempodevelopment.kanban.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.tiempodevelopment.kanban.data.KanbanProject;
import com.tiempodevelopment.kanban.data.KanbanRole;
import com.tiempodevelopment.kanban.data.KanbanTask;
import com.tiempodevelopment.kanban.data.KanbanUser;
import com.tiempodevelopment.kanban.data.ProjectAssignment;
import com.tiempodevelopment.kanban.data.WorkStation;
import com.tiempodevelopment.kanban.data.infra.DaoTemplate;

@Stateless
public class KanbanProjectService {
	@PersistenceContext(unitName="kanban")
	private EntityManager manager;
	@EJB
	KanbanWorkstationService workstation;
	@EJB
	KanbanUserService usersService;
	
	protected DaoTemplate<KanbanProject, Long> getDao() {
		return new DaoTemplate<KanbanProject, Long>(manager);
	}
	
	protected DaoTemplate<ProjectAssignment, Long> assignmentDao() {
		return new DaoTemplate<ProjectAssignment, Long>(manager);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void add(KanbanProject project) {
		getDao().add(project);
	}
	
	public KanbanProject byName(String name) {
		return getDao().uniqueByNamedQuery("project.byName", name);
	}

	public WorkStation firstStation(KanbanProject project) {
		List<WorkStation> lanes = workstation.byProject(project);
		WorkStation ws = null;
		
		if(lanes != null) 
			for(WorkStation w : lanes)
				if(ws == null || w.getOrder() < ws.getOrder())
					ws = w;
		
		return ws;
	}
	
	public void assignUser(KanbanProject project, KanbanUser user, KanbanRole role) {
		KanbanProject p = byName(project.getName());
		ProjectAssignment assignment = new ProjectAssignment();
		assignment.setProject(p);
		assignment.setUser(user);
		assignment.setRole(role);
		
		assignmentDao().add(assignment);
	}

	public List<KanbanProject> byUser(String username) {
		KanbanUser user = users().byName(username);
		
		if(user == null)
			return new ArrayList<KanbanProject>();
		
		return getDao().byNamedQuery("project.byUser", user);
	}
	
	private KanbanUserService users() {
		return usersService;
	}

	public KanbanProject byId(long projectId) {
		return getDao().byId(KanbanProject.class, projectId);
	}

	public List<WorkStation> dashboard(long projectId) {
		List<WorkStation> dashboard = this.byId(projectId).getLanes();
		for(WorkStation ws : dashboard)
			for(KanbanTask task : ws.getTasks())
				task.getAsignee();
		
		return dashboard;
	}
}
