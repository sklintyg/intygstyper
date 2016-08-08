package se.inera.intyg.intygstyper.fkparent.model.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.riv.clinicalprocess.healthcond.certificate.revokeCertificate.v1.RevokeCertificateType;

public class InternalToRevokeTest {

    @Test
    public void convertRevokeCertificateRequest() throws Exception {
        String meddelande = "meddelande";
        Utlatande utlatande = IntygTestDataBuilder.getUtlatande();
        HoSPersonal skapatAv = utlatande.getGrundData().getSkapadAv();
        RevokeCertificateType res = InternalToRevoke.convert(utlatande, skapatAv, meddelande);

        assertNotNull(res.getIntygsId().getRoot());
        assertEquals(utlatande.getId(), res.getIntygsId().getExtension());
        assertEquals(meddelande, res.getMeddelande());
        assertEquals("1.2.752.129.2.1.3.1", res.getPatientPersonId().getRoot());
        assertEquals(utlatande.getGrundData().getPatient().getPersonId().getPersonnummerWithoutDash(),res.getPatientPersonId().getExtension());
        assertEquals(skapatAv.getPersonId(), res.getSkickatAv().getPersonalId().getExtension());
        assertEquals(skapatAv.getFullstandigtNamn(), res.getSkickatAv().getFullstandigtNamn());
        assertNotNull(res.getSkickatAv().getEnhet());
        assertNotNull(res.getSkickatTidpunkt());
    }
}
