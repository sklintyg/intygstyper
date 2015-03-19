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
package se.inera.certificate.modules.ts_bas.validator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.certificate.modules.support.api.dto.ValidateDraftResponse;
import se.inera.certificate.modules.ts_bas.model.internal.Utlatande;
import se.inera.certificate.modules.ts_bas.validator.external.TransportValidatorInstance;
import se.inera.certificate.modules.ts_bas.validator.internal.InternalValidatorInstance;
import se.inera.certificate.xml.SchemaValidatorBuilder;
import se.inera.intygstjanster.ts.services.v1.TSBasIntyg;

public class TsBasValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(TsBasValidator.class);

    private static final String COMMON_UTLATANDE_SCHEMA = "/intygstjanster-services/core-components/se_intygstjanster_services_1.0.xsd";

    private static final String COMMON_UTLATANDE_TYPES_SCHEMA = "/intygstjanster-services/core-components/se_intygstjanster_services_types_1.0.xsd";

    private static final String COMMON_UTLATANDE_ISO_SCHEMA = "/clinicalprocess-healthcond-certificate/core-components/iso_dt_subset_1.0.xsd";

    private static Schema commonSchema;

    private static void initCommonSchema() throws Exception {
        SchemaValidatorBuilder schemaValidatorBuilder = new SchemaValidatorBuilder();
        Source rootSource = schemaValidatorBuilder.registerResource(COMMON_UTLATANDE_SCHEMA);
        schemaValidatorBuilder.registerResource(COMMON_UTLATANDE_ISO_SCHEMA);
        schemaValidatorBuilder.registerResource(COMMON_UTLATANDE_TYPES_SCHEMA);

        commonSchema = schemaValidatorBuilder.build(rootSource);
    }

    /**
     * Validates an internal Utlatande.
     *
     * @param utlatande
     *            se.inera.certificate.modules.ts_bas.model.internal.Utlatande
     * @return List of validation errors, or an empty string if validated correctly.
     */
    public ValidateDraftResponse validateInternal(Utlatande utlatande) {
        InternalValidatorInstance instance = new InternalValidatorInstance();
        return instance.validate(utlatande);
    }

    /**
     * Performs programmatic validation a TSBasIntyg on the transport format.
     *
     * @param TSBasIntyg
     * @return List of validation errors, or an empty string if validated correctly.
     */
    public List<String> validateTransport(TSBasIntyg intyg) {
        TransportValidatorInstance instance = new TransportValidatorInstance();
        return instance.validate(intyg);
    }
    
    /**
     * Perform schema validation of the transport format.
     * 
     * @param TSBasIntyg
     * @return true if model is valid, false otherwise.
     * @throws Exception if schema init failed
     */
    public boolean isSchemaValid(TSBasIntyg intyg) {
        try {
            initCommonSchema();
            Validator schemaValidator = commonSchema.newValidator(); 

            ByteArrayOutputStream output = new ByteArrayOutputStream();

            JAXBElement<TSBasIntyg> jaxbElement = new JAXBElement<TSBasIntyg>(new QName("ns3:basIntyg"), TSBasIntyg.class, intyg);
            JAXBContext context = JAXBContext.newInstance(TSBasIntyg.class);
            context.createMarshaller().marshal(jaxbElement, output);

            schemaValidator.validate(new StreamSource(new ByteArrayInputStream(output.toByteArray())));
        } catch (Exception e) {
            LOGGER.error("Schemavalidation in TS-bas failed with msg: " + e.getMessage());
            return false;
        }
        return true;
    }
}
