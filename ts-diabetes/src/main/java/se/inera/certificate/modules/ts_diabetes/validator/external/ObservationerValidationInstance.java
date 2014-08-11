package se.inera.certificate.modules.ts_diabetes.validator.external;

import java.util.Iterator;
import java.util.List;

import se.inera.certificate.model.Id;
import se.inera.certificate.model.Kod;
import se.inera.certificate.modules.ts_diabetes.model.codes.CodeConverter;
import se.inera.certificate.modules.ts_diabetes.model.codes.ObservationsKod;
import se.inera.certificate.modules.ts_diabetes.model.external.Observation;

public class ObservationerValidationInstance extends ExternalValidatorInstance {

    private static final Kod OBS_170745003 = CodeConverter.toKod(ObservationsKod.DIABETIKER_ENBART_KOST);
    private static final Kod OBS_170746006 = CodeConverter.toKod(ObservationsKod.DIABETIKER_INSULINBEHANDLING);
    private static final Kod OBS_170746002 = CodeConverter.toKod(ObservationsKod.DIABETIKER_TABLETTBEHANDLING);
    private static final Kod OBS_OBS10 = CodeConverter.toKod(ObservationsKod.DIABETIKER_ANNAN_BEHANDLING);

    private static final Kod OBS_OBS19 = CodeConverter.toKod(ObservationsKod.KUNSKAP_ATGARD_HYPOGLYKEMI);
    private static final Kod OBS_OBS20 = CodeConverter.toKod(ObservationsKod.HYPOGLYKEMIER_MED_TECKEN_PA_NEDSATT_HJARNFUNKTION);
    private static final Kod OBS_OBS21 = CodeConverter.toKod(ObservationsKod.SAKNAR_FORMAGA_KANNA_HYPOGLYKEMI);
    private static final Kod OBS_OBS22 = CodeConverter.toKod(ObservationsKod.ALLVARLIG_HYPOGLYKEMI);
    private static final Kod OBS_OBS23 = CodeConverter.toKod(ObservationsKod.ALLVARLIG_HYPOGLYKEMI_I_TRAFIKEN);
    private static final Kod OBS_OBS24 = CodeConverter.toKod(ObservationsKod.ALLVARLIG_HYPOGLYKEMI_VAKET_TILLSTAND);

    private static final Kod OBS_OBS25 = CodeConverter.toKod(ObservationsKod.SYNFALTSPROVNING_UTAN_ANMARKNING);

    private static final Kod OBS_420050001 = CodeConverter.toKod(ObservationsKod.EJ_KORRIGERAD_SYNSKARPA);
    private static final Kod OBS_397535007 = CodeConverter.toKod(ObservationsKod.KORRIGERAD_SYNSKARPA);

    private static final Kod OBS_H53_2 = CodeConverter.toKod(ObservationsKod.DIPLOPI);
    private static final Kod OBS_E10 = CodeConverter.toKod(ObservationsKod.DIABETES_TYP_1);
    private static final Kod OBS_E11 = CodeConverter.toKod(ObservationsKod.DIABETES_TYP_2);

    private final List<Observation> observationer;

    public ObservationerValidationInstance(ExternalValidatorInstance prototype, List<Observation> observationer) {
        super(prototype.getValidationErrors(), prototype.getContext());
        this.observationer = observationer;
    }

    /**
     * Validate all the observations. Make sure required observations are present, and that the correct number is
     * present.
     */
    public void validateObservationer() {

        Iterable<Kod> kodList = new ObservationerIterable(observationer);

        //Diabetestyper
        assertKodCountBetween(kodList, OBS_E10, 0, 1, "observationer");
        assertKodCountBetween(kodList, OBS_E11, 0, 1, "observationer");

        //Diabetesbehandlingar
        assertKodCountBetween(kodList, OBS_170745003, 0, 1, "observationer");
        assertKodCountBetween(kodList, OBS_170746006, 0, 1, "observationer");
        assertKodCountBetween(kodList, OBS_170746002, 0, 1, "observationer");
        assertKodCountBetween(kodList, OBS_OBS10, 0, 1, "observationer");

        //Hypoglykemi
        assertKodCountBetween(kodList, OBS_OBS19, 1, 1, "observationer");
        assertKodCountBetween(kodList, OBS_OBS20, 1, 1, "observationer");
        assertKodCountBetween(kodList, OBS_OBS21, 0, 1, "observationer");
        assertKodCountBetween(kodList, OBS_OBS22, 0, 1, "observationer");
        assertKodCountBetween(kodList, OBS_OBS23, 0, 1, "observationer");
        assertKodCountBetween(kodList, OBS_OBS24, 0, 1, "observationer");

        //Syns
        assertKodCountBetween(kodList, OBS_OBS25, 0, 1, "observationer");
        assertKodCountBetween(kodList, OBS_397535007, 0, 3, "observationer");
        assertKodCountBetween(kodList, OBS_420050001, 0, 3, "observationer");
        assertKodCountBetween(kodList, OBS_H53_2, 0, 1, "observationer");

        for (Observation observation : observationer) {
            String entity = "Observation " + getDisplayCode(observation.getObservationskod());
            assertKodInEnum(observation.getObservationskod(), ObservationsKod.class, entity + ".observationsKod");

            if (observation.getObservationskod().equals(OBS_397535007)) {
                assertNotNull(observation.getLateralitet(), entity + ".lateralitet");
                if (assertNotNull(observation.getVarde(), entity + ".varde").success()) {
                    if (observation.getVarde().get(0).getQuantity() < 0.0
                            || observation.getVarde().get(0).getQuantity() > 2.0) {
                        validationError("Varde for synskarpa med korrektion must be in the interval 0.0 <= X <= 2.0");
                    }
                }
                assertNull(observation.getId(), entity + ".observationsid");
                assertNull(observation.getBeskrivning(), entity + ".beskrivning");
                assertNull(observation.getObservationskategori(), entity + ".Observationskategori");
                assertNull(observation.getObservationsperiod(), entity + ".observationsperiod");

            } else if (observation.getObservationskod().equals(OBS_420050001)) {
                assertNotNull(observation.getLateralitet(), entity + ".lateralitet");
                if (assertNotNull(observation.getVarde(), entity + ".varde").success()) {
                    if (observation.getVarde().get(0).getQuantity() < 0.0
                            || observation.getVarde().get(0).getQuantity() > 2.0) {
                        validationError("Varde for synskarpa utan korrektion must be in the interval 0.0 <= X <= 2.0");
                    }
                }

                assertNull(observation.getForekomst(), entity + ".förekomst");
                assertNull(observation.getId(), entity + ".observationsid");
                assertNull(observation.getBeskrivning(), entity + ".beskrivning");
                assertNull(observation.getObservationskategori(), entity + ".Observationskategori");
                assertNull(observation.getObservationsperiod(), entity + ".observationsperiod");

            } else if (observation.getObservationskod().equals(OBS_OBS22)) {
                if (assertNotNull(observation.getForekomst(), entity + ".förekomst").success()) {
                    if (observation.getForekomst()) {
                        assertNotNull(observation.getBeskrivning(), entity + ".Beskrivning");
                    }
                }
                assertNull(observation.getId(), entity + ".observationsid");
                assertNull(observation.getObservationskategori(), entity + ".Observationskategori");
                assertNull(observation.getObservationsperiod(), entity + ".observationsperiod");

            } else if (observation.getObservationskod().equals(OBS_OBS23)) {
                if (assertNotNull(observation.getForekomst(), entity + ".förekomst").success()) {
                    if (observation.getForekomst()) {
                        assertNotNull(observation.getBeskrivning(), entity + ".Beskrivning");
                    }
                }
                assertNull(observation.getId(), entity + ".observationsid");
                assertNull(observation.getObservationskategori(), entity + ".Observationskategori");
                assertNull(observation.getObservationsperiod(), entity + ".observationsperiod");

            } else if (observation.getObservationskod().equals(OBS_OBS24)) {
                if (assertNotNull(observation.getForekomst(), entity + ".förekomst").success()) {
                    if (observation.getForekomst()) {
                        assertNotNull(observation.getObservationstid(), entity + ".Observationstid");
                    }
                }
                assertNull(observation.getId(), entity + ".observationsid");
                assertNull(observation.getObservationskategori(), entity + ".Observationskategori");
                assertNull(observation.getObservationsperiod(), entity + ".observationsperiod");
            }
        }

        // Check syn-related stuff when separatOgonlakarintyg is NOT set
        if (!getContext().isSeparatOgonlakarintyg()) {
            Observation utanAnmarkning = getObservationWithKod(OBS_OBS25);
            Observation utanKorrektion = getObservationWithKod(OBS_397535007);
            Observation diplopi = getObservationWithKod(OBS_H53_2);

            if (utanAnmarkning == null) {
                validationError("Observation OBS25 (Synfältsprövning utan anmärkning) must be present when no Bilaga is set");
            }

            if (utanKorrektion == null) {
                validationError("Observation 397535007 (Synskärpa utan korrektion) must be present when no Bilaga is set");
            }
            if (diplopi == null) {
                validationError("Observation H53.2 (Dubbelseende) must be present when no Bilaga is set");
            }
        }

        // If the diabetes flag is set, assert that an observation of the type of diabetes is supplied
        if (getContext().isDiabetesContext()) {
            Observation diabetesTyp1 = getObservationWithKod(OBS_E10);
            Observation diabetesTyp2 = getObservationWithKod(OBS_E11);
            if (diabetesTyp1 == null && diabetesTyp2 == null) {
                validationError("observation E10 or E11 must be present when observation 73211009 exitst");
            }

            if (diabetesTyp1 != null && diabetesTyp2 != null) {
                validationError("only one of E10 or E11 can be present at the same time");
            }

            // Also determine a treatment is specified if type2 diabetes is confirmed
            if (diabetesTyp2 != null) {
                Observation treatmentTablett = getObservationWithKod(OBS_170746002);
                Observation treatmentInsulin = getObservationWithKod(OBS_170746006);
                Observation treatmentKost = getObservationWithKod(OBS_170745003);
                if (treatmentTablett == null && treatmentInsulin == null && treatmentKost == null) {
                    validationError("At least one treatment for diabetes type2 must be specified");
                }
            }
        }

        // If the persontransport flag is set, assert required observations are supplied
        if (getContext().isHogrePersontransportContext()) {
            Observation hypoglykemiVakenTid = getObservationWithKod(OBS_OBS24);

            if (hypoglykemiVakenTid == null) {
                validationError("Observation OBS24 must be present when intygAvser contains any of [C1, C1E, C, CE, D1, D1E, D, DE or TAXI]");
            }

        }
    }

    /**
     * Returns an Observation based on the specified Kod, or <code>null</code> if none where found.
     *
     * @param observationskod Find an observation with this {@link Kod}
     * @return an {@link Observation} if it is found, or null otherwise
     */
    public Observation getObservationWithKod(Kod observationskod) {
        for (Observation observation : observationer) {
            if (observationskod.equals(observation.getObservationskod())) {
                return observation;
            }
        }

        return null;
    }

    /**
     * Returns an Observation based on the specified Id, or <code>null</code> if none where found.
     *
     * @param id Find an observation with this {@link Id}
     * @return an {@link Observation} if it is found, or null otherwise
     */
    public Observation getObservationWithId(Id id) {
        for (Observation observation : observationer) {
            if (id.equals(observation.getId())) {
                return observation;
            }
        }

        return null;
    }

    /**
     * An {@link Iterable} emitting the {@link Kod}er of an underlying list of {@link Observation}er.
     */
    protected static class ObservationerIterable implements Iterable<Kod> {
        private final List<Observation> observationer;

        public ObservationerIterable(List<Observation> observationer) {
            this.observationer = observationer;
        }

        @Override
        public Iterator<Kod> iterator() {
            final Iterator<Observation> iter = observationer.iterator();
            return new Iterator<Kod>() {
                @Override
                public boolean hasNext() {
                    return iter.hasNext();
                }

                @Override
                public Kod next() {
                    return iter.next().getObservationskod();
                }

                @Override
                public void remove() {
                }
            };
        }
    }
}
