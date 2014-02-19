package se.inera.certificate.modules.ts_diabetes.model.internal;

import java.util.EnumSet;
import java.util.Set;

public class Bedomning {

    private Set<BedomningKorkortstyp> korkortstyp;

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
}
