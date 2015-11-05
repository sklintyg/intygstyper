package se.inera.certificate.modules.sjukersattning.model.internal;

import java.util.Objects;

public class BehandlingsAtgard {

    private String atgardsKod;

    private String atgardsKodSystem;

    private String atgardsBeskrivning;

    public BehandlingsAtgard() {
    }

    public BehandlingsAtgard(String atgardsKod, String atgardsKodSystem, String atgardsBeskrivning) {
        this.atgardsKod = atgardsKod;
        this.atgardsKodSystem = atgardsKodSystem;
        this.atgardsBeskrivning = atgardsBeskrivning;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        }
        final BehandlingsAtgard that = (BehandlingsAtgard) object;
        return Objects.equals(this.atgardsKod, that.atgardsKod) && Objects.equals(this.atgardsKodSystem, that.atgardsKodSystem) &&
                Objects.equals(this.atgardsBeskrivning, that.atgardsBeskrivning);
    }

    @Override
    public int hashCode() {
        return Objects.hash(atgardsKod, atgardsKodSystem, atgardsBeskrivning);
    }

    public String getAtgardsKod() {
        return atgardsKod;
    }

    public void setAtgardsKod(String atgardsKod) {
        this.atgardsKod = atgardsKod;
    }

    public String getAtgardsKodSystem() {
        return atgardsKodSystem;
    }

    public void setAtgardsKodSystem(String atgardsKodSystem) {
        this.atgardsKodSystem = atgardsKodSystem;
    }

    public String getAtgardsBeskrivning() {
        return atgardsBeskrivning;
    }

    public void setAtgardsBeskrivning(String atgardsBeskrivning) {
        this.atgardsBeskrivning = atgardsBeskrivning;
    }
}
