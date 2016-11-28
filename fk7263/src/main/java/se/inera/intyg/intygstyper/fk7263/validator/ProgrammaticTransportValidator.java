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

package se.inera.intyg.intygstyper.fk7263.validator;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.*;
import se.inera.ifv.insuranceprocess.healthreporting.v2.*;
import se.inera.intyg.common.support.Constants;
import se.inera.intyg.common.support.common.enumerations.Diagnoskodverk;
import se.inera.intyg.intygstyper.fk7263.schemas.insuranceprocess.healthreporting.validator.PatientValidator;

/**
 * Validates presence and validity of formal external model properties such as OID's. Preferably this validation should
 * have been implemented using Schema Validation, but since this is not an option we perform this pre-validation in
 * code. more specific business rules should subsequently validated in {@see UtlatandeValidator}
 *
 * @author marced
 */
public class ProgrammaticTransportValidator extends AbstractValidator {

    private LakarutlatandeType utlatande;

    // Fält 1
    private boolean inSmittskydd = false;

    // Declared outside to serve as context when validating Fält 13
    // Fält 4
    private ReferensType inAnnat = null;
    // Fält 10
    private boolean inArbetsformagaGarEjAttBedomma = false;

    public ProgrammaticTransportValidator(LakarutlatandeType utlatande) {
        this.utlatande = utlatande;
    }

    public List<String> validate() {

        validateUtlatande();
        validatePatient();
        validateHospersonal();
        validateReferens();
        validateSmittskyddRelated();
        validateSysselsattning();
        validateNedsattning();
        validatePrognosAngivelse();
        validateRessatt();
        validateKommentar();
        validateRekommendationer();
        validateAktuelltSjukdomsforlopp();

        return getValidationErrors();
    }

    private void validateAktuelltSjukdomsforlopp() {
        if (utlatande.getBedomtTillstand() != null && utlatande.getBedomtTillstand().getBeskrivning() == null) {
            addValidationError("Beskrivning must be set for Falt3 Aktuellt Sjukdomsforlopp");
        }

    }

    private void validateRekommendationer() {
        AktivitetType ovrigt = findAktivitetWithCode(utlatande.getAktivitet(), Aktivitetskod.OVRIGT);
        if (ovrigt != null && ovrigt.getBeskrivning() == null) {
            addValidationError("Beskrivning must be set for Aktivitet Rekommendation Ovrigt");
        }

        AktivitetType atgardSjukvarden = findAktivitetWithCode(utlatande.getAktivitet(),
                Aktivitetskod.PLANERAD_ELLER_PAGAENDE_BEHANDLING_ELLER_ATGARD_INOM_SJUKVARDEN);
        if (atgardSjukvarden != null && atgardSjukvarden.getBeskrivning() == null) {
            addValidationError("Beskrivning must be set for Aktivitet Rekommendation Planerad eller pågående åtgärd inom sjukvården");
        }

        AktivitetType atgardAnnan = findAktivitetWithCode(utlatande.getAktivitet(), Aktivitetskod.PLANERAD_ELLER_PAGAENDE_ANNAN_ATGARD);
        if (atgardAnnan != null && atgardAnnan.getBeskrivning() == null) {
            addValidationError("Beskrivning must be set for Aktivitet Rekommendation Planerad eller pågående annan atgärd");
        }

    }

    private void validateReferens() {
        if (utlatande.getReferens() != null) {
            for (ReferensType r : utlatande.getReferens()) {
                if (r.getDatum() == null) {
                    addValidationError("Field 4: Referens is missing datum");
                }
            }
        }

    }

    private void validateUtlatande() {
        String id = utlatande.getLakarutlatandeId();
        if (id == null || StringUtils.isEmpty(id)) {
            addValidationError("Head: Utlatande Id is mandatory!");
        }

        // Check skickat datum - mandatory
        if (utlatande.getSkickatDatum() == null) {
            addValidationError("Header: No or wrong skickatDatum found!");
        }
        // Check signeringsdatum - mandatory
        if (utlatande.getSigneringsdatum() == null) {
            addValidationError("Field 14: No signeringsDatum found!");
        }

    }

    private void validatePatient() {
        PatientType inPatient = utlatande.getPatient();
        // Validate personnummer with PatientValidator
        for (String s : PatientValidator.validateAndCorrect(inPatient)) {
            addValidationError(s);
        }
        // Get namn for patient - mandatory
        if (inPatient != null && (inPatient.getFullstandigtNamn() == null || inPatient.getFullstandigtNamn().length() < 1)) {
            addValidationError("No Patient fullstandigtNamn elements found or set!");
        }
    }

    private void validateHospersonal() {
        // Check that we got a skapadAvHosPersonal element
        if (utlatande.getSkapadAvHosPersonal() == null) {
            addValidationError("No SkapadAvHosPersonal element found!");
            return;
        }
        HosPersonalType inHoSP = utlatande.getSkapadAvHosPersonal();

        // Check lakar id - mandatory
        if (inHoSP.getPersonalId() == null || inHoSP.getPersonalId().getExtension() == null
                || inHoSP.getPersonalId().getExtension().length() < 1) {
            addValidationError("No personal-id found!");
        }
        // Check lakar id o.i.d.
        if (inHoSP.getPersonalId() == null || inHoSP.getPersonalId().getRoot() == null
                || !inHoSP.getPersonalId().getRoot().equalsIgnoreCase(Constants.HSA_ID_OID)) {
            addValidationError("Wrong o.i.d. for personalId! Should be " + Constants.HSA_ID_OID);
        }

        // Check lakarnamn - mandatory
        if (inHoSP.getFullstandigtNamn() == null || inHoSP.getFullstandigtNamn().length() < 1) {
            addValidationError("No skapadAvHosPersonal fullstandigtNamn found.");
        }

        validateHosPersonalEnhet(inHoSP);
    }

    private void validateHosPersonalEnhet(HosPersonalType inHoSP) {
        // Check that we got a enhet element
        if (inHoSP.getEnhet() == null) {
            addValidationError("No enhet element found!");
            return;
        }

        EnhetType inEnhet = inHoSP.getEnhet();

        // Check enhets id - mandatory
        if (inEnhet.getEnhetsId() == null || isNullOrEmpty(inEnhet.getEnhetsId().getExtension())) {
            addValidationError("No enhets-id found!");
        }
        // Check enhets o.i.d
        if (inEnhet.getEnhetsId() == null || inEnhet.getEnhetsId().getRoot() == null
                || !inEnhet.getEnhetsId().getRoot().equalsIgnoreCase(Constants.HSA_ID_OID)) {
            addValidationError("Wrong o.i.d. for enhetsId! Should be " + Constants.HSA_ID_OID);
        }

        // Check enhetsnamn - mandatory
        if (isNullOrEmpty(inEnhet.getEnhetsnamn())) {
            addValidationError("No enhetsnamn found!");
        }

        if (inEnhet.getArbetsplatskod() == null) {
            addValidationError("No arbetsplatskod element found!");
        } else {
            if (!inEnhet.getArbetsplatskod().getRoot().equalsIgnoreCase(Constants.ARBETSPLATS_KOD_OID)) {
                addValidationError("Wrong o.i.d for arbetsplatskod, should be " + Constants.ARBETSPLATS_KOD_OID);
            }
            if (isNullOrEmpty(inEnhet.getArbetsplatskod().getExtension())) {
                addValidationError("No arbetsplatskod found!");
            }
        }

        // Check enhetsadress - mandatory
        if (isNullOrEmpty(inEnhet.getPostadress())) {
            addValidationError("No postadress found for enhet!");
        }
        if (isNullOrEmpty(inEnhet.getPostnummer())) {
            addValidationError("No postnummer found for enhet!");
        }
        if (isNullOrEmpty(inEnhet.getPostort())) {
            addValidationError("No postort found for enhet!");
        }
        if (isNullOrEmpty(inEnhet.getTelefonnummer())) {
            addValidationError("No telefonnummer found for enhet!");
        }

        validateVardgivare(inEnhet);
    }

    private void validateVardgivare(EnhetType inEnhet) {
        // Check that we got a vardgivare element
        if (inEnhet.getVardgivare() == null) {
            addValidationError("No vardgivare element found!");
            return;
        }
        VardgivareType inVardgivare = inEnhet.getVardgivare();

        // Check vardgivare id - mandatory
        if (inVardgivare.getVardgivareId() == null || isNullOrEmpty(inVardgivare.getVardgivareId().getExtension())) {
            addValidationError("No vardgivare-id found!");
        }
        // Check vardgivare o.i.d.
        if (inVardgivare.getVardgivareId() == null || inVardgivare.getVardgivareId().getRoot() == null
                || !inVardgivare.getVardgivareId().getRoot().equalsIgnoreCase(Constants.HSA_ID_OID)) {
            addValidationError("Wrong o.i.d. for vardgivareId! Should be " + Constants.HSA_ID_OID);
        }

        // Check vardgivarename - mandatory
        if (isNullOrEmpty(inVardgivare.getVardgivarnamn())) {
            addValidationError("No vardgivarenamn found!");
        }
    }

    private void validateSmittskyddRelated() {
        // Fält 1 - no rule
        inSmittskydd = findAktivitetWithCode(utlatande.getAktivitet(), Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA) != null ? true : false;

        // Must be set as this element contains a lot of mandatory
        // information
        FunktionstillstandType inAktivitetFunktion = findFunktionsTillstandType(
                utlatande.getFunktionstillstand(), TypAvFunktionstillstand.AKTIVITET);
        if (inAktivitetFunktion == null) {
            addValidationError("No funktionstillstand - aktivitet element found!");
        }

        if (!inSmittskydd) {
            // Fält 2 - Check that we got a medicinsktTillstand element
            if (utlatande.getMedicinsktTillstand() == null) {
                addValidationError("No medicinsktTillstand element found!");
                return;
            }

            // Fält 2 - Medicinskt tillstånd kod - mandatory
            MedicinsktTillstandType medTillstand = utlatande.getMedicinsktTillstand();
            if (medTillstand.getTillstandskod() == null || isNullOrEmpty(medTillstand.getTillstandskod().getCode())) {
                addValidationError("No tillstandskod in medicinsktTillstand found!");
            }

            // Fält 2 - Medicinskt tillstånd kodsystemnamn - mandatory
            if (medTillstand.getTillstandskod() == null
                    || medTillstand.getTillstandskod().getCodeSystemName() == null
                    || !(medTillstand.getTillstandskod().getCodeSystemName().equalsIgnoreCase(Diagnoskodverk.ICD_10_SE.getCodeSystemName())
                    ||   medTillstand.getTillstandskod().getCodeSystemName().equalsIgnoreCase(Diagnoskodverk.KSH_97_P.getCodeSystemName()))) {
                addValidationError("Wrong code system name for medicinskt tillstand - tillstandskod (diagnoskod)! Should be ICD-10 OR KSH97P");
            }
            // Fält 2 - Medicinskt tillstånd beskrivning - optional

            // Fält 3 - Not mandatory

            // Fält 4 - vänster Check that we got a funktionstillstand -
            // kroppsfunktion element
            FunktionstillstandType inKroppsFunktion = findFunktionsTillstandType(
                    utlatande.getFunktionstillstand(), TypAvFunktionstillstand.KROPPSFUNKTION);
            if (inKroppsFunktion == null) {
                addValidationError("No funktionstillstand - kroppsfunktion element found!");
                return;
            }

            // Fält 4 - vänster Funktionstillstand - kroppsfunktion
            // beskrivning - mandatory
            if (isNullOrEmpty(inKroppsFunktion.getBeskrivning())) {
                addValidationError("No beskrivning in funktionstillstand - kroppsfunktion found!");
            }

            // Fält 4 - höger översta kryssrutan
            VardkontaktType inUndersokning = findVardkontaktTyp(utlatande.getVardkontakt(),
                    Vardkontakttyp.MIN_UNDERSOKNING_AV_PATIENTEN);

            // Fält 4 - höger näst översta kryssrutan
            VardkontaktType telefonkontakt = findVardkontaktTyp(utlatande.getVardkontakt(),
                    Vardkontakttyp.MIN_TELEFONKONTAKT_MED_PATIENTEN);

            // Fält 4 - höger näst nedersta kryssrutan
            ReferensType journal = findReferensTyp(utlatande.getReferens(), Referenstyp.JOURNALUPPGIFTER);

            // Fält 4 - höger nedersta kryssrutan
            inAnnat = findReferensTyp(utlatande.getReferens(), Referenstyp.ANNAT);

            // Fält 4 - höger Check that we at least got one field set
            if (inUndersokning == null && telefonkontakt == null && journal == null && inAnnat == null) {
                addValidationError("No vardkontakt or referens element found ! At least one must be set!");
            }
            // Fält 4 - höger - 1:a kryssrutan Check that we got a date if
            // choice is set
            if (inUndersokning != null && inUndersokning.getVardkontaktstid() == null) {
                addValidationError("No or wrong date for vardkontakt - min undersokning av patienten found!");
            }
            // Fält 4 - höger - 2:a kryssrutan Check that we got a date if
            // choice is set
            if (telefonkontakt != null && telefonkontakt.getVardkontaktstid() == null) {
                addValidationError("No or wrong date for vardkontakt - telefonkontakt found!");
            }
            // Fält 4 - höger - 3:e kryssrutan Check that we got a date if
            // choice is set
            if (journal != null && journal.getDatum() == null) {
                addValidationError("No or wrong date for referens - journal found!");
            }
            // Fält 4 - höger - 4:e kryssrutan Check that we got a date if
            // choice is set
            if (inAnnat != null && inAnnat.getDatum() == null) {
                addValidationError("No or wrong date for referens - annat found!");
            }
            // Fält 5 - not mandatory

            // Fält 6 - not mandatory

            // Fält 7 - not mandatory
        }
    }

    private void validateSysselsattning() {
        FunktionstillstandType inAktivitetFunktion = findFunktionsTillstandType(
                utlatande.getFunktionstillstand(), TypAvFunktionstillstand.AKTIVITET);

        // Fält 8a - Check that we got a arbetsformaga element, needs to be present even if smittskydd is set.
        if (inAktivitetFunktion == null || inAktivitetFunktion.getArbetsformaga() == null) {
            addValidationError("No arbetsformaga element found for field 8a!");
            return;
        }

        // Fält 8a
        SysselsattningType inArbete = findTypAvSysselsattning(inAktivitetFunktion.getArbetsformaga()
                .getSysselsattning(), TypAvSysselsattning.NUVARANDE_ARBETE);
        SysselsattningType inArbetslos = findTypAvSysselsattning(inAktivitetFunktion.getArbetsformaga()
                .getSysselsattning(), TypAvSysselsattning.ARBETSLOSHET);
        SysselsattningType inForaldraledig = findTypAvSysselsattning(inAktivitetFunktion.getArbetsformaga()
                .getSysselsattning(), TypAvSysselsattning.FORALDRALEDIGHET);

        // Fält 8a - Check that we at least got one choice
        if (!inSmittskydd) {
            if (inArbete == null && inArbetslos == null && inForaldraledig == null) {
                addValidationError("No sysselsattning element found for field 8a! Nuvarande arbete, arbestloshet or foraldraledig should be set.");
            }
        }

        ArbetsuppgiftType inArbetsBeskrivning = inAktivitetFunktion.getArbetsformaga().getArbetsuppgift();

        // Fält 8a - Check that we got a arbetsuppgift element if arbete
        // is set
        if (inArbete != null) {
            if (inArbetsBeskrivning == null) {
                if (!inSmittskydd) {
                    addValidationError("No arbetsuppgift element found when arbete set in field 8a!.");
                }
            } else {
                if (inArbetsBeskrivning.getTypAvArbetsuppgift() == null) {
                    addValidationError("No typAvArbetsuppgift element found!");
                } else if (!inSmittskydd && isNullOrEmpty(inArbetsBeskrivning.getTypAvArbetsuppgift())) {
                    addValidationError("No typAvArbetsuppgift found when arbete set in field 8a!.");
                }
            }
        }
    }

    private void validateNedsattning() {
        FunktionstillstandType inAktivitetFunktion = findFunktionsTillstandType(
                utlatande.getFunktionstillstand(), TypAvFunktionstillstand.AKTIVITET);

        if (inAktivitetFunktion == null || inAktivitetFunktion.getArbetsformaga() == null) {
            addValidationError("No arbetsformaga element found 8b!.");
            return;
        }

        // Fält 8b - kryssruta 1
        ArbetsformagaNedsattningType nedsatt14del = findArbetsformaga(inAktivitetFunktion.getArbetsformaga()
                .getArbetsformagaNedsattning(),
                Nedsattningsgrad.NEDSATT_MED_1_4);

        // Fält 8b - kryssruta 2
        ArbetsformagaNedsattningType nedsatthalften = findArbetsformaga(inAktivitetFunktion.getArbetsformaga()
                .getArbetsformagaNedsattning(),
                Nedsattningsgrad.NEDSATT_MED_1_2);

        // Fält 8b - kryssruta 3
        ArbetsformagaNedsattningType nedsatt34delar = findArbetsformaga(inAktivitetFunktion.getArbetsformaga()
                .getArbetsformagaNedsattning(),
                Nedsattningsgrad.NEDSATT_MED_3_4);

        // Fält 8b - kryssruta 4
        ArbetsformagaNedsattningType heltNedsatt = findArbetsformaga(inAktivitetFunktion.getArbetsformaga()
                .getArbetsformagaNedsattning(),
                Nedsattningsgrad.HELT_NEDSATT);

        // Check that we at least got one choice
        if (nedsatt14del == null && nedsatthalften == null && nedsatt34delar == null && heltNedsatt == null) {
            addValidationError("No arbetsformaganedsattning element found 8b!.");
        }

        // Fält 8b - kryssruta 1 - varaktighet From
        if (nedsatt14del != null) {
            if (nedsatt14del.getVaraktighetFrom() == null) {
                addValidationError("No or wrong date for nedsatt 1/4 from date found!");
            }
            if (nedsatt14del.getVaraktighetTom() == null) {
                addValidationError("No or wrong date for nedsatt 1/4 tom date found!");
            }
            if (nedsatt14del.getVaraktighetFrom() != null && nedsatt14del.getVaraktighetTom() != null
                    && nedsatt14del.getVaraktighetFrom().isAfter(nedsatt14del.getVaraktighetTom())) {
                addValidationError("Invalid date interval for 1/4, from is after tom.");
            }
        }

        // Fält 8b - kryssruta 2
        if (nedsatthalften != null) {
            if (nedsatthalften.getVaraktighetFrom() == null) {
                addValidationError("No or wrong date for nedsatt 1/2 from date found!");
            }
            if (nedsatthalften.getVaraktighetTom() == null) {
                addValidationError("No or wrong date for nedsatt 1/2 tom date found!");
            }
            if (nedsatthalften.getVaraktighetFrom() != null && nedsatthalften.getVaraktighetTom() != null
                    && nedsatthalften.getVaraktighetFrom().isAfter(nedsatthalften.getVaraktighetTom())) {
                addValidationError("Invalid date interval for nedsatt 1/2 , from is after tom.");
            }

        }

        // Fält 8b - kryssruta 3
        if (nedsatt34delar != null) {
            if (nedsatt34delar.getVaraktighetFrom() == null) {
                addValidationError("No or wrong date for nedsatt 3/4 from date found!");
            }
            if (nedsatt34delar.getVaraktighetTom() == null) {
                addValidationError("No or wrong date for nedsatt 3/4 tom date found!");
            }
            if (nedsatt34delar.getVaraktighetFrom() != null && nedsatt34delar.getVaraktighetTom() != null
                    && nedsatt34delar.getVaraktighetFrom().isAfter(nedsatt34delar.getVaraktighetTom())) {
                addValidationError("Invalid date interval for heltNedsatt, from is after tom.");
            }
        }

        // Fält 8b - kryssruta 4
        if (heltNedsatt != null) {
            if (heltNedsatt.getVaraktighetFrom() == null) {
                addValidationError("No or wrong date for helt nedsatt from date found!");
            }
            if (heltNedsatt.getVaraktighetTom() == null) {
                addValidationError("No or wrong date for helt nedsatt tom date found!");
            }
            if (heltNedsatt.getVaraktighetFrom() != null && heltNedsatt.getVaraktighetTom() != null
                    && heltNedsatt.getVaraktighetFrom().isAfter(heltNedsatt.getVaraktighetTom())) {
                addValidationError("Invalid date interval for heltNedsatt, from is after tom.");
            }
        }
    }

    private void validatePrognosAngivelse() {
        FunktionstillstandType inAktivitetFunktion = findFunktionsTillstandType(
                utlatande.getFunktionstillstand(), TypAvFunktionstillstand.AKTIVITET);

        if (inAktivitetFunktion != null && inAktivitetFunktion.getArbetsformaga() != null) {
            boolean inArbetsformagaAterstallasHelt = false;
            boolean inArbetsformagaAterstallasDelvis = false;
            boolean inArbetsformagaEjAterstallas = false;

            if (inAktivitetFunktion.getArbetsformaga().getPrognosangivelse() != null) {
                inArbetsformagaAterstallasHelt = inAktivitetFunktion.getArbetsformaga().getPrognosangivelse()
                        .compareTo(Prognosangivelse.ATERSTALLAS_HELT) == 0;
                inArbetsformagaAterstallasDelvis = inAktivitetFunktion.getArbetsformaga().getPrognosangivelse()
                        .compareTo(Prognosangivelse.ATERSTALLAS_DELVIS) == 0;
                inArbetsformagaEjAterstallas = inAktivitetFunktion.getArbetsformaga().getPrognosangivelse()
                        .compareTo(Prognosangivelse.INTE_ATERSTALLAS) == 0;
                inArbetsformagaGarEjAttBedomma = inAktivitetFunktion.getArbetsformaga().getPrognosangivelse()
                        .compareTo(Prognosangivelse.DET_GAR_INTE_ATT_BEDOMMA) == 0;
            }

            // If we got more then one prognoselement these will not be read as
            // only the first is set!
            int inPrognosCount = 0;
            if (inArbetsformagaAterstallasHelt) {
                inPrognosCount++;
            }
            if (inArbetsformagaAterstallasDelvis) {
                inPrognosCount++;
            }
            if (inArbetsformagaEjAterstallas) {
                inPrognosCount++;
            }
            if (inArbetsformagaGarEjAttBedomma) {
                inPrognosCount++;
            }

            // Fält 10 - Prognosangivelse - Check that we only got one choice
            if (inPrognosCount > 2) {
                addValidationError("Only one prognosangivelse should be set for field 10.");
            }
        }
    }

    private void validateRessatt() {
        // Fält 11 - optional
        AktivitetType inForandratRessatt = findAktivitetWithCode(utlatande.getAktivitet(),
                Aktivitetskod.FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_AKTUELLT);
        AktivitetType inEjForandratRessatt = findAktivitetWithCode(utlatande.getAktivitet(),
                Aktivitetskod.FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_EJ_AKTUELLT);

        // Fält 11 - If set only one should be set
        if (inForandratRessatt != null && inEjForandratRessatt != null) {
            addValidationError("Only one forandrat ressatt could be set for field 11.");
        }
    }

    private void validateKommentar() {
        // Fält 13 - Upplysningar - optional
        // If field 4 annat satt or field 10 går ej att bedömma is set then
        // field 13 should contain data.
        if ((inAnnat != null || inArbetsformagaGarEjAttBedomma) && isNullOrEmpty(utlatande.getKommentar())) {
            addValidationError("Upplysningar should contain data as field 4 or fields 10 is checked.");
        }

    }

    // Helper methods
    protected static AktivitetType findAktivitetWithCode(List<AktivitetType> aktiviteter, Aktivitetskod aktivitetskod) {
        AktivitetType foundAktivitet = null;
        if (aktiviteter != null) {
            for (int i = 0; i < aktiviteter.size(); i++) {
                AktivitetType listAktivitet = aktiviteter.get(i);
                if (listAktivitet.getAktivitetskod() != null
                        && listAktivitet.getAktivitetskod().compareTo(aktivitetskod) == 0) {
                    foundAktivitet = listAktivitet;
                    break;
                }
            }
        }
        return foundAktivitet;
    }

    protected static FunktionstillstandType findFunktionsTillstandType(List<FunktionstillstandType> funktionstillstand,
            TypAvFunktionstillstand funktionstillstandsTyp) {
        FunktionstillstandType foundFunktionstillstand = null;
        if (funktionstillstand != null) {
            for (int i = 0; i < funktionstillstand.size(); i++) {
                FunktionstillstandType listFunktionstillstand = funktionstillstand.get(i);
                if (listFunktionstillstand.getTypAvFunktionstillstand() != null
                        && listFunktionstillstand.getTypAvFunktionstillstand().compareTo(funktionstillstandsTyp) == 0) {
                    foundFunktionstillstand = listFunktionstillstand;
                    break;
                }
            }
        }
        return foundFunktionstillstand;
    }

    protected static VardkontaktType findVardkontaktTyp(List<VardkontaktType> vardkontakter, Vardkontakttyp vardkontaktTyp) {
        VardkontaktType foundVardkontaktType = null;
        if (vardkontakter != null) {
            for (int i = 0; i < vardkontakter.size(); i++) {
                VardkontaktType listVardkontakter = vardkontakter.get(i);
                if (listVardkontakter.getVardkontakttyp() != null
                        && listVardkontakter.getVardkontakttyp().compareTo(vardkontaktTyp) == 0) {
                    foundVardkontaktType = listVardkontakter;
                    break;
                }
            }
        }
        return foundVardkontaktType;
    }

    protected static ReferensType findReferensTyp(List<ReferensType> referenser, Referenstyp referensTyp) {
        ReferensType foundReferensType = null;
        if (referenser != null) {
            for (int i = 0; i < referenser.size(); i++) {
                ReferensType listReferenser = referenser.get(i);
                if (listReferenser.getReferenstyp() != null
                        && listReferenser.getReferenstyp().compareTo(referensTyp) == 0) {
                    foundReferensType = listReferenser;
                    break;
                }
            }
        }
        return foundReferensType;
    }

    protected static SysselsattningType findTypAvSysselsattning(List<SysselsattningType> sysselsattning,
            TypAvSysselsattning sysselsattningsTyp) {
        SysselsattningType foundSysselsattningType = null;
        if (sysselsattning != null) {
            for (int i = 0; i < sysselsattning.size(); i++) {
                SysselsattningType listSysselsattning = sysselsattning.get(i);
                if (listSysselsattning.getTypAvSysselsattning() != null
                        && listSysselsattning.getTypAvSysselsattning().compareTo(sysselsattningsTyp) == 0) {
                    foundSysselsattningType = listSysselsattning;
                    break;
                }
            }
        }
        return foundSysselsattningType;
    }

    protected static ArbetsformagaNedsattningType findArbetsformaga(List<ArbetsformagaNedsattningType> arbetsformaga,
            Nedsattningsgrad arbetsformagaNedsattningTyp) {
        ArbetsformagaNedsattningType foundArbetsformagaType = null;
        if (arbetsformaga != null) {
            for (int i = 0; i < arbetsformaga.size(); i++) {
                ArbetsformagaNedsattningType listArbetsformaga = arbetsformaga.get(i);
                if (listArbetsformaga.getNedsattningsgrad() != null
                        && listArbetsformaga.getNedsattningsgrad().compareTo(arbetsformagaNedsattningTyp) == 0) {
                    foundArbetsformagaType = listArbetsformaga;
                    break;
                }
            }
        }
        return foundArbetsformagaType;
    }

    private boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }

}
