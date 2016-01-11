/*
 * Copyright (C) 2015 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.inera.certificate.modules.sjukersattning.integration;

import se.riv.clinicalprocess.healthcond.certificate.v2.ErrorIdType;
import se.riv.clinicalprocess.healthcond.certificate.v2.ResultCodeType;
import se.riv.clinicalprocess.healthcond.certificate.v2.ResultType;

public final class ResultUtil {

    private ResultUtil() {
    }

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
