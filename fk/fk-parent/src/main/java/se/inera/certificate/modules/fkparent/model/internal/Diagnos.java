/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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

    private String displayName;

    Diagnos() {
    }

    @JsonCreator
    public static Diagnos create(@JsonProperty("diagnosKod") String diagnosKod,
                                 @JsonProperty("diagnosKodSystem") String diagnosKodSystem,
                                 @JsonProperty("diagnosBeskrivning") String diagnosBeskrivning) {
        return new AutoValue_Diagnos(diagnosKod, diagnosKodSystem, diagnosBeskrivning);
    }
    
    public Diagnos createWithDisplayName(String displayName){
        Diagnos diagnos = create(getDiagnosKod(), getDiagnosKodSystem(), getDiagnosBeskrivning());
        this.displayName = displayName;
        return diagnos;
    }

    @Nullable
    public abstract String getDiagnosKod();

    @Nullable
    public abstract String getDiagnosKodSystem();

    @Nullable
    public abstract String getDiagnosBeskrivning();

    public String getDisplayName() {
        return displayName;
    }

}
