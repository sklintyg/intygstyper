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

package se.inera.intyg.intygstyper.ts_bas.model.converter.util;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import se.inera.intyg.common.support.modules.support.api.CertificateHolder;
import se.inera.intyg.common.util.integration.integration.json.CustomObjectMapper;
import se.inera.intyg.intygstyper.ts_bas.model.internal.Utlatande;

public class ConverterUtilTest {

    private ObjectMapper objectMapper = new CustomObjectMapper();

    private String jsonString = "{\"id\":\"7cc59f3e-ed60-4f79-8f3c-a863a8e43c50\",\"typ\":\"TS_BAS_U06_V06\",\"grundData\":{\"signeringsdatum\":\"2015-03-17T11:26:24.000\",\"skapadAv\":{\"personId\":\"IFV1239877878-1049\",\"fullstandigtNamn\":\"Jan Nilsson\",\"befattningar\":[\"\"],\"specialiteter\":[\"\"],\"vardenhet\":{\"enhetsid\":\"IFV1239877878-1042\",\"enhetsnamn\":\"WebCert-Enhet1\",\"postadress\":\"Storgatan 1\",\"postnummer\":\"12345\",\"postort\":\"Småmåla\",\"telefonnummer\":\"0101234567890\",\"vardgivare\":{\"vardgivarid\":\"IFV1239877878-1041\",\"vardgivarnamn\":\"WebCert-Vårdgivare1\"}}},\"patient\":{\"personId\":\"19121212-1212\",\"fullstandigtNamn\":\"Tolvan Tolvansson\",\"fornamn\":\"Tolvan\",\"efternamn\":\"Tolvansson\",\"postadress\":\"Svensson, Storgatan 1, PL 1234\",\"postnummer\":\"12345\",\"postort\":\"Småmåla\",\"samordningsNummer\":false}},\"vardkontakt\":{\"typ\":\"5880005\",\"idkontroll\":\"ID_KORT\"},\"intygAvser\":{\"korkortstyp\":[{\"type\":\"C1\",\"selected\":true},{\"type\":\"C1E\",\"selected\":false},{\"type\":\"C\",\"selected\":false},{\"type\":\"CE\",\"selected\":false},{\"type\":\"D1\",\"selected\":false},{\"type\":\"D1E\",\"selected\":false},{\"type\":\"D\",\"selected\":false},{\"type\":\"DE\",\"selected\":false},{\"type\":\"TAXI\",\"selected\":false},{\"type\":\"ANNAT\",\"selected\":false}]},\"syn\":{\"synfaltsdefekter\":false,\"nattblindhet\":false,\"progressivOgonsjukdom\":false,\"diplopi\":false,\"nystagmus\":false,\"hogerOga\":{\"utanKorrektion\":1.0,\"medKorrektion\":1.0},\"vansterOga\":{\"utanKorrektion\":1.0,\"medKorrektion\":1.0},\"binokulart\":{\"utanKorrektion\":1.0,\"medKorrektion\":1.1}},\"horselBalans\":{\"balansrubbningar\":false},\"funktionsnedsattning\":{\"funktionsnedsattning\":false},\"hjartKarl\":{\"hjartKarlSjukdom\":false,\"hjarnskadaEfterTrauma\":false,\"riskfaktorerStroke\":false},\"diabetes\":{\"harDiabetes\":false},\"neurologi\":{\"neurologiskSjukdom\":false},\"medvetandestorning\":{\"medvetandestorning\":false},\"njurar\":{\"nedsattNjurfunktion\":false},\"kognitivt\":{\"sviktandeKognitivFunktion\":false},\"somnVakenhet\":{\"teckenSomnstorningar\":false},\"narkotikaLakemedel\":{\"teckenMissbruk\":false,\"foremalForVardinsats\":false,\"lakarordineratLakemedelsbruk\":false},\"psykiskt\":{\"psykiskSjukdom\":false},\"utvecklingsstorning\":{\"psykiskUtvecklingsstorning\":false,\"harSyndrom\":false},\"sjukhusvard\":{\"sjukhusEllerLakarkontakt\":false},\"medicinering\":{\"stadigvarandeMedicinering\":false},\"bedomning\":{\"korkortstyp\":[{\"type\":\"C1\",\"selected\":true},{\"type\":\"C1E\",\"selected\":false},{\"type\":\"C\",\"selected\":false},{\"type\":\"CE\",\"selected\":false},{\"type\":\"D1\",\"selected\":false},{\"type\":\"D1E\",\"selected\":false},{\"type\":\"D\",\"selected\":false},{\"type\":\"DE\",\"selected\":false},{\"type\":\"TAXI\",\"selected\":false},{\"type\":\"ANNAT\",\"selected\":false}]}}";

    @Test
    public void testConvertFromUtlatande() throws Exception {
        Utlatande utlatande = objectMapper.readValue(jsonString, Utlatande.class);
        CertificateHolder holder = ConverterUtil.toCertificateHolder(utlatande);
        Assert.assertEquals("7cc59f3e-ed60-4f79-8f3c-a863a8e43c50", holder.getId());
        Assert.assertEquals("IFV1239877878-1042", holder.getCareUnitId());
        Assert.assertEquals("IFV1239877878-1041", holder.getCareGiverId());
    }
}
