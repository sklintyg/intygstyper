package se.inera.certificate.modules.sjukpenning.model.converter;

import java.util.ArrayList;
import java.util.List;

import se.inera.certificate.model.CertificateState;
import se.inera.certificate.model.InternalDate;
import se.inera.certificate.model.InternalLocalDateInterval;
import se.inera.certificate.model.Status;
import se.inera.certificate.model.common.internal.*;
import se.inera.certificate.model.common.internal.GrundData;
import se.inera.certificate.model.common.internal.Patient;
import se.inera.certificate.model.common.internal.Vardenhet;
import se.inera.certificate.model.common.internal.Vardgivare;
import se.inera.certificate.model.converter.util.ConverterException;
import se.inera.certificate.modules.sjukpenning.model.internal.SjukpenningUtlatande;
import se.inera.certificate.modules.sjukpenning.support.SjukpenningEntryPoint;
import se.inera.certificate.modules.support.api.dto.CertificateMetaData;
import se.inera.intygstjanster.fk.services.v1.*;

public class TransportToInternal {

    public static SjukpenningUtlatande convert(SjukpenningIntyg source) throws ConverterException {
        SjukpenningUtlatande utlatande = new SjukpenningUtlatande();
        utlatande.setId(source.getIntygsId());
        utlatande.setTyp(source.getIntygsTyp());
        setGrundData(utlatande, source.getGrundData());
        utlatande.setAvstangningSmittskydd(source.isAvstangningSmitta());
        if (source.getVardKontakter() != null) {
            setVardkontakter(utlatande, source.getVardKontakter().getVardkontakt());
        }
        if (source.getArbete() != null) {
            utlatande.setNuvarandeArbete(true);
            utlatande.setNuvarandeArbetsuppgifter(source.getArbete());
        }
        if (source.isArbetssokande() != null && source.isArbetssokande()) {
            utlatande.setArbetsloshet(true);
        }
        if (source.isForaldraledighet() != null && source.isForaldraledighet()) {
            utlatande.setForaldraledighet(true);
        }
        if (source.isStudier() != null && source.isStudier()) {
            utlatande.setStudier(true);
        }
        if (source.isArbetsmarknadsatgard() != null && source.isArbetsmarknadsatgard()) {
            utlatande.setArbetsmarknadsProgram(true);
        }
        if (source.getSjukdomar() != null) {
            setSjukdomar(utlatande, source.getSjukdomar().getDiagnos());
        }
        if (source.getBehandling() != null) {
            setBehandling(utlatande, source.getBehandling());
        }
        if (source.getBedomning() != null) {
            setBedomning(utlatande, source.getBedomning());
        }
        if (source.getForslagAtgarder() != null) {
            setAtgarder(utlatande, source.getForslagAtgarder().getAtgard());
        }
        if (source.getKonsekvenser() != null) {
            utlatande.setAktivitetsbegransning(source.getKonsekvenser().getBegransning());
            utlatande.setFunktionsnedsattning(source.getKonsekvenser().getKonsekvens());
        }
        if (source.getKanGora() != null) {
            utlatande.setVadPatientenKanGora(source.getKanGora());
        }
        if (source.getPrognosAterga() != null) {
            utlatande.setPrognosNarPatientKanAterga(source.getPrognosAterga());
        }
        utlatande.setKommentar(source.getOvrigt());
        utlatande.setKontaktMedFk(source.getOnskarKontakt().isOnskarKontakt());
        return utlatande;
    }

    private static void setAtgarder(SjukpenningUtlatande utlatande, List<AtgardTyp> atgarder) {
        for (AtgardTyp atgard : atgarder) {
            switch (atgard) {
            case INTE_AKTUELLT:
                utlatande.setAtgardInteAktuellt(true);
                break;
            case ARBETSTRANING:
                utlatande.setAtgardArbetstraning(true);
                break;
            case ARBETSANPASSNING:
                utlatande.setAtgardArbetsanpassning(true);
                break;
            case SOKA_NYTT_ARBETE:
                utlatande.setAtgardSokaNyttArbete(true);
                break;
            case BESOK_PA_ARBETSPLATSEN:
                utlatande.setAtgardBesokPaArbete(true);
                break;
            case ERGONOMISK_BEDOMNING:
                utlatande.setAtgardErgonomi(true);
                break;
            case HJALPMEDEL:
                utlatande.setAtgardHjalpmedel(true);
                break;
            case KONFLIKTHANTERING:
                utlatande.setAtgardKonflikthantering(true);
                break;
            case KONTAKT_MED_FORETAGSHALSOVARD:
                utlatande.setAtgardForetagshalsovard(true);
                break;
            case OMFORDELNING_AV_ARBETSUPPGIFTER:
                utlatande.setAtgardOmfordelning(true);
                break;
            case OVRIGT:
                utlatande.setAtgardOvrigt(true);
                break;
            }
        }
    }

    private static void setBedomning(SjukpenningUtlatande utlatande, BedomningTyp bedomning) {
        utlatande.setRessattTillArbeteAktuellt(bedomning.isAnnatFardmedel());
        utlatande.setRekommendationOverSocialstyrelsensBeslutsstod(bedomning.isOverskridenLangd());
        for (SjukskrivningTyp sjukskrivning : bedomning.getSjukskrivning()) {
            switch (sjukskrivning.getNedsattningsgrad()) {
            case HELT_NEDSATT:
                utlatande.setNedsattMed100(new InternalLocalDateInterval(new InternalDate(sjukskrivning.getVaraktighetFrom()),
                        new InternalDate(sjukskrivning.getVaraktighetTom())));
                break;
            case NEDSATT_MED_3_4:
                utlatande.setNedsattMed75(new InternalLocalDateInterval(new InternalDate(sjukskrivning.getVaraktighetFrom()),
                        new InternalDate(sjukskrivning.getVaraktighetTom())));
                break;
            case NEDSATT_MED_1_2:
                utlatande.setNedsattMed50(new InternalLocalDateInterval(new InternalDate(sjukskrivning.getVaraktighetFrom()),
                        new InternalDate(sjukskrivning.getVaraktighetTom())));
                break;
            case NEDSATT_MED_1_4:
                utlatande.setNedsattMed25(new InternalLocalDateInterval(new InternalDate(sjukskrivning.getVaraktighetFrom()),
                        new InternalDate(sjukskrivning.getVaraktighetTom())));
                break;
            }
        }
    }

    private static void setBehandling(SjukpenningUtlatande utlatande, BehandlingTyp behandling) {
        utlatande.setPlaneradBehandling(behandling.getPlanerade() != null ? behandling.getPlanerade() : null);
        utlatande.setPagaendeBehandling(behandling.getPagaende() != null ? behandling.getPagaende() : null);
    }

    private static void setSjukdomar(SjukpenningUtlatande utlatande, List<SjukdomTyp> diagnoser) {
        int i = 1;
        for (SjukdomTyp sjukdom : diagnoser) {
            switch (i) {
                case 1:
                utlatande.setDiagnosKodsystem1(sjukdom.getCodeSystemId());
                utlatande.setDiagnosKod1(sjukdom.getCodeSystemCode());
                    utlatande.setDiagnosBeskrivning1(sjukdom.getDescription());
                    break;
                case 2:
                utlatande.setDiagnosKodsystem2(sjukdom.getCodeSystemId());
                utlatande.setDiagnosKod2(sjukdom.getCodeSystemCode());
                    utlatande.setDiagnosBeskrivning2(sjukdom.getDescription());
                    break;
                case 3:
                utlatande.setDiagnosKodsystem3(sjukdom.getCodeSystemId());
                utlatande.setDiagnosKod3(sjukdom.getCodeSystemCode());
                utlatande.setDiagnosBeskrivning3(sjukdom.getDescription());
                break;
            }
            i++;
        }
    }

    private static void setGrundData(SjukpenningUtlatande utlatande, se.inera.intygstjanster.fk.services.v1.GrundData sourceGrundData) {
        GrundData grundData = new GrundData();
        setPatient(grundData, sourceGrundData.getPatient());
        setSkapadAv(grundData, sourceGrundData.getSkapadAv());
        grundData.setSigneringsdatum(sourceGrundData.getSigneringsTidstampel());
        utlatande.setGrundData(grundData);
    }

    private static void setSkapadAv(GrundData grundData, se.inera.intygstjanster.fk.services.v1.SkapadAv sourceSkapadAv) {
        HoSPersonal skapadAv = new HoSPersonal();
        skapadAv.setPersonId(sourceSkapadAv.getPersonId());
        skapadAv.setFullstandigtNamn(sourceSkapadAv.getFullstandigtNamn());
        setVardenhet(skapadAv, sourceSkapadAv.getVardenhet());
        grundData.setSkapadAv(skapadAv);
    }

    private static void setVardenhet(HoSPersonal hosPersonal, se.inera.intygstjanster.fk.services.v1.Vardenhet sourceVardenhet) {
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setEnhetsid(sourceVardenhet.getEnhetsId());
        vardenhet.setEnhetsnamn(sourceVardenhet.getEnhetsnamn());
        vardenhet.setPostnummer(sourceVardenhet.getPostnummer());
        vardenhet.setPostadress(sourceVardenhet.getPostadress());
        vardenhet.setPostort(sourceVardenhet.getPostort());
        vardenhet.setTelefonnummer(sourceVardenhet.getTelefonnummer());
        setVardgivare(vardenhet, sourceVardenhet.getVardgivare());
        hosPersonal.setVardenhet(vardenhet);
    }

    private static void setVardgivare(Vardenhet vardenhet, se.inera.intygstjanster.fk.services.v1.Vardgivare sourceVardgivare) {
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarid(sourceVardgivare.getVardgivarid());
        vardgivare.setVardgivarnamn(sourceVardgivare.getVardgivarnamn());
        vardenhet.setVardgivare(vardgivare);
    }

    private static void setPatient(GrundData grundData, se.inera.intygstjanster.fk.services.v1.Patient sourcePatient) {
        Patient patient = new Patient();
        patient.setFornamn(sourcePatient.getFornamn());
        patient.setEfternamn(sourcePatient.getEfternamn());
        patient.setFullstandigtNamn(sourcePatient.getFullstandigtNamn());
        patient.setPersonId(sourcePatient.getPersonId());
        patient.setPostadress(sourcePatient.getPostadress());
        patient.setPostort(sourcePatient.getPostort());
        patient.setPostnummer(sourcePatient.getPostnummer());
        grundData.setPatient(patient);
    }

    private static void setVardkontakter(SjukpenningUtlatande utlatande, List<VardkontaktTyp> vardkontakter) {
        for (VardkontaktTyp vardkontakt : vardkontakter) {
            if (vardkontakt.getBeskrivning().equals(VardkontaktBeskrivningTyp.JOURNALUPPGIFTER)) {
                utlatande.setJournaluppgifter(new InternalDate(vardkontakt.getDatum()));

            }
            if (vardkontakt.getBeskrivning().equals(VardkontaktBeskrivningTyp.TELEFONKONTAKT_MED_PATIENTEN)) {
                utlatande.setTelefonkontaktMedPatienten(new InternalDate(vardkontakt.getDatum()));

            }
            if (vardkontakt.getBeskrivning().equals(VardkontaktBeskrivningTyp.UNDERSOKNING_AV_PATIENTEN)) {
                utlatande.setUndersokningAvPatienten(new InternalDate(vardkontakt.getDatum()));

            }
        }
    }

    public static CertificateMetaData getMetaData(SjukpenningIntyg source, IntygMeta meta) {
        CertificateMetaData metaData = new CertificateMetaData();
        metaData.setCertificateId(source.getIntygsId());
        metaData.setAdditionalInfo(source.getOvrigt());
        metaData.setCertificateType(SjukpenningEntryPoint.MODULE_ID);
        metaData.setFacilityName(source.getGrundData().getSkapadAv().getVardenhet().getEnhetsnamn());
        metaData.setIssuerName(source.getGrundData().getSkapadAv().getFullstandigtNamn());
        metaData.setSignDate(source.getGrundData().getSigneringsTidstampel());

        List<Status> statuses = new ArrayList<>();
        for (IntygStatus sourceStatus : meta.getStatus()) {
            statuses.add(new Status(CertificateState.valueOf(sourceStatus.getType().value()), sourceStatus.getTarget(), sourceStatus.getTimestamp()));
        }
        metaData.setStatus(statuses);

        return metaData;
    }

}