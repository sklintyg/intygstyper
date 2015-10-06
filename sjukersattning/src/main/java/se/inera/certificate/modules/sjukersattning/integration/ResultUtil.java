package se.inera.certificate.modules.sjukersattning.integration;

import se.inera.intygstjanster.fk.services.v1.ErrorIdType;
import se.inera.intygstjanster.fk.services.v1.ResultCodeType;
import se.inera.intygstjanster.fk.services.v1.ResultatTyp;

public class ResultUtil {

    public static ResultatTyp okResult() {
        ResultatTyp resultat = new ResultatTyp();
        resultat.setResultCode(ResultCodeType.OK);
        return resultat;
    }

    public static ResultatTyp infoResult(String message) {
        ResultatTyp resultat = new ResultatTyp();
        resultat.setResultCode(ResultCodeType.INFO);
        resultat.setResultText(message);
        return resultat;
    }

    public static ResultatTyp errorResult(ErrorIdType errorIdtype, String message) {
        ResultatTyp resultat = new ResultatTyp();
        resultat.setResultCode(ResultCodeType.ERROR);
        resultat.setErrorId(errorIdtype);
        resultat.setResultText(message);
        return resultat;
    }

}
