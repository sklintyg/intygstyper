package se.inera.certificate.modules.sjukpenning_utokad.model.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Sysselsattning {
    Sysselsattning() {
    }
    @JsonCreator
    public static Sysselsattning create(@JsonProperty("typ") SysselsattningsTyp typ) {
        return new AutoValue_Sysselsattning(typ);
    }

    public abstract SysselsattningsTyp getSysselsattningsTyp();

    public enum SysselsattningsTyp {
        nuvarande_arbete(1, ""),
        arbetssokande(2, ""),
        foraldrarledighet_vard_av_barn(3, ""),
        studier(4, ""),
        arbetsmarknadspolitiskt_program(5, "");
        
        private final int id;
        private final String label;
    
        SysselsattningsTyp(int id, String label) {
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
        public static SysselsattningsTyp fromId(@JsonProperty("id") int id) {
            for (SysselsattningsTyp typ : values()) {
                if (typ.id == id) {
                    return typ;
                }
            }
            throw new IllegalArgumentException();
        }
    }
}
