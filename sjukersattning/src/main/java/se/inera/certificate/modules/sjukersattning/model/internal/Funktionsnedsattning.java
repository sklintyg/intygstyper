package se.inera.certificate.modules.sjukersattning.model.internal;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public class Funktionsnedsattning {

    private Funktionsomrade id;

    private String beskrivning;

    public Funktionsnedsattning(Funktionsomrade funktionsomrade, String beskrivning) {
        this.id = funktionsomrade;
        this.beskrivning = beskrivning;
    }

    public Funktionsnedsattning() {}

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        }
        final Funktionsnedsattning that = (Funktionsnedsattning) object;
        return Objects.equals(this.id, that.id) && Objects.equals(this.beskrivning, that.beskrivning);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, beskrivning);
    }

    public void setId(Funktionsomrade id) {
        this.id = id;
    }

    public Funktionsomrade getId() {
        return id;
    }

    public void setBeskrivning(String beskrivning) {
        this.beskrivning = beskrivning;
    }

    public String getBeskrivning() {
        return beskrivning;
    }

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
