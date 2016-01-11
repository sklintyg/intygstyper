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


package se.inera.certificate.modules.sjukpenning.model.converter;

import se.inera.certificate.modules.sjukpenning.model.internal.SjukpenningUtlatande;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateMetaData;
import se.riv.clinicalprocess.healthcond.certificate.v2.Intyg;

public final class TransportToInternal {

    private TransportToInternal() {
    }

    public static SjukpenningUtlatande convert(Intyg source) throws ConverterException {
        SjukpenningUtlatande utlatande = new SjukpenningUtlatande();
        return utlatande;
    }

    public static CertificateMetaData getMetaData(Intyg source) {
        CertificateMetaData metaData = new CertificateMetaData();
        return metaData;
    }

}
