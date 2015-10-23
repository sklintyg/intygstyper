package se.inera.certificate.modules.sjukersattning.model.converter;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.junit.Test;

import se.inera.certificate.model.InternalDate;
import se.inera.certificate.modules.fkparent.model.converter.IntygGrundDataBuilder;
import se.inera.certificate.modules.sjukersattning.model.internal.Funktionsnedsattning;
import se.inera.certificate.modules.sjukersattning.model.internal.SjukersattningUtlatande;
import se.inera.certificate.modules.sjukersattning.model.internal.Underlag;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateType;

public class TransportToInternalTest {

    @Test
    public void endToEnd() throws Exception {
        SjukersattningUtlatande originalUtlatande = getUtlatande();
        RegisterCertificateType transportCertificate = InternalToTransport.convert(originalUtlatande);
        SjukersattningUtlatande convertedIntyg = TransportToInternal.convert(transportCertificate.getIntyg());
        assertEquals(originalUtlatande, convertedIntyg);
    }

    private SjukersattningUtlatande getUtlatande() {
        SjukersattningUtlatande utlatande = new SjukersattningUtlatande();
        utlatande.setId("1234567");
        utlatande.setGrundData(IntygGrundDataBuilder.getGrundData());
        utlatande.setUndersokningAvPatienten(new InternalDate(new LocalDate()));
        utlatande.setKannedomOmPatient(new InternalDate(new LocalDate()));
        utlatande.getUnderlag().add(new Underlag(Underlag.UnderlagsTyp.OVRIGT, new InternalDate(new LocalDate()), false));
        utlatande.getUnderlag().add(new Underlag(Underlag.UnderlagsTyp.UNDERLAG_FRAN_ARBETSTERAPEUT, new InternalDate(new LocalDate().plusWeeks(2)), true));
        utlatande.setDiagnosKod1("S47");
        utlatande.setDiagnosKodsystem1("ICD-10-SE");
        utlatande.setDiagnosBeskrivning1("Klämskada skuldra");
        utlatande.setDiagnosKod2("S48");
        utlatande.setDiagnosBeskrivning2("Klämskada arm");
        utlatande.setDiagnosKodsystem2("PP-CODES");
        utlatande.setBehandlingsAtgardKod1("ABC");
        utlatande.setBehandlingsAtgardBeskrivning1("Kristallterapi");
        utlatande.getFunktionsnedsattnings().add(new Funktionsnedsattning(Funktionsnedsattning.Funktionsomrade.ANNAN_KROPPSLIG, "Kan inte smida"));
        utlatande.getFunktionsnedsattnings().add(new Funktionsnedsattning(Funktionsnedsattning.Funktionsomrade.ANNAN_PSYKISK, "Lite ledsen"));
        utlatande.setAktivitetsbegransning("Väldigt sjuk");
        utlatande.setDiagnostisering("Helt galen");
        utlatande.setPagaendeBehandling("Medicin");
        utlatande.setAvslutadBehandling("Gammal medicin");
        utlatande.setPlaneradBehandling("Mer medicin");
        utlatande.setVadPatientenKanGora("Dansa");
        utlatande.setPrognosNarPatientKanAterga("Aldrig");
        utlatande.setKommentar("Trevlig kille");
        utlatande.setKontaktMedFk(false);
        return utlatande;
    }

}
