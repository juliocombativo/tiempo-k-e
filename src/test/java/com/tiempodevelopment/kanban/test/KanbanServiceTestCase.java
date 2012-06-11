package com.tiempodevelopment.kanban.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.tiempodevelopment.kanban.data.KanbanProject;
import com.tiempodevelopment.kanban.data.KanbanRole;
import com.tiempodevelopment.kanban.data.KanbanTask;
import com.tiempodevelopment.kanban.data.KanbanUser;
import com.tiempodevelopment.kanban.data.WorkStation;
import com.tiempodevelopment.kanban.service.KanbanProjectService;
import com.tiempodevelopment.kanban.service.KanbanService;
import com.tiempodevelopment.kanban.service.KanbanTaskService;
import com.tiempodevelopment.kanban.service.KanbanUserService;
import com.tiempodevelopment.kanban.service.KanbanWorkstationService;
import com.tiempodevelopment.kanban.test.infra.Deployments;

@RunWith(Arquillian.class)
public class KanbanServiceTestCase {
	@Inject
	private KanbanService service;
	@Inject
	private KanbanProjectService projects;
	@Inject
	private KanbanTaskService tasks;
	@Inject
	private KanbanWorkstationService stations;
	@Inject
	private KanbanUserService users;
	
	@Deployment
	public static WebArchive archive() {
		return Deployments.warDeployment();
	}
	
	@Before
	public void insertSampleData() {
		service.createProject("Foundation");
		KanbanProject p = projects.byName("Foundation");
		
		service.createTask(p, "Foundation", 20);
		service.createTask(p, "Foundation and Empire", 25);
		service.createTask(p, "Second Foundation", 10);
		
		addUser("admin@td.com");
		addUser("dev@td.com");
		addUser("tester@td.com");
		addUser("sample@domain.com");
		
		assertThat(users.byName("admin@td.com"), is(not(nullValue())));
		projects.assignUser(p, users.byName("admin@td.com"), KanbanRole.ADMIN);
		projects.assignUser(p, users.byName("dev@td.com"), KanbanRole.DEVELOPER);
		projects.assignUser(p, users.byName("tester@td.com"), KanbanRole.TESTER);
	}
	
	private void addUser(String username) {
		KanbanUser user = new KanbanUser();
		user.setLogin(username);
		user.setDomain("tiempodevelopment.com");
		
		users.add(user);
	}
	
	@Test
	public void activateProject() {
		KanbanProject project = projects.byName("Foundation");
		
		assertThat("Cannot locate project!", project, is(not(nullValue())));
		assertThat("Project is not active!", project.isActive(), is(true));
		
		service.activateProject(project, false);
		project = projects.byName("Foundation");
		assertThat("Project is active!", project.isActive(), is(false));
		
		service.activateProject(project, true);
		project = projects.byName("Foundation");
		assertThat("Project is active!", project.isActive(), is(true));
	}
	
	@Test
	public void addLane() {
		service.createProject("Planets");
		KanbanProject p = projects.byName("Planets");
		assertThat("Cannot locate project!", p, is(not(nullValue())));
		 
		
		checkAddStation(p, "Mars", 4, -1, true);
		// Cannot add duplicated, ok?
		checkAddStation(p, "Mars", 4, -1, false);
		checkAddStation(p, "Earth", 3, -1, true);
		checkAddStation(p, "Neptune", -1, -1, true);
		checkAddStation(p, "Pluto", 3, -1, true); // I Know, but I don't care!
	}
	
	private void checkAddStation(KanbanProject project, String laneName, long order, long wip, boolean success) {
		List<WorkStation> stationList = stations.byProject(project);
		
		List<WorkStation> nextTask = new ArrayList<WorkStation>();
		for(WorkStation ws : stationList) {
			if(ws.getOrder() >= order)
				nextTask.add(ws);
		}
		
		
		service.addLane(project, laneName, wip, order);
		List<WorkStation> afterList = stations.byProject(project);
		
		if(success) {
			assertThat("Workstation number is the same!", stationList.size(), is(afterList.size() - 1));
			
			WorkStation ws = stations.byProjectAndName(project, laneName);
			assertThat("Name is not the expected!", ws.getName(), is(laneName));
			assertThat("WIP is not the expected!", ws.getWip(), is((long) wip));
			if(order == -1)
				assertThat("Order is not the expected!", ws.getOrder(), is(1l));
			else
				assertThat("Name is not the expected!", ws.getOrder(), is(order));
			
			for(WorkStation nws : nextTask) {
				WorkStation updated = stations.byProjectAndName(project, nws.getName());
				assertThat("Workstation was not moved forward!", updated.getOrder(), is(nws.getOrder() + 1));
			}
		} else {
			assertThat("Workstation number has been modified!", stationList.size(), is(afterList.size()));
		}
	}
	
	@Test
	public void assignTask() {
		KanbanProject p = projects.byName("Foundation");
		KanbanTask t = tasks.byProjectAndName(p, "Foundation");
		assertThat("Cannot find task!", t, is(not(nullValue())));
		
		KanbanUser u = users.byName("admin@td.com");
		assertThat("Cannot find valid user!", t, is(not(nullValue())));
		
		assertThat("Cannot assign task to valid user!", service.assignTask(t, u), is(true));
		t = tasks.byProjectAndName(p, "Foundation");
		assertThat("Task has not the expected asignee!", t.getAsignee().getId(), is(u.getId()));
		
		KanbanUser noProjectUser = users.byName("sample@domain.com");
		assertThat("Could not assign task for non project user!", service.assignTask(t, noProjectUser), is(false));
		t = tasks.byProjectAndName(p, "Foundation");
		assertThat("Task has not the expected asignee!", t.getAsignee().getId(), is(u.getId()));
		
		assertThat("Assign to the same user!", service.assignTask(t, u), is(true));
		t = tasks.byProjectAndName(p, "Foundation");
		assertThat("Task has not the expected asignee!", t.getAsignee().getId(), is(u.getId()));
		
		u = users.byName("dev@td.com");
		assertThat("Cannot assign task to another valid user!", service.assignTask(t, u), is(true));
		t = tasks.byProjectAndName(p, "Foundation");
		assertThat("Task has not the expected asignee!", t.getAsignee().getId(), is(u.getId()));
		
		// Can assign to a null user (deassign in other words)
		assertThat("Cannot assign task to null user!", service.assignTask(t, null), is(true));
		t = tasks.byProjectAndName(p, "Foundation");
		assertThat("Task has not the expected asignee!", t.getAsignee(), is(nullValue()));
	}
	
	@Test
	public void canMove() {
		KanbanProject p = projects.byName("Foundation");
		KanbanTask t1 = tasks.byProjectAndName(p, "Foundation");
		KanbanTask t2 = tasks.byProjectAndName(p, "Foundation and Empire");
		KanbanTask t3 = tasks.byProjectAndName(p, "Second Foundation");
		KanbanUser admin = users.byName("admin@td.com");
		KanbanUser dev = users.byName("dev@td.com");
		KanbanUser tester = users.byName("tester@td.com");
		KanbanUser other = users.byName("sample@domain.com");
		
		List<WorkStation> stationList = stations.byProject(p);
		Map<Long, WorkStation> stationMap = new LinkedHashMap<Long, WorkStation>();
		for(WorkStation ws : stationList)
			stationMap.put(ws.getOrder(), ws);
		
		assertThat("Cannot locate task!", t1, is(not(nullValue())));
		assertThat("Cannot locate task!", t2, is(not(nullValue())));
		assertThat("Cannot locate task!", t3, is(not(nullValue())));
		
		assertThat("First task is not in the expected station!", t1.getStation().getName(), is("Backlog"));
		
		// Check moving forward, backward and jumping stations
		service.assignTask(t1, null);
		assertThat("Other user cannot move station!", service.canMove(t1, stationMap.get(2l), admin), is(true));
		assertThat("Other user can move station!", service.canMove(t1, stationMap.get(2l), tester), is(false));
		assertThat("Other user can move station!", service.canMove(t1, stationMap.get(2l), dev), is(false));
		
		// Now assign to A, try to move with Admin, Dev and QA
		service.assignTask(t2, dev);
		assertThat("Other user can move station!", service.canMove(t2, stationMap.get(2l), tester), is(false));
		assertThat("Other user cannot move station!", service.canMove(t2, stationMap.get(2l), admin), is(true));
		assertThat("User cannot move station!", service.canMove(t2, stationMap.get(2l), dev), is(true));
		
		// Try to move tasks using other user
		assertThat("Other user can move station!", service.canMove(t1, stationMap.get(2l), other), is(false));
		assertThat("Other user can move station!", service.canMove(t2, stationMap.get(2l), other), is(false));
		assertThat("Other user can move station!", service.canMove(t3, stationMap.get(2l), other), is(false));
	}
	
	@Test
	public void createProject() {
		assertThat("Service is not present!", service, is(not(nullValue())));
		assertThat("Cannot create project!", service.createProject("Robots"), is(true));
		
		// Project can be located and must be active
		KanbanProject project = projects.byName("Robots");
		assertThat("Cannot found project!", project, is(not(nullValue())));
		assertThat("Project is not active!", project.isActive(), is(true));
		
		// First workstation can be recovered
		WorkStation ws = projects.firstStation(project);
		assertThat("Cannot locate first state!", ws, is(not(nullValue())));
		assertThat("First state is not the expectes!", 
				ws.getName(), is("Backlog"));
	}
	
	@Test
	public void createTask() {
		KanbanProject p = projects.byName("Foundation");
		assertThat("Cannot found project!", p, is(not(nullValue())));
		
		// Can create a task
		List<KanbanTask> taskList = tasks.byProject(p);
		int tasksBefore = taskList.size();
		assertThat("Cannot create project!", service.createTask(p, "Forward Foundation", 10), is(true));
		taskList = tasks.byProject(p);
		int tasksAfter = taskList.size();
		assertThat("No extra task found!", tasksBefore, is(tasksAfter - 1));
		
		// Check task structure
		KanbanTask newTask = tasks.byProjectAndName(p, "Forward Foundation");
		assertThat("Cannot locate new task!", newTask, is(not(nullValue())));
		assertThat("Does not have workstation!", newTask.getStation(), is(not(nullValue())));
		assertThat("Does not have expected workstation!", newTask.getStation().getId(), 
			is(projects.firstStation(p).getId()));
		
		// Cannot create duplicated tasks
		assertThat("Cannot create project!", service.createTask(p, "Forward Foundation", 15), is(false));
	}
	
	@Test
	public void moveTask() {
		KanbanProject p = projects.byName("Foundation");
		KanbanTask t1 = tasks.byProjectAndName(p, "Foundation");
		KanbanTask t2 = tasks.byProjectAndName(p, "Foundation and Empire");
		KanbanTask t3 = tasks.byProjectAndName(p, "Second Foundation");
		KanbanUser admin = users.byName("admin@td.com");
		KanbanUser dev = users.byName("dev@td.com");
		KanbanUser tester = users.byName("tester@td.com");
		KanbanUser other = users.byName("sample@domain.com");
		
		List<WorkStation> stationList = stations.byProject(p);
		Map<Long, WorkStation> stationMap = new LinkedHashMap<Long, WorkStation>();
		for(WorkStation ws : stationList)
			stationMap.put(ws.getOrder(), ws);
		
		assertThat("Cannot locate task!", t1, is(not(nullValue())));
		assertThat("Cannot locate task!", t2, is(not(nullValue())));
		assertThat("Cannot locate task!", t3, is(not(nullValue())));
		
		assertThat("First task is not in the expected station!", t1.getStation().getName(), is("Backlog"));
		
		// Check moving forward, backward and jumping stations
		moveFoundationTask(t1, stationMap.get(2l), admin, true);
		moveFoundationTask(t1, stationMap.get(1l), admin, true);
		
		// Now assign to A, try to move with Admin, Dev and QA
		service.assignTask(t2, dev);
		moveFoundationTask(t2, stationMap.get(2l), tester, false);
		moveFoundationTask(t2, stationMap.get(2l), admin, true);
		moveFoundationTask(t2, stationMap.get(1l), dev, true);
		
		// B to A now
		service.assignTask(t3, tester);
		moveFoundationTask(t3, stationMap.get(2l), dev, false);
		moveFoundationTask(t3, stationMap.get(2l), admin, true);
		moveFoundationTask(t3, stationMap.get(1l), tester, true);
		
		// Try to move tasks using other user
		moveFoundationTask(t1, stationMap.get(2l), other, false);
		moveFoundationTask(t2, stationMap.get(2l), other, false);
		moveFoundationTask(t3, stationMap.get(2l), other, false);
	}
	
	private void moveFoundationTask(KanbanTask originalTask, WorkStation ws, KanbanUser u, boolean success) {
		KanbanTask task = tasks.byProjectAndName(originalTask.getProject(), originalTask.getName());
		long originalLane = task.getStation().getOrder();
		service.moveTask(task, ws, u);
		
		KanbanTask modifiedTask = tasks.byProjectAndName(task.getProject(), task.getName());
		if(success) {
			assertThat("Cannot move task!", modifiedTask.getStation().getOrder(), is(ws.getOrder()));
			assertThat("Cannot move task!", modifiedTask.getStation().getOrder(), is(not(originalLane)));
		} else {
			assertThat("Can move task!", modifiedTask.getStation().getOrder(), is(not(ws.getOrder())));
			assertThat("Can move task!", modifiedTask.getStation().getOrder(), is(originalLane));
		}
	}
}
