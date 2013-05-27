package se.inera.certificate.modules.fk7263.rest;

import se.inera.certificate.integration.ModuleRestApi;
import se.inera.certificate.integration.v1.Lakarutlatande;
import se.inera.certificate.model.Ovrigt;
import se.inera.certificate.model.Valideringsresultat;

/**
 * @author andreaskaltenbach
 */
public class Fk7263ModuleApi implements ModuleRestApi {

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
