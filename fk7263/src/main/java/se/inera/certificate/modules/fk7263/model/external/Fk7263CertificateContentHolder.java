package se.inera.certificate.modules.fk7263.model.external;


/**
 * @author marced
 */
public class Fk7263CertificateContentHolder {

    private Fk7263Utlatande certificateContent;
    private CertificateContentMeta certificateContentMeta;

  
    public CertificateContentMeta getCertificateContentMeta() {
        return certificateContentMeta;
    }

    public void setCertificateContentMeta(CertificateContentMeta certificateContentMeta) {
        this.certificateContentMeta = certificateContentMeta;
    }

    public Fk7263Utlatande getCertificateContent() {
        return certificateContent;
    }

    public void setCertificateContent(Fk7263Utlatande certificateContent) {
        this.certificateContent = certificateContent;
    }
}
