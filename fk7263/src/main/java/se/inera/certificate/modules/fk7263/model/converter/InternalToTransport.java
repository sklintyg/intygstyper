package se.inera.certificate.modules.fk7263.model.converter;

import static se.inera.certificate.model.util.Iterables.addAll;

import java.util.ArrayList;
import java.util.List;

import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.AktivitetType;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.Aktivitetskod;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.ArbetsformagaNedsattningType;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.ArbetsformagaType;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.ArbetsuppgiftType;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.BedomtTillstandType;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.FunktionstillstandType;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.Lakarutlatande;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.MedicinsktTillstandType;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.Nedsattningsgrad;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.Prognosangivelse;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.ReferensType;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.Referenstyp;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.SysselsattningType;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.TypAvFunktionstillstand;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.TypAvSysselsattning;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.VardkontaktType;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.Vardkontakttyp;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.registermedicalcertificate.v3.RegisterMedicalCertificate;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.v2.EnhetType;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.v2.HosPersonalType;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.v2.PatientType;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.v2.VardgivareType;
import se.inera.certificate.fk7263.iso.v21090.dt.v1.CD;
import se.inera.certificate.fk7263.iso.v21090.dt.v1.II;
import se.inera.certificate.model.common.internal.HoSPersonal;
import se.inera.certificate.model.common.internal.Patient;
import se.inera.certificate.model.common.internal.Vardenhet;
import se.inera.certificate.model.common.internal.Vardgivare;
import se.inera.certificate.model.converter.util.ConverterException;
import se.inera.certificate.modules.fk7263.model.codes.Kodverk;
import se.inera.certificate.modules.fk7263.model.internal.Utlatande;

public final class InternalToTransport {

    private static final String FK7263 = "Läkarintyg enligt 3 kap, 8 § lagen (1962:381) om allmän försäkring";
    private static final String PERS_ID_ROOT = "1.2.752.129.2.1.3.1";
    private static final String HSA_ID_ROOT = "1.2.752.129.2.1.4.1";
    private static final String ARBETSPLATSKOD_ROOT = "1.2.752.29.4.71";

    public static final int FORMOGA_3_4 = 75;
    public static final int FORMOGA_1_2 = 50;
    public static final int FORMOGA_1_4 = 25;

    private InternalToTransport() {
    }

    public static RegisterMedicalCertificate getJaxbObject(Utlatande source) throws ConverterException {
        RegisterMedicalCertificate register = new RegisterMedicalCertificate();
        register.setLakarutlatande(new Lakarutlatande());
        if (source.getId() != null) {
            register.getLakarutlatande().setLakarutlatandeId(source.getId());
        }
        register.getLakarutlatande().setTypAvUtlatande(FK7263);

        if (!isNullOrEmpty(source.getKommentar())) {
            register.getLakarutlatande().setKommentar(source.getKommentar());
        }

        register.getLakarutlatande().setSigneringsdatum(source.getIntygMetadata().getSigneringsdatum());
        register.getLakarutlatande().setSkickatDatum(source.getIntygMetadata().getSkickatdatum());
        register.getLakarutlatande().setPatient(patientToJaxb(source.getIntygMetadata().getPatient()));
        register.getLakarutlatande().setSkapadAvHosPersonal(hosPersonalToJaxb(source.getIntygMetadata().getSkapadAv()));
        register.getLakarutlatande().setKommentar(source.getKommentar());
        
        if (!isNullOrEmpty(source.getSjukdomsforlopp())) {
            register.getLakarutlatande().setBedomtTillstand(sjukdomsforloppToJaxb(source.getSjukdomsforlopp()));
        }

        if (!isNullOrEmpty(source.getDiagnosKod())) {
            register.getLakarutlatande().setMedicinsktTillstand(toMedicinsktTillstand(source.getDiagnosKod(), source.getDiagnosBeskrivning()));
        }

        convertAktiviteter(register, source);

        convertReferenser(register, source);

        addAll(register.getLakarutlatande().getVardkontakts(), convertVardkontakter(source));

        if (!isNullOrEmpty(source.getFunktionsnedsattning())) {
            register.getLakarutlatande().getFunktionstillstands()
                    .add(toFunktionstillstand(source.getFunktionsnedsattning(), TypAvFunktionstillstand.KROPPSFUNKTION));
        }

        if (!isNullOrEmpty(source.getAktivitetsbegransning())) {

            // add arbetsformaga to aktivitetsbegransing
            FunktionstillstandType aktivitetsbegransing = toFunktionstillstand(source.getAktivitetsbegransning(), TypAvFunktionstillstand.AKTIVITET);
            aktivitetsbegransing.setArbetsformaga(toArbetsformaga(source));

            register.getLakarutlatande().getFunktionstillstands().add(aktivitetsbegransing);

        } else if (source.isAvstangningSmittskydd()) {
            // If no ObservationsKoder.AKTIVITETER_OCH_DELAKTIGHET is found,
            // create FunktionstillstandType and populate it with Arbetsformaga to not loose information
            FunktionstillstandType aktivitetsbegransing = new FunktionstillstandType();
            aktivitetsbegransing.setTypAvFunktionstillstand(TypAvFunktionstillstand.AKTIVITET);
            aktivitetsbegransing.setArbetsformaga(toArbetsformaga(source));
            register.getLakarutlatande().getFunktionstillstands().add(aktivitetsbegransing);
        }
        return register;
    }

    private static FunktionstillstandType toFunktionstillstand(String source, TypAvFunktionstillstand tillstand) {
        FunktionstillstandType funktionstillstandType = new FunktionstillstandType();
        funktionstillstandType.setTypAvFunktionstillstand(tillstand);
        funktionstillstandType.setBeskrivning(source);
        return funktionstillstandType;
    }

    private static ArbetsformagaType toArbetsformaga(Utlatande source) throws ConverterException {

        ArbetsformagaType arbetsformagaType = new ArbetsformagaType();

        if (!isNullOrEmpty(source.getArbetsformagaPrognos())) {
            arbetsformagaType.setMotivering(source.getArbetsformagaPrognos());
        }

        if (source.getPrognosBedomning() != null) {
            switch (source.getPrognosBedomning()) {
            case arbetsformagaPrognosGarInteAttBedoma:
                arbetsformagaType.setPrognosangivelse(Prognosangivelse.DET_GAR_INTE_ATT_BEDOMMA);
                break;
            case arbetsformagaPrognosJa:
                arbetsformagaType.setPrognosangivelse(Prognosangivelse.ATERSTALLAS_HELT);
                break;
            case arbetsformagaPrognosJaDelvis:
                arbetsformagaType.setPrognosangivelse(Prognosangivelse.ATERSTALLAS_DELVIS);
                break;
            case arbetsformagaPrognosNej:
                arbetsformagaType.setPrognosangivelse(Prognosangivelse.INTE_ATERSTALLAS);
                break;
            default:
                throw new ConverterException("Unknown prognosBedomning: " + source.getPrognosBedomning());
            }
        }

        if (source.isNuvarandeArbete()) {
            // attach arbetsuppgift if available
            if (!isNullOrEmpty(source.getNuvarandeArbetsuppgifter())) {
                ArbetsuppgiftType arbetsuppgift = new ArbetsuppgiftType();
                arbetsuppgift.setTypAvArbetsuppgift(source.getNuvarandeArbetsuppgifter());
                arbetsformagaType.setArbetsuppgift(arbetsuppgift);
            }

            arbetsformagaType.getSysselsattnings().addAll(convertSysselsattnings(source));
        }
        if (source.getNedsattMed25() != null) {
            ArbetsformagaNedsattningType nedsattningType = new ArbetsformagaNedsattningType();
            nedsattningType.setNedsattningsgrad(Nedsattningsgrad.NEDSATT_MED_1_4);
            if (source.getNedsattMed25().isValid()) {
                nedsattningType.setVaraktighetFrom(source.getNedsattMed25().fromAsLocalDate());
                nedsattningType.setVaraktighetTom(source.getNedsattMed25().tomAsLocalDate());
            }
            arbetsformagaType.getArbetsformagaNedsattnings().add(nedsattningType);
        }   
        if (source.getNedsattMed50() != null) {
            ArbetsformagaNedsattningType nedsattningType = new ArbetsformagaNedsattningType();
            nedsattningType.setNedsattningsgrad(Nedsattningsgrad.NEDSATT_MED_1_2);
            if (source.getNedsattMed50().isValid()) {
                nedsattningType.setVaraktighetFrom(source.getNedsattMed50().fromAsLocalDate());
                nedsattningType.setVaraktighetTom(source.getNedsattMed50().tomAsLocalDate());
            }
            arbetsformagaType.getArbetsformagaNedsattnings().add(nedsattningType);
        }   
        if (source.getNedsattMed75() != null) {
            ArbetsformagaNedsattningType nedsattningType = new ArbetsformagaNedsattningType();
            nedsattningType.setNedsattningsgrad(Nedsattningsgrad.NEDSATT_MED_3_4);
            if (source.getNedsattMed75().isValid()) {
                nedsattningType.setVaraktighetFrom(source.getNedsattMed75().fromAsLocalDate());
                nedsattningType.setVaraktighetTom(source.getNedsattMed75().tomAsLocalDate());
            }
            arbetsformagaType.getArbetsformagaNedsattnings().add(nedsattningType);
        }   
        if (source.getNedsattMed100() != null) {
            ArbetsformagaNedsattningType nedsattningType = new ArbetsformagaNedsattningType();
            nedsattningType.setNedsattningsgrad(Nedsattningsgrad.HELT_NEDSATT);
            if (source.getNedsattMed100().isValid()) {
                nedsattningType.setVaraktighetFrom(source.getNedsattMed100().fromAsLocalDate());
                nedsattningType.setVaraktighetTom(source.getNedsattMed100().tomAsLocalDate());
            }
            arbetsformagaType.getArbetsformagaNedsattnings().add(nedsattningType);
        }   

        return arbetsformagaType;
    }

    private static List<SysselsattningType> convertSysselsattnings(Utlatande source) {
        List<SysselsattningType> sysselsattningTypes = new ArrayList<>();

        if (source.isNuvarandeArbete()) {
            SysselsattningType sysselsattningType = new SysselsattningType();
            sysselsattningType.setTypAvSysselsattning(TypAvSysselsattning.NUVARANDE_ARBETE);
            sysselsattningTypes.add(sysselsattningType);
        }

        if (source.isArbetsloshet()) {
            SysselsattningType sysselsattningType = new SysselsattningType();
            sysselsattningType.setTypAvSysselsattning(TypAvSysselsattning.ARBETSLOSHET);
            sysselsattningTypes.add(sysselsattningType);
        }

        if (source.isForaldrarledighet()) {
            SysselsattningType sysselsattningType = new SysselsattningType();
            sysselsattningType.setTypAvSysselsattning(TypAvSysselsattning.FORALDRALEDIGHET);
            sysselsattningTypes.add(sysselsattningType);
        }
        return sysselsattningTypes;
    }

    private static List<VardkontaktType> convertVardkontakter(Utlatande source) {
        if (source == null) {
            return null;
        }

        List<VardkontaktType> vardkontaktTypes = new ArrayList<>();
        if (source.getUndersokningAvPatienten() != null) {
            VardkontaktType vardkontaktType = new VardkontaktType();
            vardkontaktType.setVardkontaktstid(source.getUndersokningAvPatienten().asLocalDate());
            vardkontaktType.setVardkontakttyp(Vardkontakttyp.MIN_UNDERSOKNING_AV_PATIENTEN);
            vardkontaktTypes.add(vardkontaktType);
        }

        if (source.getTelefonkontaktMedPatienten() != null) {
            VardkontaktType vardkontaktType = new VardkontaktType();
            vardkontaktType.setVardkontaktstid(source.getTelefonkontaktMedPatienten().asLocalDate());
            vardkontaktType.setVardkontakttyp(Vardkontakttyp.MIN_TELEFONKONTAKT_MED_PATIENTEN);
            vardkontaktTypes.add(vardkontaktType);
        }

        return vardkontaktTypes;
    }

    private static void convertReferenser(RegisterMedicalCertificate register, Utlatande source) {
        if (source == null) {
            return;
        }

        List<ReferensType> referensTypes = new ArrayList<>();
        if (source.getJournaluppgifter() != null) {
            ReferensType referensType = new ReferensType();
            referensType.setDatum(source.getJournaluppgifter().asLocalDate());
            referensType.setReferenstyp(Referenstyp.JOURNALUPPGIFTER);
            referensTypes.add(referensType);
        }

        if (source.getAnnanReferens() != null) {
            ReferensType referensType = new ReferensType();
            referensType.setDatum(source.getAnnanReferens().asLocalDate());
            referensType.setReferenstyp(Referenstyp.ANNAT);
            referensTypes.add(referensType);
        }
        if (!referensTypes.isEmpty()) {
            register.getLakarutlatande().getReferens().addAll(referensTypes);
        }
    }

    /**
     * Build AktivitetTypes from internal model
     * 
     * @param source
     *            Utlatande
     * @return List of AktivitetType
     */
    private static void convertAktiviteter(RegisterMedicalCertificate register, Utlatande source) {
        if (source == null) {
            return;
        }

        List<AktivitetType> aktivitets = new ArrayList<>();

        if (source.isAvstangningSmittskydd()) {
            AktivitetType smittskydd = new AktivitetType();
            smittskydd.setAktivitetskod(Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA);
            aktivitets.add(smittskydd);
        }

        if (source.isRekommendationKontaktArbetsformedlingen()) {
            AktivitetType arbetsformedlingen = new AktivitetType();
            arbetsformedlingen.setAktivitetskod(Aktivitetskod.PATIENTEN_BEHOVER_FA_KONTAKT_MED_ARBETSFORMEDLINGEN);
            aktivitets.add(arbetsformedlingen);
        }

        if (source.isRekommendationKontaktForetagshalsovarden()) {
            AktivitetType foretagshalsovarden = new AktivitetType();
            foretagshalsovarden.setAktivitetskod(Aktivitetskod.PATIENTEN_BEHOVER_FA_KONTAKT_MED_FORETAGSHALSOVARDEN);
            aktivitets.add(foretagshalsovarden);
        }

        if (source.isRekommendationOvrigtCheck() || !isNullOrEmpty(source.getRekommendationOvrigt())) {
            AktivitetType ovrigt = new AktivitetType();
            ovrigt.setAktivitetskod(Aktivitetskod.OVRIGT);
            ovrigt.setBeskrivning(source.getRekommendationOvrigt());
            aktivitets.add(ovrigt);
        }

        if (source.getAtgardInomSjukvarden() != null) {
            AktivitetType atgard = new AktivitetType();
            atgard.setAktivitetskod(Aktivitetskod.PLANERAD_ELLER_PAGAENDE_BEHANDLING_ELLER_ATGARD_INOM_SJUKVARDEN);
            atgard.setBeskrivning(source.getAtgardInomSjukvarden());
            aktivitets.add(atgard);
        }

        if (source.getAnnanAtgard() != null) {
            AktivitetType annanAtgard = new AktivitetType();
            annanAtgard.setAktivitetskod(Aktivitetskod.PLANERAD_ELLER_PAGAENDE_ANNAN_ATGARD);
            annanAtgard.setBeskrivning(source.getAnnanAtgard());
            aktivitets.add(annanAtgard);
        }

        if (source.getRehabilitering() != null) {
            AktivitetType rehab = new AktivitetType();
            switch (source.getRehabilitering()) {
            case rehabiliteringGarInteAttBedoma:
                rehab.setAktivitetskod(Aktivitetskod.GAR_EJ_ATT_BEDOMMA_OM_ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL);
                break;
            case rehabiliteringAktuell:
                rehab.setAktivitetskod(Aktivitetskod.ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL);
                break;
            case rehabiliteringEjAktuell:
                rehab.setAktivitetskod(Aktivitetskod.ARBETSLIVSINRIKTAD_REHABILITERING_AR_EJ_AKTUELL);
                break;
            default:
                break;
            }
            aktivitets.add(rehab);
        }

        if (source.isRessattTillArbeteAktuellt()) {
            AktivitetType ressatt = new AktivitetType();
            ressatt.setAktivitetskod(Aktivitetskod.FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_AKTUELLT);
            aktivitets.add(ressatt);
        }

        if (source.isRessattTillArbeteEjAktuellt()) {
            AktivitetType ressatt = new AktivitetType();
            ressatt.setAktivitetskod(Aktivitetskod.FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_EJ_AKTUELLT);
            aktivitets.add(ressatt);
        }

        if (source.isKontaktMedFk()) {
            AktivitetType fk = new AktivitetType();
            fk.setAktivitetskod(Aktivitetskod.KONTAKT_MED_FORSAKRINGSKASSAN_AR_AKTUELL);
            aktivitets.add(fk);
        }

        if (!aktivitets.isEmpty()) {
            register.getLakarutlatande().getAktivitets().addAll(aktivitets);
        }
    }

    private static MedicinsktTillstandType toMedicinsktTillstand(String diagnoskod, String diagnosbeskrivning) {
        MedicinsktTillstandType tillstand = new MedicinsktTillstandType();
        tillstand.setBeskrivning(diagnosbeskrivning);
        tillstand.setTillstandskod(createCD(diagnoskod, null, Kodverk.ICD_10.getCodeSystemName()));
        return tillstand;
    }

    private static CD createCD(String code, String codeSystem, String codeSystemName) {
        CD cd = new CD();
        cd.setCode(code);
        if (codeSystem != null) {
            cd.setCodeSystem(codeSystem);
        }
        if (codeSystemName != null) {
            cd.setCodeSystemName(codeSystemName);
        }
        return cd;
    }

    private static BedomtTillstandType sjukdomsforloppToJaxb(String source) {
        BedomtTillstandType tillstand = new BedomtTillstandType();
        tillstand.setBeskrivning(source);
        return tillstand;
    }

    private static EnhetType vardenhetToTransportJaxb(Vardenhet source) {
        if (source == null) {
            return null;
        }

        EnhetType enhet = new EnhetType();
        enhet.setEnhetsnamn(source.getEnhetsnamn());
        enhet.setPostadress(source.getPostadress());
        enhet.setPostnummer(source.getPostnummer());
        enhet.setPostort(source.getPostort());
        enhet.setTelefonnummer(source.getTelefonnummer());
        enhet.setEpost(source.getEpost());

        II arbetsplatskod = new II();
        arbetsplatskod.setExtension(source.getArbetsplatsKod());
        arbetsplatskod.setRoot(ARBETSPLATSKOD_ROOT);
        enhet.setArbetsplatskod(arbetsplatskod);

        II id = new II();
        id.setExtension(source.getEnhetsid());
        id.setRoot(HSA_ID_ROOT);
        enhet.setEnhetsId(id);

        enhet.setVardgivare(vardgivareToJaxb(source.getVardgivare()));
        return enhet;
    }

    private static VardgivareType vardgivareToJaxb(Vardgivare source) {
        if (source == null) {
            return null;
        }

        VardgivareType vardgivare = new VardgivareType();
        vardgivare.setVardgivarnamn(source.getVardgivarnamn());
        II id = new II();
        id.setExtension(source.getVardgivarid());
        id.setRoot(HSA_ID_ROOT);
        vardgivare.setVardgivareId(id);
        return vardgivare;
    }

    private static HosPersonalType hosPersonalToJaxb(HoSPersonal skapadAv) {
        HosPersonalType personal = new HosPersonalType();
        personal.setForskrivarkod(skapadAv.getForskrivarKod());
        personal.setFullstandigtNamn(skapadAv.getFullstandigtNamn());
        II id = new II();
        id.setExtension(skapadAv.getPersonId());
        id.setRoot(HSA_ID_ROOT);
        personal.setPersonalId(id);
        personal.setEnhet(vardenhetToTransportJaxb(skapadAv.getVardenhet()));

        return personal;
    }

    private static PatientType patientToJaxb(Patient source) {
        PatientType patient = new PatientType();
        patient.setFullstandigtNamn(source.getFullstandigtNamn());
        II id = new II();
        id.setRoot(PERS_ID_ROOT);
        id.setExtension(source.getPersonId());
        patient.setPersonId(id);
        return patient;
    }

    private static boolean isNullOrEmpty(String source) {
        return source == null || source.isEmpty();
    }
}
