package se.inera.certificate.modules.ts_bas.validator;

import java.util.Iterator;
import java.util.List;

import se.inera.certificate.model.Kod;
import se.inera.certificate.modules.ts_bas.model.codes.CodeConverter;
import se.inera.certificate.modules.ts_bas.model.codes.ObservationsKod;
import se.inera.certificate.modules.ts_bas.model.external.Observation;

public class ObservationerValidationInstance extends ExternalValidatorInstance {

    private static final Kod OBS_H53_4 = CodeConverter.toKod(ObservationsKod.SYNFALTSDEFEKTER);
    private static final Kod OBS_H53_6 = CodeConverter.toKod(ObservationsKod.NATTBLINDHET);
    private static final Kod OBS_OBS1 = CodeConverter.toKod(ObservationsKod.PROGRESIV_OGONSJUKDOM);
    private static final Kod OBS_H53_2 = CodeConverter.toKod(ObservationsKod.DIPLOPI);
    private static final Kod OBS_H55_9 = CodeConverter.toKod(ObservationsKod.NYSTAGMUS_MM);

    public ObservationerValidationInstance(List<String> validationErrors) {
        super(validationErrors);
    }

    public void validateObservationer(List<Observation> observationer) {
        Iterable<Kod> kodList = new ObservationerIterable(observationer);
        assertKodCountBetween(kodList, OBS_H53_4, 1, 1, "observationer");
        assertKodCountBetween(kodList, OBS_H53_6, 1, 1, "observationer");
        assertKodCountBetween(kodList, OBS_OBS1, 1, 1, "observationer");
        assertKodCountBetween(kodList, OBS_H53_2, 1, 1, "observationer");
        assertKodCountBetween(kodList, OBS_H55_9, 1, 1, "observationer");
        // TODO: Count all other observations here

        for (Observation observation : observationer) {
            String entity = "rekommendation" + getDisplayCode(observation.getObservationskod());
            assertKodInEnum(observation.getObservationskod(), ObservationsKod.class, entity
                    + ".rekommendationsKod");

            if (observation.getObservationskod().equals(OBS_H53_4)) {
                // TODO: Add checks for H53.4 here

            } else if (observation.getObservationskod().equals(OBS_H53_6)) {
                // TODO: Add checks for H53.6 here
            }
            // TODO: Check all other observations here
        }
    }

    private static class ObservationerIterable implements Iterable<Kod> {
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
