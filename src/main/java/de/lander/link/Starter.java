package de.lander.link;

import java.io.File;

import javax.servlet.ServletException;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import de.lander.link.rest.RestHello;

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
		Context rootCtx = tomcat.addContext("/app", base.getAbsolutePath());

		Wrapper servlet = Tomcat.addServlet(rootCtx, "Jersey REST Service",
				new ServletContainer(new ResourceConfig(RestHello.class)));

		servlet.addInitParameter("jersey.config.server.provider.packages",
				"de.lander.link.rest");
		servlet.addMapping("/rest/*");

		rootCtx.addServletMapping("/rest/*", "Jersey REST Service");

		tomcat.start();
		tomcat.getServer().await();
		tomcat.stop();

	}
}
