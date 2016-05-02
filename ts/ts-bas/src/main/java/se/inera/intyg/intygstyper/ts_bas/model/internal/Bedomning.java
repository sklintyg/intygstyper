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

public class Bedomning {

    @JsonSerialize(using = BedomningKorkortstypEnumSetSerializer.class)
    @JsonDeserialize(using = BedomningKorkortstypEnumSetDeserializer.class)
    private EnumSet<BedomningKorkortstyp> korkortstyp;

    private Boolean kanInteTaStallning;

    private String lakareSpecialKompetens;

    public Set<BedomningKorkortstyp> getKorkortstyp() {
        if (korkortstyp == null) {
            korkortstyp = EnumSet.noneOf(BedomningKorkortstyp.class);
        }
        return korkortstyp;
    }

    public Boolean getKanInteTaStallning() {
        return kanInteTaStallning;
    }

    public void setKanInteTaStallning(Boolean kanInteTaStallning) {
        this.kanInteTaStallning = kanInteTaStallning;
    }

    public String getLakareSpecialKompetens() {
        return lakareSpecialKompetens;
    }

    public void setLakareSpecialKompetens(String lakareSpecialKompetens) {
        this.lakareSpecialKompetens = lakareSpecialKompetens;
    }

    public static class BedomningKorkortstypEnumSetSerializer extends AbstractEnumSetSerializer<BedomningKorkortstyp> {
        protected BedomningKorkortstypEnumSetSerializer() {
            super(BedomningKorkortstyp.class);
        }
    }

    public static class BedomningKorkortstypEnumSetDeserializer extends
            AbstractEnumSetDeserializer<BedomningKorkortstyp> {
        protected BedomningKorkortstypEnumSetDeserializer() {
            super(BedomningKorkortstyp.class);
        }
    }
}
