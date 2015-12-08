package se.inera.certificate.modules.sjukersattning.integration;

import javax.annotation.Nonnull;
import javax.xml.transform.Source;

import org.oclc.purl.dsdl.svrl.SchematronOutputType;

import com.helger.schematron.ISchematronResource;
import com.helger.schematron.pure.SchematronResourcePure;

public class RegisterSjukersattningValidator {
    private static final String SCHEMATRON_SCHEMA = "sjukersattning-structure.sch";

    private ISchematronResource schematronResource;

    public RegisterSjukersattningValidator() {
        schematronResource = SchematronResourcePure.fromClassPath(SCHEMATRON_SCHEMA);
        if (!schematronResource.isValidSchematron()) {
            throw new IllegalArgumentException("Invalid Schematron!");
        }
    }

    public RegisterSjukersattningValidator(String location) {
        schematronResource = SchematronResourcePure.fromClassPath(location);
        if (!schematronResource.isValidSchematron()) {
            throw new IllegalArgumentException("Invalid Schematron!");
        }
    }

    public SchematronOutputType validateSchematron(@Nonnull final Source xmlContent) throws Exception {
        return schematronResource.applySchematronValidationToSVRL(xmlContent);
    }

}
