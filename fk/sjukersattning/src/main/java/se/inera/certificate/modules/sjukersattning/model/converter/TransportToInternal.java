/*
 * Copyright (C) 2015 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.inera.certificate.modules.sjukersattning.model.converter;

import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.AKTIVITETSBEGRANSNING_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.AKTIVITETSFORMAGA_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.AKTIVITETSFORMAGA_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.ANLEDNING_TILL_KONTAKT_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.AVSLUTADBEHANDLING_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.AVSLUTADBEHANDLING_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.BEHANDLINGSATGARD_CODE_SYSTEM;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.DIAGNOSTISERING_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.DIAGNOSTISERING_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.DIAGNOS_BESKRIVNING_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.DIAGNOS_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_BESKRIVNING_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_FUNKTIONSOMRADE_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.HUVUDSAKLIG_ORSAK_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.KONTAKT_ONSKAS_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.KONTAKT_ONSKAS_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.NYBEDOMNING_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.NYBEDOMNING_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.OVRIGKANNEDOM_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.OVRIGKANNEDOM_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.OVRIGT_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.OVRIGT_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.PAGAENDEBEHANDLING_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.PAGAENDEBEHANDLING_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.PLANERADBEHANDLING_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.PLANERADBEHANDLING_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.PROGNOS_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.PROGNOS_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.REFERENSDATUM_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.REFERENSTYP_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.REFERENS_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.UNDERLAG_BILAGA_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.UNDERLAG_DATUM_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.UNDERLAG_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.UNDERLAG_TYP_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.YTTERLIGARE_ORSAK_BESKRIVNING_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.YTTERLIGARE_ORSAK_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.YTTERLIGARE_ORSAK_SVAR_ID;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import se.inera.certificate.modules.fkparent.model.converter.RespConstants.ReferensTyp;
import se.inera.certificate.modules.sjukersattning.model.internal.BehandlingsAtgard;
import se.inera.certificate.modules.sjukersattning.model.internal.Diagnos;
import se.inera.certificate.modules.sjukersattning.model.internal.Funktionsnedsattning;
import se.inera.certificate.modules.sjukersattning.model.internal.SjukersattningUtlatande;
import se.inera.certificate.modules.sjukersattning.model.internal.SjukersattningUtlatande.Builder;
import se.inera.certificate.modules.sjukersattning.model.internal.Underlag;
import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Patient;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.inera.intyg.common.support.model.common.internal.Vardgivare;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateMetaData;
import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.Befattning;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.CVType;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.Specialistkompetens;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.Statuskod;
import se.riv.clinicalprocess.healthcond.certificate.v2.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v2.IntygsStatus;
import se.riv.clinicalprocess.healthcond.certificate.v2.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v2.Svar.Delsvar;

public final class TransportToInternal {

    private TransportToInternal() {
    }

    public static SjukersattningUtlatande convert(Intyg source) throws ConverterException {
        Builder utlatande = SjukersattningUtlatande.builder();
        utlatande.setId(source.getIntygsId().getExtension());
        utlatande.setGrundData(getGrundData(source));
        setSvar(utlatande, source);
        return utlatande.build();
    }

    private static GrundData getGrundData(Intyg source) {
        GrundData grundData = new GrundData();
        grundData.setPatient(getPatient(source));
        grundData.setSkapadAv(getSkapadAv(source));
        grundData.setSigneringsdatum(source.getSigneringstidpunkt());
        return grundData;
    }

    private static HoSPersonal getSkapadAv(Intyg source) {
        HoSPersonal personal = new HoSPersonal();
        personal.setPersonId(source.getSkapadAv().getPersonalId().getExtension());
        personal.setFullstandigtNamn(source.getSkapadAv().getFullstandigtNamn());
        personal.setForskrivarKod(source.getSkapadAv().getForskrivarkod());
        personal.setVardenhet(getVardenhet(source));
        for (Befattning befattning : source.getSkapadAv().getBefattning()) {
            personal.getBefattningar().add(befattning.getCode());
        }
        for (Specialistkompetens kompetens : source.getSkapadAv().getSpecialistkompetens()) {
            personal.getSpecialiteter().add(kompetens.getCode());
        }
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
        metaData.setCertificateId(source.getIntygsId().getExtension());
        metaData.setCertificateType(source.getTyp().getCode());
        // TODO
        metaData.setValidFrom(null);
        metaData.setValidTo(null);
        metaData.setIssuerName(source.getSkapadAv().getFullstandigtNamn());
        metaData.setFacilityName(source.getSkapadAv().getEnhet().getEnhetsnamn());
        metaData.setSignDate(source.getSigneringstidpunkt());
        // TODO
        metaData.setAdditionalInfo(null);
        List<Status> statuses = toStatusList(source.getStatus());
        metaData.setStatus(statuses);
        return metaData;
    }

    private static List<Status> toStatusList(List<IntygsStatus> certificateStatuses) {
        List<Status> statuses = new ArrayList<>(certificateStatuses.size());
        for (IntygsStatus certificateStatus : certificateStatuses) {
            statuses.add(toStatus(certificateStatus));
        }
        return statuses;
    }

    private static Status toStatus(IntygsStatus certificateStatus) {
        return new Status(
                getState(certificateStatus.getStatus()),
                certificateStatus.getPart().getCode(),
                certificateStatus.getTidpunkt());
    }

    private static CertificateState getState(Statuskod status) {
        switch (status.getCode()) {
        case "DELETED":
            return CertificateState.DELETED;
        case "RESTORED":
            return CertificateState.RESTORED;
        case "CANCELLED":
            return CertificateState.CANCELLED;
        case "SENT":
            return CertificateState.SENT;
        case "RECEIVED":
            return CertificateState.RECEIVED;
        default:
            throw new IllegalArgumentException();
        }
    }

    private static void setSvar(Builder utlatande, Intyg source) {
        List<Underlag> underlag = new ArrayList<>();
        List<Diagnos> diagnoser = new ArrayList<>();
        List<BehandlingsAtgard> atgarder = new ArrayList<>();
        List<Funktionsnedsattning> funktionsnedsattningar = new ArrayList<>();

        for (Svar svar : source.getSvar()) {
            switch (svar.getId()) {
            case REFERENS_SVAR_ID:
                handleReferens(utlatande, svar);
                break;
            case OVRIGKANNEDOM_SVAR_ID:
                handleOvrigKannedom(utlatande, svar);
                break;
            case UNDERLAG_SVAR_ID:
                handleUnderlag(underlag, svar);
                break;
            case HUVUDSAKLIG_ORSAK_SVAR_ID:
                handleHuvudsakligOrsak(diagnoser, svar);
                break;
            case YTTERLIGARE_ORSAK_SVAR_ID:
                handleYtterligareOrsak(diagnoser, atgarder, svar);
                break;
            case DIAGNOSTISERING_SVAR_ID:
                handleDiagnostisering(utlatande, svar);
                break;
            case NYBEDOMNING_SVAR_ID:
                handleNyBedomning(utlatande, svar);
                break;
            case FUNKTIONSNEDSATTNING_SVAR_ID:
                handleFunktionsNedsattning(funktionsnedsattningar, svar);
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
            default:
                break;
            }
        }

        utlatande.setUnderlag(underlag);
        utlatande.setDiagnoser(diagnoser);
        utlatande.setAtgarder(atgarder);
        utlatande.setFunktionsnedsattningar(funktionsnedsattningar);
    }

    private static void handleReferens(Builder utlatande, Svar svar) {
        InternalDate referensDatum = null;
        ReferensTyp referensTyp = ReferensTyp.UNKNOWN;
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case REFERENSDATUM_DELSVAR_ID:
                referensDatum = new InternalDate(getSvarContent(delsvar, String.class));
                break;
            case REFERENSTYP_DELSVAR_ID:
                String referensTypString = getSvarContent(delsvar, CVType.class).getCode();
                referensTyp = ReferensTyp.byTransport(referensTypString);
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

    private static void handleOvrigKannedom(Builder utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
        case OVRIGKANNEDOM_DELSVAR_ID:
            utlatande.setKannedomOmPatient(new InternalDate(getSvarContent(delsvar, String.class)));
            break;
        default:
            throw new IllegalArgumentException();
        }
    }

    private static void handleUnderlag(List<Underlag> underlag, Svar svar) {
        Underlag.UnderlagsTyp underlagsTyp = Underlag.UnderlagsTyp.OKAND;
        InternalDate date = null;
        boolean attachment = false;
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
        underlag.add(Underlag.create(underlagsTyp, date, attachment));
    }

    private static void handleHuvudsakligOrsak(List<Diagnos> diagnoser, Svar svar) {
        String diagnosKod = null;
        String diagnosKodSystem = null;
        String diagnosBeskrivning = null;
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case DIAGNOS_DELSVAR_ID:
                CVType diagnos = getSvarContent(delsvar, CVType.class);
                diagnosKod = diagnos.getCode();
                diagnosKodSystem = diagnos.getCodeSystem();
                break;
            case DIAGNOS_BESKRIVNING_DELSVAR_ID:
                diagnosBeskrivning = getSvarContent(delsvar, String.class);
                break;
            default:
                throw new IllegalArgumentException();
            }
        }
        diagnoser.add(0, Diagnos.create(diagnosKod, diagnosKodSystem, diagnosBeskrivning));
    }

    private static void handleYtterligareOrsak(List<Diagnos> diagnoser, List<BehandlingsAtgard> atgarder, Svar svar) {
        String kod = null;
        String kodSystem = null;
        String beskrivning = null;
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case YTTERLIGARE_ORSAK_DELSVAR_ID:
                CVType diagnos = getSvarContent(delsvar, CVType.class);
                kodSystem = diagnos.getCodeSystem();
                kod = diagnos.getCode();
                break;
            case YTTERLIGARE_ORSAK_BESKRIVNING_DELSVAR_ID:
                beskrivning = getSvarContent(delsvar, String.class);
                break;
            default:
                throw new IllegalArgumentException();
            }
        }
        if (BEHANDLINGSATGARD_CODE_SYSTEM.equals(kodSystem)) {
            atgarder.add(BehandlingsAtgard.create(kod, kodSystem, beskrivning));
        } else {
            diagnoser.add(Diagnos.create(kod, kodSystem, beskrivning));
        }
    }

    private static void handleDiagnostisering(Builder utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
        case DIAGNOSTISERING_DELSVAR_ID:
            utlatande.setDiagnostisering(getSvarContent(delsvar, String.class));
            break;
        default:
            throw new IllegalArgumentException();
        }
    }

    private static void handleNyBedomning(Builder utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
        case NYBEDOMNING_DELSVAR_ID:
            utlatande.setNyBedomningDiagnos(Boolean.valueOf(getSvarContent(delsvar, String.class)));
            break;
        default:
            throw new IllegalArgumentException();
        }
    }

    private static void handleFunktionsNedsattning(List<Funktionsnedsattning> funktionsnedsattningar, Svar svar) {
        String beskrivning = null;
        Funktionsnedsattning.Funktionsomrade funktionsomrade = Funktionsnedsattning.Funktionsomrade.OKAND;
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case FUNKTIONSNEDSATTNING_BESKRIVNING_DELSVAR_ID:
                beskrivning = getSvarContent(delsvar, String.class);
                break;
            case FUNKTIONSNEDSATTNING_FUNKTIONSOMRADE_DELSVAR_ID:
                CVType funktionsomradestype = getSvarContent(delsvar, CVType.class);
                funktionsomrade = Funktionsnedsattning.Funktionsomrade.fromId(Integer.parseInt(funktionsomradestype.getCode()));
                break;
            default:
                throw new IllegalArgumentException();
            }
        }
        funktionsnedsattningar.add(Funktionsnedsattning.create(funktionsomrade, beskrivning));
    }

    private static void handleAktivitetsbegransning(Builder utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
        case AKTIVITETSBEGRANSNING_DELSVAR_ID:
            utlatande.setAktivitetsbegransning(getSvarContent(delsvar, String.class));
            break;
        default:
            throw new IllegalArgumentException();
        }
    }

    private static void handlePagaendeBehandling(Builder utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
        case PAGAENDEBEHANDLING_DELSVAR_ID:
            utlatande.setPagaendeBehandling(getSvarContent(delsvar, String.class));
            break;
        default:
            throw new IllegalArgumentException();
        }
    }

    private static void handleAvslutadBehandling(Builder utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
        case AVSLUTADBEHANDLING_DELSVAR_ID:
            utlatande.setAvslutadBehandling(getSvarContent(delsvar, String.class));
            break;
        default:
            throw new IllegalArgumentException();
        }
    }

    private static void handlePlaneradBehandling(Builder utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
        case PLANERADBEHANDLING_DELSVAR_ID:
            utlatande.setPlaneradBehandling(getSvarContent(delsvar, String.class));
            break;
        default:
            throw new IllegalArgumentException();
        }
    }

    private static void handleAktivitetsformaga(Builder utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
        case AKTIVITETSFORMAGA_DELSVAR_ID:
            utlatande.setAktivitetsFormaga(getSvarContent(delsvar, String.class));
            break;
        default:
            throw new IllegalArgumentException();
        }
    }

    private static void handlePrognos(Builder utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
        case PROGNOS_DELSVAR_ID:
            utlatande.setPrognos(getSvarContent(delsvar, String.class));
            break;
        default:
            throw new IllegalArgumentException();
        }
    }

    private static void handleOvrigt(Builder utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
        case OVRIGT_DELSVAR_ID:
            utlatande.setOvrigt(getSvarContent(delsvar, String.class));
            return;
        default:
            throw new IllegalArgumentException();
        }
    }

    private static void handleOnskarKontakt(Builder utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case KONTAKT_ONSKAS_DELSVAR_ID:
                utlatande.setKontaktMedFk(Boolean.valueOf(getSvarContent(delsvar, String.class)));
                break;
            case ANLEDNING_TILL_KONTAKT_DELSVAR_ID:
                utlatande.setAnledningTillKontakt(getSvarContent(delsvar, String.class));
                break;
            default:
                throw new IllegalArgumentException();
            }
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

}
