    package se.inera.certificate.modules.fk7263.model.converter;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.inera.certificate.clinicalprocess.healthcond.certificate.certificatestatusupdateforcareresponder.v1.Arbetsformaga;
import se.inera.certificate.clinicalprocess.healthcond.certificate.certificatestatusupdateforcareresponder.v1.CertificateStatusUpdateForCareType;
import se.inera.certificate.clinicalprocess.healthcond.certificate.certificatestatusupdateforcareresponder.v1.Enhet;
import se.inera.certificate.clinicalprocess.healthcond.certificate.certificatestatusupdateforcareresponder.v1.FragorOchSvar;
import se.inera.certificate.clinicalprocess.healthcond.certificate.certificatestatusupdateforcareresponder.v1.Handelse;
import se.inera.certificate.clinicalprocess.healthcond.certificate.certificatestatusupdateforcareresponder.v1.HosPersonal;
import se.inera.certificate.clinicalprocess.healthcond.certificate.certificatestatusupdateforcareresponder.v1.Patient;
import se.inera.certificate.clinicalprocess.healthcond.certificate.certificatestatusupdateforcareresponder.v1.UtlatandeType;
import se.inera.certificate.clinicalprocess.healthcond.certificate.types.v1.DatumPeriod;
import se.inera.certificate.clinicalprocess.healthcond.certificate.types.v1.Diagnos;
import se.inera.certificate.clinicalprocess.healthcond.certificate.types.v1.Handelsekod;
import se.inera.certificate.clinicalprocess.healthcond.certificate.types.v1.HandelsekodKodRestriktion;
import se.inera.certificate.clinicalprocess.healthcond.certificate.types.v1.HsaId;
import se.inera.certificate.clinicalprocess.healthcond.certificate.types.v1.PQ;
import se.inera.certificate.clinicalprocess.healthcond.certificate.types.v1.PersonId;
import se.inera.certificate.clinicalprocess.healthcond.certificate.types.v1.TypAvUtlatande;
import se.inera.certificate.clinicalprocess.healthcond.certificate.types.v1.UtlatandeId;
import se.inera.certificate.codes.Diagnoskodverk;
import se.inera.certificate.model.InternalLocalDateInterval;
import se.inera.certificate.model.common.internal.HoSPersonal;
import se.inera.certificate.modules.fk7263.model.converter.util.ConverterUtil;
import se.inera.certificate.modules.fk7263.model.internal.Utlatande;
import se.inera.certificate.modules.service.WebcertModuleService;
import se.inera.certificate.modules.support.api.exception.ModuleException;
import se.inera.certificate.modules.support.api.notification.FragaSvar;
import se.inera.certificate.modules.support.api.notification.HandelseType;
import se.inera.certificate.modules.support.api.notification.NotificationMessage;

public class InternalToNotification {

    private static final Logger LOG = LoggerFactory.getLogger(InternalToNotification.class);

    private static final String INTYGSID_ROOT = "1.2.752.129.2.1.2.1";

    private static final String HSAID_ROOT = "1.2.752.129.2.1.4.1";

    private static final String PERSONNUMMER_ROOT = "1.2.752.129.2.1.3.1";

    private static final String TYPAVUTLATANDE_FK7263_CODE = "FK7263";

    private static final String TYPAVUTLATANDE_FK7263_DISPLAYNAME = "Läkarintyg enligt 3 kap. 8 § lagen (1962:381) om allmän försäkring";

    private static final String TYPAVUTLATANDE_CODESYSTEM = "f6fb361a-e31d-48b8-8657-99b63912dd9b";

    private static final String TYPAVUTLATANDE_CODESYSTEM_NAME = "kv_utlåtandetyp_intyg";

    private static final String HANDELSE_CODESYSTEM = "dfd7bbad-dbe5-4a2f-ba25-f7b9b2cc6b14";

    private static final String HANDELSE_CODESYSTEM_NAME = "kv_händelse";

    private static final String ARBETSFORMAGA_UNIT = "%";

    @Autowired(required = false)
    private WebcertModuleService moduleService;

    @Autowired
    private ConverterUtil converterUtil;

    public CertificateStatusUpdateForCareType createCertificateStatusUpdateForCareType(NotificationMessage notificationMessage)
            throws ModuleException {

        LOG.debug("Creating CertificateStatusUpdateForCareType for certificate {}, event {}", notificationMessage.getIntygsId(),
                notificationMessage.getHandelse());

        Utlatande utlatandeSource = converterUtil.fromJsonString(notificationMessage.getUtkast());

        UtlatandeType utlatandeType = new UtlatandeType();

        decorateWithTypAvUtlatande(utlatandeType, utlatandeSource);
        decorateWithUtlatandeId(utlatandeType, utlatandeSource);
        decorateWithSigneringsDatum(utlatandeType, utlatandeSource);
        decorateWithPatient(utlatandeType, utlatandeSource);
        decorateWithSkapadAv(utlatandeType, utlatandeSource);
        decorateWithOptionalDiagnos(utlatandeType, utlatandeSource);
        decorateWithOptionalArbetsformagor(utlatandeType, utlatandeSource);
        decorateWithHandelse(utlatandeType, notificationMessage);
        decorateWithFragorOchSvar(utlatandeType, notificationMessage);

        CertificateStatusUpdateForCareType statusUpdateType = new CertificateStatusUpdateForCareType();
        statusUpdateType.setUtlatande(utlatandeType);

        return statusUpdateType;
    }

    private void decorateWithTypAvUtlatande(UtlatandeType utlatandeType, Utlatande utlatandeSource) {
        TypAvUtlatande typAvUtlatande = new TypAvUtlatande();
        typAvUtlatande.setCode(TYPAVUTLATANDE_FK7263_CODE);
        typAvUtlatande.setCodeSystem(TYPAVUTLATANDE_CODESYSTEM);
        typAvUtlatande.setCodeSystemName(TYPAVUTLATANDE_CODESYSTEM_NAME);
        typAvUtlatande.setDisplayName(TYPAVUTLATANDE_FK7263_DISPLAYNAME);
        utlatandeType.setTypAvUtlatande(typAvUtlatande);
    }

    private void decorateWithUtlatandeId(UtlatandeType utlatandeType, Utlatande utlatandeSource) {
        UtlatandeId utlatandeId = new UtlatandeId();
        utlatandeId.setRoot(INTYGSID_ROOT);
        utlatandeId.setExtension(utlatandeSource.getId());
        utlatandeType.setUtlatandeId(utlatandeId);
    }

    private void decorateWithSigneringsDatum(UtlatandeType utlatandeType, Utlatande utlatandeSource) {
        if (utlatandeSource.getGrundData().getSigneringsdatum() != null) {
            utlatandeType.setSigneringsdatum(utlatandeSource.getGrundData().getSigneringsdatum());
        }
    }

    private void decorateWithPatient(UtlatandeType utlatandeType, Utlatande utlatandeSource) {
        PersonId personId = new PersonId();
        personId.setExtension(utlatandeSource.getGrundData().getPatient().getPersonId());
        personId.setRoot(PERSONNUMMER_ROOT);

        Patient patientType = new Patient();
        patientType.setPersonId(personId);

        utlatandeType.setPatient(patientType);
    }

    private void decorateWithSkapadAv(UtlatandeType utlatandeType, Utlatande utlatandeSource) {
        HoSPersonal vardpersonReferens = utlatandeSource.getGrundData().getSkapadAv();

        HosPersonal hoSPerson = new HosPersonal();
        hoSPerson.setFullstandigtNamn(vardpersonReferens.getFullstandigtNamn());

        HsaId personHsaId = createHsaId(vardpersonReferens.getPersonId());
        hoSPerson.setPersonalId(personHsaId);

        Enhet vardEnhet = new Enhet();
        vardEnhet.setEnhetsnamn(vardpersonReferens.getVardenhet().getEnhetsnamn());

        HsaId vardEnhetHsaId = createHsaId(vardpersonReferens.getVardenhet().getEnhetsid());
        vardEnhet.setEnhetsId(vardEnhetHsaId);

        hoSPerson.setEnhet(vardEnhet);

        utlatandeType.setSkapadAv(hoSPerson);
    }

    private void decorateWithHandelse(UtlatandeType utlatandeType, NotificationMessage notificationMessage) {
        
        HandelseType handelseTyp = notificationMessage.getHandelse();
        
        Handelsekod handelseKod = new Handelsekod();
        handelseKod.setCodeSystem(HANDELSE_CODESYSTEM);
        handelseKod.setCodeSystemName(HANDELSE_CODESYSTEM_NAME);
        handelseKod.setDisplayName(handelseTyp.toString());

        HandelsekodKodRestriktion handelseValue = convertToHandelsekod(handelseTyp);
        handelseKod.setCode(handelseValue.value());
        
        Handelse handelseType = new Handelse();
        handelseType.setHandelsekod(handelseKod);
        handelseType.setHandelsetidpunkt(notificationMessage.getHandelseTid());
        
        utlatandeType.setHandelse(handelseType);
    }

    private void decorateWithOptionalDiagnos(UtlatandeType utlatandeType, Utlatande utlatandeSource) {

        String diagnosKod = utlatandeSource.getDiagnosKod();

        if (StringUtils.isBlank(diagnosKod)) {
            LOG.debug("Diagnos code was not found in utlatande");
            return;
        }

        // Default diagnosKodverk is ICD-10-SE
        String diagnosKodsystem = utlatandeSource.getDiagnosKodsystem1();
        Diagnoskodverk diagnosKodverk = (StringUtils.isNotBlank(diagnosKodsystem)) ? Diagnoskodverk.valueOf(diagnosKodsystem)
                : Diagnoskodverk.ICD_10_SE;

        if (!moduleService.validateDiagnosisCode(diagnosKod, diagnosKodverk)) {
            LOG.debug("Diagnos code '{}' ({}) is not valid.", diagnosKod, diagnosKodverk.getCodeSystemName());
            return;
        }

        // Set this to empty string if not found
        String diagnosBeskrivning = utlatandeSource.getDiagnosBeskrivning1();
        diagnosBeskrivning = (StringUtils.isNotBlank(diagnosBeskrivning)) ? diagnosBeskrivning : "";

        Diagnos dt = new Diagnos();
        dt.setCode(diagnosKod);
        dt.setCodeSystem(diagnosKodverk.getCodeSystem());
        dt.setCodeSystemName(diagnosKodverk.getCodeSystemName());
        dt.setDisplayName(diagnosBeskrivning);

        LOG.debug("Adding diagnos '{}, {}'", dt.getCode(), dt.getDisplayName());
    }

    private void decorateWithOptionalArbetsformagor(UtlatandeType utlatandeType, Utlatande utlatandeSource) {
    
        List<Arbetsformaga> arbetsformagor = utlatandeType.getArbetsformaga();
    
        addArbetsformaga(arbetsformagor, utlatandeSource.getNedsattMed25(), 25);
        addArbetsformaga(arbetsformagor, utlatandeSource.getNedsattMed50(), 50);
        addArbetsformaga(arbetsformagor, utlatandeSource.getNedsattMed75(), 75);
        addArbetsformaga(arbetsformagor, utlatandeSource.getNedsattMed100(), 100);
    }

    private void decorateWithFragorOchSvar(UtlatandeType utlatandeType, NotificationMessage notificationMessage) {
        FragaSvar fragaSvar = notificationMessage.getFragaSvar();

        FragorOchSvar fosType = new FragorOchSvar();
        fosType.setAntalFragor(fragaSvar.getAntalFragor());
        fosType.setAntalHanteradeFragor(fragaSvar.getAntalHanteradeFragor());
        fosType.setAntalHanteradeSvar(fragaSvar.getAntalHanteradeSvar());
        fosType.setAntalSvar(fragaSvar.getAntalSvar());

        utlatandeType.setFragorOchSvar(fosType);
    }

    private void addArbetsformaga(List<Arbetsformaga> arbetsformagor, InternalLocalDateInterval nedsattningsPeriod, double nedsattningMed) {
    
        if (nedsattningsPeriod == null) {
            LOG.debug("Could not find nedsattning for {}%", nedsattningMed);
            return;
        }
        
        DatumPeriod datumPeriod = new DatumPeriod();
        
        if (nedsattningsPeriod.getFrom() != null) {
            datumPeriod.setFrom(nedsattningsPeriod.getFrom().asLocalDate());
        }
        
        if (nedsattningsPeriod.getTom() != null) {
            datumPeriod.setTom(nedsattningsPeriod.getTom().asLocalDate());
        }
        
        // Calculates the REMAINING arbetsformaga based on the nedsattning of arbetsformaga
        PQ arbestformagaVarde = new PQ();
        arbestformagaVarde.setUnit(ARBETSFORMAGA_UNIT);
        arbestformagaVarde.setValue(100 - nedsattningMed);
        
        Arbetsformaga arbetsformaga = new Arbetsformaga();
        arbetsformaga.setPeriod(datumPeriod);
        arbetsformaga.setVarde(arbestformagaVarde);
    
        arbetsformagor.add(arbetsformaga);
        
        LOG.debug("Added nedsattning for {}%", nedsattningMed);
    }

    private HsaId createHsaId(String id) {
        HsaId hsaId = new HsaId();
        hsaId.setRoot(HSAID_ROOT);
        hsaId.setExtension(id);
        return hsaId;
    }

    private HandelsekodKodRestriktion convertToHandelsekod(HandelseType handelse) {
        switch (handelse) {
        case FRAGA_FRAN_FK:
            return HandelsekodKodRestriktion.HAN_6;
        case FRAGA_TILL_FK:
            return HandelsekodKodRestriktion.HAN_8;
        case FRAGA_FRAN_FK_HANTERAD:
            return HandelsekodKodRestriktion.HAN_9;
        case INTYG_MAKULERAT:
            return HandelsekodKodRestriktion.HAN_5;
        case INTYG_SKICKAT_FK:
            return HandelsekodKodRestriktion.HAN_3;
        case INTYGSUTKAST_ANDRAT:
            return HandelsekodKodRestriktion.HAN_11;
        case INTYGSUTKAST_RADERAT:
            return HandelsekodKodRestriktion.HAN_4;
        case INTYGSUTKAST_SIGNERAT:
            return HandelsekodKodRestriktion.HAN_2;
        case INTYGSUTKAST_SKAPAT:
            return HandelsekodKodRestriktion.HAN_1;
        case SVAR_FRAN_FK:
            return HandelsekodKodRestriktion.HAN_7;
        case SVAR_FRAN_FK_HANTERAD:
            return HandelsekodKodRestriktion.HAN_10;
        default:
            return null;
        }
    }
}
