package se.inera.certificate.modules.sjukpenning.integration;

import javax.annotation.Nonnull;
import javax.xml.transform.Source;

import org.oclc.purl.dsdl.svrl.SchematronOutputType;

import com.helger.schematron.ISchematronResource;
import com.helger.schematron.pure.SchematronResourcePure;

public class RegisterSjukpenningValidator {
    private static final String SCHEMATRON_SCHEMA = "sjukpenning-structure.sch";

    private ISchematronResource schematronResource;

    public RegisterSjukpenningValidator() {
        schematronResource = SchematronResourcePure.fromClassPath(SCHEMATRON_SCHEMA);
        if (!schematronResource.isValidSchematron()) {
            throw new IllegalArgumentException("Invalid Schematron!");
        }
    }

    public SchematronOutputType validateSchematron(@Nonnull final Source xmlContent) throws Exception {
        return schematronResource.applySchematronValidationToSVRL(xmlContent);
    }

}
