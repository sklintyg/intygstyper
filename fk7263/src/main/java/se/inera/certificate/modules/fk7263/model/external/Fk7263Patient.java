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
    private List<String> fornamns = new ArrayList<>();
    private List<String> mellannamns = new ArrayList<>();
    private List<String> efternamns = new ArrayList<>();

    private List<Sysselsattning> sysselsattnings = new ArrayList<>();
    private List<Arbetsuppgift> arbetsuppgifts = new ArrayList<>();
    
    //Test property to be removed
    private Fk7263Arbetsgivare arbetsgivare;

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public List<String> getFornamns() {
        return fornamns;
    }

    public void setFornamns(List<String> fornamns) {
        this.fornamns = fornamns;
    }

    public List<String> getMellannamns() {
        return mellannamns;
    }

    public void setMellannamns(List<String> mellannamns) {
        this.mellannamns = mellannamns;
    }

    public List<String> getEfternamns() {
        return efternamns;
    }

    public void setEfternamns(List<String> efternamns) {
        this.efternamns = efternamns;
    }

    public String getFullstandigtNamn() {
        List<String> names = new ArrayList<>();

        if (fornamns != null) {
            names.addAll(fornamns);
        }
        if (mellannamns != null) {
            names.addAll(mellannamns);
        }
        if (efternamns != null) {
            names.addAll(efternamns);
        }

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

    public Fk7263Arbetsgivare getArbetsgivare() {
        return arbetsgivare;
    }

    public void setArbetsgivare(Fk7263Arbetsgivare arbetsgivare) {
        this.arbetsgivare = arbetsgivare;
    }
}
