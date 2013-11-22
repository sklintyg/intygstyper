package se.inera.webcert.integration.rest;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import se.inera.webcert.integration.rest.dto.CreateDraftCertificateHolder;

/**
 * API exposing services for use in modules extending WebCert.
 * 
 * @author nikpet
 * 
 */
@Path("draft")
public interface WebCertModuleDraftApi {

    /**
     * Returns a list of all certificates
     * 
     * @return a JSON object
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Object getDraftCertificateList();

    /**
     * Creates an empty JSON object for a draft certificate. The type of the draft certificate to be created is
     * identified by certificateType.
     * 
     * @param draftInfo
     *            The type of draft certificate to produce etc
     * @return The draft certificate as a JSON object
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     * @throws JAXBException
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Object createDraftCertificate(CreateDraftCertificateHolder draftInfo) throws JsonParseException,
            JsonMappingException, IOException, JAXBException;

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
