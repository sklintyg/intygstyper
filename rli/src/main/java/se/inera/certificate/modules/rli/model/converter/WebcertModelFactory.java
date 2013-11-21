package se.inera.certificate.modules.rli.model.converter;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.certificate.model.HosPersonal;
import se.inera.certificate.modules.rli.model.internal.wc.HoSPersonal;
import se.inera.certificate.modules.rli.model.internal.wc.Patient;
import se.inera.certificate.modules.rli.model.internal.wc.Undersokning;
import se.inera.certificate.modules.rli.model.internal.wc.Utlatande;
import se.inera.certificate.modules.rli.model.internal.wc.Vardenhet;
import se.inera.certificate.modules.rli.model.internal.wc.Vardgivare;
import se.inera.certificate.modules.rli.rest.dto.CreateNewDraftCertificateHolder;

/**
 * Factory for creating a editable model.
 * 
 * @author nikpet
 * 
 */
public class WebcertModelFactory {

    private static final Logger LOG = LoggerFactory.getLogger(WebcertModelFactory.class);

    public Utlatande createNewWebcertDraft(CreateNewDraftCertificateHolder newDraftData) throws ConverterException {

        Utlatande utlatande = new Utlatande();

        utlatande.setUtlatandeid(newDraftData.getCertificateId());

        populateWithSkapadAv(utlatande, newDraftData.getSkapadAv());

        populateWithPatientInfo(utlatande, newDraftData.getPatientInfo());

        populateWithAktivitetKliniskUndersokning(utlatande, newDraftData.getSkapadAv().getVardenhet());

        return utlatande;
    }

    private void populateWithPatientInfo(Utlatande utlatande, se.inera.certificate.model.Patient patient)
            throws ConverterException {
        
        if (patient == null) {
            throw new ConverterException("Got null while trying to populateWithPatientInfo");
        }
        
        utlatande.setPatient(convertPatientToEdit(patient));
    }

    private Patient convertPatientToEdit(se.inera.certificate.model.Patient patientInfo) {
        Patient patient = new Patient();
        patient.setFornamn(StringUtils.join(patientInfo.getFornamn(), " "));
        patient.setEfternamn(patientInfo.getEfternamn());
        patient.setFullstandigtNamn(patientInfo.getFullstandigtNamn());
        patient.setPersonid(patientInfo.getId().getExtension());
        patient.setPostadress(patientInfo.getPostadress());
        patient.setPostnummer(patientInfo.getPostnummer());
        patient.setPostort(patientInfo.getPostort());
        
        return patient;
    }

    private void populateWithAktivitetKliniskUndersokning(Utlatande utlatande,
            se.inera.certificate.model.Vardenhet vardenhet) throws ConverterException {

        if (vardenhet == null) {
            throw new ConverterException("Got null while trying to convert Vardenhet");
        }
        utlatande.setUndersokning(createKliniskUndersokning(convertVardenhetToEdit(vardenhet)));
    }

    private Undersokning createKliniskUndersokning(Vardenhet vardenhet) {
        Undersokning undersokning = new Undersokning();
        undersokning.setUtforsVid(vardenhet);

        /** Create a new LocalDate for when the actual examination is taking place */
        undersokning.setUndersokningsdatum(LocalDate.now().toString());

        return undersokning;
    }

    private void populateWithSkapadAv(Utlatande utlatande, HosPersonal skapadAv) throws ConverterException {
        if (skapadAv == null) {
            throw new ConverterException("Got null while trying to populateWithSkapadAv");
        }

        utlatande.setSkapadAv(convertHosPersonalToEdit(skapadAv));
    }

    private HoSPersonal convertHosPersonalToEdit(HosPersonal hosPersType) {
        HoSPersonal hosPersonal = new HoSPersonal();

        hosPersonal.setPersonid(hosPersType.getId().getExtension());
        hosPersonal.setFullstandigtNamn(hosPersType.getNamn());

        if (hosPersType.getBefattning() != null) {
            hosPersonal.setBefattning(hosPersType.getBefattning());
        }

        hosPersonal.setVardenhet(convertVardenhetToEdit(hosPersType.getVardenhet()));

        return hosPersonal;
    }

    private Vardenhet convertVardenhetToEdit(se.inera.certificate.model.Vardenhet enhet) {
        if (enhet == null) {
            return null;
        }
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setEnhetsid(enhet.getId().getExtension());
        vardenhet.setEnhetsnamn(enhet.getNamn());
        vardenhet.setEpost(enhet.getEpost());
        vardenhet.setPostadress(enhet.getPostadress());
        vardenhet.setPostort(enhet.getPostort());
        vardenhet.setPostnummer(enhet.getPostnummer());
        vardenhet.setTelefonnummer(enhet.getTelefonnummer());
        vardenhet.setVardgivare(convertVardgivareType(enhet.getVardgivare()));

        return vardenhet;
    }

    private Vardgivare convertVardgivareType(se.inera.certificate.model.Vardgivare source) {
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarid(source.getId().getExtension());
        vardgivare.setVardgivarnamn(source.getNamn());
        return vardgivare;
    }
}