package se.inera.certificate.modules.sjukersattning.model.converter;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.junit.Test;

import se.inera.certificate.model.InternalDate;
import se.inera.certificate.modules.fkparent.model.converter.IntygGrundDataBuilder;
import se.inera.certificate.modules.fkparent.model.converter.RespConstants;
import se.inera.certificate.modules.sjukersattning.model.internal.*;
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
        utlatande.getUnderlag().add(Underlag.create(Underlag.UnderlagsTyp.OVRIGT, new InternalDate(new LocalDate()), false));
        utlatande.getUnderlag().add(
                Underlag.create(Underlag.UnderlagsTyp.UNDERLAG_FRAN_ARBETSTERAPEUT, new InternalDate(new LocalDate().plusWeeks(2)), true));
        utlatande.getDiagnoser().add(Diagnos.create("S47", "ICD-10-SE", "Klämskada skuldra"));
        utlatande.getDiagnoser().add(Diagnos.create("S48", "ICD-10-SE", "Klämskada arm"));
        utlatande.getAtgarder().add(BehandlingsAtgard.create("ABC", RespConstants.BEHANDLINGSATGARD_CODE_SYSTEM, "Kristallterapi"));
        utlatande.getFunktionsnedsattnings().add(Funktionsnedsattning.create(Funktionsnedsattning.Funktionsomrade.ANNAN_KROPPSLIG, "Kan inte smida"));
        utlatande.getFunktionsnedsattnings().add(Funktionsnedsattning.create(Funktionsnedsattning.Funktionsomrade.ANNAN_PSYKISK, "Lite ledsen"));
        utlatande.setAktivitetsbegransning("Väldigt sjuk");
        utlatande.setDiagnostisering("Helt galen");
        utlatande.setPagaendeBehandling("Medicin");
        utlatande.setAvslutadBehandling("Gammal medicin");
        utlatande.setPlaneradBehandling("Mer medicin");
        utlatande.setAktivitetsFormaga("Dansa");
        utlatande.setPrognos("Aldrig");
        utlatande.setOvrigt("Trevlig kille");
        utlatande.setKontaktMedFk(false);
        return utlatande;
    }

}
