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
package se.inera.intyg.intygstyper.fkparent.pdf;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import se.inera.intyg.common.services.texts.model.IntygTexts;
import se.inera.intyg.common.support.common.enumerations.PartKod;
import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.modules.support.ApplicationOrigin;
import se.inera.intyg.intygstyper.fkparent.model.internal.Diagnos;
import se.inera.intyg.intygstyper.fkparent.model.internal.SitUtlatande;

/**
 * Base class with common methods used by SIT type PDF definition construction.
 *
 * Created by marced on 2016-10-25.
 */
public class FkBasePdfDefinitionBuilder {

    protected static final String DATE_PATTERN = "yyyy-MM-dd";
    protected IntygTexts intygTexts;

    protected String getPrintedByText(ApplicationOrigin applicationOrigin) {
        switch (applicationOrigin) {
        case WEBCERT:
            return "Intyget är utskrivet från Webcert";
        case MINA_INTYG:
            return "Intyget är utskrivet från Mina intyg";
        default:
            throw new IllegalArgumentException("Unknown ApplicationOrigin " + applicationOrigin);
        }
    }

    protected String getText(String key) {
        String text = intygTexts.getTexter().get(key);
        if (text == null) {
            // Not finding a text is considered fatal
            throw new IllegalArgumentException(intygTexts.getIntygsTyp() + " (version " + intygTexts.getVersion() + ") dynamic text for key '" + key
                    + "' requested for PDF but was not found. Please check text sources / question id's");
        }
        return text;
    }

    protected boolean isSentToFk(List<Status> statuses) {
        return statuses != null && statuses.stream().filter(Objects::nonNull)
                .filter(s -> CertificateState.SENT.equals(s.getType()) && PartKod.FKASSA.getValue().equals(s.getTarget())).findAny().isPresent();
    }

    protected String nullSafeString(String string) {
        return string != null ? string : "";
    }

    protected String nullSafeString(InternalDate date) {
        return date != null ? date.getDate() : "";
    }

    protected Diagnos safeGetDiagnos(SitUtlatande intyg, int index) {
        if (index < intyg.getDiagnoser().size()) {
            return intyg.getDiagnoser().get(index);
        }
        return Diagnos.create("", "", "", "");
    }

    protected boolean safeBoolean(Boolean b) {
        return b != null && b;
    }

    protected String concatStringList(List<String> strings) {
        StringJoiner sj = new StringJoiner(", ");
        for (String s : strings) {
            sj.add(s);
        }
        return sj.toString();
    }

    protected String buildVardEnhetAdress(Vardenhet ve) {
        StringBuilder sb = new StringBuilder();
        sb.append(nullSafeString(ve.getEnhetsnamn())).append("\n")
                .append(nullSafeString(ve.getPostadress())).append("\n")
                .append(nullSafeString(ve.getPostnummer())).append(" ").append(nullSafeString(ve.getPostort())).append("\n");
        if (nullSafeString(ve.getTelefonnummer()).length() > 0) {
            sb.append("Telefon: ").append(nullSafeString(ve.getTelefonnummer()));
        }

        return sb.toString();

    }
}
