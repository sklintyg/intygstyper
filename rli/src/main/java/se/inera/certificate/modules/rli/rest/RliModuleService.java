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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.inera.certificate.model.util.Strings;
import se.inera.certificate.modules.rli.model.converter.ConverterException;
import se.inera.certificate.modules.rli.model.converter.ExternalToInternalConverter;
import se.inera.certificate.modules.rli.model.converter.ExternalToTransportConverter;
import se.inera.certificate.modules.rli.model.converter.InternalToExternalConverter;
import se.inera.certificate.modules.rli.model.converter.TransportToExternalConverter;
import se.inera.certificate.modules.rli.model.converter.WebcertModelFactory;
import se.inera.certificate.modules.rli.model.external.Utlatande;
import se.inera.certificate.modules.rli.pdf.PdfGenerator;
import se.inera.certificate.modules.rli.pdf.PdfGeneratorException;
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
    private InternalToExternalConverter internalToExternalConverter;

    @Autowired
    private PdfGenerator pdfGenerator;

    @Autowired
    private WebcertModelFactory webcertModelFactory;

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
            return transportToExternalConverter.convert(transportModel);

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
            return externalToTransportConverter.convert(externalModel);

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
            se.inera.certificate.modules.rli.model.internal.mi.Utlatande internalUtlatande = externalToInternalConverter
                    .convert(certificateContentHolder);

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
    public se.inera.certificate.modules.rli.model.internal.mi.Utlatande convertExternalToInternal(
            CertificateContentHolder certificateContentHolder) {
        try {
            return externalToInternalConverter.convert(certificateContentHolder);

        } catch (ConverterException e) {
            LOG.error("Could not convert external model to internal model", e);
            throw new BadRequestException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public se.inera.certificate.modules.rli.model.external.Utlatande convertInternalToExternal(
            se.inera.certificate.modules.rli.model.internal.wc.Utlatande internalModel) {
        try {
            return internalToExternalConverter.convert(internalModel);
        } catch (ConverterException e) {
            LOG.error("Could not convert external model to internal model", e);
            throw new BadRequestException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public se.inera.certificate.modules.rli.model.internal.wc.Utlatande createNewInternal(
            CreateNewDraftCertificateHolder draftCertificateHolder) {
        try {
            return webcertModelFactory.createNewWebcertDraft(draftCertificateHolder);

        } catch (ConverterException e) {
            LOG.error("Could not create a new internal Webcert model", e);
            throw new BadRequestException(e);
        }
    }
}
