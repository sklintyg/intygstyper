package se.inera.intyg.intygstyper.ts_diabetes.validator.internal;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessage;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationMessageType;
import se.inera.intyg.common.support.validate.StringValidator;
import se.inera.intyg.intygstyper.ts_diabetes.model.internal.*;

/**
 * Class for validating drafts of the internal model.
 *
 * @author erik
 */
public class InternalValidatorInstance {

    //private static final String AR_FORMAT = "[1-2][0-9]{3,3}(-((0[1-9])|(1[0-2]))(-((0[1-9])|([1-2][0-9])|(3[0-1])))?)?";

    private static final Logger LOG = LoggerFactory.getLogger(InternalValidatorInstance.class);

    private static final StringValidator STRING_VALIDATOR = new StringValidator();

    private List<ValidationMessage> validationMessages;

    private ValidationContext context;

    public InternalValidatorInstance() {
        validationMessages = new ArrayList<>();
    }

    /**
     * Validates an internal draft of an {@link Utlatande} (this means the object being validated is not necessarily
     * complete).
     *
     * @param utlatande an internal {@link Utlatande}
     * @return a {@link ValidateDraftResponseHolder} with a status and a list of validationErrors
     */
    public ValidateDraftResponse validate(Utlatande utlatande) {

        if (utlatande == null) {
            addValidationError("utlatande", ValidationMessageType.OTHER, "ts-diabetes.validation.utlatande.missing");

        } else {

            context = new ValidationContext(utlatande);
            validatePatient(utlatande.getGrundData().getPatient());
            validateIntygAvser(utlatande.getIntygAvser());
            validateIdentitetStyrkt(utlatande.getVardkontakt());
            validateDiabetes(utlatande.getDiabetes());
            validateHypoglykemi(utlatande.getHypoglykemier());
            validateSyn(utlatande.getSyn());
            validateBedomning(utlatande.getBedomning());
            validateHoSPersonal(utlatande.getGrundData().getSkapadAv());
        }

        ValidateDraftResponse response = new ValidateDraftResponse(getValidationStatus(), validationMessages);

        return response;
    }

    private void validatePatient(Patient patient) {
        if (patient == null) {
            return;
        }
        assertDescriptionNotEmpty(patient.getPostadress(), "patient.postadress", "ts-diabetes.validation.patient.postadress.missing");
        assertDescriptionNotEmpty(patient.getPostnummer(), "patient.postnummer", "ts-diabetes.validation.patient.postnummer.missing");
        assertDescriptionNotEmpty(patient.getPostort(), "patient.postort", "ts-diabetes.validation.patient.postort.missing");
    }

    private void validateHypoglykemi(Hypoglykemier hypoglykemier) {
        if (hypoglykemier == null) {
            addValidationError("hypoglykemier", ValidationMessageType.EMPTY, "ts-diabetes.validation.hypoglykemier.missing");
            return;
        }

        if (hypoglykemier.getKunskapOmAtgarder() == null) {
            addValidationError("hypoglykemier.kunskapOmAtgarder", ValidationMessageType.EMPTY,
                    "ts-diabetes.validation.hypoglykemier.kunskap-om-atgarder.missing");
        }

        if (hypoglykemier.getTeckenNedsattHjarnfunktion() == null) {
            addValidationError("hypoglykemier.teckenNedsattHjarnfunktion", ValidationMessageType.EMPTY,
                    "ts-diabetes.validation.hypoglykemier.tecken-nedsatt-hjarnfunktion.missing");
        }

        if (isTrue(hypoglykemier.getTeckenNedsattHjarnfunktion())) {
            if (hypoglykemier.getSaknarFormagaKannaVarningstecken() == null) {
                addValidationError("hypoglykemier.saknarFormagaKannaVarningstecken", ValidationMessageType.EMPTY,
                        "ts-diabetes.validation.hypoglykemier.saknar-formaga-kanna-varningstecken.missing");
            }

            if (hypoglykemier.getAllvarligForekomst() == null) {
                addValidationError("hypoglykemier.allvarligForekomst", ValidationMessageType.EMPTY,
                        "ts-diabetes.validation.hypoglykemier.allvarlig-forekomst.missing");
            }

            if (hypoglykemier.getAllvarligForekomstTrafiken() == null) {
                addValidationError("hypoglykemier.allvarligForekomstTrafiken", ValidationMessageType.EMPTY,
                        "ts-diabetes.validation.hypoglykemier.allvarlig-forekomst-trafiken.missing");
            }
        }

        if (isTrue(hypoglykemier.getAllvarligForekomst())) {
            assertDescriptionNotEmpty(hypoglykemier.getAllvarligForekomstBeskrivning(),
                    "hypoglykemier.allvarligForekomstBeskrivning",
                    "ts-diabetes.validation.hypoglykemier.allvarlig-forekomst.beskrivning.missing");
        }

        if (isTrue(hypoglykemier.getAllvarligForekomstTrafiken())) {
            assertDescriptionNotEmpty(hypoglykemier.getAllvarligForekomstTrafikBeskrivning(),
                    "hypoglykemier.allvarligForekomstTrafikBeskrivning",
                    "ts-diabetes.validation.hypoglykemier.allvarlig-forekomst-trafiken.beskrivning.missing");
        }

        if (isTrue(hypoglykemier.getAllvarligForekomstVakenTid())) {
            if (hypoglykemier.getAllvarligForekomstVakenTidObservationstid() == null) {
                addValidationError("hypoglykemier.allvarligForekomstVakenTidObservationstid", ValidationMessageType.EMPTY,
                        "ts-diabetes.validation.hypoglykemier.allvarlig-forekomst-vaken-tid.observationstid.missing");
            } else if (hypoglykemier.getAllvarligForekomstVakenTidObservationstid().invalidOrInFuture()) {
                addValidationError("hypoglykemier.allvarligForekomstVakenTidObservationstid", ValidationMessageType.INVALID_FORMAT,
                        "ts-diabetes.validation.hypoglykemier.allvarlig-forekomst-vaken-tid.observationstid.incorrect-date");
            }
        }

        if (context.isHogreBehorighetContext()) {
            if (hypoglykemier.getEgenkontrollBlodsocker() == null) {
                addValidationError("hypoglykemier.egenkontrollBlodsocker", ValidationMessageType.EMPTY,
                        "ts-diabetes.validation.hypoglykemier.egenkontroll-blodsocker.missing");
            }

            if (hypoglykemier.getAllvarligForekomstVakenTid() == null) {
                addValidationError("hypoglykemier.allvarligForekomstVakenTid", ValidationMessageType.EMPTY,
                        "ts-diabetes.validation.hypoglykemier.allvarlig-forekomst-vaken-tid.missing");
            }
        }

    }

    private void validateIdentitetStyrkt(Vardkontakt vardkontakt) {
        if (vardkontakt == null) {
            addValidationError("identitet", ValidationMessageType.EMPTY, "ts-diabetes.validation.vardkontakt.missing");
            return;
        }
        if (vardkontakt.getIdkontroll() == null) {
            addValidationError("identitet", ValidationMessageType.EMPTY, "ts-diabetes.validation.identitet.missing");
        }
    }

    private void validateBedomning(final Bedomning bedomning) {

        if (bedomning == null) {
            addValidationError("bedomning", ValidationMessageType.EMPTY, "ts-diabetes.validation.bedomning.missing");
            return;
        }
        if (bedomning.getKorkortstyp().isEmpty() && (bedomning.getKanInteTaStallning() == null || isFalse(bedomning.getKanInteTaStallning()))) {
            addValidationError("bedomning", ValidationMessageType.EMPTY, "ts-diabetes.validation.bedomning.must-choose-one");
        }

        if (context.isHogreBehorighetContext()) {
            if (bedomning.getLamplighetInnehaBehorighet() == null) {
                addValidationError("bedomning.lamplighetInnehaBehorighet", ValidationMessageType.EMPTY,
                        "ts-diabetes.validation.bedomning.lamplighet-inneha-behorighet.missing");
            }
        }
    }

    private void validateDiabetes(final Diabetes diabetes) {

        if (diabetes == null) {
            addValidationError("diabetes", ValidationMessageType.EMPTY, "ts-diabetes.validation.diabetes.missing");
            return;
        }

        if (diabetes.getObservationsperiod() == null) {
            addValidationError("diabetes.observationsperiod", ValidationMessageType.EMPTY, "ts-diabetes.validation.diabetes.observationsperiod.missing");
        } else if (!STRING_VALIDATOR.validateStringIsYear(diabetes.getObservationsperiod())) {
            addValidationError("diabetes.observationsperiod", ValidationMessageType.INVALID_FORMAT, "ts-diabetes.validation.diabetes.observationsperiod.incorrect-format");
        }

        if (diabetes.getDiabetestyp() == null) {
            addValidationError("diabetes.diabetesTyp", ValidationMessageType.EMPTY, "ts-diabetes.validation.diabetes.diabetesTyp.missing");
        }

        boolean annanBehandling = diabetes.getAnnanBehandlingBeskrivning() != null
                && !diabetes.getAnnanBehandlingBeskrivning().isEmpty();
        if (!(isTrue(diabetes.getEndastKost()) || isTrue(diabetes.getTabletter()) || isTrue(diabetes.getInsulin()) || annanBehandling)) {
            addValidationError("diabetes", ValidationMessageType.EMPTY, "ts-diabetes.validation.diabetes.behandling.missing");
        }

        if (isTrue(diabetes.getInsulin())) {
            if (diabetes.getInsulinBehandlingsperiod() == null) {
                addValidationError("diabetes.insulin", ValidationMessageType.EMPTY, "ts-diabetes.validation.diabetes.insulin.behandlingsperiod.missing");
            } else if (!STRING_VALIDATOR.validateStringIsYear(diabetes.getInsulinBehandlingsperiod())) {
                addValidationError("diabetes.insulin", ValidationMessageType.INVALID_FORMAT, "ts-diabetes.validation.diabetes.insulin.behandlingsperiod.incorrect-format");
            }
        }
    }

    private void validateHoSPersonal(final HoSPersonal skapadAv) {
        if (skapadAv == null) {
            LOG.debug("No HoSPersonal found");
            return;
        }
        assertDescriptionNotEmpty(skapadAv.getVardenhet().getPostadress(), "vardenhet.postadress",
                "ts-diabetes.validation.vardenhet.postadress.missing");
        if (assertDescriptionNotEmpty(skapadAv.getVardenhet().getPostnummer(), "vardenhet.postnummer",
                "ts-diabetes.validation.vardenhet.postnummer.missing").success()) {
            if (!STRING_VALIDATOR.validateStringAsPostalCode(skapadAv.getVardenhet().getPostnummer())) {
                addValidationError("vardenhet.postnummer", ValidationMessageType.INVALID_FORMAT, "ts-diabetes.validation.vardenhet.postnummer.incorrect-format");
            }
        }

        assertDescriptionNotEmpty(skapadAv.getVardenhet().getPostort(), "vardenhet.postort",
                "ts-diabetes.validation.vardenhet.postort.missing");
        assertDescriptionNotEmpty(skapadAv.getVardenhet().getTelefonnummer(), "vardenhet.telefonnummer",
                "ts-diabetes.validation.vardenhet.telefonnummer.missing");

    }

    private void validateIntygAvser(final IntygAvser intygAvser) {

        if (intygAvser == null) {
            addValidationError("intygAvser", ValidationMessageType.EMPTY, "ts-diabetes.validation.intygAvser.missing");
            return;
        }

        if (intygAvser.getKorkortstyp().isEmpty()) {
            addValidationError("intygAvser", ValidationMessageType.EMPTY, "ts-diabetes.validation.intygAvser.must-choose-one");
        }
    }

    private void validateSyn(final Syn syn) {

        if (syn == null) {
            return;
        }

        if (syn.getSeparatOgonlakarintyg() == null) {
            addValidationError("syn.separatOgonlakarintyg", ValidationMessageType.EMPTY, "ts-diabetes.validation.syn.separat-ogonlakarintyg.missing");

        } else if (!syn.getSeparatOgonlakarintyg()) {

            if (syn.getSynfaltsprovningUtanAnmarkning() == null) {
                addValidationError("syn.provningUtanAnmarkning", ValidationMessageType.EMPTY, "ts-diabetes.validation.syn.provning-utan-anmarkning.missing");
            }

            if (syn.getHoger() == null || syn.getHoger().getUtanKorrektion() == null) {
                addValidationError("syn.hoger.utanKorrektion", ValidationMessageType.EMPTY, "ts-diabetes.validation.syn.hoger.utanKorrektion.missing");

            } else {
                if (syn.getHoger().getUtanKorrektion() < 0.0 || syn.getHoger().getUtanKorrektion() > 2.0) {
                    addValidationError("syn.hoger.utanKorrektion", ValidationMessageType.INVALID_FORMAT,
                            "ts-diabetes.validation.syn.hoger.utankorrektion.out-of-bounds");
                }

                if (syn.getHoger().getMedKorrektion() != null) {
                    if (syn.getHoger().getMedKorrektion() < 0.0 || syn.getHoger().getMedKorrektion() > 2.0) {
                        addValidationError("syn.hoger.medKorrektion", ValidationMessageType.INVALID_FORMAT,
                                "ts-diabetes.validation.syn.hoger.medKorrektion.out-of-bounds");
                    }
                }
            }

            if (syn.getVanster() == null || syn.getVanster().getUtanKorrektion() == null) {
                addValidationError("syn.vanster.utanKorrektion", ValidationMessageType.EMPTY, "ts-diabetes.validation.syn.vanster.utankorrektion.missing");

            } else {

                if (syn.getVanster().getUtanKorrektion() < 0.0 || syn.getVanster().getUtanKorrektion() > 2.0) {
                    addValidationError("syn.vanster.utanKorrektion", ValidationMessageType.EMPTY, "ts-diabetes.validation.syn.vanster.utankorrektion.missing");
                }

                if (syn.getVanster().getMedKorrektion() != null) {
                    if (syn.getVanster().getMedKorrektion() < 0.0 || syn.getVanster().getMedKorrektion() > 2.0) {
                        addValidationError("syn.vanster.medKorrektion", ValidationMessageType.INVALID_FORMAT,
                                "ts-diabetes.validation.syn.vanster.medkorrektion.out-of-bounds");
                    }
                }
            }

            if (syn.getBinokulart() == null || syn.getBinokulart().getUtanKorrektion() == null) {
                addValidationError("syn.binokulart.utanKorrektion", ValidationMessageType.EMPTY,
                        "ts-diabetes.validation.syn.binokulart.utankorrektion.missing");

            } else {
                if (syn.getBinokulart().getUtanKorrektion() < 0.0 || syn.getBinokulart().getUtanKorrektion() > 2.0) {
                    addValidationError("syn.binokulart.utanKorrektion", ValidationMessageType.INVALID_FORMAT,
                            "ts-diabetes.validation.syn.binokulart.utankorrektion.out-of-bounds");
                }

                if (syn.getBinokulart().getMedKorrektion() != null) {
                    if (syn.getBinokulart().getMedKorrektion() < 0.0 || syn.getBinokulart().getMedKorrektion() > 2.0) {
                        addValidationError("syn.binokulart.medKorrektion", ValidationMessageType.INVALID_FORMAT,
                                "ts-diabetes.validation.syn.binokulart.medkorrektion.out-of-bounds");
                    }
                }
            }

            if (syn.getDiplopi() == null) {
                addValidationError("syn.diplopi", ValidationMessageType.EMPTY, "ts-diabetes.validation.syn.diplopi.missing");
            }
        }
    }

    private boolean isTrue(Boolean bool) {
        return bool != null && bool;
    }

    private boolean isFalse(Boolean bool) {
        return bool != null && !bool;
    }

    /**
     * Check for null or empty String, if so add a validation error for field with errorCode.
     *
     * @param beskrivning the String to check
     * @param field       the target field in the model
     * @param errorCode   the errorCode to log in validation errors
     */
    private AssertionResult assertDescriptionNotEmpty(String beskrivning, String field, String errorCode) {
        if (beskrivning == null || beskrivning.isEmpty()) {
            addValidationError(field, ValidationMessageType.EMPTY, errorCode);
            LOG.debug(field + " " + errorCode);
            return AssertionResult.FAILURE;
        }
        return AssertionResult.SUCCESS;
    }

    /**
     * Check if there are validation errors.
     *
     * @return {@link ValidationStatus.COMPLETE} if there are no errors, and {@link ValidationStatus.INCOMPLETE}
     * otherwise
     */
    private se.inera.intyg.common.support.modules.support.api.dto.ValidationStatus getValidationStatus() {
        return (validationMessages.isEmpty()) ? se.inera.intyg.common.support.modules.support.api.dto.ValidationStatus.VALID
                : se.inera.intyg.common.support.modules.support.api.dto.ValidationStatus.INVALID;
    }

    /**
     * Create a ValidationMessage and add it to the {@link ValidateDraftResponseHolder}.
     *
     * @param field a String with the name of the field
     * @param msg   a String with an error code for the front end implementation
     */
    private void addValidationError(String field, ValidationMessageType type, String msg) {
        validationMessages.add(new ValidationMessage(field, type, msg));
        LOG.debug(field + " " + msg);
    }

    /**
     * Since the validator assertions doesn't throw exceptions on assertion failure, they instead return an assertion
     * result. This might be used to implement conditional logic based on if an assertion {@link #failed()} or was
     * {@link #success()}ful.
     */
    protected enum AssertionResult {
        SUCCESS(true), FAILURE(false);

        AssertionResult(boolean assertSuccessfull) {
            this.assertSuccessful = assertSuccessfull;
        }

        private final boolean assertSuccessful;

        public boolean failed() {
            return !assertSuccessful;
        }

        public boolean success() {
            return assertSuccessful;
        }
    }
}
