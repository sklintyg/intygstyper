package se.inera.certificate.modules.rli.model.converters;

import se.inera.certificate.modules.rli.model.internal.Rekommendation;

public interface RekommendationPopulator {

    public abstract Rekommendation createAndPopulateRekommendation(
            se.inera.certificate.modules.rli.model.external.Utlatande extUtlatande);

}
