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
package se.inera.intyg.intygstyper.fkparent.model.internal;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

/**
 * Created by BESA on 2016-02-23.
 */
@AutoValue
public abstract class Diagnos {

    @JsonCreator
    public static Diagnos create(@JsonProperty("diagnosKod") String diagnosKod,
            @JsonProperty("diagnosKodSystem") String diagnosKodSystem,
            @JsonProperty("diagnosBeskrivning") String diagnosBeskrivning,
            @JsonProperty("diagnosDisplayName")String diagnosDisplayName) {
        return new AutoValue_Diagnos(diagnosKod, diagnosKodSystem, diagnosBeskrivning, diagnosDisplayName);
    }


    @Nullable
    public abstract String getDiagnosKod();

    @Nullable
    public abstract String getDiagnosKodSystem();

    @Nullable
    public abstract String getDiagnosBeskrivning();

    @Nullable
    public abstract String getDiagnosDisplayName();

}
