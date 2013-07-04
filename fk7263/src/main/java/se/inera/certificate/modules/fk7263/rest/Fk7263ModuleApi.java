package se.inera.certificate.modules.fk7263.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.util.List;

import com.itextpdf.text.DocumentException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.certificate.integration.v1.Lakarutlatande;
import se.inera.certificate.model.util.Strings;
import se.inera.certificate.modules.fk7263.model.Fk7263Intyg;
import se.inera.certificate.modules.fk7263.pdf.PdfGenerator;
import se.inera.certificate.modules.fk7263.validator.LakarutlatandeValidator;

/**
 * @author andreaskaltenbach
 */
public class Fk7263ModuleApi {

    private static final Logger LOG = LoggerFactory.getLogger(Fk7263ModuleApi.class);

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
            String response = Strings.join(",", validationErrors);
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }

    @POST
    @Path("/pdf")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/pdf")
    public Response pdf(Fk7263Intyg intyg) {

        try {
            byte[] generatedPdf = new PdfGenerator(intyg).getBytes();
            return Response.ok(generatedPdf).build();
        } catch (IOException e) {
            LOG.error("Failed to generate PDF for certificate #" + intyg.getId(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (DocumentException e) {
            LOG.error("Failed to generate PDF for certificate #" + intyg.getId(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
