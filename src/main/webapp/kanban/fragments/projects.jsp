<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div id="my-projects" class="ui-widget">
<h4 class="ui-widget-header">My Projects</h4>
<div style="text-align: center;">

<div id="project-buttons">
	<a href="#new-project" class="add-project"><i class="icon-plus"></i> New Project</a>
	<a href="#task-info" class="add-task"><i class="icon-plus"></i> New Task</a>
</div>

<div id="project-list">
	<div id="project-1" class="project">
		<div class="lane ui-state-default" id="lane-1">
			<h4 class="lane-title">Backlog</h4>
			<div id="task-12" class="task on-project-1 ui-widget-content ui-corner-all">
				<p>Task Name</p>
			</div>
			<div id="task-13" class="task on-project-1 ui-widget-content ui-corner-all">
				<p>Another Task</p>
			</div>
			<div id="task-14" class="task on-project-1 ui-widget-content ui-corner-all">
				<p>Simple Task Name</p>
			</div>
		</div>
		<div class="lane ui-state-default" id="lane-2">
			<h4 class="lane-title">In Progress</h4>
		</div>
		<div class="lane ui-state-default" id="lane-3">
			<h4 class="lane-title">Integration Build</h4>
		</div>
		<div class="lane ui-state-default" id="lane-4">
			<h4 class="lane-title">QA</h4>
		</div>
		<div class="lane ui-state-default" id="lane-5">
			<h4 class="lane-title">Done</h4>
		</div>
	</div>
	
	<div id="new-project" class="ui-helper-hidden" title="Create Project">
		<c:url value="/api/projects/add" var="addUrl" />
		<form id="new-project-form" action="${addUrl}" enctype="application/x-www-form-urlencoded" accept="UTF-8" method="post">
		<fieldset>
			<label for="projectName">Project Name:</label>
			<input type="text" name="projectName" />
		</fieldset>
		</form>
	</div>
	<div id="task-info" class="ui-helper-hidden" title="Task">
		<c:url value="/api/project/add" var="addUrl" />
		<form action="${addUrl}" enctype="application/x-www-form-urlencoded">
		<fieldset>
			<label for="name"><i class="icon-caret-right"></i>Name: </label>
			<input type="text" name="name" placeholder="Task Name" />
			<label for="asignee"><i class="icon-caret-right"></i>Asignee: </label>
			<select name="asignee">
				<option value="${sessionScope['USER'].id}">${sessionScope['USER'].name}</option>
			</select>
			<label for="estimate"><i class="icon-caret-right"></i>Estimate: </label>
			<input type="text" name="estimate" />
		</fieldset>
		</form>
	</div>
</div>

<c:url value="/api" var="apiUrl" />
<a style="display: none; visibility: hidden;" class="api-url" href="${apiUrl}">&nbsp;</a>

</div>

</div>