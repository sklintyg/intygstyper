package se.inera.certificate.modules.luae_fs.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import se.inera.certificate.modules.fkparent.model.validator.InternalValidatorUtil;
import se.inera.certificate.modules.luae_fs.model.internal.LuaefsUtlatande;
import se.inera.certificate.modules.luae_fs.model.internal.Underlag;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Magnus Ekstrand on 2016-04-20.
 */
@RunWith(MockitoJUnitRunner.class)
public class InternalDraftValidatorTest {

    private static final String PATIENT_PERSON_ID = "19121212-1212";
    private static final String SKAPADAV_PERSON_ID = "19010101-9801";
    private static final String SKAPADAV_PERSON_NAMN = "Torsten Ericsson";
    private static final String VARDGIVARE_ID = "vardgivareId";
    private static final String VARDGIVARE_NAMN = "vardgivareNamn";
    private static final String ENHET_ID = "enhetId";
    private static final String ENHET_NAMN = "enhetNamn";
    private static final String INTYG_ID = "intyg-1";

    InternalDraftValidator validator;
    InternalValidatorUtil validatorUtil;

    List<ValidationMessage> validationMessages;

    LuaefsUtlatande.Builder builderTemplate;

    @Before
    public void setUp() throws Exception {
        validatorUtil = new InternalValidatorUtil();
        validator = new InternalDraftValidator(validatorUtil);
        validationMessages = new ArrayList<>();

        builderTemplate = LuaefsUtlatande.builder()
                .setId(INTYG_ID)
                .setGrundData(buildGrundData(LocalDateTime.now()))
                .setTextVersion("");
    }

    // Kategori 1 – Grund för medicinskt underlag

    @Test
    public void validateGrundForMU_Ok() throws Exception {
        LuaefsUtlatande utlatande = builderTemplate
                .setAnhorigsBeskrivningAvPatienten(new InternalDate(LocalDate.now()))
                .setKannedomOmPatient(new InternalDate(LocalDate.now().minusDays(2)))
                .build();

        validator.validateGrundForMU(utlatande, validationMessages);

        assertTrue(validationMessages.isEmpty());
    }

    @Test
    public void validateGrundForMU_IngenTypOchIngenKannedomOmPatient() throws Exception {
        LuaefsUtlatande utlatande = builderTemplate.build();

        validator.validateGrundForMU(utlatande, validationMessages);

        assertTrue(2 == validationMessages.size());

        assertValidationMessage("luae_fs.validation.grund-for-mu.missing", 0);
        assertValidationMessage("luae_fs.validation.grund-for-mu.kannedom.missing", 1);
        assertValidationMessageType(ValidationMessageType.EMPTY, 0);
        assertValidationMessageType(ValidationMessageType.EMPTY, 1);
    }

    @Test
    public void validateGrundForMU_IngenKannedomOmPatient() throws Exception {
        LuaefsUtlatande utlatande = builderTemplate
                .setAnhorigsBeskrivningAvPatienten(new InternalDate(LocalDate.now()))
                .build();

        validator.validateGrundForMU(utlatande, validationMessages);

        assertTrue(1 == validationMessages.size());

        assertValidationMessage("luae_fs.validation.grund-for-mu.kannedom.missing", 0);
        assertValidationMessageType(ValidationMessageType.EMPTY, 0);
    }

    @Test
    public void validateGrundForMU_KannedomOmPatientEfterUndersokning() throws Exception {
        LuaefsUtlatande utlatande = builderTemplate
                .setUndersokningAvPatienten(new InternalDate(LocalDate.now()))
                .setKannedomOmPatient(new InternalDate(LocalDate.now().plusDays(1)))
                .build();

        validator.validateGrundForMU(utlatande, validationMessages);

        assertTrue(1 == validationMessages.size());

        assertValidationMessage("luae_fs.validation.grund-for-mu.kannedom.after.undersokning", 0);
        assertValidationMessageType(ValidationMessageType.OTHER, 0);
    }

    @Test
    public void validateGrundForMU_KannedomOmPatientEfterAnhorigsBeskrivning() throws Exception {
        LuaefsUtlatande utlatande = builderTemplate
                .setAnhorigsBeskrivningAvPatienten(new InternalDate(LocalDate.now()))
                .setKannedomOmPatient(new InternalDate(LocalDate.now().plusDays(1)))
                .build();

        validator.validateGrundForMU(utlatande, validationMessages);

        assertTrue(1 == validationMessages.size());

        assertValidationMessage("luae_fs.validation.grund-for-mu.kannedom.after.anhorigsbeskrivning", 0);
        assertValidationMessageType(ValidationMessageType.OTHER, 0);
    }

    @Test
    public void validateGrundForMU_OmAnnanGrundBeskrivningOchInteAnnanGrundDatum() throws Exception {
        LuaefsUtlatande utlatande = builderTemplate
                .setAnnanGrundForMUBeskrivning("En beskrivning...")
                .setKannedomOmPatient(new InternalDate(LocalDate.now().plusDays(1)))
                .build();

        validator.validateGrundForMU(utlatande, validationMessages);

        assertTrue(2 == validationMessages.size());

        assertValidationMessage("luae_fs.validation.grund-for-mu.missing", 0);
        assertValidationMessageType(ValidationMessageType.EMPTY, 0);
        assertValidationMessage("luae_fs.validation.grund-for-mu.annat.beskrivning.invalid_combination", 1);
        assertValidationMessageType(ValidationMessageType.EMPTY, 1);
    }

    @Test
    public void validateGrundForMU_OmAnnanGrundKraverBeskrivning() throws Exception {
        LuaefsUtlatande utlatande = builderTemplate
                .setAnnanGrundForMU(new InternalDate(LocalDate.now()))
                .setKannedomOmPatient(new InternalDate(LocalDate.now().plusDays(1)))
                .build();

        validator.validateGrundForMU(utlatande, validationMessages);

        assertTrue(1 == validationMessages.size());

        assertValidationMessage("luae_fs.validation.grund-for-mu.annat.beskrivning.missing", 0);
        assertValidationMessageType(ValidationMessageType.EMPTY, 0);
    }

    // Kategori 2 - Andra medicinska utredningar och underlag

    @Test
    public void validateUnderlag_UnderlagFinnsInte() throws Exception {
        LuaefsUtlatande utlatande = builderTemplate.build();

        validator.validateUnderlag(utlatande, validationMessages);

        assertTrue(1 == validationMessages.size());

        assertValidationMessage("luae_fs.validation.underlagfinns.missing", 0);
        assertValidationMessageType(ValidationMessageType.EMPTY, 0);
    }

    @Test
    public void validateUnderlag_UnderlagFinnsMenArTomt() throws Exception {
        LuaefsUtlatande utlatande = builderTemplate.setUnderlagFinns(true).build();

        validator.validateUnderlag(utlatande, validationMessages);

        assertTrue(1 == validationMessages.size());

        assertValidationMessage("luae_fs.validation.underlagfinns.missing", 0);
        assertValidationMessageType(ValidationMessageType.EMPTY, 0);
    }

    @Test
    @Ignore
    public void validateUnderlag_UnderlagFinnsInteMenArIfyllt() throws Exception {
        LuaefsUtlatande utlatande = builderTemplate
                .setUnderlagFinns(false)
                .setUnderlag(buildUnderlag())
                .build();

        validator.validateUnderlag(utlatande, validationMessages);

        assertTrue(1 == validationMessages.size());

        assertValidationMessage("luae_fs.validation.underlagfinns.missing", 0);
        assertValidationMessageType(ValidationMessageType.EMPTY, 0);
    }

    private List<Underlag> buildUnderlag() {
        return null;
    }

    // - - - Private scope - - -

    private void assertValidationMessage(String expectedMessage, int index) {
        assertEquals(expectedMessage, validationMessages.get(index).getMessage());
    }

    private void assertValidationMessageType(ValidationMessageType expectedType, int index) {
        assertTrue(expectedType == validationMessages.get(index).getType());
    }

    private GrundData buildGrundData(LocalDateTime timeStamp) {
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarid(VARDGIVARE_ID);
        vardgivare.setVardgivarnamn(VARDGIVARE_NAMN);

        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setEnhetsid(ENHET_ID);
        vardenhet.setEnhetsnamn(ENHET_NAMN);
        vardenhet.setVardgivare(vardgivare);

        HoSPersonal skapadAv = new HoSPersonal();
        skapadAv.setVardenhet(vardenhet);
        skapadAv.setPersonId(SKAPADAV_PERSON_ID);
        skapadAv.setFullstandigtNamn(SKAPADAV_PERSON_NAMN);

        Patient patient = new Patient();
        patient.setPersonId(new Personnummer(PATIENT_PERSON_ID));

        GrundData grundData = new GrundData();
        grundData.setSkapadAv(skapadAv);
        grundData.setPatient(patient);
        grundData.setSigneringsdatum(timeStamp);

        return grundData;
    }

}