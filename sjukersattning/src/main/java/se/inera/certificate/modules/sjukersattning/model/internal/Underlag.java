package se.inera.certificate.modules.sjukersattning.model.internal;

import se.inera.certificate.model.InternalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Underlag {

    protected Underlag() {
    }

    @JsonCreator
    public static Underlag create(@JsonProperty("typ") UnderlagsTyp typ,
            @JsonProperty("datum") InternalDate datum,
            @JsonProperty("bilaga") boolean bilaga) {
        return new AutoValue_Underlag(typ, datum, bilaga);
    }

    public abstract UnderlagsTyp getTyp();

    public abstract InternalDate getDatum();

    public abstract boolean isBilaga();

    public enum UnderlagsTyp {
        NEUROPSYKIATRISKT_UTLATANDE(1, "Neuropsykiatriskt utlåtande"),
        UNDERLAG_FRAN_HABILITERINGEN(2, "Underlag från habiliteringen"),
        UNDERLAG_FRAN_ARBETSTERAPEUT(3, "Underlag från arbetsterapeut"),
        UNDERLAG_FRAN_FYSIOTERAPEUT(4, "Underlag från fysioterapeut"),
        UNDERLAG_FRAN_LOGOPED(5, "Underlag från logoped"),
        UNDERLAG_FRANPSYKOLOG(6, "Underlag från psykolog"),
        UNDERLAG_FRANFORETAGSHALSOVARD(7, "Underlag från företagshälsovård"),
        UTREDNING_AV_ANNAN_SPECIALISTKLINIK(8, "Utredning av annan specialistklinik"),
        UTREDNING_FRAN_VARDINRATTNING_UTOMLANDS(9, "Utredning från vårdinrättning utomlands"),
        OVRIGT(10, "Övrigt"),
        OKAND(-1, "Okand");

        private final int id;
        private final String label;

        UnderlagsTyp(int id, String label) {
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
        public static UnderlagsTyp fromId(@JsonProperty("id") int id) {
            for (UnderlagsTyp typ : values()) {
                if (typ.id == id) {
                    return typ;
                }
            }
            throw new IllegalArgumentException();
        }

    }

}
