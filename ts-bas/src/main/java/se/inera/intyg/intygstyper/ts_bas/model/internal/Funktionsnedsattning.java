package se.inera.certificate.modules.ts_bas.model.internal;

public class Funktionsnedsattning {

    private Boolean funktionsnedsattning;

    private String beskrivning;

    //För att hjälpa passagerare in och ut samt med säkerhetsbälte
    private Boolean otillrackligRorelseformaga;

    public Boolean getFunktionsnedsattning() {
        return funktionsnedsattning;
    }

    public void setFunktionsnedsattning(Boolean funktionsnedsattning) {
        this.funktionsnedsattning = funktionsnedsattning;
    }

    public String getBeskrivning() {
        return beskrivning;
    }

    public void setBeskrivning(String beskrivning) {
        this.beskrivning = beskrivning;
    }

    public Boolean getOtillrackligRorelseformaga() {
        return otillrackligRorelseformaga;
    }

    public void setOtillrackligRorelseformaga(Boolean otillrackligRorelseformaga) {
        this.otillrackligRorelseformaga = otillrackligRorelseformaga;
    }


}
