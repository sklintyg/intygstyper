package se.inera.certificate.modules.sjukersattning.model.converter;

import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.*;

import javax.xml.bind.JAXBElement;

import se.inera.certificate.model.InternalDate;
import se.inera.certificate.model.common.internal.*;
import se.inera.certificate.model.converter.util.ConverterException;
import se.inera.certificate.modules.fkparent.model.converter.RespConstants;
import se.inera.certificate.modules.sjukersattning.model.internal.SjukersattningUtlatande;
import se.inera.certificate.modules.sjukersattning.model.internal.Underlag;
import se.inera.certificate.modules.support.api.dto.CertificateMetaData;
import se.inera.certificate.modules.support.api.dto.Personnummer;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.CVType;
import se.riv.clinicalprocess.healthcond.certificate.v2.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v2.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v2.Svar.Delsvar;

public class TransportToInternal {

    public static SjukersattningUtlatande convert(Intyg source) throws ConverterException {
        SjukersattningUtlatande utlatande = new SjukersattningUtlatande();
        utlatande.setId(source.getIntygsId().getRoot());
        utlatande.setGrundData(getGrundData(source));
        setSvar(utlatande, source);
        return utlatande;
    }

    private static void setSvar(SjukersattningUtlatande utlatande, Intyg source) {
        for (Svar svar : source.getSvar()) {
            switch (svar.getId()) {
            case REFERENS_SVAR_ID:
                handleReferens(utlatande, svar);
                break;
            case OVRIGKANNEDOM_SVAR_ID:
                handleOvrigKannedom(utlatande, svar);
                break;
            case UNDERLAG_SVAR_ID:
                handleUnderlag(utlatande, svar);
                break;
            case HUVUDSAKLIG_ORSAK_SVAR_ID:
                handleHuvudsakligOrsak(utlatande, svar);
                break;
            case YTTERLIGARE_ORSAK_SVAR_ID:
                handleYtterligareOrsak(utlatande, svar);
                break;
            case DIAGNOSTISERING_SVAR_ID:
                handleDiagnostisering(utlatande, svar);
                break;
            case NYBEDOMNING_SVAR_ID:
                handleNyBedomning(utlatande, svar);
                break;
            case FUNKTIONSNEDSATTNING_SVAR_ID:
                handleFunktionsNedsattning(utlatande, svar);
                break;
            case AKTIVITETSBEGRANSNING_SVAR_ID:
                handleAktivitetsbegransning(utlatande, svar);
                break;
            case PAGAENDEBEHANDLING_SVAR_ID:
                handlePagaendeBehandling(utlatande, svar);
                break;
            case AVSLUTADBEHANDLING_SVAR_ID:
                handleAvslutadBehandling(utlatande, svar);
                break;
            case PLANERADBEHANDLING_SVAR_ID:
                handlePlaneradBehandling(utlatande, svar);
                break;
            case AKTIVITETSFORMAGA_SVAR_ID:
                handleAktivitetsformaga(utlatande, svar);
                break;
            case PROGNOS_SVAR_ID:
                handlePrognos(utlatande, svar);
                break;
            case OVRIGT_SVAR_ID:
                handleOvrigt(utlatande, svar);
                break;
            case KONTAKT_ONSKAS_SVAR_ID:
                handleOnskarKontakt(utlatande, svar);
                break;
            }
        }
    }

    private static void handleReferens(SjukersattningUtlatande utlatande, Svar svar) {
        InternalDate referensDatum = null;
        RespConstants.ReferensTyp referensTyp = ReferensTyp.UNKNOWN;
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case REFERENSDATUM_DELSVAR_ID:
                referensDatum = new InternalDate(getSvarContent(delsvar, String.class));
                break;
            case REFERENSTYP_DELSVAR_ID:
                String referensTypString = getSvarContent(delsvar, CVType.class).getCode();
                referensTyp = RespConstants.ReferensTyp.byTransport(referensTypString);
                break;
            default:
                throw new IllegalArgumentException();
            }
        }

        switch (referensTyp) {
        case UNDERSOKNING:
            utlatande.setUndersokningAvPatienten(referensDatum);
            break;
        case TELEFONKONTAKT:
            utlatande.setTelefonkontaktMedPatienten(referensDatum);
            break;
        case JOURNAL:
            utlatande.setJournaluppgifter(referensDatum);
            break;
        default:
            throw new IllegalArgumentException();
        }
    }

    private static void handleOvrigKannedom(SjukersattningUtlatande utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
        case OVRIGKANNEDOM_DELSVAR_ID:
            utlatande.setKannedomOmPatient(new InternalDate(getSvarContent(delsvar, String.class)));
            return;
        default:
            throw new IllegalArgumentException();
        }
    }

    private static void handleUnderlag(SjukersattningUtlatande utlatande, Svar svar) {
        Underlag.UnderlagsTyp underlagsTyp = Underlag.UnderlagsTyp.OKAND;
        InternalDate date = null;
        Boolean attachment = null;
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case UNDERLAG_TYP_DELSVAR_ID:
                CVType typ = getSvarContent(delsvar, CVType.class);
                underlagsTyp = Underlag.UnderlagsTyp.fromId(Integer.parseInt(typ.getCode()));
                break;
            case UNDERLAG_DATUM_DELSVAR_ID:
                date = new InternalDate(getSvarContent(delsvar, String.class));
                break;
            case UNDERLAG_BILAGA_DELSVAR_ID:
                String svarString = getSvarContent(delsvar, String.class);
                attachment = Boolean.parseBoolean(svarString);
                break;
            default:
                throw new IllegalArgumentException();
            }
        }
        utlatande.getUnderlag().add(new Underlag(underlagsTyp, date, attachment));
    }

    private static void handleHuvudsakligOrsak(SjukersattningUtlatande utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case DIAGNOS_DELSVAR_ID:
                CVType diagnos = getSvarContent(delsvar, CVType.class);
                utlatande.setDiagnosKod1(diagnos.getCode());
                utlatande.setDiagnosKodsystem1(diagnos.getCodeSystem());
                break;
            case DIAGNOS_BESKRIVNING_DELSVAR_ID:
                String diagnosBeskrivning = getSvarContent(delsvar, String.class);
                utlatande.setDiagnosBeskrivning1(diagnosBeskrivning);
                break;
            default:
                throw new IllegalArgumentException();
            }
        }
    }

    private static void handleYtterligareOrsak(SjukersattningUtlatande utlatande, Svar svar) {
        String diagnosKodSystem = null;
        String diagnosKod = null;
        String diagnosBeskrivning = null;
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case YTTERLIGARE_ORSAK_DELSVAR_ID:
                CVType diagnos = getSvarContent(delsvar, CVType.class);
                diagnosKodSystem = diagnos.getCodeSystem();
                diagnosKod = diagnos.getCode();
                break;
            case YTTERLIGARE_ORSAK_BESKRIVNING_DELSVAR_ID:
                diagnosBeskrivning = getSvarContent(delsvar, String.class);
                break;
            default:
                throw new IllegalArgumentException();
            }
        }
        if (diagnosKodSystem.equals(BEHANDLINGSATGARD_CODE_SYSTEM)) {
            if (utlatande.getBehandlingsAtgardKod1() == null) {
                utlatande.setBehandlingsAtgardKod1(diagnosKod);
                utlatande.setBehandlingsAtgardBeskrivning1(diagnosBeskrivning);
            } else {
                utlatande.setBehandlingsAtgardKod2(diagnosKod);
                utlatande.setBehandlingsAtgardBeskrivning2(diagnosBeskrivning);
            }
        } else {
            if (utlatande.getDiagnosKod2() == null) {
                utlatande.setDiagnosKodsystem2(diagnosKodSystem);
                utlatande.setDiagnosKod2(diagnosKod);
                utlatande.setDiagnosBeskrivning2(diagnosBeskrivning);
            } else {
                utlatande.setDiagnosKodsystem3(diagnosKodSystem);
                utlatande.setDiagnosKod3(diagnosKod);
                utlatande.setDiagnosBeskrivning3(diagnosBeskrivning);
            }
        }
    }

    private static void handleDiagnostisering(SjukersattningUtlatande utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
        case DIAGNOSTISERING_DELSVAR_ID:
            utlatande.setDiagnostisering(getSvarContent(delsvar, String.class));
            return;
        default:
            throw new IllegalArgumentException();
        }
    }

    private static void handleNyBedomning(SjukersattningUtlatande utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
        case NYBEDOMNING_DELSVAR_ID:
            utlatande.setNyBedomningDiagnos(Boolean.valueOf(getSvarContent(delsvar, String.class)));
            return;
        default:
            throw new IllegalArgumentException();
        }
    }

    private static void handleFunktionsNedsattning(SjukersattningUtlatande utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
        case FUNKTIONSNEDSATTNING_DELSVAR_ID:
            utlatande.setFunktionsnedsattning(getSvarContent(delsvar, String.class));
            return;
        default:
            throw new IllegalArgumentException();
        }
    }

    private static void handleAktivitetsbegransning(SjukersattningUtlatande utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
        case AKTIVITETSBEGRANSNING_DELSVAR_ID:
            utlatande.setAktivitetsbegransning(getSvarContent(delsvar, String.class));
            return;
        default:
            throw new IllegalArgumentException();
        }
    }

    private static void handlePagaendeBehandling(SjukersattningUtlatande utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
        case PAGAENDEBEHANDLING_DELSVAR_ID:
            utlatande.setPagaendeBehandling(getSvarContent(delsvar, String.class));
            return;
        default:
            throw new IllegalArgumentException();
        }
    }

    private static void handleAvslutadBehandling(SjukersattningUtlatande utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
        case AVSLUTADBEHANDLING_DELSVAR_ID:
            utlatande.setAvslutadBehandling(getSvarContent(delsvar, String.class));
            return;
        default:
            throw new IllegalArgumentException();
        }
    }

    private static void handlePlaneradBehandling(SjukersattningUtlatande utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
        case PLANERADBEHANDLING_DELSVAR_ID:
            utlatande.setPlaneradBehandling(getSvarContent(delsvar, String.class));
            return;
        default:
            throw new IllegalArgumentException();
        }
    }

    private static void handleAktivitetsformaga(SjukersattningUtlatande utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
        case AKTIVITETSFORMAGA_DELSVAR_ID:
            utlatande.setVadPatientenKanGora(getSvarContent(delsvar, String.class));
            return;
        default:
            throw new IllegalArgumentException();
        }
    }

    private static void handlePrognos(SjukersattningUtlatande utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
        case PROGNOS_DELSVAR_ID:
            utlatande.setPrognosNarPatientKanAterga(getSvarContent(delsvar, String.class));
            return;
        default:
            throw new IllegalArgumentException();
        }
    }

    private static void handleOvrigt(SjukersattningUtlatande utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
        case OVRIGT_DELSVAR_ID:
            utlatande.setKommentar(getSvarContent(delsvar, String.class));
            return;
        default:
            throw new IllegalArgumentException();
        }
    }

    private static void handleOnskarKontakt(SjukersattningUtlatande utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
        case KONTAKT_ONSKAS_DELSVAR_ID:
            utlatande.setKontaktMedFk(Boolean.valueOf(getSvarContent(delsvar, String.class)));
            return;
        default:
            throw new IllegalArgumentException();
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T getSvarContent(Delsvar delsvar, Class<T> clazz) {
        Object content = delsvar.getContent().get(0);
        if (content instanceof JAXBElement) {
            return ((JAXBElement<T>) content).getValue();
        }
        return (T) content;
    }

    private static GrundData getGrundData(Intyg source) {
        GrundData grundData = new GrundData();
        grundData.setPatient(getPatient(source));
        grundData.setSkapadAv(getSkapadAv(source));
        return grundData;
    }

    private static HoSPersonal getSkapadAv(Intyg source) {
        HoSPersonal personal = new HoSPersonal();
        personal.setPersonId(source.getSkapadAv().getPersonalId().getExtension());
        personal.setFullstandigtNamn(source.getSkapadAv().getFullstandigtNamn());
        personal.setForskrivarKod(source.getSkapadAv().getForskrivarkod());
        personal.setVardenhet(getVardenhet(source));
        return personal;
    }

    private static Vardenhet getVardenhet(Intyg source) {
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setPostort(source.getSkapadAv().getEnhet().getPostort());
        vardenhet.setPostadress(source.getSkapadAv().getEnhet().getPostadress());
        vardenhet.setPostnummer(source.getSkapadAv().getEnhet().getPostnummer());
        vardenhet.setEpost(source.getSkapadAv().getEnhet().getEpost());
        vardenhet.setEnhetsid(source.getSkapadAv().getEnhet().getEnhetsId().getExtension());
        vardenhet.setArbetsplatsKod(source.getSkapadAv().getEnhet().getArbetsplatskod().getExtension());
        vardenhet.setEnhetsnamn(source.getSkapadAv().getEnhet().getEnhetsnamn());
        vardenhet.setTelefonnummer(source.getSkapadAv().getEnhet().getTelefonnummer());
        vardenhet.setVardgivare(getVardgivare(source));
        return vardenhet;
    }

    private static Vardgivare getVardgivare(Intyg source) {
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarid(source.getSkapadAv().getEnhet().getVardgivare().getVardgivareId().getExtension());
        vardgivare.setVardgivarnamn(source.getSkapadAv().getEnhet().getVardgivare().getVardgivarnamn());
        return vardgivare;
    }

    private static Patient getPatient(Intyg source) {
        Patient patient = new Patient();
        patient.setEfternamn(source.getPatient().getEfternamn());
        patient.setFornamn(source.getPatient().getFornamn());
        patient.setMellannamn(source.getPatient().getMellannamn());
        patient.setPostort(source.getPatient().getPostort());
        patient.setPostnummer(source.getPatient().getPostnummer());
        patient.setPostadress(source.getPatient().getPostadress());
        patient.setPersonId(new Personnummer(source.getPatient().getPersonId().getExtension()));
        return patient;
    }

    public static CertificateMetaData getMetaData(Intyg source) {
        CertificateMetaData metaData = new CertificateMetaData();
        return metaData;
    }

}
