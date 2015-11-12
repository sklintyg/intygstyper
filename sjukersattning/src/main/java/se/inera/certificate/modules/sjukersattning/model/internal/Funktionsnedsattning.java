package se.inera.certificate.modules.sjukersattning.model.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

@AutoValue
public abstract class Funktionsnedsattning {

    Funktionsnedsattning() {
    }

    @JsonCreator
    public static Funktionsnedsattning create(@JsonProperty("funktionsomrade") Funktionsomrade funktionsomrade,
            @JsonProperty("beskrivning") String beskrivning) {
        return new AutoValue_Funktionsnedsattning(funktionsomrade, beskrivning);
    }

    @Nullable
    public abstract Funktionsomrade getFunktionsomrade();

    @Nullable
    public abstract String getBeskrivning();

    public enum Funktionsomrade {
        INTELLEKTUELL(1, "Intellektuell funktion"), KOMMUNIKATION(2, "Kommunikation och social interaktion"),
        KONCENTRATION(3, "Uppmärksamhet och koncentration"), ANNAN_PSYKISK(4, "Annan psykisk funktion"),
        SYN_HORSEL_TAL(5, "Syn, hörsel och tal"), BALANS(6, "Balans, koordination och motorik"),
        ANNAN_KROPPSLIG(7, "Annan kroppslig funktion"), OKAND(8, "Okänd");

        private int id;

        private String label;

        Funktionsomrade(int id, String label) {
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
        public static Funktionsomrade fromId(@JsonProperty("id") int id) {
            for (Funktionsomrade funktionsomrade : values()) {
                if (funktionsomrade.id == id) {
                    return funktionsomrade;
                }
            }
            throw new IllegalArgumentException();
        }
    }

}
