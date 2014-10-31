package se.inera.certificate.model;

import java.util.ArrayList;
import java.util.List;

public abstract class HosPersonal {

    private Id id;

    private String namn;

    private String forskrivarkod;

    private List<String> befattningar;

    private List<Kod> yrkesgrupper;

    public final Id getId() {
        return id;
    }

    public final void setId(Id id) {
        this.id = id;
    }

    public final String getNamn() {
        return namn;
    }

    public final void setNamn(String namn) {
        this.namn = namn;
    }

    public final String getForskrivarkod() {
        return forskrivarkod;
    }

    public final void setForskrivarkod(String forskrivarkod) {
        this.forskrivarkod = forskrivarkod;
    }

    public final List<String> getBefattningar() {
        if (befattningar == null) {
            befattningar = new ArrayList<>();
        }
        return befattningar;
    }

    public final List<Kod> getYrkesgrupper() {
        if (yrkesgrupper == null) {
            yrkesgrupper = new ArrayList<>();
        }
        return yrkesgrupper;
    }

    public abstract Vardenhet getVardenhet();
}
