/**
 * 
 */
package se.inera.certificate.modules.rli.model.internal.wc;

import se.inera.certificate.modules.rli.model.codes.UtforarrollKod;


/**
 * @author erik
 *
 */
public class Utforare {
    private UtforarrollKod utforartyp;
    private HoSPersonal antasAv;
    
    public Utforare(){
        
    }
    
    public UtforarrollKod getUtforartyp() {
        return utforartyp;
    }
    public void setUtforartyp(UtforarrollKod utforartyp) {
        this.utforartyp = utforartyp;
    }
    
    public HoSPersonal getAntasAv() {
        return antasAv;
    }

    public void setAntasAv(HoSPersonal antasAv) {
        this.antasAv = antasAv;
    }
    
}
