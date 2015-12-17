package se.inera.certificate.modules.sjukersattning.model.converter;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.certificate.modules.sjukersattning.model.internal.SjukersattningUtlatande;
import se.inera.certificate.modules.sjukersattning.model.internal.SjukersattningUtlatande.Builder;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.model.converter.util.WebcertModelFactoryUtil;
import se.inera.intyg.common.support.modules.support.api.dto.*;

/**
 * Factory for creating an editable model.
 */
public class WebcertModelFactory {
    private static final Logger LOG = LoggerFactory.getLogger(WebcertModelFactory.class);

    /**
     * Create a new sjukersattning draft pre-populated with the attached data.
     *
     * @param newDraftData
     *            {@link CreateNewDraftHolder}
     * @return {@link SjukersattningUtlatande} or throws a ConverterException if something unforeseen happens
     * @throws ConverterException
     */
    public SjukersattningUtlatande createNewWebcertDraft(CreateNewDraftHolder newDraftData) throws ConverterException {

        LOG.trace("Creating draft with id {}", newDraftData.getCertificateId());

        Builder template = SjukersattningUtlatande.builder();
        GrundData grundData = new GrundData();

        populateWithId(template, newDraftData.getCertificateId());
        populateWithSkapadAv(grundData, newDraftData.getSkapadAv());
        populateWithPatientInfo(grundData, newDraftData.getPatient());

        return template.setGrundData(grundData).build();
    }

    public SjukersattningUtlatande createCopy(CreateDraftCopyHolder copyData, SjukersattningUtlatande template) throws ConverterException {

        LOG.trace("Creating copy with id {} from {}", copyData.getCertificateId(), template.getId());

        SjukersattningUtlatande.Builder templateBuilder = template.toBuilder();
        GrundData grundData = template.getGrundData();

        populateWithId(templateBuilder, copyData.getCertificateId());
        populateWithSkapadAv(grundData, copyData.getSkapadAv());

        if (copyData.hasPatient()) {
            populateWithPatientInfo(grundData, copyData.getPatient());
        }

        if (copyData.hasNewPersonnummer()) {
            populateWithNewPersonnummer(grundData, copyData.getNewPersonnummer());
        }

        resetDataInCopy(grundData);

        return templateBuilder.build();
    }

    private void populateWithId(Builder utlatande, String utlatandeId) throws ConverterException {
        if (utlatandeId == null) {
            throw new ConverterException("No certificateID found");
        }
        utlatande.setId(utlatandeId);
    }

    private void populateWithNewPersonnummer(GrundData grundData, Personnummer newPersonnummer) {
        grundData.getPatient().setPersonId(newPersonnummer);
    }

    private void populateWithPatientInfo(GrundData grundData, Patient patient) throws ConverterException {
        if (patient == null) {
            throw new ConverterException("Got null while trying to populateWithPatientInfo");
        }
        grundData.setPatient(WebcertModelFactoryUtil.convertPatientToEdit(patient));
    }

    private void populateWithSkapadAv(GrundData grundData, HoSPersonal hoSPersonal) throws ConverterException {
        if (hoSPersonal == null) {
            throw new ConverterException("Got null while trying to populateWithSkapadAv");
        }
        grundData.setSkapadAv(WebcertModelFactoryUtil.convertHosPersonalToEdit(hoSPersonal));
    }

    private void resetDataInCopy(GrundData grundData) {
        grundData.setSigneringsdatum(null);
    }

    public void updateSkapadAv(SjukersattningUtlatande utlatande, HoSPersonal hosPerson, LocalDateTime signeringsdatum) {
        utlatande.getGrundData().getSkapadAv().setPersonId(hosPerson.getHsaId());
        utlatande.getGrundData().getSkapadAv().setFullstandigtNamn(hosPerson.getNamn());
        utlatande.getGrundData().getSkapadAv().setForskrivarKod(hosPerson.getForskrivarkod());
        utlatande.getGrundData().setSigneringsdatum(signeringsdatum);
    }
}
