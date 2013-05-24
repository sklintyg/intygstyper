package se.inera.certificate.modules.rli.rest;

import se.inera.certificate.integration.ModuleRestApi;
import se.inera.certificate.integration.v1.LakarutlatandeType;
import se.inera.certificate.model.Ovrigt;
import se.inera.certificate.model.Valideringsresultat;

/**
 * @author andreaskaltenbach
 */
public class RliModuleApi implements ModuleRestApi {

    @Override
    public Ovrigt extract(LakarutlatandeType lakarutlatandeType) {
        Ovrigt ovrigt = new Ovrigt();

        return ovrigt;
    }

    @Override
    public Valideringsresultat validate(LakarutlatandeType lakarutlatandeType) {
        return new Valideringsresultat(null);
    }


}
