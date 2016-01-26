package se.inera.certificate.modules.sjukpenning_utokad.model.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Prognos {
    Prognos() {
    }

    @JsonCreator
    public static Prognos create(@JsonProperty("prognos") PrognosTyp prognosTyp,
            @JsonProperty("fortydligande") String fortydligande){
        return new AutoValue_Prognos(prognosTyp, fortydligande);
    }

    public abstract PrognosTyp getPrognosTyp();
    
    public abstract String getFortydligande();
    
    public enum PrognosTyp {
        /**
         * Med stor sannolikhet (id 1).
         */
        MED_STOR_SANNOLIKHET(1, "Med stor sannolikhet"),
        /**
         * Sannolikt sjukskrivning 180 dagar (id 2).
         */
        SANNOLIK_SJUKSKRIVNING_180_DAGAR(2, "Patienten bedöms kunna återgå i nuvarande sysselsättning, men sjukskrivningstiden är sannolikt längre än 180 dagar"),
        /**
         * Sannolikt ej återgå till nuvarande sysselsättning (id 3).
         */
        SANNOLIKT_EJ_ATERGA_TILL_SYSSELSATTNING(3, "Patienten kan sannolikt inte återgå till nuvarande sysselsättning"),
        /**
         * Prognos oklar (id 4).
         */
        PROGNOS_OKLAR(4, "Prognos för återgång i nuvarande sysselsättning är oklar");
        public final int id;

        public final String label;

        PrognosTyp(int id, String label) {
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
        public static PrognosTyp fromId(@JsonProperty("id") int id) {
            for (PrognosTyp typ : values()) {
                if (typ.id == id) {
                    return typ;
                }
            }
            throw new IllegalArgumentException();
        }
    }
}
