package se.inera.certificate.modules.fk7263.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;
import static se.inera.certificate.model.util.Strings.isNullOrEmpty;

import org.joda.time.LocalDate;
import se.inera.certificate.model.LocalDateInterval;
import se.inera.certificate.modules.fk7263.model.internal.Fk7263Intyg;
import se.inera.certificate.modules.fk7263.model.internal.Vardperson;

/**
 * @author andreaskaltenbach, marced
 */
public class UtlatandeValidator {

    private Fk7263Intyg utlatande;

    private List<String> validationErrors = new ArrayList<>();

    public static final String PERSON_NUMBER_REGEX = "[0-9]{8}[-+][0-9]{4}";

    private static final List<String> PATIENT_ID_OIDS = asList("1.2.752.129.2.1.3.1", "1.2.752.129.2.1.3.3");

    private static final List<String> ENHET_OID = asList("1.2.752.129.2.1.4.1");

    private static final List<String> HOS_PERSONAL_OID = asList("1.2.752.129.2.1.4.1");

    public static final List<String> ARBETSPLATS_CODE_OID = asList("1.2.752.29.4.71");

    public UtlatandeValidator(Fk7263Intyg utlatande) {
        this.utlatande = utlatande;
    }

    public List<String> validate() {

        validateUtlatande();

        validatePatient();

        validateNonSmittskydd();
        validateVardperson();
        validateRessatt();
        validateKommentar();

        return validationErrors;
    }

    private void validateUtlatande() {
        if (utlatande.getId() == null) {
            validationErrors.add("Header: No Lakarutlatande Id found!");
        }

        // Check skickat datum - mandatory
        if (utlatande.getSkickatDatum() == null) {
            validationErrors.add("Header: No or wrong skickatDatum found!");
        }
        // Check signeringsdatum - mandatory
        if (utlatande.getSigneringsdatum() == null) {
            validationErrors.add("Field 14: No signeringsDatum found!");
        }

    }

    private void validatePatient() {

        // Check format of patient id (has to be a valid personnummer)
        if (isNullOrEmpty(utlatande.getPatientPersonnummer())
                || !Pattern.matches(PERSON_NUMBER_REGEX, utlatande.getPatientPersonnummer())) {
            validationErrors
                    .add("Header: Wrong format for patient person-id! Valid format is YYYYMMDD-XXXX or YYYYMMDD+XXXX.");
        }

        // Get namn for patient - mandatory
        if (isNullOrEmpty(utlatande.getPatientNamn())) {
            validationErrors.add("Header: No Patient fullstandigtNamn elements found or set!");
        }
    }

    private void validateVardperson() {

        if (utlatande.getVardperson() == null) {
            validationErrors.add("Field 15: No SkapadAvHosPersonal element found!");
            return;
        }

        // Note: hos-personal-name is not mandatory according to insuranceprocess_healthreporting_2.0.xsd ->
        // hosPersonalType
        if (isNullOrEmpty(utlatande.getVardperson().getNamn())) {
            validationErrors.add("Field 15: hos-personal namn not found!");
        }
        validateHosPersonalEnhet();
    }

    private void validateHosPersonalEnhet() {

        Vardperson vardperson = utlatande.getVardperson();

        // Check enhetsnamn - mandatory
        if (isNullOrEmpty(vardperson.getEnhetsnamn())) {
            validationErrors.add("Field 15: No enhetsnamn found!");
        }

        // Check enhetsadress - mandatory
        if (isNullOrEmpty(vardperson.getPostadress())) {
            validationErrors.add("Field 15: No postadress found for enhet!");
        }

        if (isNullOrEmpty(vardperson.getPostnummer())) {
            validationErrors.add("Field 15: No postnummer found for enhet!");
        }

        if (isNullOrEmpty(vardperson.getPostort())) {
            validationErrors.add("Field 15: No postort found for enhet!");
        }

        if (isNullOrEmpty(vardperson.getTelefonnummer())) {
            validationErrors.add("Field 15: No telefonnummer found for enhet!");
        }

        validateVardgivare();

    }

    private void validateVardgivare() {

        // Check vardgivarename - mandatory
        if (isNullOrEmpty(utlatande.getVardperson().getVardgivarnamn())) {
            validationErrors.add("Field 15: No vardgivarenamn found!");
        }
    }

    private void validateNonSmittskydd() {

        // Many fields are optional if smittskydd is checked, if not set validate these below
        boolean inSmittskydd = utlatande.isAvstangningSmittskydd();

        if (inSmittskydd) {
            return;
        }

        // Fält 2 - Medicinskt tillstånd kod - mandatory
        if (isNullOrEmpty(utlatande.getDiagnosKod())) {
            validationErrors.add("Field 2: No tillstandskod in medicinsktTillstand found!");
        }

        validateFunktionsnedsattning();
        validateArbetsformaga();
    }

    private void validateFunktionsnedsattning() {

        // Fält 4 - vänster Check that we got a funktionsnedsattning element
        String funktionsnedsattning = utlatande.getFunktionsnedsattning();
        if (funktionsnedsattning == null) {
            validationErrors.add("Field 4: No funktionsnedsattning element found!");
            return;
        }

        // Fält 4 - höger Check that we at least got one field set
        if (utlatande.getUndersokningAvPatienten() == null && utlatande.getTelefonkontaktMedPatienten() == null
                && utlatande.getJournaluppgifter() == null && utlatande.getAnnanReferens() == null) {
            validationErrors.add("Field 4: At least 1 vardkontakt or referens element must be set!");
            return;
        }
    }

    private void validateArbetsformaga() {

        // Fält 8a - arbetsformoga
        boolean hasArbetsuppgifts = utlatande.getNuvarandeArbetsuppgifter() != null;

        // validate 8b
        boolean valid100 = validArbetsformageNedsattning(utlatande.getNedsattMed100());
        boolean valid75 = validArbetsformageNedsattning(utlatande.getNedsattMed75());
        boolean valid50 = validArbetsformageNedsattning(utlatande.getNedsattMed50());
        boolean valid25 = validArbetsformageNedsattning(utlatande.getNedsattMed25());

        if (!valid100 && !valid75 && !valid50 && !valid25) {
            validationErrors.add("Field 8b: Invalid span in arbetsformaga nedsattningfield.");
        }

        if (utlatande.getNedsattMed100() == null && utlatande.getNedsattMed75() == null
                && utlatande.getNedsattMed50() == null && utlatande.getNedsattMed25() == null) {
            validationErrors.add("Field 8b: At least 1 nedsatt arbetsformaga field must be filled");
        }

    }

    /**
     * Checks that the given Observation is about arbetsformaga and has a period where tom>from
     * 
     * @param nedsattning
     * @return
     */
    private boolean validArbetsformageNedsattning(LocalDateInterval nedsattning) {

        if (nedsattning == null) {
            return true;
        }

        if (nedsattning.getStart() != null && nedsattning.getEnd() != null) {
            if (nedsattning.getEnd().isBefore(nedsattning.getStart())) {
                // Must be something wrong with the observationPeriod:
                validationErrors.add("Field 8b: Invalid date interval (from " + nedsattning.getStart() + ", tom "
                        + nedsattning.getEnd());
            } else {
                return true;
            }

        } else {
            // No period given
            validationErrors.add("Field 8b: No date interval given");
        }

        return false;
    }

    private void validateRessatt() {
        // Fält 11 - optional
        boolean inForandratRessatt = utlatande.isRessattTillArbeteAktuellt();
        boolean inEjForandratRessatt = utlatande.isRessattTillArbeteEjAktuellt();

        // Fält 11 - If set only one should be set
        if (inForandratRessatt && inEjForandratRessatt) {
            validationErrors.add("Field 11: Only one forandrat ressatt could be set.");
        }
    }

    private void validateKommentar() {

        // Fält 13 - Upplysningar - optional
        // If field 4 annat satt or field 10 går ej att bedömma is set then
        // field 13 should contain data.
        boolean hasKommentar = utlatande.getKommentar() != null;

        LocalDate annat = utlatande.getAnnanReferens();

        boolean garEjAttBedomma = utlatande.isArbetsformataPrognosGarInteAttBedoma();

        if ((annat != null || garEjAttBedomma) && !hasKommentar) {
            validationErrors.add("Field 13: Field should contain data as field 4 or fields 10 are checked.");
        }
    }
}
