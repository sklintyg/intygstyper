package se.inera.certificate.modules.rli.pdf;

import se.inera.certificate.modules.rli.model.internal.Utlatande;

public interface PdfGenerator {

    public abstract byte[] generatePDF(Utlatande utlatande) throws PdfGeneratorException;

}
