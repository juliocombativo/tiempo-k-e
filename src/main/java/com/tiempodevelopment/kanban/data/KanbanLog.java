package com.tiempodevelopment.kanban.data;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="moves")
public class KanbanLog {
	@Id @GeneratedValue
	private long id;
	
	@ManyToOne
	private KanbanTask task;
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	@ManyToOne
	private WorkStation from;
	@ManyToOne
	private WorkStation to;
	@ManyToOne
	private KanbanUser user;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public KanbanTask getTask() {
		return task;
	}
	public void setTask(KanbanTask task) {
		this.task = task;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public WorkStation getFrom() {
		return from;
	}
	public void setFrom(WorkStation from) {
		this.from = from;
	}
	public WorkStation getTo() {
		return to;
	}
	public void setTo(WorkStation to) {
		this.to = to;
	}
	public KanbanUser getUser() {
		return user;
	}
	public void setUser(KanbanUser user) {
		this.user = user;
	}
}
