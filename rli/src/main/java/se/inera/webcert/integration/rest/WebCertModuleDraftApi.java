package se.inera.webcert.integration.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * API exposing services for use in modules extending WebCert.
 * 
 * @author nikpet
 * 
 */
@Path("draft")
public interface WebCertModuleDraftApi {

    /**
     * Creates an empty JSON object for a draft certificate. The type of the draft certificate to be created is
     * identified by certificateType.
     * 
     * @param certificateType
     *            The type of draft certificate to produce
     * @return The draft certificate as a JSON object
     */
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    Object createDraftCertificate(String certificateType);

    /**
     * Returns the draft certificate as JSON identified by the certificateId.
     * 
     * @param certificateId
     * @return a JSON object
     */
    @Path("{certId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Object getDraftCertificate(@PathParam("certId") String certificateId);

    /**
     * Returns a list of all certificates
     * @return a JSON object
     */
    @Path("/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Object getDraftCertificateList();

    
    /**
     * Persists the supplied draft certificate using the certificateId as key.
     * 
     * @param certificateId
     *            The id of the certificate
     * @param draftCertificate
     */
    @Path("{certId}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    void saveDraftCertificate(@PathParam("certId") String certificateId, Object draftCertificate);

    /**
     * Deletes the draft certificate identified by the certificateId.
     * 
     * @param certificateId
     */
    @Path("{certId}")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    void deleteDraftCertificate(@PathParam("certId") String certificateId);

    /**
     * Adds the draft certificate to a batch of certificates to be signed.
     * 
     * @param certificateId
     */
    @Path("{certId}/bulksign")
    @POST
    void addDraftCertificateForSigning(@PathParam("certId") String certificateId);

    /**
     * Invokes signing of the draft certificate.
     * 
     * @param certificateId
     */
    @Path("{certId}/sign")
    @POST
    void signDraftCertificate(@PathParam("certId") String certificateId);
}
