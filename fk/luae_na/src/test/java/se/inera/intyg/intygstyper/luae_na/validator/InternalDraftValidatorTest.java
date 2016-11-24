/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.intygstyper.luae_na.validator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.intygstyper.fkparent.model.internal.Diagnos;
import se.inera.intyg.intygstyper.fkparent.model.internal.Underlag;
import se.inera.intyg.intygstyper.fkparent.model.validator.ValidatorUtilFK;
import se.inera.intyg.intygstyper.luae_na.model.internal.LuaenaUtlatande;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

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

    @InjectMocks
    InternalDraftValidatorImpl validator;

    @InjectMocks
    ValidatorUtilFK validatorUtil;

    LuaenaUtlatande.Builder builderTemplate;

    @Mock
    WebcertModuleService moduleService;

    @Before
    public void setUp() throws Exception {
        builderTemplate = LuaenaUtlatande.builder()
                .setId(INTYG_ID)
                .setGrundData(buildGrundData(LocalDateTime.now()))
                .setAnhorigsBeskrivningAvPatienten(new InternalDate(LocalDate.now()))
                .setKannedomOmPatient(new InternalDate(LocalDate.now().minusDays(2)))
                .setUnderlagFinns(false)
                .setSjukdomsforlopp("sjukdomsforlopp")
                .setDiagnoser(Arrays.asList(Diagnos.create("S47", "ICD_10_SE", "Beskrivning", "diagnosDisplayName")))
                .setDiagnosgrund("diagnosgrund")
                .setNyBedomningDiagnosgrund(false)
                .setFunktionsnedsattningKoncentration("funktionsnedsattningKoncentration")
                .setAktivitetsbegransning("aktivitetsbegransning")
                .setMedicinskaForutsattningarForArbete("medicinskaForutsattningarForArbete")
                .setTextVersion("");

        when(moduleService.validateDiagnosisCode(anyString(), anyString())).thenReturn(true);

        // use reflection to set ValidatorUtilFK in InternalDraftValidator
        Field field = InternalDraftValidatorImpl.class.getDeclaredField("validatorUtilFK");
        field.setAccessible(true);
        field.set(validator, validatorUtil);
    }

    @Test
    public void validateOK() throws Exception {
        ValidateDraftResponse res = validator.validateDraft(builderTemplate.build());

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateGrundForMUMissing() throws Exception {
        LuaenaUtlatande utlatande = builderTemplate
                .setAnhorigsBeskrivningAvPatienten(null)
                .setKannedomOmPatient(null).build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(2, res.getValidationErrors().size());
        assertEquals("grundformu.baserasPa", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
        assertEquals("grundformu.kannedomOmPatient", res.getValidationErrors().get(1).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(1).getType());
    }

    @Test
    public void validateGrundForMUKannedomOmPatientMissing() throws Exception {
        LuaenaUtlatande utlatande = builderTemplate
                .setAnhorigsBeskrivningAvPatienten(new InternalDate(LocalDate.now()))
                .setKannedomOmPatient(null)
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("grundformu.kannedomOmPatient", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateGrundForMUKannedomOmPatientAfterUndersokning() throws Exception {
        LuaenaUtlatande utlatande = builderTemplate
                .setAnhorigsBeskrivningAvPatienten(null)
                .setUndersokningAvPatienten(new InternalDate(LocalDate.now().minusDays(2)))
                .setKannedomOmPatient(new InternalDate(LocalDate.now().minusDays(1)))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("luae_na.validation.grund-for-mu.kannedom.after.undersokning", res.getValidationErrors().get(0).getMessage());
        assertEquals("KV_FKMU_0001.UNDERSOKNING.RBK", res.getValidationErrors().get(0).getDynamicKey());
        assertEquals(ValidationMessageType.OTHER, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateGrundForMUKannedomOmPatientAfterAnhorigsBeskrivning() throws Exception {
        LuaenaUtlatande utlatande = builderTemplate
                .setAnhorigsBeskrivningAvPatienten(new InternalDate(LocalDate.now().minusDays(2)))
                .setKannedomOmPatient(new InternalDate(LocalDate.now().minusDays(1)))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("luae_na.validation.grund-for-mu.kannedom.after.anhorigsbeskrivning", res.getValidationErrors().get(0).getMessage());
        assertEquals("KV_FKMU_0001.ANHORIG.RBK", res.getValidationErrors().get(0).getDynamicKey());
        assertEquals(ValidationMessageType.OTHER, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateGrundForMUKannedomOmPatientFramtidaDatum() throws Exception {
        LuaenaUtlatande utlatande = builderTemplate
                .setAnhorigsBeskrivningAvPatienten(null)
                .setJournaluppgifter(new InternalDate(LocalDate.now().minusDays(1)))
                .setKannedomOmPatient(new InternalDate(LocalDate.now().plusDays(10)))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(0, res.getValidationErrors().size());
        assertEquals(1, res.getValidationWarnings().size());
        assertEquals("common.validation.future.datum", res.getValidationWarnings().get(0).getMessage());
        assertEquals(ValidationMessageType.WARN, res.getValidationWarnings().get(0).getType());
    }

    @Test
    public void validateGrundForMUKannedomOmPatientSammaDatum() throws Exception {
        LuaenaUtlatande utlatande = builderTemplate
                .setAnhorigsBeskrivningAvPatienten(null)
                .setJournaluppgifter(new InternalDate(LocalDate.now().minusDays(1)))
                .setKannedomOmPatient(new InternalDate(LocalDate.now()))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(0, res.getValidationErrors().size());
    }


    @Test
    public void validateGrundForMUAnnanGrundBeskrivningNotAnnanGrundDatum() throws Exception {
        LuaenaUtlatande utlatande = builderTemplate
                .setAnhorigsBeskrivningAvPatienten(null)
                .setAnnatGrundForMUBeskrivning("En beskrivning...")
                .setKannedomOmPatient(new InternalDate(LocalDate.now().minusDays(1)))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(2, res.getValidationErrors().size());
        assertEquals("grundformu.baserasPa", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
        assertEquals("luae_na.validation.grund-for-mu.incorrect_combination_annat_beskrivning", res.getValidationErrors().get(1).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(1).getType());
    }

    @Test
    public void validateGrundForMUAnnanGrundBeskrivningMissing() throws Exception {
        LuaenaUtlatande utlatande = builderTemplate
                .setAnhorigsBeskrivningAvPatienten(null)
                .setAnnatGrundForMU(new InternalDate(LocalDate.now().minusDays(1)))
                .setKannedomOmPatient(new InternalDate(LocalDate.now().minusDays(1)))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("grundformu.annat", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateUnderlagUnderlagFinnsMissing() throws Exception {
        LuaenaUtlatande utlatande = builderTemplate
                .setUnderlag(new ArrayList<>())
                .setUnderlagFinns(null).build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("grundformu.underlagFinns", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateUnderlagMissing() throws Exception {
        LuaenaUtlatande utlatande = builderTemplate
                .setUnderlag(new ArrayList<>())
                .setUnderlagFinns(true).build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("grundformu.underlag", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateUnderlagExistsButUnderlagFinnsFalse() throws Exception {
        LuaenaUtlatande utlatande = builderTemplate
                .setUnderlagFinns(false)
                .setUnderlag(buildUnderlag("NEUROPSYKIATRISKT"))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("luae_na.validation.underlagfinns.incorrect_combination", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.OTHER, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateUnderlagOK() throws Exception {
        LuaenaUtlatande utlatande = builderTemplate
                .setUnderlagFinns(true)
                .setUnderlag(buildUnderlag(
                        "NEUROPSYKIATRISKT",
                        "HABILITERING",
                        "ARBETSTERAPEUT"))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateUnderlagTooMany() throws Exception {
        LuaenaUtlatande utlatande = builderTemplate
                .setUnderlagFinns(true)
                .setUnderlag(buildUnderlag(
                        "NEUROPSYKIATRISKT",
                        "HABILITERING",
                        "ARBETSTERAPEUT",
                        "FYSIOTERAPEUT",
                        "LOGOPED",
                        "PSYKOLOG",
                        "SKOLHALSOVARD",
                        "SPECIALISTKLINIK",
                        "VARD_UTOMLANDS",
                        "OVRIGT_UTLATANDE"))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("luae_na.validation.underlag.too_many", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.OTHER, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateUnderlagInformationMissing() throws Exception {
        LuaenaUtlatande utlatande = builderTemplate
                .setUnderlagFinns(true)
                .setUnderlag(Arrays.asList(Underlag.create(Underlag.UnderlagsTyp.fromId("NEUROPSYKIATRISKT"), null, null)))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(2, res.getValidationErrors().size());
        assertEquals("luae_na.validation.underlag.date.missing", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
        assertEquals("luae_na.validation.underlag.hamtas-fran.missing", res.getValidationErrors().get(1).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(1).getType());
    }

    @Test
    public void validateDiagnosMissing() throws Exception {
        LuaenaUtlatande utlatande = builderTemplate
                .setDiagnoser(new ArrayList<>()).build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("common.validation.diagnos.missing", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateDiagnosDiagnosKodOK() throws Exception {
        LuaenaUtlatande utlatande = builderTemplate
                .setDiagnoser(buildDiagnoser("J22", "Z730", "F642"))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateDiagnosDiagnosKodInvalid() throws Exception {
        LuaenaUtlatande utlatande = builderTemplate
                .setDiagnoser(buildDiagnoser("J2", "Z73", "F6"))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(3, res.getValidationErrors().size());

        assertEquals("common.validation.diagnos0.length-3", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.INVALID_FORMAT, res.getValidationErrors().get(0).getType());
        assertEquals("common.validation.diagnos1.psykisk.length-4", res.getValidationErrors().get(1).getMessage());
        assertEquals(ValidationMessageType.INVALID_FORMAT, res.getValidationErrors().get(1).getType());
        assertEquals("common.validation.diagnos2.psykisk.length-4", res.getValidationErrors().get(2).getMessage());
        assertEquals(ValidationMessageType.INVALID_FORMAT, res.getValidationErrors().get(2).getType());
    }

    @Test
    public void validateDiagnosDiagnosBeskrivningMissing() throws Exception {
        LuaenaUtlatande utlatande = builderTemplate
                .setDiagnoser(Arrays.asList(Diagnos.create("J22", "ICD-10-SE", null, "Ett namn...")))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("common.validation.diagnos0.description.missing", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateSjukdomsforloppMissing() throws Exception {
        LuaenaUtlatande utlatande = builderTemplate
                .setSjukdomsforlopp(null)
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("sjukdomsforlopp", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateAktivitetsbegransningMissing() throws Exception {
        LuaenaUtlatande utlatande = builderTemplate
                .setAktivitetsbegransning(null)
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("aktivitetsbegransning", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateMedicinskaForutsattningarForArbeteMissing() throws Exception {
        LuaenaUtlatande utlatande = builderTemplate
                .setMedicinskaForutsattningarForArbete(null)
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("medicinskaforutsattningarforarbete", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateFunktionsnedsattningMissing() throws Exception {
        LuaenaUtlatande utlatande = builderTemplate
                .setFunktionsnedsattningKoncentration(null)
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("funktionsnedsattning", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateDiagnosgrundMissing() throws Exception {
        LuaenaUtlatande utlatande = builderTemplate
                .setDiagnosgrund(null)
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("diagnos.diagnosgrund", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateNyBedomningDiagnosgrundMissing() throws Exception {
        LuaenaUtlatande utlatande = builderTemplate
                .setNyBedomningDiagnosgrund(null)
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("diagnos.nyBedomningDiagnosgrund", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateNyBedomningDiagnosgrundBeskrivningMissing() throws Exception {
        LuaenaUtlatande utlatande = builderTemplate
                .setNyBedomningDiagnosgrund(true)
                .setDiagnosForNyBedomning(null)
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("diagnos.diagnosForNyBedomning", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateNyBedomningDiagnosgrundInvalidCombination() throws Exception {
        LuaenaUtlatande utlatande = builderTemplate
                .setNyBedomningDiagnosgrund(false)
                .setDiagnosForNyBedomning("diagnosForNyBedomning")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("diagnos.nyBedomningDiagnosgrund", res.getValidationErrors().get(0).getField());
        assertEquals("luae_na.validation.diagnosfornybedomning.incorrect_combination", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateKontaktOK() throws Exception {
        LuaenaUtlatande utlatande = builderTemplate
                .setKontaktMedFk(true)
                .setAnledningTillKontakt("En anledning")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateKontaktNoAnledningOK() throws Exception {
        LuaenaUtlatande utlatande = builderTemplate
                .setKontaktMedFk(true)
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateKontaktAnledningInvalidCombination() throws Exception {
        LuaenaUtlatande utlatande = builderTemplate
                .setKontaktMedFk(false)
                .setAnledningTillKontakt("En andledning...")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("luae_na.validation.kontakt.incorrect_combination", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.INCORRECT_COMBINATION, res.getValidationErrors().get(0).getType());
    }

    private List<Diagnos> buildDiagnoser(String... diagnosKoder) {
        List<Diagnos> diagnoser = new ArrayList<>();

        for (String kod : diagnosKoder) {
            diagnoser.add(Diagnos.create(kod, "ICD-10-SE", "En beskrivning...", "Ett namn..."));
        }

        return diagnoser;
    }

    private GrundData buildGrundData(LocalDateTime timeStamp) {
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarid(VARDGIVARE_ID);
        vardgivare.setVardgivarnamn(VARDGIVARE_NAMN);

        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setEnhetsid(ENHET_ID);
        vardenhet.setEnhetsnamn(ENHET_NAMN);
        vardenhet.setVardgivare(vardgivare);
        vardenhet.setPostadress("postadress");
        vardenhet.setPostnummer("12345");
        vardenhet.setPostort("postort");
        vardenhet.setTelefonnummer("telefonNummer");

        HoSPersonal skapadAv = new HoSPersonal();
        skapadAv.setVardenhet(vardenhet);
        skapadAv.setPersonId(SKAPADAV_PERSON_ID);
        skapadAv.setFullstandigtNamn(SKAPADAV_PERSON_NAMN);

        Patient patient = new Patient();
        patient.setPersonId(new Personnummer(PATIENT_PERSON_ID));
        patient.setPostadress("postadress");
        patient.setPostnummer("12345");
        patient.setPostort("postort");

        GrundData grundData = new GrundData();
        grundData.setSkapadAv(skapadAv);
        grundData.setPatient(patient);
        grundData.setSigneringsdatum(timeStamp);

        return grundData;
    }

    private List<Underlag> buildUnderlag(String... underlagsTyper) {
        List<Underlag> underlag = new ArrayList<>();

        for (String typ : underlagsTyper) {
            underlag.add(Underlag.create(Underlag.UnderlagsTyp.fromId(typ), new InternalDate(LocalDate.now()), "Hamtas fran..."));
        }

        return underlag;
    }

}
