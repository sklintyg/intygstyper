package se.inera.intyg.intygstyper.luse.pdf;

import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.services.texts.repo.IntygTextsRepositoryImpl;

import javax.xml.parsers.DocumentBuilderFactory;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.SortedMap;

/**
 * Created by eriklupander on 2016-10-03.
 */
public class IntygTextsLuseRepositoryTestHelper extends IntygTextsRepositoryImpl {

    public IntygTextsLuseRepositoryTestHelper() {
        super.intygTexts = new HashSet<>();
    }

    @Override
    public void update()  {

        try {
            Document e = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ClassPathResource("text/texterMU_LUSE_v1.0.xml").getInputStream());
            Element root = e.getDocumentElement();
            String version = root.getAttribute("version");
            String intygsTyp = root.getAttribute("typ").toLowerCase();
            String pdfPath = root.getAttribute("pdf");
            LocalDate giltigFrom = super.getDate(root, "giltigFrom");
            LocalDate giltigTo = super.getDate(root, "giltigTom");
            SortedMap texts = super.getTexter(root);
            List tillaggsFragor = this.getTillaggsfragor(e);
            super.intygTexts.add(new IntygTexts(version, intygsTyp, giltigFrom, giltigTo, texts, tillaggsFragor, pdfPath));
        } catch (Exception e1) {
            e1.printStackTrace();
            throw new RuntimeException(e1.getMessage());
        }
    }
}
