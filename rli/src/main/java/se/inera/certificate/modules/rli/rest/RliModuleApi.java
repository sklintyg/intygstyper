package se.inera.certificate.modules.rli.rest;

import se.inera.certificate.integration.v1.Lakarutlatande;
import se.inera.certificate.model.Valideringsresultat;
import se.inera.certificate.modules.rli.model.Resa;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * @author andreaskaltenbach
 */
public class RliModuleApi {

    @POST
    @Path( "/extension" )
    @Consumes( MediaType.APPLICATION_XML )
    @Produces(MediaType.APPLICATION_JSON)
    public Resa extract(Lakarutlatande intyg) {
        return new Resa(intyg.getResmal(), intyg.getResenar());
    }

    @POST
    @Path( "/valid" )
    @Consumes( MediaType.APPLICATION_XML )
    @Produces( MediaType.APPLICATION_JSON )
    public Valideringsresultat validate(Lakarutlatande intyg) {
        List<String> errors = new ArrayList<>();

        if (intyg.getResenar() == null) {
            errors.add("Resenär is empty");
        }

        if (intyg.getResmal() == null) {
            errors.add("Resmal is empty");
        }

        return new Valideringsresultat(errors);
    }
}
