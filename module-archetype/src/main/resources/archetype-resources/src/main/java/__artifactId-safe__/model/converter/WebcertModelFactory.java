#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
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
package ${package}.${artifactId-safe}.model.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.certificate.modules.support.api.dto.CreateNewDraftHolder;
import ${package}.${artifactId-safe}.model.internal.Utlatande;

/**
 * Factory for creating a editable model.
 */
public class WebcertModelFactory {

    private static final Logger LOG = LoggerFactory.getLogger(WebcertModelFactory.class);

    /**
     * Create a new draft pre-populated with the attached data.
     * 
     * @param newDraftData
     *            {@link CreateNewDraftHolder}
     * @param template
     *            A template to use as a base, or <code>null</code> if an empty internal model should be used.
     * 
     * @return {@link Utlatande}
     * 
     * @throws ConverterException
     *             if something unforeseen happens
     */
    public Utlatande createNewWebcertDraft(CreateNewDraftHolder newDraftData, Utlatande template) throws ConverterException {
        if (template == null) {
            LOG.trace("Creating draft with id {}", newDraftData.getCertificateId());
            template = new Utlatande();

        } else {
            LOG.trace("Creating copy with id {} from {}", newDraftData.getCertificateId(), template.getId());
        }
        
        // TODO: Implement
        
        return template;
    }
}
