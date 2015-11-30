package se.inera.certificate.modules.ts_bas.model.internal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class SomnVakenhet {

    @JsonInclude(Include.NON_EMPTY)
    private Boolean teckenSomnstorningar;

    public Boolean getTeckenSomnstorningar() {
        return teckenSomnstorningar;
    }

    public void setTeckenSomnstorningar(Boolean teckenSomnstorningar) {
        this.teckenSomnstorningar = teckenSomnstorningar;
    }

}
