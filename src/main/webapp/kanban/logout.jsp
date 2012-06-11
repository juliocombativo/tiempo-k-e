<%@ page session="true" %>
<%
	session.invalidate();
    response.sendRedirect("kanban/index.jsp");
%>
