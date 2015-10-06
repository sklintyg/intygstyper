package se.inera.certificate.modules.sjukpenning.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.inera.certificate.integration.module.exception.InvalidCertificateException;
import se.inera.certificate.modules.sjukpenning.model.converter.InternalToTransport;
import se.inera.certificate.modules.sjukpenning.model.converter.util.ConverterUtil;
import se.inera.certificate.modules.sjukpenning.rest.SjukpenningModuleApi;
import se.inera.certificate.modules.support.api.CertificateHolder;
import se.inera.intygstjanster.fk.services.getsjukpenningresponder.v1.GetSjukpenningResponderInterface;
import se.inera.intygstjanster.fk.services.getsjukpenningresponder.v1.GetSjukpenningResponseType;
import se.inera.intygstjanster.fk.services.getsjukpenningresponder.v1.GetSjukpenningType;
import se.inera.intygstjanster.fk.services.registersjukpenningresponder.v1.RegisterSjukpenningType;
import se.inera.intygstjanster.fk.services.v1.ErrorIdType;
import se.inera.intygstjanster.fk.services.v1.IntygMeta;

import com.google.common.base.Throwables;

public class GetSjukpenningResponderImpl implements GetSjukpenningResponderInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetSjukpenningResponderImpl.class);

    @Autowired
    private SjukpenningModuleApi moduleApi;

    @Autowired
    private ConverterUtil converterUtil;

    @Override
    public GetSjukpenningResponseType getSjukpenning(String logicalAddress, GetSjukpenningType request) {

        GetSjukpenningResponseType response = new GetSjukpenningResponseType();

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

    protected void attachCertificateDocument(CertificateHolder certificate, GetSjukpenningResponseType response) {
        try {
            RegisterSjukpenningType jaxbObject = InternalToTransport.convert(converterUtil.fromJsonString(certificate.getDocument()));
            response.setIntyg(jaxbObject.getIntyg());
        } catch (Exception e) {
            LOGGER.error("Error while converting in getSjukpenning for id: {} with stacktrace: {}", certificate.getId(), e.getStackTrace());
            Throwables.propagate(e);
        }
    }

    protected void attachMeta(CertificateHolder certificate, GetSjukpenningResponseType response) {
        try {
            IntygMeta intygMeta = InternalToTransport.getMeta(certificate);
            response.setMeta(intygMeta);
        } catch (Exception e) {
            LOGGER.error("Error while converting in getSjukpenning for id: {} with stacktrace: {}", certificate.getId(), e.getStackTrace());
            Throwables.propagate(e);
        }
    }

}
