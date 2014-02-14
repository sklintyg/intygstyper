package se.inera.certificate.modules.ts_bas.model.internal;

import java.util.EnumSet;
import java.util.Set;

import se.inera.certificate.modules.ts_bas.json.AbstractEnumSetDeserializer;
import se.inera.certificate.modules.ts_bas.json.AbstractEnumSetSerializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * The Korkortstyp[er] a specific Utlatande concerns
 * 
 * @author erik
 * 
 */
public class IntygAvser {

    @JsonSerialize(using = IntygAvserEnumSetSerializer.class)
    @JsonDeserialize(using = IntygAvserEnumSetDeserializer.class)
    EnumSet<IntygAvserKategori> korkortstyp;
    
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
