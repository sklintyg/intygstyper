package se.inera.webcert.integration.rest.mock;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.inera.certificate.modules.rli.model.edit.Utlatande;
import se.inera.certificate.modules.rli.model.factory.EditModelFactory;
import se.inera.webcert.integration.rest.WebCertModuleDraftApi;
import se.inera.webcert.integration.rest.dto.CreateDraftCertificateHolder;

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
    public Object getDraftCertificateList() {
        LOG.info("Getting all draft certificates");
        return mockStore.keySet();
    }

    @Override
    public Object createDraftCertificate(CreateDraftCertificateHolder draftInfo) {
        String certificateType = draftInfo.getCertificateType();

        if (!MODULE_TYPE_RLI.equals(certificateType)) {
            throw new WebApplicationException(Status.BAD_REQUEST);
        }

        String certificateId = UUID.randomUUID().toString();
        LOG.info("Creating new draft (type: {}), with id: {}", certificateType, certificateId);

        Utlatande utlatande = editModelFactory.createEditableUtlatande(certificateId,
                draftInfo.getInitialParameters());
        mockStore.put(certificateId, utlatande);
        simulateLatency(1000);
        return utlatande;
    }

    @Override
    public Object getDraftCertificate(String certificateId) {
        LOG.info("Getting draft with id: {}", certificateId);

        Object certificate = mockStore.get(certificateId);
        if (certificate == null) {
            throw new WebApplicationException(Status.NOT_FOUND);
        }
        simulateLatency(1000);
        return certificate;
    }

    @Override
    public void saveDraftCertificate(String certificateId, Object draftCertificate) {
        LOG.info("Saving draft with id: {}", certificateId);

        if (!mockStore.containsKey(certificateId)) {
            throw new WebApplicationException(Status.BAD_REQUEST);
        }
        simulateLatency(1000);
        mockStore.put(certificateId, draftCertificate);
    }

    @Override
    public void deleteDraftCertificate(String certificateId) {
        LOG.info("Removing draft with id: {}", certificateId);

        if (!mockStore.containsKey(certificateId)) {
            throw new WebApplicationException(Status.NOT_FOUND);
        }
        simulateLatency(1000);
        mockStore.remove(certificateId);
    }

    @Override
    public void addDraftCertificateForSigning(String certificateId) {
        throw new WebApplicationException(Status.NOT_IMPLEMENTED);
    }

    @Override
    public void signDraftCertificate(String certificateId) {
        throw new WebApplicationException(Status.NOT_IMPLEMENTED);
    }

    private void simulateLatency(long length) {
        try {
            Thread.sleep(length);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
