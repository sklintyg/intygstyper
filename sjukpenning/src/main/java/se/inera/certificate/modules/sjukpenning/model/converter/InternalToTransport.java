package se.inera.certificate.modules.sjukpenning.model.converter;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import se.inera.certificate.model.InternalLocalDateInterval;
import se.inera.certificate.model.common.internal.HoSPersonal;
import se.inera.certificate.model.common.internal.Vardenhet;
import se.inera.certificate.model.converter.util.ConverterException;
import se.inera.certificate.modules.sjukpenning.model.internal.SjukpenningUtlatande;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.*;
import se.riv.clinicalprocess.healthcond.certificate.v2.*;
import se.riv.clinicalprocess.healthcond.certificate.v2.Svar.Delsvar;

public class InternalToTransport {

    public static final int UNDERSOKNING_AV_PATIENT = 1;
    public static final int TELEFONKONTAKT = 2;
    public static final int JOURNALUPPGIFTER = 3;

    public static final int ARBETE = 1;
    public static final int ARBETSLOSHET = 2;
    public static final int FORALDRALEDIGHET = 3;
    public static final int STUDIER = 4;
    public static final int ARBETSMARKNADSPROGRAM = 5;

    public static final String CERTIFICATE_CODE_SYSTEM = "f6fb361a-e31d-48b8-8657-99b63912dd9b";
    public static final String DIAGNOS_CODE_SYSTEM = "ICD-10-SE";
    public static final String REFERENS_CODE_SYSTEM = "KV_FKMU_0001";
    public static final String HSA_CODE_SYSTEM = "1.2.752.129.2.1.4.1";
    public static final String INTYP_TYP_CODE_SYSTEM = "kv_utlåtandetyp_intyg";
    public static final String PERSON_ID_CODE_SYSTEM= "1.2.752.129.2.1.3.3";
    public static final String SYSSELSATTNING_CODE_SYSTEM = "KV_FKMU_0002";

    public static final String SMITTA_SVAR_ID = "2";
    public static final String SMITTA_DELSVAR_ID = "2.1";

    public static final String SYSSELSATTNING_SVAR_ID = "1";
    public static final String SYSSELSATTNING_TYP_DELSVAR = "1.1";
    public static final String SYSSELSATTNING_BESKRIVNING_DELSVAR_ID = "1.2";
    public static final String REFERENS_SVAR_ID = "10";
    public static final String REFERENSTYP_DELSVAR_ID = "10.1";
    public static final String REFERENSDATUM_DELSVAR_ID = "10.2";
    public static final String HUVUDSAKLIG_ORSAK_SVAR_ID = "3";
    public static final String DIAGNOS_DELSVAR_ID = "3.1";
    public static final String DIAGNOS_BESKRIVNING_DELSVAR_ID = "3.2";
    public static final String YTTERLIGARE_ORSAK_SVAR_ID = "4";
    public static final String YTTERLIGARE_ORSAK_DELSVAR_ID = "4.1";
    public static final String FUNKTIONSNEDSATTNING_SVAR_ID = "11";
    public static final String FUNKTIONSNEDSATTNING_DELSVAR_ID = "11.1";
    public static final String AKTIVITETSBEGRANSNING_SVAR_ID = "5";
    public static final String AKTIVITETSBEGRANSNING_DELSVAR_ID = "5.1";
    public static final String PAGAENDEBEHANDLING_SVAR_ID = "12";
    public static final String PAGAENDEBEHANDLING_DELSVAR_ID = "12.1";
    public static final String PLANERADBEHANDLING_SVAR_ID = "13";
    public static final String PLANERADBEHANDLING_DELSVAR_ID = "13.1";
    public static final String NEDSATTNING_SVAR_ID = "6";
    public static final String NEDSATTNING_DELSVAR_ID = "6.1";
    public static final String RESSATT_SVAR_ID = "7";
    public static final String RESSATT_DELSVAR_ID = "7.1";
    public static final String REKOMMENDATION_OVERSKRIDER_SVAR_ID = "14";
    public static final String REKOMMENDATION_OVERSKRIDER_DELSVAR_ID = "14.1";
    public static final String AKTIVITETSFORMAGA_SVAR_ID = "16";
    public static final String AKTIVITETSFORMAGA_DELSVAR_ID = "16.1";
    public static final String PROGNOS_SVAR_ID = "17";
    public static final String PROGNOS_DELSVAR_ID = "17.1";
    public static final String OVRIGT_SVAR_ID = "22";
    public static final String OVRIGT_DELSVAR_ID = "22.1";
    public static final String KONTAKT_ONSKAS_SVAR_ID = "21";
    public static final String KONTAKT_ONSKAS_DELSVAR_ID = "21.1";

    public static RegisterCertificateType convert(SjukpenningUtlatande source) throws ConverterException {
        if (source == null) {
            throw new ConverterException("Source utlatande was null, cannot convert");
        }

        RegisterCertificateType sjukpenningType = new RegisterCertificateType();
        sjukpenningType.setIntyg(getIntyg(source));
        return sjukpenningType;
    }

    private static Intyg getIntyg(SjukpenningUtlatande source) {
        Intyg intyg = new Intyg();
        intyg.setTyp(getTypAvIntyg(source));
        intyg.setIntygsId(getIntygsId(source));
        intyg.setVersion("1");
        intyg.setSigneringstidpunkt(source.getGrundData().getSigneringsdatum());
        intyg.setSkickatTidpunkt(new LocalDateTime());
        intyg.setSkapadAv(getSkapadAv(source));
        intyg.setPatient(getPatient(source.getGrundData().getPatient()));
        intyg.getSvar().addAll(getSvar(source));
        return intyg;
    }

    private static HosPersonal getSkapadAv(SjukpenningUtlatande source) {
        HoSPersonal sourceSkapadAv = source.getGrundData().getSkapadAv();
        HosPersonal skapadAv = new HosPersonal();
        skapadAv.setPersonalId(anHsaId(sourceSkapadAv.getPersonId()));
        skapadAv.setFullstandigtNamn(sourceSkapadAv.getFullstandigtNamn());
        skapadAv.setEnhet(getEnhet(sourceSkapadAv.getVardenhet()));
        return skapadAv;
    }

    private static Enhet getEnhet(Vardenhet sourceVardenhet) {
        Enhet vardenhet = new Enhet();
        vardenhet.setEnhetsId(anHsaId(sourceVardenhet.getEnhetsid()));
        vardenhet.setEnhetsnamn(sourceVardenhet.getEnhetsnamn());
        vardenhet.setPostnummer(sourceVardenhet.getPostnummer());
        vardenhet.setPostadress(sourceVardenhet.getPostadress());
        vardenhet.setPostort(sourceVardenhet.getPostort());
        vardenhet.setTelefonnummer(sourceVardenhet.getTelefonnummer());
        vardenhet.setVardgivare(getVardgivare(sourceVardenhet.getVardgivare()));
        return vardenhet;
    }

    private static Vardgivare getVardgivare(se.inera.certificate.model.common.internal.Vardgivare sourceVardgivare) {
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivareId(anHsaId(sourceVardgivare.getVardgivarid()));
        vardgivare.setVardgivarnamn(sourceVardgivare.getVardgivarnamn());
        return vardgivare;
    }

    private static Patient getPatient(se.inera.certificate.model.common.internal.Patient sourcePatient) {
        Patient patient = new se.riv.clinicalprocess.healthcond.certificate.v2.Patient();
        patient.setEfternamn(sourcePatient.getEfternamn());
        patient.setFornamn(sourcePatient.getFornamn());
        patient.setMellannamn(sourcePatient.getMellannamn());
        PersonId personId = new PersonId();
        personId.setRoot(PERSON_ID_CODE_SYSTEM);
        personId.setExtension(sourcePatient.getPersonId().replaceAll("-", ""));
        patient.setPersonId(personId);
        patient.setPostadress(sourcePatient.getPostadress());
        patient.setPostnummer(sourcePatient.getPostnummer());
        patient.setPostort(sourcePatient.getPostadress());
        return patient;
    }

    private static IntygId getIntygsId(SjukpenningUtlatande source) {
        IntygId intygId = new IntygId();
        intygId.setRoot(source.getId());
        return intygId;
    }

    private static TypAvIntyg getTypAvIntyg(SjukpenningUtlatande source) {
        TypAvIntyg typAvIntyg = new TypAvIntyg();
        typAvIntyg.setCodeSystem(CERTIFICATE_CODE_SYSTEM);
        typAvIntyg.setCodeSystemName(INTYP_TYP_CODE_SYSTEM);
        typAvIntyg.setCode(source.getTyp());
        return typAvIntyg;
    }

    private static List<Svar> getSvar(SjukpenningUtlatande source) {
        List<Svar> svars = new ArrayList<>();

        svars.add(aSvar(SMITTA_SVAR_ID).withDelsvar(SMITTA_DELSVAR_ID, Boolean.toString(source.isAvstangningSmittskydd())).build());

        if (source.getUndersokningAvPatienten() != null) {
            svars.add(aSvar(REFERENS_SVAR_ID).
                    withDelsvar(REFERENSTYP_DELSVAR_ID, aCV(REFERENS_CODE_SYSTEM, Integer.toString(UNDERSOKNING_AV_PATIENT), null)).
                    withDelsvar(REFERENSDATUM_DELSVAR_ID, source.getUndersokningAvPatienten().asLocalDate().toString()).build());
        }
        if (source.getTelefonkontaktMedPatienten() != null) {
            svars.add(aSvar(REFERENS_SVAR_ID).
                    withDelsvar(REFERENSTYP_DELSVAR_ID, aCV(REFERENS_CODE_SYSTEM, Integer.toString(TELEFONKONTAKT), null)).
                            withDelsvar(REFERENSDATUM_DELSVAR_ID, source.getTelefonkontaktMedPatienten().asLocalDate().toString()).build());
        }
        if (source.getJournaluppgifter() != null) {
            svars.add(aSvar(REFERENS_SVAR_ID).
                    withDelsvar(REFERENSTYP_DELSVAR_ID, aCV(REFERENS_CODE_SYSTEM, Integer.toString(JOURNALUPPGIFTER), null)).
                    withDelsvar(REFERENSDATUM_DELSVAR_ID, source.getJournaluppgifter().asLocalDate().toString()).build());
        }

        if (source.isNuvarandeArbete()) {
            svars.add(aSvar(SYSSELSATTNING_SVAR_ID).
                    withDelsvar(SYSSELSATTNING_TYP_DELSVAR, aCV(SYSSELSATTNING_CODE_SYSTEM, Integer.toString(ARBETE), null)).
                    withDelsvar(SYSSELSATTNING_BESKRIVNING_DELSVAR_ID, source.getNuvarandeArbetsuppgifter()).build());
        }
        if (source.isArbetsloshet()) {
            svars.add(aSvar(SYSSELSATTNING_SVAR_ID).withDelsvar(SYSSELSATTNING_TYP_DELSVAR, aCV(SYSSELSATTNING_CODE_SYSTEM, Integer.toString(ARBETSLOSHET), null)).build());
        }
        if (source.isForaldraledighet()) {
            svars.add(aSvar(SYSSELSATTNING_SVAR_ID).withDelsvar(SYSSELSATTNING_TYP_DELSVAR, aCV(SYSSELSATTNING_CODE_SYSTEM, Integer.toString(FORALDRALEDIGHET), null)).build());
        }
        if (source.isStudier()) {
            svars.add(aSvar(SYSSELSATTNING_SVAR_ID).withDelsvar(SYSSELSATTNING_TYP_DELSVAR, aCV(SYSSELSATTNING_CODE_SYSTEM, Integer.toString(STUDIER), null)).build());
        }
        if (source.isArbetsmarknadsProgram()) {
            svars.add(aSvar(SYSSELSATTNING_SVAR_ID).withDelsvar(SYSSELSATTNING_TYP_DELSVAR, aCV(SYSSELSATTNING_CODE_SYSTEM, Integer.toString(ARBETSMARKNADSPROGRAM), null)).build());
        }

        svars.add(aSvar(HUVUDSAKLIG_ORSAK_SVAR_ID).
                withDelsvar(DIAGNOS_DELSVAR_ID, aCV(DIAGNOS_CODE_SYSTEM, source.getDiagnosKod1(), source.getDiagnosBeskrivning1())).
                withDelsvar(DIAGNOS_BESKRIVNING_DELSVAR_ID, source.getAktivitetsbegransning()).build());

        if (source.getDiagnosKod2() != null) {
            svars.add(aSvar(YTTERLIGARE_ORSAK_SVAR_ID).withDelsvar(YTTERLIGARE_ORSAK_DELSVAR_ID,
                    aCV(DIAGNOS_CODE_SYSTEM, source.getDiagnosKod2(), source.getDiagnosBeskrivning2())).build());
        }

        if (source.getDiagnosKod3() != null) {
            svars.add(aSvar(YTTERLIGARE_ORSAK_SVAR_ID).withDelsvar(YTTERLIGARE_ORSAK_DELSVAR_ID,
                    aCV(DIAGNOS_CODE_SYSTEM, source.getDiagnosKod3(), source.getDiagnosBeskrivning3())).build());
        }

        // TODO: Åtgärd som orsak till nedsatt arbetsförmåga ska in här.

        svars.add(aSvar(FUNKTIONSNEDSATTNING_SVAR_ID).withDelsvar(FUNKTIONSNEDSATTNING_DELSVAR_ID, source.getFunktionsnedsattning()).build());
        svars.add(aSvar(AKTIVITETSBEGRANSNING_SVAR_ID).withDelsvar(AKTIVITETSBEGRANSNING_DELSVAR_ID, source.getAktivitetsbegransning()).build());
        svars.add(aSvar(PAGAENDEBEHANDLING_SVAR_ID).withDelsvar(PAGAENDEBEHANDLING_DELSVAR_ID, source.getPagaendeBehandling()).build());
        svars.add(aSvar(PLANERADBEHANDLING_SVAR_ID).withDelsvar(PLANERADBEHANDLING_DELSVAR_ID, source.getPlaneradBehandling()).build());

        if (source.getNedsattMed100() != null) {
            // TODO: reactivate!
            //svars.add(aSvar(NEDSATTNING_SVAR_ID).withDelsvar(NEDSATTNING_DELSVAR_ID, aPeriod(source.getNedsattMed100())).build());
        }
        if (source.getNedsattMed75() != null) {
            svars.add(aSvar(NEDSATTNING_SVAR_ID).withDelsvar(NEDSATTNING_DELSVAR_ID, aPeriod(source.getNedsattMed75())).build());
        }
        if (source.getNedsattMed50() != null) {
            svars.add(aSvar(NEDSATTNING_SVAR_ID).withDelsvar(NEDSATTNING_DELSVAR_ID, aPeriod(source.getNedsattMed50())).build());
        }
        if (source.getNedsattMed25() != null) {
            svars.add(aSvar(NEDSATTNING_SVAR_ID).withDelsvar(NEDSATTNING_DELSVAR_ID, aPeriod(source.getNedsattMed25())).build());
        }

        svars.add(aSvar(RESSATT_SVAR_ID).withDelsvar(RESSATT_DELSVAR_ID, Boolean.toString(source.isRessattTillArbeteAktuellt())).build());

        svars.add(aSvar(REKOMMENDATION_OVERSKRIDER_SVAR_ID).
                withDelsvar(REKOMMENDATION_OVERSKRIDER_DELSVAR_ID, Boolean.toString(source.isRekommendationOverSocialstyrelsensBeslutsstod())).build());

        // Förändrad arbetstidsförläggning svars(add(aSvar("8").withDelsvar("8.1", source.foran)))

        svars.add(aSvar(AKTIVITETSFORMAGA_SVAR_ID).withDelsvar(AKTIVITETSFORMAGA_DELSVAR_ID, source.getVadPatientenKanGora()).build());

        svars.add(aSvar(PROGNOS_SVAR_ID).withDelsvar(PROGNOS_DELSVAR_ID, source.getPrognosNarPatientKanAterga()).build());

        // Arbetslivsinriktade åtgärder svars.add(aSvar("18").withDelsvar("18.1", source.))

        svars.add(aSvar(OVRIGT_SVAR_ID).withDelsvar(OVRIGT_DELSVAR_ID, source.getKommentar()).build());

        svars.add(aSvar(KONTAKT_ONSKAS_SVAR_ID).withDelsvar(KONTAKT_ONSKAS_DELSVAR_ID, Boolean.toString(source.isKontaktMedFk())).build());

        return svars;
    }

    private static HsaId anHsaId(String id) {
        HsaId hsaId = new HsaId();
        hsaId.setRoot(HSA_CODE_SYSTEM);
        hsaId.setExtension(id);
        return hsaId;
    }

    private static JAXBElement<TimePeriodType> aPeriod(InternalLocalDateInterval nedsattning) {
        TimePeriodType period = new TimePeriodType();
        period.setStart(nedsattning.fromAsLocalDate().toLocalDateTime(LocalTime.MIDNIGHT));
        period.setEnd(nedsattning.tomAsLocalDate().toLocalDateTime(LocalTime.MIDNIGHT));
        return new JAXBElement<>(new QName("urn:riv:clinicalprocess:healthcond:certificate:types:2", "timePeriod"), TimePeriodType.class, null, period);
    }

    private static JAXBElement<CVType> aCV(String codeSystem, String code, String displayName) {
        CVType cv = new CVType();
        cv.setCodeSystem(codeSystem);
        cv.setCode(code);
        cv.setDisplayName(displayName);
        return new JAXBElement<>(new QName("urn:riv:clinicalprocess:healthcond:certificate:types:2", "cv"), CVType.class, null, cv);
    }

    private static class SvarBuilder {
        private String id;
        private List<Delsvar> delSvars = new ArrayList<>();

        public SvarBuilder(String id) {
            this.id = id;
        }

        public Svar build() {
            Svar svar = new Svar();
            svar.setId(id);
            svar.getDelsvar().addAll(delSvars);
            return svar;
        }

        public SvarBuilder withDelsvar(String delsvarsId, Object content) {
            Delsvar delsvar = new Delsvar();
            delsvar.setId(delsvarsId);
            delsvar.getContent().add(content);
            delSvars.add(delsvar);
            return this;
        }
    }

    private static SvarBuilder aSvar(String id) {
        return new SvarBuilder(id);
    }

}
