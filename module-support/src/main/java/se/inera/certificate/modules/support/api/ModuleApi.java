package se.inera.certificate.modules.support.api;

import se.inera.certificate.modules.support.ApplicationOrigin;
import se.inera.certificate.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.certificate.modules.support.api.dto.ExternalModelHolder;
import se.inera.certificate.modules.support.api.dto.ExternalModelResponse;
import se.inera.certificate.modules.support.api.dto.HoSPersonal;
import se.inera.certificate.modules.support.api.dto.InternalModelHolder;
import se.inera.certificate.modules.support.api.dto.InternalModelResponse;
import se.inera.certificate.modules.support.api.dto.PdfResponse;
import se.inera.certificate.modules.support.api.dto.TransportModelHolder;
import se.inera.certificate.modules.support.api.dto.TransportModelResponse;
import se.inera.certificate.modules.support.api.dto.TransportModelVersion;
import se.inera.certificate.modules.support.api.dto.ValidateDraftResponse;
import se.inera.certificate.modules.support.api.exception.ModuleException;

/**
 * The module API, exposing the methods that all methods needs to implement.
 */
public interface ModuleApi {

    /**
     * Handles conversion from the transport model (XML) to the external JSON model.
     *
     * @param transportModel The transport model to convert.
     * @return An instance of the external model, generated from the transport model.
     */
    ExternalModelResponse unmarshall(TransportModelHolder transportModel) throws ModuleException;

    /**
     * Handles conversion from the external JSON model to the transport model (XML).
     *
     * @param externalModel The external model to convert.
     * @param version       The expected version of the transport model.
     * @return An instance of the transport model, generated from the external model.
     */
    TransportModelResponse marshall(ExternalModelHolder externalModel, TransportModelVersion version)
            throws ModuleException;

    /**
     * Validates the external model. If the validation succeeds, a empty result will be returned. If the validation
     * fails, a list of validation messages will be returned as a HTTP 400.
     *
     * @param externalModel The external model to validate.
     * @return messages
     */
    String validate(ExternalModelHolder externalModel) throws ModuleException;

    /**
     * Validates the internal model. The status (complete, incomplete) and a list of validation errors is returned.
     *
     * @param internalModel The internal model to validate.
     * @return response
     */
    ValidateDraftResponse validateDraft(InternalModelHolder internalModel) throws ModuleException;

    /**
     * Generates a PDF from the external model.
     *
     * @param externalModel The external model to generate a PDF from.
     * @param applicationOrigin The context from which this method was called (i.e Webcert or MinaIntyg)
     * @return A binary stream containing a PDF template populated with the information of the external model.
     */
    PdfResponse pdf(ExternalModelHolder externalModel, ApplicationOrigin applicationOrigin) throws ModuleException;

    /**
     * Handles conversion from the external model to the internal model.
     *
     * @param externalModel The external model to convert.
     * @return An instance of the internal model, generated from the external model.
     */
    InternalModelResponse convertExternalToInternal(ExternalModelHolder externalModel) throws ModuleException;

    /**
     * Handles conversion from the internal model to the external model.
     *
     * @param internalModel The internal model to convert.
     * @return An instance of the external model, generated from the internal model.
     */
    ExternalModelResponse convertInternalToExternal(InternalModelHolder internalModel) throws ModuleException;

    /**
     * Creates a new editable model for use in WebCert. The model is pre populated using data contained in the
     * CreateNewDraftCertificateHolder parameter.
     *
     * @param draftCertificateHolder draftCertificateHolder
     * @return response
     */
    InternalModelResponse createNewInternal(CreateNewDraftHolder draftCertificateHolder) throws ModuleException;

    /**
     * Returns complementary information for a specific module that can be displayed when intyg are listed.
     *
     * @param externalModel The external model to extract complementary information from.
     *
     * @return The complementary info for this type of intyg.
     */
    String getComplementaryInfo(ExternalModelHolder externalModel) throws ModuleException;

    /**
     * Returns an updated version of the external model with the HoS person information.
     *
     * @param internalModel The internal model to extract complementary information from.
     * @param hosPerson The HoS person to update the model with.
     *
     * @return The internal model updated with the hosPerson info.
     */
    InternalModelHolder updateInternal(InternalModelHolder internalModel, HoSPersonal hosPerson) throws ModuleException;
}
