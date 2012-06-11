package com.tiempodevelopment.kanban.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.tiempodevelopment.kanban.data.KanbanProject;
import com.tiempodevelopment.kanban.data.KanbanTask;
import com.tiempodevelopment.kanban.data.infra.DaoTemplate;

@Stateless
public class KanbanTaskService {
	@PersistenceContext(unitName="kanban")
	private EntityManager manager;
	
	protected DaoTemplate<KanbanTask, Long> getDao() {
		return new DaoTemplate<KanbanTask, Long>(manager);
	}
	
	protected DaoTemplate<KanbanProject, Long> getProjectDao() {
		return new DaoTemplate<KanbanProject, Long>(manager);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void add(KanbanTask task) {
		getDao().add(task);
	}

	public KanbanTask byProjectAndName(KanbanProject project, String name) {
		return getDao().uniqueByNamedQuery("task.byProjectAndName", getProjectDao().byId(KanbanProject.class, project.getId()), name);
	}

	public List<KanbanTask> byProject(KanbanProject project) {
		return getDao().byNamedQuery("task.byProject", getProjectDao().byId(KanbanProject.class, project.getId()));
	}

	public KanbanTask byProjectAndId(KanbanProject project, long taskId) {
		return getDao().uniqueByNamedQuery("task.byProjectAndId", project, taskId);
	}
}
