package se.inera.certificate.modules.rli.model;

/**
 * @author andreaskaltenbach
 */
public class Resa {
    private String resmal;
    private String resenar;

    public Resa(String resmal, String resenar) {
        this.resmal = resmal;
        this.resenar = resenar;
    }

    public String getResmal() {
        return resmal;
    }

    public String getResenar() {
        return resenar;
    }
}
