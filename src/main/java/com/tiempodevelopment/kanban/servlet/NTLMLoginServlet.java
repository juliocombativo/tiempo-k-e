package com.tiempodevelopment.kanban.servlet;

import java.io.IOException;
import java.util.Map;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tiempodevelopment.kanban.data.KanbanUser;
import com.tiempodevelopment.kanban.service.KanbanUserService;
import com.tiempodevelopment.sharepoint.NTLMLogin;

@SuppressWarnings("serial")
@WebServlet(name="loginServlet",
	urlPatterns="/login")
public class NTLMLoginServlet extends HttpServlet {
	NTLMLogin login;
	
	@EJB
	KanbanUserService userService;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userName = (String) request.getParameter("userName");
		String password = (String) request.getParameter("password");
		
		if(request.getSession().getAttribute("USER_INFO") != null) {
			response.sendRedirect(request.getContextPath() + "/kanban/");
		} else {
			if(userName == null && password == null) {
				response.sendRedirect(request.getContextPath() + "/login.jsp");
			} else {
				login = new NTLMLogin();
				Map<String, String> userInfo = login.login("sharepoint.tiempodevelopment.com", userName, password);
				if(userInfo.size() > 0) {
					KanbanUser credentials = userService.login(userName, userInfo.get("Name"), userInfo.get("Email"));
					request.getSession().setAttribute("USER_INFO", userInfo);
					request.getSession().setAttribute("USER", credentials);
					response.sendRedirect(request.getContextPath() + "/kanban/");
				} else {
					request.setAttribute("login.error", "login.error");
					getServletContext().getRequestDispatcher("/login.jsp").forward(request, response);
				}
			}
		}
	}
}
