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

package se.inera.certificate.modules.aktivitetsersattning_na.model.converter;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.AKTIVITETSBEGRANSNING_DELSVAR_ID_17;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.AKTIVITETSBEGRANSNING_SVAR_ID_17;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.AKTIVITETSFORMAGA_DELSVAR_ID_23;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.AKTIVITETSFORMAGA_SVAR_ID_23;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.ANHORIGSBESKRIVNING;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.ANLEDNING_TILL_KONTAKT_DELSVAR_ID_26;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.ANNAT;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.ARBETSPLATSKOD_CODE_SYSTEM;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.AVSLUTADBEHANDLING_DELSVAR_ID_18;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.AVSLUTADBEHANDLING_SVAR_ID_18;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.BEFATTNING_CODE_SYSTEM;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.CERTIFICATE_CODE_SYSTEM;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.DIAGNOSGRUND_DELSVAR_ID_7;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.DIAGNOSGRUND_NYBEDOMNING_DELSVAR_ID_7;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.DIAGNOSGRUND_SVAR_ID_7;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.DIAGNOS_BESKRIVNING_DELSVAR_ID_6;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.DIAGNOS_DELSVAR_ID_6;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.DIAGNOS_SVAR_ID_6;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.FORSLAG_TILL_ATGARD_DELSVAR_ID_24;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.FORSLAG_TILL_ATGARD_SVAR_ID_24;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_ANNAN_DELSVAR_ID_14;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_ANNAN_SVAR_ID_14;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_BALANSKOORDINATION_DELSVAR_ID_13;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_BALANSKOORDINATION_SVAR_ID_13;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_INTELLEKTUELL_DELSVAR_ID_8;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_INTELLEKTUELL_SVAR_ID_8;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_KOMMUNIKATION_DELSVAR_ID_9;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_KOMMUNIKATION_SVAR_ID_9;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_KONCENTRATION_DELSVAR_ID_10;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_KONCENTRATION_SVAR_ID_10;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_PSYKISK_DELSVAR_ID_11;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_PSYKISK_SVAR_ID_11;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SYNHORSELTAL_DELSVAR_ID_12;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.FUNKTIONSNEDSATTNING_SYNHORSELTAL_SVAR_ID_12;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_ID_1;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.HSA_CODE_SYSTEM;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.JOURNALUPPGIFTER;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.KANNEDOM_DELSVAR_ID_2;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.KANNEDOM_SVAR_ID_2;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.KONTAKT_ONSKAS_DELSVAR_ID_26;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.KONTAKT_ONSKAS_SVAR_ID_26;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.MEDICINSKAFORUTSATTNINGARFORARBETE_DELSVAR_ID_22;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_ID_22;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.OVRIGT_DELSVAR_ID_25;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.OVRIGT_SVAR_ID_25;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.PAGAENDEBEHANDLING_DELSVAR_ID_19;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.PAGAENDEBEHANDLING_SVAR_ID_19;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.PERSON_ID_CODE_SYSTEM;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.PLANERADBEHANDLING_DELSVAR_ID_20;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.PLANERADBEHANDLING_SVAR_ID_20;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.RELATION_CODE_SYSTEM;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.SJUKDOMSFORLOPP_DELSVAR_ID_5;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.SJUKDOMSFORLOPP_SVAR_ID_5;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.SUBSTANSINTAG_DELSVAR_ID_21;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.SUBSTANSINTAG_SVAR_ID_21;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.UNDERLAGFINNS_DELSVAR_ID_3;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.UNDERLAGFINNS_SVAR_ID_3;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.UNDERLAG_CODE_SYSTEM;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.UNDERLAG_DATUM_DELSVAR_ID_4;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.UNDERLAG_HAMTAS_FRAN_DELSVAR_ID_4;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.UNDERLAG_SVAR_ID_4;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.UNDERLAG_TYP_DELSVAR_ID_4;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.UNDERSOKNING_AV_PATIENT;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.apache.commons.lang3.StringUtils;

import se.inera.certificate.modules.aktivitetsersattning_na.model.internal.AktivitetsersattningNAUtlatande;
import se.inera.certificate.modules.aktivitetsersattning_na.model.internal.Tillaggsfraga;
import se.inera.certificate.modules.aktivitetsersattning_na.model.internal.Underlag;
import se.inera.certificate.modules.fkparent.model.converter.RespConstants;
import se.inera.certificate.modules.fkparent.model.internal.Diagnos;
import se.inera.intyg.common.support.common.enumerations.BefattningKod;
import se.inera.intyg.common.support.common.enumerations.Diagnoskodverk;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.ArbetsplatsKod;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.Befattning;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.CVType;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.HsaId;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.IntygId;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.PersonId;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.Specialistkompetens;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.TypAvIntyg;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.TypAvRelation;
import se.riv.clinicalprocess.healthcond.certificate.v2.Enhet;
import se.riv.clinicalprocess.healthcond.certificate.v2.HosPersonal;
import se.riv.clinicalprocess.healthcond.certificate.v2.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v2.Patient;
import se.riv.clinicalprocess.healthcond.certificate.v2.Relation;
import se.riv.clinicalprocess.healthcond.certificate.v2.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v2.Svar.Delsvar;
import se.riv.clinicalprocess.healthcond.certificate.v2.Vardgivare;

public final class UtlatandeToIntyg {

    private static final String CERTIFICATE_DISPLAY_NAME = "Läkarutlåtande för aktivitetsersättning nedsatt arbetsförmåga";

    private UtlatandeToIntyg() {
    }

    static Intyg convert(AktivitetsersattningNAUtlatande source) {
        Intyg intyg = new Intyg();
        intyg.setTyp(getTypAvIntyg(source));
        intyg.setIntygsId(getIntygsId(source));
        intyg.setVersion(getTextVersion(source));
        intyg.setSigneringstidpunkt(source.getGrundData().getSigneringsdatum());
        intyg.setSkickatTidpunkt(source.getGrundData().getSigneringsdatum());
        intyg.setSkapadAv(getSkapadAv(source));
        intyg.setPatient(getPatient(source.getGrundData().getPatient()));
        decorateWithRelation(intyg, source);
        intyg.getSvar().addAll(getSvar(source));
        return intyg;
    }

    private static HosPersonal getSkapadAv(AktivitetsersattningNAUtlatande source) {
        HoSPersonal sourceSkapadAv = source.getGrundData().getSkapadAv();
        HosPersonal skapadAv = new HosPersonal();
        skapadAv.setPersonalId(anHsaId(sourceSkapadAv.getPersonId()));
        skapadAv.setFullstandigtNamn(sourceSkapadAv.getFullstandigtNamn());
        skapadAv.setForskrivarkod(sourceSkapadAv.getForskrivarKod());
        skapadAv.setEnhet(getEnhet(sourceSkapadAv.getVardenhet()));
        for (String sourceBefattning : sourceSkapadAv.getBefattningar()) {
            Befattning befattning = new Befattning();
            befattning.setCodeSystem(BEFATTNING_CODE_SYSTEM);
            befattning.setCode(sourceBefattning);
            befattning.setDisplayName(BefattningKod.getDisplayNameFromCode(sourceBefattning));
            skapadAv.getBefattning().add(befattning);
        }
        for (String sourceKompetens : sourceSkapadAv.getSpecialiteter()) {
            Specialistkompetens kompetens = new Specialistkompetens();
            kompetens.setCode(sourceKompetens);
            skapadAv.getSpecialistkompetens().add(kompetens);
        }
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
        vardenhet.setEpost(sourceVardenhet.getEpost());
        vardenhet.setVardgivare(getVardgivare(sourceVardenhet.getVardgivare()));
        vardenhet.setArbetsplatskod(getArbetsplatsKod(sourceVardenhet.getArbetsplatsKod()));
        return vardenhet;
    }

    private static ArbetsplatsKod getArbetsplatsKod(String sourceArbetsplatsKod) {
        ArbetsplatsKod arbetsplatsKod = new ArbetsplatsKod();
        arbetsplatsKod.setRoot(ARBETSPLATSKOD_CODE_SYSTEM);
        arbetsplatsKod.setExtension(sourceArbetsplatsKod);
        return arbetsplatsKod;
    }

    private static Vardgivare getVardgivare(se.inera.intyg.common.support.model.common.internal.Vardgivare sourceVardgivare) {
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivareId(anHsaId(sourceVardgivare.getVardgivarid()));
        vardgivare.setVardgivarnamn(sourceVardgivare.getVardgivarnamn());
        return vardgivare;
    }

    private static Patient getPatient(se.inera.intyg.common.support.model.common.internal.Patient sourcePatient) {
        Patient patient = new se.riv.clinicalprocess.healthcond.certificate.v2.Patient();
        patient.setEfternamn(sourcePatient.getEfternamn());
        patient.setFornamn(sourcePatient.getFornamn());
        patient.setMellannamn(sourcePatient.getMellannamn());
        PersonId personId = new PersonId();
        personId.setRoot(PERSON_ID_CODE_SYSTEM);
        personId.setExtension(sourcePatient.getPersonId().getPersonnummer().replaceAll("-", ""));
        patient.setPersonId(personId);
        patient.setPostadress(sourcePatient.getPostadress());
        patient.setPostnummer(sourcePatient.getPostnummer());
        patient.setPostort(sourcePatient.getPostort());
        return patient;
    }

    private static IntygId getIntygsId(AktivitetsersattningNAUtlatande source) {
        IntygId intygId = new IntygId();
        intygId.setRoot(source.getGrundData().getSkapadAv().getVardenhet().getEnhetsid());
        intygId.setExtension(source.getId());
        return intygId;
    }

    private static TypAvIntyg getTypAvIntyg(AktivitetsersattningNAUtlatande source) {
        TypAvIntyg typAvIntyg = new TypAvIntyg();
        typAvIntyg.setCode(source.getTyp().toUpperCase());
        typAvIntyg.setCodeSystem(CERTIFICATE_CODE_SYSTEM);
        typAvIntyg.setDisplayName(CERTIFICATE_DISPLAY_NAME);
        return typAvIntyg;
    }

    private static String getTextVersion(AktivitetsersattningNAUtlatande source) {
        return source.getTextVersion();
    }

    private static void decorateWithRelation(Intyg intyg, AktivitetsersattningNAUtlatande source) {
        if (source.getGrundData().getRelation() == null || source.getGrundData().getRelation().getRelationKod() == null) {
            return;
        }
        Relation relation = new Relation();

        IntygId intygId = new IntygId();
        intygId.setRoot(source.getGrundData().getSkapadAv().getVardenhet().getEnhetsid());
        intygId.setExtension(source.getGrundData().getRelation().getRelationIntygsId());

        TypAvRelation typAvRelation = new TypAvRelation();
        typAvRelation.setCode(source.getGrundData().getRelation().getRelationKod().value());
        typAvRelation.setCodeSystem(RELATION_CODE_SYSTEM);
        typAvRelation.setDisplayName(source.getGrundData().getRelation().getRelationKod().getKlartext());

        relation.setIntygsId(intygId);
        relation.setTyp(typAvRelation);

        intyg.getRelation().add(relation);
    }

    private static List<Svar> getSvar(AktivitetsersattningNAUtlatande source) {
        List<Svar> svars = new ArrayList<>();

        if (source.getUndersokningAvPatienten() != null) {
            svars.add(aSvar(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1)
                    .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1,
                            aCV(GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM, Integer.toString(UNDERSOKNING_AV_PATIENT),
                                    RespConstants.getDisplayName(GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM, Integer.toString(UNDERSOKNING_AV_PATIENT))))
                    .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1, source.getUndersokningAvPatienten().asLocalDate().toString())
                    .build());
        }
        if (source.getJournaluppgifter() != null) {
            svars.add(aSvar(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1)
                    .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1,
                            aCV(GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM, Integer.toString(JOURNALUPPGIFTER),
                                    RespConstants.getDisplayName(GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM, Integer.toString(JOURNALUPPGIFTER))))
                    .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1, source.getJournaluppgifter().asLocalDate().toString()).build());
        }
        if (source.getAnhorigsBeskrivningAvPatienten() != null) {
            svars.add(aSvar(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1)
                    .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1,
                            aCV(GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM, Integer.toString(ANHORIGSBESKRIVNING),
                                    RespConstants.getDisplayName(GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM, Integer.toString(ANHORIGSBESKRIVNING))))
                    .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1, source.getAnhorigsBeskrivningAvPatienten().asLocalDate().toString())
                    .build());
        }
        if (source.getAnnatGrundForMU() != null) {
            svars.add(aSvar(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID_1)
                    .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID_1,
                            aCV(GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM, Integer.toString(ANNAT),
                                    RespConstants.getDisplayName(GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM, Integer.toString(ANNAT))))
                    .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID_1, source.getAnnatGrundForMU().asLocalDate().toString())
                    .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_ID_1, source.getAnnatGrundForMUBeskrivning()).build());
        }

        if (source.getKannedomOmPatient() != null) {
            svars.add(aSvar(KANNEDOM_SVAR_ID_2).withDelsvar(KANNEDOM_DELSVAR_ID_2, source.getKannedomOmPatient().asLocalDate().toString()).build());
        }

        if (source.getUnderlagFinns() != null) {
            svars.add(aSvar(UNDERLAGFINNS_SVAR_ID_3).withDelsvar(UNDERLAGFINNS_DELSVAR_ID_3, source.getUnderlagFinns().toString()).build());
        }

        for (Underlag underlag : source.getUnderlag()) {
            svars.add(
                    aSvar(UNDERLAG_SVAR_ID_4).withDelsvar(UNDERLAG_TYP_DELSVAR_ID_4,
                            aCV(UNDERLAG_CODE_SYSTEM, Integer.toString(underlag.getTyp().getId()),
                                    RespConstants.getDisplayName(UNDERLAG_CODE_SYSTEM, Integer.toString(underlag.getTyp().getId()))))
                            .withDelsvar(UNDERLAG_DATUM_DELSVAR_ID_4,
                                    underlag.getDatum() != null ? underlag.getDatum().asLocalDate().toString() : null)
                            .withDelsvar(UNDERLAG_HAMTAS_FRAN_DELSVAR_ID_4, underlag.getHamtasFran()).build());
        }

        addIfNotBlank(svars, SJUKDOMSFORLOPP_SVAR_ID_5, SJUKDOMSFORLOPP_DELSVAR_ID_5, source.getSjukdomsforlopp());

        for (Diagnos diagnos : source.getDiagnoser()) {
            Diagnoskodverk diagnoskodverk = Diagnoskodverk.valueOf(diagnos.getDiagnosKodSystem());
            svars.add(aSvar(DIAGNOS_SVAR_ID_6)
                    .withDelsvar(DIAGNOS_DELSVAR_ID_6, aCV(diagnoskodverk.getCodeSystem(), diagnos.getDiagnosKod(), diagnos.getDiagnosDisplayName()))
                    .withDelsvar(DIAGNOS_BESKRIVNING_DELSVAR_ID_6, diagnos.getDiagnosBeskrivning()).build());
        }

        if (source.getNyBedomningDiagnosgrund() != null) {
            svars.add(aSvar(DIAGNOSGRUND_SVAR_ID_7).withDelsvar(DIAGNOSGRUND_DELSVAR_ID_7, source.getDiagnosgrund())
                    .withDelsvar(DIAGNOSGRUND_NYBEDOMNING_DELSVAR_ID_7, source.getNyBedomningDiagnosgrund().toString()).build());
        }

        addIfNotBlank(svars, FUNKTIONSNEDSATTNING_INTELLEKTUELL_SVAR_ID_8, FUNKTIONSNEDSATTNING_INTELLEKTUELL_DELSVAR_ID_8,
                source.getFunktionsnedsattningIntellektuell());
        addIfNotBlank(svars, FUNKTIONSNEDSATTNING_KOMMUNIKATION_SVAR_ID_9, FUNKTIONSNEDSATTNING_KOMMUNIKATION_DELSVAR_ID_9,
                source.getFunktionsnedsattningKommunikation());
        addIfNotBlank(svars, FUNKTIONSNEDSATTNING_KONCENTRATION_SVAR_ID_10, FUNKTIONSNEDSATTNING_KONCENTRATION_DELSVAR_ID_10,
                source.getFunktionsnedsattningKoncentration());
        addIfNotBlank(svars, FUNKTIONSNEDSATTNING_PSYKISK_SVAR_ID_11, FUNKTIONSNEDSATTNING_PSYKISK_DELSVAR_ID_11,
                source.getFunktionsnedsattningPsykisk());
        addIfNotBlank(svars, FUNKTIONSNEDSATTNING_SYNHORSELTAL_SVAR_ID_12, FUNKTIONSNEDSATTNING_SYNHORSELTAL_DELSVAR_ID_12,
                source.getFunktionsnedsattningSynHorselTal());
        addIfNotBlank(svars, FUNKTIONSNEDSATTNING_BALANSKOORDINATION_SVAR_ID_13, FUNKTIONSNEDSATTNING_BALANSKOORDINATION_DELSVAR_ID_13,
                source.getFunktionsnedsattningBalansKoordination());
        addIfNotBlank(svars, FUNKTIONSNEDSATTNING_ANNAN_SVAR_ID_14, FUNKTIONSNEDSATTNING_ANNAN_DELSVAR_ID_14, source.getFunktionsnedsattningAnnan());
        addIfNotBlank(svars, AKTIVITETSBEGRANSNING_SVAR_ID_17, AKTIVITETSBEGRANSNING_DELSVAR_ID_17, source.getAktivitetsbegransning());
        addIfNotBlank(svars, AVSLUTADBEHANDLING_SVAR_ID_18, AVSLUTADBEHANDLING_DELSVAR_ID_18, source.getAvslutadBehandling());
        addIfNotBlank(svars, PAGAENDEBEHANDLING_SVAR_ID_19, PAGAENDEBEHANDLING_DELSVAR_ID_19, source.getPagaendeBehandling());
        addIfNotBlank(svars, PLANERADBEHANDLING_SVAR_ID_20, PLANERADBEHANDLING_DELSVAR_ID_20, source.getPlaneradBehandling());
        addIfNotBlank(svars, SUBSTANSINTAG_SVAR_ID_21, SUBSTANSINTAG_DELSVAR_ID_21, source.getSubstansintag());
        addIfNotBlank(svars, MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_ID_22, MEDICINSKAFORUTSATTNINGARFORARBETE_DELSVAR_ID_22,
                source.getMedicinskaForutsattningarForArbete());
        addIfNotBlank(svars, FORSLAG_TILL_ATGARD_SVAR_ID_24, FORSLAG_TILL_ATGARD_DELSVAR_ID_24, source.getForslagTillAtgard());
        addIfNotBlank(svars, AKTIVITETSFORMAGA_SVAR_ID_23, AKTIVITETSFORMAGA_DELSVAR_ID_23, source.getFormagaTrotsBegransning());
        addIfNotBlank(svars, OVRIGT_SVAR_ID_25, OVRIGT_DELSVAR_ID_25, source.getOvrigt());

        if (source.getKontaktMedFk() != null) {
            if (source.getKontaktMedFk() && !StringUtils.isBlank(source.getAnledningTillKontakt())) {
                svars.add(aSvar(KONTAKT_ONSKAS_SVAR_ID_26).withDelsvar(KONTAKT_ONSKAS_DELSVAR_ID_26, source.getKontaktMedFk().toString())
                        .withDelsvar(ANLEDNING_TILL_KONTAKT_DELSVAR_ID_26, source.getAnledningTillKontakt()).build());
            } else {
                svars.add(aSvar(KONTAKT_ONSKAS_SVAR_ID_26).withDelsvar(KONTAKT_ONSKAS_DELSVAR_ID_26, source.getKontaktMedFk().toString()).build());
            }
        }

        for (Tillaggsfraga tillaggsfraga : source.getTillaggsfragor()) {
            addIfNotBlank(svars, tillaggsfraga.getId(), tillaggsfraga.getId() + ".1", tillaggsfraga.getSvar());
        }

        return svars;
    }

    private static void addIfNotBlank(List<Svar> svars, String svarsId, String delsvarsId, String content) {
        if (!StringUtils.isBlank(content)) {
            svars.add(aSvar(svarsId).withDelsvar(delsvarsId, content).build());
        }
    }

    private static HsaId anHsaId(String id) {
        HsaId hsaId = new HsaId();
        hsaId.setRoot(HSA_CODE_SYSTEM);
        hsaId.setExtension(id);
        return hsaId;
    }

    private static JAXBElement<CVType> aCV(String codeSystem, String code, String displayName) {
        CVType cv = new CVType();
        cv.setCodeSystem(codeSystem);
        cv.setCode(code);
        cv.setDisplayName(displayName);
        return new JAXBElement<>(new QName("urn:riv:clinicalprocess:healthcond:certificate:types:2", "cv"), CVType.class, null, cv);
    }

    private static SvarBuilder aSvar(String id) {
        return new SvarBuilder(id);
    }

    private static class SvarBuilder {
        private String id;
        private List<Delsvar> delSvars = new ArrayList<>();

        SvarBuilder(String id) {
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
}
