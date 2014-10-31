package se.inera.certificate.modules.support.api.dto;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PatientTest {

    @Test
    public void testFullstandigtNamnWithMellannamn() {
        Patient patient = new Patient("Test", "Svensson", "Testsson", "19121212-1212", null, null, null);
        assertEquals("Test Svensson Testsson", patient.getFullstandigtNamn());
    }

    @Test
    public void testFullstandigtNamnWithoutMellannamn() {
        Patient patient = new Patient("Test", null, "Testsson", "19121212-1212", null, null, null);
        assertEquals("Test Testsson", patient.getFullstandigtNamn());
    }
}
