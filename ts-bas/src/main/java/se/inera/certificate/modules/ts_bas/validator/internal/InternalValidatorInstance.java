package se.inera.certificate.modules.ts_bas.validator.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import se.inera.certificate.modules.ts_bas.model.internal.Neurologi;
import se.inera.certificate.modules.ts_bas.model.internal.Njurar;
import se.inera.certificate.modules.ts_bas.model.internal.Psykiskt;
import se.inera.certificate.modules.ts_bas.model.internal.Sjukhusvard;
import se.inera.certificate.modules.ts_bas.model.internal.SomnVakenhet;
import se.inera.certificate.modules.ts_bas.model.internal.Syn;
import se.inera.certificate.modules.ts_bas.model.internal.Utlatande;
import se.inera.certificate.modules.ts_bas.model.internal.Utvecklingsstorning;
import se.inera.certificate.modules.ts_bas.model.internal.Vardkontakt;
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

    private static Logger LOG = LoggerFactory.getLogger(InternalValidatorInstance.class);

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

        if (utlatande == null) {
            addValidationError("utlatande", "ts.validation.utlatande.missing");
            return validationResponse;
        }

        validateBedomning(utlatande.getBedomning());
        validateDiabetes(utlatande.getDiabetes());
        validateFunktionsnedsattning(utlatande.getFunktionsnedsattning());
        validateHjartKarl(utlatande.getHjartKarl());
        validateHorselBalans(utlatande.getHorselBalans());
        validateHoSPersonal(utlatande.getSkapadAv());

        validateIntygAvser(utlatande.getIntygAvser());
        validateIdentitetStyrkt(utlatande.getVardkontakt());

        validateKognitivt(utlatande.getKognitivt());
        validateMedicinering(utlatande.getMedicinering());
        validateMedvetandestorning(utlatande.getMedvetandestorning());
        validateSyn(utlatande.getSyn());
        validateNarkotikaLakemedel(utlatande.getNarkotikaLakemedel());
        validateSjukhusvard(utlatande.getSjukhusvard());
        validateNeurologi(utlatande.getNeurologi());
        validateNjurar(utlatande.getNjurar());
        validateSomnVakenhet(utlatande.getSomnVakenhet());
        validatePsykiskt(utlatande.getPsykiskt());
        validateUtvecklingsstorning(utlatande.getUtvecklingsstorning());

        validationResponse.setStatus(getValidationStatus());

        return validationResponse;
    }

    private void validateIdentitetStyrkt(Vardkontakt vardkontakt) {
        if (vardkontakt == null) {
            addValidationError("vardkontakt", "ts.validation.vardkontakt.missing");
            return;
        }
        if (vardkontakt.getIdkontroll() == null) {
            addValidationError("identitet", "ts.validation.identitet.missing");
        }
    }

    private void validateUtvecklingsstorning(Utvecklingsstorning utvecklingsstorning) {
        if (utvecklingsstorning == null) {
            addValidationError("utvecklingsstorning", "ts.validation.utvecklingsstorning.missing");
            return;
        }
        if (utvecklingsstorning.getHarSyndrom() == null) {
            addValidationError("utvecklingsstorning.harSyndrom", "ts.validation.utvecklingsstorning.harsyndrom.missing");
        }
        if (utvecklingsstorning.getPsykiskUtvecklingsstorning() == null) {
            addValidationError("utvecklingsstorning.psykiskUtvecklingsstorning",
                    "ts.validation.utvecklingsstorning.psykiskutvecklingsstorning.missing");
        }

    }

    private void validatePsykiskt(Psykiskt psykiskt) {
        if (psykiskt == null) {
            addValidationError("psykiskt", "ts.validation.psykiskt.missing");
            return;
        }

        if (psykiskt.getPsykiskSjukdom() == null) {
            addValidationError("psykiskt", "ts.validation.psykiskt.psykisksjukdom.missing");
        }
    }

    private void validateSomnVakenhet(SomnVakenhet somnVakenhet) {
        if (somnVakenhet == null) {
            addValidationError("somnVakenhet", "ts.validation.somnvakenhet.missing");
            return;
        }
        if (somnVakenhet.getTeckenSomnstorningar() == null) {
            addValidationError("somnVakenhet.teckenSomnstorningar",
                    "ts.validation.somnvakenhet.teckensomnstorningar.missing");
        }
    }

    private void validateNjurar(Njurar njurar) {
        if (njurar == null) {
            addValidationError("njurar", "ts.validation.njurar.missing");
            return;
        }
        if (njurar.getNedsattNjurfunktion() == null) {
            addValidationError("njurar.nedsattNjurfunktion", "ts.validation.njurar.nedsattnjurfunktion.missing");
        }
    }

    private void validateNeurologi(Neurologi neurologi) {
        if (neurologi == null) {
            addValidationError("neurologi", "ts.validation.neurologi.missing");
            return;
        }
        if (neurologi.getNeurologiskSjukdom() == null) {
            addValidationError("neurologi.neurologiskSjukdom", "ts.validation.neurologi.neurologisksjukdom.missing");
        }
    }

    private void validateSjukhusvard(Sjukhusvard sjukhusvard) {

        if (sjukhusvard == null) {
            addValidationError("sjukhusvard", "ts.validation.sjukhusvard.missing");
            return;
        }

        if (sjukhusvard.getSjukhusEllerLakarkontakt() == null) {
            addValidationError("sjukhusvard.sjukhusEllerLakarkontakt",
                    "ts.validation.sjukhusvard.sjukhusEllerLakarkontakt.missing");
            return;

        }

        if (sjukhusvard.getSjukhusEllerLakarkontakt()) {
            assertDescriptionNotEmpty(sjukhusvard.getAnledning(), "sjukhusvard.anledning",
                    "ts.validation.sjukhusvard.anledning.missing");
            assertDescriptionNotEmpty(sjukhusvard.getTidpunkt(), "sjukhusvard.tidpunkt",
                    "ts.validation.sjukhusvard.tidpunkt.missing");
            assertDescriptionNotEmpty(sjukhusvard.getVardinrattning(), "sjukhusvard.vardinrattning",
                    "ts.validation.sjukhusvard.vardinrattning.missing");
        }
    }

    private void validateBedomning(final Bedomning bedomning) {

        if (bedomning == null) {
            addValidationError("bedomning", "ts.validation.bedomning.missing");
            return;
        }

        if (bedomning.getKanInteTaStallning() == null && bedomning.getKorkortstyp().isEmpty()) {
            addValidationError("bedomning", "ts.validation.bedomning.must-choose-one");
        }
    }

    private void validateDiabetes(final Diabetes diabetes) {

        if (diabetes == null) {
            addValidationError("diabetes", "ts.validation.diabetes.missing");
            return;
        }

        if (diabetes.getHarDiabetes() == null) {
            addValidationError("diabetes.harDiabetes", "ts.validation.diabetes.harDiabetes.missing");
            return;
        }
        if (diabetes.getHarDiabetes()) {

            if (diabetes.getDiabetesTyp() == null) {
                addValidationError("diabetes.diabetesTyp", "ts.validation.diabetes.diabetesTyp.missing");

            } else if (diabetes.getDiabetesTyp().equals(ObservationsKod.DIABETES_TYP_2.name())) {
                if (diabetes.getInsulin() == null && diabetes.getKost() == null && diabetes.getTabletter() == null) {
                    addValidationError("diabetes.diabetesTyp", "ts.validation.diabetes.diabetesTyp.must-choose-one");
                }
            }
        }
    }

    private void validateFunktionsnedsattning(final Funktionsnedsattning funktionsnedsattning) {

        if (funktionsnedsattning == null) {
            addValidationError("funktionsnedsattning", "ts.validation.funktionsnedsattning.missing");
            return;
        }

        if (funktionsnedsattning.getFunktionsnedsattning() == null) {
            addValidationError("funktionsnedsattning.funktionsnedsattning",
                    "ts.validation.funktionsnedsattning.funktionsnedsattning.missing");
            return;

        } else if (funktionsnedsattning.getFunktionsnedsattning()) {
            assertDescriptionNotEmpty(funktionsnedsattning.getBeskrivning(), "funktionsnedsattning.beskrivning",
                    "ts.validation.funktionsnedsattning.beskrivning.missing");
        }
    }

    private void validateHjartKarl(final HjartKarl hjartKarl) {

        if (hjartKarl == null) {
            addValidationError("hjartKarl", "ts.validation.hjartKarl.missing");
            return;
        }

        if (hjartKarl.getRiskfaktorerStroke() == null) {
            addValidationError("hjartKarl.getRiskfaktorerStroke", "ts.validation.hjartKarl.riskfaktorerStroke.missing");
            return;

        } else if (hjartKarl.getRiskfaktorerStroke()) {
            assertDescriptionNotEmpty(hjartKarl.getBeskrivningRiskfaktorer(), "hjartKarl.beskrivningRiskfaktorer",
                    "ts.validation.hjartKarl.beskrivningRiskfaktorer.missing");
        }
    }

    private void validateHorselBalans(final HorselBalans horselBalans) {

        if (horselBalans == null) {
            addValidationError("horselBalans", "ts.validation.horselBalans.missing");
            return;
        }

        if (horselBalans.getBalansrubbningar() == null) {
            addValidationError("horselBalans.balansrubbningar", "ts.validation.horselBalans.balansrubbningar.missing");
        }
    }

    private void validateHoSPersonal(final HoSPersonal skapadAv) {
        // TODO: validate something here?
    }

    private void validateIntygAvser(final IntygAvser intygAvser) {

        if (intygAvser == null) {
            addValidationError("intygAvser", "ts.validation.intygAvser.missing");
            return;
        }

        if (intygAvser.getKorkortstyp().isEmpty()) {
            addValidationError("intygAvser", "ts.validation.intygAvser.must-choose-one");
        }
    }

    private void validateKognitivt(final Kognitivt kognitivt) {

        if (kognitivt == null) {
            addValidationError("kognitivt", "ts.validation.kognitivt.missing");
            return;
        }

        if (kognitivt.getSviktandeKognitivFunktion() == null) {
            addValidationError("kognitivt.sviktandeKognitivFunktion",
                    "ts.validation.kognitivt.sviktandeKognitivFunktion.missing");
        }
    }

    private void validateMedicinering(final Medicinering medicinering) {

        if (medicinering == null) {
            addValidationError("medicinering", "ts.validation.medicinering.missing");
            return;
        }

        if (medicinering.getStadigvarandeMedicinering() == null) {
            addValidationError("medicinering.stadigvarandeMedicinering",
                    "ts.validation.medicinering.stadigvarandeMedicinering.missing");
            return;

        } else if (medicinering.getStadigvarandeMedicinering()) {
            assertDescriptionNotEmpty(medicinering.getBeskrivning(), "medicinering.beskrivning",
                    "ts.validation.medicinering.beskrivning.missing");
        }
    }

    private void validateNarkotikaLakemedel(final NarkotikaLakemedel narkotikaLakemedel) {

        if (narkotikaLakemedel == null) {
            addValidationError("narkotikaLakemedel", "ts.validation.narkotikaLakemedel.missing");
            return;
        }

        if (narkotikaLakemedel.getLakarordineratLakemedelsbruk() == null) {
            addValidationError("narkotikaLakemedel",
                    "ts.validation.narkotikaLakemedel.lakarordineratLakemedelsbruk.missing");
            return;

        } else if (narkotikaLakemedel.getLakarordineratLakemedelsbruk()) {
            assertDescriptionNotEmpty(narkotikaLakemedel.getLakemedelOchDos(), "narkotikaLakemedel.getLakemedelOchDos",
                    "ts.validation.narkotikaLakemedel.lakemedelOchDos.missing");
        }
    }

    private void validateMedvetandestorning(final Medvetandestorning medvetandestorning) {

        if (medvetandestorning == null) {
            addValidationError("medvetandestorning", "ts.validation.medvetandestorning.missing");
            return;
        }

        if (medvetandestorning.getMedvetandestorning() == null) {
            addValidationError("medvetandestorning.medvetandestorning",
                    "ts.validation.medvetandestorning.medvetandestorning.missing");
            return;

        } else if (medvetandestorning.getMedvetandestorning()) {
            assertDescriptionNotEmpty(medvetandestorning.getBeskrivning(), "medvetandestorning.beskrivning",
                    "ts.validation.medvetandestorning.beskrivning.missing");
        }
    }

    private void validateSyn(final Syn syn) {

        if (syn == null) {
            addValidationError("syn", "ts.validation.syn.missing");
            return;
        }

        if (syn.getHogerOga() == null) {
            addValidationError("syn.hogerOga", "ts.validation.syn.hogeroga.missing");
        } else {
            if (syn.getHogerOga().getUtanKorrektion() == null) {
                addValidationError("syn.hogerOga.utanKorrektion", "ts.validation.syn.hogeroga.utanKorrektion.missing");

            } else if (syn.getHogerOga().getUtanKorrektion() < 0.0 || syn.getHogerOga().getUtanKorrektion() > 2.0) {
                addValidationError("syn.hogerOga.utanKorrektion",
                        "ts.validation.syn.hogeroga.utankorrektion.out-of-bounds");
            }

            if (syn.getHogerOga().getMedKorrektion() != null) {
                if (syn.getHogerOga().getMedKorrektion() < 0.0 || syn.getHogerOga().getMedKorrektion() > 2.0) {
                    addValidationError("syn.hogerOga.medKorrektion",
                            "ts.validation.syn.hogerOga.medKorrektion.out-of-bounds");
                }
            }
        }

        if (syn.getVansterOga() == null) {
            addValidationError("syn.vansterOga", "ts.validation.syn.vansteroga.missing");
        } else {
            if (syn.getVansterOga().getUtanKorrektion() == null) {
                addValidationError("syn.vansterOga.utanKorrektion",
                        "ts.validation.syn.vansteroga.utankorrektion.missing");

            } else if (syn.getVansterOga().getUtanKorrektion() < 0.0 || syn.getVansterOga().getUtanKorrektion() > 2.0) {
                addValidationError("syn.vansterOga.utanKorrektion",
                        "ts.validation.syn.vansteroga.utankorrektion.missing");
            }

            if (syn.getVansterOga().getMedKorrektion() != null) {
                if (syn.getVansterOga().getMedKorrektion() < 0.0 || syn.getVansterOga().getMedKorrektion() > 2.0) {
                    addValidationError("syn.vansterOga.medKorrektion",
                            "ts.validation.syn.vansteroga.medkorrektion.out-of-bounds");
                }
            }
        }

        if (syn.getBinokulart() == null) {
            addValidationError("syn.binokulart", "ts.validation.syn.binokulart.missing");
        } else {
            if (syn.getBinokulart().getUtanKorrektion() == null) {
                addValidationError("syn.binokulart.utanKorrektion", "ts.validation.syn.binokulart.utankorrektion.missing");
                
            } else if (syn.getBinokulart().getUtanKorrektion() < 0.0 || syn.getBinokulart().getUtanKorrektion() > 2.0) {
                addValidationError("syn.binokulart.utanKorrektion",
                        "ts.validation.syn.binokulart.utankorrektion.out-of-bounds");
            }

            if (syn.getBinokulart().getMedKorrektion() != null) {
                if (syn.getBinokulart().getMedKorrektion() < 0.0 || syn.getBinokulart().getMedKorrektion() > 2.0) {
                    addValidationError("syn.binokulart.medKorrektion",
                            "ts.validation.syn.binokulart.medkorrektion.out-of-bounds");
                }
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
            LOG.debug(field + " " + errorCode);
        }
    }

    /**
     * Check if there are validation errors
     * 
     * @return {@link ValidationStatus.COMPLETE} if there are no errors, and {@link ValidationStatus.INCOMPLETE}
     *         otherwise
     */
    private ValidationStatus getValidationStatus() {
        return (validationResponse.hasErrorMessages()) ? ValidationStatus.INVALID : ValidationStatus.VALID;
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
        validationResponse.addErrorMessage(new ValidationMessage(field, msg));
        LOG.debug(field + " " + msg);
    }
}
