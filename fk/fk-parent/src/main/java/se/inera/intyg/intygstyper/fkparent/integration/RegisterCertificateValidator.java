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

package se.inera.intyg.intygstyper.fkparent.integration;

import javax.annotation.Nonnull;
import javax.xml.transform.Source;

import org.oclc.purl.dsdl.svrl.SchematronOutputType;

import com.helger.schematron.xslt.SchematronResourceSCH;

import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;

public class RegisterCertificateValidator {
    private SchematronResourceSCH schematronResource;

    public RegisterCertificateValidator(@Nonnull final String location)  {
        schematronResource = SchematronResourceSCH.fromClassPath(location);
        if (!schematronResource.isValidSchematron()) {
            throw new IllegalArgumentException("Invalid Schematron!");
        }
    }

    public SchematronOutputType validateSchematron(@Nonnull final Source xmlContent) throws ModuleException {
        try {
            return schematronResource.applySchematronValidationToSVRL(xmlContent);
        } catch (Exception e) {
            throw new ModuleException(e);
        }
    }

}
