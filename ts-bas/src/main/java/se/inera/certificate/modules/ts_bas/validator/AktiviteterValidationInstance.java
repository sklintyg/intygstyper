package se.inera.certificate.modules.ts_bas.validator;

import java.util.Iterator;
import java.util.List;

import se.inera.certificate.model.Id;
import se.inera.certificate.model.Kod;
import se.inera.certificate.modules.ts_bas.model.codes.AktivitetKod;
import se.inera.certificate.modules.ts_bas.model.codes.CodeConverter;
import se.inera.certificate.modules.ts_bas.model.external.Aktivitet;

public class AktiviteterValidationInstance extends ExternalValidatorInstance {

    private static final Kod AKT_86944008 = CodeConverter.toKod(AktivitetKod.SYNFALTSUNDERSOKNING);
    private static final Kod AKT_AKT18 = CodeConverter.toKod(AktivitetKod.PROVNING_AV_OGATS_RORLIGHET);
    private static final Kod AKT_AKT17 = CodeConverter.toKod(AktivitetKod.UNDERSOKNING_PLUS8_KORREKTIONSGRAD);
    private static final Kod AKT_AKT15 = CodeConverter.toKod(AktivitetKod.VARDINSATS_MISSBRUK_BEROENDE);
    private static final Kod AKT_AKT14 = CodeConverter.toKod(AktivitetKod.PROVTAGNING_ALKOHO_NARKOTIKA);
    private static final Kod AKT_AKT19 = CodeConverter.toKod(AktivitetKod.VARD_PA_SJUKHUS);

    private final List<Aktivitet> aktiviteter;

    public AktiviteterValidationInstance(ExternalValidatorInstance prototype, List<Aktivitet> aktiviteter) {
        super(prototype.validationErrors, prototype.context);
        this.aktiviteter = aktiviteter;
    }

    public void validateAktiviteter() {
        Iterable<Kod> kodList = new AktiviteterIterable(aktiviteter);
        assertKodCountBetween(kodList, AKT_86944008, 1, 1, "aktiviteter");
        assertKodCountBetween(kodList, AKT_AKT18, 1, 1, "aktiviteter");
        assertKodCountBetween(kodList, AKT_AKT17, 0, 1, "aktiviteter");
        assertKodCountBetween(kodList, AKT_AKT15, 1, 1, "aktiviteter");
        assertKodCountBetween(kodList, AKT_AKT14, 0, 1, "aktiviteter");
        assertKodCountBetween(kodList, AKT_AKT19, 1, 1, "aktiviteter");

        for (Aktivitet aktivitet : aktiviteter) {
            String entity = "aktivitet" + getDisplayCode(aktivitet.getAktivitetskod());
            assertKodInEnum(aktivitet.getAktivitetskod(), AktivitetKod.class, entity + ".aktivitetsKod");

            if (aktivitet.getAktivitetskod().equals(AKT_86944008)) {
                assertNotNull(aktivitet.getId(), entity + ".aktivitetsId");
                assertNull(aktivitet.getForekomst(), entity + ".forekomst");
                assertNotNull(aktivitet.getMetod(), entity + ".metod");
                assertNull(aktivitet.getAktivitetstid(), entity + ".aktivitetstid");
                assertNull(aktivitet.getPlats(), entity + ".plats");
                assertNull(aktivitet.getBeskrivning(), entity + ".beskrivning");

            } else if (aktivitet.getAktivitetskod().equals(AKT_AKT18)) {
                assertNotNull(aktivitet.getId(), entity + ".aktivitetsId");
                assertNull(aktivitet.getForekomst(), entity + ".forekomst");
                assertNull(aktivitet.getMetod(), entity + ".metod");
                assertNull(aktivitet.getAktivitetstid(), entity + ".aktivitetstid");
                assertNull(aktivitet.getPlats(), entity + ".plats");
                assertNull(aktivitet.getBeskrivning(), entity + ".beskrivning");

            } else if (aktivitet.getAktivitetskod().equals(AKT_AKT17)) {
                assertNull(aktivitet.getId(), entity + ".aktivitetsId");
                assertNotNull(aktivitet.getForekomst(), entity + ".forekomst");
                assertNull(aktivitet.getMetod(), entity + ".metod");
                assertNull(aktivitet.getAktivitetstid(), entity + ".aktivitetstid");
                assertNull(aktivitet.getPlats(), entity + ".plats");
                assertNull(aktivitet.getBeskrivning(), entity + ".beskrivning");

            } else if (aktivitet.getAktivitetskod().equals(AKT_AKT15)) {
                assertNull(aktivitet.getId(), entity + ".aktivitetsId");
                assertNotNull(aktivitet.getForekomst(), entity + ".forekomst");
                assertNull(aktivitet.getMetod(), entity + ".metod");
                assertNull(aktivitet.getAktivitetstid(), entity + ".aktivitetstid");
                assertNull(aktivitet.getPlats(), entity + ".plats");
                assertNull(aktivitet.getBeskrivning(), entity + ".beskrivning");

            } else if (aktivitet.getAktivitetskod().equals(AKT_AKT14)) {
                assertNull(aktivitet.getId(), entity + ".aktivitetsId");
                assertNotNull(aktivitet.getForekomst(), entity + ".forekomst");
                assertNull(aktivitet.getMetod(), entity + ".metod");
                assertNull(aktivitet.getAktivitetstid(), entity + ".aktivitetstid");
                assertNull(aktivitet.getPlats(), entity + ".plats");
                assertNull(aktivitet.getBeskrivning(), entity + ".beskrivning");

            } else if (aktivitet.getAktivitetskod().equals(AKT_AKT19)) {
                assertNull(aktivitet.getId(), entity + ".aktivitetsId");
                assertNotNull(aktivitet.getForekomst(), entity + ".forekomst");

                if (aktivitet.getForekomst()) {
                    assertNotNull(aktivitet.getAktivitetstid(), entity + ".aktivitetstid");
                    assertNotNull(aktivitet.getPlats(), entity + ".plats");
                    assertNotNull(aktivitet.getBeskrivning(), entity + ".beskrivning");
                }

                assertNull(aktivitet.getMetod(), entity + ".metod");
            }
        }
    }

    public Aktivitet getAktivitetWithKod(Kod aktivitetskod) {
        for (Aktivitet aktivitet : aktiviteter) {
            if (aktivitetskod.equals(aktivitet.getAktivitetskod())) {
                return aktivitet;
            }
        }

        return null;
    }

    public Aktivitet getAktivitetWithId(Id id) {
        for (Aktivitet aktivitet : aktiviteter) {
            if (id.equals(aktivitet.getId())) {
                return aktivitet;
            }
        }

        return null;
    }

    private static class AktiviteterIterable implements Iterable<Kod> {
        private final List<Aktivitet> aktivieter;

        public AktiviteterIterable(List<Aktivitet> aktivieter) {
            this.aktivieter = aktivieter;
        }

        @Override
        public Iterator<Kod> iterator() {
            final Iterator<Aktivitet> iter = aktivieter.iterator();
            return new Iterator<Kod>() {
                @Override
                public boolean hasNext() {
                    return iter.hasNext();
                }

                @Override
                public Kod next() {
                    return iter.next().getAktivitetskod();
                }

                @Override
                public void remove() {
                }
            };
        }
    }
}
