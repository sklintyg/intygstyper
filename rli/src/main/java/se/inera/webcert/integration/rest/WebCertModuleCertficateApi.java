package se.inera.webcert.integration.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
@Path("certificate")
public interface WebCertModuleCertficateApi {

    /**
     * Returns the draft certificate as JSON identified by the certificateId.
     * 
     * @param certificateId
     * @return a JSON object
     */
    @Path("{certId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Object getCertificate(@PathParam("certId") String certificateId);

    // TODO: Add method for copying a certificate

    /**
     * Revokes the certificate identified by certificateId
     * 
     * @param certificateId
     */
    @Path("{certId}/revoke")
    @POST
    void revokeCertificate(@PathParam("certId") String certificateId);

}
