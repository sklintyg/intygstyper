package se.inera.certificate.modules.ts_diabetes.validator.external;

import java.util.Iterator;
import java.util.List;

import se.inera.certificate.model.Kod;
import se.inera.certificate.model.common.codes.CodeConverter;
import se.inera.certificate.modules.ts_diabetes.model.codes.RekommendationVardeKod;
import se.inera.certificate.modules.ts_diabetes.model.codes.RekommendationsKod;
import se.inera.certificate.modules.ts_diabetes.model.external.Rekommendation;

public class RekommendationerValidationInstance extends ExternalValidatorInstance {

    private static final Kod REK_REK8 = CodeConverter.toKod(RekommendationsKod.PATIENT_UPPFYLLER_KRAV_FOR);
    private static final Kod REK_REK9 = CodeConverter.toKod(RekommendationsKod.PATIENT_BOR_UNDESOKAS_AV_SPECIALIST);
    private static final Kod REK_REK10 = CodeConverter
            .toKod(RekommendationsKod.LAMPLIGHET_INNEHA_BEHORIGHET_TILL_KORNINGAR_OCH_ARBETSFORMER);

    private final List<Rekommendation> rekommendationer;

    public RekommendationerValidationInstance(ExternalValidatorInstance prototype, List<Rekommendation> rekommendationer) {
        super(prototype.getValidationErrors(), prototype.getContext());
        this.rekommendationer = rekommendationer;
    }

    public void validateRekommendationer() {
        // Check that rekommendationer are defined as many times as expected
        Iterable<Kod> kodList = new RekommendationerIterable(rekommendationer);
        assertKodCountBetween(kodList, REK_REK8, 1, 1, "rekommendationer");
        assertKodCountBetween(kodList, REK_REK9, 0, 1, "rekommendationer");
        assertKodCountBetween(kodList, REK_REK10, 0, 1, "rekommendationer");

        for (Rekommendation rekommendation : rekommendationer) {

            String entity = "rekommendation" + getDisplayCode(rekommendation.getRekommendationskod());

            assertKodInEnum(rekommendation.getRekommendationskod(), RekommendationsKod.class, entity
                    + ".rekommendationsKod");

            if (rekommendation.getRekommendationskod().equals(REK_REK8)) {
                assertNull(rekommendation.getBeskrivning(), entity + ".beskrivning");
                for (Object varde : rekommendation.getVarde()) {
                    if (varde instanceof Kod) {
                        assertKodInEnum((Kod) varde, RekommendationVardeKod.class, entity + ".varde");
                    } else {
                        validationError(entity + ".varde must be of type Kod");
                    }
                }

            } else if (rekommendation.getRekommendationskod().equals(REK_REK9)) {
                assertNotEmpty(rekommendation.getBeskrivning(), entity + ".beskrivning");
                if (!rekommendation.getVarde().isEmpty()) {
                    validationError(entity + ".varde must be empty");
                }

            } else if (rekommendation.getRekommendationskod().equals(REK_REK10)) {
                assertNull(rekommendation.getBeskrivning(), entity + ".beskrivning");
                Object varde = getSingleItem(rekommendation.getVarde());
                if (varde == null) {
                    validationError(entity + ".varde must be non-null");
                } else if (!(varde instanceof Boolean)) {
                    validationError(entity + ".varde must be of type Boolean");
                }
            }
        }

        if (getContext().isHogrePersontransportContext()) {
            Rekommendation lamplighet = getRekommendationWithKod(REK_REK10);
            if (lamplighet == null) {
                validationError("Rekommendation REK10 (Lämplighet att inneha behörighet ...) must be present when intygAvser any of [C1, C1E, C, CE, D1, D1E, D, DE or TAXI]");
            }
        }

    }

    /**
     * Returns an Rekommendation based on the specified Kod, or <code>null</code> if none where found.
     *
     * @param rekommendationskod Find a rekommendation with this {@link Kod}
     * @return an {@link Rekommendation} if it is found, or null otherwise
     */
    private Rekommendation getRekommendationWithKod(Kod rekommendationskod) {
        for (Rekommendation rekommendation : rekommendationer) {
            if (rekommendationskod.equals(rekommendation.getRekommendationskod())) {
                return rekommendation;
            }
        }

        return null;
    }

    /**
     * Gets a single Object from a list of objects, returns null if the size of the list is anything other than 1.
     *
     * @param objects List of Objects
     * @return Object
     */
    private Object getSingleItem(List<?> objects) {
        Object ret = objects.size() == 1 ? objects.get(0) : null;
        return ret;
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
