package se.inera.certificate.modules.sjukersattning.model.converter;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.junit.Test;

import se.inera.certificate.modules.fkparent.model.converter.IntygTestDataBuilder;
import se.inera.certificate.modules.sjukersattning.model.internal.*;
import se.inera.intyg.common.support.model.InternalDate;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateType;

public class TransportToInternalTest {

    @Test
    public void endToEnd() throws Exception {
        SjukersattningUtlatande originalUtlatande = getUtlatande();
        RegisterCertificateType transportCertificate = InternalToTransport.convert(originalUtlatande);
        SjukersattningUtlatande convertedIntyg = TransportToInternal.convert(transportCertificate.getIntyg());
        assertEquals(originalUtlatande, convertedIntyg);
    }

    public static SjukersattningUtlatande getUtlatande() {
        SjukersattningUtlatande.Builder utlatande = SjukersattningUtlatande.builder();
        utlatande.setId("1234567");
        utlatande.setGrundData(IntygTestDataBuilder.getGrundData());
        utlatande.setUndersokningAvPatienten(new InternalDate(new LocalDate()));
        utlatande.setKannedomOmPatient(new InternalDate(new LocalDate()));
        utlatande.setUnderlagFinns(true);
        utlatande.setUnderlag(asList(Underlag.create(Underlag.UnderlagsTyp.OVRIGT, new InternalDate(new LocalDate()), "plats 1"),
                Underlag.create(Underlag.UnderlagsTyp.UNDERLAG_FRAN_ARBETSTERAPEUT, new InternalDate(new LocalDate().plusWeeks(2)), "plats 2")));
        utlatande.setSjukdomsforlopp("Snabbt");
        utlatande.setDiagnoser(asList((Diagnos.create("S47", "ICD_10_SE", "Klämskada skuldra")), Diagnos.create("S48", "ICD_10_SE", "Klämskada arm")));
        utlatande.setDiagnosgrund("Ingen som vet");
        utlatande.setNyBedomningDiagnosgrund(true);
        utlatande.setFunktionsnedsattningIntellektuell("Bra");
        utlatande.setFunktionsnedsattningKommunikation("Tyst");
        utlatande.setFunktionsnedsattningKoncentration("Noll");
        utlatande.setFunktionsnedsattningPsykisk("Lite ledsen");
        utlatande.setFunktionsnedsattningSynHorselTal("Vitt");
        utlatande.setFunktionsnedsattningBalansKoordination("Tyst");
        utlatande.setFunktionsnedsattningAnnan("Kan inte smida");
        utlatande.setAktivitetsbegransning("Väldigt sjuk");
        utlatande.setPagaendeBehandling("Medicin");
        utlatande.setAvslutadBehandling("Gammal medicin");
        utlatande.setPlaneradBehandling("Mer medicin");
        utlatande.setMedicinskaForutsattningarForArbete("Svårt");
        utlatande.setAktivitetsFormaga("Dansa");
        utlatande.setOvrigt("Trevlig kille");
        utlatande.setKontaktMedFk(true);
        utlatande.setAnledningTillKontakt("Känner mig ensam");
        return utlatande.build();
    }

}
