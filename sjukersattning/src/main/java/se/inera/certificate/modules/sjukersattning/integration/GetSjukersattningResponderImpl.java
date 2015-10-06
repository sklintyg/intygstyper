package se.inera.certificate.modules.sjukersattning.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.inera.certificate.integration.module.exception.InvalidCertificateException;
import se.inera.certificate.modules.sjukersattning.model.converter.InternalToTransport;
import se.inera.certificate.modules.sjukersattning.model.converter.util.ConverterUtil;
import se.inera.certificate.modules.sjukersattning.rest.SjukersattningModuleApi;
import se.inera.certificate.modules.support.api.CertificateHolder;
import se.inera.intygstjanster.fk.services.getsjukersattningresponder.v1.GetSjukersattningResponderInterface;
import se.inera.intygstjanster.fk.services.getsjukersattningresponder.v1.GetSjukersattningResponseType;
import se.inera.intygstjanster.fk.services.getsjukersattningresponder.v1.GetSjukersattningType;
import se.inera.intygstjanster.fk.services.registersjukersattningresponder.v1.RegisterSjukersattningType;
import se.inera.intygstjanster.fk.services.v1.ErrorIdType;
import se.inera.intygstjanster.fk.services.v1.IntygMeta;

import com.google.common.base.Throwables;

public class GetSjukersattningResponderImpl implements GetSjukersattningResponderInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetSjukersattningResponderImpl.class);

    @Autowired
    private SjukersattningModuleApi moduleApi;

    @Autowired
    private ConverterUtil converterUtil;

    @Override
    public GetSjukersattningResponseType getSjukersattning(String logicalAddress, GetSjukersattningType request) {

        GetSjukersattningResponseType response = new GetSjukersattningResponseType();

        String certificateId = request.getIntygsId();

        try {
            CertificateHolder certificate = moduleApi.getModuleContainer().getCertificate(certificateId, null, false);
            if (certificate.isDeletedByCareGiver()) {
                response.setResultat(ResultUtil.errorResult(ErrorIdType.APPLICATION_ERROR,
                        String.format("Certificate '%s' has been deleted by care giver", certificateId)));
            } else {
                attachCertificateDocument(certificate, response);
                attachMeta(certificate, response);
                if (certificate.isRevoked()) {
                    response.setResultat(ResultUtil.errorResult(ErrorIdType.REVOKED, String.format("Certificate '%s' has been revoked", certificateId)));
                } else {
                    response.setResultat(ResultUtil.okResult());
                }
            }
        } catch (InvalidCertificateException e) {
            response.setResultat(ResultUtil.errorResult(ErrorIdType.VALIDATION_ERROR, e.getMessage()));
        }

        return response;
    }

    protected void attachCertificateDocument(CertificateHolder certificate, GetSjukersattningResponseType response) {
        try {
            RegisterSjukersattningType jaxbObject = InternalToTransport.convert(converterUtil.fromJsonString(certificate.getDocument()));
            response.setIntyg(jaxbObject.getIntyg());
        } catch (Exception e) {
            LOGGER.error("Error while converting in getSjukersattning for id: {} with stacktrace: {}", certificate.getId(), e.getStackTrace());
            Throwables.propagate(e);
        }
    }

    protected void attachMeta(CertificateHolder certificate, GetSjukersattningResponseType response) {
        try {
            IntygMeta intygMeta = InternalToTransport.getMeta(certificate);
            response.setMeta(intygMeta);
        } catch (Exception e) {
            LOGGER.error("Error while converting in getSjukersattning for id: {} with stacktrace: {}", certificate.getId(), e.getStackTrace());
            Throwables.propagate(e);
        }
    }

}
