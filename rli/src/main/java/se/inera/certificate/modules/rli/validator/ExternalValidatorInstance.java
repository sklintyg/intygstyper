package se.inera.certificate.modules.rli.validator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import se.inera.certificate.model.HosPersonal;
import se.inera.certificate.model.Id;
import se.inera.certificate.model.Kod;
import se.inera.certificate.model.Observation;
import se.inera.certificate.model.Patient;
import se.inera.certificate.model.Rekommendation;
import se.inera.certificate.model.Vardenhet;
import se.inera.certificate.modules.rli.model.codes.AktivitetsKod;
import se.inera.certificate.modules.rli.model.codes.ArrangemangsTyp;
import se.inera.certificate.modules.rli.model.codes.CodeConverter;
import se.inera.certificate.modules.rli.model.codes.HSpersonalTyp;
import se.inera.certificate.modules.rli.model.codes.ICodeSystem;
import se.inera.certificate.modules.rli.model.codes.ObservationsKod;
import se.inera.certificate.modules.rli.model.codes.RekommendationsKod;
import se.inera.certificate.modules.rli.model.codes.SjukdomsKannedom;
import se.inera.certificate.modules.rli.model.external.Aktivitet;
import se.inera.certificate.modules.rli.model.external.Arrangemang;
import se.inera.certificate.modules.rli.model.external.Utlatande;
import se.inera.certificate.validate.IdValidator;
import se.inera.certificate.validate.SimpleIdValidatorBuilder;

public class ExternalValidatorInstance {

    private final List<String> validationErrors;

    private IdValidator idValidator;

    public ExternalValidatorInstance() {
        validationErrors = new ArrayList<>();

        idValidator = new SimpleIdValidatorBuilder().withPersonnummerValidator(false)
                .withSamordningsnummerValidator(false).withHsaIdValidator().build();
    }

    public List<String> validate(Utlatande utlatande) {
        validateUtlatande(utlatande);
        validatePatient(utlatande);
        validateSkapasAv(utlatande);
        validateRekommendationer(utlatande);
        validateAktiviteter(utlatande);
        validateObservationer(utlatande);
        validateArrangemang(utlatande);

        return validationErrors;
    }

    /**
     * 
     * Validates that required attributes connected with the actual class Utlatande are present
     */
    private void validateUtlatande(Utlatande utlatande) {
        assertNotNull(utlatande.getId(), "id");
        assertNotNull(utlatande.getTyp(), "utlatandetyp");
        assertNotNull(utlatande.getSigneringsdatum(), "signeringsdatum");
    }

    /**
     * 
     * Validates Arrangemang in Utlatande First makes sure Arrangemang is not null, if so, the required attributes are
     * validated in turn and any errors are added to validation error list
     */
    private void validateArrangemang(Utlatande utlatande) {
        Arrangemang arrangemang = utlatande.getArrangemang();

        if (assertNotNull(arrangemang, "arrangemang")) {
            return;
        }

        if (arrangemang.getArrangemangstyp().getCode() == null
                || !arrangemang.getArrangemangstyp().getCode().equals(ArrangemangsTyp.RESA.getCode())) {
            validationError("Code in arrangemang must be SNOMED-CT code: " + ArrangemangsTyp.RESA.getCode());
        }

        checkBokningsdatum(arrangemang);

        assertNotNull(arrangemang.getArrangemangstid(), "arrangemangstid");
        assertNotNull(arrangemang.getPlats(), "plats");

    }

    private void checkBokningsdatum(Arrangemang arrangemang) {
        assertNotNull(arrangemang.getBokningsdatum(), "Bokningsdatum");
    }

    private void validateSkapasAv(Utlatande utlatande) {
        HosPersonal skapatAv = utlatande.getSkapadAv();

        if (assertNotNull(skapatAv, "SkapadAv")) {
            return;
        }

        checkAndValidateHsaId(skapatAv.getId(), "skapatAv.id");

        assertNotNull(skapatAv.getNamn(), "skapadAv.namn");

        validateVardenhet(skapatAv.getVardenhet());
    }

    private void validateVardenhet(Vardenhet vardenhet) {
        if (assertNotNull(vardenhet, "vardenhet")) {
            return;
        }

        checkAndValidateHsaId(vardenhet.getId(), "vardenhet");

        assertNotNull(vardenhet.getNamn(), "vardenhet.namn");

        if (assertNotNull(vardenhet.getVardgivare(), "vardenhet.vardgivare")) {
            return;
        }

        checkAndValidateHsaId(vardenhet.getVardgivare().getId(), "vardgivare");

        assertNotEmpty(vardenhet.getVardgivare().getNamn(), "vardgivare.namn");
    }

    private void checkAndValidateHsaId(Id id, String element) {
        assertNotNull(id, element);
        if (!id.getRoot().equals(HSpersonalTyp.HSA_ID.getCode())) {
            validationError(element + " should be an HSA-ID with root: " + HSpersonalTyp.HSA_ID.getCode());
        }
        validationErrors.addAll(idValidator.validate(id));
    }

    /**
     * 
     * Make sure Utlatande contains 1..* Observation
     */
    private void validateObservationer(Utlatande utlatande) {
        List<Observation> observationer = utlatande.getObservationer();

        if (observationer.size() < 1 || observationer.size() > 2) {
            validationError("one or two observationer must be present");
            return;
        }

        for (Observation obs : observationer) {
            checkObservation(obs);
        }

    }

    private void checkObservation(Observation source) {
        if (source.getObservationskod().getCode() == ObservationsKod.GRAVIDITET.getCode()) {
            assertNotNull(source.getObservationsperiod(), "observationsperiod");
        }

        checkAndValidateHsaId(source.getUtforsAv().getAntasAv().getId(), "observation.utforsAv.antasAv.id");
    }

    /**
     * 
     * Validate Aktiviteter,
     */
    private void validateAktiviteter(Utlatande utlatande) {
        List<Aktivitet> aktiviteter = utlatande.getAktiviteter();

        if (assertNotEmpty(aktiviteter, "aktiviteter")) {
            return;
        }

        /**
         * Make sure Utlatande contains 1..3 Aktiviteter and nothing else
         */
        if (aktiviteter.size() < 1 && aktiviteter.size() > 3) {
            validationError("Utlatande does not contain 1 to 3 Aktiviteter");
            return;
        }
        /**
         * Make sure one instance of aktivitet is of type KLINISK UNDERSOKNING
         */
        Aktivitet akt = (Aktivitet) CollectionUtils.find(aktiviteter, new AktivitetsKodPredicate(
                AktivitetsKod.KLINISK_UNDERSOKNING));

        if (!assertNotNull(akt, "aktivitet of type KLINISK UNDERSOKNING")) {
            assertNotNull(akt.getAktivitetstid(),
                    "aktivitet.aktivitetstid for Aktivitet of type Klinisk Undersokning (AV020 / UNS)");
        }

        for (Aktivitet aktivitet : aktiviteter) {
            assertNotNull(aktivitet.getAktivitetskod(), "aktivitetskod");
            assertNotNull(aktivitet.getPlats(), "plats");
        }
    }

    /**
     * Make sure Utlatande contains 1..* Rekommendationer and that a correct code is used.
     */
    private void validateRekommendationer(Utlatande utlatande) {
        List<Rekommendation> rekommendationer = utlatande.getRekommendationer();
        if (assertNotEmpty(rekommendationer, "rekommendation")) {
            return;
        }

        for (Rekommendation rek : rekommendationer) {
            assertKodInEnum(rek.getRekommendationskod(), RekommendationsKod.class, "rekomendationskod");
            assertKodInEnum(rek.getSjukdomskannedom(), SjukdomsKannedom.class, "sjukdomskannedom");
        }
    }

    /**
     * Make sure Utlatande contains 1 Patient
     */
    private void validatePatient(Utlatande utlatande) {

        Patient patient = utlatande.getPatient();

        if (assertNotNull(patient, "patient")) {
            return;
        }

        if (!assertNotNull(patient.getId(), "patient.id")) {
            validationErrors.addAll(idValidator.validate(patient.getId()));
        }

        /**
         * Make sure Fornamn and Efternamn contains at least one item
         */
        assertNotEmpty(patient.getFornamn(), "patient.fornamn");
        assertNotEmpty(patient.getEfternamn(), "patient.efternamn");

        assertNotEmpty(patient.getPostadress(), "patient.postadress");
        assertNotEmpty(patient.getPostnummer(), "patient.postnummer");
        assertNotEmpty(patient.getPostort(), "patient.postort");
    }

    /**
     * Predicate for AktivitetsKod
     * 
     * @author erik
     * 
     */
    private class AktivitetsKodPredicate implements Predicate {

        private final AktivitetsKod aktKodEnum;

        public AktivitetsKodPredicate(AktivitetsKod aktKodEnum) {
            this.aktKodEnum = aktKodEnum;
        }

        @Override
        public boolean evaluate(Object obj) {

            if (!(obj instanceof Aktivitet)) {
                return false;
            }

            Aktivitet akt = (Aktivitet) obj;
            Kod aktKod = akt.getAktivitetskod();

            return (aktKod.getCode().equals(aktKodEnum.getCode()));
        }

    }

    private void validationError(String error) {
        validationErrors.add(error);
    }

    private boolean assertNotNull(Object value, String element) {
        if (value == null) {
            validationError(element + " was not found");
            return true;
        }
        return false;
    }

    private boolean assertNotEmpty(String value, String element) {
        if (value == null || value.isEmpty()) {
            validationError(element + " was empty");
            return true;
        }
        return false;
    }

    private boolean assertNotEmpty(Collection<?> value, String element) {
        if (value == null || value.isEmpty()) {
            validationError("At least one " + element + " must be present");
            return true;
        }
        return false;
    }

    private boolean assertKod(Kod kod, ICodeSystem expected, String element) {
        if (!kod.getCode().equals(expected.getCode())) {
            validationError("");
            return true;
        }
        if (!kod.getCodeSystem().equals(expected.getCodeSystem())) {
            validationError("");
            return true;
        }
        return false;
    }

    private boolean assertKodInEnum(Kod kod, Class<? extends ICodeSystem> expectedEnum, String element) {
        assertNotNull(kod, element);
        try {
            CodeConverter.fromCode(kod, expectedEnum);
        } catch (Exception e ) {
            validationError("Kod " + element + " was unknown");
            return true;
        }
        return false;
    }
}
