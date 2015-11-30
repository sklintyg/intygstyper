package se.inera.certificate.modules.ts_diabetes.model.internal;

import java.util.EnumSet;
import java.util.Set;

import se.inera.certificate.modules.ts_parent.json.AbstractEnumSetDeserializer;
import se.inera.certificate.modules.ts_parent.json.AbstractEnumSetSerializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

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
