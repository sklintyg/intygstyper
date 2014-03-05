package se.inera.certificate.modules.fk7263.validator;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import se.inera.certificate.model.HosPersonal;
import se.inera.certificate.model.Id;
import se.inera.certificate.model.Kod;
import se.inera.certificate.model.Observation;
import se.inera.certificate.model.Vardenhet;
import se.inera.certificate.model.Vardgivare;
import se.inera.certificate.model.util.Strings;
import se.inera.certificate.modules.fk7263.model.codes.ObservationsKoder;
import se.inera.certificate.modules.fk7263.model.converter.TransportToExternalFk7263LegacyConverter;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Utlatande;

/**
 * Validates presence and validity of formal external model properties such as OID's. Preferably this validation should
 * have been implemented using Schema Validation, but since this is not an option we perform this pre-validation in
 * code. more specific business rules should subsequently validated in {@see UtlatandeValidator}
 * 
 * @author marced
 */
public class ProgrammaticLegacyTransportSchemaValidator {

    private Fk7263Utlatande externalutlatande;

    private List<String> validationErrors = new ArrayList<>();

    private static final List<String> PATIENT_ID_OIDS = asList("1.2.752.129.2.1.3.1", "1.2.752.129.2.1.3.3");

    private static final List<String> ENHET_OID = asList("1.2.752.129.2.1.4.1");

    private static final List<String> HOS_PERSONAL_OID = asList("1.2.752.129.2.1.4.1");

    private static final List<String> ARBETSPLATS_CODE_OID = asList("1.2.752.29.4.71");

    private static final List<String> VALID_DIAGNOSE_CODE_SYSTEMS = asList("ICD-10");

    private static final String VALIDATION_ERROR_PREFIX = "Validation Error (em):";

    public ProgrammaticLegacyTransportSchemaValidator(Fk7263Utlatande externalutlatande) {
        this.externalutlatande = externalutlatande;
    }

    public List<String> validate() {
        
        validateUtlatande();
        
        validateDiagonse();

        validatePatient();

        validateHospersonal();

        return validationErrors;
    }

    private void validateUtlatande() {
        
        Kod typ = externalutlatande.getTyp();
        if (!TransportToExternalFk7263LegacyConverter.FK_7263.equals(typ.getCode())) {
            addValidationError("Head: Invalid utlatandetyp - must be " + TransportToExternalFk7263LegacyConverter.FK_7263);
        }
        if (!TransportToExternalFk7263LegacyConverter.UTLATANDE_TYP_OID.equals(typ.getCodeSystem())) {
            addValidationError("Head: Invalid utlatandetyp code system - must be " + TransportToExternalFk7263LegacyConverter.UTLATANDE_TYP_OID);
        }
        
        
        Id id = externalutlatande.getId();
        if (StringUtils.isEmpty(id.getRoot())) {
            addValidationError("Head: Utlatande Id.root is mandatory!");
        }
        
        
        
    }

    private void validateDiagonse() {

        Observation huvudDiagnos = externalutlatande.findObservationByKategori(ObservationsKoder.DIAGNOS);
        if (huvudDiagnos == null || huvudDiagnos.getObservationskod() == null
                || StringUtils.isEmpty(huvudDiagnos.getObservationskod().getCode())) {
            if (!externalutlatande.isAvstangningEnligtSmL()) {
                addValidationError("Field 2: Missing diagnose code");
            }
            return;
        }

        if (StringUtils.isEmpty(huvudDiagnos.getObservationskod().getCodeSystemName())
                || !VALID_DIAGNOSE_CODE_SYSTEMS.contains(huvudDiagnos.getObservationskod().getCodeSystemName())) {
            addValidationError(String.format("Field 2: Invalid diagnose code system! Should be one of %s",
                    Strings.join(" or ", VALID_DIAGNOSE_CODE_SYSTEMS)));
            return;
        }

    }

    private void validatePatient() {
        if (externalutlatande.getPatient() == null) {
            addValidationError("missing patient element");
            return;
        }

        if (externalutlatande.getPatient().getId() == null
                || !PATIENT_ID_OIDS.contains(externalutlatande.getPatient().getId().getRoot())) {
            addValidationError(String.format("Wrong o.i.d. for Patient Id! Should be one of %s",
                    Strings.join(" or ", PATIENT_ID_OIDS)));
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

        // Check enhetsIdRoot - mandatory
        if (vardenhet.getId() == null || !ENHET_OID.contains(vardenhet.getId().getRoot())) {
            addValidationError(String.format(
                    "Field 15: No hos-personal enhetsId root found or was invalid! Should be one of %s",
                    Strings.join(",", ENHET_OID)));
        }

        if (vardenhet.getArbetsplatskod() == null) {
            addValidationError("Field 17: No arbetsplatskod found for enhet!");
            return;
        }

        if (vardenhet.getArbetsplatskod() == null
                || !ARBETSPLATS_CODE_OID.contains(vardenhet.getArbetsplatskod().getRoot())) {
            addValidationError(String.format(
                    "Field 17: Invalid arbetsplatskod root found for enhet! Should be one of %s",
                    Strings.join(" or ", ARBETSPLATS_CODE_OID)));
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
    }

    private void addValidationError(String errorDesc) {
        validationErrors.add(VALIDATION_ERROR_PREFIX + errorDesc);
    }
}
