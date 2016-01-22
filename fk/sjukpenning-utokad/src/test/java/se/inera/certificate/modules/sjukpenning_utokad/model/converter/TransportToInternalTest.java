package se.inera.certificate.modules.sjukpenning_utokad.model.converter;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;

import se.inera.certificate.modules.fkparent.model.converter.IntygTestDataBuilder;
import se.inera.certificate.modules.sjukpenning_utokad.model.internal.Diagnos;
import se.inera.certificate.modules.sjukpenning_utokad.model.internal.SjukpenningUtokadUtlatande;
import se.inera.certificate.modules.sjukpenning_utokad.model.internal.Tillaggsfraga;
import se.inera.intyg.common.support.model.InternalDate;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateType;

public class TransportToInternalTest {

//    @Test
    public void endToEnd() throws Exception {
        SjukpenningUtokadUtlatande originalUtlatande = getUtlatande();
        RegisterCertificateType transportCertificate = InternalToTransport.convert(originalUtlatande);
        SjukpenningUtokadUtlatande convertedIntyg = TransportToInternal.convert(transportCertificate.getIntyg());
        assertEquals(originalUtlatande, convertedIntyg);
    }

    public static SjukpenningUtokadUtlatande getUtlatande() {
        SjukpenningUtokadUtlatande.Builder utlatande = SjukpenningUtokadUtlatande.builder();
        utlatande.setId("1234567");
        utlatande.setGrundData(IntygTestDataBuilder.getGrundData());
        utlatande.setUndersokningAvPatienten(new InternalDate(new LocalDate()));
        utlatande.setDiagnoser(asList((Diagnos.create("S47", "ICD_10_SE", "Klämskada skuldra")), Diagnos.create("S48", "ICD_10_SE", "Klämskada arm")));
        utlatande.setAktivitetsbegransning("Väldigt sjuk");
        utlatande.setPagaendeBehandling("Medicin");
        utlatande.setPlaneradBehandling("Mer medicin");
        utlatande.setOvrigt("Trevlig kille");
        utlatande.setKontaktMedFk(true);
        utlatande.setAnledningTillKontakt("Känner mig ensam");
        utlatande.setTillaggsfragor(asList(Tillaggsfraga.create("9001", "Svar text 1"), Tillaggsfraga.create("9002", "Svar text 2")));
        return utlatande.build();
    }

}
