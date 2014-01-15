package se.inera.certificate.modules.ts_bas.validator;

import java.util.Iterator;
import java.util.List;

import se.inera.certificate.model.Id;
import se.inera.certificate.model.Kod;
import se.inera.certificate.modules.ts_bas.model.codes.CodeConverter;
import se.inera.certificate.modules.ts_bas.model.codes.ObservationsKod;
import se.inera.certificate.modules.ts_bas.model.external.Observation;

public class ObservationerValidationInstance extends ExternalValidatorInstance {

    private static final Kod OBS_H53_4 = CodeConverter.toKod(ObservationsKod.SYNFALTSDEFEKTER);
    private static final Kod OBS_H53_6 = CodeConverter.toKod(ObservationsKod.NATTBLINDHET);
    private static final Kod OBS_OBS1 = CodeConverter.toKod(ObservationsKod.PROGRESIV_OGONSJUKDOM);
    private static final Kod OBS_OBS2 = CodeConverter.toKod(ObservationsKod.ANFALL_BALANSRUBBNING_YRSEL);
    private static final Kod OBS_OBS3 = CodeConverter.toKod(ObservationsKod.SVARIGHET_SAMTAL_4M);
    private static final Kod OBS_OBS4 = CodeConverter.toKod(ObservationsKod.FORSAMRAD_RORLIGHET_FRAMFORA_FORDON);
    private static final Kod OBS_OBS5 = CodeConverter.toKod(ObservationsKod.FORSAMRAD_RORLIGHET_HJALPA_PASSAGERARE);
    private static final Kod OBS_OBS6 = CodeConverter.toKod(ObservationsKod.HJART_KARLSJUKDOM_TRAFIKSAKERHETSRISK);
    private static final Kod OBS_OBS7 = CodeConverter.toKod(ObservationsKod.RISKFAKTORER_STROKE);
    private static final Kod OBS_OBS8 = CodeConverter.toKod(ObservationsKod.TECKEN_PA_HJARNSKADA);
    private static final Kod OBS_OBS9 = CodeConverter.toKod(ObservationsKod.DIABETIKER_KOSTBEHANDLING);
    private static final Kod OBS_OBS10 = CodeConverter.toKod(ObservationsKod.NEDSATT_NJURFUNKTION_TRAFIKSAKERHETSRISK);
    private static final Kod OBS_OBS11 = CodeConverter.toKod(ObservationsKod.SVIKTANDE_KOGNITIV_FUNKTION);
    private static final Kod OBS_OBS12 = CodeConverter.toKod(ObservationsKod.SOMN_VAKENHETSSTORNING);
    private static final Kod OBS_OBS13 = CodeConverter.toKod(ObservationsKod.TECKEN_PA_MISSBRUK);
    private static final Kod OBS_OBS15 = CodeConverter.toKod(ObservationsKod.LAKEMEDELSANVANDNING_TRAFIKSAKERHETSRISK);
    private static final Kod OBS_OBS16 = CodeConverter.toKod(ObservationsKod.PSYKISK_SJUKDOM);
    private static final Kod OBS_OBS17 = CodeConverter.toKod(ObservationsKod.ADHD_DAMP_MM);
    private static final Kod OBS_OBS18 = CodeConverter.toKod(ObservationsKod.STADIGVARANDE_MEDICINERING);

    private static final Kod OBS_170746006 = CodeConverter.toKod(ObservationsKod.DIABETIKER_INSULINBEHANDLING);
    private static final Kod OBS_170746002 = CodeConverter.toKod(ObservationsKod.DIABETIKER_TABLETTBEHANDLING);
    private static final Kod OBS_129104009 = CodeConverter.toKod(ObservationsKod.PSYKISK_UTVECKLINGSSTORNING);
    private static final Kod OBS_73211009 = CodeConverter.toKod(ObservationsKod.HAR_DIABETES);
    private static final Kod OBS_420050001 = CodeConverter.toKod(ObservationsKod.EJ_KORRIGERAD_SYNSKARPA);
    private static final Kod OBS_397535007 = CodeConverter.toKod(ObservationsKod.KORRIGERAD_SYNSKARPA);
    private static final Kod OBS_285049007 = CodeConverter.toKod(ObservationsKod.KONTAKTLINSER);

    private static final Kod OBS_H53_2 = CodeConverter.toKod(ObservationsKod.DIPLOPI);
    private static final Kod OBS_H55_9 = CodeConverter.toKod(ObservationsKod.NYSTAGMUS_MM);
    private static final Kod OBS_E10 = CodeConverter.toKod(ObservationsKod.DIABETES_TYP_1);
    private static final Kod OBS_E11 = CodeConverter.toKod(ObservationsKod.DIABETES_TYP_2);
    private static final Kod OBS_G40_9 = CodeConverter.toKod(ObservationsKod.EPILEPSI);

    private final List<Observation> observationer;

    public ObservationerValidationInstance(ExternalValidatorInstance prototype, List<Observation> observationer) {
        super(prototype.validationErrors, prototype.context);
        this.observationer = observationer;
    }

    /**
     * Validate all the observations. Make sure required observations are present, and that the correct number is
     * present.
     * 
     */
    public void validateObservationer() {

        Iterable<Kod> kodList = new ObservationerIterable(observationer);

        assertKodCountBetween(kodList, OBS_H53_4, 1, 1, "observationer");
        assertKodCountBetween(kodList, OBS_H53_6, 1, 1, "observationer");
        assertKodCountBetween(kodList, OBS_H53_2, 1, 1, "observationer");
        assertKodCountBetween(kodList, OBS_H55_9, 1, 1, "observationer");

        assertKodCountBetween(kodList, OBS_OBS1, 1, 1, "observationer");
        assertKodCountBetween(kodList, OBS_OBS2, 1, 1, "observationer");
        assertKodCountBetween(kodList, OBS_OBS3, 0, 1, "observationer");
        assertKodCountBetween(kodList, OBS_OBS4, 1, 1, "observationer");
        assertKodCountBetween(kodList, OBS_OBS5, 0, 1, "observationer");
        assertKodCountBetween(kodList, OBS_OBS6, 1, 1, "observationer");
        assertKodCountBetween(kodList, OBS_OBS7, 1, 1, "observationer");
        assertKodCountBetween(kodList, OBS_OBS8, 1, 1, "observationer");
        assertKodCountBetween(kodList, OBS_OBS9, 0, 1, "observationer");
        assertKodCountBetween(kodList, OBS_OBS10, 1, 1, "observationer");
        assertKodCountBetween(kodList, OBS_OBS11, 1, 1, "observationer");
        assertKodCountBetween(kodList, OBS_OBS12, 1, 1, "observationer");
        assertKodCountBetween(kodList, OBS_OBS13, 1, 1, "observationer");
        assertKodCountBetween(kodList, OBS_OBS15, 1, 1, "observationer");
        assertKodCountBetween(kodList, OBS_OBS16, 1, 1, "observationer");
        assertKodCountBetween(kodList, OBS_OBS17, 1, 1, "observationer");
        assertKodCountBetween(kodList, OBS_OBS18, 1, 1, "observationer");

        assertKodCountBetween(kodList, OBS_129104009, 1, 1, "observationer");
        assertKodCountBetween(kodList, OBS_73211009, 0, 1, "observationer");
        assertKodCountBetween(kodList, OBS_285049007, 2, 2, "observationer");
        assertKodCountBetween(kodList, OBS_397535007, 0, 3, "observationer");
        assertKodCountBetween(kodList, OBS_420050001, 3, 3, "observationer");

        assertKodCountBetween(kodList, OBS_G40_9, 1, 1, "observationer");
        assertKodCountBetween(kodList, OBS_OBS15, 1, 1, "observationer");
        // TODO: Count all other observations here

        for (Observation observation : observationer) {
            String entity = "Observation " + getDisplayCode(observation.getObservationskod());
            assertKodInEnum(observation.getObservationskod(), ObservationsKod.class, entity + ".observationsKod");

            if (observation.getObservationskod().equals(OBS_H53_4)) {
                assertNotNull(observation.getForekomst(), entity + ".förekomst");
                assertNotNull(observation.getId(), entity + ".observationsid");

                assertNull(observation.getBeskrivning(), entity + ".beskrivning");
                assertNull(observation.getLateralitet(), entity + ".lateralitet");
                assertNull(observation.getObservationskategori(), entity + ".Observationskategori");
                assertNull(observation.getObservationsperiod(), entity + ".observationsperiod");
                assertNull(observation.getVarde(), entity + ".varde");

            } else if (observation.getObservationskod().equals(OBS_H53_6)) {
                assertNotNull(observation.getForekomst(), entity + ".förekomst");

                assertNull(observation.getId(), entity + ".observationsid");
                assertNull(observation.getBeskrivning(), entity + ".beskrivning");
                assertNull(observation.getLateralitet(), entity + ".lateralitet");
                assertNull(observation.getObservationskategori(), entity + ".Observationskategori");
                assertNull(observation.getObservationsperiod(), entity + ".observationsperiod");
                assertNull(observation.getVarde(), entity + ".varde");

            } else if (observation.getObservationskod().equals(OBS_285049007)) {
                assertNotNull(observation.getForekomst(), entity + ".förekomst");
                assertNotNull(observation.getLateralitet(), entity + ".lateralitet");

                assertNull(observation.getId(), entity + ".observationsid");
                assertNull(observation.getBeskrivning(), entity + ".beskrivning");
                assertNull(observation.getObservationskategori(), entity + ".Observationskategori");
                assertNull(observation.getObservationsperiod(), entity + ".observationsperiod");
                assertNull(observation.getVarde(), entity + ".varde");

            } else if (observation.getObservationskod().equals(OBS_397535007)) {
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

            } else if (observation.getObservationskod().equals(OBS_OBS1)) {
                assertNotNull(observation.getForekomst(), entity + ".förekomst");

                assertNull(observation.getId(), entity + ".observationsid");
                assertNull(observation.getBeskrivning(), entity + ".beskrivning");
                assertNull(observation.getLateralitet(), entity + ".lateralitet");
                assertNull(observation.getObservationskategori(), entity + ".Observationskategori");
                assertNull(observation.getObservationsperiod(), entity + ".observationsperiod");
                assertNull(observation.getVarde(), entity + ".varde");

            } else if (observation.getObservationskod().equals(OBS_OBS2)) {
                assertNotNull(observation.getForekomst(), entity + ".förekomst");

                assertNull(observation.getId(), entity + ".observationsid");
                assertNull(observation.getBeskrivning(), entity + ".beskrivning");
                assertNull(observation.getLateralitet(), entity + ".lateralitet");
                assertNull(observation.getObservationskategori(), entity + ".Observationskategori");
                assertNull(observation.getObservationsperiod(), entity + ".observationsperiod");
                assertNull(observation.getVarde(), entity + ".varde");

            } else if (observation.getObservationskod().equals(OBS_OBS3)) {
                assertNotNull(observation.getForekomst(), entity + ".förekomst");

                assertNull(observation.getId(), entity + ".observationsid");
                assertNull(observation.getBeskrivning(), entity + ".beskrivning");
                assertNull(observation.getLateralitet(), entity + ".lateralitet");
                assertNull(observation.getObservationskategori(), entity + ".Observationskategori");
                assertNull(observation.getObservationsperiod(), entity + ".observationsperiod");
                assertNull(observation.getVarde(), entity + ".varde");

            } else if (observation.getObservationskod().equals(OBS_OBS4)) {
                // Check OBS4 (Sjukdom som påverkar och medför att fordon inte kan köras på trafiksäkert sätt)
                if (assertNotNull(observation.getForekomst(), entity + ".förekomst").success()) {
                    if (observation.getForekomst()) {
                        assertNotNull(observation.getBeskrivning(), entity + ".beskrivning");
                    }
                }
                assertNull(observation.getId(), entity + ".observationsid");
                assertNull(observation.getLateralitet(), entity + ".lateralitet");
                assertNull(observation.getObservationskategori(), entity + ".Observationskategori");
                assertNull(observation.getObservationsperiod(), entity + ".observationsperiod");
                assertNull(observation.getVarde(), entity + ".varde");

            } else if (observation.getObservationskod().equals(OBS_OBS5)) {
                assertNotNull(observation.getForekomst(), entity + ".förekomst");

                assertNull(observation.getId(), entity + ".observationsid");
                assertNull(observation.getBeskrivning(), entity + ".beskrivning");
                assertNull(observation.getLateralitet(), entity + ".lateralitet");
                assertNull(observation.getObservationskategori(), entity + ".Observationskategori");
                assertNull(observation.getObservationsperiod(), entity + ".observationsperiod");
                assertNull(observation.getVarde(), entity + ".varde");

            } else if (observation.getObservationskod().equals(OBS_OBS6)) {
                assertNotNull(observation.getForekomst(), entity + ".förekomst");
                
                assertNull(observation.getBeskrivning(), entity + ".beskrivning");
                assertNull(observation.getId(), entity + ".observationsid");
                assertNull(observation.getLateralitet(), entity + ".lateralitet");
                assertNull(observation.getObservationskategori(), entity + ".Observationskategori");
                assertNull(observation.getObservationsperiod(), entity + ".observationsperiod");
                assertNull(observation.getVarde(), entity + ".varde");
                
            } else if (observation.getObservationskod().equals(OBS_OBS7)) {
                // (Riskfaktor för stroke)
                if (assertNotNull(observation.getForekomst(), entity + ".förekomst").success()) {
                    if (observation.getForekomst()) {
                        assertNotNull(observation.getBeskrivning(), entity + ".beskrivning");
                    }
                }
                assertNull(observation.getId(), entity + ".observationsid");
                assertNull(observation.getLateralitet(), entity + ".lateralitet");
                assertNull(observation.getObservationskategori(), entity + ".Observationskategori");
                assertNull(observation.getObservationsperiod(), entity + ".observationsperiod");
                assertNull(observation.getVarde(), entity + ".varde");

            } else if (observation.getObservationskod().equals(OBS_OBS8)) {
                assertNotNull(observation.getForekomst(), entity + ".förekomst");

                assertNull(observation.getId(), entity + ".observationsid");
                assertNull(observation.getBeskrivning(), entity + ".beskrivning");
                assertNull(observation.getLateralitet(), entity + ".lateralitet");
                assertNull(observation.getObservationskategori(), entity + ".Observationskategori");
                assertNull(observation.getObservationsperiod(), entity + ".observationsperiod");
                assertNull(observation.getVarde(), entity + ".varde");

            }
            // Diabetes treatment related
            else if (observation.getObservationskod().equals(OBS_OBS9)) {
                assertNotNull(observation.getForekomst(), entity + ".förekomst");

                assertNull(observation.getId(), entity + ".observationsid");
                assertNull(observation.getBeskrivning(), entity + ".beskrivning");
                assertNull(observation.getLateralitet(), entity + ".lateralitet");
                assertNull(observation.getObservationskategori(), entity + ".Observationskategori");
                assertNull(observation.getObservationsperiod(), entity + ".observationsperiod");
                assertNull(observation.getVarde(), entity + ".varde");

            } else if (observation.getObservationskod().equals(OBS_170746002)) {
                assertNotNull(observation.getForekomst(), entity + ".förekomst");

                assertNull(observation.getId(), entity + ".observationsid");
                assertNull(observation.getBeskrivning(), entity + ".beskrivning");
                assertNull(observation.getLateralitet(), entity + ".lateralitet");
                assertNull(observation.getObservationskategori(), entity + ".Observationskategori");
                assertNull(observation.getObservationsperiod(), entity + ".observationsperiod");
                assertNull(observation.getVarde(), entity + ".varde");

            } else if (observation.getObservationskod().equals(OBS_170746006)) {
                assertNotNull(observation.getForekomst(), entity + ".förekomst");

                assertNull(observation.getId(), entity + ".observationsid");
                assertNull(observation.getBeskrivning(), entity + ".beskrivning");
                assertNull(observation.getLateralitet(), entity + ".lateralitet");
                assertNull(observation.getObservationskategori(), entity + ".Observationskategori");
                assertNull(observation.getObservationsperiod(), entity + ".observationsperiod");
                assertNull(observation.getVarde(), entity + ".varde");

            }
            // Diabetes related end
            else if (observation.getObservationskod().equals(OBS_OBS10)) {
                assertNotNull(observation.getForekomst(), entity + ".förekomst");

                assertNull(observation.getId(), entity + ".observationsid");
                assertNull(observation.getBeskrivning(), entity + ".beskrivning");
                assertNull(observation.getLateralitet(), entity + ".lateralitet");
                assertNull(observation.getObservationskategori(), entity + ".Observationskategori");
                assertNull(observation.getObservationsperiod(), entity + ".observationsperiod");
                assertNull(observation.getVarde(), entity + ".varde");

            } else if (observation.getObservationskod().equals(OBS_OBS11)) {
                assertNotNull(observation.getForekomst(), entity + ".förekomst");

                assertNull(observation.getId(), entity + ".observationsid");
                assertNull(observation.getBeskrivning(), entity + ".beskrivning");
                assertNull(observation.getLateralitet(), entity + ".lateralitet");
                assertNull(observation.getObservationskategori(), entity + ".Observationskategori");
                assertNull(observation.getObservationsperiod(), entity + ".observationsperiod");
                assertNull(observation.getVarde(), entity + ".varde");

            } else if (observation.getObservationskod().equals(OBS_OBS12)) {
                assertNotNull(observation.getForekomst(), entity + ".förekomst");

                assertNull(observation.getId(), entity + ".observationsid");
                assertNull(observation.getBeskrivning(), entity + ".beskrivning");
                assertNull(observation.getLateralitet(), entity + ".lateralitet");
                assertNull(observation.getObservationskategori(), entity + ".Observationskategori");
                assertNull(observation.getObservationsperiod(), entity + ".observationsperiod");
                assertNull(observation.getVarde(), entity + ".varde");

            } else if (observation.getObservationskod().equals(OBS_OBS13)) {
                assertNotNull(observation.getForekomst(), entity + ".förekomst");

                assertNull(observation.getId(), entity + ".observationsid");
                assertNull(observation.getBeskrivning(), entity + ".beskrivning");
                assertNull(observation.getLateralitet(), entity + ".lateralitet");
                assertNull(observation.getObservationskategori(), entity + ".Observationskategori");
                assertNull(observation.getObservationsperiod(), entity + ".observationsperiod");
                assertNull(observation.getVarde(), entity + ".varde");

            } else if (observation.getObservationskod().equals(OBS_G40_9)) {
                // Check G40_9 (Epileptiskt anfall eller annan medvetandestörning)
                if (assertNotNull(observation.getForekomst(), entity + ".förekomst").success()) {
                    if (observation.getForekomst()) {
                        assertNotNull(observation.getBeskrivning(), entity + ".beskrivning");
                    }
                }
                assertNull(observation.getId(), entity + ".observationsid");
                assertNull(observation.getLateralitet(), entity + ".lateralitet");
                assertNull(observation.getObservationskategori(), entity + ".Observationskategori");
                assertNull(observation.getObservationsperiod(), entity + ".observationsperiod");
                assertNull(observation.getVarde(), entity + ".varde");


            } else if (observation.getObservationskod().equals(OBS_OBS15)) {
                //Check OBS15 (Regelbundet ordinerat bruk av läkemedel)
                if (assertNotNull(observation.getForekomst(), entity + ".förekomst").success()) {
                    if (observation.getForekomst()) {
                        assertNotNull(observation.getBeskrivning(), entity + ".beskrivning");
                    }
                }
                assertNull(observation.getId(), entity + ".observationsid");
                assertNull(observation.getBeskrivning(), entity + ".beskrivning");
                assertNull(observation.getLateralitet(), entity + ".lateralitet");
                assertNull(observation.getObservationskategori(), entity + ".Observationskategori");
                assertNull(observation.getObservationsperiod(), entity + ".observationsperiod");
                assertNull(observation.getVarde(), entity + ".varde");

            }
            // TODO: Check all other observations here
        }

        // If the diabetes flag is set, assert that an observation of the type of diabetes is supplied
        if (context.isDiabetesContext()) {
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
                Observation treatmentKost = getObservationWithKod(OBS_OBS9);
                if (treatmentTablett == null && treatmentInsulin == null && treatmentKost == null) {
                    validationError("At least one treatment for diabetes type2 must be specified");
                }
            }
        }

        // If the persontransport flag is set, assert required observations are supplied
        if (context.isPersontransportContext()) {
            Observation rorelseformaga = getObservationWithKod(OBS_OBS5);
            if (rorelseformaga == null) {
                validationError("observation OBS5 must be present when intygAvser contains any of [D1, D1E, D, DE, or TAXI]");
            }

            Observation samtal4meter = getObservationWithKod(OBS_OBS3);
            if (samtal4meter == null) {
                validationError("observation OBS3 must be present when intygAvser contains any of [D1, D1E, D, DE, or TAXI]");
            }
        }
    }

    /**
     * Returns an Observation based on the specified Kod, or <code>null</code> if none where found.
     * 
     * @param observationskod
     *            Find an observation with this {@link Kod}
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
     * @param observationskod
     *            Find an observation with this {@link Id}
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
