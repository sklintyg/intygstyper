package se.inera.certificate.modules.fk7263.validator;

import static java.util.Arrays.asList;
import static se.inera.certificate.model.util.Strings.isNullOrEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import se.inera.certificate.model.Aktivitet;
import se.inera.certificate.model.Arbetsuppgift;
import se.inera.certificate.model.HosPersonal;
import se.inera.certificate.model.Id;
import se.inera.certificate.model.Observation;
import se.inera.certificate.model.Referens;
import se.inera.certificate.model.Vardenhet;
import se.inera.certificate.model.Vardgivare;
import se.inera.certificate.model.Vardkontakt;
import se.inera.certificate.modules.fk7263.model.codes.ObservationsKoder;
import se.inera.certificate.modules.fk7263.model.codes.Prognoskoder;
import se.inera.certificate.modules.fk7263.model.codes.Referenstypkoder;
import se.inera.certificate.modules.fk7263.model.codes.Vardkontakttypkoder;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Patient;
import se.inera.certificate.modules.fk7263.model.internal.Fk7263Intyg;

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

        validatePatient(utlatande.getPatient());

        validateNonSmittskydd();
        validateSkapadAvHosPersonal(utlatande.getSkapadAv());
        validateRessatt();
        validateKommentar();

        return validationErrors;
    }

    private void validateUtlatande() {
        if (utlatande.getId() == null || isNullOrEmpty(utlatande.getId().getExtension())) {
            validationErrors.add("Header: No Lakarutlatande Id found!");
        }

        // Check skickat datum - mandatory
        if (utlatande.getSkickatDatum() == null) {
            validationErrors.add("Header: No or wrong skickatDatum found!");
        }
        // Check signeringsdatum - mandatory
        if (utlatande.getSigneringsDatum() == null) {
            validationErrors.add("Field 14: No signeringsDatum found!");
        }

    }

    private void validatePatient(Fk7263Patient patient) {

        if (patient == null) {
            validationErrors.add("Header: No Patient element found!");
            // cant check any more properties on a null patient
            return;
        }
        // Check format of patient id (has to be a valid personnummer)
        if (patient.getId() == null || isNullOrEmpty(patient.getId().getExtension())
                || !Pattern.matches(PERSON_NUMBER_REGEX, patient.getId().getExtension())) {
            validationErrors
                    .add("Header: Wrong format for patient person-id! Valid format is YYYYMMDD-XXXX or YYYYMMDD+XXXX.");
        }

        // Check patient o.i.d.
        if (patient.getId() == null || isNullOrEmpty(patient.getId().getRoot())
                || !PATIENT_ID_OIDS.contains(patient.getId().getRoot())) {
            validationErrors.add(String.format("Header: Wrong o.i.d. for Patient Id! Should be one of %s",
                    StringUtils.arrayToCommaDelimitedString(PATIENT_ID_OIDS.toArray())));
        }

        // Get namn for patient - mandatory
        if (isNullOrEmpty(patient.getFullstandigtNamn())) {
            validationErrors.add("Header: No Patient fullstandigtNamn elements found or set!");
        }
    }

    private void validateSkapadAvHosPersonal(HosPersonal hosPersonal) {

        if (hosPersonal == null) {
            validationErrors.add("Field 15: No SkapadAvHosPersonal element found!");
            return;
        }

        // Check hos-personal id - mandatory
        validateIdEntity("Field 15: hos-personal-id", hosPersonal.getId(), HOS_PERSONAL_OID);

        // Note: hos-personal-name is not mandatory according to insuranceprocess_healthreporting_2.0.xsd ->
        // hosPersonalType
        if (isNullOrEmpty(hosPersonal.getNamn())) {
            validationErrors.add("Field 15: hos-personal namn not found!");
        }
        validateHosPersonalEnhet(hosPersonal.getVardenhet());
    }

    private void validateHosPersonalEnhet(Vardenhet enhet) {

        if (enhet == null) {
            validationErrors.add("Field 15: No enhet element found!");
            return;
        }

        // Check enhets id - mandatory
        validateIdEntity("enhets-id", enhet.getId(), ENHET_OID);

        // Check enhetsnamn - mandatory
        if (isNullOrEmpty(enhet.getNamn())) {
            validationErrors.add("Field 15: No enhetsnamn found!");
        }

        // Check enhetsadress - mandatory
        if (isNullOrEmpty(enhet.getPostadress())) {
            validationErrors.add("Field 15: No postadress found for enhet!");
        }

        if (isNullOrEmpty(enhet.getPostnummer())) {
            validationErrors.add("Field 15: No postnummer found for enhet!");
        }

        if (isNullOrEmpty(enhet.getPostort())) {
            validationErrors.add("Field 15: No postort found for enhet!");
        }

        if (isNullOrEmpty(enhet.getTelefonnummer())) {
            validationErrors.add("Field 15: No telefonnummer found for enhet!");
        }

        validateVardgivare(enhet.getVardgivare());

        validateIdEntity("Field 17: Arbetsplatskod", enhet.getArbetsplatskod(), ARBETSPLATS_CODE_OID);

    }

    private void validateVardgivare(Vardgivare vardgivare) {
        if (vardgivare == null) {
            validationErrors.add("Field 15: No vardgivare element found!");
            return;
        }

        // Check vardgivare id - mandatory
        validateIdEntity("Field 15: vardgivare-id", vardgivare.getId(), HOS_PERSONAL_OID);

        // Check vardgivarename - mandatory
        if (isNullOrEmpty(vardgivare.getNamn())) {
            validationErrors.add("Field 15: No vardgivarenamn found!");
        }

    }

    private void validateNonSmittskydd() {

        // Many fields are optional if smittskydd is checked, if not set validate these below
        boolean inSmittskydd = utlatande.getAvstangningEnligtSmittskyddslagen() != null;

        if (inSmittskydd) {
            return;
        }

        // Fält 2 - Check that we got a medicinsktTillstand element
        if (utlatande.getMedicinsktTillstand() == null) {
            validationErrors.add("Field 2: No medicinsktTillstand found!");
            return;
        }

        // Fält 2 - Medicinskt tillstånd kod - mandatory
        if (utlatande.getMedicinsktTillstand().getObservationsKod() == null
                || isNullOrEmpty(utlatande.getMedicinsktTillstand().getObservationsKod().getCode())) {
            validationErrors.add("Field 2: No tillstandskod in medicinsktTillstand found!");
        }

        validateFunktionsnedsattning();
        validateArbetsformaga();
    }

    private void validateFunktionsnedsattning() {

        // Fält 4 - vänster Check that we got a funktionsnedsattning element
        Observation funktionsnedsattning = utlatande.getFunktionsnedsattning();
        if (funktionsnedsattning == null) {
            validationErrors.add("Field 4: No funktionsnedsattning element found!");
            return;
        }

        // Fält 4 - höger översta kryssrutan
        Vardkontakt inUndersokning = utlatande.getVardkontakt(Vardkontakttypkoder.MIN_UNDERSOKNING_AV_PATIENTEN);

        // Fält 4 - höger näst översta kryssrutan
        Vardkontakt telefonkontakt = utlatande.getVardkontakt(Vardkontakttypkoder.MIN_TELEFONKONTAKT_MED_PATIENTEN);

        // Fält 4 - höger näst nedersta kryssrutan
        Referens journal = utlatande.getReferens(Referenstypkoder.JOURNALUPPGIFT);

        // Fält 4 - höger nedersta kryssrutan
        Referens inAnnat = utlatande.getReferens(Referenstypkoder.ANNAT);

        // Fält 4 - höger Check that we at least got one field set
        if (inUndersokning == null && telefonkontakt == null && journal == null && inAnnat == null) {
            validationErrors.add("Field 4: At least 1 vardkontakt or referens element must be set!");
            return;
        }
    }

    private void validateArbetsformaga() {

        // Fält 8a - arbetsformoga
        List<Arbetsuppgift> arbetsuppgifts = utlatande.getPatient().getArbetsuppgifts();
        boolean hasArbetsuppgifts = (arbetsuppgifts != null && !arbetsuppgifts.isEmpty());

        // Fält 8a
        boolean nuvarandeArbete = utlatande.isArbetsformagaIForhallandeTillNuvarandeArbete();

        // Fält 8a - Check that we got a arbetsuppgift element if arbete is set
        if (nuvarandeArbete && !hasArbetsuppgifts) {
            validationErrors.add("Field 8a: No arbetsuppgift found when arbete set in field 8a!.");
            return;
        }
        // validate 8b
        boolean valid100 = validArbetsformageNedsattning(utlatande.getNedsattning100percent());
        boolean valid75 = validArbetsformageNedsattning(utlatande.getNedsattning75percent());
        boolean valid50 = validArbetsformageNedsattning(utlatande.getNedsattning50percent());
        boolean valid25 = validArbetsformageNedsattning(utlatande.getNedsattning25percent());

        if (!valid100 && !valid75 && !valid50 && !valid25) {
            validationErrors.add("Field 8b: At least 1 nedsatt arbetsformaga field must be filled");
        }

    }

    /**
     * Checks that the given Observation is about arbetsformaga and has a period where tom>from
     * 
     * @param nedsattning
     * @return
     */
    private boolean validArbetsformageNedsattning(Observation nedsattning) {

        if (nedsattning == null || !ObservationsKoder.ARBETSFORMAGA.equals(nedsattning.getObservationsKod())) {
            return false;
        }
        if ((nedsattning.getObservationsPeriod() != null) && (nedsattning.getObservationsPeriod().getFrom() != null)
                && (nedsattning.getObservationsPeriod().getTom() != null)) {
            if (nedsattning.getObservationsPeriod().getFrom().isBefore(nedsattning.getObservationsPeriod().getTom())) {
                return true;
            } else {
                // Must be something wrong with the observationPeriod:
                validationErrors.add("Field 8b: Invalid date interval (from "
                        + nedsattning.getObservationsPeriod().getFrom() + ", tom "
                        + nedsattning.getObservationsPeriod().getTom());
            }

        } else {
            // No period given
            validationErrors.add("Field 8b: No date interval given");
        }

        return false;
    }

    private void validateRessatt() {
        // Fält 11 - optional
        Aktivitet inForandratRessatt = utlatande.getForandratRessattAktuellt();
        Aktivitet inEjForandratRessatt = utlatande.getForandratRessattEjAktuellt();

        // Fält 11 - If set only one should be set
        if (inForandratRessatt != null && inEjForandratRessatt != null) {
            validationErrors.add("Field 11: Only one forandrat ressatt could be set.");
        }
    }

    private void validateKommentar() {

        // Fält 13 - Upplysningar - optional
        // If field 4 annat satt or field 10 går ej att bedömma is set then
        // field 13 should contain data.
        boolean hasKommentar = utlatande.getKommentars() != null && !utlatande.getKommentars().isEmpty();

        Referens annat = utlatande.getReferens(Referenstypkoder.ANNAT);
        boolean garEjAttBedomma = utlatande.getArbetsformaga() != null
                && utlatande.getArbetsformaga().getPrognoser() != null
                && !utlatande.getArbetsformaga().getPrognoser().isEmpty()
                && Prognoskoder.DET_GAR_INTE_ATT_BEDOMA.equals(utlatande.getArbetsformaga().getPrognoser().get(0)
                        .getPrognosKod());

        if ((annat != null || garEjAttBedomma) && !hasKommentar) {
            validationErrors.add("Field 13: Field should contain data as field 4 or fields 10 are checked.");
        }
    }

    /**
     * Check the validity of an mandatory Id attribute
     * 
     * @param fieldName
     *            - Name of id field used to construct validation error message
     * @param mandatoryId
     *            - The Id to check
     * @param validOIDs
     *            - List of valid OID's for this Id
     * @return
     */
    private boolean validateIdEntity(String fieldName, Id mandatoryId, List<String> validOIDs) {
        if (mandatoryId == null) {
            validationErrors.add(fieldName + " element not found!");
            return false;
        }

        // Check id - mandatory
        if (isNullOrEmpty(mandatoryId.getExtension())) {
            validationErrors.add(fieldName + " No extension found");
            return false;
        }

        // Check o.i.d existence
        if (isNullOrEmpty(mandatoryId.getRoot())) {
            validationErrors.add(fieldName + " No root o.i.d found");
            return false;
        }

        // Check that o.i.d value is valid
        if (!validOIDs.contains(mandatoryId.getRoot())) {
            validationErrors.add(fieldName + " Invalid o.i.d. - Should be one of ["
                    + StringUtils.collectionToCommaDelimitedString(validOIDs) + "]");
            return false;
        }

        return true;
    }
}
