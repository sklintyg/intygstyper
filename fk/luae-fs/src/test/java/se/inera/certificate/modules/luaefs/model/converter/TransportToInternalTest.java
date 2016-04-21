package se.inera.certificate.modules.luaefs.model.converter;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.junit.Test;
import se.inera.certificate.modules.fkparent.model.converter.IntygTestDataBuilder;
import se.inera.certificate.modules.fkparent.model.internal.Diagnos;
import se.inera.certificate.modules.luaefs.model.internal.LuaefsUtlatande;
import se.inera.certificate.modules.luaefs.model.internal.Tillaggsfraga;
import se.inera.certificate.modules.luaefs.model.internal.Underlag;
import se.inera.intyg.common.support.model.InternalDate;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateType;

import java.util.Arrays;
import java.util.List;

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
        utlatande.setAnhorigsBeskrivningAvPatienten(new InternalDate("2015-10-11"));
        utlatande.setJournaluppgifter(new InternalDate("2015-10-10"));
        utlatande.setAnnanGrundForMU(new InternalDate("2015-10-12"));
        utlatande.setAnnanGrundForMUBeskrivning("Lider av svår discofobi");

        utlatande.setKannedomOmPatient(new InternalDate("2015-10-10"));
        utlatande.setUnderlagFinns(true);
        utlatande.setUnderlag(buildUnderlag());

        utlatande.setDiagnoser(asList((Diagnos.create("S47", "ICD_10_SE", "Klämskada skuldra", "Klämskada skuldra")), Diagnos.create("S48", "ICD_10_SE", "Klämskada arm", "Klämskada arm"), Diagnos.create("S49", "ICD_10_SE", "Klämskada hand", "Klämskada hand")));

        utlatande.setFunktionsnedsattningDebut("Debut 1");
        utlatande.setFunktionsnedsattningPaverkan("Påverkan 1");

        utlatande.setOvrigt("Trevlig kille");
        utlatande.setKontaktMedFk(true);
        utlatande.setAnledningTillKontakt("Känner mig ensam");
        utlatande.setTillaggsfragor(asList(Tillaggsfraga.create("9001", "Svar text 1"), Tillaggsfraga.create("9002", "Svar text 2")));
        return utlatande.build();
    }

    private static List<Underlag> buildUnderlag() {
        Underlag underlag = Underlag.create(Underlag.UnderlagsTyp.UNDERLAG_FRAN_FYSIOTERAPEUT, new InternalDate("2015-10-10"), "Postledes");
        return Arrays.asList(underlag);
    }

}
