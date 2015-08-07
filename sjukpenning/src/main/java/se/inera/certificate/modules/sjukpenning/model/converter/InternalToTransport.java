package se.inera.certificate.modules.sjukpenning.model.converter;

import java.util.List;

import se.inera.certificate.model.InternalDate;
import se.inera.certificate.model.InternalLocalDateInterval;
import se.inera.certificate.model.common.internal.HoSPersonal;
import se.inera.certificate.model.converter.util.ConverterException;
import se.inera.certificate.modules.sjukpenning.model.internal.SjukpenningUtlatande;
import se.inera.intygstjanster.fk.services.registersjukpenningresponder.v1.RegisterSjukpenningType;
import se.inera.intygstjanster.fk.services.v1.*;

public class InternalToTransport {

    public static RegisterSjukpenningType convert(SjukpenningUtlatande source) throws ConverterException {
        if (source == null) {
            throw new ConverterException("Source utlatande was null, cannot convert");
        }

        RegisterSjukpenningType sjukpenningType = new RegisterSjukpenningType();
        sjukpenningType.setIntyg(getIntyg(source));
        return sjukpenningType;
    }

    private static SjukpenningIntyg getIntyg(SjukpenningUtlatande source) {
        SjukpenningIntyg sjukpenningIntyg = new SjukpenningIntyg();
        sjukpenningIntyg.setIntygsTyp(source.getTyp());
        sjukpenningIntyg.setIntygsId(source.getId());
        sjukpenningIntyg.setGrundData(getGrundData(source.getGrundData()));
        sjukpenningIntyg.setBedomning(getBedomning(source));
        sjukpenningIntyg.setBehandling(getBehandling(source));
        sjukpenningIntyg.setForslagAtgarder(getAtgarder(source));
        sjukpenningIntyg.setKanGora(source.getVadPatientenKanGora());
        sjukpenningIntyg.setKonsekvenser(getKonsekvenser(source));
        sjukpenningIntyg.setOnskarKontakt(getKontakt(source));
        sjukpenningIntyg.setOvrigt(source.getKommentar());
        sjukpenningIntyg.setPrognosAterga(source.getPrognosNarPatientKanAterga());
        sjukpenningIntyg.setSjukdomar(getSjukdomar(source));
        sjukpenningIntyg.setSysselsattning(getSysselsattning(source));
        sjukpenningIntyg.setVardKontakter(getVardkontakter(source));
        return sjukpenningIntyg;
    }

    private static VardkontakterTyp getVardkontakter(SjukpenningUtlatande source) {
        VardkontakterTyp vardkontakter = new VardkontakterTyp();
        List<VardkontaktTyp> vardkontaktList = vardkontakter.getVardkontakt();
        if (source.getTelefonkontaktMedPatienten() != null) {
            vardkontaktList.add(getVardkontakt(VardkontaktBeskrivningTyp.TELEFONKONTAKT_MED_PATIENTEN, source.getTelefonkontaktMedPatienten()));
        }
        if (source.getUndersokningAvPatienten() != null) {
            vardkontaktList.add(getVardkontakt(VardkontaktBeskrivningTyp.UNDERSOKNING_AV_PATIENTEN, source.getUndersokningAvPatienten()));
        }
        if (source.getJournaluppgifter() != null) {
            vardkontaktList.add(getVardkontakt(VardkontaktBeskrivningTyp.JOURNALUPPGIFTER, source.getJournaluppgifter()));
        }
        return vardkontakter;
    }

    private static VardkontaktTyp getVardkontakt(VardkontaktBeskrivningTyp vardkontaktBeskrivning, InternalDate kontaktDate) {
        VardkontaktTyp vardkontakt = new VardkontaktTyp();
        vardkontakt.setDatum(kontaktDate.asLocalDate());
        vardkontakt.setBeskrivning(vardkontaktBeskrivning);
        return vardkontakt;
    }

    private static SysselsattningTyp getSysselsattning(SjukpenningUtlatande source) {
        if (source.isNuvarandeArbete()) {
            return SysselsattningTyp.NUVARANDE_ARBETE;
        }
        if (source.isArbetsloshet()) {
            return SysselsattningTyp.ARBETSLOSHET;
        }
        if (source.isForaldraledighet()) {
            return SysselsattningTyp.FORALDRALEDIGHET;
        }
        return SysselsattningTyp.OKAND;
    }

    private static SjukdomarTyp getSjukdomar(SjukpenningUtlatande source) {
        SjukdomarTyp sjukdomar = new SjukdomarTyp();
        List<SjukdomTyp> sjukdomList = sjukdomar.getDiagnos();
        if (source.getDiagnosKod1() != null) {
            sjukdomList.add(getSjukdom(source.getDiagnosKodsystem1(), source.getDiagnosKod1(), source.getDiagnosBeskrivning1()));
        }
        if (source.getDiagnosKod2() != null) {
            sjukdomList.add(getSjukdom(source.getDiagnosKodsystem2(), source.getDiagnosKod2(), source.getDiagnosBeskrivning2()));
        }
        if (source.getDiagnosKod3() != null) {
            sjukdomList.add(getSjukdom(source.getDiagnosKodsystem3(), source.getDiagnosKod3(), source.getDiagnosBeskrivning3()));
        }
        return sjukdomar;
    }

    private static SjukdomTyp getSjukdom(String diagnosKodsystem, String diagnosKod, String diagnosBeskrivning) {
        SjukdomTyp sjukdom = new SjukdomTyp();
        sjukdom.setCodeSystemCode(diagnosKodsystem);
        sjukdom.setCodeSystemId(diagnosKod);
        sjukdom.setDescription(diagnosBeskrivning);
        return sjukdom;
    }

    private static KontaktTyp getKontakt(SjukpenningUtlatande source) {
        KontaktTyp kontakt = new KontaktTyp();
        kontakt.setOnskarKontakt(source.isKontaktMedFk());
        return kontakt;
    }

    private static KonsekvensTyp getKonsekvenser(SjukpenningUtlatande source) {
        if (source.getAktivitetsbegransning() != null || source.getFunktionsnedsattning() != null) {
            KonsekvensTyp konsekvens = new KonsekvensTyp();
            konsekvens.setBegransning(source.getAktivitetsbegransning());
            konsekvens.setKonsekvens(source.getFunktionsnedsattning());
            return konsekvens;
        }
        return null;
    }

    private static AtgarderTyp getAtgarder(SjukpenningUtlatande source) {
        AtgarderTyp atgarder = new AtgarderTyp();
        List<AtgardTyp> atgardList = atgarder.getAtgard();
        if (source.isAtgardArbetsanpassning()) {
            atgardList.add(AtgardTyp.ARBETSANPASSNING);
        }
        if (source.isAtgardArbetstraning()) {
            atgardList.add(AtgardTyp.ARBETSTRANING);
        }
        if (source.isAtgardBesokPaArbete()) {
            atgardList.add(AtgardTyp.BESOK_PA_ARBETSPLATSEN);
        }
        if (source.isAtgardErgonomi()) {
            atgardList.add(AtgardTyp.ERGONOMISK_BEDOMNING);
        }
        if (source.isAtgardForetagshalsovard()) {
            atgardList.add(AtgardTyp.KONTAKT_MED_FORETAGSHALSOVARD);
        }
        if (source.isAtgardHjalpmedel()) {
            atgardList.add(AtgardTyp.HJALPMEDEL);
        }
        if (source.isAtgardInteAktuellt()) {
            atgardList.add(AtgardTyp.INTE_AKTUELLT);
        }
        if (source.isAtgardKonflikthantering()) {
            atgardList.add(AtgardTyp.KONFLIKTHANTERING);
        }
        if (source.isAtgardOmfordelning()) {
            atgardList.add(AtgardTyp.OMFORDELNING_AV_ARBETSUPPGIFTER);
        }
        if (source.isAtgardSokaNyttArbete()) {
            atgardList.add(AtgardTyp.SOKA_NYTT_ARBETE);
        }
        if (source.isAtgardOvrigt()) {
            atgardList.add(AtgardTyp.OVRIGT);
        }
        if (atgardList.size() > 0) {
            return atgarder;
        }
        return null;
    }

    private static BehandlingTyp getBehandling(SjukpenningUtlatande source) {
        if (source.getPagaendeBehandling() != null || source.getPlaneradBehandling() != null) {
            BehandlingTyp behandling = new BehandlingTyp();
            behandling.setPagaende(source.getPagaendeBehandling());
            behandling.setPlanerade(source.getPlaneradBehandling());
            return behandling;
        }
        return null;
    }

    private static BedomningTyp getBedomning(SjukpenningUtlatande source) {
        BedomningTyp bedomning = new BedomningTyp();
        List<SjukskrivningTyp> sjukskrivningar = bedomning.getSjukskrivning();
        if (source.getNedsattMed100() != null) {
            sjukskrivningar.add(getSjukskrivning(Nedsattningsgrad.HELT_NEDSATT, source.getNedsattMed100()));
        }
        if (source.getNedsattMed75() != null) {
            sjukskrivningar.add(getSjukskrivning(Nedsattningsgrad.NEDSATT_MED_3_4, source.getNedsattMed75()));
        }
        if (source.getNedsattMed50() != null) {
            sjukskrivningar.add(getSjukskrivning(Nedsattningsgrad.NEDSATT_MED_1_2, source.getNedsattMed50()));
        }
        if (source.getNedsattMed25() != null) {
            sjukskrivningar.add(getSjukskrivning(Nedsattningsgrad.NEDSATT_MED_3_4, source.getNedsattMed25()));
        }
        bedomning.setAnnatFardmedel(source.isRessattTillArbeteAktuellt());
        bedomning.setOverskridenLangd(source.isRekommendationOverSocialstyrelsensBeslutsstod());
        return bedomning;
    }

    private static SjukskrivningTyp getSjukskrivning(Nedsattningsgrad nedsattningsgrad, InternalLocalDateInterval nedsattningsperiod) {
        SjukskrivningTyp sjukskrivning = new SjukskrivningTyp();
        sjukskrivning.setNedsattningsgrad(nedsattningsgrad);
        sjukskrivning.setVaraktighetFrom(nedsattningsperiod.fromAsLocalDate());
        sjukskrivning.setVaraktighetTom(nedsattningsperiod.tomAsLocalDate());
        return sjukskrivning;
    }

    private static GrundData getGrundData(se.inera.certificate.model.common.internal.GrundData sourceGrundData) {
        GrundData grundData = new GrundData();
        grundData.setPatient(getPatient(sourceGrundData.getPatient()));
        grundData.setSkapadAv(getSkapadAv(sourceGrundData.getSkapadAv()));
        grundData.setSigneringsTidstampel(sourceGrundData.getSigneringsdatum());
        return grundData;
    }

    private static SkapadAv getSkapadAv(HoSPersonal sourceSkapadAv) {
        SkapadAv skapadAv = new SkapadAv();
        skapadAv.setPersonId(sourceSkapadAv.getPersonId());
        skapadAv.setFullstandigtNamn(sourceSkapadAv.getFullstandigtNamn());
        skapadAv.setVardenhet(getVardenhet(sourceSkapadAv.getVardenhet()));
        return skapadAv;
    }

    private static Vardenhet getVardenhet(se.inera.certificate.model.common.internal.Vardenhet sourceVardenhet) {
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setEnhetsId(sourceVardenhet.getEnhetsid());
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
        vardgivare.setVardgivarid(sourceVardgivare.getVardgivarid());
        vardgivare.setVardgivarnamn(sourceVardgivare.getVardgivarnamn());
        return vardgivare;
    }

    private static Patient getPatient(se.inera.certificate.model.common.internal.Patient sourcePatient) {
        Patient patient = new Patient();
        patient.setFornamn(sourcePatient.getFornamn());
        patient.setEfternamn(sourcePatient.getEfternamn());
        patient.setFullstandigtNamn(sourcePatient.getFullstandigtNamn());
        patient.setPersonId(sourcePatient.getPersonId());
        patient.setPostadress(sourcePatient.getPostadress());
        patient.setPostort(sourcePatient.getPostort());
        patient.setPostnummer(sourcePatient.getPostnummer());
        return patient;
    }

}
