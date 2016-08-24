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

package se.inera.intyg.intygstyper.ts_diabetes.model.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intygstjanster.ts.services.types.v1.II;
import se.inera.intygstjanster.ts.services.v1.*;

public class TransportToInternalConverterTest {

    private static final String ENHETSNAMN = "enhetsnamn";
    private static final String ENHETSID = "enhetsid";
    private static final String VARDGIVARNAMN = "vardgivarnamn";
    private static final String POSTADRESS = "postadress";
    private static final String POSTNUMMER = "postnummer";
    private static final String POSTORT = "postort";
    private static final String TELEFONNUMMER = "telefonnummer";
    private static final String VARDGIVARID = "vardgivarid";
    private static final List<String> SPECIALIST_KOMPETENS = Arrays.asList("a", "b", "c");
    private static final String FULLSTANDIGT_NAMN = "test testorsson";
    private static final String PERSONID = "personid";

    @Test
    public void testReadSkapadAv() {
        SkapadAv skapadAv = buildSkapadAv(SPECIALIST_KOMPETENS);
        HoSPersonal hosPersonal = TransportToInternalConverter.readSkapadAv(skapadAv);
        assertEquals(ENHETSNAMN, hosPersonal.getVardenhet().getEnhetsnamn());
        assertEquals(ENHETSID, hosPersonal.getVardenhet().getEnhetsid());
        assertEquals(VARDGIVARNAMN, hosPersonal.getVardenhet().getVardgivare().getVardgivarnamn());
        assertEquals(POSTADRESS, hosPersonal.getVardenhet().getPostadress());
        assertEquals(POSTNUMMER, hosPersonal.getVardenhet().getPostnummer());
        assertEquals(POSTORT, hosPersonal.getVardenhet().getPostort());
        assertEquals(TELEFONNUMMER, hosPersonal.getVardenhet().getTelefonnummer());
        assertEquals(VARDGIVARID, hosPersonal.getVardenhet().getVardgivare().getVardgivarid());
        assertEquals(FULLSTANDIGT_NAMN, hosPersonal.getFullstandigtNamn());
        assertEquals(PERSONID, hosPersonal.getPersonId());
        assertEquals(SPECIALIST_KOMPETENS, hosPersonal.getSpecialiteter());
    }

    @Test
    public void testReadSkapadAvNullSpecialistkompetens() {
        SkapadAv skapadAv = buildSkapadAv(null);
        HoSPersonal hosPersonal = TransportToInternalConverter.readSkapadAv(skapadAv);
        assertTrue(hosPersonal.getSpecialiteter().isEmpty());
    }

    private SkapadAv buildSkapadAv(List<String> specialistKompetens) {
        SkapadAv skapadAv = new SkapadAv();
        skapadAv.setPersonId(buildII(PERSONID));
        skapadAv.setFullstandigtNamn(FULLSTANDIGT_NAMN);
        if (specialistKompetens != null) {
            skapadAv.getSpecialiteter().addAll(specialistKompetens);
        }

        Vardenhet vardenhet = new Vardenhet();

        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarid(buildII(VARDGIVARID));
        vardgivare.setVardgivarnamn(VARDGIVARNAMN);
        vardenhet.setVardgivare(vardgivare);

        vardenhet.setEnhetsId(buildII(ENHETSID));
        vardenhet.setEnhetsnamn(ENHETSNAMN);
        vardenhet.setPostadress(POSTADRESS);
        vardenhet.setPostnummer(POSTNUMMER);
        vardenhet.setPostort(POSTORT);
        vardenhet.setTelefonnummer(TELEFONNUMMER);
        skapadAv.setVardenhet(vardenhet);

        return skapadAv;
    }

    private II buildII(String extension) {
        II ii = new II();
        ii.setExtension(extension);
        return ii;
    }
}