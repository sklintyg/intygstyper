package se.inera.certificate.modules.fk7263.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

import com.google.common.base.Joiner;
import com.itextpdf.text.DocumentException;
import se.inera.certificate.integration.v1.Lakarutlatande;
import se.inera.certificate.modules.fk7263.model.Fk7263Intyg;
import se.inera.certificate.modules.fk7263.pdf.PdfGenerator;
import se.inera.certificate.modules.fk7263.validator.LakarutlatandeValidator;

/**
 * @author andreaskaltenbach
 */
public class Fk7263ModuleApi {

    @POST
    @Path("/extension")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_JSON)
    public Object extract(Lakarutlatande lakarutlatande) {
        return "{}";
    }

    @POST
    @Path("/valid")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.TEXT_PLAIN)
    public Response validate(Lakarutlatande lakarutlatande) {

        List<String> validationErrors = new LakarutlatandeValidator(lakarutlatande).validate();

        if (validationErrors.isEmpty()) {
            return Response.ok().build();
        } else {
            String response = Joiner.on(",").join(validationErrors);
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }

    @POST
    @Path( "/pdf" )
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( "application/pdf" )
    public byte[] pdf(Fk7263Intyg intyg) throws IOException, DocumentException {
        return new PdfGenerator(intyg).getBytes();
    }
}
