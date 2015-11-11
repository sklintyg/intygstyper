package se.inera.certificate.modules.sjukersattning.model.converter;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.certificate.model.common.internal.GrundData;
import se.inera.certificate.model.converter.util.ConverterException;
import se.inera.certificate.model.converter.util.WebcertModelFactoryUtil;
import se.inera.certificate.modules.sjukersattning.model.internal.SjukersattningUtlatande;
import se.inera.certificate.modules.sjukersattning.model.internal.SjukersattningUtlatande.Builder;
import se.inera.certificate.modules.support.api.dto.*;

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

        // TODO
//        LOG.trace("Creating copy with id {} from {}", copyData.getCertificateId(), template.getId());
//
//        populateWithId(template, copyData.getCertificateId());
//        populateWithSkapadAv(template, copyData.getSkapadAv());
//
//        if (copyData.hasPatient()) {
//            populateWithPatientInfo(template, copyData.getPatient());
//        }
//
//        if (copyData.hasNewPersonnummer()) {
//            populateWithNewPersonnummer(template, copyData.getNewPersonnummer());
//        }
//
//        resetDataInCopy(template);
//
//        return template;
        return null;
    }

    private void populateWithId(Builder utlatande, String utlatandeId) throws ConverterException {
        if (utlatandeId == null) {
            throw new ConverterException("No certificateID found");
        }
        utlatande.setId(utlatandeId);
    }

    private void populateWithNewPersonnummer(SjukersattningUtlatande template, Personnummer newPersonnummer) {
        template.getGrundData().getPatient().setPersonId(newPersonnummer);
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

    private void resetDataInCopy(SjukersattningUtlatande utlatande) {
        utlatande.getGrundData().setSigneringsdatum(null);
    }

    public void updateSkapadAv(SjukersattningUtlatande utlatande, HoSPersonal hosPerson, LocalDateTime signeringsdatum) {
        utlatande.getGrundData().getSkapadAv().setPersonId(hosPerson.getHsaId());
        utlatande.getGrundData().getSkapadAv().setFullstandigtNamn(hosPerson.getNamn());
        utlatande.getGrundData().getSkapadAv().setForskrivarKod(hosPerson.getForskrivarkod());
        utlatande.getGrundData().setSigneringsdatum(signeringsdatum);
    }
}
