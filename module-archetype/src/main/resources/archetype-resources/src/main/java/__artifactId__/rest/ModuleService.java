#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
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
package ${package}.${artifactId}.rest;

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
import ${package}.${artifactId}.model.converter.ConverterException;
import ${package}.${artifactId}.model.converter.WebcertModelFactory;
import ${package}.${artifactId}.model.converter.ExternalToInternalConverter;
import ${package}.${artifactId}.model.converter.ExternalToTransportConverter;
import ${package}.${artifactId}.model.converter.TransportToExternalConverter;
import ${package}.${artifactId}.model.external.Utlatande;
import ${package}.${artifactId}.pdf.PdfGenerator;
import ${package}.${artifactId}.pdf.PdfGeneratorException;
import ${package}.${artifactId}.rest.dto.CertificateContentHolder;
import ${package}.${artifactId}.rest.dto.CreateNewDraftCertificateHolder;
import ${package}.${artifactId}.validator.ExternalValidator;

/**
 * The contract between the certificate module and the generic components (Intygstjänsten and Mina-Intyg).
 * 
 * @author Gustav Norbäcker, R2M
 */
public class ModuleService implements ModuleApi {

    private static final Logger LOG = LoggerFactory.getLogger(ModuleService.class);

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
    private WebcertModelFactory webcertModelFactory;

    @Context
    private HttpServletResponse httpResponse;

    /**
     * {@inheritDoc}
     */
    @Override
    public Utlatande unmarshall(se.inera.certificate.common.v1.Utlatande transportModel) {
        try {
            return transportToExternalConverter.transportToExternal(transportModel);

        } catch (ConverterException e) {
            LOG.error("Could not unmarshall transport model to external model", e);
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
            LOG.error("Could not marshall external model to transport model", e);
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
            ${package}.${artifactId}.model.internal.mi.Utlatande internalUtlatande = externalToInternalConverter
                    .fromExternalToInternal(certificateContentHolder);

            httpResponse.addHeader("Content-Disposition",
                    "filename=" + pdfGenerator.generatePdfFilename(internalUtlatande));

            return pdfGenerator.generatePDF(internalUtlatande);

        } catch (ConverterException e) {
            LOG.error("Failed to generate PDF - conversion to internal model failed", e);
            throw new BadRequestException(e);

        } catch (PdfGeneratorException e) {
            LOG.error("Failed to generate PDF for certificate!", e);
            throw new InternalServerErrorException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ${package}.${artifactId}.model.internal.mi.Utlatande convertExternalToInternal(
            CertificateContentHolder certificateContentHolder) {
        try {
            return externalToInternalConverter.fromExternalToInternal(certificateContentHolder);

        } catch (ConverterException e) {
            LOG.error("Could not convert external model to internal model", e);
            throw new BadRequestException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ${package}.${artifactId}.model.internal.mi.Utlatande convertInternalToExternal(
            ${package}.${artifactId}.model.internal.mi.Utlatande internalModel) {
        // TODO: Implement when conversion from an internal model i required.
        throw new ServiceUnavailableException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ${package}.${artifactId}.model.internal.wc.Utlatande createNewInternal(
            CreateNewDraftCertificateHolder draftCertificateHolder) {
        try {
            return webcertModelFactory.createNewWebcertDraft(draftCertificateHolder);

        } catch (ConverterException e) {
            LOG.error("Could not create a new internal Webcert model", e);
            throw new BadRequestException(e);
        }
    }
}
