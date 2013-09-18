package se.inera.certificate.modules.fk7263.model.external;

import java.util.ArrayList;
import java.util.List;

import se.inera.certificate.model.Arbetsuppgift;
import se.inera.certificate.model.Id;
import se.inera.certificate.model.Sysselsattning;
import se.inera.certificate.model.util.Strings;

/**
 * @author andreaskaltenbach
 */
public class Fk7263Patient {

    private Id id;
    private List<String> fornamn = new ArrayList<>();
    private List<String> mellannamn = new ArrayList<>();
    private String efternamn;

    private List<Sysselsattning> sysselsattnings = new ArrayList<>();
    private List<Arbetsuppgift> arbetsuppgifts = new ArrayList<>();

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public List<String> getFornamn() {
        return fornamn;
    }

    public void setFornamn(List<String> fornamn) {
        this.fornamn = fornamn;
    }

    public List<String> getMellannamn() {
        return mellannamn;
    }

    public void setMellannamn(List<String> mellannamn) {
        this.mellannamn = mellannamn;
    }

    public String getEfternamn() {
        return efternamn;
    }

    public void setEfternamn(String efternamn) {
        this.efternamn = efternamn;
    }

    public String getFullstandigtNamn() {
        List<String> names = new ArrayList<>();

        if (fornamn != null) {
            names.addAll(fornamn);
        }
        if (mellannamn != null) {
            names.addAll(mellannamn);
        }
        names.add(efternamn);

        return Strings.join(" ", names);
    }

    public List<Sysselsattning> getSysselsattnings() {
        return sysselsattnings;
    }

    public void setSysselsattnings(List<Sysselsattning> sysselsattnings) {
        this.sysselsattnings = sysselsattnings;
    }

    public List<Arbetsuppgift> getArbetsuppgifts() {
        return arbetsuppgifts;
    }

    public void setArbetsuppgifts(List<Arbetsuppgift> arbetsuppgifts) {
        this.arbetsuppgifts = arbetsuppgifts;
    }
}
