package com.tiempodevelopment.kanban.data;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name="credentials")
@NamedQueries({
	@NamedQuery(name="user.byName",
		query="SELECT u FROM KanbanUser u WHERE u.login = ?1"),
	@NamedQuery(name="user.byProject",
		query="SELECT a.user FROM ProjectAssignment a WHERE a.project = ?1"),
	@NamedQuery(name="user.byNameAndProject",
		query="SELECT a.user FROM ProjectAssignment a WHERE a.project = ?2 AND a.user.login = ?1")
})
@XmlRootElement(name="user")
public class KanbanUser {
	@Id @GeneratedValue
	private long id;
	
	private String domain;
	private String login;
	
	private String name;
	private String email;
	
	@OneToMany
	@XmlTransient
	private List<ProjectAssignment> projects;
	@OneToMany
	@XmlTransient
	private List<KanbanTask> tasks;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public List<ProjectAssignment> getProjects() {
		return projects;
	}
	public void setProjects(List<ProjectAssignment> projects) {
		this.projects = projects;
	}
	public List<KanbanTask> getTasks() {
		return tasks;
	}
	public void setTasks(List<KanbanTask> tasks) {
		this.tasks = tasks;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
