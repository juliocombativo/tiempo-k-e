package com.tiempodevelopment.kanban.data;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name="projects")
@NamedQueries({
	@NamedQuery(name="project.byName", 
		query="SELECT p FROM KanbanProject p WHERE p.name=?1"),
	@NamedQuery(name="project.byUser",
		query="SELECT a.project FROM ProjectAssignment a WHERE a.user = ?1")
})
@XmlRootElement(name="project")
@XmlAccessorType(XmlAccessType.FIELD)
public class KanbanProject {
	@Id @GeneratedValue
	private long id;
	
	private String name;
	private boolean active;
	@OneToMany
	@XmlTransient
	private List<WorkStation> lanes;
	@OneToMany
	@XmlTransient
	private List<ProjectAssignment> people;
	@OneToMany
	@XmlTransient
	private List<KanbanTask> tasks;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	@XmlTransient
	public List<WorkStation> getLanes() {
		return lanes;
	}
	public void setLanes(List<WorkStation> lanes) {
		this.lanes = lanes;
	}
	public List<ProjectAssignment> getPeople() {
		return people;
	}
	public void setPeople(List<ProjectAssignment> people) {
		this.people = people;
	}
	public List<KanbanTask> getTasks() {
		return tasks;
	}
	public void setTasks(List<KanbanTask> tasks) {
		this.tasks = tasks;
	}
}
