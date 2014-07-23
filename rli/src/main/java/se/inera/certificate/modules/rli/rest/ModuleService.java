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

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

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
import se.inera.certificate.modules.rli.validator.Validator;
import se.inera.certificate.modules.support.ApplicationOrigin;
import se.inera.certificate.modules.support.api.ModuleApi;
import se.inera.certificate.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.certificate.modules.support.api.dto.ExternalModelHolder;
import se.inera.certificate.modules.support.api.dto.ExternalModelResponse;
import se.inera.certificate.modules.support.api.dto.HoSPersonal;
import se.inera.certificate.modules.support.api.dto.InternalModelHolder;
import se.inera.certificate.modules.support.api.dto.InternalModelResponse;
import se.inera.certificate.modules.support.api.dto.PdfResponse;
import se.inera.certificate.modules.support.api.dto.TransportModelHolder;
import se.inera.certificate.modules.support.api.dto.TransportModelResponse;
import se.inera.certificate.modules.support.api.dto.TransportModelVersion;
import se.inera.certificate.modules.support.api.dto.ValidateDraftResponse;
import se.inera.certificate.modules.support.api.exception.ModuleConverterException;
import se.inera.certificate.modules.support.api.exception.ModuleCopyNotSupportedException;
import se.inera.certificate.modules.support.api.exception.ModuleException;
import se.inera.certificate.modules.support.api.exception.ModuleSystemException;
import se.inera.certificate.modules.support.api.exception.ModuleValidationException;
import se.inera.certificate.modules.support.api.exception.ModuleVersionUnsupportedException;

import com.fasterxml.jackson.databind.ObjectMapper;

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
    private Validator validator;

    @Autowired
    private ExternalToInternalConverter externalToInternalConverter;

    @Autowired
    private InternalToExternalConverter internalToExternalConverter;

    @Autowired
    private PdfGenerator pdfGenerator;

    @Autowired
    private WebcertModelFactory webcertModelFactory;

    @Autowired
    @Qualifier("rli-jaxbContext")
    private JAXBContext jaxbContext;

    @Autowired
    @Qualifier("rli-objectMapper")
    private ObjectMapper objectMapper;

    /**
     * {@inheritDoc}
     *
     * @throws ModuleException
     */
    @Override
    public ExternalModelResponse unmarshall(TransportModelHolder transportModel) throws ModuleException {
        try {
            return toExternalModelResponse(transportToExternalConverter.convert(getTransport(transportModel)));

        } catch (ConverterException e) {
            LOG.error("Could not unmarshall transport model to external model", e);
            throw new ModuleConverterException("Could not unmarshall transport model to external model", e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws ModuleException
     */
    @Override
    public TransportModelResponse marshall(ExternalModelHolder externalModel, TransportModelVersion version)
            throws ModuleException {
        if (!version.equals(TransportModelVersion.UTLATANDE_V1)) {
            throw new ModuleVersionUnsupportedException("ivar does not support transport model version " + version);
        }

        try {
            return toTransportModelResponse(externalToTransportConverter.convert(getExternal(externalModel)));

        } catch (ConverterException e) {
            LOG.error("Could not marshall external model to transport model", e);
            throw new ModuleConverterException("Could not unmarshall transport model to external model", e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws ModuleException
     */
    @Override
    public String validate(ExternalModelHolder externalHolder) throws ModuleException {

        List<String> validationErrors = validator.validateExternal(getExternal(externalHolder));

        if (validationErrors.isEmpty()) {
            return null;

        } else {
            String response = Strings.join(",", validationErrors);
            throw new ModuleValidationException(Collections.singletonList(response));
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws ModuleException
     */
    @Override
    public PdfResponse pdf(ExternalModelHolder externalModel, ApplicationOrigin applicationOrigin) throws ModuleException {
        try {
            se.inera.certificate.modules.rli.model.internal.mi.Utlatande internalUtlatande = externalToInternalConverter
                    .convert(getExternal(externalModel));

            return new PdfResponse(pdfGenerator.generatePDF(internalUtlatande),
                    pdfGenerator.generatePdfFilename(internalUtlatande));

        } catch (ConverterException e) {
            LOG.error("Failed to generate PDF - conversion to internal model failed", e);
            throw new ModuleConverterException("Failed to generate PDF - conversion to internal model failed", e);

        } catch (PdfGeneratorException e) {
            LOG.error("Failed to generate PDF for certificate!", e);
            throw new ModuleSystemException("Failed to generate PDF for certificate!", e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws ModuleException
     */
    @Override
    public InternalModelResponse convertExternalToInternal(ExternalModelHolder externalHolder) throws ModuleException {
        try {
            return toInteralMiModelResponse(externalToInternalConverter.convert(getExternal(externalHolder)));

        } catch (ConverterException e) {
            LOG.error("Could not convert external model to internal model", e);
            throw new ModuleConverterException("Could not convert external model to internal model", e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws ModuleException
     */
    @Override
    public ExternalModelResponse convertInternalToExternal(InternalModelHolder internalHolder) throws ModuleException {
        try {
            return toExternalModelResponse(internalToExternalConverter.convert(getInternal(internalHolder)));
        } catch (ConverterException e) {
            LOG.error("Could not convert external model to internal model", e);
            throw new ModuleConverterException("Could not convert external model to internal model", e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws ModuleException
     */
    @Override
    public InternalModelResponse createNewInternal(CreateNewDraftHolder draftCertificateHolder) throws ModuleException {
        try {
            return toInteralWcModelResponse(webcertModelFactory.createNewWebcertDraft(draftCertificateHolder));

        } catch (ConverterException e) {
            LOG.error("Could not create a new internal Webcert model", e);
            throw new ModuleConverterException("Could not create a new internal Webcert model", e);
        }
    }

    @Override
    public InternalModelResponse createNewInternalFromTemplate(CreateNewDraftHolder draftCertificateHolder, ExternalModelHolder template) throws ModuleException {
        throw new ModuleCopyNotSupportedException("Not yet");
    }

    @Override
    public ValidateDraftResponse validateDraft(InternalModelHolder internalModel) throws ModuleException {
        // TODO Auto-generated method stub
        return null;
    }

    // Response transformations:
    private se.inera.certificate.rli.model.v1.Utlatande getTransport(TransportModelHolder transportModel)
            throws ModuleException {
        try {
            return (se.inera.certificate.rli.model.v1.Utlatande) jaxbContext.createUnmarshaller().unmarshal(
                    new StringReader(transportModel.getTransportModel()));

        } catch (ValidationException e) {
            throw new ModuleValidationException(Collections.singletonList(e.getMessage()),
                    "XML validation of transport model failed", e);

        } catch (JAXBException e) {
            throw new ModuleSystemException("Failed to unmarshall transport model", e);
        }
    }

    private se.inera.certificate.modules.rli.model.external.Utlatande getExternal(ExternalModelHolder externalModel)
            throws ModuleException {
        try {
            return objectMapper.readValue(externalModel.getExternalModel(),
                    se.inera.certificate.modules.rli.model.external.Utlatande.class);

        } catch (IOException e) {
            throw new ModuleSystemException("Failed to deserialize external model", e);
        }
    }

    private se.inera.certificate.modules.rli.model.internal.wc.Utlatande getInternal(InternalModelHolder internalHolder)
            throws ModuleException {
        try {
            return objectMapper.readValue(internalHolder.getInternalModel(),
                    se.inera.certificate.modules.rli.model.internal.wc.Utlatande.class);

        } catch (IOException e) {
            throw new ModuleSystemException("Failed to deserialize external model", e);
        }
    }

    private TransportModelResponse toTransportModelResponse(se.inera.certificate.rli.model.v1.Utlatande transportModel)
            throws ModuleException {
        try {
            StringWriter writer = new StringWriter();
            jaxbContext.createMarshaller().marshal(transportModel, writer);
            return new TransportModelResponse(writer.toString());

        } catch (JAXBException e) {
            throw new ModuleSystemException("Failed to marshall transport model", e);
        }
    }

    private ExternalModelResponse toExternalModelResponse(
            se.inera.certificate.modules.rli.model.external.Utlatande externalModel) throws ModuleException {
        try {
            StringWriter writer = new StringWriter();
            objectMapper.writeValue(writer, externalModel);
            return new ExternalModelResponse(writer.toString(), externalModel);

        } catch (IOException e) {
            throw new ModuleSystemException("Failed to serialize external model", e);
        }
    }

    private InternalModelResponse toInteralWcModelResponse(
            se.inera.certificate.modules.rli.model.internal.wc.Utlatande internalModel) throws ModuleException {
        try {
            StringWriter writer = new StringWriter();
            objectMapper.writeValue(writer, internalModel);
            return new InternalModelResponse(writer.toString());

        } catch (IOException e) {
            throw new ModuleSystemException("Failed to serialize internal model", e);
        }
    }

    private InternalModelResponse toInteralMiModelResponse(
            se.inera.certificate.modules.rli.model.internal.mi.Utlatande internalModel) throws ModuleException {
        try {
            StringWriter writer = new StringWriter();
            objectMapper.writeValue(writer, internalModel);
            return new InternalModelResponse(writer.toString());

        } catch (IOException e) {
            throw new ModuleSystemException("Failed to serialize internal model", e);
        }
    }

    @Override
    public String getComplementaryInfo(ExternalModelHolder externalModel) throws ModuleException {
        Utlatande utlatande = getExternal(externalModel);
        return String.format("%s %s", utlatande.getArrangemang().getPlats(), utlatande.getArrangemang().getArrangemangstid().getFrom());
    }

    @Override
    public InternalModelResponse updateInternal(InternalModelHolder internalModel, HoSPersonal hosPerson) throws ModuleException {
        return toInteralWcModelResponse(getInternal(internalModel));
    }
}
