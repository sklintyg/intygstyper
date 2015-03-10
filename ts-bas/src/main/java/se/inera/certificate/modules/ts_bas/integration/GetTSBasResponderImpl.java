package se.inera.certificate.modules.ts_bas.integration;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;

import se.inera.certificate.integration.module.exception.InvalidCertificateException;
import se.inera.certificate.integration.module.exception.MissingConsentException;
import se.inera.certificate.logging.LogMarkers;
import se.inera.certificate.modules.support.api.CertificateHolder;
import se.inera.certificate.modules.ts_bas.model.converter.InternalToTransport;
import se.inera.certificate.modules.ts_bas.model.converter.util.ConverterUtil;
import se.inera.certificate.modules.ts_bas.rest.ModuleService;
import se.intygstjanster.ts.services.GetTSBasResponder.v1.GetTSBasResponderInterface;
import se.intygstjanster.ts.services.GetTSBasResponder.v1.GetTSBasResponseType;
import se.intygstjanster.ts.services.GetTSBasResponder.v1.GetTSBasType;
import se.intygstjanster.ts.services.utils.ResultTypeUtil;
import se.intygstjanster.ts.services.v1.ErrorIdType;
import se.intygstjanster.ts.services.v1.IntygMeta;
import se.intygstjanster.ts.services.v1.TSBasIntyg;

import com.google.common.base.Throwables;

public class GetTSBasResponderImpl implements GetTSBasResponderInterface{

    protected static final Logger LOGGER = LoggerFactory.getLogger(GetTSBasResponderImpl.class);

    private JAXBContext jaxbContext;

    private ConverterUtil converterUtil;

    @PostConstruct
    public void initializeJaxbContext() throws JAXBException {
        jaxbContext = JAXBContext.newInstance(TSBasIntyg.class);
        converterUtil =  new ConverterUtil();
    }

    @Autowired
    ModuleService moduleApi;

    @Override
    public GetTSBasResponseType getTSBas(String logicalAddress, GetTSBasType request) {
        GetTSBasResponseType response = new GetTSBasResponseType();

        String certificateId = request.getIntygsId();
        String personNummer = request.getPersonId().getExtension();

        if (certificateId == null || certificateId.length() == 0) {
            LOGGER.info(LogMarkers.VALIDATION, "Tried to get certificate with non-existing certificateId '.");
            response.setResultat(ResultTypeUtil.errorResult(ErrorIdType.APPLICATION_ERROR, "non-existing certificateId"));
            return response;
        }

        if (personNummer == null || personNummer.length() == 0) {
            LOGGER.info(LogMarkers.VALIDATION, "Tried to get certificate with non-existing nationalIdentityNumber '.");
            response.setResultat(ResultTypeUtil.errorResult(ErrorIdType.VALIDATION_ERROR, "nationalIdentityNumber mismatch"));
            return response;
        }

        CertificateHolder certificate = null;
        
        try {
            certificate = moduleApi.getModuleContainer().getCertificate(certificateId, personNummer, true);
            if (certificate.isRevoked()) {
                LOGGER.info("Certificate {} has been revoked", certificateId);
                response.setResultat(ResultTypeUtil.errorResult(ErrorIdType.REVOKED, String.format("Certificate '%s' has been revoked", certificateId)));
            } else if (certificate.isDeletedByCareGiver()) {
                response.setResultat(ResultTypeUtil.errorResult(ErrorIdType.APPLICATION_ERROR, String.format("Certificate '%s' has been deleted by care giver", certificateId)));
            } else {
                response.setMeta(createCertificateMetaType(certificate));
                attachCertificateDocument(certificate, response);
                response.setResultat(ResultTypeUtil.okResult());
            }
        } catch (InvalidCertificateException | MissingConsentException e) {
            response.setResultat(ResultTypeUtil.errorResult(ErrorIdType.VALIDATION_ERROR, e.getMessage()));
        }
        
        return response;
    }

    private void attachCertificateDocument(CertificateHolder certificate, GetTSBasResponseType response) {
        try {
            
            // Create the Document
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.newDocument();

            TSBasIntyg tsBasIntyg = InternalToTransport.convert(converterUtil.fromJsonString(certificate.getDocument()));

            // Marshal the Object to a Document
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.marshal(tsBasIntyg, document);
            tsBasIntyg.getAny().add(document.getDocumentElement());
            response.setIntyg(tsBasIntyg);

        } catch (Exception e) {
            Throwables.propagate(e);
        }
    }

    private IntygMeta createCertificateMetaType(CertificateHolder certificate) {
        
        return null;
    }

}
