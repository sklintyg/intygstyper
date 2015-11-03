package se.inera.certificate.modules.sjukersattning.model.internal;

import java.util.Objects;

import se.inera.certificate.model.InternalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public class Underlag {

    private UnderlagsTyp id;

    private InternalDate datum;

    private boolean attachment;

    public Underlag(UnderlagsTyp typ, InternalDate internalDate, boolean attachment) {
        this.id = typ;
        this.datum = internalDate;
        this.attachment = attachment;
    }

    public Underlag() {
    }

    public UnderlagsTyp getId() {
        return id;
    }

    public void setId(UnderlagsTyp id) {
        this.id = id;
    }

    public InternalDate getDatum() {
        return datum;
    }

    public void setDatum(InternalDate datum) {
        this.datum = datum;
    }

    public boolean isAttachment() {
        return attachment;
    }

    public void setAttachment(boolean attachment) {
        this.attachment = attachment;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        }
        final Underlag that = (Underlag) object;
        return Objects.equals(this.id, that.id) && Objects.equals(this.datum, that.datum) && Objects.equals(this.attachment, that.attachment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, datum, attachment);
    }

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
