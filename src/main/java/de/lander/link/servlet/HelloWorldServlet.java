package de.lander.link.servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HelloWorldServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(final HttpServletRequest req,
			final HttpServletResponse res) throws ServletException, IOException {
		
		String out = "Hello ";
		String target = req.getParameter("target");
		
		out += (target != null) ? target : "Welt!";
		
		res.getWriter()
				.append(String.format("It's %s now\n\n\n\n"+out,
						new Date()));
	}
	
}
