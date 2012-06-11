<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
	<title>Kanban Board - Login</title>
	<link rel="stylesheet" href="kanban/css/font-awesome.css" />
	<link rel="stylesheet" href="kanban/css/font-awesome-ie7.css" />
	<link rel="stylesheet" href="kanban/css/main.css" />
</head>
<body>

<div id="main">

<div id="container">

<h2>Login</h2>

<c:if test="${requestScope['login.error'] eq 'login.error'}">
<p>Invalid credentials</p>
</c:if>

<form action="login" method="post">

<table>
	<tr>
		<th><i class="icon-user"></i></th>
		<td><input type="text" name="userName" id="userName" placeholder="Username" /></td>
	</tr>
	<tr>
		<th><i class="icon-key"></i></th>
		<td><input type="password" name="password" id="password" placeholder="Password" /></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td align="right">
			<button type="submit" id="login"><i class="icon-signin"></i> Login</button>
		</td>
	</tr>
</table>

</form>

</div>
</div>

</body>
</html>
