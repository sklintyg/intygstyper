package se.inera.certificate.validate;

import java.util.List;

/**
 * Validator for a given <code>root</code> of an {@link se.inera.certificate.model} object.
 *
 * @author Gustav Norbäcker, R2M
 */
public interface RootValidator {

    /**
     * The root that this validator supports.
     *
     * @return The name of the {@link se.inera.certificate.model} <code>root</code>.
     */
    String getRoot();

    /**
     * Performs validation of the {@link se.inera.certificate.model} <code>extension</code> of the <code>root</code> that this validator
     * supports.
     *
     * @param extension The extension to validate.
     * @return A list of validation messages. An empty string if validation was successful.
     */
    List<String> validateExtension(String extension);
}
