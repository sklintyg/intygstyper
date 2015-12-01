package se.inera.intyg.intygstyper.fk7263.model.converter.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import se.inera.intyg.intygstyper.fk7263.model.internal.Utlatande;
import se.inera.intyg.intygstyper.fk7263.support.Fk7263EntryPoint;
import se.inera.intyg.common.support.modules.support.api.CertificateHolder;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleSystemException;

import java.io.IOException;
import java.io.StringWriter;

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
        certificateHolder.setCareUnitId(utlatande.getGrundData().getSkapadAv().getVardenhet().getEnhetsid());
        certificateHolder.setCareUnitName(utlatande.getGrundData().getSkapadAv().getVardenhet().getEnhetsnamn());
        certificateHolder.setCareGiverId(utlatande.getGrundData().getSkapadAv().getVardenhet().getVardgivare().getVardgivarid());
        certificateHolder.setSigningDoctorName(utlatande.getGrundData().getSkapadAv().getFullstandigtNamn());
        certificateHolder.setCivicRegistrationNumber(utlatande.getGrundData().getPatient().getPersonId());
        certificateHolder.setSignedDate(utlatande.getGrundData().getSigneringsdatum());
        certificateHolder.setType(Fk7263EntryPoint.MODULE_ID);
        certificateHolder.setValidFromDate(utlatande.getGiltighet().getFrom().toString());
        certificateHolder.setValidToDate(utlatande.getGiltighet().getTom().toString());
        certificateHolder.setAdditionalInfo(utlatande.getGiltighet().getFrom().toString() + " - " + utlatande.getGiltighet().getTom().toString());
        certificateHolder.setDocument(document);
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
