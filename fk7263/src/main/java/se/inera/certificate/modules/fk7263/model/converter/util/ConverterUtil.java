package se.inera.certificate.modules.fk7263.model.converter.util;

import java.io.IOException;
import java.io.StringWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.fasterxml.jackson.databind.ObjectMapper;

import se.inera.certificate.modules.fk7263.model.internal.Utlatande;
import se.inera.certificate.modules.support.api.CertificateHolder;
import se.inera.certificate.modules.support.api.exception.ModuleException;
import se.inera.certificate.modules.support.api.exception.ModuleSystemException;
import se.inera.certificate.modules.fk7263.support.Fk7263EntryPoint;

public class ConverterUtil {

    @Autowired
    @Qualifier("fk7263-objectMapper")
    private ObjectMapper objectMapper;

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public  CertificateHolder toCertificateHolder(Utlatande utlatande) throws ModuleException {
        String document = toJsonString(utlatande);
        return toCertificateHolder(utlatande, document);
    }

    public  CertificateHolder toCertificateHolder(String document) throws ModuleException {
        Utlatande utlatande = fromJsonString(document);
        return toCertificateHolder(utlatande, document);
    }

    public  CertificateHolder toCertificateHolder(Utlatande utlatande, String document) throws ModuleException {
        CertificateHolder certificateHolder = new CertificateHolder();
        certificateHolder.setId(utlatande.getId());
        certificateHolder.setCareUnitId(utlatande.getIntygMetadata().getSkapadAv().getVardenhet().getEnhetsid());
        certificateHolder.setCareUnitName(utlatande.getIntygMetadata().getSkapadAv().getVardenhet().getEnhetsnamn());
        certificateHolder.setSigningDoctorName(utlatande.getIntygMetadata().getSkapadAv().getFullstandigtNamn());
        certificateHolder.setCivicRegistrationNumber(utlatande.getIntygMetadata().getPatient().getPersonId());
        certificateHolder.setSignedDate(utlatande.getIntygMetadata().getSigneringsdatum());
        certificateHolder.setType(Fk7263EntryPoint.FK_7263);
        certificateHolder.setValidFromDate(utlatande.getGiltighet().getFrom().toString());
        certificateHolder.setValidToDate(utlatande.getGiltighet().getTom().toString());
        certificateHolder.setAdditionalInfo(utlatande.getGiltighet().toString());
        certificateHolder.setDocument(toJsonString(utlatande));
        return certificateHolder;
    }

    public Utlatande fromJsonString(String s) throws ModuleException {
        try {
            return objectMapper.readValue(s, Utlatande.class);
        } catch (IOException e) {
            throw new ModuleSystemException("Failed to deserialize internal model", e);
        }
    }

    public String toJsonString(Utlatande utlatande) throws ModuleException {
        StringWriter writer = new StringWriter();
        try {
            objectMapper.writeValue(writer, utlatande);
        } catch (IOException e) {
            throw new ModuleException("Failed to serialize internal model", e);
        }
        return writer.toString();
    }
}
