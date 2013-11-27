/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Certificate Modules (http://code.google.com/p/inera-certificate-modules).
 *
 * Inera Certificate Modules is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Inera Certificate Modules is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.certificate.modules.rli.model.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.model.HosPersonal;
import se.inera.certificate.model.Id;
import se.inera.certificate.model.Kod;
import se.inera.certificate.model.PartialInterval;
import se.inera.certificate.model.Patient;
import se.inera.certificate.model.Vardenhet;
import se.inera.certificate.model.Vardgivare;
import se.inera.certificate.modules.rli.model.external.Arrangemang;
import se.inera.certificate.modules.rli.model.external.Utlatande;
import se.inera.certificate.modules.rli.model.internal.mi.HoSPersonal;

/**
 * Unit test for the ExternalToInteralConverter. This test is Spring configured.
 * 
 * @author Niklas Pettersson, R2M
 * 
 */
@ContextConfiguration(locations = ("/rli-test-config.xml"))
@RunWith(SpringJUnit4ClassRunner.class)
public class ExternalToInternalConverterTest {

    @Autowired
    private ExternalToInternalConverter converter;

    @Test
    public void testConvertUtlatande() throws Exception {

        Utlatande extUtlatande = readUtlatandeFromJsonFile("/rli-example-1.json");

        se.inera.certificate.modules.rli.model.internal.mi.Utlatande res = converter
                .convertUtlatandeFromExternalToInternal(extUtlatande);

        assertNotNull(res);

        assertNotNull(res.getUtlatandeid());
        assertNotNull(res.getSigneringsdatum());
        assertNotNull(res.getSkickatdatum());
        assertNotNull(res.getTypAvUtlatande());

        assertNotNull(res.getArrangemang());
        assertNotNull(res.getPatient());
        assertNotNull(res.getSkapadAv());
        assertNotNull(res.getUndersokning());
        assertNotNull(res.getRekommendation());
    }

    private Utlatande readUtlatandeFromJsonFile(String fileName) {

        Utlatande extUtlatande = null;

        InputStream src = this.getClass().getResourceAsStream(fileName);

        try {
            CustomObjectMapper mapper = new CustomObjectMapper();
            extUtlatande = mapper.readValue(src, Utlatande.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return extUtlatande;
    }

    @Test
    public void testConvertToIntPatient()  throws Exception {

        Patient extPatient = buildPatient();

        se.inera.certificate.modules.rli.model.internal.mi.Patient res = converter.convertToIntPatient(extPatient);

        assertNotNull(res);

        assertEquals("19121212-1212", res.getPersonid());
        assertEquals("Abel Baker", res.getFornamn());
        assertEquals("Smith Doe", res.getEfternamn());
        assertEquals("Abel Baker Smith Doe", res.getFullstandigtNamn());
        assertNotNull(res.getPostadress());
        assertNotNull(res.getPostnummer());
        assertNotNull(res.getPostort());

    }

    private Patient buildPatient() {

        Patient pat = new Patient();

        pat.setId(new Id("PersonId", "19121212-1212"));
        pat.getFornamn().addAll(Arrays.asList("Abel", "Baker"));
        pat.setEfternamn("Smith Doe");
        pat.setPostadress("Testgatan 123");
        pat.setPostnummer("123 45");
        pat.setPostort("Teststaden");

        return pat;
    }

    @Test
    public void testConvertToIntArrangemang() throws Exception {

        Arrangemang extArr = buildArrangemang();
        se.inera.certificate.modules.rli.model.internal.mi.Arrangemang res = converter.convertToIntArrangemang(extArr);

        assertNotNull(res);
        assertNotNull(res.getArrangemangstyp());
        assertNotNull(res.getBokningsreferens());
        assertNotNull(res.getBokningsdatum());
        assertNotNull(res.getAvbestallningsdatum());
        assertNotNull(res.getArrangemangsdatum());
    }

    private Arrangemang buildArrangemang() {

        Arrangemang arr = new Arrangemang();
        arr.setArrangemangstyp(new Kod("420008001"));
        arr.setBokningsreferens("1234567-890");
        arr.setPlats("Långtbortistan");
        arr.setBokningsdatum(TestUtils.constructPartial(2013, 8, 6));
        arr.setAvbestallningsdatum(TestUtils.constructPartial(2013, 8, 22));

        PartialInterval arrTid = new PartialInterval();
        arrTid.setFrom(TestUtils.constructPartial(2013, 9, 1));
        arrTid.setTom(TestUtils.constructPartial(2013, 9, 16));

        arr.setArrangemangstid(arrTid);

        return arr;
    }

    @Test
    public void testConvertToIntHoSPersonal() throws Exception {

        HosPersonal extHoSPersonal = buildHoSPersonal();
        HoSPersonal res = converter.convertToIntHoSPersonal(extHoSPersonal);

        assertNotNull(res);
        assertNotNull(res.getFullstandigtNamn());
        assertNotNull(res.getPersonid());

    }

    private HosPersonal buildHoSPersonal() {

        HosPersonal hosPers = new HosPersonal();

        hosPers.setId(new Id(null, "19101010-1010"));
        hosPers.setNamn("Börje Dengroth");
        hosPers.setForskrivarkod("12345-67");

        Vardenhet vardenhet = buildVardenhet();
        hosPers.setVardenhet(vardenhet);

        return hosPers;
    }

    @Test
    public void testConvertToIntVardenhet() throws Exception {

        Vardenhet extVardenhet = buildVardenhet();
        se.inera.certificate.modules.rli.model.internal.mi.Vardenhet res = converter.convertToIntVardenhet(extVardenhet);

        assertNotNull(res);

        assertNotNull(res.getEnhetsid());
        assertNotNull(res.getEnhetsnamn());
        assertNotNull(res.getPostadress());
        assertNotNull(res.getPostnummer());
        assertNotNull(res.getPostort());
        assertNotNull(res.getEpost());
        assertNotNull(res.getTelefonnummer());
        assertNotNull(res.getVardgivare());
    }

    private Vardenhet buildVardenhet() {

        Vardenhet vardenhet = new Vardenhet();

        vardenhet.setId(new Id(null, "123-456"));
        vardenhet.setArbetsplatskod(new Id(null, "1234-56"));
        vardenhet.setNamn("Tolvberga Vårdcentral");
        vardenhet.setPostadress("Nollstigen 12");
        vardenhet.setPostnummer("12345");
        vardenhet.setPostort("Tolvberga");
        vardenhet.setTelefonnummer("012-345678");
        vardenhet.setEpost("ingen@alls.nu");

        Vardgivare vardgivare = buildVardgivare();
        vardenhet.setVardgivare(vardgivare);

        return vardenhet;
    }

    private Vardgivare buildVardgivare() {

        Vardgivare vardgivare = new Vardgivare();

        vardgivare.setId(new Id(null, "1234567"));
        vardgivare.setNamn("Landstinget");

        return vardgivare;

    }

}
