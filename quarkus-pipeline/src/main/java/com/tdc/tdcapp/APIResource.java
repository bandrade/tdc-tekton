package com.tdc.tdcapp;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;

import io.quarkus.scheduler.Scheduled;

/**
 * APIResource
 */
@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
public class APIResource {
  private static final Logger LOG = Logger.getLogger(APIResource.class);
  int userCount = 0;
  int msgCount = 0;
  // String color = "#6bbded"; // blue
   String color = "#5bbf45"; // green
  //String color = "#f2f25e"; // canary

  String hello = "Huhu";

  String hostname = System.getenv().getOrDefault("HOSTNAME", "unknown");
  // Map<String, HttpSession> sessions = new ConcurrentHashMap<>();

  @GET
  public Response pollingendpoint (@Context HttpServletRequest request) {
    // quick & dirty way to know the number of unique user sessions
    String useragent = request.getHeader("user-agent");
    
    StringBuffer msg = new StringBuffer();
        msg.append("{");
        msg.append("\"msgCount\" :" + msgCount);
        msg.append(", \"hello\" : \"" + hello + "\"");        
        msg.append(", \"pod\" : \"" + hostname + "\"");
        msg.append(", \"useragent\" : \"" + useragent + "\"");        
        msg.append(", \"color\" : \"" + color + "\"");
        msg.append("}");

    LOG.info(msg.toString());

    return Response.ok(msg.toString()).build();
  }
  
  @GET
  @Path("headers")
  public Response headerEndpoint (@Context HttpHeaders httpHeaders) {

    Set<String> headerKeys = httpHeaders.getRequestHeaders().keySet();
    for(String header:headerKeys){
            System.out.println(header+":"+httpHeaders.getRequestHeader(header).get(0));
    }

    return Response.ok().entity("stuff").build();
  }
  
  @Scheduled(every="2s")
    void count() {
        msgCount++;        
  }

}
