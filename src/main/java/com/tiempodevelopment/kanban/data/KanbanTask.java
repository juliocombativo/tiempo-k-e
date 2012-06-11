package com.tiempodevelopment.kanban.data;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name="tasks")
@NamedQueries({
	@NamedQuery(name="task.byProject",
		query="SELECT t FROM KanbanTask t WHERE t.project = ?1"),
	@NamedQuery(name="task.byProjectAndName",
		query="SELECT t FROM KanbanTask t WHERE t.project = ?1 and t.name = ?2"),
	@NamedQuery(name="task.byProjectAndId",
		query="SELECT t FROM KanbanTask t WHERE t.project = ?1 and t.id = ?2")
})
@XmlRootElement(name="task")
public class KanbanTask {
	@Id @GeneratedValue
	private long id;
	
	private String name;
	@ManyToOne
	@XmlTransient
	private KanbanProject project;
	@ManyToOne
	@XmlTransient
	private WorkStation station;
	@OneToMany
	@XmlTransient
	private List<KanbanLog> log;
	@ManyToOne
	private KanbanUser asignee;
	private int estimate;
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
	public KanbanProject getProject() {
		return project;
	}
	public void setProject(KanbanProject project) {
		this.project = project;
	}
	public WorkStation getStation() {
		return station;
	}
	public void setStation(WorkStation station) {
		this.station = station;
	}
	public List<KanbanLog> getLog() {
		return log;
	}
	public void setLog(List<KanbanLog> log) {
		this.log = log;
	}
	public KanbanUser getAsignee() {
		return asignee;
	}
	public void setAsignee(KanbanUser asignee) {
		this.asignee = asignee;
	}
	public int getEstimate() {
		return estimate;
	}
	public void setEstimate(int estimate) {
		this.estimate = estimate;
	}
}
