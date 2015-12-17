package se.inera.certificate.modules.sjukpenning.integration;

import se.riv.clinicalprocess.healthcond.certificate.v2.ErrorIdType;
import se.riv.clinicalprocess.healthcond.certificate.v2.ResultCodeType;
import se.riv.clinicalprocess.healthcond.certificate.v2.ResultType;

public class ResultUtil {

    public static ResultType okResult() {
        ResultType resultat = new ResultType();
        resultat.setResultCode(ResultCodeType.OK);
        return resultat;
    }

    public static ResultType infoResult(String message) {
        ResultType resultat = new ResultType();
        resultat.setResultCode(ResultCodeType.INFO);
        resultat.setResultText(message);
        return resultat;
    }

    public static ResultType errorResult(ErrorIdType errorIdtype, String message) {
        ResultType resultat = new ResultType();
        resultat.setResultCode(ResultCodeType.ERROR);
        resultat.setErrorId(errorIdtype);
        resultat.setResultText(message);
        return resultat;
    }

}
