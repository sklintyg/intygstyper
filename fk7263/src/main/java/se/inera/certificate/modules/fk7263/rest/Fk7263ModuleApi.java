package se.inera.certificate.modules.fk7263.rest;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.certificate.model.util.Strings;
import se.inera.certificate.modules.fk7263.model.Fk7263Intyg;
import se.inera.certificate.modules.fk7263.model.converter.UtlatandeJaxbToFk7263IntygConverter;
import se.inera.certificate.modules.fk7263.pdf.PdfGenerator;
import se.inera.certificate.modules.fk7263.validator.UtlatandeValidator;

import com.itextpdf.text.DocumentException;

/**
 * @author andreaskaltenbach
 */
public class Fk7263ModuleApi {

    private static final Logger LOG = LoggerFactory.getLogger(Fk7263ModuleApi.class);
    /**
     * @param utlatande
     * @return
     */
    @POST
    @Path( "/unmarshall" )
    @Consumes( MediaType.APPLICATION_XML )
    @Produces( MediaType.APPLICATION_JSON )
    public Response unmarshall(se.inera.certificate.common.v1.Utlatande utlatande) {
        //TODO: Schema validation according to fk7263_intyg.xsd

        //Convert from jaxb-> Fk7263Intyg
        //TODO: use the "clean" Fk7263ExternalModel - Fk7263Intyg is today decorated with internal (MI-specific) properties
        //Perhaps have several internal formats, one for MI, one for webcert
        Fk7263Intyg fk7263Intyg = UtlatandeJaxbToFk7263IntygConverter.convert(utlatande);

        //validate (Fk7263Intyg) : if error return error list json with error status code
       
        return Response.ok(fk7263Intyg).build();
    }
    
    @POST
    @Path( "/valid" )
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.TEXT_PLAIN )
    public Response validate(Fk7263Intyg utlatande) {

        List<String> validationErrors = new UtlatandeValidator(utlatande).validate();

        if (validationErrors.isEmpty()) {
            return Response.ok().build();
        } else {
            String response = Strings.join(",", validationErrors);
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }

    @POST
    @Path( "/pdf" )
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( "application/pdf" )
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

    @PUT
    @Path( "/internal" )
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.APPLICATION_JSON )
    public Response convertExternalToInternal(Fk7263Intyg utlatande) {
        return Response.ok(utlatande).build();
    }

    @PUT
    @Path( "/external" )
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.APPLICATION_JSON )
    public Response convertInternalToExternal(Object utlatande) {
        return Response.ok(utlatande).build();
    }
}
