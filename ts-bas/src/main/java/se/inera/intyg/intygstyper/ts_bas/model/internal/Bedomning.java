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
