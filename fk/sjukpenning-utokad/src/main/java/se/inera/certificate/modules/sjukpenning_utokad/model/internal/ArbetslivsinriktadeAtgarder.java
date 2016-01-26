package se.inera.certificate.modules.sjukpenning_utokad.model.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ArbetslivsinriktadeAtgarder {
    ArbetslivsinriktadeAtgarder() {
    }

    @JsonCreator
    public static ArbetslivsinriktadeAtgarder create(@JsonProperty("val") ArbetslivsinriktadeAtgarderVal val) {
        return new AutoValue_ArbetslivsinriktadeAtgarder(val);
    }

    public abstract ArbetslivsinriktadeAtgarderVal getVal();

    public enum ArbetslivsinriktadeAtgarderVal {
        /** 
         * id 1.
         */
        INTE_AKTUELLT(1, "Inte aktuellt"),
        /**
         * id 2.
         */
        ARBETSTRANING(2, "Arbetsträning"),
        /**
         * id 3.
         */
        ARBETSANPASSNING(3, "Arbetsanpassning"),
        /**
         * id 4.
         */
        SOKA_NYTT_ARBETE(4, "Söka nytt arbete"),
        /**
         * id 5.
         */
        BESOK_PA_ARBETSPLATSEN(5, "Besök på arbetsplatsen"),
        /**
         * id 6.
         */
        ERGONOMISK_BEDOMNING(6, "Ergonomisk bedömning"),
        /**
         * id 7.
         */
        HJALPMEDEL(7, "Hjälpmedel"),
        /**
         * id 8.
         */
        KONFLIKTHANTERING(8, "Konflikthantering"),
        /**
         * id 9.
         */
        KONTAKT_MED_FORETAGSHALSOVARD(9, "Kontakt med företagshälsovård"),
        /**
         * id 10.
         */
        OMFORDELNING_AV_ARBETSUPPGIFTER(10, "Omfördelning av arbetsuppgifter"),
        /**
         * id 11.
         */
        OVRIGT(11, "Övrigt");
        private final int id;
        private final String label;

        ArbetslivsinriktadeAtgarderVal(int id, String label) {
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
        public static ArbetslivsinriktadeAtgarderVal fromId(@JsonProperty("id") int id) {
            for (ArbetslivsinriktadeAtgarderVal typ : values()) {
                if (typ.id == id) {
                    return typ;
                }
            }
            throw new IllegalArgumentException();
        }
    }

}
