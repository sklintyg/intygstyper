package se.inera.intyg.intygstyper.luse.pdf.common.model;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.PdfContentByte;

import java.io.IOException;

/**
 * Created by eriklupander on 2016-10-03.
 */
public class FkImage extends PdfComponent<FkImage> {

    private byte[] imageData;

    private float linearScale = 1.0f;

    public FkImage withLinearScale(float linearScale) {
        this.linearScale = linearScale;
        return this;
    }

    public FkImage(byte[] imageData) {
        this.imageData = imageData;
    }

    public void render(PdfContentByte canvas, float x, float y) throws DocumentException {
        Image fkLogo = null;
        try {
            fkLogo = Image.getInstance(imageData);
            fkLogo.setAbsolutePosition(Utilities.millimetersToPoints(x), Utilities.millimetersToPoints(y));
            fkLogo.scalePercent(linearScale*100f);
            canvas.addImage(fkLogo);
        } catch (IOException e) {
            throw new DocumentException("Unable to render Image: " + e.getMessage());
        }

    }

}
