package se.inera.certificate.modules.rli.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static se.inera.certificate.model.util.Strings.join;

import se.inera.certificate.common.v1.Utlatande;

/**
 * @author andreaskaltenbach
 */
public class RliModuleApi {

    @POST
    @Path( "/extension" )
    @Consumes( MediaType.APPLICATION_XML )
    @Produces( MediaType.APPLICATION_JSON )
    public Object extract(Utlatande intyg) {
        return intyg;
    }

    @POST
    @Path( "/valid" )
    @Consumes( MediaType.APPLICATION_XML )
    @Produces( MediaType.TEXT_PLAIN )
    public Response validate(Utlatande intyg) {

        List<String> errors = new ArrayList<>();
/*
        if (intyg.getResenar() == null) {
            errors.add("Resenär saknas");
        }

        if (intyg.getResmal() == null) {
            errors.add("Resmål saknas");
        }
*/
        if (errors.isEmpty()) {
            return Response.ok().build();
        } else {
            String response = join(",", errors);
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }

    @POST
    @Path( "/pdf" )
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( "application/pdf" )
    public Response pdf(se.inera.certificate.model.Utlatande utlatande) {
        return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
    }
}
