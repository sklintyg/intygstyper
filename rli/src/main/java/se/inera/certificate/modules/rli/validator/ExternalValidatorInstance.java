package se.inera.certificate.modules.rli.validator;

import java.util.ArrayList;
import java.util.List;

import se.inera.certificate.model.HosPersonal;
import se.inera.certificate.model.Id;
import se.inera.certificate.model.Kod;
import se.inera.certificate.model.Observation;
import se.inera.certificate.model.Patient;
import se.inera.certificate.model.Rekommendation;
import se.inera.certificate.model.Utforarroll;
import se.inera.certificate.model.Vardenhet;
import se.inera.certificate.modules.rli.model.codes.AktivitetsKod;
import se.inera.certificate.modules.rli.model.codes.ArrangemangsKod;
import se.inera.certificate.modules.rli.model.codes.BefattningsKod;
import se.inera.certificate.modules.rli.model.codes.CodeConverter;
import se.inera.certificate.modules.rli.model.codes.HSpersonalKod;
import se.inera.certificate.modules.rli.model.codes.ICodeSystem;
import se.inera.certificate.modules.rli.model.codes.ObservationsKod;
import se.inera.certificate.modules.rli.model.codes.RekommendationsKod;
import se.inera.certificate.modules.rli.model.codes.SjukdomskannedomKod;
import se.inera.certificate.modules.rli.model.codes.UtforarrollKod;
import se.inera.certificate.modules.rli.model.codes.UtlatandeKod;
import se.inera.certificate.modules.rli.model.external.Aktivitet;
import se.inera.certificate.modules.rli.model.external.Arrangemang;
import se.inera.certificate.modules.rli.model.external.Utlatande;
import se.inera.certificate.validate.IdValidator;
import se.inera.certificate.validate.SimpleIdValidatorBuilder;

public class ExternalValidatorInstance {

    private final List<String> validationErrors;

    private final static IdValidator ID_VALIDATOR;

    static {
        SimpleIdValidatorBuilder builder = new SimpleIdValidatorBuilder();
        builder.withPersonnummerValidator(false);
        builder.withSamordningsnummerValidator(false);
        builder.withHsaIdValidator();

        ID_VALIDATOR = builder.build();
    }

    public ExternalValidatorInstance() {
        validationErrors = new ArrayList<>();
    }

    public List<String> validate(Utlatande utlatande) {
        validateUtlatande(utlatande);
        validatePatient(utlatande.getPatient());
        validateHosPersonal(utlatande.getSkapadAv(), "skapadAv");
        validateRekommendationer(utlatande.getRekommendationer());
        validateAktiviteter(utlatande.getAktiviteter());
        validateObservationer(utlatande.getObservationer());
        validateArrangemang(utlatande.getArrangemang());

        return validationErrors;
    }

    /**
     * 
     * Validates that required attributes connected with the actual class Utlatande are present
     */
    private void validateUtlatande(Utlatande utlatande) {
        assertNotNull(utlatande.getId(), "id");
        assertKodInEnum(utlatande.getTyp(), UtlatandeKod.class, "utlatandetyp");
        assertNotNull(utlatande.getSigneringsdatum(), "signeringsdatum");
    }

    /**
     * 
     * Validates Arrangemang in Utlatande First makes sure Arrangemang is not null, if so, the required attributes are
     * validated in turn and any errors are added to validation error list
     */
    private void validateArrangemang(Arrangemang arrangemang) {
        if (assertNotNull(arrangemang, "arrangemang")) {
            return;
        }

        assertNotNull(arrangemang.getBokningsdatum(), "arrangemang.bokningsdatum");
        assertNotNull(arrangemang.getArrangemangstid(), "arrangemang.arrangemangstid");
        assertKodInEnum(arrangemang.getArrangemangstyp(), ArrangemangsKod.class, "arrangemang.arrangemangstyp");
        assertNotEmpty(arrangemang.getPlats(), "plats");
    }

    private void validateHosPersonal(HosPersonal skapatAv, String elementPrefix) {
        if (assertNotNull(skapatAv, elementPrefix)) {
            return;
        }

        assertValidHsaId(skapatAv.getId(), elementPrefix + ".id");

        assertNotEmpty(skapatAv.getNamn(), elementPrefix + ".namn");

        if (skapatAv.getBefattning() != null) {
            // TODO: Change befattning to Kod when the time is right.
            assertKodInEnum(new Kod(skapatAv.getBefattning()), BefattningsKod.class, elementPrefix + ".befattning");
        }

        validateVardenhet(skapatAv.getVardenhet(), elementPrefix);
    }

    private void validateVardenhet(Vardenhet vardenhet, String elementPrefix) {
        if (assertNotNull(vardenhet, elementPrefix + ".vardenhet")) {
            return;
        }

        assertValidHsaId(vardenhet.getId(), elementPrefix + ".vardenhet.id");
        assertNotEmpty(vardenhet.getNamn(), elementPrefix + ".vardenhet.namn");

        if (assertNotNull(vardenhet.getVardgivare(), elementPrefix + ".vardenhet.vardgivare")) {
            return;
        }

        assertValidHsaId(vardenhet.getVardgivare().getId(), elementPrefix + ".vardenhet.vardgivare.id");
        assertNotEmpty(vardenhet.getVardgivare().getNamn(), elementPrefix + ".vardenhet.vardgivare.namn");
    }

    /**
     * 
     * Make sure Utlatande contains 1..* Observation
     */
    private void validateObservationer(List<Observation> observationer) {
        if (observationer.size() < 1 || observationer.size() > 2) {
            validationError("one or two observationer must be present");
            return;
        }

        for (Observation observation : observationer) {
            assertKodInEnum(observation.getObservationskod(), ObservationsKod.class, "observation.observationskod");
            if (ObservationsKod.GRAVIDITET.matches(observation.getObservationskod())) {
                assertNotNull(observation.getObservationsperiod(), "observation.observationsperiod");
            }

            validateUtforsAv(observation.getUtforsAv(), "observation");
        }

    }

    private void validateUtforsAv(Utforarroll utforsAv, String elementPrefix) {
        assertKodInEnum(utforsAv.getUtforartyp(), UtforarrollKod.class, elementPrefix + ".utforsAv.utforartyp");

        validateHosPersonal(utforsAv.getAntasAv(), elementPrefix + ".antasAv");
    }

    /**
     * 
     * Validate Aktiviteter,
     */
    private void validateAktiviteter(List<Aktivitet> aktiviteter) {
        /**
         * Make sure Utlatande contains 1..3 Aktiviteter and nothing else
         */
        if (aktiviteter.size() < 1 && aktiviteter.size() > 3) {
            validationError("Utlatande does not contain 1 to 3 Aktiviteter");
            return;
        }

        for (Aktivitet aktivitet : aktiviteter) {
            assertKodInEnum(aktivitet.getAktivitetskod(), AktivitetsKod.class, "aktivitet.aktivitetskod");
//            assertNotNull(aktivitet.getPlats(), "aktivitet.plats");

            if (AktivitetsKod.KLINISK_UNDERSOKNING.matches(aktivitet.getAktivitetskod())) {
                assertNotNull(aktivitet.getAktivitetstid(), "aktivitet.aktivitetstid");
            }
            if (AktivitetsKod.FORSTA_UNDERSOKNING.matches(aktivitet.getAktivitetskod())) {
                assertNotNull(aktivitet.getAktivitetstid(), "aktivitet.aktivitetstid");
            }
            
        }

    }

    /**
     * Make sure Utlatande contains 1..* Rekommendationer and that a correct code is used.
     */
    private void validateRekommendationer(List<Rekommendation> rekommendationer) {
        for (Rekommendation rek : rekommendationer) {
            assertKodInEnum(rek.getRekommendationskod(), RekommendationsKod.class, "rekommendation.rekomendationskod");
            assertKodInEnum(rek.getSjukdomskannedom(), SjukdomskannedomKod.class, "rekommendation.sjukdomskannedom");
        }
    }

    /**
     * Make sure Utlatande contains 1 Patient
     */
    private void validatePatient(Patient patient) {
        if (assertNotNull(patient, "patient")) {
            return;
        }

        assertValidPersonId(patient.getId(), "patient.id");

        for (String fornamn : patient.getFornamn()) {
            assertNotEmpty(fornamn, "patient.fornamn");
        }
        assertNotEmpty(patient.getEfternamn(), "patient.efternamn");

        assertNotEmpty(patient.getPostadress(), "patient.postadress");
        assertNotEmpty(patient.getPostnummer(), "patient.postnummer");
        assertNotEmpty(patient.getPostort(), "patient.postort");
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

    private boolean assertKodInEnum(Kod kod, Class<? extends ICodeSystem> expectedEnum, String element) {
        assertNotNull(kod, element);
        try {
            CodeConverter.fromCode(kod, expectedEnum);
        } catch (Exception e) {
            validationError("Kod " + element + " was unknown");
            return true;
        }
        return false;
    }

    private void assertValidPersonId(Id id, String element) {
        assertNotNull(id, element);
        if (!id.getRoot().equals("1.2.752.129.2.1.3.1") && !id.getRoot().equals("1.2.752.129.2.1.3.3")) {
            validationError(element + " should be a personnummer or samordningsnummer");
        }
        validationErrors.addAll(ID_VALIDATOR.validate(id));
    }

    private void assertValidHsaId(Id id, String element) {
        assertNotNull(id, element);
        if (!id.getRoot().equals(HSpersonalKod.HSA_ID.getCode())) {
            validationError(element + " should be an HSA-ID with root: " + HSpersonalKod.HSA_ID.getCode());
        }
        validationErrors.addAll(ID_VALIDATOR.validate(id));
    }
}
