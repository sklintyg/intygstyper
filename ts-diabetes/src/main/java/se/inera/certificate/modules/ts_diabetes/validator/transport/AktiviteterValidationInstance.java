package se.inera.certificate.modules.ts_diabetes.validator.transport;

import java.util.List;

import se.inera.certificate.ts_diabetes.model.v1.AktivitetType;

public class AktiviteterValidationInstance extends ProgrammaticTransportValidatorInstance {

    private final List<AktivitetType> aktiviteter;

    AktiviteterValidationInstance(ProgrammaticTransportValidatorInstance prototype, List<AktivitetType> aktiviteter) {
        super(prototype.getValidationErrors(), prototype.getContext());
        this.aktiviteter = aktiviteter;
    }

    public void validateAktiviteter() {
        if (getContext().isHogrePersontransportContext()) {
            AktivitetType aktivitet = getAktivitetWithKod("308113006");
            if (aktivitet == null) {
                validationError("Aktivitet AKT_308113006 (Egenkontroll av blodsocker) must be present when intygAvser contains any of [C1, C1E, C, CE, D1, D1E, D, DE or TAXI]");
            }
        }
    }

    /**
     * Returns the aktivitet with the specified kod, or <code>null</code> if none where found.
     *
     * @param aktivitetskod The kod to find.
     * @return A matching aktivitet or <code>null</code>.
     */
    public AktivitetType getAktivitetWithKod(String aktivitetskod) {
        for (AktivitetType aktivitet : aktiviteter) {
            if (aktivitetskod.equals(aktivitet.getAktivitetskod().getCode())) {
                return aktivitet;
            }
        }

        return null;
    }
}
