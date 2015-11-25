package se.inera.certificate.modules.rli.model.external;

import java.util.ArrayList;
import java.util.List;

import se.inera.intyg.common.support.model.PatientRelation;

public class Patient extends se.inera.intyg.common.support.model.Patient {

    private List<PatientRelation> patientrelationer;

    @Override
    public List<PatientRelation> getPatientrelationer() {
        if (patientrelationer == null) {
            patientrelationer = new ArrayList<>();
        }
        return patientrelationer;
    }
}
