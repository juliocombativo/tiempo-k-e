package com.tiempodevelopment.kanban.service;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.tiempodevelopment.kanban.data.KanbanLog;
import com.tiempodevelopment.kanban.data.KanbanProject;
import com.tiempodevelopment.kanban.data.KanbanTask;
import com.tiempodevelopment.kanban.data.KanbanUser;
import com.tiempodevelopment.kanban.data.WorkStation;
import com.tiempodevelopment.kanban.data.infra.DaoTemplate;

@Stateless
public class KanbanWorkstationService {
	@PersistenceContext(unitName="kanban")
	private EntityManager manager;
	
	protected DaoTemplate<WorkStation, Long> getDao() {
		return new DaoTemplate<WorkStation, Long>(manager);
	}
	
	protected DaoTemplate<KanbanLog, Long> getLogDao() {
		return new DaoTemplate<KanbanLog, Long>(manager);
	}
	
	protected DaoTemplate<KanbanProject, Long> getProjectDao() {
		return new DaoTemplate<KanbanProject, Long>(manager);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void add(WorkStation workstation) {
		if(workstation.getOrder() < 0)
			workstation.setOrder(1l);
		
		Query q = manager.createNamedQuery("station.updateOrder");
		q.setParameter("project", workstation.getProject());
		q.setParameter("order", workstation.getOrder());
		q.executeUpdate();
		
		getDao().add(workstation);
	}

	public List<WorkStation> byProject(KanbanProject project) {
		return getDao().byNamedQuery("station.byProject", project);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void moveTask(KanbanTask task, WorkStation to, KanbanUser by) {
		KanbanLog log = new KanbanLog();
		log.setDate(new Date());
		log.setFrom(task.getStation());
		log.setTo(to);
		log.setUser(by);
		getLogDao().add(log);
		
		task.setStation(to);
	}

	public WorkStation byProjectAndName(KanbanProject project, String name) {
		return getDao().uniqueByNamedQuery("station.byProjectAndName", getProjectDao().byId(KanbanProject.class, project.getId()), name);
	}

	public WorkStation byProjectAndId(KanbanProject project, long laneId) {
		return getDao().uniqueByNamedQuery("station.byProjectAndId", getProjectDao().byId(KanbanProject.class, project.getId()), laneId);
	}
}
