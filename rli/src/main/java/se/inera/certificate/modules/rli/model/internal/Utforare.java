/**
 * 
 */
package se.inera.certificate.modules.rli.model.internal;


/**
 * @author erik
 *
 */
public class Utforare {
    private String utforartyp;
    private HoSPersonal antasAv;
    
    public Utforare(){
        
    }
    
    public String getUtforartyp() {
        return utforartyp;
    }
    public void setUtforartyp(String utforartyp) {
        this.utforartyp = utforartyp;
    }
    
    public HoSPersonal getAntasAv() {
        return antasAv;
    }

    public void setAntasAv(HoSPersonal antasAv) {
        this.antasAv = antasAv;
    }
    
}
