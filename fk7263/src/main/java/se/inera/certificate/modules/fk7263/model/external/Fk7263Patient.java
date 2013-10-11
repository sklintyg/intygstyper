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
    private List<String> fornamn;
    private List<String> mellannamn;
    private String efternamn;

    private List<Sysselsattning> sysselsattningar;
    private List<Arbetsuppgift> arbetsuppgifter;

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public List<String> getFornamn() {
        if (fornamn == null) {
            fornamn = new ArrayList<>();
        }
        return fornamn;
    }

    public List<String> getMellannamn() {
        if (mellannamn == null) {
            mellannamn = new ArrayList<>();
        }
        return mellannamn;
    }

    public String getEfternamn() {
        return efternamn;
    }

    public void setEfternamn(String efternamn) {
        this.efternamn = efternamn;
    }

    public String getFullstandigtNamn() {
        List<String> names = new ArrayList<>();

        names.addAll(getFornamn());
        names.addAll(getMellannamn());
        names.add(efternamn);

        return Strings.join(" ", names);
    }

    public List<Sysselsattning> getSysselsattningar() {
        if (sysselsattningar == null) {
            sysselsattningar = new ArrayList<>();
        }
        return sysselsattningar;
    }

    public List<Arbetsuppgift> getArbetsuppgifter() {
        if (arbetsuppgifter == null) {
            arbetsuppgifter = new ArrayList<>();
        }
        return arbetsuppgifter;
    }
}
