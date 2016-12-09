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

package se.inera.intyg.intygstyper.fk7263.schemas.insuranceprocess.healthreporting.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import iso.v21090.dt.v1.II;
import se.inera.ifv.insuranceprocess.healthreporting.medcertqa.v1.VardAdresseringsType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.*;
import se.inera.intyg.common.support.Constants;

public class VardAdresseringsTypeValidatorTest {

    @Test
    public void testValidateAndCorrectOk() {
        List<String> validationErrors = new ArrayList<>();
        new VardAdresseringsTypeValidator(buildVardAdresseringsType(), validationErrors ).validateAndCorrect();

        assertTrue(validationErrors.isEmpty());
    }

    @Test
    public void testValidateAndCorrectNoVardAdress() {
        List<String> validationErrors = new ArrayList<>();
        new VardAdresseringsTypeValidator(null, validationErrors ).validateAndCorrect();

        assertEquals(1, validationErrors.size());
        assertEquals("No vardAdress element found!", validationErrors.get(0));
    }

    @Test
    public void testValidateAndCorrectNoHosPersonal() {
        List<String> validationErrors = new ArrayList<>();
        new VardAdresseringsTypeValidator(new VardAdresseringsType(), validationErrors).validateAndCorrect();

        assertEquals(1, validationErrors.size());
        assertEquals("No SkapadAvHosPersonal element found!", validationErrors.get(0));
    }

    @Test
    public void testValidateAndCorrectPersonalIdMissing() {
        VardAdresseringsType adressVard = buildVardAdresseringsType();
        List<String> validationErrors = new ArrayList<>();

        adressVard.getHosPersonal().getPersonalId().setExtension(null);
        new VardAdresseringsTypeValidator(adressVard, validationErrors).validateAndCorrect();

        assertEquals(1, validationErrors.size());
        assertEquals("No personal-id found!", validationErrors.get(0));
    }

    @Test
    public void testValidateAndCorrectPersonalIdRootMissing() {
        VardAdresseringsType adressVard = buildVardAdresseringsType();
        List<String> validationErrors = new ArrayList<>();

        adressVard.getHosPersonal().getPersonalId().setRoot(null);
        new VardAdresseringsTypeValidator(adressVard, validationErrors).validateAndCorrect();

        assertEquals(1, validationErrors.size());
        assertTrue(validationErrors.get(0).startsWith("Wrong o.i.d. for personalId!"));
    }

    @Test
    public void testValidateAndCorrectPersonalIdRootWrong() {
        VardAdresseringsType adressVard = buildVardAdresseringsType();
        List<String> validationErrors = new ArrayList<>();

        adressVard.getHosPersonal().getPersonalId().setRoot("wrong");
        new VardAdresseringsTypeValidator(adressVard, validationErrors).validateAndCorrect();

        assertEquals(1, validationErrors.size());
        assertTrue(validationErrors.get(0).startsWith("Wrong o.i.d. for personalId!"));
    }

    @Test
    public void testValidateAndCorrectHosPersonalFullstandigtNamnMissing() {
        VardAdresseringsType adressVard = buildVardAdresseringsType();
        List<String> validationErrors = new ArrayList<>();

        adressVard.getHosPersonal().setFullstandigtNamn(null);
        new VardAdresseringsTypeValidator(adressVard, validationErrors).validateAndCorrect();

        assertEquals(1, validationErrors.size());
        assertEquals("No skapadAvHosPersonal fullstandigtNamn found.", validationErrors.get(0));
    }

    @Test
    public void testValidateAndCorrectEnhetMissing() {
        VardAdresseringsType adressVard = buildVardAdresseringsType();
        List<String> validationErrors = new ArrayList<>();

        adressVard.getHosPersonal().setEnhet(null);
        new VardAdresseringsTypeValidator(adressVard, validationErrors).validateAndCorrect();

        assertEquals(1, validationErrors.size());
        assertEquals("No enhet element found!", validationErrors.get(0));
    }

    @Test
    public void testValidateAndCorrectEnhetIIMissing() {
        VardAdresseringsType adressVard = buildVardAdresseringsType();
        List<String> validationErrors = new ArrayList<>();

        adressVard.getHosPersonal().getEnhet().setEnhetsId(null);
        new VardAdresseringsTypeValidator(adressVard, validationErrors).validateAndCorrect();

        assertEquals(2, validationErrors.size());
        assertEquals("No enhets-id found!", validationErrors.get(0));
        assertTrue(validationErrors.get(1).startsWith("Wrong o.i.d. for enhetsId!"));
    }

    @Test
    public void testValidateAndCorrectEnhetidMissing() {
        VardAdresseringsType adressVard = buildVardAdresseringsType();
        List<String> validationErrors = new ArrayList<>();

        adressVard.getHosPersonal().getEnhet().getEnhetsId().setExtension(null);
        new VardAdresseringsTypeValidator(adressVard, validationErrors).validateAndCorrect();

        assertEquals(1, validationErrors.size());
        assertEquals("No enhets-id found!", validationErrors.get(0));
    }

    @Test
    public void testValidateAndCorrectEnhetidRootMissing() {
        VardAdresseringsType adressVard = buildVardAdresseringsType();
        List<String> validationErrors = new ArrayList<>();

        adressVard.getHosPersonal().getEnhet().getEnhetsId().setRoot(null);
        new VardAdresseringsTypeValidator(adressVard, validationErrors).validateAndCorrect();

        assertEquals(1, validationErrors.size());
        assertTrue(validationErrors.get(0).startsWith("Wrong o.i.d. for enhetsId!"));
    }

    @Test
    public void testValidateAndCorrectEnhetidRootWrong() {
        VardAdresseringsType adressVard = buildVardAdresseringsType();
        List<String> validationErrors = new ArrayList<>();

        adressVard.getHosPersonal().getEnhet().getEnhetsId().setRoot("wrong");
        new VardAdresseringsTypeValidator(adressVard, validationErrors).validateAndCorrect();

        assertEquals(1, validationErrors.size());
        assertTrue(validationErrors.get(0).startsWith("Wrong o.i.d. for enhetsId!"));
    }

    @Test
    public void testValidateAndCorrectEnhetsnamnMissing() {
        VardAdresseringsType adressVard = buildVardAdresseringsType();
        List<String> validationErrors = new ArrayList<>();

        adressVard.getHosPersonal().getEnhet().setEnhetsnamn(null);
        new VardAdresseringsTypeValidator(adressVard, validationErrors).validateAndCorrect();

        assertEquals(1, validationErrors.size());
        assertEquals("No enhetsnamn found!", validationErrors.get(0));
    }

    @Test
    public void testValidateAndCorrectVardgivareMissing() {
        VardAdresseringsType adressVard = buildVardAdresseringsType();
        List<String> validationErrors = new ArrayList<>();

        adressVard.getHosPersonal().getEnhet().setVardgivare(null);
        new VardAdresseringsTypeValidator(adressVard, validationErrors).validateAndCorrect();

        assertEquals(1, validationErrors.size());
        assertEquals("No vardgivare element found!", validationErrors.get(0));
    }

    @Test
    public void testValidateAndCorrectVardgivareIIMissing() {
        VardAdresseringsType adressVard = buildVardAdresseringsType();
        List<String> validationErrors = new ArrayList<>();

        adressVard.getHosPersonal().getEnhet().getVardgivare().setVardgivareId(null);
        new VardAdresseringsTypeValidator(adressVard, validationErrors).validateAndCorrect();

        assertEquals(2, validationErrors.size());
        assertEquals("No vardgivare-id found!", validationErrors.get(0));
        assertTrue(validationErrors.get(1).startsWith("Wrong o.i.d. for vardgivareId!"));
    }

    @Test
    public void testValidateAndCorrectVardgivaridMissing() {
        VardAdresseringsType adressVard = buildVardAdresseringsType();
        List<String> validationErrors = new ArrayList<>();

        adressVard.getHosPersonal().getEnhet().getVardgivare().getVardgivareId().setExtension(null);
        new VardAdresseringsTypeValidator(adressVard, validationErrors).validateAndCorrect();

        assertEquals(1, validationErrors.size());
        assertEquals("No vardgivare-id found!", validationErrors.get(0));
    }

    @Test
    public void testValidateAndCorrectVardgivaridRootMissing() {
        VardAdresseringsType adressVard = buildVardAdresseringsType();
        List<String> validationErrors = new ArrayList<>();

        adressVard.getHosPersonal().getEnhet().getVardgivare().getVardgivareId().setRoot(null);
        new VardAdresseringsTypeValidator(adressVard, validationErrors).validateAndCorrect();

        assertEquals(1, validationErrors.size());
        assertTrue(validationErrors.get(0).startsWith("Wrong o.i.d. for vardgivareId!"));
    }

    @Test
    public void testValidateAndCorrectVardgivaridRootWrong() {
        VardAdresseringsType adressVard = buildVardAdresseringsType();
        List<String> validationErrors = new ArrayList<>();

        adressVard.getHosPersonal().getEnhet().getVardgivare().getVardgivareId().setRoot("wrong");
        new VardAdresseringsTypeValidator(adressVard, validationErrors).validateAndCorrect();

        assertEquals(1, validationErrors.size());
        assertTrue(validationErrors.get(0).startsWith("Wrong o.i.d. for vardgivareId!"));
    }

    @Test
    public void testValidateAndCorrectVardgivarnamnMissing() {
        VardAdresseringsType adressVard = buildVardAdresseringsType();
        List<String> validationErrors = new ArrayList<>();

        adressVard.getHosPersonal().getEnhet().getVardgivare().setVardgivarnamn(null);
        new VardAdresseringsTypeValidator(adressVard, validationErrors).validateAndCorrect();

        assertEquals(1, validationErrors.size());
        assertEquals("No vardgivarenamn found!", validationErrors.get(0));
    }

    private VardAdresseringsType buildVardAdresseringsType() {
        VardAdresseringsType adressVard = new VardAdresseringsType();
        adressVard.setHosPersonal(new HosPersonalType());
        adressVard.getHosPersonal().setPersonalId(new II());
        adressVard.getHosPersonal().getPersonalId().setRoot(Constants.HSA_ID_OID);
        adressVard.getHosPersonal().getPersonalId().setExtension("personalId");
        adressVard.getHosPersonal().setFullstandigtNamn("fullstandigt namn");
        adressVard.getHosPersonal().setEnhet(new EnhetType());
        adressVard.getHosPersonal().getEnhet().setEnhetsId(new II());
        adressVard.getHosPersonal().getEnhet().getEnhetsId().setRoot(Constants.HSA_ID_OID);
        adressVard.getHosPersonal().getEnhet().getEnhetsId().setExtension("enhetsid");
        adressVard.getHosPersonal().getEnhet().setEnhetsnamn("enhetsnamn");
        adressVard.getHosPersonal().getEnhet().setVardgivare(new VardgivareType());
        adressVard.getHosPersonal().getEnhet().getVardgivare().setVardgivareId(new II());
        adressVard.getHosPersonal().getEnhet().getVardgivare().getVardgivareId().setRoot(Constants.HSA_ID_OID);
        adressVard.getHosPersonal().getEnhet().getVardgivare().getVardgivareId().setExtension("vardgivarid");
        adressVard.getHosPersonal().getEnhet().getVardgivare().setVardgivarnamn("vardgivarnamn");
        return adressVard;
    }
}
