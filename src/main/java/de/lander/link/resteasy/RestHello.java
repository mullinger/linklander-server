package de.lander.link.resteasy;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Path("/hello")
public class RestHello {

  @GET
  @Produces("text/plain")
  public String sayHello(@QueryParam("target") String target) {
	  String out = "Hallo ";
	  out += (target != null) ? target : "Welt";
	  out += "!";
	  return out;
  }
  
} 