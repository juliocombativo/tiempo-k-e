package com.tiempodevelopment.kanban.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(filterName="loginFilter",
	urlPatterns="/kanban/*")
public class NTLMLoginFilter implements Filter {
	
	
	public void init(FilterConfig filterConfig) throws ServletException { ; }
	public void destroy() { ; }

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpSession session = httpRequest.getSession();
		
		boolean isResource = httpRequest.getRequestURI().endsWith(".css") ||
				httpRequest.getRequestURI().endsWith(".js") || 
				httpRequest.getRequestURI().endsWith(".ttf") || 
				httpRequest.getRequestURI().endsWith(".otf") || 
				httpRequest.getRequestURI().endsWith(".woff") || 
				httpRequest.getRequestURI().endsWith(".svg");
		if(session.getAttribute("USER_INFO") == null && !isResource)
			((HttpServletResponse) response).sendRedirect(((HttpServletRequest) request).getContextPath() + "/login.jsp");
		else
			chain.doFilter(request, response);
	}
}
