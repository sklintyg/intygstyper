package se.inera.certificate.modules.support.api;

import org.joda.time.LocalDateTime;

import se.inera.certificate.modules.support.ApplicationOrigin;
import se.inera.certificate.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.certificate.modules.support.api.dto.ExternalModelHolder;
import se.inera.certificate.modules.support.api.dto.ExternalModelResponse;
import se.inera.certificate.modules.support.api.dto.HoSPersonal;
import se.inera.certificate.modules.support.api.dto.InternalModelHolder;
import se.inera.certificate.modules.support.api.dto.InternalModelResponse;
import se.inera.certificate.modules.support.api.dto.Patient;
import se.inera.certificate.modules.support.api.dto.PdfResponse;
import se.inera.certificate.modules.support.api.dto.TransportModelHolder;
import se.inera.certificate.modules.support.api.dto.TransportModelResponse;
import se.inera.certificate.modules.support.api.dto.TransportModelVersion;
import se.inera.certificate.modules.support.api.dto.ValidateDraftResponse;
import se.inera.certificate.modules.support.api.exception.ModuleException;

/**
 * The module API defines methods that interact with one of the tree models that every module handles:
 * <ul>
 * <li>Transport model (XML-model that can be used to transmit an intyg between different parties)
 * <li>External model (JSON-model that is persisted in Intygstjänsten)
 * <li>Internal model (JSON-model used to visualize an intyg in Mina Intyg and Webcert)
 * </ul>
 * 
 * There exists methods for converting between models, generate PDFs, interact with the internal model and extract meta
 * data.
 */
public interface ModuleApi {

    /**
     * Handles conversion from the transport model (XML) to the external JSON model.
     * 
     * @param transportModel
     *            The transport model to convert.
     * 
     * @return An instance of the external model, generated from the transport model.
     */
    ExternalModelResponse unmarshall(TransportModelHolder transportModel) throws ModuleException;

    /**
     * Handles conversion from the external JSON model to the transport model (XML).
     * 
     * @param externalModel
     *            The external model to convert.
     * @param version
     *            The expected version of the transport model.
     * 
     * @return An instance of the transport model, generated from the external model.
     */
    TransportModelResponse marshall(ExternalModelHolder externalModel, TransportModelVersion version)
            throws ModuleException;

    /**
     * Validates the external model. If the validation succeeds, the method silently returns. If the validation
     * fails, a list of validation messages will be returned as a {@link ModuleValidationException}.
     * 
     * @param externalModel
     *            The external model to validate.
     */
    void validate(ExternalModelHolder externalModel) throws ModuleException;

    /**
     * Validates the internal model. The status (complete, incomplete) and a list of validation errors is returned.
     * 
     * @param internalModel
     *            The internal model to validate.
     * 
     * @return response The validation result.
     */
    ValidateDraftResponse validateDraft(InternalModelHolder internalModel) throws ModuleException;

    /**
     * Generates a PDF from the external model.
     * 
     * @param externalModel
     *            The external model to generate a PDF from.
     * @param applicationOrigin
     *            The context from which this method was called (i.e Webcert or MinaIntyg)
     * 
     * @return A {@link PdfResponse} consisting of a binary stream containing a PDF data and a suitable filename.
     */
    PdfResponse pdf(ExternalModelHolder externalModel, ApplicationOrigin applicationOrigin) throws ModuleException;

    /**
     * Handles conversion from the external model to the internal model.
     * 
     * @param externalModel
     *            The external model to convert.
     * 
     * @return An instance of the internal model, generated from the external model.
     */
    InternalModelResponse convertExternalToInternal(ExternalModelHolder externalModel) throws ModuleException;

    /**
     * Handles conversion from the internal model to the external model.
     * 
     * @param internalModel
     *            The internal model to convert.
     * 
     * @return An instance of the external model, generated from the internal model.
     */
    ExternalModelResponse convertInternalToExternal(InternalModelHolder internalModel) throws ModuleException;

    /**
     * Creates a new internal model. The model is prepopulated using data contained in the {@link CreateNewDraftHolder}
     * parameter.
     * 
     * @param draftCertificateHolder
     *            The id of the new internal model, the {@link HoSPersonal} and {@link Patient} data.
     * 
     * @return A new instance of the internal model.
     */
    InternalModelResponse createNewInternal(CreateNewDraftHolder draftCertificateHolder) throws ModuleException;

    /**
     * Creates a new internal model. The model is prepopulated using data contained in the {@link CreateNewDraftHolder}
     * parameter and {@link ExternalModelHolder} template.
     * 
     * @param draftCertificateHolder
     *            The id of the new internal model, the {@link HoSPersonal} and {@link Patient} data.
     * @param template
     *            An external model used as a template for the new internal model.
     * 
     * @return A new instance of the internal model.S
     */
    InternalModelResponse createNewInternalFromTemplate(CreateNewDraftHolder draftCertificateHolder, ExternalModelHolder template)
            throws ModuleException;

    /**
     * Returns complementary information for a specific module that can be displayed when an intyg is listed.
     * 
     * @param externalModel
     *            The external model to extract complementary information from.
     * 
     * @return The complementary info for this type of intyg.
     */
    String getComplementaryInfo(ExternalModelHolder externalModel) throws ModuleException;

    /**
     * Returns an updated version of the internal model with new HoS person information.
     * 
     * @param internalModel
     *            The internal model to use as a base.
     * @param hosPerson
     *            The HoS person to complement the model with.
     * @param signingDate
     *            The timestamp of the signing of the intyg.
     * 
     * @return A new internal model updated with the hosPerson info.
     */
    InternalModelResponse updateInternal(InternalModelHolder internalModel, HoSPersonal hosPerson, LocalDateTime signingDate) throws ModuleException;
}
