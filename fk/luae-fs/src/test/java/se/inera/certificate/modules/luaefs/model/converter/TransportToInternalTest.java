package se.inera.certificate.modules.luaefs.model.converter;

import org.joda.time.LocalDate;
import org.junit.Test;
import se.inera.certificate.modules.fkparent.model.converter.IntygTestDataBuilder;
import se.inera.certificate.modules.fkparent.model.internal.Diagnos;
import se.inera.certificate.modules.luaefs.model.internal.LuaefsUtlatande;
import se.inera.certificate.modules.luaefs.model.internal.Tillaggsfraga;
import se.inera.intyg.common.support.model.InternalDate;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateType;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class TransportToInternalTest {

    @Test
    public void endToEnd() throws Exception {
        LuaefsUtlatande originalUtlatande = getUtlatande();
        RegisterCertificateType transportCertificate = InternalToTransport.convert(originalUtlatande);
        LuaefsUtlatande convertedIntyg = TransportToInternal.convert(transportCertificate.getIntyg());
        assertEquals(originalUtlatande, convertedIntyg);
    }

    public static LuaefsUtlatande getUtlatande() {
        LuaefsUtlatande.Builder utlatande = LuaefsUtlatande.builder();
        utlatande.setId("1234567");
        utlatande.setGrundData(IntygTestDataBuilder.getGrundData());
        utlatande.setTextVersion("1.0");
        utlatande.setUndersokningAvPatienten(new InternalDate(new LocalDate()));
        utlatande.setDiagnoser(asList((Diagnos.create("S47", "ICD_10_SE", "Klämskada skuldra", "Klämskada skuldra")), Diagnos.create("S48", "ICD_10_SE", "Klämskada arm", "Klämskada arm")));
//        utlatande.setAktivitetsbegransning("Väldigt sjuk");
//        utlatande.setPagaendeBehandling("Medicin");
//        utlatande.setPlaneradBehandling("Mer medicin");
//
//        utlatande.setArbetslivsinriktadeAtgarder(asList(ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.ARBETSANPASSNING)));
//
//        utlatande.setSysselsattning(Sysselsattning.create(SysselsattningsTyp.STUDIER));
//        utlatande.setPrognos(Prognos.create(PrognosTyp.MED_STOR_SANNOLIKHET, "Förtydligande"));
//        utlatande.setFunktionsnedsattning("Funktionsnedsättning");

        utlatande.setOvrigt("Trevlig kille");
        utlatande.setKontaktMedFk(true);
        utlatande.setAnledningTillKontakt("Känner mig ensam");
        utlatande.setTillaggsfragor(asList(Tillaggsfraga.create("9001", "Svar text 1"), Tillaggsfraga.create("9002", "Svar text 2")));
        return utlatande.build();
    }

}
