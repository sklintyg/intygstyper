package se.inera.certificate.modules.sjukpenning_utokad.model.internal;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.auto.value.AutoValue;

import se.inera.intyg.common.support.model.InternalDate;

@AutoValue
public abstract class MedicinsktUnderlag {

    @JsonCreator
    public static MedicinsktUnderlag create(@JsonProperty("medicinsktUnderlagTyp") MedicinsktUnderlagTyp medicinsktUnderlagTyp,
            @JsonProperty("datum") InternalDate datum, @JsonProperty("annanGrund") String annanGrund){
        return new AutoValue_MedicinsktUnderlag(medicinsktUnderlagTyp, datum, annanGrund);
    }

    public abstract MedicinsktUnderlagTyp getMedicinsktUnderlagTyp();

    public abstract InternalDate getDatumForMedicinsktUnderlag();

    @Nullable
    public abstract String getAnnanGrundForMedicinsktUnderlag();

    public enum MedicinsktUnderlagTyp {
        MIN_UNDERSOKNING_AV_PATIENTEN(1, "Min undersökning av patienten"),
        MIN_TELEFONKONTAKT_MED_PATIENTEN(2, "Min telefonkontakt med patienten"),
        JOURNALUPPGIFTER(3, "Journaluppgifter"),
        ANHORIGS_BESKRIVNING_AV_PATIENTEN(4, "Anhörigs beskrivning av patienten"),
        ANNAT(5, "Annat");

        private final int id;
        private final String label;
        MedicinsktUnderlagTyp(int id, String label) {
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
        public static MedicinsktUnderlagTyp fromId(@JsonProperty("id") int id) {
            for (MedicinsktUnderlagTyp typ : values()) {
                if (typ.id == id) {
                    return typ;
                }
            }
            throw new IllegalArgumentException();
        }
    }
}
