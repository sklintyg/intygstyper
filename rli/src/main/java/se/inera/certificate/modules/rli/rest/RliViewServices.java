package se.inera.certificate.modules.rli.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import se.inera.certificate.modules.rli.model.internal.Utlatande;

@Path("/view")
public class RliViewServices {

	@GET
	@Path("/utlatande/{utlatande-id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Utlatande getUtlatande(@PathParam("utlatande-id") String utlatandeId) {

		return null;
	}
}
