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
package ${package}.${artifactId-safe}.rest;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.ValidationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.xml.sax.SAXException;

import ${package}.support.ApplicationOrigin;
import ${package}.support.api.ModuleApi;
import ${package}.support.api.dto.CreateNewDraftHolder;
import ${package}.support.api.dto.ExternalModelHolder;
import ${package}.support.api.dto.ExternalModelResponse;
import ${package}.support.api.dto.HoSPersonal;
import ${package}.support.api.dto.InternalModelHolder;
import ${package}.support.api.dto.InternalModelResponse;
import ${package}.support.api.dto.PdfResponse;
import ${package}.support.api.dto.TransportModelHolder;
import ${package}.support.api.dto.TransportModelResponse;
import ${package}.support.api.dto.TransportModelVersion;
import ${package}.support.api.dto.ValidateDraftResponse;
import ${package}.support.api.exception.ModuleConverterException;
import ${package}.support.api.exception.ModuleException;
import ${package}.support.api.exception.ModuleSystemException;
import ${package}.support.api.exception.ModuleValidationException;
import ${package}.support.api.exception.ModuleVersionUnsupportedException;
import ${package}.${artifactId-safe}.model.converter.ConverterException;
import ${package}.${artifactId-safe}.model.converter.ExternalToInternalConverter;
import ${package}.${artifactId-safe}.model.converter.ExternalToTransportConverter;
import ${package}.${artifactId-safe}.model.converter.InternalToExternalConverter;
import ${package}.${artifactId-safe}.model.converter.TransportToExternalConverter;
import ${package}.${artifactId-safe}.model.converter.WebcertModelFactory;
import ${package}.${artifactId-safe}.model.external.Utlatande;
import ${package}.${artifactId-safe}.pdf.PdfGenerator;
import ${package}.${artifactId-safe}.pdf.PdfGeneratorException;
import ${package}.${artifactId-safe}.validator.Validator;
import se.inera.certificate.xml.SchemaValidatorBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The contract between the certificate module and the generic components (Intygstjänsten and Mina-Intyg).
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
    @Qualifier("${artifactId}-jaxbContext")
    private JAXBContext jaxbContext;

    @Autowired
    @Qualifier("${artifactId}-objectMapper")
    private ObjectMapper objectMapper;

    private final Schema transportSchema;

    public ModuleService() throws Exception {
        SchemaValidatorBuilder builder = new SchemaValidatorBuilder();
        Source rootSource = builder.registerResource("schemas/${artifactId}_model.xsd");
        builder.registerResource("schemas/${artifactId}_model_extension.xsd");
        builder.registerResource("schemas/core_components/clinicalprocess_healthcond_types_0.9.xsd");
        builder.registerResource("schemas/core_components/iso_dt_subset_1.0.xsd");

        transportSchema = builder.build(rootSource);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExternalModelResponse unmarshall(TransportModelHolder transportModel) throws ModuleException {
        try {
            se.inera.certificate.${artifactId-safe}.model.v1.Utlatande utlatande = getTransport(transportModel);

            // Perform validation agains XML schema
            validateSchema(transportModel.getTransportModel());
            // Convert to external model
            Utlatande externalUtlatande = transportToExternalConverter.convert(utlatande);
            // Validate external model
            validate(externalUtlatande);

            return toExternalModelResponse(externalUtlatande);

        } catch (ConverterException e) {
            LOG.error("Could not unmarshall transport model to external model", e);
            throw new ModuleConverterException("Could not unmarshall transport model to external model", e);
        }
    }

    /**
     * Validates the XML of a {@link Utlatande}.
     *
     * @param utlatandeXml The xml as a string.
     * @throws ModuleValidationException
     */
    private void validateSchema(String utlatandeXml) throws ModuleValidationException {
        try {
            javax.xml.validation.Validator validator = transportSchema.newValidator();
            validator.validate(new StreamSource(new StringReader(utlatandeXml)));

        } catch (SAXException e) {
            throw new ModuleValidationException(Collections.singletonList(e.getMessage()), e);

        } catch (IOException e) {
            LOG.error("Failed to validate message against schema", e);
            throw new RuntimeException("Failed to validate message against schema", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TransportModelResponse marshall(ExternalModelHolder externalModel, TransportModelVersion version) throws ModuleException {
        if (!version.equals(TransportModelVersion.UTLATANDE_V1)) {
            throw new ModuleVersionUnsupportedException("${artifactId} does not support transport model version "
                    + version);
        }

        try {
            return toTransportModelResponse(externalToTransportConverter.convert(getExternal(externalModel)));

        } catch (ConverterException e) {
            LOG.error("Could not marshall external model to transport model", e);
            throw new ModuleConverterException("Could not marshall external model to transport model", e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws ModuleException
     */
    @Override
    public void validate(ExternalModelHolder externalModelHolder) throws ModuleException {
        validate(getExternal(externalModelHolder));
    }

    private String validate(Utlatande utlatande) throws ModuleException {
        List<String> validationErrors = validator.validateExternal(utlatande);

        if (validationErrors.isEmpty()) {
            return null;

        } else {
            throw new ModuleValidationException(validationErrors);
        }
    }

    @Override
    public ValidateDraftResponse validateDraft(InternalModelHolder internalModel) throws ModuleException {
        return validator.validateInternal(getInternal(internalModel));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PdfResponse pdf(ExternalModelHolder externalModel, ApplicationOrigin applicationOrigin) throws ModuleException {
        try {
            ${package}.${artifactId-safe}.model.internal.Utlatande internalUtlatande = externalToInternalConverter
                    .convert(getExternal(externalModel));
            return new PdfResponse(pdfGenerator.generatePDF(internalUtlatande, applicationOrigin),
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
     */
    @Override
    public InternalModelResponse convertExternalToInternal(ExternalModelHolder externalModel) throws ModuleException {
        try {
            return toInteralModelResponse(externalToInternalConverter.convert(getExternal(externalModel)));

        } catch (ConverterException e) {
            LOG.error("Could not convert external model to internal model", e);
            throw new ModuleConverterException("Could not convert external model to internal model", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExternalModelResponse convertInternalToExternal(InternalModelHolder internalModel) throws ModuleException {
        try {
            return toExternalModelResponse(internalToExternalConverter.convert(getInternal(internalModel)));
        } catch (ConverterException e) {
            LOG.error("Could not convert external model to internal model", e);
            throw new ModuleConverterException("Could not convert external model to internal model", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InternalModelResponse createNewInternal(CreateNewDraftHolder draftCertificateHolder) throws ModuleException {
        try {
            return toInteralModelResponse(webcertModelFactory.createNewWebcertDraft(draftCertificateHolder, null));

        } catch (ConverterException e) {
            LOG.error("Could not create a new internal Webcert model", e);
            throw new ModuleConverterException("Could not create a new internal Webcert model", e);
        }
    }

    @Override
    public InternalModelResponse createNewInternalFromTemplate(CreateNewDraftHolder draftCertificateHolder, ExternalModelHolder template) throws ModuleException {
        try {
            ${package}.${artifactId-safe}.model.internal.Utlatande internal = externalToInternalConverter.convert(getExternal(template));
            return toInteralModelResponse(webcertModelFactory.createNewWebcertDraft(draftCertificateHolder, internal));
        } catch (ConverterException e) {
            LOG.error("Could not create a new internal Webcert model", e);
            throw new ModuleConverterException("Could not create a new internal Webcert model", e);
        }
    }

    @Override
    public String getComplementaryInfo(ExternalModelHolder externalModel) throws ModuleException {
        // TODO implement

        return null;
    }

    @Override
    public InternalModelResponse updateInternal(InternalModelHolder internalModel, HoSPersonal hosPerson, LocalDateTime signingDate) throws ModuleException {
        try {
            ${package}.${artifactId-safe}.model.internal.Utlatande utlatande = getInternal(internalModel);
            utlatande.setSigneringsdatum(signingDate);
            utlatande.getSkapadAv().setPersonid(hosPerson.getHsaId());
            utlatande.getSkapadAv().setFullstandigtNamn(hosPerson.getNamn());

            // TODO Add additional fields here

            return toInteralModelResponse(utlatande);

        } catch (ModuleException e) {
            throw new ModuleException("Convert error of internal model", e);
        }
    }

    private se.inera.certificate.${artifactId-safe}.model.v1.Utlatande getTransport(TransportModelHolder transportModel)
            throws ModuleException {
        try {
            return (se.inera.certificate.${artifactId-safe}.model.v1.Utlatande) jaxbContext.createUnmarshaller().unmarshal(
                    new StringReader(transportModel.getTransportModel()));

        } catch (ValidationException e) {
            throw new ModuleValidationException(Collections.singletonList(e.getMessage()),
                    "XML validation of transport model failed", e);

        } catch (JAXBException e) {
            throw new ModuleSystemException("Failed to unmarshall transport model", e);
        }
    }

    private ${package}.${artifactId-safe}.model.external.Utlatande getExternal(ExternalModelHolder externalModel)
            throws ModuleException {
        try {
            return objectMapper.readValue(externalModel.getExternalModel(),
                    ${package}.${artifactId-safe}.model.external.Utlatande.class);

        } catch (IOException e) {
            throw new ModuleSystemException("Failed to deserialize external model", e);
        }
    }

    private ${package}.${artifactId-safe}.model.internal.Utlatande getInternal(InternalModelHolder internalModel)
            throws ModuleException {
        try {
            return objectMapper.readValue(internalModel.getInternalModel(),
                    ${package}.${artifactId-safe}.model.internal.Utlatande.class);

        } catch (IOException e) {
            throw new ModuleSystemException("Failed to deserialize internal model", e);
        }
    }

    private TransportModelResponse toTransportModelResponse(se.inera.certificate.${artifactId-safe}.model.v1.Utlatande transportModel) throws ModuleException {
        try {
            StringWriter writer = new StringWriter();
            jaxbContext.createMarshaller().marshal(transportModel, writer);
            return new TransportModelResponse(writer.toString());

        } catch (JAXBException e) {
            throw new ModuleSystemException("Failed to marshall transport model", e);
        }
    }

    private ExternalModelResponse toExternalModelResponse(
            ${package}.${artifactId-safe}.model.external.Utlatande externalModel) throws ModuleException {
        try {
            StringWriter writer = new StringWriter();
            objectMapper.writeValue(writer, externalModel);
            return new ExternalModelResponse(writer.toString(), externalModel);

        } catch (IOException e) {
            throw new ModuleSystemException("Failed to serialize external model", e);
        }
    }

    private InternalModelResponse toInteralModelResponse(
            ${package}.${artifactId-safe}.model.internal.Utlatande internalModel) throws ModuleException {
        try {
            StringWriter writer = new StringWriter();
            objectMapper.writeValue(writer, internalModel);
            return new InternalModelResponse(writer.toString());

        } catch (IOException e) {
            throw new ModuleSystemException("Failed to serialize internal model", e);
        }
    }
}
