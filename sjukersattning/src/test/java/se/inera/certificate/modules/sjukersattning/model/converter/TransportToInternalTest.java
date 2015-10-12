package se.inera.certificate.modules.sjukersattning.model.converter;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.junit.Test;

import se.inera.certificate.model.InternalDate;
import se.inera.certificate.modules.fkparent.model.converter.IntygGrundDataBuilder;
import se.inera.certificate.modules.sjukersattning.model.internal.SjukersattningUtlatande;
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
        utlatande.setDiagnosKod1("S47");
        utlatande.setDiagnosBeskrivning1("Klämskada skuldra");
        utlatande.setDiagnosYtterligareBeskrivning1("Måste få hjälp!");
        utlatande.setDiagnosKod2("S48");
        utlatande.setDiagnosBeskrivning2("Klämskada arm");
        utlatande.setDiagnosYtterligareBeskrivning2("Nämnde jag hjälp?");
        utlatande.setFunktionsnedsattning("Kan inte smida");
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
