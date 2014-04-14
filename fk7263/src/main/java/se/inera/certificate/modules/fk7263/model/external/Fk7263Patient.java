package se.inera.certificate.modules.fk7263.model.external;

import java.util.ArrayList;
import java.util.List;

import se.inera.certificate.model.Arbetsuppgift;
import se.inera.certificate.model.Patient;
import se.inera.certificate.model.Sysselsattning;

/**
 * @author andreaskaltenbach
 */
public class Fk7263Patient extends Patient {

    private List<Sysselsattning> sysselsattningar;
    private List<Arbetsuppgift> arbetsuppgifter;

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
