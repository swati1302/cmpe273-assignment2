package edu.sjsu.cmpe.procurement.api.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.yammer.metrics.annotation.Timed;


@Path("/v1/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RootResource {

    public RootResource() {
	// do nothing
    }

    @GET
    @Timed(name = "get-root")
    public Response getRoot() {
ProcurementServiceResource res =new ProcurementServiceResource();
res.doHttpGet();
	return Response.ok().build();
    }
    @POST
    @Timed(name="do-post")
    public Response getrootdopost()
    {
    	ProcurementServiceResource res =new ProcurementServiceResource();
    	res.doPostServer();
    	return Response.ok().build();
    }
}

