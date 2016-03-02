/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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

package se.inera.certificate.modules.sjukersattning.model.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.inera.certificate.modules.sjukersattning.model.converter.util.ConverterUtil;
import se.inera.certificate.modules.sjukersattning.support.SjukersattningEntryPoint;
import se.inera.intyg.common.schemas.clinicalprocess.healthcond.certificate.converter.CertificateStatusUpdateForCareTypeConverter;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.modules.support.api.notification.NotificationMessage;
import se.riv.clinicalprocess.healthcond.certificate.certificatestatusupdateforcareresponder.v2.CertificateStatusUpdateForCareType;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.TypAvIntyg;

public class InternalToNotification {

    private static final Logger LOG = LoggerFactory.getLogger(InternalToNotification.class);

    private static final String TYP_CODE = SjukersattningEntryPoint.MODULE_ID.toUpperCase();

    private static final String TYP_CODESYSTEM = "b64ea353-e8f6-4832-b563-fc7d46f29548";

    private static final String TYP_CODESYSTEM_NAME = "KV_Intygstyp";

    @Autowired
    private ConverterUtil converterUtil;

    public CertificateStatusUpdateForCareType createCertificateStatusUpdateForCareType(NotificationMessage notificationMessage)
            throws ModuleException {

        LOG.debug("Creating CertificateStatusUpdateForCareType for certificate {}, event {}", notificationMessage.getIntygsId(),
                notificationMessage.getHandelse());

        Utlatande utlatandeSource = converterUtil.fromJsonString(notificationMessage.getUtkast());

        return CertificateStatusUpdateForCareTypeConverter.convert(notificationMessage, utlatandeSource, buildTypAvIntyg());
    }

    private TypAvIntyg buildTypAvIntyg() {
        TypAvIntyg typAvIntyg = new TypAvIntyg();
        typAvIntyg.setCode(TYP_CODE);
        typAvIntyg.setCodeSystem(TYP_CODESYSTEM);
        typAvIntyg.setCodeSystemName(TYP_CODESYSTEM_NAME);
        return typAvIntyg;
    }

}
