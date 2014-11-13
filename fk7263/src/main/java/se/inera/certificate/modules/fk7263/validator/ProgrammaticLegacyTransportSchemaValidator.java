package se.inera.certificate.modules.fk7263.validator;

import static java.util.Arrays.asList;
import static se.inera.certificate.model.util.Strings.isNullOrEmpty;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import se.inera.certificate.model.HosPersonal;
import se.inera.certificate.model.Id;
import se.inera.certificate.model.Kod;
import se.inera.certificate.model.Referens;
import se.inera.certificate.model.Vardenhet;
import se.inera.certificate.model.Vardgivare;
import se.inera.certificate.model.util.Strings;
import se.inera.certificate.modules.fk7263.model.codes.ObservationsKoder;
import se.inera.certificate.modules.fk7263.model.converter.TransportToInternal;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Observation;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Utlatande;

/**
 * Validates presence and validity of formal external model properties such as OID's. Preferably this validation should
 * have been implemented using Schema Validation, but since this is not an option we perform this pre-validation in
 * code. more specific business rules should subsequently validated in {@see UtlatandeValidator}
 *
 * @author marced
 */
public class ProgrammaticLegacyTransportSchemaValidator extends AbstractValidator {

    private Fk7263Utlatande externalutlatande;

    private static final String PERSON_NUMBER_REGEX = "[0-9]{8}[-+][0-9]{4}";

    private static final List<String> PATIENT_ID_OIDS = asList("1.2.752.129.2.1.3.1", "1.2.752.129.2.1.3.3");

    private static final List<String> ENHET_OID = asList("1.2.752.129.2.1.4.1");

    private static final List<String> HOS_PERSONAL_OID = asList("1.2.752.129.2.1.4.1");

    private static final List<String> ARBETSPLATS_CODE_OID = asList("1.2.752.29.4.71");

    public ProgrammaticLegacyTransportSchemaValidator(Fk7263Utlatande externalutlatande) {
        this.externalutlatande = externalutlatande;
    }

    public List<String> validate() {

        validateUtlatande();
        validatePatient();
        validateHospersonal();
        validateReferens();

        return getValidationErrors();
    }

    private void validateReferens() {
        if (externalutlatande.getReferenser() != null) {
            for (Referens r : externalutlatande.getReferenser()) {
                if (r.getDatum() == null) {
                    addValidationError("Field 4: Referens is missing datum");
                }
            }
        }

    }

    private void validateUtlatande() {

        Kod typ = externalutlatande.getTyp();

        if (typ == null || !TransportToInternal.FK_7263.equals(typ.getCode())) {
            addValidationError("Head: Invalid utlatandetyp - must be "
                    + TransportToInternal.FK_7263);
        }
        if (typ == null || !TransportToInternal.UTLATANDE_TYP_OID.equals(typ.getCodeSystem())) {
            addValidationError("Head: Invalid utlatandetyp code system - must be "
                    + TransportToInternal.UTLATANDE_TYP_OID);
        }

        Id id = externalutlatande.getId();
        if (id == null || StringUtils.isEmpty(id.getRoot())) {
            addValidationError("Head: Utlatande Id.root is mandatory!");
        }

        // Check skickat datum - mandatory
        if (externalutlatande.getSkickatdatum() == null) {
            addValidationError("Header: No or wrong skickatDatum found!");
        }
        // Check signeringsdatum - mandatory
        if (externalutlatande.getSigneringsdatum() == null) {
            addValidationError("Field 14: No signeringsDatum found!");
        }
        // Check date interval for arbetsformaga-nedsattning - mandatory
        List<Fk7263Observation> arbetsformagor = externalutlatande.getObservationsByKod(ObservationsKoder.ARBETSFORMAGA);
        for (Fk7263Observation arbetsfarmaga : arbetsformagor) {
            if (arbetsfarmaga.getObservationsperiod() == null) {
                addValidationError("Field 8b: Invalid date interval!");
            }
        }
    }

    private void validatePatient() {
        if (externalutlatande.getPatient() == null) {
            addValidationError("Missing patient element");
            return;
        }

        if (externalutlatande.getPatient().getId() == null) {
            addValidationError("Missing patient Id element");
            return;
        }

        // Check OID
        if (!PATIENT_ID_OIDS.contains(externalutlatande.getPatient().getId().getRoot())) {
            addValidationError(String.format("Wrong o.i.d. for Patient Id! Should be one of %s",
                    Strings.join(" or ", PATIENT_ID_OIDS)));
        }

        // Check format of patient idextension (has to be a valid personnummer)
        String personNummer = externalutlatande.getPatient().getId().getExtension();
        if (isNullOrEmpty(personNummer) || !Pattern.matches(PERSON_NUMBER_REGEX, personNummer)) {
            addValidationError("Header: Invalid format for patient person-id! Valid format is YYYYMMDD-XXXX or YYYYMMDD+XXXX.");
        }

        /*
         * Patient name - mandatory (TransportToExternalFk7263LegacyConverter converts fullstandigtNamn -> Efternamn)
         */
        if (isNullOrEmpty(externalutlatande.getPatient().getEfternamn())) {
            addValidationError("Header: No Patient fullstandigtNamn elements found or set!");

        }

    }

    private void validateHospersonal() {
        HosPersonal hosPersonal = externalutlatande.getSkapadAv();

        if (hosPersonal == null) {
            addValidationError("Field 15: No SkapadAvHosPersonal element found!");
            return;
        }

        if (hosPersonal.getId() == null || !HOS_PERSONAL_OID.contains(hosPersonal.getId().getRoot())) {
            addValidationError(String.format("Field 15: Wrong hos-personal id root! Should be one of %s",
                    Strings.join(",", HOS_PERSONAL_OID)));
        }

        if (StringUtils.isEmpty(hosPersonal.getId().getExtension())) {
            addValidationError("Field 15: hos-personal id not found");
        }
        // Note: hos-personal-name is not mandatory according to insuranceprocess_healthreporting_2.0.xsd ->
        // hosPersonalType
        if (isNullOrEmpty(hosPersonal.getNamn())) {
            addValidationError("Field 15: hos-personal namn not found!");
        }

        validateHosPersonalEnhet();
    }

    private void validateHosPersonalEnhet() {

        HosPersonal hosPersonal = externalutlatande.getSkapadAv();
        Vardenhet vardenhet = hosPersonal.getVardenhet();
        // Check enhets - mandatory
        if (vardenhet == null) {
            addValidationError("Field 15: No hos-personal enhet element found!");
            return;
        }

        if (isNullOrEmpty(vardenhet.getPostadress())) {
            addValidationError("Field 15: No postadress found for enhet!");
        }

        if (isNullOrEmpty(vardenhet.getPostnummer())) {
            addValidationError("Field 15: No postnummer found for enhet!");
        }

        if (isNullOrEmpty(vardenhet.getPostort())) {
            addValidationError("Field 15: No postort found for enhet!");
        }

        if (isNullOrEmpty(vardenhet.getTelefonnummer())) {
            addValidationError("Field 15: No telefonnummer found for enhet!");
        }

        // Check enhetsId - mandatory
        if (vardenhet.getId() == null || !ENHET_OID.contains(vardenhet.getId().getRoot())) {
            addValidationError(String.format(
                    "Field 15: No hos-personal enhetsId root found or was invalid! Should be one of %s",
                    Strings.join(",", ENHET_OID)));
        }
        if (vardenhet.getId() == null || isNullOrEmpty(vardenhet.getId().getExtension())) {
            addValidationError("Field 15: No enhetsId extension found!");
        }

        // Check enhetsnamn - mandatory
        if (isNullOrEmpty(vardenhet.getNamn())) {
            addValidationError("Field 15: No enhetsnamn found!");
        }

        if (vardenhet.getArbetsplatskod() == null) {
            addValidationError("Field 17: No arbetsplatskod found for enhet!");
            return;
        } else if (!ARBETSPLATS_CODE_OID.contains(vardenhet.getArbetsplatskod().getRoot())) {
            addValidationError(String.format(
                    "Field 17: Invalid arbetsplatskod root found for enhet! Should be one of %s",
                    Strings.join(" or ", ARBETSPLATS_CODE_OID)));
        }
        if (vardenhet.getArbetsplatskod() == null || isNullOrEmpty(vardenhet.getArbetsplatskod().getExtension())) {
            addValidationError("Field 17: Invalid arbetsplatskod extension found for enhet!");
        }

        validateVardgivare();

    }

    private void validateVardgivare() {
        HosPersonal hosPersonal = externalutlatande.getSkapadAv();
        Vardenhet vardenhet = hosPersonal.getVardenhet();
        Vardgivare vardgivare = vardenhet.getVardgivare();
        // Check vardgivare root - mandatory
        if (vardgivare == null) {
            addValidationError("Field 15: No vardgivare element found!");
            return;
        }

        if (vardgivare.getId() == null || !ENHET_OID.contains(vardgivare.getId().getRoot())) {
            addValidationError(String.format("Field 15: No vardgivarId root found or was invalid! Should be one of %s",
                    Strings.join(" or ", ENHET_OID)));
        }

        if (vardgivare.getId() == null || isNullOrEmpty(vardgivare.getId().getExtension())) {
            addValidationError("Field 15: No vardgivarId extension found or was invalid!");
        }

        // Check vardgivarename - mandatory
        if (isNullOrEmpty(vardgivare.getNamn())) {
            addValidationError("Field 15: No vardgivarenamn found!");
        }
    }

}
