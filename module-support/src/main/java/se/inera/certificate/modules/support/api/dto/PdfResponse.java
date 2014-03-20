package se.inera.certificate.modules.support.api.dto;

import static org.springframework.util.Assert.hasText;
import static org.springframework.util.Assert.notNull;

public class PdfResponse {

    private final byte[] pdfData;

    private final String filename;

    public PdfResponse(byte[] pdfData, String filename) {
        notNull(pdfData, "'pdfData' must not be null");
        hasText(filename, "'filename' must not be empty");
        this.pdfData = pdfData;
        this.filename = filename;
    }

    public byte[] getPdfData() {
        // If we want to have true immutability we need to copy the byte[] here. But is it worth it?
        return pdfData;
    }

    public String getFilename() {
        return filename;
    }
}
