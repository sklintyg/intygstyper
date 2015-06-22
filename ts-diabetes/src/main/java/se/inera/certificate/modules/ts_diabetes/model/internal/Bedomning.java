package se.inera.certificate.modules.ts_diabetes.model.internal;

import java.util.EnumSet;
import java.util.Set;

import se.inera.certificate.modules.ts_parent.json.AbstractEnumSetDeserializer;
import se.inera.certificate.modules.ts_parent.json.AbstractEnumSetSerializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class Bedomning {

    @JsonSerialize(using = BedomningKorkortstypEnumSetSerializer.class)
    @JsonDeserialize(using = BedomningKorkortstypEnumSetDeserializer.class)
    private EnumSet<BedomningKorkortstyp> korkortstyp;

    private Boolean kanInteTaStallning;

    private String lakareSpecialKompetens;

    private Boolean lamplighetInnehaBehorighet;

    private String kommentarer;

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

    public Boolean getLamplighetInnehaBehorighet() {
        return lamplighetInnehaBehorighet;
    }

    public void setLamplighetInnehaBehorighet(Boolean lamplighetInnehaBehorighet) {
        this.lamplighetInnehaBehorighet = lamplighetInnehaBehorighet;
    }

    public String getKommentarer() {
        return kommentarer;
    }

    public void setKommentarer(String kommentarer) {
        this.kommentarer = kommentarer;
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
