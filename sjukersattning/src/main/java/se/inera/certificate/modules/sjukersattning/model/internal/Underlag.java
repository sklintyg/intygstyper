package se.inera.certificate.modules.sjukersattning.model.internal;

import se.inera.certificate.model.InternalDate;

import java.util.Objects;

public class Underlag {

    private UnderlagsTyp id;

    private InternalDate datum;

    private boolean attachment;

    public Underlag(UnderlagsTyp typ, InternalDate internalDate, boolean attachment) {
        this.id = typ;
        this.datum = internalDate;
        this.attachment = attachment;
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
        NEUROPSYKIATRISKT_UTLATANDE("Neuropsykiatriskt utlåtande"),
        UNDERLAG_FRAN_HABILITERINGEN("Underlag från habiliteringen"),
        UNDERLAG_FRAN_ARBETSTERAPEUT("Underlag från arbetsterapeut"),
        UNDERLAG_FRAN_FYSIOTERAPEUT("Underlag från fysioterapeut"),
        UNDERLAG_FRAN_LOGOPED("Underlag från logoped"),
        UNDERLAG_FRANPSYKOLOG("Underlag från psykolog"),
        UNDERLAG_FRANFÖRETAGSHALSOVARD("Underlag från företagshälsovård"),
        UTREDNING_AV_ANNAN_SPECIALISTKLINIK("Utredning av annan specialistklinik"),
        UTREDNING_FRAN_VARDINRATTNING_UTOMLANDS("Utredning från vårdinrättning utomlands"),
        OVRIGT("Övrigt");

        private final String label;

        UnderlagsTyp(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

}
