package se.inera.certificate.modules.sjukpenning.model.converter;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.certificate.model.converter.util.ConverterException;
import se.inera.certificate.model.converter.util.WebcertModelFactoryUtil;
import se.inera.certificate.modules.sjukpenning.model.internal.Utlatande;
import se.inera.certificate.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.certificate.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.certificate.modules.support.api.dto.HoSPersonal;
import se.inera.certificate.modules.support.api.dto.Patient;

/**
 * Factory for creating an editable model.
 */
public class WebcertModelFactory {
    private static final Logger LOG = LoggerFactory.getLogger(WebcertModelFactory.class);

    /**
     * Create a new sjukpenning draft pre-populated with the attached data.
     *
     * @param newDraftData
     *            {@link CreateNewDraftHolder}
     * @return {@link Utlatande} or throws a ConverterException if something unforeseen happens
     * @throws ConverterException
     */
    public Utlatande createNewWebcertDraft(CreateNewDraftHolder newDraftData) throws ConverterException {

        LOG.trace("Creating draft with id {}", newDraftData.getCertificateId());

        Utlatande template = new Utlatande();

        populateWithId(template, newDraftData.getCertificateId());

        template.setNuvarandeArbete(true);
        template.setArbetsloshet(false);

        template.setAvstangningSmittskydd(false);
        template.setForaldrarledighet(false);
        template.setKontaktMedFk(false);
        template.setRekommendationKontaktArbetsformedlingen(false);
        template.setRekommendationKontaktForetagshalsovarden(false);
        template.setRessattTillArbeteAktuellt(false);
        template.setRessattTillArbeteEjAktuellt(false);

        populateWithSkapadAv(template, newDraftData.getSkapadAv());
        populateWithPatientInfo(template, newDraftData.getPatient());

        return template;
    }

    public Utlatande createCopy(CreateDraftCopyHolder copyData, Utlatande template) throws ConverterException {

        LOG.trace("Creating copy with id {} from {}", copyData.getCertificateId(), template.getId());

        populateWithId(template, copyData.getCertificateId());
        populateWithSkapadAv(template, copyData.getSkapadAv());

        if (copyData.hasPatient()) {
            populateWithPatientInfo(template, copyData.getPatient());
        }

        if (copyData.hasNewPersonnummer()) {
            populateWithNewPersonnummer(template, copyData.getNewPersonnummer());
        }
        
        resetDataInCopy(template);
        
        return template;
    }

    private void populateWithNewPersonnummer(Utlatande template, String newPersonnummer) {
        template.getGrundData().getPatient().setPersonId(newPersonnummer);
    }

    private void populateWithId(Utlatande utlatande, String utlatandeId) throws ConverterException {

        if (utlatandeId == null) {
            throw new ConverterException("No certificateID found");
        }

        utlatande.setId(utlatandeId);
    }

    private void populateWithPatientInfo(Utlatande utlatande, Patient patient) throws ConverterException {
        if (patient == null) {
            throw new ConverterException("Got null while trying to populateWithPatientInfo");
        }

        utlatande.getGrundData().setPatient(WebcertModelFactoryUtil.convertPatientToEdit(patient));
    }

    private void populateWithSkapadAv(Utlatande utlatande, se.inera.certificate.modules.support.api.dto.HoSPersonal hoSPersonal)
            throws ConverterException {
        if (hoSPersonal == null) {
            throw new ConverterException("Got null while trying to populateWithSkapadAv");
        }

        utlatande.getGrundData().setSkapadAv(WebcertModelFactoryUtil.convertHosPersonalToEdit(hoSPersonal));
    }

    private void resetDataInCopy(Utlatande utlatande) {
        utlatande.getGrundData().setSigneringsdatum(null);
    }

    public void updateSkapadAv(Utlatande utlatande, HoSPersonal hosPerson, LocalDateTime signeringsdatum) {
        utlatande.getGrundData().getSkapadAv().setPersonId(hosPerson.getHsaId());
        utlatande.getGrundData().getSkapadAv().setFullstandigtNamn(hosPerson.getNamn());
        utlatande.getGrundData().getSkapadAv().setForskrivarKod(hosPerson.getForskrivarkod());
        utlatande.getGrundData().setSigneringsdatum(signeringsdatum);
    }
}
