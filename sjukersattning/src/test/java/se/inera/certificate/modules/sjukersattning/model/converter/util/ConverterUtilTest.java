package se.inera.certificate.modules.sjukersattning.model.converter.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.joda.time.LocalDate;
import org.junit.Test;
import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.model.InternalDate;
import se.inera.certificate.modules.fkparent.model.converter.IntygGrundDataBuilder;
import se.inera.certificate.modules.fkparent.model.converter.RespConstants;
import se.inera.certificate.modules.sjukersattning.model.internal.*;
import se.inera.certificate.modules.support.api.CertificateHolder;

import static org.junit.Assert.*;

public class ConverterUtilTest {

    @Test
    public void testConversion() throws Exception {
        ObjectMapper mapper = new CustomObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        ConverterUtil converterUtil = new ConverterUtil();
        converterUtil.setObjectMapper(mapper);
        SjukersattningUtlatande originalUtlatande = getUtlatande();
        CertificateHolder certificateHolder = converterUtil.toCertificateHolder(originalUtlatande);
        System.out.println(certificateHolder.getDocument());
        SjukersattningUtlatande sjukersattningUtlatande = converterUtil.fromJsonString(certificateHolder.getDocument());
        assertEquals(originalUtlatande, sjukersattningUtlatande);
    }

    private SjukersattningUtlatande getUtlatande() {
        SjukersattningUtlatande utlatande = new SjukersattningUtlatande();
        utlatande.setId("1234567");
        utlatande.setGrundData(IntygGrundDataBuilder.getGrundData());
        utlatande.setUndersokningAvPatienten(new InternalDate(new LocalDate()));
        utlatande.setKannedomOmPatient(new InternalDate(new LocalDate()));
        utlatande.getUnderlag().add(new Underlag(Underlag.UnderlagsTyp.OVRIGT, new InternalDate(new LocalDate()), false));
        utlatande.getUnderlag().add(new Underlag(Underlag.UnderlagsTyp.UNDERLAG_FRAN_ARBETSTERAPEUT, new InternalDate(new LocalDate().plusWeeks(2)), true));
        utlatande.getDiagnoser().add(new Diagnos("S47", "ICD-10-SE", "Klämskada skuldra"));
        utlatande.getDiagnoser().add(new Diagnos("S48", "ICD-10-SE", "Klämskada arm"));
        utlatande.getAtgarder().add(new BehandlingsAtgard("ABC", RespConstants.BEHANDLINGSATGARD_CODE_SYSTEM, "Kristallterapi"));
        utlatande.getFunktionsnedsattnings().add(new Funktionsnedsattning(Funktionsnedsattning.Funktionsomrade.ANNAN_KROPPSLIG, "Kan inte smida"));
        utlatande.getFunktionsnedsattnings().add(new Funktionsnedsattning(Funktionsnedsattning.Funktionsomrade.ANNAN_PSYKISK, "Lite ledsen"));
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