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

package se.inera.intyg.intygstyper.ts_bas.model.internal;

import java.util.EnumSet;
import java.util.Set;

import se.inera.intyg.intygstyper.ts_parent.json.AbstractEnumSetDeserializer;
import se.inera.intyg.intygstyper.ts_parent.json.AbstractEnumSetSerializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * The Korkortstyp[er] a specific Utlatande concerns.
 *
 * @author erik
 */
public class IntygAvser {

    @JsonSerialize(using = IntygAvserEnumSetSerializer.class)
    @JsonDeserialize(using = IntygAvserEnumSetDeserializer.class)
    private EnumSet<IntygAvserKategori> korkortstyp;

    public Set<IntygAvserKategori> getKorkortstyp() {
        if (korkortstyp == null) {
            korkortstyp = EnumSet.noneOf(IntygAvserKategori.class);
        }
        return korkortstyp;
    }

    public static class IntygAvserEnumSetSerializer extends AbstractEnumSetSerializer<IntygAvserKategori> {
        protected IntygAvserEnumSetSerializer() {
            super(IntygAvserKategori.class);
        }
    }

    public static class IntygAvserEnumSetDeserializer extends AbstractEnumSetDeserializer<IntygAvserKategori> {
        protected IntygAvserEnumSetDeserializer() {
            super(IntygAvserKategori.class);
        }
    }

}
