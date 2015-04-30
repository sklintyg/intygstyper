package se.inera.certificate.modules.ts_bas.validator.internal;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.certificate.model.common.internal.HoSPersonal;
import se.inera.certificate.model.common.internal.Patient;
import se.inera.certificate.modules.support.api.dto.ValidateDraftResponse;
import se.inera.certificate.modules.support.api.dto.ValidationMessage;
import se.inera.certificate.modules.support.api.dto.ValidationMessageType;
import se.inera.certificate.modules.support.api.dto.ValidationStatus;
import se.inera.certificate.modules.ts_bas.model.codes.DiabetesKod;
import se.inera.certificate.modules.ts_bas.model.internal.Bedomning;
import se.inera.certificate.modules.ts_bas.model.internal.Diabetes;
import se.inera.certificate.modules.ts_bas.model.internal.Funktionsnedsattning;
import se.inera.certificate.modules.ts_bas.model.internal.HjartKarl;
import se.inera.certificate.modules.ts_bas.model.internal.HorselBalans;
import se.inera.certificate.modules.ts_bas.model.internal.IntygAvser;
import se.inera.certificate.modules.ts_bas.model.internal.Kognitivt;
import se.inera.certificate.modules.ts_bas.model.internal.Medicinering;
import se.inera.certificate.modules.ts_bas.model.internal.Medvetandestorning;
import se.inera.certificate.modules.ts_bas.model.internal.NarkotikaLakemedel;
import se.inera.certificate.modules.ts_bas.model.internal.Neurologi;
import se.inera.certificate.modules.ts_bas.model.internal.Njurar;
import se.inera.certificate.modules.ts_bas.model.internal.Psykiskt;
import se.inera.certificate.modules.ts_bas.model.internal.Sjukhusvard;
import se.inera.certificate.modules.ts_bas.model.internal.SomnVakenhet;
import se.inera.certificate.modules.ts_bas.model.internal.Syn;
import se.inera.certificate.modules.ts_bas.model.internal.Utlatande;
import se.inera.certificate.modules.ts_bas.model.internal.Utvecklingsstorning;
import se.inera.certificate.modules.ts_bas.model.internal.Vardkontakt;
import se.inera.certificate.validate.StringValidator;

/**
 * Class for validating drafts of the internal model.
 *
 * @author erik
 */
public class InternalValidatorInstance {

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
     * @param utlatande
     *            an internal {@link Utlatande}
     * @return a {@link ValidateDraftResponseHolder} with a status and a list of validationErrors
     */
    public ValidateDraftResponse validate(Utlatande utlatande) {

        if (utlatande == null) {
            addValidationError("utlatande", ValidationMessageType.EMPTY, "ts-bas.validation.utlatande.missing");

        } else {

            context = new ValidationContext(utlatande);

            // OBS! Utökas formuläret i framtiden, lägg in validering i rätt ordning nedan.
            validatePatient(utlatande.getGrundData().getPatient());
            validateIntygAvser(utlatande.getIntygAvser());
            validateIdentitetStyrkt(utlatande.getVardkontakt());
            validateSyn(utlatande.getSyn()); // 1.
            validateHorselBalans(utlatande.getHorselBalans());  // 2.
            validateFunktionsnedsattning(utlatande.getFunktionsnedsattning()); // 3.
            validateHjartKarl(utlatande.getHjartKarl()); // 4.
            validateDiabetes(utlatande.getDiabetes());  // 5.
            validateNeurologi(utlatande.getNeurologi()); // 6.
            validateMedvetandestorning(utlatande.getMedvetandestorning());  // 7.
            validateNjurar(utlatande.getNjurar()); // 8.
            validateKognitivt(utlatande.getKognitivt()); // 9.
            validateSomnVakenhet(utlatande.getSomnVakenhet()); // 10.
            validateNarkotikaLakemedel(utlatande.getNarkotikaLakemedel()); // 11.
            validatePsykiskt(utlatande.getPsykiskt()); // 12.
            validateUtvecklingsstorning(utlatande.getUtvecklingsstorning()); // 13.
            validateSjukhusvard(utlatande.getSjukhusvard()); // 14.
            validateMedicinering(utlatande.getMedicinering()); // 15.
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
        assertDescriptionNotEmpty(patient.getPostadress(), "patientadress.postadress", "ts-bas.validation.patient.postadress.missing");
        assertDescriptionNotEmpty(patient.getPostnummer(), "patientadress.postnummer", "ts-bas.validation.patient.postnummer.missing");
        assertDescriptionNotEmpty(patient.getPostort(), "patientadress.postort", "ts-bas.validation.patient.postort.missing");
    }

    private void validateIdentitetStyrkt(Vardkontakt vardkontakt) {
        if (vardkontakt == null) {
            addValidationError("vardkontakt", ValidationMessageType.EMPTY, "ts-bas.validation.vardkontakt.missing");
            return;
        }
        if (vardkontakt.getIdkontroll() == null) {
            addValidationError("identitet", ValidationMessageType.EMPTY, "ts-bas.validation.identitet.missing");
        }
    }

    private void validateUtvecklingsstorning(Utvecklingsstorning utvecklingsstorning) {
        if (utvecklingsstorning == null) {
            addValidationError("utvecklingsstorning", ValidationMessageType.EMPTY, "ts-bas.validation.utvecklingsstorning.missing");
            return;
        }
        if (utvecklingsstorning.getHarSyndrom() == null) {
            addValidationError("utvecklingsstorning.harSyndrom", ValidationMessageType.EMPTY, "ts-bas.validation.utvecklingsstorning.harsyndrom.missing");
        }
        if (utvecklingsstorning.getPsykiskUtvecklingsstorning() == null) {
            addValidationError("utvecklingsstorning.psykiskUtvecklingsstorning", ValidationMessageType.EMPTY,
                    "ts-bas.validation.utvecklingsstorning.psykiskutvecklingsstorning.missing");
        }

    }

    private void validatePsykiskt(Psykiskt psykiskt) {
        if (psykiskt == null) {
            addValidationError("psykiskt", ValidationMessageType.EMPTY, "ts-bas.validation.psykiskt.missing");
            return;
        }

        if (psykiskt.getPsykiskSjukdom() == null) {
            addValidationError("psykiskt", ValidationMessageType.EMPTY, "ts-bas.validation.psykiskt.psykisksjukdom.missing");
        }
    }

    private void validateSomnVakenhet(SomnVakenhet somnVakenhet) {
        if (somnVakenhet == null) {
            addValidationError("somnVakenhet", ValidationMessageType.EMPTY, "ts-bas.validation.somnvakenhet.missing");
            return;
        }
        if (somnVakenhet.getTeckenSomnstorningar() == null) {
            addValidationError("somnVakenhet.teckenSomnstorningar", ValidationMessageType.EMPTY,
                    "ts-bas.validation.somnvakenhet.teckensomnstorningar.missing");
        }
    }

    private void validateNjurar(Njurar njurar) {
        if (njurar == null) {
            addValidationError("njurar", ValidationMessageType.EMPTY, "ts-bas.validation.njurar.missing");
            return;
        }
        if (njurar.getNedsattNjurfunktion() == null) {
            addValidationError("njurar.nedsattNjurfunktion", ValidationMessageType.EMPTY, "ts-bas.validation.njurar.nedsattnjurfunktion.missing");
        }
    }

    private void validateNeurologi(Neurologi neurologi) {
        if (neurologi == null) {
            addValidationError("neurologi", ValidationMessageType.EMPTY, "ts-bas.validation.neurologi.missing");
            return;
        }
        if (neurologi.getNeurologiskSjukdom() == null) {
            addValidationError("neurologi.neurologiskSjukdom", ValidationMessageType.EMPTY, "ts-bas.validation.neurologi.neurologisksjukdom.missing");
        }
    }

    private void validateSjukhusvard(Sjukhusvard sjukhusvard) {

        if (sjukhusvard == null) {
            addValidationError("sjukhusvard", ValidationMessageType.EMPTY, "ts-bas.validation.sjukhusvard.missing");
            return;
        }

        if (sjukhusvard.getSjukhusEllerLakarkontakt() == null) {
            addValidationError("sjukhusvard.sjukhusEllerLakarkontakt", ValidationMessageType.EMPTY,
                    "ts-bas.validation.sjukhusvard.sjukhusEllerLakarkontakt.missing");
            return;

        }

        if (sjukhusvard.getSjukhusEllerLakarkontakt()) {
            assertDescriptionNotEmpty(sjukhusvard.getAnledning(), "sjukhusvard.anledning",
                    "ts-bas.validation.sjukhusvard.anledning.missing");
            assertDescriptionNotEmpty(sjukhusvard.getTidpunkt(), "sjukhusvard.tidpunkt",
                    "ts-bas.validation.sjukhusvard.tidpunkt.missing");
            assertDescriptionNotEmpty(sjukhusvard.getVardinrattning(), "sjukhusvard.vardinrattning",
                    "ts-bas.validation.sjukhusvard.vardinrattning.missing");
        }
    }

    private void validateBedomning(final Bedomning bedomning) {

        if (bedomning == null) {
            addValidationError("bedomning", ValidationMessageType.EMPTY, "ts-bas.validation.bedomning.missing");
            return;
        }

        if ((bedomning.getKanInteTaStallning() == null || !bedomning.getKanInteTaStallning()) && bedomning.getKorkortstyp().isEmpty()) {
            addValidationError("bedomning", ValidationMessageType.EMPTY, "ts-bas.validation.bedomning.must-choose-one");
        }
    }

    private void validateDiabetes(final Diabetes diabetes) {

        if (diabetes == null) {
            addValidationError("diabetes", ValidationMessageType.EMPTY, "ts-bas.validation.diabetes.missing");
            return;
        }

        if (diabetes.getHarDiabetes() == null) {
            addValidationError("diabetes.harDiabetes", ValidationMessageType.EMPTY, "ts-bas.validation.diabetes.harDiabetes.missing");
            return;
        }
        if (diabetes.getHarDiabetes()) {

            if (diabetes.getDiabetesTyp() == null) {
                addValidationError("diabetes.diabetesTyp", ValidationMessageType.EMPTY, "ts-bas.validation.diabetes.diabetesTyp.missing");

            } else if (diabetes.getDiabetesTyp().equals(DiabetesKod.DIABETES_TYP_2.name())) {
                if (diabetes.getInsulin() == null && diabetes.getKost() == null && diabetes.getTabletter() == null) {
                    addValidationError("diabetes.diabetesTyp", ValidationMessageType.EMPTY, "ts-bas.validation.diabetes.diabetesTyp.must-choose-one");
                }
            }
        }
    }

    private void validateFunktionsnedsattning(final Funktionsnedsattning funktionsnedsattning) {

        if (funktionsnedsattning == null) {
            addValidationError("funktionsnedsattning", ValidationMessageType.EMPTY, "ts-bas.validation.funktionsnedsattning.missing");
            return;
        }

        if (funktionsnedsattning.getFunktionsnedsattning() == null) {
            addValidationError("funktionsnedsattning.funktionsnedsattning", ValidationMessageType.EMPTY,
                    "ts-bas.validation.funktionsnedsattning.funktionsnedsattning.missing");
            return;

        } else if (funktionsnedsattning.getFunktionsnedsattning()) {
            assertDescriptionNotEmpty(funktionsnedsattning.getBeskrivning(), "funktionsnedsattning.beskrivning",
                    "ts-bas.validation.funktionsnedsattning.beskrivning.missing");
        }

        if (context.isPersontransportContext()) {
            if (funktionsnedsattning.getOtillrackligRorelseformaga() == null) {
                addValidationError("funktionsnedsattning.otillrackligRorelseformaga", ValidationMessageType.EMPTY,
                        "ts-bas.validation.funktionsnedsattning.otillrackligrorelseformaga.missing");
            }
        }
    }

    private void validateHjartKarl(final HjartKarl hjartKarl) {

        if (hjartKarl == null) {
            addValidationError("hjartKarl", ValidationMessageType.EMPTY, "ts-bas.validation.hjartKarl.missing");
            return;
        }

        if (hjartKarl.getHjartKarlSjukdom() == null) {
            addValidationError("hjartKarl.hjartKarlSjukdom", ValidationMessageType.EMPTY, "ts-bas.validation.hjartKarl.hjartkarlsjukdom.missing");
        }

        if (hjartKarl.getHjarnskadaEfterTrauma() == null) {
            addValidationError("hjartKarl.hjarnskadaEfterTrauma", ValidationMessageType.EMPTY, "ts-bas.validation.hjartkarl.hjarnskadaeftertrauma.missing");
        }

        if (hjartKarl.getRiskfaktorerStroke() == null) {
            addValidationError("hjartKarl.riskfaktorerStroke", ValidationMessageType.EMPTY, "ts-bas.validation.hjartkarl.riskfaktorerStroke.missing");

        } else if (hjartKarl.getRiskfaktorerStroke()) {
            assertDescriptionNotEmpty(hjartKarl.getBeskrivningRiskfaktorer(), "hjartKarl.beskrivningRiskfaktorer",
                    "ts-bas.validation.hjartkarl.beskrivningriskfaktorer.missing");
        }
    }

    private void validateHorselBalans(final HorselBalans horselBalans) {

        if (horselBalans == null) {
            addValidationError("horselBalans", ValidationMessageType.EMPTY, "ts-bas.validation.horselBalans.missing");
            return;
        }

        if (horselBalans.getBalansrubbningar() == null) {
            addValidationError("horselBalans.balansrubbningar", ValidationMessageType.EMPTY, "ts-bas.validation.horselBalans.balansrubbningar.missing");
        }

        if (context.isPersontransportContext()) {
            if (horselBalans.getSvartUppfattaSamtal4Meter() == null) {
                addValidationError("horselBalans.svartUpfattaSamtal4Meter", ValidationMessageType.EMPTY, "ts-bas.validation.horselbalans.uppfattasamtal4meter.missing");
            }
        }
    }

    private void validateHoSPersonal(final HoSPersonal skapadAv) {
        if (skapadAv == null) {
            LOG.debug("No HoSPersonal found");
            return;
        }
        assertDescriptionNotEmpty(skapadAv.getVardenhet().getPostadress(), "vardenhet.postadress",
                "ts-bas.validation.vardenhet.postadress.missing");
        if (assertDescriptionNotEmpty(skapadAv.getVardenhet().getPostnummer(), "vardenhet.postnummer",
                "ts-bas.validation.vardenhet.postnummer.missing").success()) {
            if (!STRING_VALIDATOR.validateStringAsPostalCode(skapadAv.getVardenhet().getPostnummer())) {
                addValidationError("vardenhet.postnummer", ValidationMessageType.INVALID_FORMAT, "ts-bas.validation.vardenhet.postnummer.incorrect-format");
            }
        }

        assertDescriptionNotEmpty(skapadAv.getVardenhet().getPostort(), "vardenhet.postort",
                "ts-bas.validation.vardenhet.postort.missing");
        assertDescriptionNotEmpty(skapadAv.getVardenhet().getTelefonnummer(), "vardenhet.telefonnummer",
                "ts-bas.validation.vardenhet.telefonnummer.missing");

    }

    private void validateIntygAvser(final IntygAvser intygAvser) {

        if (intygAvser == null) {
            addValidationError("intygAvser", ValidationMessageType.EMPTY, "ts-bas.validation.intygAvser.missing");
            return;
        }

        if (intygAvser.getKorkortstyp().isEmpty()) {
            addValidationError("intygAvser", ValidationMessageType.EMPTY, "ts-bas.validation.intygAvser.must-choose-one");
        }
    }

    private void validateKognitivt(final Kognitivt kognitivt) {

        if (kognitivt == null) {
            addValidationError("kognitivt", ValidationMessageType.EMPTY, "ts-bas.validation.kognitivt.missing");
            return;
        }

        if (kognitivt.getSviktandeKognitivFunktion() == null) {
            addValidationError("kognitivt.sviktandeKognitivFunktion", ValidationMessageType.EMPTY,
                    "ts-bas.validation.kognitivt.sviktandeKognitivFunktion.missing");
        }
    }

    private void validateMedicinering(final Medicinering medicinering) {

        if (medicinering == null) {
            addValidationError("medicinering", ValidationMessageType.EMPTY, "ts-bas.validation.medicinering.missing");
            return;
        }

        if (medicinering.getStadigvarandeMedicinering() == null) {
            addValidationError("medicinering.stadigvarandeMedicinering", ValidationMessageType.EMPTY,
                    "ts-bas.validation.medicinering.stadigvarandeMedicinering.missing");
        } else if (medicinering.getStadigvarandeMedicinering()) {
            assertDescriptionNotEmpty(medicinering.getBeskrivning(), "medicinering.beskrivning",
                    "ts-bas.validation.medicinering.beskrivning.missing");
        }
    }

    private void validateNarkotikaLakemedel(final NarkotikaLakemedel narkotikaLakemedel) {

        if (narkotikaLakemedel == null) {
            addValidationError("narkotikaLakemedel", ValidationMessageType.EMPTY, "ts-bas.validation.narkotikaLakemedel.missing");
            return;
        }

        if (narkotikaLakemedel.getForemalForVardinsats() == null) {
            addValidationError("narkotikaLakemedel.vardinsats", ValidationMessageType.EMPTY, "ts-bas.validation.narkotikaLakemedel.vardinsats-bas.missing");
        }

        if (narkotikaLakemedel.getTeckenMissbruk() == null) {
            addValidationError("narkotikaLakemedel.teckenMissbruk", ValidationMessageType.EMPTY, "ts-bas.validation.narkotikaLakemedel.teckenmissbruk.missing");

        } else if (narkotikaLakemedel.getTeckenMissbruk()) {
            if (narkotikaLakemedel.getProvtagningBehovs() == null) {
                addValidationError("narkotikaLakemedel.provtagningBehovs", ValidationMessageType.EMPTY, "ts-bas.validation.narkotikalakemedel.provtagning-behovs.missing");
            }
        }



        if (narkotikaLakemedel.getLakarordineratLakemedelsbruk() == null) {
            addValidationError("narkotikaLakemedel.lakarordineratLakemedelsbruk", ValidationMessageType.EMPTY,
                    "ts-bas.validation.narkotikaLakemedel.lakarordineratLakemedelsbruk.missing");
            return;

        } else if (narkotikaLakemedel.getLakarordineratLakemedelsbruk()) {
            assertDescriptionNotEmpty(narkotikaLakemedel.getLakemedelOchDos(), "narkotikaLakemedel.getLakemedelOchDos",
                    "ts-bas.validation.narkotikaLakemedel.lakemedelOchDos.missing");
        }
    }

    private void validateMedvetandestorning(final Medvetandestorning medvetandestorning) {

        if (medvetandestorning == null) {
            addValidationError("medvetandestorning", ValidationMessageType.EMPTY, "ts-bas.validation.medvetandestorning.missing");
            return;
        }

        if (medvetandestorning.getMedvetandestorning() == null) {
            addValidationError("medvetandestorning.medvetandestorning", ValidationMessageType.EMPTY,
                    "ts-bas.validation.medvetandestorning.medvetandestorning.missing");
        }
    }

    private void validateSyn(final Syn syn) {

        if (syn == null) {
            addValidationError("syn", ValidationMessageType.EMPTY, "ts-bas.validation.syn.missing");
            return;
        }

        if (syn.getSynfaltsdefekter() == null) {
            addValidationError("syn.teckenSynfaltsdefekter", ValidationMessageType.EMPTY, "ts-bas.validation.syn.tecken-synfaltsdefekter.missing");
        }

        if (syn.getNattblindhet() == null) {
            addValidationError("syn.nattblindhet", ValidationMessageType.EMPTY, "ts-bas.validation.syn.nattblindhet.missing");
        }

        if (syn.getProgressivOgonsjukdom() == null) {
            addValidationError("syn.progressivOgonsjukdom", ValidationMessageType.EMPTY, "ts-bas.validation.syn.progressiv-ogonsjukdom.missing");
        }

        if (syn.getDiplopi() == null) {
            addValidationError("syn.diplopi", ValidationMessageType.EMPTY, "ts-bas.validation.syn.diplopi.missing");
        }

        if (syn.getNystagmus() == null) {
            addValidationError("syn.nystagmus", ValidationMessageType.EMPTY, "ts-bas.validation.syn.nystagmus.missing");
        }

        if (syn.getHogerOga() == null) {
            addValidationError("syn.hogerOga", ValidationMessageType.EMPTY, "ts-bas.validation.syn.hogeroga.missing");
        } else {
            if (syn.getHogerOga().getUtanKorrektion() == null) {
                addValidationError("syn.hogerOga.utanKorrektion", ValidationMessageType.EMPTY, "ts-bas.validation.syn.hogeroga.utanKorrektion.missing");

            } else if (syn.getHogerOga().getUtanKorrektion() < 0.0 || syn.getHogerOga().getUtanKorrektion() > 2.0) {
                addValidationError("syn.hogerOga.utanKorrektion", ValidationMessageType.INVALID_FORMAT,
                        "ts-bas.validation.syn.hogeroga.utankorrektion.out-of-bounds");
            }

            if (syn.getHogerOga().getMedKorrektion() != null) {
                if (syn.getHogerOga().getMedKorrektion() < 0.0 || syn.getHogerOga().getMedKorrektion() > 2.0) {
                    addValidationError("syn.hogerOga.medKorrektion", ValidationMessageType.INVALID_FORMAT,
                            "ts-bas.validation.syn.hogerOga.medKorrektion.out-of-bounds");
                }
            }
        }

        if (syn.getVansterOga() == null) {
            addValidationError("syn.vansterOga", ValidationMessageType.EMPTY, "ts-bas.validation.syn.vansteroga.missing");
        } else {
            if (syn.getVansterOga().getUtanKorrektion() == null) {
                addValidationError("syn.vansterOga.utanKorrektion", ValidationMessageType.EMPTY,
                        "ts-bas.validation.syn.vansteroga.utankorrektion.missing");

            } else if (syn.getVansterOga().getUtanKorrektion() < 0.0 || syn.getVansterOga().getUtanKorrektion() > 2.0) {
                addValidationError("syn.vansterOga.utanKorrektion", ValidationMessageType.INVALID_FORMAT,
                        "ts-bas.validation.syn.vansteroga.utankorrektion.out-of-bounds");
            }

            if (syn.getVansterOga().getMedKorrektion() != null) {
                if (syn.getVansterOga().getMedKorrektion() < 0.0 || syn.getVansterOga().getMedKorrektion() > 2.0) {
                    addValidationError("syn.vansterOga.medKorrektion", ValidationMessageType.INVALID_FORMAT,
                            "ts-bas.validation.syn.vansteroga.medkorrektion.out-of-bounds");
                }
            }
        }

        if (syn.getBinokulart() == null) {
            addValidationError("syn.binokulart", ValidationMessageType.EMPTY, "ts-bas.validation.syn.binokulart.missing");
        } else {
            if (syn.getBinokulart().getUtanKorrektion() == null) {
                addValidationError("syn.binokulart.utanKorrektion", ValidationMessageType.EMPTY,
                        "ts-bas.validation.syn.binokulart.utankorrektion.missing");

            } else if (syn.getBinokulart().getUtanKorrektion() < 0.0 || syn.getBinokulart().getUtanKorrektion() > 2.0) {
                addValidationError("syn.binokulart.utanKorrektion", ValidationMessageType.INVALID_FORMAT,
                        "ts-bas.validation.syn.binokulart.utankorrektion.out-of-bounds");
            }

            if (syn.getBinokulart().getMedKorrektion() != null) {
                if (syn.getBinokulart().getMedKorrektion() < 0.0 || syn.getBinokulart().getMedKorrektion() > 2.0) {
                    addValidationError("syn.binokulart.medKorrektion", ValidationMessageType.INVALID_FORMAT,
                            "ts-bas.validation.syn.binokulart.medkorrektion.out-of-bounds");
                }
            }
        }
    }

    /**
     * Check for null or empty String, if so add a validation error for field with errorCode.
     *
     * @param beskrivning
     *            the String to check
     * @param field
     *            the target field in the model
     * @param errorCode
     *            the errorCode to log in validation errors
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
     * @return {@link ValidationStatus.VALID} if there are no errors, and {@link ValidationStatus.INVALID} otherwise
     */
    private ValidationStatus getValidationStatus() {
        return (validationMessages.isEmpty()) ? ValidationStatus.VALID : ValidationStatus.INVALID;
    }

    /**
     * Create a ValidationMessage and add it to the {@link ValidateDraftResponseHolder}.
     *
     * @param field
     *            a String with the name of the field
     * @param msg
     *            a String with an error code for the front end implementation
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
