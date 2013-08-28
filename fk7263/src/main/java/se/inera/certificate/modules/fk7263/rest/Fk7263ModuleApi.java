package se.inera.certificate.modules.fk7263.rest;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
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
import se.inera.certificate.modules.fk7263.model.converter.ExternalToTransportFk7263LegacyConverter;
import se.inera.certificate.modules.fk7263.model.converter.TransportToExternalConverter;
import se.inera.certificate.modules.fk7263.model.external.Fk7263CertificateContentHolder;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Utlatande;
import se.inera.certificate.modules.fk7263.pdf.PdfGenerator;
import se.inera.certificate.modules.fk7263.validator.UtlatandeValidator;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificate.v3.RegisterMedicalCertificate;

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
    @Path("/unmarshall")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_JSON)
    public Response unmarshall(se.inera.certificate.fk7263.model.v1.Utlatande fk7263UtlatandeJaxb) {
        // TODO: Schema validation according to fk7263_intyg.xsd

        Fk7263Utlatande externalModel = TransportToExternalConverter.convert(fk7263UtlatandeJaxb);

        Fk7263Intyg internalModel = new Fk7263Intyg(externalModel);

        List<String> validationErrors = new UtlatandeValidator(internalModel).validate();

        if (validationErrors.isEmpty()) {
            return Response.ok(externalModel).build();
        } else {
            String response = Strings.join(",", validationErrors);
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }

    @POST
    @Path("/marshall")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_XML)
    public Response marshall(@HeaderParam("X-Schema-Version") String version, Fk7263Utlatande externalModel) {
        RegisterMedicalCertificate registerMedicalCertificateJaxb = ExternalToTransportFk7263LegacyConverter.getJaxbObject(externalModel);
        return Response.ok(registerMedicalCertificateJaxb).build();
    }

    @POST
    @Path("/valid")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
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
    @Path("/pdf")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/pdf")
    public Response pdf(Fk7263CertificateContentHolder contentHolder) {
        // create the internal model that pdf generator exspects
        Fk7263Intyg intyg = new Fk7263Intyg(contentHolder.getCertificateContent());
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
    @Path("/internal")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response convertExternalToInternal(Fk7263CertificateContentHolder contentHolder) {

        Fk7263Intyg Fk7263Intyg = new Fk7263Intyg(contentHolder.getCertificateContent());
        Fk7263Intyg.setStatus(contentHolder.getCertificateContentMeta().getStatuses());

        return Response.ok(Fk7263Intyg).build();
    }

    @PUT
    @Path("/external")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response convertInternalToExternal(Object utlatande) {
        return Response.ok(utlatande).build();
    }
}
