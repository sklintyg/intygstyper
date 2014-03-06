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

import se.inera.certificate.integration.rest.dto.CertificateContentMeta;
import se.inera.certificate.model.Utlatande;

/**
 * Wrapper class for holding the Utlatande in external format of a certificate as well as metadata about the
 * certificate, such as status
 * 
 * @author marced
 */
public class ExternalModelHolder {

    private Utlatande utlatande;

    private CertificateContentMeta certificateContentMeta;

    public CertificateContentMeta getCertificateContentMeta() {
        return certificateContentMeta;
    }

    public void setCertificateContentMeta(CertificateContentMeta certificateContentMeta) {
        this.certificateContentMeta = certificateContentMeta;
    }
    
    public Utlatande getCertificateContent() {
        return utlatande;
    }

    public <T extends Utlatande> T getCertificateContent(Class<T> type) {
        return type.cast(utlatande);
    }

    public void setCertificateContent(Utlatande certificateContent) {
        this.utlatande = certificateContent;
    }

}
