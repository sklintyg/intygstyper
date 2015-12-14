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
