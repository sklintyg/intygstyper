package se.inera.certificate.modules.ts_bas.validator.internal;

import se.inera.certificate.modules.ts_bas.model.codes.ObservationsKod;
import se.inera.certificate.modules.ts_bas.model.internal.Bedomning;
import se.inera.certificate.modules.ts_bas.model.internal.Diabetes;
import se.inera.certificate.modules.ts_bas.model.internal.Funktionsnedsattning;
import se.inera.certificate.modules.ts_bas.model.internal.HjartKarl;
import se.inera.certificate.modules.ts_bas.model.internal.HoSPersonal;
import se.inera.certificate.modules.ts_bas.model.internal.HorselBalans;
import se.inera.certificate.modules.ts_bas.model.internal.IntygAvser;
import se.inera.certificate.modules.ts_bas.model.internal.Kognitivt;
import se.inera.certificate.modules.ts_bas.model.internal.Medicinering;
import se.inera.certificate.modules.ts_bas.model.internal.Medvetandestorning;
import se.inera.certificate.modules.ts_bas.model.internal.NarkotikaLakemedel;
import se.inera.certificate.modules.ts_bas.model.internal.Sjukhusvard;
import se.inera.certificate.modules.ts_bas.model.internal.Syn;
import se.inera.certificate.modules.ts_bas.model.internal.Utlatande;
import se.inera.certificate.modules.ts_bas.rest.dto.ValidateDraftResponseHolder;
import se.inera.certificate.modules.ts_bas.rest.dto.ValidationMessage;
import se.inera.certificate.modules.ts_bas.rest.dto.ValidationStatus;

/**
 * Class for validating drafts of the internal model
 * 
 * @author erik
 * 
 */
public class InternalValidatorInstance {

    private ValidateDraftResponseHolder validationResponse;

    public InternalValidatorInstance() {
        validationResponse = new ValidateDraftResponseHolder();
    }

    /**
     * Validates an internal draft of an {@link Utlatande} (this means the object being validated is not necessarily
     * complete)
     * 
     * @param utlatande
     *            an internal {@link Utlatande}
     * @return a {@link ValidateDraftResponseHolder} with a status and a list of validationErrors
     */
    public ValidateDraftResponseHolder validate(Utlatande utlatande) {

        validateBedomning(utlatande.getBedomning());
        validateDiabetes(utlatande.getDiabetes());
        validateFunktionsnedsattning(utlatande.getFunktionsnedsattning());
        validateHjartKarl(utlatande.getHjartKarl());
        validateHorselBalans(utlatande.getHorselBalans());
        validateHoSPersonal(utlatande.getSkapadAv());
        validateIntygAvser(utlatande.getIntygAvser());
        validateKognitivt(utlatande.getKognitivt());
        validateMedicinering(utlatande.getMedicinering());
        validateMedvetandestorning(utlatande.getMedvetandestorning());
        validateSyn(utlatande.getSyn());
        validateNarkotikaLakemedel(utlatande.getNarkotikaLakemedel());
        validateSjukhusvard(utlatande.getSjukhusvard());
        validationResponse.setStatus(getValidationStatus());

        return validationResponse;
    }

    private void validateSjukhusvard(Sjukhusvard sjukhusvard) {
        if (sjukhusvard.getSjukhusEllerLakarkontakt()) {
            assertDescriptionNotEmpty(sjukhusvard.getAnledning(), "sjukhusvard.anledning", "Error");
            assertDescriptionNotEmpty(sjukhusvard.getTidpunkt(), "sjukhusvard.tidpunkt", "Error");
            assertDescriptionNotEmpty(sjukhusvard.getVardinrattning(), "sjukhusvard.vardinrattning", "Error");
        }
    }

    private void validateBedomning(final Bedomning bedomning) {
        if (bedomning.getKanInteTaStallning() == null && bedomning.getKorkortstyp().isEmpty()) {
            addValidationError("bedomning", "NULL or empty");
        }
    }

    private void validateDiabetes(final Diabetes diabetes) {
        if (diabetes.getHarDiabetes()) {
            if (diabetes.getDiabetesTyp() == null) {
                addValidationError("diabetes.diabetesTyp", "No diabetestyp!");

            } else if (diabetes.getDiabetesTyp().equals(ObservationsKod.DIABETES_TYP_2.name())) {
                if (diabetes.getInsulin() == null && diabetes.getKost() == null && diabetes.getTabletter() == null) {
                    addValidationError("diabetes.diabetesTyp", "Minst en behandling måste anges");
                }
            }
        }
    }

    private void validateFunktionsnedsattning(final Funktionsnedsattning funktionsnedsattning) {
        if (funktionsnedsattning.getFunktionsnedsattning()) {
            assertDescriptionNotEmpty(funktionsnedsattning.getBeskrivning(), "funktionsnedsattning.beskrivning",
                    "Error");
        }
    }

    private void validateHjartKarl(final HjartKarl hjartKarl) {
        if (hjartKarl.getRiskfaktorerStroke()) {
            assertDescriptionNotEmpty(hjartKarl.getBeskrivningRiskfaktorer(), "hjartKarl.beskrivningRiskfaktorer",
                    "Error");
        }
    }

    private void validateHorselBalans(final HorselBalans horselBalans) {
        if (horselBalans.getBalansrubbningar() == null) {
            addValidationError("horselBalans.balansrubbningar", "NULL");
        }
    }

    private void validateHoSPersonal(final HoSPersonal skapadAv) {
        // TODO: validate something here?
    }

    private void validateIntygAvser(final IntygAvser intygAvser) {
        if (intygAvser.getKorkortstyp().isEmpty()) {
            addValidationError("intygAvser", "At least one should be present");
        }
    }

    private void validateKognitivt(final Kognitivt kognitivt) {
        if (kognitivt.getSviktandeKognitivFunktion() == null) {
            addValidationError("kognitivt.sviktandeKognitivFunktion", "NULL");
        }
    }

    private void validateMedicinering(final Medicinering medicinering) {
        if (medicinering.getStadigvarandeMedicinering()) {
            assertDescriptionNotEmpty(medicinering.getBeskrivning(), "medicinering.beskrivning", "error");
        }
    }

    private void validateNarkotikaLakemedel(final NarkotikaLakemedel narkotikaLakemedel) {
        if (narkotikaLakemedel.getLakarordineratLakemedelsbruk()) {
            assertDescriptionNotEmpty(narkotikaLakemedel.getLakemedelOchDos(), "narkotikaLakemedel.getLakemedelOchDos",
                    "Error");
        }
    }

    private void validateMedvetandestorning(final Medvetandestorning medvetandestorning) {
        if (medvetandestorning.getMedvetandestorning()) {
            assertDescriptionNotEmpty(medvetandestorning.getBeskrivning(), "medvetandestorning.beskrivning", "Error");
        }
    }

    private void validateSyn(final Syn syn) {
        if (syn.getHogerOga().getUtanKorrektion() < 0.0 || syn.getHogerOga().getUtanKorrektion() > 2.0) {
            addValidationError("hogerOga.utanKorrektion", "ErrorCode");
        }

        if (syn.getHogerOga().getMedKorrektion() != null) {
            if (syn.getHogerOga().getMedKorrektion() < 0.0 || syn.getHogerOga().getMedKorrektion() > 2.0) {
                addValidationError("hogerOga.medKorrektion", "ErrorCode");
            }
        }

        if (syn.getVansterOga().getUtanKorrektion() < 0.0 || syn.getVansterOga().getUtanKorrektion() > 2.0) {
            addValidationError("vansterOga.utanKorrektion", "ErrorCode");
        }

        if (syn.getVansterOga().getMedKorrektion() != null) {
            if (syn.getVansterOga().getMedKorrektion() < 0.0 || syn.getVansterOga().getMedKorrektion() > 2.0) {
                addValidationError("vansterOga.medKorrektion", "ErrorCode");
            }
        }

        if (syn.getBinokulart().getUtanKorrektion() < 0.0 || syn.getBinokulart().getUtanKorrektion() > 2.0) {
            addValidationError("binokulart.utanKorrektion", "ErrorCode");
        }

        if (syn.getBinokulart().getMedKorrektion() != null) {
            if (syn.getBinokulart().getMedKorrektion() < 0.0 || syn.getBinokulart().getMedKorrektion() > 2.0) {
                addValidationError("binokulart.medKorrektion", "ErrorCode");
            }
        }
    }

    /**
     * Check for null or empty String, if so add a validation error for field with errorCode
     * 
     * @param beskrivning
     *            the String to check
     * @param field
     *            the target field in the model
     * @param errorCode
     *            the errorCode to log in validation errors
     */
    private void assertDescriptionNotEmpty(String beskrivning, String field, String errorCode) {
        if (beskrivning == null || beskrivning.isEmpty()) {
            addValidationError(field, errorCode);
        }
    }

    /**
     * Check if there are validation errors
     * 
     * @return {@link ValidationStatus.COMPLETE} if there are no errors, and {@link ValidationStatus.INCOMPLETE}
     *         otherwise
     */
    private ValidationStatus getValidationStatus() {
        if (validationResponse.getValidationErrors().isEmpty()) {
            return ValidationStatus.COMPLETE;
        }
        return ValidationStatus.INCOMPLETE;
    }

    /**
     * Create a ValidationMessage and add it to the {@link ValidateDraftResponseHolder}
     * 
     * @param field
     *            a String with the name of the field
     * @param msg
     *            a String with an error code for the front end implementation
     */
    private void addValidationError(String field, String msg) {
        ValidationMessage validationMesssage = new ValidationMessage();
        validationMesssage.setField(field);
        validationMesssage.setMessage(msg);

        validationResponse.addErrorMessage(validationMesssage);
    }
}
