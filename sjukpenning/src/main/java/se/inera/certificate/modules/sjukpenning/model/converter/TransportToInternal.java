package se.inera.certificate.modules.sjukpenning.model.converter;

import se.inera.certificate.model.InternalDate;
import se.inera.certificate.model.InternalLocalDateInterval;
import se.inera.certificate.model.common.internal.GrundData;
import se.inera.certificate.model.common.internal.HoSPersonal;
import se.inera.certificate.model.common.internal.Patient;
import se.inera.certificate.model.common.internal.Vardenhet;
import se.inera.certificate.model.converter.util.ConverterException;
import se.inera.certificate.modules.sjukpenning.model.internal.SjukpenningUtlatande;
import se.inera.intygstjanster.fk.services.v1.*;

import java.util.List;

public class TransportToInternal {

    public static SjukpenningUtlatande convert(SjukpenningIntyg source) throws ConverterException {
        SjukpenningUtlatande utlatande = new SjukpenningUtlatande();
        utlatande.setId(source.getIntygsId());
        utlatande.setTyp(source.getIntygsTyp());
        setGrundData(utlatande, source.getGrundData());
        setVardkontakter(utlatande, source.getVardKontakter().getVardkontakt());
        setSysselsattning(utlatande, source.getSysselsattning());
        setSjukdomar(utlatande, source.getSjukdomar().getDiagnos());
        setBehandling(utlatande, source.getBehandling());
        setBedomning(utlatande, source.getBedomning());
        setAtgarder(utlatande, source.getForslagAtgarder().getAtgard());
        utlatande.setAktivitetsbegransning(source.getKonsekvenser().getBegransning());
        utlatande.setFunktionsnedsattning(source.getKonsekvenser().getKonsekvens());
        utlatande.setVadPatientenKanGora(source.getKanGora());
        utlatande.setPrognosNarPatientKanAterga(source.getPrognosAterga());
        utlatande.setKommentar(source.getOvrigt());
        utlatande.setKontaktMedFk(source.getOnskarKontakt().isOnskarKontakt());
        return utlatande;
    }

    private static void setAtgarder(SjukpenningUtlatande utlatande, List<AtgardTyp> atgarder) {
        for (AtgardTyp atgard : atgarder) {
            switch (atgard) {
            case INTE_AKTUELLT:
                utlatande.setAtgardInteAktuellt(true);
            case ARBETSTRANING:
                utlatande.setAtgardArbetstraning(true);
            case ARBETSANPASSNING:
                utlatande.setAtgardArbetsanpassning(true);
            case SOKA_NYTT_ARBETE:
                utlatande.setAtgardSokaNyttArbete(true);
            case BESOK_PA_ARBETSPLATSEN:
                utlatande.setAtgardBesokPaArbete(true);
            case ERGONOMISK_BEDOMNING:
                utlatande.setAtgardErgonomi(true);
            case HJALPMEDEL:
                utlatande.setAtgardHjalpmedel(true);
            case KONFLIKTHANTERING:
                utlatande.setAtgardKonflikthantering(true);
            case KONTAKT_MED_FORETAGSHALSOVARD:
                utlatande.setAtgardForetagshalsovard(true);
            case OMFORDELNING_AV_ARBETSUPPGIFTER:
                utlatande.setAtgardOmfordelning(true);
            case OVRIGT:
                utlatande.setAtgardOvrigt(true);
            }
        }
    }

    private static void setBedomning(SjukpenningUtlatande utlatande, BedomningTyp bedomning) {
        utlatande.setRessattTillArbeteAktuellt(bedomning.isAnnatFardmedel());
        utlatande.setRekommendationOverSocialstyrelsensBeslutsstod(bedomning.isOverskridenLangd());
        for (SjukskrivningTyp sjukskrivning : bedomning.getSjukskrivning()) {
            switch (sjukskrivning.getNedsattningsgrad()) {
            case HELT_NEDSATT:
                utlatande.setNedsattMed100(new InternalLocalDateInterval(new InternalDate(sjukskrivning.getVaraktighetFrom()), new InternalDate(
                        sjukskrivning.getVaraktighetTom())));
            case NEDSATT_MED_3_4:
                utlatande.setNedsattMed75(new InternalLocalDateInterval(new InternalDate(sjukskrivning.getVaraktighetFrom()), new InternalDate(
                        sjukskrivning.getVaraktighetTom())));
            case NEDSATT_MED_1_2:
                utlatande.setNedsattMed50(new InternalLocalDateInterval(new InternalDate(sjukskrivning.getVaraktighetFrom()), new InternalDate(
                        sjukskrivning.getVaraktighetTom())));
            case NEDSATT_MED_1_4:
                utlatande.setNedsattMed25(new InternalLocalDateInterval(new InternalDate(sjukskrivning.getVaraktighetFrom()), new InternalDate(
                        sjukskrivning.getVaraktighetTom())));
            }
        }
    }

    private static void setBehandling(SjukpenningUtlatande utlatande, BehandlingTyp behandling) {
        utlatande.setPlaneradBehandling(behandling.getPlanerade());
        utlatande.setPagaendeBehandling(behandling.getPagaende());
    }

    private static void setSjukdomar(SjukpenningUtlatande utlatande, List<SjukdomTyp> diagnoser) {
        int i = 1;
        for (SjukdomTyp sjukdom : diagnoser) {
            switch (i) {
            case 1:
                utlatande.setDiagnosKodsystem1(sjukdom.getCodeSystemId());
                utlatande.setDiagnosKod1(sjukdom.getCodeSystemCode());
                utlatande.setDiagnosBeskrivning1(sjukdom.getDescription());
            case 2:
                utlatande.setDiagnosKodsystem2(sjukdom.getCodeSystemId());
                utlatande.setDiagnosKod2(sjukdom.getCodeSystemCode());
                utlatande.setDiagnosBeskrivning2(sjukdom.getDescription());
            case 3:
                utlatande.setDiagnosKodsystem3(sjukdom.getCodeSystemId());
                utlatande.setDiagnosKod3(sjukdom.getCodeSystemCode());
                utlatande.setDiagnosBeskrivning3(sjukdom.getDescription());
            }
            i++;
        }
    }

    private static void setSysselsattning(SjukpenningUtlatande utlatande, SysselsattningTyp sysselsattning) {
        switch (sysselsattning) {
        case ARBETSLOSHET:
            utlatande.setArbetsloshet(true);
        case NUVARANDE_ARBETE:
            utlatande.setNuvarandeArbete(true);
        case FORALDRALEDIGHET:
            utlatande.setForaldraledighet(true);
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
        hosPersonal.setVardenhet(vardenhet);
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

}
