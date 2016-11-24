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
package se.inera.intyg.intygstyper.lisjp.validator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;
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
import se.inera.intyg.intygstyper.fkparent.model.validator.ValidatorUtilFK;
import se.inera.intyg.intygstyper.lisjp.model.internal.ArbetslivsinriktadeAtgarder;
import se.inera.intyg.intygstyper.lisjp.model.internal.ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal;
import se.inera.intyg.intygstyper.lisjp.model.internal.LisjpUtlatande;
import se.inera.intyg.intygstyper.lisjp.model.internal.Prognos;
import se.inera.intyg.intygstyper.lisjp.model.internal.PrognosDagarTillArbeteTyp;
import se.inera.intyg.intygstyper.lisjp.model.internal.PrognosTyp;
import se.inera.intyg.intygstyper.lisjp.model.internal.Sjukskrivning;
import se.inera.intyg.intygstyper.lisjp.model.internal.Sjukskrivning.SjukskrivningsGrad;
import se.inera.intyg.intygstyper.lisjp.model.internal.Sysselsattning;
import se.inera.intyg.intygstyper.lisjp.model.internal.Sysselsattning.SysselsattningsTyp;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InternalDraftValidatorTest {

    @InjectMocks
    InternalDraftValidatorImpl validator;

    @InjectMocks
    ValidatorUtilFK validatorUtil;

    LisjpUtlatande.Builder builderTemplate;

    @Mock
    WebcertModuleService moduleService;

    @Before
    public void setUp() throws Exception {
        builderTemplate = LisjpUtlatande.builder()
                .setId("intygsId")
                .setGrundData(buildGrundData(LocalDateTime.now()))
                .setUndersokningAvPatienten(new InternalDate(LocalDate.now()))
                .setSysselsattning(Arrays.asList(Sysselsattning.create(SysselsattningsTyp.ARBETSSOKANDE)))
                .setDiagnoser(buildDiagnoser("J22"))
                .setFunktionsnedsattning("funktionsnedsattning")
                .setAktivitetsbegransning("aktivitetsbegransning")
                .setPrognos(Prognos.create(PrognosTyp.MED_STOR_SANNOLIKHET, null))
                .setSjukskrivningar(Arrays.asList(Sjukskrivning.create(SjukskrivningsGrad.HELT_NEDSATT,
                        new InternalLocalDateInterval(new InternalDate(LocalDate.now()), new InternalDate(LocalDate.now().plusDays(7))))))
                .setArbetslivsinriktadeAtgarder(Arrays.asList(ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.ARBETSTRANING)))
                .setArbetslivsinriktadeAtgarderBeskrivning("arbetslivsinriktadeAtgarderAktuelltBeskrivning")
                .setTextVersion("");

        when(moduleService.validateDiagnosisCode(anyString(), anyString())).thenReturn(true);

        // use reflection to set InternalValidatorUtil in InternalDraftValidator
        Field field = InternalDraftValidatorImpl.class.getDeclaredField("validatorUtilFK");
        field.setAccessible(true);
        field.set(validator, validatorUtil);
    }

    @Test
    public void validateDraft() throws Exception {
        LisjpUtlatande utlatande = builderTemplate.build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertFalse(res.hasErrorMessages());
        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateGrundForMUMissing() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setUndersokningAvPatienten(null)
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());

        assertEquals("grundformu.baserasPa", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateGrundForMUUndersokningAvPatienten() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setUndersokningAvPatienten(new InternalDate(LocalDate.now()))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateGrundForMUUndersokningAvPatientenInvalidDate() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setUndersokningAvPatienten(new InternalDate("invalid"))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());

        assertEquals("grundformu.undersokningAvPatienten", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.INVALID_FORMAT, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateGrundForMUTelefonkontaktMedPatienten() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setTelefonkontaktMedPatienten(new InternalDate(LocalDate.now()))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateGrundForMUTelefonkontaktMedPatientenInvalidDate() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setTelefonkontaktMedPatienten(new InternalDate("invalid"))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());

        assertEquals("grundformu.telefonkontaktMedPatienten", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.INVALID_FORMAT, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateGrundForMUJournaluppgifter() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setJournaluppgifter(new InternalDate(LocalDate.now()))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateGrundForMUJournaluppgifterInvalidDate() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setJournaluppgifter(new InternalDate("invalid"))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());

        assertEquals("grundformu.journaluppgifter", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.INVALID_FORMAT, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateGrundForMUAnnatGrundForMU() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setAnnatGrundForMU(new InternalDate(LocalDate.now()))
                .setAnnatGrundForMUBeskrivning("annatGrundForMUBeskrivning")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateGrundForMUAnnatGrundForMUInvalidDate() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setAnnatGrundForMU(new InternalDate("invalid"))
                .setAnnatGrundForMUBeskrivning("annatGrundForMUBeskrivning")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());

        assertEquals("grundformu.annatGrundForMU", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.INVALID_FORMAT, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateGrundForMUAnnatGrundForMUBeskrivingMissing() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setAnnatGrundForMU(new InternalDate(LocalDate.now()))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("grundformu.annatGrundForMUBeskrivning", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateGrundForMUAnnatGrundForMUBeskrivingOnly() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setAnnatGrundForMUBeskrivning("annatGrundForMUBeskrivning")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("lisjp.validation.grund-for-mu.annat.beskrivning.invalid_combination", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateGrundForMUDatumIFramtidenWarning() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setJournaluppgifter(new InternalDate(LocalDate.now().minusDays(1)))
                .setTelefonkontaktMedPatienten(new InternalDate(LocalDate.now().plusDays(10)))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(0, res.getValidationErrors().size());
        assertEquals(1, res.getValidationWarnings().size());
        assertEquals("common.validation.future.datum", res.getValidationWarnings().get(0).getMessage());
        assertEquals(ValidationMessageType.WARN, res.getValidationWarnings().get(0).getType());
    }

    @Test
    public void validateSysselsattningMissing() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setSysselsattning(new ArrayList<>())
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("sysselsattning", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateSysselsattningTypMissing() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setSysselsattning(Arrays.asList(Sysselsattning.create(null)))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("sysselsattning", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateSysselsattningNuvarandeArbete() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setSysselsattning(Arrays.asList(Sysselsattning.create(SysselsattningsTyp.NUVARANDE_ARBETE)))
                .setNuvarandeArbete("nuvarandeArbete")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateSysselsattningNuvarandeArbeteBeskrivingMissing() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setSysselsattning(Arrays.asList(Sysselsattning.create(SysselsattningsTyp.NUVARANDE_ARBETE)))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("sysselsattning.nuvarandeArbete", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateSysselsattningNuvarandeArbeteBeskrivingOnly() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setSysselsattning(Arrays.asList(Sysselsattning.create(SysselsattningsTyp.ARBETSSOKANDE)))
                .setNuvarandeArbete("nuvarandeArbete")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("lisjp.validation.sysselsattning.nuvarandearbete.invalid_combination", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateSysselsattningArbetsmarknadspolitisktProgram() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setSysselsattning(Arrays.asList(Sysselsattning.create(SysselsattningsTyp.ARBETSMARKNADSPOLITISKT_PROGRAM)))
                .setArbetsmarknadspolitisktProgram("arbetsmarknadspolitisktProgram")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateSysselsattningArbetsmarknadspolitisktProgramBeskrivingMissing() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setSysselsattning(Arrays.asList(Sysselsattning.create(SysselsattningsTyp.ARBETSMARKNADSPOLITISKT_PROGRAM)))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("sysselsattning.arbetsmarknadspolitisktProgram", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateSysselsattningArbetsmarknadspolitisktProgramBeskrivingOnly() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setSysselsattning(Arrays.asList(Sysselsattning.create(SysselsattningsTyp.ARBETSSOKANDE)))
                .setArbetsmarknadspolitisktProgram("arbetsmarknadspolitisktProgram")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("lisjp.validation.sysselsattning.ampolitisktprogram.invalid_combination", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateSysselsattningMultipleOK() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setSysselsattning(Arrays.asList(Sysselsattning.create(SysselsattningsTyp.NUVARANDE_ARBETE),
                        Sysselsattning.create(SysselsattningsTyp.ARBETSSOKANDE),
                        Sysselsattning.create(SysselsattningsTyp.ARBETSMARKNADSPOLITISKT_PROGRAM)))
                .setNuvarandeArbete("nuvarandeArbete")
                .setArbetsmarknadspolitisktProgram("arbetsmarknadspolitisktProgram")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateSysselsattningTooMany() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setSysselsattning(Arrays.asList(Sysselsattning.create(SysselsattningsTyp.ARBETSSOKANDE),
                        Sysselsattning.create(SysselsattningsTyp.ARBETSSOKANDE), Sysselsattning.create(SysselsattningsTyp.ARBETSSOKANDE),
                        Sysselsattning.create(SysselsattningsTyp.ARBETSSOKANDE), Sysselsattning.create(SysselsattningsTyp.ARBETSSOKANDE),
                        Sysselsattning.create(SysselsattningsTyp.ARBETSSOKANDE)))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("lisjp.validation.sysselsattning.too-many", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }


    @Test
    public void validateDiagnosis() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setDiagnoser(Arrays.asList(Diagnos.create(null, null, null, null)))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(2, res.getValidationErrors().size());
        assertEquals("common.validation.diagnos0.missing", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
        assertEquals("common.validation.diagnos0.description.missing", res.getValidationErrors().get(1).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(1).getType());
    }

    @Test
    public void validateFunktionsnedsattningMissing() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setFunktionsnedsattning(null)
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("funktionsnedsattning", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateFunktionsnedsattningBlank() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setFunktionsnedsattning(" ")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("funktionsnedsattning", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateAktivitetsbegransningMissing() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setAktivitetsbegransning(null)
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("funktionsnedsattning.aktivitetsbegransning", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateAktivitetsbegransningBlank() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setAktivitetsbegransning(" ")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("funktionsnedsattning.aktivitetsbegransning", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateSjukskrivningarEmpty() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setSjukskrivningar(new ArrayList<>())
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("lisjp.validation.bedomning.sjukskrivningar.missing", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateSjukskrivningSjukskrivingsgradMissing() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setSjukskrivningar(Arrays.asList(Sjukskrivning.create(null,
                        new InternalLocalDateInterval(new InternalDate(LocalDate.now()), new InternalDate(LocalDate.now().plusDays(2))))))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("lisjp.validation.bedomning.sjukskrivningar.sjukskrivningsgrad.missing", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateSjukskrivningPeriodMissing() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setSjukskrivningar(Arrays.asList(Sjukskrivning.create(SjukskrivningsGrad.HELT_NEDSATT, null)))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("lisjp.validation.bedomning.sjukskrivningar.periodHELT_NEDSATT.missing", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateSjukskrivningPeriodInvalidAvoidsNullpointer() throws Exception {
        InternalLocalDateInterval intervalMissingTom = new InternalLocalDateInterval(new InternalDate(LocalDate.now()), new InternalDate());
        // work-around for constructor not allowing null values (but might exist in json)
        intervalMissingTom.setTom(null);
        LisjpUtlatande utlatande = builderTemplate
                .setSjukskrivningar(Arrays.asList(Sjukskrivning.create(SjukskrivningsGrad.HELT_NEDSATT, intervalMissingTom)))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("bedomning.sjukskrivningar.period.HELT_NEDSATT", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.INVALID_FORMAT, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateSjukskrivningPeriodFromDateOutOfRange() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setSjukskrivningar(Arrays.asList(Sjukskrivning.create(SjukskrivningsGrad.HELT_NEDSATT,
                        new InternalLocalDateInterval(new InternalDate(LocalDate.parse("1800-01-01")), new InternalDate(LocalDate.now())))))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("common.validation.date_out_of_range", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.INVALID_FORMAT, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateSjukskrivningPeriodTomDateOutOfRange() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setSjukskrivningar(Arrays.asList(Sjukskrivning.create(SjukskrivningsGrad.HELT_NEDSATT,
                        new InternalLocalDateInterval(new InternalDate(LocalDate.now()), new InternalDate(LocalDate.parse("2100-01-01"))))))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("common.validation.date_out_of_range", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.INVALID_FORMAT, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateSjukskrivningPeriodNoOverlap() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setSjukskrivningar(
                        Arrays.asList(Sjukskrivning.create(SjukskrivningsGrad.HELT_NEDSATT,
                                new InternalLocalDateInterval(new InternalDate(LocalDate.now()), new InternalDate(LocalDate.now().plusDays(2)))),
                                Sjukskrivning.create(SjukskrivningsGrad.NEDSATT_HALFTEN, new InternalLocalDateInterval(
                                        new InternalDate(LocalDate.now().plusDays(3)), new InternalDate(LocalDate.now().plusDays(4))))))
                .setArbetstidsforlaggning(false)
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateSjukskrivningPeriodOverlap() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setSjukskrivningar(
                        Arrays.asList(Sjukskrivning.create(SjukskrivningsGrad.HELT_NEDSATT,
                                new InternalLocalDateInterval(new InternalDate(LocalDate.now()), new InternalDate(LocalDate.now().plusDays(2)))),
                                Sjukskrivning.create(SjukskrivningsGrad.NEDSATT_HALFTEN, new InternalLocalDateInterval(
                                        new InternalDate(LocalDate.now().plusDays(1)), new InternalDate(LocalDate.now().plusDays(2))))))
                .setArbetstidsforlaggning(false)
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(2, res.getValidationErrors().size());
        assertEquals("bedomning.sjukskrivningar.period.HELT_NEDSATT", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.PERIOD_OVERLAP, res.getValidationErrors().get(0).getType());
        assertEquals("bedomning.sjukskrivningar.period.HALFTEN", res.getValidationErrors().get(1).getField());
        assertEquals(ValidationMessageType.PERIOD_OVERLAP, res.getValidationErrors().get(1).getType());
    }

    @Test
    public void validateSjukskrivningArbetstidsforlaggningFalse() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setSjukskrivningar(Arrays.asList(Sjukskrivning.create(SjukskrivningsGrad.NEDSATT_HALFTEN, new InternalLocalDateInterval(
                        new InternalDate(LocalDate.now().plusDays(1)), new InternalDate(LocalDate.now().plusDays(2))))))
                .setArbetstidsforlaggning(false)
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateSjukskrivningArbetstidsforlaggningTrue() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setSjukskrivningar(Arrays.asList(Sjukskrivning.create(SjukskrivningsGrad.NEDSATT_HALFTEN, new InternalLocalDateInterval(
                        new InternalDate(LocalDate.now().plusDays(1)), new InternalDate(LocalDate.now().plusDays(2))))))
                .setArbetstidsforlaggning(true)
                .setArbetstidsforlaggningMotivering("arbetstidsforlaggningMotivering")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateSjukskrivningArbetstidsforlaggningMissing() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setSjukskrivningar(Arrays.asList(Sjukskrivning.create(SjukskrivningsGrad.NEDSATT_HALFTEN, new InternalLocalDateInterval(
                        new InternalDate(LocalDate.now().plusDays(1)), new InternalDate(LocalDate.now().plusDays(2))))))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("lisjp.validation.bedomning.sjukskrivningar.arbetstidsforlaggning.missing", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateSjukskrivningArbetstidsforlaggningMotiveringMissing() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setSjukskrivningar(Arrays.asList(Sjukskrivning.create(SjukskrivningsGrad.NEDSATT_HALFTEN, new InternalLocalDateInterval(
                        new InternalDate(LocalDate.now().plusDays(1)), new InternalDate(LocalDate.now().plusDays(2))))))
                .setArbetstidsforlaggning(true)
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("bedomning.arbetstidsforlaggningMotivering",
                res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateSjukskrivningArbetstidsforlaggningMotiveringOnly() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setSjukskrivningar(Arrays.asList(Sjukskrivning.create(SjukskrivningsGrad.NEDSATT_HALFTEN, new InternalLocalDateInterval(
                        new InternalDate(LocalDate.now().plusDays(1)), new InternalDate(LocalDate.now().plusDays(2))))))
                .setArbetstidsforlaggning(false)
                .setArbetstidsforlaggningMotivering("arbetstidsforlaggningMotivering")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("lisjp.validation.bedomning.sjukskrivningar.arbetstidsforlaggningmotivering.incorrect",
                res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateSjukskrivningArbetstidsforlaggningMotiveringWhenHeltNedsatt() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setSjukskrivningar(Arrays.asList(Sjukskrivning.create(SjukskrivningsGrad.HELT_NEDSATT, new InternalLocalDateInterval(
                        new InternalDate(LocalDate.now().plusDays(1)), new InternalDate(LocalDate.now().plusDays(2))))))
                .setArbetstidsforlaggningMotivering("arbetstidsforlaggningMotivering")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("lisjp.validation.bedomning.sjukskrivningar.arbetstidsforlaggningmotivering.invalid_combination",
                res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateBedomningFMB() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setForsakringsmedicinsktBeslutsstod("forskningsmedicinsktBeslutsstod")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateBedomningFMBNull() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setForsakringsmedicinsktBeslutsstod(null)
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateBedomningFMBBlank() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setForsakringsmedicinsktBeslutsstod(" ")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("lisjp.validation.bedomning.fmb.empty", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateBedomningPrognosMissing() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setPrognos(null)
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("bedomning.prognos", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateBedomningPrognosTypMissing() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setPrognos(Prognos.create(null, null))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("bedomning.prognos", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateBedomningPrognosAterXAntalDagar() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setPrognos(Prognos.create(PrognosTyp.ATER_X_ANTAL_DGR, PrognosDagarTillArbeteTyp.DAGAR_30))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateBedomningPrognosAterXAntalDagarPrognosDagarTillArbeteMissing() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setPrognos(Prognos.create(PrognosTyp.ATER_X_ANTAL_DGR, null))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("bedomning.prognos.dagarTillArbete", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateBedomningPrognosPrognosDagarTillArbeteOnly() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setPrognos(Prognos.create(PrognosTyp.MED_STOR_SANNOLIKHET, PrognosDagarTillArbeteTyp.DAGAR_30))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("lisjp.validation.bedomning.prognos.dagarTillArbete.invalid_combination", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateAtgarderMissing() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setArbetslivsinriktadeAtgarder(new ArrayList<>())
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("lisjp.validation.atgarder.missing", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateAtgarderInteAktuell() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setArbetslivsinriktadeAtgarder(Arrays.asList(ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT)))
                .setArbetslivsinriktadeAtgarderBeskrivning(null)
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateAtgarderInteAktuellCombined() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setArbetslivsinriktadeAtgarder(Arrays.asList(ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT),
                        ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.ARBETSANPASSNING)))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(2, res.getValidationErrors().size());
        assertEquals("lisjp.validation.atgarder.inte_aktuellt_no_combine", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
        assertEquals("lisjp.validation.atgarder.invalid_combination", res.getValidationErrors().get(1).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(1).getType());
    }

    @Test
    public void validateAtgarderInteAktuellBeskrivning() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setArbetslivsinriktadeAtgarder(Arrays.asList(ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.INTE_AKTUELLT)))
                .setArbetslivsinriktadeAtgarderBeskrivning("beskrivning")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("lisjp.validation.atgarder.invalid_combination", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateAtgarderAktuellBeskrivningMissing() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setArbetslivsinriktadeAtgarder(Arrays.asList(ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.ARBETSANPASSNING)))
                .setArbetslivsinriktadeAtgarderBeskrivning(null)
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("atgarder.arbetslivsinriktadeAtgarderBeskrivning", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateAtgarderAktuell() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setArbetslivsinriktadeAtgarder(Arrays.asList(ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.ARBETSANPASSNING)))
                .setArbetslivsinriktadeAtgarderBeskrivning("Beskrivning arbetsanpassning")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateAtgarderTooMany() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setArbetslivsinriktadeAtgarder(Arrays.asList(ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.ARBETSANPASSNING),
                        ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.ARBETSTRANING),
                        ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.BESOK_PA_ARBETSPLATSEN),
                        ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.ERGONOMISK_BEDOMNING),
                        ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.HJALPMEDEL),
                        ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.KONFLIKTHANTERING),
                        ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.KONTAKT_MED_FORETAGSHALSOVARD),
                        ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.OMFORDELNING_AV_ARBETSUPPGIFTER),
                        ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.OVRIGT),
                        ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.SOKA_NYTT_ARBETE),
                        ArbetslivsinriktadeAtgarder.create(ArbetslivsinriktadeAtgarderVal.ARBETSANPASSNING)))
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("lisjp.validation.atgarder.too-many", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateKontaktNull() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setKontaktMedFk(null)
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateKontaktTrue() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setKontaktMedFk(true)
                .setAnledningTillKontakt("anledningTillKontakt")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateKontaktTrueNoAnledning() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setKontaktMedFk(true)
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateKontaktFalse() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setKontaktMedFk(false)
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertTrue(res.getValidationErrors().isEmpty());
    }

    @Test
    public void validateKontaktFalseAndAnledning() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setKontaktMedFk(false)
                .setAnledningTillKontakt("anledningTillKontakt")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("lisjp.validation.kontakt.invalid_combination", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateBlankstegAnledningTillKontakt() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setAnledningTillKontakt(" ")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("lisjp.validation.blanksteg.otillatet", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateBlankstegAnnatGrundForMUBeskrivning() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setAnnatGrundForMU(new InternalDate(LocalDate.now()))
                .setAnnatGrundForMUBeskrivning(" ")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(2, res.getValidationErrors().size());
        assertEquals("grundformu.annatGrundForMUBeskrivning", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
        assertEquals("lisjp.validation.blanksteg.otillatet", res.getValidationErrors().get(1).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(1).getType());
    }

    @Test
    public void validateBlankstegPagaendeBehandling() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setPagaendeBehandling(" ")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("lisjp.validation.blanksteg.otillatet", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateBlankstegPlaneradBehandling() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setPlaneradBehandling(" ")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("lisjp.validation.blanksteg.otillatet", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateBlankstegOvrigt() throws Exception {
        LisjpUtlatande utlatande = builderTemplate
                .setOvrigt(" ")
                .build();

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("lisjp.validation.blanksteg.otillatet", res.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateEnhetPostadressMissing() throws Exception {
        LisjpUtlatande utlatande = builderTemplate.build();
        utlatande.getGrundData().getSkapadAv().getVardenhet().setPostadress(null);

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("vardenhet.grunddata.skapadAv.vardenhet.postadress", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateEnhetPostadressBlank() throws Exception {
        LisjpUtlatande utlatande = builderTemplate.build();
        utlatande.getGrundData().getSkapadAv().getVardenhet().setPostadress(" ");

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("vardenhet.grunddata.skapadAv.vardenhet.postadress", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateEnhetPostnummerMissing() throws Exception {
        LisjpUtlatande utlatande = builderTemplate.build();
        utlatande.getGrundData().getSkapadAv().getVardenhet().setPostnummer(null);

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("vardenhet.grunddata.skapadAv.vardenhet.postnummer", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateEnhetPostnummerBlank() throws Exception {
        LisjpUtlatande utlatande = builderTemplate.build();
        utlatande.getGrundData().getSkapadAv().getVardenhet().setPostnummer(" ");

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("vardenhet.grunddata.skapadAv.vardenhet.postnummer", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateEnhetPostnummerInvalid() throws Exception {
        LisjpUtlatande utlatande = builderTemplate.build();
        utlatande.getGrundData().getSkapadAv().getVardenhet().setPostnummer("invalid");

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("vardenhet.grunddata.skapadAv.vardenhet.postnummer", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.INVALID_FORMAT, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateEnhetPostortMissing() throws Exception {
        LisjpUtlatande utlatande = builderTemplate.build();
        utlatande.getGrundData().getSkapadAv().getVardenhet().setPostort(null);

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("vardenhet.grunddata.skapadAv.vardenhet.postort", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateEnhetPostortBlank() throws Exception {
        LisjpUtlatande utlatande = builderTemplate.build();
        utlatande.getGrundData().getSkapadAv().getVardenhet().setPostort(" ");

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("vardenhet.grunddata.skapadAv.vardenhet.postort", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateEnhetTelefonnummerMissing() throws Exception {
        LisjpUtlatande utlatande = builderTemplate.build();
        utlatande.getGrundData().getSkapadAv().getVardenhet().setTelefonnummer(null);

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("vardenhet.grunddata.skapadAv.vardenhet.telefonnummer", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
    }

    @Test
    public void validateEnhetTelefonnummerBlank() throws Exception {
        LisjpUtlatande utlatande = builderTemplate.build();
        utlatande.getGrundData().getSkapadAv().getVardenhet().setTelefonnummer(" ");

        ValidateDraftResponse res = validator.validateDraft(utlatande);

        assertEquals(1, res.getValidationErrors().size());
        assertEquals("vardenhet.grunddata.skapadAv.vardenhet.telefonnummer", res.getValidationErrors().get(0).getField());
        assertEquals(ValidationMessageType.EMPTY, res.getValidationErrors().get(0).getType());
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
        vardgivare.setVardgivarid("vardgivareId");
        vardgivare.setVardgivarnamn("vardgivareNamn");

        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setEnhetsid("enhetId");
        vardenhet.setEnhetsnamn("enhetNamn");
        vardenhet.setVardgivare(vardgivare);
        vardenhet.setPostadress("postadress");
        vardenhet.setPostnummer("11111");
        vardenhet.setPostort("postort");
        vardenhet.setTelefonnummer("0112312313");

        HoSPersonal skapadAv = new HoSPersonal();
        skapadAv.setVardenhet(vardenhet);
        skapadAv.setPersonId("HSAID_123");
        skapadAv.setFullstandigtNamn("Torsten Ericsson");

        Patient patient = new Patient();
        patient.setPersonId(new Personnummer("19121212-1212"));
        patient.setPostadress("postadress");
        patient.setPostnummer("11111");
        patient.setPostort("postort");

        GrundData grundData = new GrundData();
        grundData.setSkapadAv(skapadAv);
        grundData.setPatient(patient);
        grundData.setSigneringsdatum(timeStamp);

        return grundData;
    }

}
