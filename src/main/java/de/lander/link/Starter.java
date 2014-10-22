package de.lander.link;

import java.io.File;

import javax.servlet.ServletException;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;

import de.lander.link.servlet.HelloWorldServlet;

public class Starter {

	/*
	 * See:
	 * http://www.mkyong.com/webservices/jax-rs/resteasy-hello-world-example/
	 */
	public static void main(String[] args) throws LifecycleException,
			ServletException {
		Tomcat tomcat = new Tomcat();
		tomcat.setPort(8080);

		File base = new File(System.getProperty("java.io.tmpdir"));
		Context rootCtx = tomcat.addContext("/app", base.getAbsolutePath());

		registerRestEasyServlet(rootCtx);
		registerHelloHTTPServlet(rootCtx);

		tomcat.start();
		tomcat.getServer().await();
		tomcat.stop();

	}

	// RestEasy Servlet
	private static void registerRestEasyServlet(Context rootCtx) {
		// Rest pojo scan
		// String listenerName =
		// "org.jboss.resteasy.plugins.server.servlet.ResteasyBootstrap";
		// rootCtx.addApplicationListener(listenerName);
		// rootCtx.getServletContext().addListener(new ResteasyBootstrap());
		// rootCtx.getServletContext().addListener("org.jboss.resteasy.plugins.server.servlet.ResteasyBootstrap");
		// rootCtx.addParameter("resteasy.scan", "true");

		// Other configuration
		Wrapper servlet = Tomcat
				.addServlet(rootCtx, "resteasy",
						"org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher");
		servlet.addMapping("/rest/*");
		rootCtx.addServletMapping("/rest/*", "resteasy");
		rootCtx.addParameter("resteasy.servlet.mapping.prefix", "/rest");
		servlet.addInitParameter("javax.ws.rs.Application",
				"de.lander.link.resteasy.RestApplication");
	}

	private static void registerHelloHTTPServlet(Context rootCtx) {
		// Hello World HTTP Servlet
		// http://localhost:8080/app/hello
		Tomcat.addServlet(rootCtx, "helloServlet", new HelloWorldServlet());
		rootCtx.addServletMapping("/hello", "helloServlet");
	}
}
