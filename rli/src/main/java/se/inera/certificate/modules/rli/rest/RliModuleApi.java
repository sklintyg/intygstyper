/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Certificate Modules (http://code.google.com/p/inera-certificate-modules).
 *
 * Inera Certificate Modules is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Inera Certificate Modules is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.certificate.modules.rli.rest;

import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.inera.certificate.model.util.Strings;
import se.inera.certificate.modules.rli.model.converters.ConverterException;
import se.inera.certificate.modules.rli.model.converters.ExternalToInternalConverter;
import se.inera.certificate.modules.rli.model.converters.ExternalToTransportConverter;
import se.inera.certificate.modules.rli.model.converters.TransportToExternalConverter;
import se.inera.certificate.modules.rli.model.external.Utlatande;
import se.inera.certificate.modules.rli.model.factory.EditModelFactory;
import se.inera.certificate.modules.rli.pdf.PdfGenerator;
import se.inera.certificate.modules.rli.pdf.PdfGeneratorException;
import se.inera.certificate.modules.rli.rest.dto.CertificateContentHolder;
import se.inera.certificate.modules.rli.rest.dto.CreateNewDraftCertificateHolder;
import se.inera.certificate.modules.rli.validator.ExternalValidator;

/**
 * The contract between the certificate module and the generic components
 * (Intygstjänsten and Mina-Intyg).
 * 
 * @author Gustav Norbäcker, R2M
 */
public class RliModuleApi {

    private static final Logger LOG = LoggerFactory.getLogger(RliModuleApi.class);

    @Autowired
    private TransportToExternalConverter transportToExternalConverter;

    @Autowired
    private ExternalToTransportConverter externalToTransportConverter;

    @Autowired
    private ExternalValidator externalValidator;

    @Autowired
    private ExternalToInternalConverter externalToInternalConverter;

    @Autowired
    private PdfGenerator pdfGenerator;

    @Autowired
    private EditModelFactory editModelFactory;

    /**
     * Handles conversion from the transport model (XML) to the external JSON
     * model.
     * 
     * @param transportModel
     *            The transport model to convert.
     * 
     * @return An instance of the external model, generated from the transport
     *         model.
     */
    @POST
    @Path("/unmarshall")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_JSON)
    public Response unmarshall(se.inera.certificate.common.v1.Utlatande transportModel) {
        Utlatande externalModel = null;
        Response response = null;
        try {
            externalModel = transportToExternalConverter.transportToExternal(transportModel);
            response = Response.status(Response.Status.OK).entity(externalModel).build();
        } catch (ConverterException ce) {
            LOG.error("Error in unmarshall", ce);
            response = Response.status(Response.Status.BAD_REQUEST).build();
        }
        return response;
    }

    @POST
    @Path("/marshall")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_XML)
    public Response marshall(@HeaderParam("X-Schema-Version") String version, String moduleExternalJson) {
        // TODO Auto-generated method stub
        return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
    }

    /**
     * Validates the external model. If the validation succeeds, a empty result
     * will be returned. If the validation fails, a list of validation messages
     * will be returned as a HTTP 400.
     * 
     * @param externalModel
     *            The external model to validate.
     * @return
     */
    @POST
    @Path("/valid")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response validate(Utlatande utlatande) {

        List<String> validationErrors = externalValidator.validate(utlatande);

        if (validationErrors.isEmpty()) {
            return Response.ok().build();
        } else {
            String response = Strings.join(",", validationErrors);
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
    }

    /**
     * Generates a PDF from the external model.
     * 
     * @param externalModel
     *            The external model to generate a PDF from.
     * 
     * @return A binary stream containing a PDF template populated with the
     *         information of the external model.
     */
    @POST
    @Path("/pdf")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/pdf")
    public Response pdf(CertificateContentHolder certificateContentHolder) {
        try {
            
            se.inera.certificate.modules.rli.model.internal.Utlatande internalUtlatande = externalToInternalConverter
                    .fromExternalToInternal(certificateContentHolder);
            
            byte[] generatedPDF = pdfGenerator.generatePDF(internalUtlatande);
            
            return Response.ok(generatedPDF)
                    .header("Content-Disposition", "filename=" + pdfGenerator.generatePdfFilename(internalUtlatande))
                    .build();
            
        } catch (Exception p) {
            LOG.error("Failed to generate PDF for certificate!", p);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

    }

    /**
     * Handles conversion from the external model to the internal model.
     * 
     * @param externalModel
     *            The external model to convert.
     * 
     * @return An instance of the internal model, generated from the external
     *         model.
     */
    @PUT
    @Path("/internal")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response convertExternalToInternal(CertificateContentHolder certificateContentHolder) {

        se.inera.certificate.modules.rli.model.internal.Utlatande internalModel;

        Response response = null;

        try {
            internalModel = externalToInternalConverter.fromExternalToInternal(certificateContentHolder);
            response = Response.status(Response.Status.OK).entity(internalModel).build();
        } catch (ConverterException e) {
            response = Response.status(Response.Status.BAD_REQUEST).build();
        }

        return response;
    }

    /**
     * Handles conversion from the internal model to the external model.
     * 
     * @param internalModel
     *            The internal model to convert.
     * 
     * @return An instance of the external model, generated from the internal
     *         model.
     */
    @PUT
    @Path("/external")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response convertInternalToExternal(se.inera.certificate.modules.rli.model.internal.Utlatande internalModel) {
        // TODO: Change the return type of this method to the internal model
        // POJO.
        // TODO: Implement when conversion from an internal model i required.
        return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
    }

    /**
     * Creates a new editable model for use in WebCert. The model is pre
     * populated using data contained in the CreateNewDraftCertificateHolder
     * parameter.
     * 
     * @param draftCertificateHolder
     * @return
     */
    @POST
    @Path("/new")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewInternal(CreateNewDraftCertificateHolder draftCertificateHolder) {

        se.inera.certificate.modules.rli.model.edit.Utlatande editableUtlatande = editModelFactory
                .createEditableUtlatande(draftCertificateHolder);

        return Response.ok(editableUtlatande).build();
    }
}
