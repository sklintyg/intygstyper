package se.inera.webcert.integration.rest;

/**
 * API exposing services for use in modules extending WebCert.
 * 
 * @author nikpet
 *
 */
public interface WebCertModuleRestApi {

    /**
     * Creates an empty JSON object for a draft certificate. The type of the draft 
     * certificate to be created is identified by certificateType.
     * 
     * @param certificateType The type of draft certificate to produce
     * @return The draft certificate as a JSON object
     */
    Object createDraftCertificate(String certificateType);

    /**
     * Returns the draft certificate as JSON identified by the certificateId.
     * 
     * @param certificateId
     * @return a JSON object
     */
    Object getDraftCertificate(String certificateId);

    /**
     * Persists the supplied draft certificate using the certificateId as key.
     * 
     * @param certificateId
     *            The id of the certificate
     * @param draftCertificate
     */
    void saveDraftCertificate(String certificateId, Object draftCertificate);

    /**
     * Deletes the draft certificate identified by the certificateId.
     * 
     * @param certificateId
     */
    void deleteDraftCertificate(String certificateId);

    /**
     * Adds the draft certificate to a batch of certificates to be signed.
     * 
     * @param certificateId
     */
    void addDraftCertificateForSigning(String certificateId);

    /**
     * Invokes signing of the draft certificate.
     * 
     * @param certificateId
     */
    void signDraftCertificate(String certificateId);

    /**
     * Revokes the certificate identified by certificateId
     * 
     * @param certificateId
     */
    void revokeCertificate(String certificateId);

}
