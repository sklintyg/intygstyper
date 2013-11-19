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

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

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
import se.inera.certificate.modules.rli.rest.dto.CertificateContentHolder;
import se.inera.certificate.modules.rli.rest.dto.CreateNewDraftCertificateHolder;
import se.inera.certificate.modules.rli.validator.ExternalValidator;

/**
 * The contract between the certificate module and the generic components (Intygstjänsten and Mina-Intyg).
 * 
 * @author Gustav Norbäcker, R2M
 */
public class RliModuleService implements RliModuleApi {

    private static final Logger LOG = LoggerFactory.getLogger(RliModuleService.class);

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

    @Context
    private HttpServletResponse httpResponse;

    // @HeaderParam("X-Schema-Version")
    // private String SchemaVersion;

    /**
     * {@inheritDoc}
     */
    @Override
    public Utlatande unmarshall(se.inera.certificate.common.v1.Utlatande transportModel) {
        try {
            return transportToExternalConverter.transportToExternal(transportModel);

        } catch (ConverterException e) {
            LOG.error("Error in unmarshall", e);
            throw new BadRequestException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public se.inera.certificate.common.v1.Utlatande marshall(Utlatande externalModel) {
        try {
            return externalToTransportConverter.externalToTransport(externalModel);

        } catch (ConverterException e) {
            LOG.error("Error in marshall", e);
            throw new BadRequestException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String validate(Utlatande utlatande) {

        List<String> validationErrors = externalValidator.validate(utlatande);

        if (validationErrors.isEmpty()) {
            return null;

        } else {
            String response = Strings.join(",", validationErrors);
            throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity(response).build());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] pdf(CertificateContentHolder certificateContentHolder) {
        try {
            se.inera.certificate.modules.rli.model.internal.Utlatande internalUtlatande = externalToInternalConverter
                    .fromExternalToInternal(certificateContentHolder);

            httpResponse.addHeader("Content-Disposition",
                    "filename=" + pdfGenerator.generatePdfFilename(internalUtlatande));

            return pdfGenerator.generatePDF(internalUtlatande);

        } catch (Exception p) {
            LOG.error("Failed to generate PDF for certificate!", p);
            throw new InternalServerErrorException(p);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public se.inera.certificate.modules.rli.model.internal.Utlatande convertExternalToInternal(
            CertificateContentHolder certificateContentHolder) {
        try {
            return externalToInternalConverter.fromExternalToInternal(certificateContentHolder);
        } catch (ConverterException e) {
            throw new BadRequestException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public se.inera.certificate.modules.rli.model.internal.Utlatande convertInternalToExternal(
            se.inera.certificate.modules.rli.model.internal.Utlatande internalModel) {
        // TODO: Implement when conversion from an internal model i required.
        throw new ServiceUnavailableException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public se.inera.certificate.modules.rli.model.edit.Utlatande createNewInternal(CreateNewDraftCertificateHolder draftCertificateHolder) {
        try {
            return editModelFactory.createEditableUtlatande(draftCertificateHolder);
        } catch (ConverterException e) {
            throw new BadRequestException(e);
        }
    }
}
