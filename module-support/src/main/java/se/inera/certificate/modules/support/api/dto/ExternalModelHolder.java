/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Certificate Modules (http://code.google.com/p/inera-certificate-modules).
 *
 * Inera Certificate Modules is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Inera Certificate Modules is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.certificate.modules.support.api.dto;

import static org.springframework.util.Assert.notNull;

/**
 * Wrapper class for holding the Utlatande in external format of a certificate as well as metadata about the
 * certificate, such as status
 */
public class ExternalModelHolder {

    private final String externalModel;

    private final CertificateContentMeta certificateContentMeta;

    public ExternalModelHolder(String externalModel, CertificateContentMeta certificateContentMeta) {
        notNull(externalModel, "'externalModel' must not be null");
        notNull(certificateContentMeta, "'certificateContentMeta' must not be null");
        this.externalModel = externalModel;
        this.certificateContentMeta = certificateContentMeta;
    }

    public String getExternalModel() {
        return externalModel;
    }

    public CertificateContentMeta getCertificateContentMeta() {
        return certificateContentMeta;
    }
}
