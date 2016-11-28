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

package se.inera.intyg.intygstyper.fkparent.model.converter;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.annotation.Nonnull;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.google.common.base.Charsets;

import se.inera.intyg.common.support.xml.SchemaValidatorBuilder;

public class RegisterCertificateTestValidator {
    private static final String RESPONDER_SCHEMA = "interactions/RegisterCertificateInteraction/RegisterCertificateResponder_2.0.xsd";
    private static final String GENERAL_SCHEMA = "core_components/clinicalprocess_healthcond_certificate_2.0.xsd";
    private static final String TYPES_SCHEMA = "core_components/clinicalprocess_healthcond_certificate_types_2.0.xsd";

    private static final Logger LOG = LoggerFactory.getLogger(RegisterCertificateTestValidator.class);

    private Schema generalSchema;

    public void initGeneralSchema() throws IOException, SAXException {
        SchemaValidatorBuilder schemaValidatorBuilder = new SchemaValidatorBuilder();
        schemaValidatorBuilder.registerResource(TYPES_SCHEMA);
        schemaValidatorBuilder.registerResource(GENERAL_SCHEMA);
        Source rootSource = schemaValidatorBuilder.registerResource(RESPONDER_SCHEMA);
        generalSchema = schemaValidatorBuilder.build(rootSource);
    }

    public boolean validateGeneral(@Nonnull final String xmlContent) throws IOException, SAXException {
        initGeneralSchema();
        StreamSource xmlSource = new StreamSource(new ByteArrayInputStream(xmlContent.getBytes(Charsets.UTF_8)));
        try {
            generalSchema.newValidator().validate(xmlSource);
            return true;
        } catch (Exception ex) {
            LOG.error("Error: {}", ex);
            return false;
        }
    }

}
