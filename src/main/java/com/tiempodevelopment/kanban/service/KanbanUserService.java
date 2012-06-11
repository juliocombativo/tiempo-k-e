package com.tiempodevelopment.kanban.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.tiempodevelopment.kanban.data.KanbanProject;
import com.tiempodevelopment.kanban.data.KanbanUser;
import com.tiempodevelopment.kanban.data.ProjectAssignment;
import com.tiempodevelopment.kanban.data.infra.DaoTemplate;

@Stateless
public class KanbanUserService {
	@PersistenceContext(unitName="kanban")
	EntityManager em;
	
	protected DaoTemplate<KanbanUser, Long> getDao() {
		return new DaoTemplate<KanbanUser, Long>(em);
	}
	
	private DaoTemplate<ProjectAssignment, Long> getAssignmentDao() {
		return new DaoTemplate<ProjectAssignment, Long>(em);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void add(KanbanUser user) {
		getDao().add(user);
	}

	public KanbanUser byName(String username) {
		return getDao().uniqueByNamedQuery("user.byName", username);
	}

	public List<KanbanUser> byProject(KanbanProject project) {
		return getDao().byNamedQuery("user.byProject", project);
	}

	public KanbanUser byNameAndProject(String login, KanbanProject p) {
		return getDao().uniqueByNamedQuery("user.byNameAndProject", login, p);
	}

	public ProjectAssignment roleByProject(KanbanUser user, KanbanProject project) {
		return getAssignmentDao().uniqueByNamedQuery("assignment.byUserAndProject", user, project);
	}

	public KanbanUser byId(long userId) {
		return getDao().byId(KanbanUser.class, userId);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public KanbanUser login(String login, String username, String email) {
		KanbanUser user = byName(login);
		if(user == null) {
			// We don't have a user, let's register this user!
			user = new KanbanUser();
			user.setLogin(login);
			user.setName(username);
			user.setEmail(email);
			add(user);
			
			user = byName(login);
		}
		return user;
	}
}
