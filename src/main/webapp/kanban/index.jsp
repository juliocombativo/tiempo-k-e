<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>

<head>
	<title>Kanban Board</title>
	<link rel="stylesheet" href="css/font-awesome.css" />
	<link rel="stylesheet" href="css/font-awesome-ie7.css" />
	<link rel="stylesheet" href="css/smoothness/jquery-ui-1.8.20.custom.css" />
	<link rel="stylesheet" href="css/main.css" />
	<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="js/jquery-ui-1.8.20.custom.min.js"></script>
	<script type="text/javascript" src="js/main.js"></script>
</head>
<body>

<div id="main">

<div id="container">

<%@ include file="/kanban/fragments/upper_bar.jsp" %>
<%@ include file="/kanban/fragments/messages.jsp" %>
<%@ include file="/kanban/fragments/projects.jsp" %>
	
</div>

</div>

</body>
</html>