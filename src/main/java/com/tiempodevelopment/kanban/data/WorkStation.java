package com.tiempodevelopment.kanban.data;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name="workstation")
@NamedQueries({
	@NamedQuery(name="station.byProject",
		query="SELECT w FROM WorkStation w WHERE w.project = ?1 ORDER BY w.order"),
	@NamedQuery(name="station.byProjectAndName",
		query="SELECT w FROM WorkStation w WHERE w.project = ?1 AND w.name = ?2"),
	@NamedQuery(name="station.byProjectAndId",
		query="SELECT w FROM WorkStation w WHERE w.project = ?1 AND w.id = ?2"),
	@NamedQuery(name="station.updateOrder",
		query="UPDATE WorkStation w SET w.order = w.order + 1 WHERE w.project = :project AND w.order >= :order")
})
@XmlRootElement(name="lane")
@XmlAccessorType(XmlAccessType.FIELD)
public class WorkStation {
	@Id @GeneratedValue
	private long id;
	
	private String name;
	private long wip;
	@Column(name="station")
	private long order;
	@XmlTransient
	@ManyToOne
	private KanbanProject project;
	@OneToMany
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
	public long getWip() {
		return wip;
	}
	public void setWip(long wip) {
		this.wip = wip;
	}
	public KanbanProject getProject() {
		return project;
	}
	public void setProject(KanbanProject project) {
		this.project = project;
	}
	public List<KanbanTask> getTasks() {
		return tasks;
	}
	public void setTasks(List<KanbanTask> tasks) {
		this.tasks = tasks;
	}
	public long getOrder() {
		return order;
	}
	public void setOrder(long order) {
		this.order = order;
	}
}
