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

import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.*;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;

import se.inera.certificate.modules.fkparent.model.internal.Diagnos;
import se.inera.certificate.modules.sjukpenning_utokad.model.internal.*;
import se.inera.certificate.modules.sjukpenning_utokad.support.SjukpenningUtokadEntryPoint;
import se.inera.intyg.common.support.common.enumerations.Diagnoskodverk;
import se.inera.intyg.common.support.model.common.internal.HoSPersonal;
import se.inera.intyg.common.support.model.common.internal.Vardenhet;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.*;
import se.riv.clinicalprocess.healthcond.certificate.v2.*;
import se.riv.clinicalprocess.healthcond.certificate.v2.Svar.Delsvar;

public final class UtlatandeToIntyg {

    private UtlatandeToIntyg() {
    }

    static Intyg convert(SjukpenningUtokadUtlatande source) {
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

    private static String getTextVersion(SjukpenningUtokadUtlatande source) {
        return source.getTextVersion();
    }

    private static HosPersonal getSkapadAv(SjukpenningUtokadUtlatande source) {
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

    private static IntygId getIntygsId(SjukpenningUtokadUtlatande source) {
        IntygId intygId = new IntygId();
        intygId.setRoot(source.getGrundData().getSkapadAv().getVardenhet().getEnhetsid());
        intygId.setExtension(source.getId());
        return intygId;
    }

    private static TypAvIntyg getTypAvIntyg(SjukpenningUtokadUtlatande source) {
        TypAvIntyg typAvIntyg = new TypAvIntyg();
        typAvIntyg.setCode(source.getTyp().toUpperCase());
        typAvIntyg.setCodeSystem(CERTIFICATE_CODE_SYSTEM);
        typAvIntyg.setDisplayName(SjukpenningUtokadEntryPoint.MODULE_NAME);
        return typAvIntyg;
    }

    private static void decorateWithRelation(Intyg intyg, SjukpenningUtokadUtlatande source) {
        if (source.getGrundData().getRelation() == null) {
            return;
        }
        Relation relation = new Relation();

        IntygId intygId = new IntygId();
        intygId.setRoot(source.getGrundData().getSkapadAv().getVardenhet().getEnhetsid());
        intygId.setExtension(source.getGrundData().getRelation().getRelationIntygsId());

        TypAvRelation typAvRelation = new TypAvRelation();
        typAvRelation.setCode(source.getGrundData().getRelation().getRelationKod().value());
        typAvRelation.setCodeSystem(RELATION_CODE_SYSTEM);

        relation.setIntygsId(intygId);
        relation.setTyp(typAvRelation);

        intyg.getRelation().add(relation);
    }

    private static List<Svar> getSvar(SjukpenningUtokadUtlatande source) {
        List<Svar> svars = new ArrayList<>();

        if (source.getUndersokningAvPatienten() != null && source.getUndersokningAvPatienten().isValidDate()) {
            svars.add(aSvar(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID).withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID,
                    aCV(GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM, Integer.toString(UNDERSOKNING_AV_PATIENT)))
                    .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID, source.getUndersokningAvPatienten().asLocalDate().toString()).build());
        }

        if (source.getTelefonkontaktMedPatienten() != null && source.getTelefonkontaktMedPatienten().isValidDate()) {
            svars.add(aSvar(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID).withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID,
                    aCV(GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM, Integer.toString(TELEFONKONTAKT_MED_PATIENT)))
                    .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID, source.getTelefonkontaktMedPatienten().asLocalDate().toString())
                    .build());
        }

        if (source.getJournaluppgifter() != null && source.getJournaluppgifter().isValidDate()) {
            svars.add(aSvar(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID).withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID,
                    aCV(GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM, Integer.toString(JOURNALUPPGIFTER)))
                    .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID, source.getJournaluppgifter().asLocalDate().toString()).build());
        }

        if (source.getAnnatGrundForMU() != null && source.getAnnatGrundForMU().isValidDate()) {
            svars.add(aSvar(GRUNDFORMEDICINSKTUNDERLAG_SVAR_ID).withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_ID,
                    aCV(GRUNDFORMEDICINSKTUNDERLAG_CODE_SYSTEM, Integer.toString(ANNAT)))
                    .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_ID, source.getAnnatGrundForMU().asLocalDate().toString())
                    .withDelsvar(GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_ID, source.getAnnatGrundForMUBeskrivning()).build());
        }

        if (source.getSysselsattning() != null) {
            svars.add(aSvar(TYP_AV_SYSSELSATTNING_SVAR_ID).withDelsvar(TYP_AV_SYSSELSATTNING_DELSVAR_ID,
                    aCV(TYP_AV_SYSSELSATTNING_CODE_SYSTEM, Integer.toString(source.getSysselsattning().getTyp().getId()))).build());
        }

        addIfNotBlank(svars, NUVARANDE_ARBETE_SVAR_ID, NUVARANDE_ARBETE_DELSVAR_ID, source.getNuvarandeArbete());

        for (Diagnos diagnos : source.getDiagnoser()) {
            Diagnoskodverk diagnoskodverk = Diagnoskodverk.valueOf(diagnos.getDiagnosKodSystem());
            svars.add(aSvar(DIAGNOS_SVAR_ID).withDelsvar(DIAGNOS_DELSVAR_ID, aCV(diagnoskodverk.getCodeSystem(), diagnos.getDiagnosKod()))
                    .withDelsvar(DIAGNOS_BESKRIVNING_DELSVAR_ID, diagnos.getDiagnosBeskrivning()).build());
        }

        addIfNotBlank(svars, FUNKTIONSNEDSATTNING_SVAR_ID, FUNKTIONSNEDSATTNING_DELSVAR_ID, source.getFunktionsnedsattning());
        addIfNotBlank(svars, AKTIVITETSBEGRANSNING_SVAR_ID, AKTIVITETSBEGRANSNING_DELSVAR_ID, source.getAktivitetsbegransning());
        addIfNotBlank(svars, PAGAENDEBEHANDLING_SVAR_ID, PAGAENDEBEHANDLING_DELSVAR_ID, source.getPagaendeBehandling());
        addIfNotBlank(svars, PLANERADBEHANDLING_SVAR_ID, PLANERADBEHANDLING_DELSVAR_ID, source.getPlaneradBehandling());

        for (Sjukskrivning sjukskrivning : source.getSjukskrivningar()) {
            svars.add(aSvar(BEHOV_AV_SJUKSKRIVNING_SVAR_ID).withDelsvar(BEHOV_AV_SJUKSKRIVNING_NIVA_DELSVARSVAR_ID,
                    aCV(SJUKSKRIVNING_CODE_SYSTEM, Integer.toString(sjukskrivning.getSjukskrivningsgrad().getId())))
                    .withDelsvar(BEHOV_AV_SJUKSKRIVNING_PERIOD_DELSVARSVAR_ID,
                            aDatePeriod(sjukskrivning.getPeriod().fromAsLocalDate(), sjukskrivning.getPeriod().tomAsLocalDate())).build());
        }

        addIfNotBlank(svars, FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_ID, FORSAKRINGSMEDICINSKT_BESLUTSSTOD_DELSVAR_ID, source.getForsakringsmedicinsktBeslutsstod());

        if (source.getArbetstidsforlaggning() != null) {
            if (source.getArbetstidsforlaggning() && !StringUtils.isBlank(source.getArbetstidsforlaggningMotivering())) {
                svars.add(aSvar(ARBETSTIDSFORLAGGNING_SVAR_ID)
                        .withDelsvar(ARBETSTIDSFORLAGGNING_OM_DELSVAR_ID, source.getArbetstidsforlaggning().toString())
                        .withDelsvar(ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_ID, source.getArbetstidsforlaggningMotivering()).build());
            } else {
                svars.add(aSvar(ARBETSTIDSFORLAGGNING_SVAR_ID).withDelsvar(ARBETSTIDSFORLAGGNING_OM_DELSVAR_ID,
                        source.getArbetstidsforlaggning().toString()).build());
            }
        }

        if (source.getArbetsresor() != null) {
            svars.add(aSvar(ARBETSRESOR_SVAR_ID).withDelsvar(ARBETSRESOR_OM_DELSVAR_ID, source.getArbetsresor().toString()).build());
        }

        addIfNotBlank(svars, AKTIVITETSFORMAGA_SVAR_ID, AKTIVITETSFORMAGA_DELSVAR_ID, source.getFormagaTrotsBegransning());

        if (source.getPrognos() != null) {
            if (!StringUtils.isBlank(source.getPrognos().getFortydligande())) {
                svars.add(aSvar(PROGNOS_SVAR_ID).withDelsvar(PROGNOS_BESKRIVNING_DELSVAR_ID,
                        aCV(PROGNOS_CODE_SYSTEM, Integer.toString(source.getPrognos().getTyp().getId())))
                        .withDelsvar(PROGNOS_FORTYDLIGANDE_DELSVAR_ID, source.getPrognos().getFortydligande()).build());
            } else {
                svars.add(aSvar(PROGNOS_SVAR_ID).withDelsvar(PROGNOS_BESKRIVNING_DELSVAR_ID,
                        aCV(PROGNOS_CODE_SYSTEM, Integer.toString(source.getPrognos().getTyp().getId()))).build());
            }
        }

        /* Build complex object */
        SvarBuilder arbetslivsinriktadeAtgarderBuilder = aSvar(ARBETSLIVSINRIKTADE_ATGARDER_SVAR_ID);
        source.getArbetslivsinriktadeAtgarder().stream()
                .forEach((ArbetslivsinriktadeAtgarder atgarder) -> {
                    arbetslivsinriktadeAtgarderBuilder.withDelsvar(ARBETSLIVSINRIKTADE_ATGARDER_VAL_DELSVAR_ID,
                            aCV(ARBETSLIVSINRIKTADE_ATGARDER_CODE_SYSTEM, Integer.toString(atgarder.getVal().getId())));
                });

        if (!StringUtils.isBlank(source.getArbetslivsinriktadeAtgarderAktuelltBeskrivning())) {
            arbetslivsinriktadeAtgarderBuilder.withDelsvar(ARBETSLIVSINRIKTADE_ATGARDER_AKTUELLT_BESKRIVNING_DELSVAR_ID,
                    source.getArbetslivsinriktadeAtgarderAktuelltBeskrivning());
        }

        if (!StringUtils.isBlank(source.getArbetslivsinriktadeAtgarderEjAktuelltBeskrivning())) {
            arbetslivsinriktadeAtgarderBuilder.withDelsvar(ARBETSLIVSINRIKTADE_ATGARDER_EJ_AKTUELLT_BESKRIVNING_DELSVAR_ID,
                    source.getArbetslivsinriktadeAtgarderEjAktuelltBeskrivning());
        }
        if (CollectionUtils.isNotEmpty(arbetslivsinriktadeAtgarderBuilder.delSvars)) {
            svars.add(arbetslivsinriktadeAtgarderBuilder.build());
        }
        /* End complex object */

        addIfNotBlank(svars, OVRIGT_SVAR_ID, OVRIGT_DELSVAR_ID, source.getOvrigt());

        if (source.getKontaktMedFk() != null) {
            if (source.getKontaktMedFk() && !StringUtils.isBlank(source.getAnledningTillKontakt())) {
                svars.add(aSvar(KONTAKT_ONSKAS_SVAR_ID).withDelsvar(KONTAKT_ONSKAS_DELSVAR_ID, source.getKontaktMedFk().toString())
                        .withDelsvar(ANLEDNING_TILL_KONTAKT_DELSVAR_ID, source.getAnledningTillKontakt()).build());
            } else {
                svars.add(aSvar(KONTAKT_ONSKAS_SVAR_ID).withDelsvar(KONTAKT_ONSKAS_DELSVAR_ID, source.getKontaktMedFk().toString()).build());
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

    private static JAXBElement<CVType> aCV(String codeSystem, String code) {
        CVType cv = new CVType();
        cv.setCodeSystem(codeSystem);
        cv.setCode(code);
        return new JAXBElement<>(new QName("urn:riv:clinicalprocess:healthcond:certificate:types:2", "cv"), CVType.class, null, cv);
    }

    private static JAXBElement<DatePeriodType> aDatePeriod(LocalDate from, LocalDate tom) {
        DatePeriodType period = new DatePeriodType();
        period.setStart(from);
        period.setEnd(tom);
        return new JAXBElement<>(new QName("urn:riv:clinicalprocess:healthcond:certificate:types:2", "datePeriod"), DatePeriodType.class, null,
                period);
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
