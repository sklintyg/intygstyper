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

import javax.annotation.Nonnull;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerConfigurationException;

import org.oclc.purl.dsdl.svrl.SchematronOutputType;

import autovalue.shaded.com.google.common.common.base.Throwables;

import com.helger.schematron.xslt.SchematronResourceSCH;

public class RegisterSjukersattningValidator {
    private static final String SCHEMATRON_SCHEMA = "sjukersattning-structure.sch";

    private SchematronResourceSCH schematronResource;

    public RegisterSjukersattningValidator(String location)  {
        schematronResource = SchematronResourceSCH.fromClassPath(location);
        try {
            schematronResource.getXSLTProvider().getXSLTTransformer().setParameter("allow-foreign", true);
        } catch (TransformerConfigurationException e) {
            throw Throwables.propagate(e);
        }

        if (!schematronResource.isValidSchematron()) {
            throw new IllegalArgumentException("Invalid Schematron!");
        }
    }

    public RegisterSjukersattningValidator() {
        this(SCHEMATRON_SCHEMA);
    }

    public SchematronOutputType validateSchematron(@Nonnull final Source xmlContent) throws Exception {
        return schematronResource.applySchematronValidationToSVRL(xmlContent);
    }

}
