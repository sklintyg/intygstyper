package se.inera.certificate.modules.rli.rest;

import com.google.common.base.Joiner;
import se.inera.certificate.integration.v1.Lakarutlatande;
import se.inera.certificate.modules.rli.model.Resa;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * @author andreaskaltenbach
 */
public class RliModuleApi {

    @POST
    @Path("/extension")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_JSON)
    public Resa extract(Lakarutlatande intyg) {
        return new Resa(intyg.getResmal(), intyg.getResenar());
    }

    @POST
    @Path( "/valid" )
    @Consumes( MediaType.APPLICATION_XML )
    @Produces( MediaType.TEXT_PLAIN )
    public Response validate(Lakarutlatande intyg) {

        List<String> errors = new ArrayList<>();

        if (intyg.getResenar() == null) {
            errors.add("Resenär saknas");
        }

        if (intyg.getResmal() == null) {
            errors.add("Resmål saknas");
        }

        if (errors.isEmpty()) {
            return Response.ok().build();
        } else {
            String response = Joiner.on(",").join(errors);
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }
}
