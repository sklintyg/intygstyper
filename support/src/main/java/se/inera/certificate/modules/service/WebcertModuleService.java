package se.inera.certificate.modules.service;

/**
 * Interface for exposing Webcert services that can be used by modules.
 * 
 * @author npet
 *
 */
public interface WebcertModuleService {

    /**
     * Validates a diagnosis code using the DiagnosService.
     * 
     * @param diagnosisCode
     * @return true if the code matches a diagnosis, false otherwise.
     */
    public boolean validateDiagnosisCode(String codeFragment);
    
}
