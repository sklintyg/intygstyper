package se.inera.certificate.modules.fk7263.validator;

import java.util.List;

import static java.util.Arrays.asList;

import org.springframework.util.StringUtils;

import se.inera.certificate.model.Sysselsattning;
import se.inera.certificate.model.Vardkontakt;
import se.inera.certificate.model.util.Strings;
import se.inera.certificate.modules.fk7263.model.codes.Aktivitetskoder;
import se.inera.certificate.modules.fk7263.model.codes.ObservationsKoder;
import se.inera.certificate.modules.fk7263.model.codes.Sysselsattningskoder;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Aktivitet;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Observation;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Patient;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Utlatande;

/**
 * Validates a fk7263 certificate's specific rules that's not covered by internal validation.
 *
 * @author marced
 */
public class ExternalValidator extends AbstractValidator {

    private Fk7263Utlatande externalUtlatande;

    private static final List<String> VALID_DIAGNOSE_CODE_SYSTEMS = asList("ICD-10");

    public ExternalValidator(Fk7263Utlatande utlatande) {
        this.externalUtlatande = utlatande;
    }

    public List<String> validate() {

        validateDiagnose();
        validateSysselSattning();
        validateObservationDescriptions();
        validateVardkontakttid();

        return getValidationErrors();
    }

    private void validateObservationDescriptions() {
        // Field 3:
        Fk7263Observation forlopp = externalUtlatande.findObservationByKod(ObservationsKoder.FORLOPP);
        // If forlopp observation exist, it must have an description set
        if (forlopp != null && forlopp.getBeskrivning() == null) {
            addValidationError("Field 3: beskrivning is mandatory for this observation.");
        }

        // Field 4:
        Fk7263Aktivitet smittskydd = externalUtlatande.getAktivitet(Aktivitetskoder.AVSTANGNING_ENLIGT_SML_PGA_SMITTA);
        if (smittskydd  == null) {
            Fk7263Observation kroppsFunktion = externalUtlatande.findObservationByKategori(ObservationsKoder.KROPPSFUNKTIONER);
            if (kroppsFunktion != null && StringUtils.isEmpty(kroppsFunktion.getBeskrivning())) {
                addValidationError("Field 4: beskrivning is mandatory for this observation.");
            }
        }

        // Field 6a:
        Fk7263Aktivitet ovrigtRekomendation = externalUtlatande.getAktivitet(Aktivitetskoder.OVRIGT);
        if (ovrigtRekomendation != null && ovrigtRekomendation.getBeskrivning() == null) {
            addValidationError("Field 6a: beskrivning is mandatory for this Aktivitet.");
        }

        // Field 6b.1:
        Fk7263Aktivitet planeradAktivitetInomSjukVarden = externalUtlatande
                .getAktivitet(Aktivitetskoder.PLANERAD_ELLER_PAGAENDE_BEHANDLING_ELLER_ATGARD_INOM_SJUKVARDEN);
        if (planeradAktivitetInomSjukVarden != null
                && planeradAktivitetInomSjukVarden.getBeskrivning() == null) {
            addValidationError("Field 6b(1): beskrivning is mandatory for this Aktivitet.");
        }
        // Field 6b.2:
        Fk7263Aktivitet planeradAktivitetAnnan = externalUtlatande
                .getAktivitet(Aktivitetskoder.PLANERAD_ELLER_PAGAENDE_ANNAN_ATGARD);
        if (planeradAktivitetAnnan != null && planeradAktivitetAnnan.getBeskrivning() == null) {
            addValidationError("Field 6b(2): beskrivning is mandatory for this Aktivitet.");
        }

    }

    private void validateSysselSattning() {
        Fk7263Patient patient = externalUtlatande.getPatient();
        for (Sysselsattning sysselsattning : externalUtlatande.getPatient().getSysselsattningar()) {
            if (Sysselsattningskoder.NUVARANDE_ARBETE.equals(sysselsattning.getSysselsattningstyp())) {
                Fk7263Aktivitet smittskydd = externalUtlatande.getAktivitet(Aktivitetskoder.AVSTANGNING_ENLIGT_SML_PGA_SMITTA);
                if (patient.getArbetsuppgifter().isEmpty()) {
                    if (smittskydd == null) {
                        addValidationError("Field 8a(1): 'beskrivning aktuella arbetsuppgifter' is mandatory when 'nuvarande arbete' is checked.");
                    }
                } else {
                    String typAvArbetsuppgift = patient.getArbetsuppgifter().get(0).getTypAvArbetsuppgift();
                    if (typAvArbetsuppgift == null || (smittskydd == null && StringUtils.isEmpty(typAvArbetsuppgift))) {
                        addValidationError("Field 8a(1): 'beskrivning aktuella arbetsuppgifter' is mandatory when 'nuvarande arbete' is checked.");
                    }
                }
            }
        }
    }

    private void validateDiagnose() {

        Fk7263Observation huvudDiagnos = externalUtlatande.findObservationByKategori(ObservationsKoder.DIAGNOS);
        Fk7263Aktivitet smittskydd = externalUtlatande.getAktivitet(Aktivitetskoder.AVSTANGNING_ENLIGT_SML_PGA_SMITTA);

        if (huvudDiagnos == null || huvudDiagnos.getObservationskod() == null
                || StringUtils.isEmpty(huvudDiagnos.getObservationskod().getCode())) {

            // No valid huvudDiagnosis found: This is an error unless smittskydd is set
            if (smittskydd == null) {
                addValidationError("Field 2: Missing diagnose code");
            }
        } else if (StringUtils.isEmpty(huvudDiagnos.getObservationskod().getCodeSystemName())
                || !VALID_DIAGNOSE_CODE_SYSTEMS.contains(huvudDiagnos.getObservationskod().getCodeSystemName())) {
            addValidationError(String.format("Field 2: Invalid diagnose code system! Should be one of %s",
                    Strings.join(" or ", VALID_DIAGNOSE_CODE_SYSTEMS)));
        }

    }

    private void validateVardkontakttid() {
        for (Vardkontakt vardkontakt : externalUtlatande.getVardkontakter()) {
            if (vardkontakt.getVardkontaktstid() != null) {
                if (vardkontakt.getVardkontaktstid().getFrom() == null) {
                    addValidationError("Field 4: Missing date for 'vårdkontakt'");
                }
                if (vardkontakt.getVardkontaktstid().getTom() == null) {
                    addValidationError("Field 4: Missing date for 'vårdkontakt'");
                }
            }
        }
    }

}
