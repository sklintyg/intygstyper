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

package se.inera.certificate.modules.luae_fs.model.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.inera.certificate.modules.luae_fs.model.converter.util.ConverterUtil;
import se.inera.certificate.modules.luae_fs.model.internal.LuaefsUtlatande;
import se.inera.intyg.common.schemas.clinicalprocess.healthcond.certificate.converter.CertificateStatusUpdateForCareTypeConverter;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.modules.support.api.notification.NotificationMessage;
import se.riv.clinicalprocess.healthcond.certificate.certificatestatusupdateforcareresponder.v2.CertificateStatusUpdateForCareType;
import se.riv.clinicalprocess.healthcond.certificate.v2.Intyg;

public class InternalToNotification {

    private static final Logger LOG = LoggerFactory.getLogger(InternalToNotification.class);

    @Autowired
    private ConverterUtil converterUtil;

    public CertificateStatusUpdateForCareType createCertificateStatusUpdateForCareType(NotificationMessage notificationMessage)
            throws ModuleException {

        LOG.debug("Creating CertificateStatusUpdateForCareType for certificate {}, event {}", notificationMessage.getIntygsId(),
                notificationMessage.getHandelse());

        LuaefsUtlatande utlatandeSource = converterUtil.fromJsonString(notificationMessage.getUtkast());
        Intyg intyg = UtlatandeToIntyg.convert(utlatandeSource);

        return CertificateStatusUpdateForCareTypeConverter.convert(notificationMessage, intyg);
    }

}
