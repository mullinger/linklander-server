package de.lander.link;

import java.io.File;

import javax.servlet.ServletException;
import javax.tools.Tool;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;

import de.lander.link.servlet.HelloWorldServlet;

public class Starter {

	/*
	 * See:
	 * https://jersey.java.net/documentation/latest/modules-and-dependencies
	 * .html http://tomcat.apache.org/tomcat-8.0-doc/api/index.html
	 * http://www.hascode
	 * .com/2013/07/embedding-jetty-or-tomcat-in-your-java-application/
	 * http://www
	 * .vogella.com/tutorials/REST/article.html#first_servletdispatcher
	 */
	public static void main(String[] args) throws LifecycleException,
			ServletException {
		Tomcat tomcat = new Tomcat();
		tomcat.setPort(8080);

		File base = new File(System.getProperty("java.io.tmpdir"));
		Context rootCtx = tomcat.addWebapp("/app", base.getAbsolutePath());

		// RestEasy Servlet
		rootCtx.addParameter("resteasy.servlet.mapping.prefix", "/rest");

		Wrapper servlet = Tomcat
				.addServlet(rootCtx, "resteasy",
						"org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher");

		servlet.addMapping("/rest/*");
		rootCtx.addServletMapping("/rest/*", "resteasy");
		servlet.addInitParameter("javax.ws.rs.Application",
				"de.lander.link.resteasy.RestApplication");

		// Hello World HTTP Servlet
		// http://localhost:8080/app/hello
		Tomcat.addServlet(rootCtx, "helloServlet", new HelloWorldServlet());
		rootCtx.addServletMapping("/hello", "helloServlet");

		tomcat.start();
		tomcat.getServer().await();
		tomcat.stop();

	}
}
