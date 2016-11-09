/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
