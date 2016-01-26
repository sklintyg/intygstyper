package se.inera.certificate.modules.sjukpenning_utokad.model.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.auto.value.AutoValue;

import se.inera.intyg.common.support.model.InternalLocalDateInterval;

@AutoValue
public abstract class Sjukskrivning {
    Sjukskrivning() {
    }

    @JsonCreator
    public static Sjukskrivning create(@JsonProperty("sjukskrivningsgrad") SjukskrivningsGrad sjukskrivningsgrad,
            @JsonProperty("period") InternalLocalDateInterval period){
        return new AutoValue_Sjukskrivning(sjukskrivningsgrad, period);
    }

    public abstract SjukskrivningsGrad getSjukskrivningsgrad();
    
    public abstract InternalLocalDateInterval getPeriod();

    public enum SjukskrivningsGrad {
        /**
         * Helt nedsatt (id 1).
         */
        HELT_NEDSATT(1, "Helt nedsatt"),
        /**
         * Nedsatt till 3/4 (id 2).
         */
        NEDSATT_3_4(2, "Nedsatt med 3/4"),
        /**
         * Nedsatt till hälften (id 3).
         */
        NEDSATT_HALFTEN(3, "Nedsatt med hälften"),
        /**
         * Nedsatt till 1/4 (id 4).
         */
        NEDSATT_1_4(4,"Nedsatt med 1/4");

        private final int id;
        private final String label;

        SjukskrivningsGrad(int id, String label) {
            this.id = id;
            this.label = label;
        }

        @JsonValue
        public int getId() {
            return id;
        }

        public String getLabel() {
            return label;
        }

        @JsonCreator
        public static SjukskrivningsGrad fromId(@JsonProperty("id") int id) {
            for (SjukskrivningsGrad typ : values()) {
                if (typ.id == id) {
                    return typ;
                }
            }
            throw new IllegalArgumentException();
        }
    }

}
