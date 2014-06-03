package se.inera.certificate.modules.ts_bas.rest;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import se.inera.certificate.model.Kod;
import se.inera.certificate.model.util.Strings;
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
import se.inera.certificate.modules.support.api.exception.ModuleException;
import se.inera.certificate.modules.support.api.exception.ModuleSystemException;
import se.inera.certificate.modules.support.api.exception.ModuleValidationException;
import se.inera.certificate.modules.support.api.exception.ModuleVersionUnsupportedException;
import se.inera.certificate.modules.ts_bas.model.codes.CodeConverter;
import se.inera.certificate.modules.ts_bas.model.codes.IntygAvserKod;
import se.inera.certificate.ts_bas.model.v1.Utlatande;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ModuleServiceWrapper implements ModuleApi {

    @Autowired
    private ModuleService moduleService;

    @Autowired
    @Qualifier("ts-bas-objectMapper")
    private ObjectMapper objectMapper;

    @Autowired
    @Qualifier("ts-bas-jaxbContext")
    private JAXBContext jaxbContext;

    @Override
    public ExternalModelResponse unmarshall(TransportModelHolder transportModel) throws ModuleException {
        try {
            return toExternalModelResponse(moduleService.unmarshall(getTransport(transportModel)));

        } catch (WebApplicationException e) {
            if (e.getResponse().getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
                throw new ModuleConverterException("Could not convert transport model to external model", e);
            }
            throw new ModuleSystemException(e);
        }
    }

    @Override
    public TransportModelResponse marshall(ExternalModelHolder externalModel, TransportModelVersion version)
            throws ModuleException {
        if (!version.equals(TransportModelVersion.UTLATANDE_V1)) {
            throw new ModuleVersionUnsupportedException("ts-bas does not support transport model version " + version);
        }

        try {
            return toTransportModelResponse(moduleService.marshall(getExternal(externalModel)));

        } catch (WebApplicationException e) {
            if (e.getResponse().getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
                throw new ModuleConverterException("Could not convert external model to transport model", e);
            }
            throw new ModuleSystemException(e);
        }
    }

    @Override
    public String validate(ExternalModelHolder externalModel) throws ModuleException {
        try {
            return moduleService.validate(getExternal(externalModel));

        } catch (WebApplicationException e) {
            if (e.getResponse().getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
                throw new ModuleValidationException(Collections.singletonList(e.getResponse().getEntity().toString()));
            }
            throw new ModuleSystemException(e);
        }
    }

    @Override
    public ValidateDraftResponse validateDraft(InternalModelHolder internalModel) throws ModuleException {
        try {
            return moduleService.validateDraft(getInternal(internalModel));

        } catch (WebApplicationException e) {
            throw new ModuleSystemException(e);
        }
    }

    @Override
    public PdfResponse pdf(ExternalModelHolder externalModel) throws ModuleException {
        return moduleService.pdf(getExternal(externalModel));
    }

    @Override
    public InternalModelResponse convertExternalToInternal(ExternalModelHolder externalModel) throws ModuleException {
        return toInteralModelResponse(moduleService.convertExternalToInternal(getExternal(externalModel)));
    }

    @Override
    public ExternalModelResponse convertInternalToExternal(InternalModelHolder internalModel) throws ModuleException {
        return toExternalModelResponse(moduleService.convertInternalToExternal(getInternal(internalModel)));
    }

    @Override
    public InternalModelResponse createNewInternal(CreateNewDraftHolder draftCertificateHolder) throws ModuleException {
        return toInteralModelResponse(moduleService.createNewInternal(draftCertificateHolder));
    }

    private se.inera.certificate.ts_bas.model.v1.Utlatande getTransport(TransportModelHolder transportModel)
            throws ModuleException {
        try {
            return (se.inera.certificate.ts_bas.model.v1.Utlatande) jaxbContext.createUnmarshaller().unmarshal(
                    new StringReader(transportModel.getTransportModel()));

        } catch (ValidationException e) {
            throw new ModuleValidationException(Collections.singletonList(e.getMessage()),
                    "XML validation of transport model failed", e);

        } catch (JAXBException e) {
            throw new ModuleSystemException("Failed to unmarshall transport model", e);
        }
    }

    private se.inera.certificate.modules.ts_bas.model.external.Utlatande getExternal(ExternalModelHolder externalModel)
            throws ModuleException {
        try {
            return objectMapper.readValue(externalModel.getExternalModel(),
                    se.inera.certificate.modules.ts_bas.model.external.Utlatande.class);

        } catch (IOException e) {
            throw new ModuleSystemException("Failed to deserialize external model", e);
        }
    }

    private se.inera.certificate.modules.ts_bas.model.internal.Utlatande getInternal(InternalModelHolder internalModel)
            throws ModuleException {
        try {
            return objectMapper.readValue(internalModel.getInternalModel(),
                    se.inera.certificate.modules.ts_bas.model.internal.Utlatande.class);

        } catch (IOException e) {
            throw new ModuleSystemException("Failed to deserialize internal model", e);
        }
    }

    private TransportModelResponse toTransportModelResponse(Utlatande transportModel) throws ModuleException {
        try {
            StringWriter writer = new StringWriter();
            jaxbContext.createMarshaller().marshal(transportModel, writer);
            return new TransportModelResponse(writer.toString());

        } catch (JAXBException e) {
            throw new ModuleSystemException("Failed to marshall transport model", e);
        }
    }

    private ExternalModelResponse toExternalModelResponse(
            se.inera.certificate.modules.ts_bas.model.external.Utlatande externalModel) throws ModuleException {
        try {
            StringWriter writer = new StringWriter();
            objectMapper.writeValue(writer, externalModel);
            return new ExternalModelResponse(writer.toString(), externalModel);

        } catch (IOException e) {
            throw new ModuleSystemException("Failed to serialize external model", e);
        }
    }

    private InternalModelResponse toInteralModelResponse(
            se.inera.certificate.modules.ts_bas.model.internal.Utlatande internalModel) throws ModuleException {
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
        se.inera.certificate.modules.ts_bas.model.external.Utlatande utlatande = getExternal(externalModel);

        ArrayList<String> intygAvser = new ArrayList<>();
        for (Kod intygAvserKod : utlatande.getIntygAvser()) {
            intygAvser.add(CodeConverter.fromCode(intygAvserKod, IntygAvserKod.class).name());
        }
        return Strings.join(", ", intygAvser);
    }
    @Override
    public InternalModelHolder updateInternal(InternalModelHolder internalModel, HoSPersonal hosPerson) throws ModuleException {
        try {
            se.inera.certificate.modules.ts_bas.model.internal.Utlatande utlatande = getInternal(internalModel);
            utlatande.getSkapadAv().setPersonid(hosPerson.getHsaId());
            utlatande.getSkapadAv().setFullstandigtNamn(hosPerson.getNamn());
            utlatande.getSkapadAv().getBefattningar().clear();
            if (hosPerson.getBefattning() != null) {
                utlatande.getSkapadAv().getBefattningar().add(hosPerson.getBefattning());
            }
            String internalModelJson = toInteralModelResponse(utlatande).getInternalModel();
            return new InternalModelHolder(internalModelJson);
        } catch (ModuleException e) {
            throw new ModuleException("Convert error of internal model", e);
        }
    }
}
