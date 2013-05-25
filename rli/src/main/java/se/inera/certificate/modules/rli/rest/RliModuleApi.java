package se.inera.certificate.modules.rli.rest;

import se.inera.certificate.integration.ModuleRestApi;
import se.inera.certificate.integration.v1.Lakarutlatande;
import se.inera.certificate.model.Ovrigt;
import se.inera.certificate.model.Valideringsresultat;

/**
 * @author andreaskaltenbach
 */
public class RliModuleApi implements ModuleRestApi {

    @Override
    public Ovrigt extract(Lakarutlatande lakarutlatande) {
        Ovrigt ovrigt = new Ovrigt();

        return ovrigt;
    }

    @Override
    public Valideringsresultat validate(Lakarutlatande lakarutlatande) {
        return new Valideringsresultat(null);
    }


}
