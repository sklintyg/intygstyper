package se.inera.certificate.modules.rli.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import se.inera.certificate.modules.rli.model.external.Utlatande;
import se.inera.certificate.modules.rli.rest.dto.CertificateContentHolder;
import se.inera.certificate.modules.rli.rest.dto.CreateNewDraftCertificateHolder;

@Path("")
public interface RliModuleApi {

    /**
     * Handles conversion from the transport model (XML) to the external JSON model.
     * 
     * @param transportModel
     *            The transport model to convert.
     * 
     * @return An instance of the external model, generated from the transport model.
     */
    @POST
    @Path("/unmarshall")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_JSON)
    Utlatande unmarshall(se.inera.certificate.common.v1.Utlatande transportModel);

    /**
     * Handles conversion from the external JSON model to the transport model (XML).
     * 
     * @param externalModel
     *            The external model to convert.
     * 
     * @return An instance of the transport model, generated from the external model.
     */
    @POST
    @Path("/marshall")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_XML)
    se.inera.certificate.common.v1.Utlatande marshall(Utlatande externalModel);

    /**
     * Validates the external model. If the validation succeeds, a empty result will be returned. If the validation
     * fails, a list of validation messages will be returned as a HTTP 400.
     * 
     * @param externalModel
     *            The external model to validate.
     * @return
     */
    @POST
    @Path("/valid")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    String validate(Utlatande utlatande);

    /**
     * Generates a PDF from the external model.
     * 
     * @param externalModel
     *            The external model to generate a PDF from.
     * 
     * @return A binary stream containing a PDF template populated with the information of the external model.
     */
    @POST
    @Path("/pdf")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/pdf")
    byte[] pdf(CertificateContentHolder certificateContentHolder);

    /**
     * Handles conversion from the external model to the internal model.
     * 
     * @param externalModel
     *            The external model to convert.
     * 
     * @return An instance of the internal model, generated from the external model.
     */
    @PUT
    @Path("/internal")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    se.inera.certificate.modules.rli.model.internal.mi.Utlatande convertExternalToInternal(
            CertificateContentHolder certificateContentHolder);

    /**
     * Handles conversion from the internal model to the external model.
     * 
     * @param internalModel
     *            The internal model to convert.
     * 
     * @return An instance of the external model, generated from the internal model.
     */
    @PUT
    @Path("/external")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    se.inera.certificate.modules.rli.model.internal.mi.Utlatande convertInternalToExternal(
            se.inera.certificate.modules.rli.model.internal.mi.Utlatande internalModel);

    /**
     * Creates a new editable model for use in WebCert. The model is pre populated using data contained in the
     * CreateNewDraftCertificateHolder parameter.
     * 
     * @param draftCertificateHolder
     * @return
     */
    @POST
    @Path("/new")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    se.inera.certificate.modules.rli.model.internal.wc.Utlatande createNewInternal(
            CreateNewDraftCertificateHolder draftCertificateHolder);

}
