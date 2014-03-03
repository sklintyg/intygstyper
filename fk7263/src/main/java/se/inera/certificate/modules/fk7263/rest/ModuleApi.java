package se.inera.certificate.modules.fk7263.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import se.inera.certificate.modules.fk7263.model.external.Fk7263CertificateContentHolder;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Utlatande;
import se.inera.certificate.modules.fk7263.model.internal.Fk7263Intyg;
import se.inera.certificate.modules.fk7263.rest.dto.CreateNewDraftCertificateHolder;
import se.inera.certificate.modules.fk7263.rest.dto.ValidateDraftResponseHolder;

public interface ModuleApi {

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
    Response unmarshall(String transportXml);

    /**
     * Handles conversion from the external JSON model to the transport model (XML).
     * 
     * @param version
     *            The version of the transport model to marshall to.
     * @param externalModel
     *            The external model to convert.
     * 
     * @return An instance of the transport model, generated from the external model.
     */
    @POST
    @Path("/marshall")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_XML)
    Response marshall(@HeaderParam("X-Schema-Version") String version, Fk7263Utlatande externalModel);

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
    Response validate(Fk7263Utlatande externalModel);

    /**
     * Validates the internal model. The status (complete, incomplete) and a list of validation errors is returned.
     * 
     * @param externalModel
     *            The external model to validate.
     * @return
     */
    @POST
    @Path("/valid-draft")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    ValidateDraftResponseHolder validateDraft(
            se.inera.certificate.modules.fk7263.model.internal.Fk7263Intyg externalModel);

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
    byte[] pdf(Fk7263CertificateContentHolder externalModel);

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
    Response convertExternalToInternal(Fk7263CertificateContentHolder externalModel);

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
    Response convertInternalToExternal(Fk7263Intyg internalModel);

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
    se.inera.certificate.modules.fk7263.model.internal.Fk7263Intyg createNewInternal(
            CreateNewDraftCertificateHolder draftCertificateHolder);
}
