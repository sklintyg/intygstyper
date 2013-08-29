package se.inera.certificate.modules.rli.rest.dto;

import se.inera.certificate.integration.rest.dto.CertificateContentMeta;
import se.inera.certificate.modules.rli.model.external.Utlatande;

/**
 * Wrapper class for holding the Utlatande in external format of a certificate as well as
 * metadata about the certificate, such as status
 * 
 * @author marced
 */
public class CertificateContentHolder {

    private Utlatande certificateContent;

    private CertificateContentMeta certificateContentMeta;

    public CertificateContentMeta getCertificateContentMeta() {
        return certificateContentMeta;
    }

    public void setCertificateContentMeta(CertificateContentMeta certificateContentMeta) {
        this.certificateContentMeta = certificateContentMeta;
    }

    public Utlatande getCertificateContent() {
        return certificateContent;
    }

    public void setCertificateContent(Utlatande certificateContent) {
        this.certificateContent = certificateContent;
    }

}
