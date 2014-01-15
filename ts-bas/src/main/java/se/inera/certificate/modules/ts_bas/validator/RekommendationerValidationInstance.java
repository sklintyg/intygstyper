package se.inera.certificate.modules.ts_bas.validator;

import java.util.Iterator;
import java.util.List;

import se.inera.certificate.model.Kod;
import se.inera.certificate.modules.ts_bas.model.codes.CodeConverter;
import se.inera.certificate.modules.ts_bas.model.codes.RekommendationVardeKod;
import se.inera.certificate.modules.ts_bas.model.codes.RekommendationsKod;
import se.inera.certificate.modules.ts_bas.model.external.Rekommendation;

public class RekommendationerValidationInstance extends ExternalValidatorInstance {

    private static final Kod REK_REK8 = CodeConverter.toKod(RekommendationsKod.PATIENT_UPPFYLLER_KRAV_FOR);
    private static final Kod REK_REK9 = CodeConverter.toKod(RekommendationsKod.PATIENT_BOR_UNDESOKAS_AV_SPECIALIST);

    private final List<Rekommendation> rekommendationer;

    public RekommendationerValidationInstance(ExternalValidatorInstance prototype, List<Rekommendation> rekommendationer) {
        super(prototype.validationErrors, prototype.context);
        this.rekommendationer = rekommendationer;
    }

    public void validateRekommendationer() {
        // Check that rekommendationer are defined as many times as expected
        Iterable<Kod> kodList = new RekommendationerIterable(rekommendationer);
        assertKodCountBetween(kodList, REK_REK8, 1, 1, "rekommendationer");
        assertKodCountBetween(kodList, REK_REK9, 0, 1, "rekommendationer");

        // Check that each defined rekommendation is valid in itself
        for (Rekommendation rekommendation : rekommendationer) {
            String entity = "rekommendation" + getDisplayCode(rekommendation.getRekommendationskod());
            // Check that the current aktivitet is known
            assertKodInEnum(rekommendation.getRekommendationskod(), RekommendationsKod.class, entity
                    + ".rekommendationsKod");

            if (rekommendation.getRekommendationskod().equals(REK_REK8)) {
                // Patienten uppfyller kraven för (REK8)
                assertNull(rekommendation.getBeskrivning(), entity + ".beskrivning");
                for (Kod kod : rekommendation.getVarde()) {
                    assertKodInEnum(kod, RekommendationVardeKod.class, entity + ".varde");
                }

            } else if (rekommendation.getRekommendationskod().equals(REK_REK9)) {
                // Patienten bör före ärendets avgörande undersökas av läkare med specialistkompetens i (REK9)
                assertNotEmpty(rekommendation.getBeskrivning(), entity + ".beskrivning");
                assertNull(rekommendation.getVarde(), entity + ".varde");
            }
        }
    }

    /**
     * An {@link Iterable} emitting the {@link Kod}er of an underlying list of {@link Rekommendation}er.
     */
    private static class RekommendationerIterable implements Iterable<Kod> {
        private final List<Rekommendation> rekommendationer;

        public RekommendationerIterable(List<Rekommendation> rekommendationer) {
            this.rekommendationer = rekommendationer;
        }

        @Override
        public Iterator<Kod> iterator() {
            final Iterator<Rekommendation> iter = rekommendationer.iterator();
            return new Iterator<Kod>() {
                @Override
                public boolean hasNext() {
                    return iter.hasNext();
                }

                @Override
                public Kod next() {
                    return iter.next().getRekommendationskod();
                }

                @Override
                public void remove() {
                }
            };
        }
    }
}
