package se.inera.certificate.modules.ts_diabetes.validator.transport;

import java.util.ArrayList;
import java.util.List;

import se.inera.intyg.common.schemas.Constants;
import se.inera.intygstjanster.ts.services.v1.TSDiabetesIntyg;

public class TransportValidatorInstance {

    private ValidationContext context;

    private final List<String> validationErrors;

    public TransportValidatorInstance() {
        validationErrors = new ArrayList<String>();
    }

    public List<String> validate(TSDiabetesIntyg utlatande) {
        context = new ValidationContext(utlatande);
        validateIds(utlatande);
        if (context.isHogreContext()) {
            validateHogreBehorighetContext(utlatande);
        }
        return validationErrors;
    }

    private void validateIds(TSDiabetesIntyg utlatande) {
        // PersonId
        if (utlatande.getGrundData().getPatient() != null) {
            String id = utlatande.getGrundData().getPatient().getPersonId().getRoot();
            if (!id.equals(Constants.PERSON_ID_OID) && !id.equals(Constants.SAMORDNING_ID_OID)) {
                validationErrors.add(String.format("Root for patient.personnummer should be %s or %s but was %s",
                        Constants.PERSON_ID_OID, Constants.SAMORDNING_ID_OID, id));
            }
        }
        // Läkares HSAId
        if (utlatande.getGrundData().getSkapadAv() != null) {
            checkId(utlatande.getGrundData().getSkapadAv().getPersonId().getRoot(), Constants.HSA_ID_OID, "SkapadAv.hsaId");
        }
        // Vardenhet
        if (utlatande.getGrundData().getSkapadAv().getVardenhet() != null) {
            checkId(utlatande.getGrundData().getSkapadAv().getVardenhet().getEnhetsId().getRoot(), Constants.HSA_ID_OID, "vardenhet.enhetsId");
        }
        // vardgivare
        if (utlatande.getGrundData().getSkapadAv().getVardenhet().getVardgivare() != null) {
            checkId(utlatande.getGrundData().getSkapadAv().getVardenhet().getVardgivare().getVardgivarid().getRoot(), Constants.HSA_ID_OID,
                    "vardgivarId");
        }
    }
    private void checkId(String id, String expected, String field) {
        if (!id.equals(expected)) {
            validationErrors.add(String.format("Root for %s should be %s but was %s", field, expected, id));
        }
    }

    private ValidationContext getContext() {
        return this.context;
    }

    private void validateHogreBehorighetContext(TSDiabetesIntyg utlatande) {
        if (getContext().isHogreContext()) {
            if (utlatande.getHypoglykemier().isGenomforEgenkontrollBlodsocker() == null) {
                validationErrors
                        .add("'Egenkontroll av blodsocker' must be present when intygAvser contains any of [C1, C1E, C, CE, D1, D1E, D, DE or TAXI]");
            }

            if (utlatande.getHypoglykemier().isHarAllvarligForekomstVakenTid() == null) {
                validationErrors
                        .add("'Allvarlig förekomst av hypoglykemi vaken tid' must be present when intygAvser contains any of [C1, C1E, C, CE, D1, D1E, D, DE or TAXI]");
            }
        }
    }
}
