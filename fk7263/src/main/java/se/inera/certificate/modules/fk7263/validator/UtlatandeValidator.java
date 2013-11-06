package se.inera.certificate.modules.fk7263.validator;

import static se.inera.certificate.model.util.Strings.isNullOrEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.cxf.common.util.StringUtils;
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

    private static final String VALIDATION_ERROR_PREFIX = "Validation Error (im):";

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
            addValidationError("Header: No Lakarutlatande Id found!");
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

    /**
     * Validate patient properties. OID validation for patient is done in {@see ExternalUtlatandeValidator}
     */
    private void validatePatient() {

        // Check format of patient id (has to be a valid personnummer)
        if (isNullOrEmpty(utlatande.getPatientPersonnummer())
                || !Pattern.matches(PERSON_NUMBER_REGEX, utlatande.getPatientPersonnummer())) {
            addValidationError("Header: Wrong format for patient person-id! Valid format is YYYYMMDD-XXXX or YYYYMMDD+XXXX.");
        }

        // Get namn for patient - mandatory
        if (isNullOrEmpty(utlatande.getPatientNamn())) {
            addValidationError("Header: No Patient fullstandigtNamn elements found or set!");

        }
    }
    /**
     * Validate vard person properties. OID validation for hsaId is done in {@see ExternalUtlatandeValidator}
     */
    private void validateVardperson() {
        Vardperson vardperson = utlatande.getVardperson();

        if (vardperson == null) {
            addValidationError("Field 15: No SkapadAvHosPersonal element found!");
            return;
        }

        if (StringUtils.isEmpty(vardperson.getHsaId())) {
            addValidationError("Field 15: hos-personal id not found");
        }
        // Note: hos-personal-name is not mandatory according to insuranceprocess_healthreporting_2.0.xsd ->
        // hosPersonalType
        if (isNullOrEmpty(vardperson.getNamn())) {
            addValidationError("Field 15: hos-personal namn not found!");
        }

        validateHosPersonalEnhet();
    }
    /**
     * Validate enhet properties. OID validation for enhetsId and arbetsplatskod is done in {@see ExternalUtlatandeValidator}
     */
    private void validateHosPersonalEnhet() {

        Vardperson vardperson = utlatande.getVardperson();

        // Check enhetsId - mandatory
        if (isNullOrEmpty(vardperson.getEnhetsId())) {
            addValidationError("Field 15: No enhetsId found!");
        }

        if (isNullOrEmpty(vardperson.getEnhetsnamn())) {
            addValidationError("Field 15: No enhetsnamn found!");
        }
        // Check enhetsnamn - mandatory
        if (isNullOrEmpty(vardperson.getEnhetsnamn())) {
            addValidationError("Field 15: No enhetsnamn found!");
        }

        // Check enhetsadress - mandatory
        if (isNullOrEmpty(vardperson.getPostadress())) {
            addValidationError("Field 15: No postadress found for enhet!");
        }

        if (isNullOrEmpty(vardperson.getPostnummer())) {
            addValidationError("Field 15: No postnummer found for enhet!");
        }

        if (isNullOrEmpty(vardperson.getPostort())) {
            addValidationError("Field 15: No postort found for enhet!");
        }

        if (isNullOrEmpty(vardperson.getTelefonnummer())) {
            addValidationError("Field 15: No telefonnummer found for enhet!");
        }

        if (isNullOrEmpty(vardperson.getArbetsplatsKod())) {
            addValidationError("Field 15: No arbetsplatskod found for enhet!");
        }
        validateVardgivare();

    }
    /**
     * Validate vardgivare properties. OID validation for vardgivarId is done in {@see ExternalUtlatandeValidator}
     */
    private void validateVardgivare() {
        Vardperson vardperson = utlatande.getVardperson();

        // Check enhetsId - mandatory
        if (isNullOrEmpty(vardperson.getVardgivarId())) {
            addValidationError("Field 15: No vardgivarId found!");
        }

        // Check vardgivarename - mandatory
        if (isNullOrEmpty(vardperson.getVardgivarnamn())) {
            addValidationError("Field 15: No vardgivarenamn found!");
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
            addValidationError("Field 2: No tillstandskod in medicinsktTillstand found!");
        }

        validateFunktionsnedsattning();
        validateArbetsformaga();
    }

    private void validateFunktionsnedsattning() {

        // Fält 4 - vänster Check that we got a funktionsnedsattning element
        String funktionsnedsattning = utlatande.getFunktionsnedsattning();
        if (funktionsnedsattning == null) {
            addValidationError("Field 4: No funktionsnedsattning element found!");
            return;
        }

        // Fält 4 - höger Check that we at least got one field set
        if (utlatande.getUndersokningAvPatienten() == null && utlatande.getTelefonkontaktMedPatienten() == null
                && utlatande.getJournaluppgifter() == null && utlatande.getAnnanReferens() == null) {
            addValidationError("Field 4: At least 1 vardkontakt or referens element must be set!");
            return;
        }
    }

    private void validateArbetsformaga() {

        // Fält 8a - arbetsformoga - sysselsattning
        boolean hasArbetsuppgifts = utlatande.getNuvarandeArbetsuppgifter() != null;
        
        if (!hasArbetsuppgifts && !utlatande.isArbetsloshet() && !utlatande.isForaldrarledighet()) {
            addValidationError("Field 8a: At least 1 sysselsattning must be set");
        }
        
        
        // validate 8b
        boolean valid100 = validArbetsformageNedsattning(utlatande.getNedsattMed100());
        boolean valid75 = validArbetsformageNedsattning(utlatande.getNedsattMed75());
        boolean valid50 = validArbetsformageNedsattning(utlatande.getNedsattMed50());
        boolean valid25 = validArbetsformageNedsattning(utlatande.getNedsattMed25());

        if (!valid100 && !valid75 && !valid50 && !valid25) {
            addValidationError("Field 8b: Invalid span in arbetsformaga nedsattningfield.");
        }

        if (utlatande.getNedsattMed100() == null && utlatande.getNedsattMed75() == null
                && utlatande.getNedsattMed50() == null && utlatande.getNedsattMed25() == null) {
            addValidationError("Field 8b: At least 1 nedsatt arbetsformaga field must be filled");
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
                addValidationError("Field 8b: Invalid date interval (from " + nedsattning.getStart() + ", tom "
                        + nedsattning.getEnd());
            } else {
                return true;
            }

        } else {
            // No period given
            addValidationError("Field 8b: No date interval given");
        }

        return false;
    }

    private void validateRessatt() {
        // Fält 11 - optional
        boolean inForandratRessatt = utlatande.isRessattTillArbeteAktuellt();
        boolean inEjForandratRessatt = utlatande.isRessattTillArbeteEjAktuellt();

        // Fält 11 - If set only one should be set
        if (inForandratRessatt && inEjForandratRessatt) {
            addValidationError("Field 11: Only one forandrat ressatt could be set.");
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
            addValidationError("Field 13: Field should contain data as field 4 or fields 10 are checked.");
        }
    }

    private void addValidationError(String errorDesc) {
        validationErrors.add(VALIDATION_ERROR_PREFIX + errorDesc);
    }
}
