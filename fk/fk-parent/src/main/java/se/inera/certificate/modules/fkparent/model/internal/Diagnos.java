package se.inera.certificate.modules.fkparent.model.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;

/**
 * Created by BESA on 2016-02-23.
 */
@AutoValue
public abstract class Diagnos {

    Diagnos() {
    }

    @JsonCreator
    public static Diagnos create(@JsonProperty("diagnosKod") String diagnosKod,
                                 @JsonProperty("diagnosKodSystem") String diagnosKodSystem,
                                 @JsonProperty("diagnosBeskrivning") String diagnosBeskrivning) {
        return new AutoValue_Diagnos(diagnosKod, diagnosKodSystem, diagnosBeskrivning);
    }

    @Nullable
    public abstract String getDiagnosKod();

    @Nullable
    public abstract String getDiagnosKodSystem();

    @Nullable
    public abstract String getDiagnosBeskrivning();

}
