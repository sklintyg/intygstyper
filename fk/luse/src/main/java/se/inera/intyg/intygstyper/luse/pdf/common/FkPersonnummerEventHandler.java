package se.inera.intyg.intygstyper.luse.pdf.common;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Created by eriklupander on 2016-10-03.
 */
public class FkPersonnummerEventHandler extends PdfPageEventHelper {

    private String personnummer;

    public FkPersonnummerEventHandler(String personnummer) {
        this.personnummer = personnummer;
    }

    /**
     * Adds the personnummer to every page _except_ page 1.
     *
     * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(com.itextpdf.text.pdf.PdfWriter,
     *      com.itextpdf.text.Document)
     */
    @Override
    public void onEndPage(PdfWriter writer, Document document) {

        PdfPTable table = new PdfPTable(1);
        table.setTotalWidth(Utilities.millimetersToPoints(30));
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_TOP);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        if (writer.getPageNumber() > 1) {

            // Page 2+
            table.addCell(new Phrase("Personnummer", PdfConstants.FONT_NORMAL_7));
            table.completeRow();
            table.addCell(new Phrase(String.valueOf(personnummer), PdfConstants.FONT_NORMAL));

            table.writeSelectedRows(0, -1, Utilities.millimetersToPoints(152f) , document.getPageSize().getTop() - Utilities.millimetersToPoints(9f),
                    writer.getDirectContent());
        }
    }
}
