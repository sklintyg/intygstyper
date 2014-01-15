package se.inera.certificate.modules.ts_bas.model.internal.mi;

import java.util.ArrayList;
import java.util.List;

public class Bedomning {
    
    private List<String> korkortstyp;
    
    private Boolean kanInteTaStallning;
    
    private String lakareSpecialKompetens;

    public List<String> getKorkortstyp() {
        if (korkortstyp == null) {
            korkortstyp = new ArrayList<String>();
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
    
    

}
