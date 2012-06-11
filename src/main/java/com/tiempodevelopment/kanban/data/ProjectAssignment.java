package com.tiempodevelopment.kanban.data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="assignments")
@NamedQueries({
	@NamedQuery(name="assignment.byUserAndProject",
		query="SELECT a FROM ProjectAssignment a WHERE a.user = ?1 and a.project = ?2")
})
public class ProjectAssignment {
	@Id @GeneratedValue
	private long id;
	
	@ManyToOne
	private KanbanProject project;
	@ManyToOne
	private KanbanUser user;
	@Enumerated(EnumType.STRING)
	private KanbanRole role;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public KanbanProject getProject() {
		return project;
	}
	public void setProject(KanbanProject project) {
		this.project = project;
	}
	public KanbanUser getUser() {
		return user;
	}
	public void setUser(KanbanUser user) {
		this.user = user;
	}
	public KanbanRole getRole() {
		return role;
	}
	public void setRole(KanbanRole role) {
		this.role = role;
	}
}
