package se.inera.certificate.modules.rli.validator;

import java.util.List;

import se.inera.certificate.modules.rli.model.external.Utlatande;

/**
 * 
 * @author erik
 * 
 */
public interface ExternalValidator {
    /**
     * Validates an external Utlatande.
     * 
     * @param utlatande
     *            se.inera.certificate.modules.rli.model.external.Utlatande
     * @return List of validation errors, or an empty string if validated correctly
     */
    List<String> validate(Utlatande utlatande);
}
