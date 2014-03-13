package se.inera.certificate.modules.ts_diabetes.model.external;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

public class Rekommendation extends se.inera.certificate.model.Rekommendation {

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
    private List<Object> varde;

    public List<Object> getVarde() {
        if (varde == null) {
            varde = new ArrayList<>();
        }
        return varde;
    }
}
