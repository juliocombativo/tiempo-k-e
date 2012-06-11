<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="userName" value="${sessionScope['USER_INFO']['Name']}" />
<c:set var="userId" value="${sessionScope['USER'].id}" />
<div id="upper-bar">
	<span class="user-info">Hi ${userName}</span> |
	<span class="logout">
		<a class="ui-button user-logout-link" id="user-${userId}" href='<c:url value="/kanban/logout.jsp" />' title="Signout">
			<i class="icon-signout"></i>
		</a>
	</span>
</div>