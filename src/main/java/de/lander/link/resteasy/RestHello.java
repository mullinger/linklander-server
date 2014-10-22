package de.lander.link.resteasy;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/hello")
public class RestHello {

  @GET
  @Produces("text/plain")
  public String sayPlainTextHello() {
    return "Hello Jersey";
  }
  
  @GET
  @Path("/max")
  @Produces("text/plain")
  public String sayMaxHello() {
    return "Hello Max";
  }

} 