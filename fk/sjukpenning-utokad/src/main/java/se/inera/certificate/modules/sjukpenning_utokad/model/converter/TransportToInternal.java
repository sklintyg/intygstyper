/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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

package se.inera.certificate.modules.sjukpenning_utokad.model.converter;

import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.AKTIVITETSBEGRANSNING_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.AKTIVITETSFORMAGA_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.AKTIVITETSFORMAGA_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.ANLEDNING_TILL_KONTAKT_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_AKTUELLT_BESKRIVNING_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_EJ_AKTUELLT_BESKRIVNING_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.ARBETSLIVSINRIKTADE_ATGARDER_VAL_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.ARBETSMARKNADSPOLITISKT_PROGRAM_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.ARBETSMARKNADSPOLITISKT_PROGRAM_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.ARBETSRESOR_OM_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.ARBETSRESOR_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.ARBETSTIDSFORLAGGNING_OM_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.ARBETSTIDSFORLAGGNING_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_NIVA_DELSVARSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_PERIOD_DELSVARSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.BEHOV_AV_SJUKSKRIVNING_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.DIAGNOS_BESKRIVNING_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.DIAGNOS_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.DIAGNOS_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.FORSAKRINGSMEDICINSKT_BESLUTSSTOD_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.KONTAKT_ONSKAS_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.KONTAKT_ONSKAS_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.NUVARANDE_ARBETE_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.NUVARANDE_ARBETE_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.OVRIGT_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.OVRIGT_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.PAGAENDEBEHANDLING_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.PAGAENDEBEHANDLING_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.PLANERADBEHANDLING_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.PLANERADBEHANDLING_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.PROGNOS_BESKRIVNING_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.PROGNOS_FORTYDLIGANDE_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.PROGNOS_SVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.TYP_AV_SYSSELSATTNING_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.TYP_AV_SYSSELSATTNING_SVAR_ID;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.commons.lang3.StringUtils;

import se.inera.certificate.modules.fkparent.model.converter.RespConstants.ReferensTyp;
import se.inera.certificate.modules.sjukpenning_utokad.model.internal.ArbetslivsinriktadeAtgarder;
import se.inera.certificate.modules.sjukpenning_utokad.model.internal.ArbetslivsinriktadeAtgarder.ArbetslivsinriktadeAtgarderVal;
import se.inera.certificate.modules.sjukpenning_utokad.model.internal.Diagnos;
import se.inera.certificate.modules.sjukpenning_utokad.model.internal.Prognos;
import se.inera.certificate.modules.sjukpenning_utokad.model.internal.Prognos.PrognosTyp;
import se.inera.certificate.modules.sjukpenning_utokad.model.internal.SjukpenningUtokadUtlatande;
import se.inera.certificate.modules.sjukpenning_utokad.model.internal.SjukpenningUtokadUtlatande.Builder;
import se.inera.certificate.modules.sjukpenning_utokad.model.internal.Sjukskrivning;
import se.inera.certificate.modules.sjukpenning_utokad.model.internal.Sjukskrivning.SjukskrivningsGrad;
import se.inera.certificate.modules.sjukpenning_utokad.model.internal.Sysselsattning;
import se.inera.certificate.modules.sjukpenning_utokad.model.internal.Sysselsattning.SysselsattningsTyp;
import se.inera.certificate.modules.sjukpenning_utokad.model.internal.Tillaggsfraga;
import se.inera.intyg.common.support.common.enumerations.Diagnoskodverk;
import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;
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
import se.riv.clinicalprocess.healthcond.certificate.types.v2.DatePeriodType;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.Specialistkompetens;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.Statuskod;
import se.riv.clinicalprocess.healthcond.certificate.v2.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v2.IntygsStatus;
import se.riv.clinicalprocess.healthcond.certificate.v2.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v2.Svar.Delsvar;

public final class TransportToInternal {

    private static final int TILLAGGSFRAGA_START = 9001;

    private TransportToInternal() {
    }

    public static SjukpenningUtokadUtlatande convert(Intyg source) throws ConverterException {
        Builder utlatande = SjukpenningUtokadUtlatande.builder();
        utlatande.setId(source.getIntygsId().getExtension());
        utlatande.setGrundData(getGrundData(source));
        utlatande.setTextVersion(source.getVersion());
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
        List<Diagnos> diagnoser = new ArrayList<>();
        List<Tillaggsfraga> tillaggsfragor = new ArrayList<>();
        List<Sjukskrivning> sjukskrivningar = new ArrayList<>();

        for (Svar svar : source.getSvar()) {
            switch (svar.getId()) {
            case GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID:
                handleGrundForMedicinsktUnderlag(utlatande, svar);
                break;
            case TYP_AV_SYSSELSATTNING_SVAR_ID:
                handleSysselsattning(utlatande, svar);
                break;
            case NUVARANDE_ARBETE_SVAR_ID:
                handleNuvarandeArbete(utlatande, svar);
                break;
            case ARBETSMARKNADSPOLITISKT_PROGRAM_SVAR_ID:
                handleArbetsmarknadspolitisktProgram(utlatande, svar);
                break;
            case DIAGNOS_SVAR_ID:
                handleDiagnos(diagnoser, svar);
                break;
            case FUNKTIONSNEDSATTNING_SVAR_ID:
                handleFunktionsnedsattning(utlatande, svar);
                break;
            case AKTIVITETSBEGRANSNING_SVAR_ID:
                handleAktivitetsbegransning(utlatande, svar);
                break;
            case PAGAENDEBEHANDLING_SVAR_ID:
                handlePagaendeBehandling(utlatande, svar);
                break;
            case PLANERADBEHANDLING_SVAR_ID:
                handlePlaneradBehandling(utlatande, svar);
                break;
            case BEHOV_AV_SJUKSKRIVNING_SVAR_ID:
                handleBehovAvSjukskrivning(sjukskrivningar, svar);
                break;
            case FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_ID:
                handleForsakringsmedicinsktBeslutsstod(utlatande, svar);
                break;
            case ARBETSTIDSFORLAGGNING_SVAR_ID:
                handleArbetstidsforlaggning(utlatande, svar);
                break;
            case ARBETSRESOR_SVAR_ID:
                handleArbetsresor(utlatande, svar);
                break;
            case AKTIVITETSFORMAGA_SVAR_ID:
                handleFormagaTrotsBegransning(utlatande, svar);
                break;
            case PROGNOS_SVAR_ID:
                handlePrognos(utlatande, svar);
                break;
            case ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID:
                handleArbetslivsinriktadeAtgarder(utlatande, svar);
                break;
            case OVRIGT_SVAR_ID:
                handleOvrigt(utlatande, svar);
                break;
            case KONTAKT_ONSKAS_SVAR_ID:
                handleOnskarKontakt(utlatande, svar);
                break;
            default:
                if (StringUtils.isNumeric(svar.getId()) && Integer.parseInt(svar.getId()) >= TILLAGGSFRAGA_START) {
                    handleTillaggsfraga(tillaggsfragor, svar);
                }
                break;
            }
        }

        utlatande.setSjukskrivningar(sjukskrivningar);
        utlatande.setDiagnoser(diagnoser);
        utlatande.setTillaggsfragor(tillaggsfragor);
    }

    private static void handleArbetslivsinriktadeAtgarder(Builder utlatande, Svar svar) {
        List<ArbetslivsinriktadeAtgarder> arbetslivsinriktadeAtgarder = new ArrayList<>();

        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case ARBETSLIVSINRIKTADE_ATGARDER_VAL_DELSVAR_ID:
                String arbetslivsinriktadeAtgarderValKod = getSvarContent(delsvar, CVType.class).getCode();
                arbetslivsinriktadeAtgarder.add(ArbetslivsinriktadeAtgarder
                        .create(ArbetslivsinriktadeAtgarderVal.fromId(Integer.parseInt(arbetslivsinriktadeAtgarderValKod))));
                break;
            case ARBETSLIVSINRIKTADE_ATGARDER_AKTUELLT_BESKRIVNING_DELSVAR_ID:
                utlatande.setArbetslivsinriktadeAtgarderAktuelltBeskrivning(getSvarContent(delsvar, String.class));
                break;
            case ARBETSLIVSINRIKTADE_ATGARDER_EJ_AKTUELLT_BESKRIVNING_DELSVAR_ID:
                utlatande.setArbetslivsinriktadeAtgarderEjAktuelltBeskrivning(getSvarContent(delsvar, String.class));
                break;
            default:
                throw new IllegalArgumentException();
            }
        }
        utlatande.setArbetslivsinriktadeAtgarder(arbetslivsinriktadeAtgarder);
    }

    private static void handlePrognos(Builder utlatande, Svar svar) {
        String prognosKod = null;
        String fortydligande = null;
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case PROGNOS_BESKRIVNING_DELSVAR_ID:
                prognosKod = getSvarContent(delsvar, CVType.class).getCode();
                break;
            case PROGNOS_FORTYDLIGANDE_DELSVAR_ID:
                fortydligande = getSvarContent(delsvar, String.class);
                break;
            default:
                throw new IllegalArgumentException();
            }
            if (prognosKod != null && fortydligande != null) {
                utlatande.setPrognos(Prognos.create(PrognosTyp.fromId(Integer.parseInt(prognosKod)), fortydligande));
            }
        }
    }

    private static void handleFormagaTrotsBegransning(Builder utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case AKTIVITETSFORMAGA_DELSVAR_ID:
                utlatande.setFormagaTrotsBegransning(getSvarContent(delsvar, String.class));
                break;
            default:
                throw new IllegalArgumentException();
            }
        }
    }

    private static void handleArbetsresor(Builder utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case ARBETSRESOR_OM_DELSVAR_ID:
                utlatande.setArbetsresor(Boolean.valueOf(getSvarContent(delsvar, String.class)));
                break;
            default:
                throw new IllegalArgumentException();
            }
        }
    }

    private static void handleArbetstidsforlaggning(Builder utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case ARBETSTIDSFORLAGGNING_OM_DELSVAR_ID:
                utlatande.setArbetstidsforlaggning(Boolean.valueOf(getSvarContent(delsvar, String.class)));
                break;
            case ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_ID:
                utlatande.setArbetstidsforlaggningMotivering(getSvarContent(delsvar, String.class));
                break;
            default:
                throw new IllegalArgumentException();
            }
        }
    }

    private static void handleForsakringsmedicinsktBeslutsstod(Builder utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case FORSAKRINGSMEDICINSKT_BESLUTSSTOD_DELSVAR_ID:
                utlatande.setForsakringsmedicinsktBeslutsstod(getSvarContent(delsvar, String.class));
                break;
            default:
                throw new IllegalArgumentException();
            }
        }
    }

    private static void handleBehovAvSjukskrivning(List<Sjukskrivning> sjukskrivningar, Svar svar) {
        String sjukskrivningsnivaString = null;
        InternalLocalDateInterval period = null;
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case BEHOV_AV_SJUKSKRIVNING_NIVA_DELSVARSVAR_ID:
                sjukskrivningsnivaString = getSvarContent(delsvar, CVType.class).getCode();
                break;
            case BEHOV_AV_SJUKSKRIVNING_PERIOD_DELSVARSVAR_ID:
                DatePeriodType datePeriod = getSvarContent(delsvar, DatePeriodType.class);
                period = new InternalLocalDateInterval(datePeriod.getStart().toString(), datePeriod.getEnd().toString());
                break;
            default:
                throw new IllegalArgumentException();
            }
            if (sjukskrivningsnivaString != null && period != null) {
                sjukskrivningar.add(Sjukskrivning.create(SjukskrivningsGrad.fromId(Integer.parseInt(sjukskrivningsnivaString)), period));
            }
        }
    }

    private static void handleFunktionsnedsattning(Builder utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case FUNKTIONSNEDSATTNING_DELSVAR_ID:
                utlatande.setFunktionsnedsattning(getSvarContent(delsvar, String.class));
                break;
            default:
                throw new IllegalArgumentException();
            }
        }

    }

    private static void handleArbetsmarknadspolitisktProgram(Builder utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case ARBETSMARKNADSPOLITISKT_PROGRAM_DELSVAR_ID:
                utlatande.setArbetsmarknadspolitisktProgram(getSvarContent(delsvar, String.class));
                break;
            default:
                throw new IllegalArgumentException();
            }
        }
    }

    private static void handleNuvarandeArbete(Builder utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case NUVARANDE_ARBETE_DELSVAR_ID:
                utlatande.setNuvarandeArbete(getSvarContent(delsvar, String.class));
                break;
            default:
                throw new IllegalArgumentException();
            }
        }
    }

    private static void handleSysselsattning(Builder utlatande, Svar svar) {
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case TYP_AV_SYSSELSATTNING_DELSVAR_ID:
                String sysselsattningsTypString = getSvarContent(delsvar, CVType.class).getCode();
                utlatande.setSysselsattning(Sysselsattning.create(SysselsattningsTyp.fromId(Integer.parseInt(sysselsattningsTypString))));
                break;
            default:
                throw new IllegalArgumentException();
            }
        }
    }

    private static void handleGrundForMedicinsktUnderlag(Builder utlatande, Svar svar) {
        InternalDate grundForMedicinsktUnderlagDatum = null;
        ReferensTyp grundForMedicinsktUnderlagTyp = ReferensTyp.UNKNOWN;
        for (Delsvar delsvar : svar.getDelsvar()) {
            switch (delsvar.getId()) {
            case GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID:
                grundForMedicinsktUnderlagDatum = new InternalDate(getSvarContent(delsvar, String.class));
                break;
            case GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID:
                String referensTypString = getSvarContent(delsvar, CVType.class).getCode();
                grundForMedicinsktUnderlagTyp = ReferensTyp.byTransport(referensTypString);
                break;
            case GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_ID:
                utlatande.setAnnatGrundForMUBeskrivning(getSvarContent(delsvar, String.class));
                break;
            default:
                throw new IllegalArgumentException();
            }
        }

        switch (grundForMedicinsktUnderlagTyp) {
        case UNDERSOKNING:
            utlatande.setUndersokningAvPatienten(grundForMedicinsktUnderlagDatum);
            break;
        case JOURNAL:
            utlatande.setJournaluppgifter(grundForMedicinsktUnderlagDatum);
            break;
        case ANHORIGSBESKRIVNING:
            utlatande.setAnhorigsBeskrivningAvPatienten(grundForMedicinsktUnderlagDatum);
            break;
        case TELEFONKONTAKT:
            utlatande.setTelefonkontaktMedPatienten(grundForMedicinsktUnderlagDatum);
            break;
        case ANNAT:
            utlatande.setAnnatGrundForMU(grundForMedicinsktUnderlagDatum);
            break;
        default:
            throw new IllegalArgumentException();
        }
    }

    private static void handleDiagnos(List<Diagnos> diagnoser, Svar svar) {
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
        Diagnoskodverk diagnoskodverk = Diagnoskodverk.getEnumByCodeSystem(diagnosKodSystem);
        diagnoser.add(Diagnos.create(diagnosKod, diagnoskodverk.toString(), diagnosBeskrivning));
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

    private static void handleOvrigt(Builder utlatande, Svar svar) {
        Delsvar delsvar = svar.getDelsvar().get(0);
        switch (delsvar.getId()) {
        case OVRIGT_DELSVAR_ID:
            utlatande.setOvrigt(getSvarContent(delsvar, String.class));
            break;
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

    private static void handleTillaggsfraga(List<Tillaggsfraga> tillaggsFragor, Svar svar) {
        // En tilläggsfråga har endast ett delsvar
        if (svar.getDelsvar().size() > 1) {
            throw new IllegalArgumentException();
        }

        Delsvar delsvar = svar.getDelsvar().get(0);
        // Kontrollera att ID matchar
        if (delsvar.getId().equals(svar.getId() + ".1")) {
            tillaggsFragor.add(Tillaggsfraga.create(svar.getId(), getSvarContent(delsvar, String.class)));
        } else {
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

}