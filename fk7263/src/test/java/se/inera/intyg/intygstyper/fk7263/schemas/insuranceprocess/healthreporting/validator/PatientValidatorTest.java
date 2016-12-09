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

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import iso.v21090.dt.v1.II;
import se.inera.ifv.insuranceprocess.healthreporting.v2.PatientType;

public class PatientValidatorTest {
    private PatientType patient;
    private II patientId;

    @Before
    public void setup() {
        patient = new PatientType();
        patientId = new II();
        patientId.setRoot("1.2.752.129.2.1.3.1");
        patientId.setExtension("19121212-1212");
        patient.setPersonId(patientId);
        patient.setFullstandigtNamn("namn");
    }

    @Test
    public void testNullPatient() {
        patient = null;
        assertTrue(validatePatient().contains("No Patient element found!"));
    }

    @Test
    public void testNullPatientId() {
        patient.setPersonId(null);
        assertTrue(validatePatient().contains("No Patient Id found!"));
    }

    @Test
    public void testEmtpyPatientIdRoot() {
        patientId.setRoot(null);
        assertTrue(validatePatient().contains("Wrong o.i.d. for Patient Id! Should be 1.2.752.129.2.1.3.1 or 1.2.752.129.2.1.3.3"));
    }

    private List<String> validatePatient() {
        return PatientValidator.validateAndCorrect(patient);
    }

    @Test
    public void testIncorrectPatientIdRoot() {
        patientId.setRoot("incorrect");
        assertTrue(validatePatient().contains("Wrong o.i.d. for Patient Id! Should be 1.2.752.129.2.1.3.1 or 1.2.752.129.2.1.3.3"));
    }

    @Test
    public void testEmtpyPatientIdExtension() {
        patientId.setExtension(null);
        assertTrue(validatePatient().contains("No Patient Id found!"));
    }

    @Test
    public void testInvalidPatientIdExtension() {
        patientId.setExtension("19121212-121X");
        assertTrue(validatePatient().contains("Wrong format for person-id! Valid format is YYYYMMDD-XXXX or YYYYMMDD+XXXX."));
    }

    @Test
    public void testDashInPatientIdExtensionIsCorrected() {
        patientId.setExtension("191212121212");
        assertTrue(validatePatient().isEmpty());
        assertEquals("19121212-1212", patientId.getExtension());
    }

}
