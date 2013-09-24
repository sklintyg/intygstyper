package se.inera.webcert.integration.rest.mock;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.inera.certificate.modules.rli.model.edit.Utlatande;
import se.inera.certificate.modules.rli.model.factory.EditModelFactory;
import se.inera.webcert.integration.rest.WebCertModuleDraftApi;

public class WebCertModuleDraftMock implements WebCertModuleDraftApi {

    private static final Logger LOG = LoggerFactory.getLogger(WebCertModuleDraftMock.class);

    private static final Object MODULE_TYPE_RLI = "RLI";

    private final Map<String, Object> mockStore;

    @Autowired
    private EditModelFactory editModelFactory;

    public WebCertModuleDraftMock() {
        this.mockStore = new HashMap<>();
    }

    @Override
    @POST
    @Consumes("text/plain")
    @Produces("application/json")
    public Object createDraftCertificate(String certificateType) {
        if (!MODULE_TYPE_RLI.equals(certificateType)) {
            throw new WebApplicationException(Status.BAD_REQUEST);
        }

        String certificateId = UUID.randomUUID().toString();
        LOG.info("Creating new draft (type: {}), with id: {}", certificateType, certificateId);

        Utlatande utlatande = editModelFactory.createEditableUtlatande(certificateId,
                Collections.<String, Object> emptyMap());
        mockStore.put(certificateId, utlatande);

        return utlatande;
    }
    
    @Override
    @Path("/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Object getDraftCertificateList(){
        LOG.info("Getting all draft certificates");
        return mockStore.keySet();
    }

    @Override
    @Path("{certId}")
    @GET
    @Produces("application/json")
    public Object getDraftCertificate(@PathParam("certId") String certificateId) {
        LOG.info("Getting draft with id: {}", certificateId);

        Object certificate = mockStore.get(certificateId);
        if (certificate == null) {
            throw new WebApplicationException(Status.NOT_FOUND);
        }

        return certificate;
    }
    
    @Override
    @Path("{certId}")
    @PUT
    @Consumes("application/json")
    public void saveDraftCertificate(@PathParam("certId") String certificateId, Object draftCertificate) {
        LOG.info("Saving draft with id: {}", certificateId);

        if (!mockStore.containsKey(certificateId)) {
            throw new WebApplicationException(Status.BAD_REQUEST);
        }

        mockStore.put(certificateId, draftCertificate);
    }

    @Override
    @Path("{certId}")
    @DELETE
    @Consumes("application/json")
    public void deleteDraftCertificate(@PathParam("certId") String certificateId) {
        LOG.info("Removing draft with id: {}", certificateId);

        if (!mockStore.containsKey(certificateId)) {
            throw new WebApplicationException(Status.NOT_FOUND);
        }

        mockStore.remove(certificateId);
    }

    @Override
    @Path("{certId}/bulksign")
    @POST
    public void addDraftCertificateForSigning(@PathParam("certId") String certificateId) {
        throw new WebApplicationException(Status.NOT_IMPLEMENTED);
    }

    @Override
    @Path("{certId}/sign")
    @POST
    public void signDraftCertificate(@PathParam("certId") String certificateId) {
        throw new WebApplicationException(Status.NOT_IMPLEMENTED);
    }
}
