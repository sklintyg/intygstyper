package se.inera.certificate.modules.ts_diabetes.validator.internal;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.certificate.modules.support.api.dto.ValidateDraftResponse;
import se.inera.certificate.modules.support.api.dto.ValidationMessage;
import se.inera.certificate.modules.ts_diabetes.model.internal.Bedomning;
import se.inera.certificate.modules.ts_diabetes.model.internal.Diabetes;
import se.inera.certificate.modules.ts_diabetes.model.internal.HoSPersonal;
import se.inera.certificate.modules.ts_diabetes.model.internal.Hypoglykemier;
import se.inera.certificate.modules.ts_diabetes.model.internal.IntygAvser;
import se.inera.certificate.modules.ts_diabetes.model.internal.Patient;
import se.inera.certificate.modules.ts_diabetes.model.internal.Syn;
import se.inera.certificate.modules.ts_diabetes.model.internal.Utlatande;
import se.inera.certificate.modules.ts_diabetes.model.internal.Vardkontakt;

/**
 * Class for validating drafts of the internal model.
 *
 * @author erik
 */
public class InternalValidatorInstance {

    private static final String POSTNUMMER_FORMAT = "\\d{3}\\s?\\d{2}";

    private static final Logger LOG = LoggerFactory.getLogger(InternalValidatorInstance.class);

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
            addValidationError("utlatande", "ts-diabetes.validation.utlatande.missing");

        } else {

            context = new ValidationContext(utlatande);
            validateBedomning(utlatande.getBedomning());
            validateDiabetes(utlatande.getDiabetes());
            validateHoSPersonal(utlatande.getSkapadAv());
            validateIntygAvser(utlatande.getIntygAvser());
            validateIdentitetStyrkt(utlatande.getVardkontakt());
            validateSyn(utlatande.getSyn());
            validateHypoglykemi(utlatande.getHypoglykemier());
            validatePatient(utlatande.getPatient());
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
            addValidationError("hypoglykemier", "ts-diabetes.validation.hypoglykemier.missing");
            return;
        }

        if (hypoglykemier.getKunskapOmAtgarder() == null) {
            addValidationError("hypoglykemier.kunskapOmAtgarder",
                    "ts-diabetes.validation.hypoglykemier.kunskap-om-atgarder.missing");
        }

        if (hypoglykemier.getTeckenNedsattHjarnfunktion() == null) {
            addValidationError("hypoglykemier.teckenNedsattHjarnfunktion",
                    "ts-diabetes.validation.hypoglykemier.tecken-nedsatt-hjarnfunktion.missing");
        }

        if (isTrue(hypoglykemier.getTeckenNedsattHjarnfunktion())) {
            if (hypoglykemier.getSaknarFormagaKannaVarningstecken() == null) {
                addValidationError("hypoglykemier.saknarFormagaKannaVarningstecken",
                        "ts-diabetes.validation.hypoglykemier.saknar-formaga-kanna-varningstecken.missing");
            }

            if (hypoglykemier.getAllvarligForekomst() == null) {
                addValidationError("hypoglykemier.allvarligForekomst",
                        "ts-diabetes.validation.hypoglykemier.allvarlig-forekomst.missing");
            }

            if (hypoglykemier.getAllvarligForekomstTrafiken() == null) {
                addValidationError("hypoglykemier.allvarligForekomstTrafiken",
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
                addValidationError("hypoglykemier.allvarligForekomstVakenTidObservationstid",
                        "ts-diabetes.validation.hypoglykemier.allvarlig-forekomst-vaken-tid.observationstid.missing");
            } else if (!isValidDate(hypoglykemier.getAllvarligForekomstVakenTidObservationstid(), "yyyy-MM-dd")) {
                addValidationError("hypoglykemier.allvarligForekomstVakenTidObservationstid",
                        "ts-diabetes.validation.hypoglykemier.allvarlig-forekomst-vaken-tid.observationstid.incorrect-date");
            }
        }

        if (context.isHogreBehorighetContext()) {
            if (hypoglykemier.getEgenkontrollBlodsocker() == null) {
                addValidationError("hypoglykemier.egenkontrollBlodsocker",
                        "ts-diabetes.validation.hypoglykemier.egenkontroll-blodsocker.missing");
            }

            if (hypoglykemier.getAllvarligForekomstVakenTid() == null) {
                addValidationError("hypoglykemier.allvarligForekomstVakenTid",
                        "ts-diabetes.validation.hypoglykemier.allvarlig-forekomst-vaken-tid.missing");
            }
        }

    }

    private void validateIdentitetStyrkt(Vardkontakt vardkontakt) {
        if (vardkontakt == null) {
            addValidationError("identitet", "ts-diabetes.validation.vardkontakt.missing");
            return;
        }
        if (vardkontakt.getIdkontroll() == null) {
            addValidationError("identitet", "ts-diabetes.validation.identitet.missing");
        }
    }

    private void validateBedomning(final Bedomning bedomning) {

        if (bedomning == null) {
            addValidationError("bedomning", "ts-diabetes.validation.bedomning.missing");
            return;
        }

        if (isFalse(bedomning.getKanInteTaStallning()) && bedomning.getKorkortstyp().isEmpty()) {
            addValidationError("bedomning", "ts-diabetes.validation.bedomning.must-choose-one");
        }

        if (context.isHogreBehorighetContext()) {
            if (bedomning.getLamplighetInnehaBehorighet() == null) {
                addValidationError("bedomning.lamplighetInnehaBehorighet",
                        "ts-diabetes.validation.bedomning.lamplighet-inneha-behorighet.missing");
            }
        }
    }

    private void validateDiabetes(final Diabetes diabetes) {

        if (diabetes == null) {
            addValidationError("diabetes", "ts-diabetes.validation.diabetes.missing");
            return;
        }

        if (diabetes.getDiabetestyp() == null) {
            addValidationError("diabetes.diabetesTyp", "ts-diabetes.validation.diabetes.diabetesTyp.missing");
        }

        if (diabetes.getObservationsperiod() == null) {
            addValidationError("diabetes.observationsperiod", "ts-diabetes.validation.diabetes.observationsperiod.missing");
        }

        boolean annanBehandling = diabetes.getAnnanBehandlingBeskrivning() != null
                && !diabetes.getAnnanBehandlingBeskrivning().isEmpty();
        if (!(isTrue(diabetes.getEndastKost()) || isTrue(diabetes.getTabletter()) || isTrue(diabetes.getInsulin()) || annanBehandling)) {
            addValidationError("diabetes", "ts-diabetes.validation.diabetes.behandling.missing");
        }

        if (isTrue(diabetes.getInsulin())) {
            assertDescriptionNotEmpty(diabetes.getInsulinBehandlingsperiod(), "diabetes.insulin",
                    "ts-diabetes.validation.diabetes.insulin.behandlingsperiod.missing");
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
            if (!skapadAv.getVardenhet().getPostnummer().matches(POSTNUMMER_FORMAT)) {
                addValidationError("vardenhet.postnummer", "ts-diabetes.validation.vardenhet.postnummer.incorrect-format");
            }
        }

        assertDescriptionNotEmpty(skapadAv.getVardenhet().getPostort(), "vardenhet.postort",
                "ts-diabetes.validation.vardenhet.postort.missing");
        assertDescriptionNotEmpty(skapadAv.getVardenhet().getTelefonnummer(), "vardenhet.telefonnummer",
                "ts-diabetes.validation.vardenhet.telefonnummer.missing");

    }

    private void validateIntygAvser(final IntygAvser intygAvser) {

        if (intygAvser == null) {
            addValidationError("intygAvser", "ts-diabetes.validation.intygAvser.missing");
            return;
        }

        if (intygAvser.getKorkortstyp().isEmpty()) {
            addValidationError("intygAvser", "ts-diabetes.validation.intygAvser.must-choose-one");
        }
    }

    private void validateSyn(final Syn syn) {

        if (syn == null) {
            return;
        }

        if (syn.getSeparatOgonlakarintyg() == null) {
            addValidationError("syn.separatOgonlakarintyg", "ts-diabetes.validation.syn.separat-ogonlakarintyg.missing");

        } else if (!syn.getSeparatOgonlakarintyg()) {

            if (syn.getSynfaltsprovningUtanAnmarkning() == null) {
                addValidationError("syn.provningUtanAnmarkning", "ts-diabetes.validation.syn.provning-utan-anmarkning.missing");
            }

            if (syn.getDiplopi() == null) {
                addValidationError("syn.diplopi", "ts-diabetes.validation.syn.diplopi.missing");
            }

            if (syn.getHoger() == null || syn.getHoger().getUtanKorrektion() == null) {
                addValidationError("syn.hoger.utanKorrektion", "ts-diabetes.validation.syn.hoger.utanKorrektion.missing");

            } else {
                if (syn.getHoger().getUtanKorrektion() < 0.0 || syn.getHoger().getUtanKorrektion() > 2.0) {
                    addValidationError("syn.hoger.utanKorrektion",
                            "ts-diabetes.validation.syn.hoger.utankorrektion.out-of-bounds");
                }

                if (syn.getHoger().getMedKorrektion() != null) {
                    if (syn.getHoger().getMedKorrektion() < 0.0 || syn.getHoger().getMedKorrektion() > 2.0) {
                        addValidationError("syn.hoger.medKorrektion",
                                "ts-diabetes.validation.syn.hoger.medKorrektion.out-of-bounds");
                    }
                }
            }

            if (syn.getVanster() == null || syn.getVanster().getUtanKorrektion() == null) {
                addValidationError("syn.vanster.utanKorrektion", "ts-diabetes.validation.syn.vanster.utankorrektion.missing");

            } else {

                if (syn.getVanster().getUtanKorrektion() < 0.0 || syn.getVanster().getUtanKorrektion() > 2.0) {
                    addValidationError("syn.vanster.utanKorrektion", "ts-diabetes.validation.syn.vanster.utankorrektion.missing");
                }

                if (syn.getVanster().getMedKorrektion() != null) {
                    if (syn.getVanster().getMedKorrektion() < 0.0 || syn.getVanster().getMedKorrektion() > 2.0) {
                        addValidationError("syn.vanster.medKorrektion",
                                "ts-diabetes.validation.syn.vanster.medkorrektion.out-of-bounds");
                    }
                }
            }

            if (syn.getBinokulart() == null || syn.getBinokulart().getUtanKorrektion() == null) {
                addValidationError("syn.binokulart.utanKorrektion",
                        "ts-diabetes.validation.syn.binokulart.utankorrektion.missing");

            } else {
                if (syn.getBinokulart().getUtanKorrektion() < 0.0 || syn.getBinokulart().getUtanKorrektion() > 2.0) {
                    addValidationError("syn.binokulart.utanKorrektion",
                            "ts-diabetes.validation.syn.binokulart.utankorrektion.out-of-bounds");
                }

                if (syn.getBinokulart().getMedKorrektion() != null) {
                    if (syn.getBinokulart().getMedKorrektion() < 0.0 || syn.getBinokulart().getMedKorrektion() > 2.0) {
                        addValidationError("syn.binokulart.medKorrektion",
                                "ts-diabetes.validation.syn.binokulart.medkorrektion.out-of-bounds");
                    }
                }
            }
        }
    }

    /**
     * Make sure a string representing a date conforms to the desired format.
     *
     * @param dateString the date
     * @param dateFormat the format
     * @return true if it does, false otherwise
     */
    private boolean isValidDate(String dateString, String dateFormat) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(dateFormat);
        try {
            LocalDate.parse(dateString, formatter);
        } catch (Exception e) {
            return false;
        }
        return true;
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
            addValidationError(field, errorCode);
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
    private se.inera.certificate.modules.support.api.dto.ValidationStatus getValidationStatus() {
        return (validationMessages.isEmpty()) ? se.inera.certificate.modules.support.api.dto.ValidationStatus.VALID
                : se.inera.certificate.modules.support.api.dto.ValidationStatus.INVALID;
    }

    /**
     * Create a ValidationMessage and add it to the {@link ValidateDraftResponseHolder}.
     *
     * @param field a String with the name of the field
     * @param msg   a String with an error code for the front end implementation
     */
    private void addValidationError(String field, String msg) {
        validationMessages.add(new ValidationMessage(field, msg));
        LOG.debug(field + " " + msg);
    }

    /**
     * Since the validator assertions doesn't throw exceptions on assertion failure, they instead return an assertion
     * result. This might be used to implement conditional logic based on if an assertion {@link #failed()} or was
     * {@link #success()}ful.
     */
    protected static enum AssertionResult {
        SUCCESS(true), FAILURE(false);

        private AssertionResult(boolean assertSuccessfull) {
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
