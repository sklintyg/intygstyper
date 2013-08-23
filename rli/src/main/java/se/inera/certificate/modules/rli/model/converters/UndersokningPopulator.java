package se.inera.certificate.modules.rli.model.converters;

import se.inera.certificate.modules.rli.model.internal.Undersokning;

public interface UndersokningPopulator {

    Undersokning createAndPopulateUndersokning(se.inera.certificate.modules.rli.model.external.Utlatande extUtlatande);

}
