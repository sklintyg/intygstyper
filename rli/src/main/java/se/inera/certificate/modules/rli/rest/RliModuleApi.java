package se.inera.certificate.modules.rli.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import se.inera.certificate.modules.rli.model.converters.TransportToExternalConverter;
import se.inera.certificate.modules.rli.model.external.Utlatande;

/**
 * The contract between the certificate module and the generic components (Intygstjänsten and Mina-Intyg).
 * 
 * @author Gustav Norbäcker, R2M
 */
public class RliModuleApi {

	@Autowired
	private TransportToExternalConverter transportToExternalConverter;

	/**
	 * Handles conversion from the transport model (XML) to the external JSON model.
	 * 
	 * @param transportModel
	 *            The transport model to convert.
	 * 
	 * @return An instance of the external model, generated from the transport model.
	 */
	@PUT
	@Path("/unmarshall")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_JSON)
	public Utlatande unmarshall(se.inera.certificate.common.v1.Utlatande transportModel) {
		return transportToExternalConverter.transportToExternal(transportModel);
	}

	/**
	 * Handles conversion from the external JSON model to the transport model (XML).
	 * 
	 * @param externalModel
	 *            The external model to convert.
	 * 
	 * @return An instance of the transport model, generated from the external model.
	 */
	@PUT
	@Path("/marshall")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_XML)
	public se.inera.certificate.common.v1.Utlatande marshall(Utlatande externalModel) {
		// TODO: Implement when conversion from the external model i required.
		return null;
	}

	/**
	 * Validates the external model. If the validation succeeds, a empty result will be returned. If the validation
	 * fails, a list of validation messages will be returned as a HTTP 400.
	 * 
	 * @param externalModel
	 *            The external model to validate.
	 * @return
	 */
	@POST
	@Path("/valid")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.TEXT_PLAIN)
	public Response validate(Utlatande externalModel) {
		// TODO: Implement when validation is required.
		return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
	}

	/**
	 * Generates a PDF from the external model.
	 * 
	 * @param externalModel
	 *            The external model to generate a PDF from.
	 * 
	 * @return A binary stream containing a PDF template populated with the information of the external model.
	 */
	@POST
	@Path("/pdf")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/pdf")
	public Response pdf(Utlatande externalModel) {
		// TODO: Implement when PDF generation is required.
		return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
	}

	/**
	 * Handles conversion from the external model to the internal model.
	 * 
	 * @param externalModel
	 *            The external model to convert.
	 * 
	 * @return An instance of the internal model, generated from the external model.
	 */
	@PUT
	@Path("/internal")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response convertExternalToInternal(Utlatande externalModel) {
		// TODO: Change the return type of this method to the internal model POJO.
		// TODO: Implement when conversion from the external model i required.
		return null;
	}

	/**
	 * Handles conversion from the internal model to the external model.
	 * 
	 * @param internalModel
	 *            The internal model to convert.
	 * 
	 * @return An instance of the external model, generated from the internal model.
	 */
	@PUT
	@Path("/external")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Utlatande convertInternalToExternal(Object internalModel) {
		// TODO: Change the return type of this method to the internal model POJO.
		// TODO: Implement when conversion from an internal model i required.
		return null;
	}
}