/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Certificate Modules (http://code.google.com/p/inera-certificate-modules).
 *
 * Inera Certificate Modules is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Inera Certificate Modules is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.certificate.modules.ts_bas.model.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.certificate.model.HosPersonal;
import se.inera.certificate.model.Kod;
import se.inera.certificate.modules.ts_bas.model.codes.AktivitetKod;
import se.inera.certificate.modules.ts_bas.model.codes.CodeConverter;
import se.inera.certificate.modules.ts_bas.model.codes.LateralitetsKod;
import se.inera.certificate.modules.ts_bas.model.codes.ObservationsKod;
import se.inera.certificate.modules.ts_bas.model.codes.RekommendationVardeKod;
import se.inera.certificate.modules.ts_bas.model.codes.RekommendationsKod;
import se.inera.certificate.modules.ts_bas.model.external.Aktivitet;
import se.inera.certificate.modules.ts_bas.model.external.Observation;
import se.inera.certificate.modules.ts_bas.model.external.Rekommendation;
import se.inera.certificate.modules.ts_bas.model.internal.mi.Bedomning;
import se.inera.certificate.modules.ts_bas.model.internal.mi.BedomningKorkortstyp;
import se.inera.certificate.modules.ts_bas.model.internal.mi.Diabetes;
import se.inera.certificate.modules.ts_bas.model.internal.mi.Funktionsnedsattning;
import se.inera.certificate.modules.ts_bas.model.internal.mi.HjartKarl;
import se.inera.certificate.modules.ts_bas.model.internal.mi.HoSPersonal;
import se.inera.certificate.modules.ts_bas.model.internal.mi.HorselBalans;
import se.inera.certificate.modules.ts_bas.model.internal.mi.Kognitivt;
import se.inera.certificate.modules.ts_bas.model.internal.mi.Medicinering;
import se.inera.certificate.modules.ts_bas.model.internal.mi.Medvetandestorning;
import se.inera.certificate.modules.ts_bas.model.internal.mi.NarkotikaLakemedel;
import se.inera.certificate.modules.ts_bas.model.internal.mi.Neurologi;
import se.inera.certificate.modules.ts_bas.model.internal.mi.Njurar;
import se.inera.certificate.modules.ts_bas.model.internal.mi.Patient;
import se.inera.certificate.modules.ts_bas.model.internal.mi.Psykiskt;
import se.inera.certificate.modules.ts_bas.model.internal.mi.Sjukhusvard;
import se.inera.certificate.modules.ts_bas.model.internal.mi.SomnVakenhet;
import se.inera.certificate.modules.ts_bas.model.internal.mi.Syn;
import se.inera.certificate.modules.ts_bas.model.internal.mi.Utlatande;
import se.inera.certificate.modules.ts_bas.model.internal.mi.Utvecklingsstorning;
import se.inera.certificate.modules.ts_bas.model.internal.mi.Vardenhet;
import se.inera.certificate.modules.ts_bas.model.internal.mi.Vardgivare;
import se.inera.certificate.modules.ts_bas.rest.dto.CertificateContentHolder;

/**
 * Converter for converting the external format to the internal view format.
 * 
 * 
 * @author Erik
 * 
 */
public class ExternalToInternalConverter {

    private static final Logger LOG = LoggerFactory.getLogger(ExternalToInternalConverter.class);

    private List<Observation> observationer;

    private List<Aktivitet> aktiviteter;

    public Utlatande convert(CertificateContentHolder certificateContentHolder) throws ConverterException {
        se.inera.certificate.modules.ts_bas.model.external.Utlatande extUtlatande = certificateContentHolder
                .getCertificateContent();

        Utlatande intUtlatande = convertUtlatandeFromExternalToInternal(extUtlatande);
        LOG.trace("Converting external model to internal");
        return intUtlatande;
    }

    private Utlatande convertUtlatandeFromExternalToInternal(
            se.inera.certificate.modules.ts_bas.model.external.Utlatande extUtlatande) throws ConverterException {
        LOG.debug("Converting Utlatande '{}' from external to internal", extUtlatande.getId());

        this.observationer = extUtlatande.getObservationer();
        this.aktiviteter = extUtlatande.getAktiviteter();

        Utlatande intUtlatande = new Utlatande();

        intUtlatande.setUtlatandeid(InternalModelConverterUtils.getExtensionFromId(extUtlatande.getId()));
        intUtlatande.setTypAvUtlatande(InternalModelConverterUtils.getValueFromKod(extUtlatande.getTyp()));
        intUtlatande.setSigneringsdatum(extUtlatande.getSigneringsdatum());
        intUtlatande.setSkickatdatum(extUtlatande.getSkickatdatum());
        intUtlatande.setKommentarer(extUtlatande.getKommentarer());

        HoSPersonal intHoSPersonal = convertToIntHoSPersonal(extUtlatande.getSkapadAv());
        intUtlatande.setSkapadAv(intHoSPersonal);

        Patient intPatient = convertToIntPatient(extUtlatande.getPatient());
        intUtlatande.setPatient(intPatient);

        // Create internal model-specific objects grouping similar attributes together
        intUtlatande.setSyn(createSyn(extUtlatande));
        intUtlatande.setHorselBalans(createHorselBalans(extUtlatande));
        intUtlatande.setFunktionsnersattning(createFunktionsnedsattning(extUtlatande));
        intUtlatande.setHjartKarl(createHjartKarl(extUtlatande));
        intUtlatande.setDiabetes(createDiabetes(extUtlatande));
        intUtlatande.setNeurologi(createNeurologi(extUtlatande));
        intUtlatande.setMedvertandestorning(createMedvetandestorning(extUtlatande));
        intUtlatande.setNjurar(createNjurar(extUtlatande));
        intUtlatande.setKognitivt(createKognitivt(extUtlatande));
        intUtlatande.setSomnVakenhet(createSomnVakenhet(extUtlatande));
        intUtlatande.setNarkotikaLakemedel(createNarkotikaLakemedel(extUtlatande));
        intUtlatande.setPsykiskt(createPsykiskt(extUtlatande));
        intUtlatande.setUtvecklingsstorning(createUtvecklingsstorning(extUtlatande));
        intUtlatande.setSjukhusvard(createSjukhusvard(extUtlatande));
        intUtlatande.setMedicinering(createMedicinering(extUtlatande));
        intUtlatande.setBedomning(createBedomning(extUtlatande));

        return intUtlatande;
    }

    /**
     * Create a Bedomning object from a List of Rekommendation[er]
     * 
     * @param extUtlatande
     *            {@link se.inera.certificate.modules.ts_bas.model.external.Utlatande}
     * @return {@link Bedomning}
     */
    private Bedomning createBedomning(se.inera.certificate.modules.ts_bas.model.external.Utlatande extUtlatande) {
        Bedomning bedomning = new Bedomning();

        List<Rekommendation> rekommendationer = extUtlatande.getRekommendationer();

        for (Rekommendation rek : rekommendationer) {
            if (rek.getRekommendationskod().getCode().equals(RekommendationsKod.PATIENT_UPPFYLLER_KRAV_FOR.getCode())) {
                
                if (rek.getVarde().contains(RekommendationVardeKod.INTE_TA_STALLNING)) {
                    bedomning.setKanInteTaStallning(true);
                } else {
                    bedomning.getKorkortstyp().addAll(convertKoderToEnum(rek.getVarde()));
                }

            } else if (rek.getRekommendationskod().getCode()
                    .equals(RekommendationsKod.PATIENT_BOR_UNDESOKAS_AV_SPECIALIST.getCode())) {
                bedomning.setLakareSpecialKompetens(rek.getBeskrivning());
            } 
        }
        return bedomning;
    }

    // Is there a better way to do this?
    private Collection<? extends BedomningKorkortstyp> convertKoderToEnum(Collection<Kod> vardeList) {

        Set<BedomningKorkortstyp> theSet = EnumSet.noneOf(BedomningKorkortstyp.class);

        for (Kod kod : vardeList) {
            if (RekommendationVardeKod.C1.matches(kod)) {
                theSet.add(BedomningKorkortstyp.C1);

            } else if (RekommendationVardeKod.C1E.matches(kod)) {
                theSet.add(BedomningKorkortstyp.C1E);

            } else if (RekommendationVardeKod.C.matches(kod)) {
                theSet.add(BedomningKorkortstyp.C);

            } else if (RekommendationVardeKod.CE.matches(kod)) {
                theSet.add(BedomningKorkortstyp.CE);

            } else if (RekommendationVardeKod.D1.matches(kod)) {
                theSet.add(BedomningKorkortstyp.D1);

            } else if (RekommendationVardeKod.D1E.matches(kod)) {
                theSet.add(BedomningKorkortstyp.D1E);

            } else if (RekommendationVardeKod.D.matches(kod)) {
                theSet.add(BedomningKorkortstyp.D);

            } else if (RekommendationVardeKod.DE.matches(kod)) {
                theSet.add(BedomningKorkortstyp.DE);

            } else if (RekommendationVardeKod.TAXI.matches(kod)) {
                theSet.add(BedomningKorkortstyp.TAXI);

            } else if (RekommendationVardeKod.ANNAT.matches(kod)) {
                theSet.add(BedomningKorkortstyp.ANNAT);
            }
        }
        return theSet;
    }

    /**
     * Create Medicinering object from Observation
     * 
     * @param extUtlatande
     *            {@link se.inera.certificate.modules.ts_bas.model.external.Utlatande}
     * @return {@link Medicinering}
     */
    private Medicinering createMedicinering(se.inera.certificate.modules.ts_bas.model.external.Utlatande extUtlatande) {
        Medicinering medicinering = new Medicinering();

        Observation stadigvarandeMedicinering = getObservationWithKod(CodeConverter
                .toKod(ObservationsKod.STADIGVARANDE_MEDICINERING));

        if (stadigvarandeMedicinering != null) {
            medicinering.setStadigvarandeMedicinering(stadigvarandeMedicinering.getForekomst());
            if (stadigvarandeMedicinering.getForekomst()) {
                medicinering.setMediciner(stadigvarandeMedicinering.getBeskrivning());
            }
        }
        return medicinering;
    }

    /**
     * Create Sjukhusvard object from Aktivitet
     * 
     * @param extUtlatande
     *            {@link se.inera.certificate.modules.ts_bas.model.external.Utlatande}
     * @return {@link Sjukhusvard}
     */
    private Sjukhusvard createSjukhusvard(se.inera.certificate.modules.ts_bas.model.external.Utlatande extUtlatande) {
        Sjukhusvard sjukhusvard = new Sjukhusvard();

        Aktivitet harVistatsSjukhus = getAktivitetWithKod(CodeConverter.toKod(AktivitetKod.VARD_PA_SJUKHUS));

        if (harVistatsSjukhus != null) {
            sjukhusvard.setSjukhusEllerLakarkontakt(harVistatsSjukhus.getForekomst());
            if (harVistatsSjukhus.getForekomst()) {

                // Make a nicely formatted string with from partial and tom partial
                sjukhusvard.setTidpunkt(PartialConverter
                        .partialToString(harVistatsSjukhus.getAktivitetstid().getFrom())
                        + " - "
                        + PartialConverter.partialToString(harVistatsSjukhus.getAktivitetstid().getTom()));

                sjukhusvard.setVardinrattning(harVistatsSjukhus.getPlats());
                sjukhusvard.setAnledning(harVistatsSjukhus.getBeskrivning());
            }
        }

        return sjukhusvard;
    }

    /**
     * Create Utvecklingsstorning object from Observationer
     * 
     * @param extUtlatande
     *            {@link se.inera.certificate.modules.ts_bas.model.external.Utlatande}
     * @return {@link Utvecklingsstorning}
     */
    private Utvecklingsstorning createUtvecklingsstorning(
            se.inera.certificate.modules.ts_bas.model.external.Utlatande extUtlatande) {
        Utvecklingsstorning utvecklingsstorning = new Utvecklingsstorning();

        Observation psykiskUtvecklingsstorning = getObservationWithKod(CodeConverter
                .toKod(ObservationsKod.PSYKISK_UTVECKLINGSSTORNING));
        Observation adhdDampMm = getObservationWithKod(CodeConverter.toKod(ObservationsKod.ADHD_DAMP_MM));

        if (psykiskUtvecklingsstorning != null) {
            utvecklingsstorning.setPsykiskUtvecklingsstorning(psykiskUtvecklingsstorning.getForekomst());
        }

        if (adhdDampMm != null) {
            utvecklingsstorning.setHarSyndrom(adhdDampMm.getForekomst());
        }

        return utvecklingsstorning;
    }

    /**
     * Create Psykiskt object from Observationer
     * 
     * @param extUtlatande
     *            {@link se.inera.certificate.modules.ts_bas.model.external.Utlatande}
     * @return {@link Psykiskt}
     */
    private Psykiskt createPsykiskt(se.inera.certificate.modules.ts_bas.model.external.Utlatande extUtlatande) {
        Psykiskt psykiskt = new Psykiskt();

        Observation psykiskSjukdom = getObservationWithKod(CodeConverter.toKod(ObservationsKod.PSYKISK_SJUKDOM));

        if (psykiskSjukdom != null) {
            psykiskt.setPsykiskSjukdom(psykiskSjukdom.getForekomst());
        }

        return psykiskt;
    }

    /**
     * Create NarkotikaLakemedel object from Observationer and Aktiviteter
     * 
     * @param extUtlatande
     *            {@link se.inera.certificate.modules.ts_bas.model.external.Utlatande}
     * @return {@link NarkotikaLakemedel}
     */
    private NarkotikaLakemedel createNarkotikaLakemedel(
            se.inera.certificate.modules.ts_bas.model.external.Utlatande extUtlatande) {
        NarkotikaLakemedel narkotikaLakemedel = new NarkotikaLakemedel();

        Observation teckenPaMissbruk = getObservationWithKod(CodeConverter.toKod(ObservationsKod.TECKEN_PA_MISSBRUK));
        Aktivitet vardinsatsMissbruk = getAktivitetWithKod(CodeConverter
                .toKod(AktivitetKod.VARDINSATS_MISSBRUK_BEROENDE));
        Aktivitet provtagningMissbruk = getAktivitetWithKod(CodeConverter
                .toKod(AktivitetKod.PROVTAGNING_ALKOHO_NARKOTIKA));
        Observation lakemedelsanvandning = getObservationWithKod(CodeConverter
                .toKod(ObservationsKod.LAKEMEDELSANVANDNING_TRAFIKSAKERHETSRISK));

        if (teckenPaMissbruk != null) {
            narkotikaLakemedel.setTeckenMissbruk(teckenPaMissbruk.getForekomst());
        }
        if (vardinsatsMissbruk != null) {
            narkotikaLakemedel.setForemalForVardinsats(vardinsatsMissbruk.getForekomst());
        }
        if (provtagningMissbruk != null) {
            narkotikaLakemedel.setProvtagningBehovs(provtagningMissbruk.getForekomst());
        }
        if (lakemedelsanvandning != null) {
            narkotikaLakemedel.setLakarordineratLakemedelsbruk(lakemedelsanvandning.getForekomst());
            if (lakemedelsanvandning.getForekomst()) {
                narkotikaLakemedel.setLakemedelOchDos(lakemedelsanvandning.getBeskrivning());
            }
        }
        return narkotikaLakemedel;
    }

    /**
     * Create SomnVakenhet object from Observationer
     * 
     * @param extUtlatande
     *            {@link se.inera.certificate.modules.ts_bas.model.external.Utlatande}
     * @return {@link SomnVakenhet}
     */
    private SomnVakenhet createSomnVakenhet(se.inera.certificate.modules.ts_bas.model.external.Utlatande extUtlatande) {
        SomnVakenhet somnVakenhet = new SomnVakenhet();

        Observation somnVakenhetsstorning = getObservationWithKod(CodeConverter
                .toKod(ObservationsKod.SOMN_VAKENHETSSTORNING));

        if (somnVakenhetsstorning != null) {
            somnVakenhet.setTeckenSomnstorningar(somnVakenhetsstorning.getForekomst());
        }

        return somnVakenhet;
    }

    /**
     * Create Kognitivt object from Observationer
     * 
     * @param extUtlatande
     *            {@link se.inera.certificate.modules.ts_bas.model.external.Utlatande}
     * @return {@link Kognitivt}
     */
    private Kognitivt createKognitivt(se.inera.certificate.modules.ts_bas.model.external.Utlatande extUtlatande) {
        Kognitivt kognitivt = new Kognitivt();

        Observation sviktandeKognitivFunktion = getObservationWithKod(CodeConverter
                .toKod(ObservationsKod.SVIKTANDE_KOGNITIV_FUNKTION));

        if (sviktandeKognitivFunktion != null) {
            kognitivt.setSviktandeKognitivFunktion(sviktandeKognitivFunktion.getForekomst());
        }

        return kognitivt;
    }

    /**
     * Create Njurar object from Observationer
     * 
     * @param extUtlatande
     *            {@link se.inera.certificate.modules.ts_bas.model.external.Utlatande}
     * @return {@link Njurar}
     */
    private Njurar createNjurar(se.inera.certificate.modules.ts_bas.model.external.Utlatande extUtlatande) {
        Njurar njurar = new Njurar();

        Observation nedsattNjurfunktion = getObservationWithKod(CodeConverter
                .toKod(ObservationsKod.NEDSATT_NJURFUNKTION_TRAFIKSAKERHETSRISK));

        if (nedsattNjurfunktion != null) {
            njurar.setNedsattNjurfunktion(nedsattNjurfunktion.getForekomst());
        }

        return njurar;
    }

    /**
     * Create Medvetandestorning object from Observationer
     * 
     * @param extUtlatande
     *            {@link se.inera.certificate.modules.ts_bas.model.external.Utlatande}
     * @return {@link Medvetandestorning}
     */
    private Medvetandestorning createMedvetandestorning(
            se.inera.certificate.modules.ts_bas.model.external.Utlatande extUtlatande) {
        Medvetandestorning medvetandestorning = new Medvetandestorning();

        Observation harMedvetandestorning = getObservationWithKod(CodeConverter.toKod(ObservationsKod.EPILEPSI));

        if (harMedvetandestorning != null) {
            medvetandestorning.setMedvetandestorning(harMedvetandestorning.getForekomst());
            if (harMedvetandestorning.getForekomst()) {
                medvetandestorning.setBeskrivning(harMedvetandestorning.getBeskrivning());
            }
        }

        return medvetandestorning;
    }

    /**
     * Create Neurologi object from Observationer
     * 
     * @param extUtlatande
     *            {@link se.inera.certificate.modules.ts_bas.model.external.Utlatande}
     * @return {@link Neurologi} object
     */
    private Neurologi createNeurologi(se.inera.certificate.modules.ts_bas.model.external.Utlatande extUtlatande) {
        Neurologi neurologi = new Neurologi();

        Observation neuroSjukdom = getObservationWithKod(CodeConverter
                .toKod(ObservationsKod.TECKEN_PA_NEUROLOGISK_SJUKDOM));

        if (neuroSjukdom != null) {
            neurologi.setNeurologiskSjukdom(neuroSjukdom.getForekomst());
        }

        return neurologi;
    }

    /**
     * Create Diabetes object from Observationer
     * 
     * @param extUtlatande
     *            {@link se.inera.certificate.modules.ts_bas.model.external.Utlatande}
     * @return {@link Diabetes} object
     */
    private Diabetes createDiabetes(se.inera.certificate.modules.ts_bas.model.external.Utlatande extUtlatande) {
        Diabetes diabetes = new Diabetes();

        Observation harDiabetes = getObservationWithKod(CodeConverter.toKod(ObservationsKod.HAR_DIABETES));
        Observation diabetesTyp1 = getObservationWithKod(CodeConverter.toKod(ObservationsKod.DIABETES_TYP_1));
        Observation diabetesTyp2 = getObservationWithKod(CodeConverter.toKod(ObservationsKod.DIABETES_TYP_2));
        Observation insulin = getObservationWithKod(CodeConverter.toKod(ObservationsKod.DIABETIKER_INSULINBEHANDLING));
        Observation tabletter = getObservationWithKod(CodeConverter.toKod(ObservationsKod.DIABETIKER_TABLETTBEHANDLING));
        Observation kost = getObservationWithKod(CodeConverter.toKod(ObservationsKod.DIABETIKER_KOSTBEHANDLING));

        if (harDiabetes != null) {
            diabetes.setHarDiabetes(harDiabetes.getForekomst());
            if (harDiabetes.getForekomst()) {
                if (diabetesTyp1 != null) {
                    diabetes.setDiabetesTyp(diabetesTyp1.getObservationskod().getCode());
                } else if (diabetesTyp2 != null) {
                    diabetes.setDiabetesTyp(diabetesTyp2.getObservationskod().getCode());
                    if (insulin != null) {
                        diabetes.setInsulin(insulin.getForekomst());
                    }
                    if (tabletter != null) {
                        diabetes.setTabletter(tabletter.getForekomst());
                    }
                    if (kost != null) {
                        diabetes.setKost(kost.getForekomst());
                    }
                }
            }
        }

        return diabetes;
    }

    /**
     * Create object encapsulating HjartKarl-related stuff
     * 
     * @param extUtlatande
     *            {@link se.inera.certificate.modules.ts_bas.model.external.Utlatande}
     * @return {@link HjartKarl}
     */
    private HjartKarl createHjartKarl(se.inera.certificate.modules.ts_bas.model.external.Utlatande extUtlatande) {
        HjartKarl hjartKarl = new HjartKarl();

        Observation hjartKarlSjukdom = getObservationWithKod(CodeConverter
                .toKod(ObservationsKod.HJART_KARLSJUKDOM_TRAFIKSAKERHETSRISK));
        Observation hjarnskadaEfterTrauma = getObservationWithKod(CodeConverter
                .toKod(ObservationsKod.TECKEN_PA_HJARNSKADA));
        Observation riskStroke = getObservationWithKod(CodeConverter.toKod(ObservationsKod.RISKFAKTORER_STROKE));

        if (hjartKarlSjukdom != null) {
            hjartKarl.setHjartKarlSjukdom(hjartKarlSjukdom.getForekomst());
        }

        if (hjarnskadaEfterTrauma != null) {
            hjartKarl.setHjarnskadaEfterTrauma(hjarnskadaEfterTrauma.getForekomst());
        }

        if (riskStroke != null) {
            hjartKarl.setBeskrivningRiskfaktorer(riskStroke.getBeskrivning());
        }

        return hjartKarl;
    }

    /**
     * Create object encapsulating Funktionsnedsattnings-related stuff
     * 
     * @param extUtlatande
     *            {@link se.inera.certificate.modules.ts_bas.model.external.Utlatande}
     * @return {@link Funktionsnedsattning}
     */
    private Funktionsnedsattning createFunktionsnedsattning(
            se.inera.certificate.modules.ts_bas.model.external.Utlatande extUtlatande) {

        Funktionsnedsattning funktionsnedsattning = new Funktionsnedsattning();

        Observation framforaFordon = getObservationWithKod(CodeConverter
                .toKod(ObservationsKod.FORSAMRAD_RORLIGHET_FRAMFORA_FORDON));
        Observation hjalpaPassagerare = getObservationWithKod(CodeConverter
                .toKod(ObservationsKod.FORSAMRAD_RORLIGHET_HJALPA_PASSAGERARE));

        if (framforaFordon != null) {
            funktionsnedsattning.setFunktionsnedsattning(framforaFordon.getForekomst());
            if (framforaFordon.getForekomst()) {
                funktionsnedsattning.setBeskrivning(framforaFordon.getBeskrivning());
            }
        }

        if (hjalpaPassagerare != null) {
            funktionsnedsattning.setOtillrackligRorelseformaga(hjalpaPassagerare.getForekomst());
        }

        return funktionsnedsattning;
    }

    /**
     * Create object encapsulating Horsel and Balans-related stuff
     * 
     * @param extUtlatande
     *            {@link se.inera.certificate.modules.ts_bas.model.external.Utlatande}
     * @return {@link HorselBalans}
     */
    private HorselBalans createHorselBalans(se.inera.certificate.modules.ts_bas.model.external.Utlatande extUtlatande) {
        HorselBalans horselBalans = new HorselBalans();

        Observation balansrubbning = getObservationWithKod(CodeConverter
                .toKod(ObservationsKod.ANFALL_BALANSRUBBNING_YRSEL));
        Observation samtal4M = getObservationWithKod(CodeConverter.toKod(ObservationsKod.SVARIGHET_SAMTAL_4M));
        if (balansrubbning != null) {
            horselBalans.setBalansrubbningar(balansrubbning.getForekomst());
        }

        if (samtal4M != null) {
            horselBalans.setSvartUppfattaSamtal4Meter(samtal4M.getForekomst());
        }

        return horselBalans;
    }

    /**
     * Create a {@link Syn} object from {@link Observation}s in
     * {@link se.inera.certificate.modules.ts_bas.model.external.Utlatande}
     * 
     * @param extUtlatande
     *            {@link se.inera.certificate.modules.ts_bas.model.external.Utlatande}
     */
    private Syn createSyn(se.inera.certificate.modules.ts_bas.model.external.Utlatande extUtlatande) {

        Syn syn = new Syn();

        // Find the Syn-related Aktivitet
        Aktivitet plus8Korretionsgrader = getAktivitetWithKod(CodeConverter
                .toKod(AktivitetKod.UNDERSOKNING_PLUS8_KORREKTIONSGRAD));
        if (plus8Korretionsgrader != null) {
            syn.setKorrektionsglasensStyrka(true);
        }

        // Handle Syn related Observationer
        Observation diplopi = getObservationWithKod(CodeConverter.toKod(ObservationsKod.DIPLOPI));
        Observation nattblindhet = getObservationWithKod(CodeConverter.toKod(ObservationsKod.NATTBLINDHET));
        Observation nystagmus = getObservationWithKod(CodeConverter.toKod(ObservationsKod.NYSTAGMUS_MM));
        Observation progresivSjukdom = getObservationWithKod(CodeConverter.toKod(ObservationsKod.PROGRESIV_OGONSJUKDOM));
        Observation synfaltsdefekter = getObservationWithKod(CodeConverter.toKod(ObservationsKod.SYNFALTSDEFEKTER));

        if (diplopi != null) {
            syn.setDiplopi(diplopi.getForekomst());
        }
        if (nattblindhet != null) {
            syn.setDiplopi(nattblindhet.getForekomst());
        }
        if (nystagmus != null) {
            syn.setDiplopi(nystagmus.getForekomst());
        }
        if (progresivSjukdom != null) {
            syn.setDiplopi(progresivSjukdom.getForekomst());
        }
        if (synfaltsdefekter != null) {
            syn.setDiplopi(synfaltsdefekter.getForekomst());
        }

        // Used to populate Syn with Synskarpa later
        List<Observation> synskarpa = new ArrayList<Observation>();

        // Hmmm does this need to be this way or can getObservationWithKod be used instead?
        for (Observation obs : extUtlatande.getObservationer()) {
            Kod kod = obs.getObservationskod();
            if (kod.getCode().equals(ObservationsKod.EJ_KORRIGERAD_SYNSKARPA.getCode())
                    || (kod.getCode().equals(ObservationsKod.KORRIGERAD_SYNSKARPA.getCode()) && obs.getForekomst())
                    || kod.getCode().equals(ObservationsKod.KONTAKTLINSER.getCode())) {
                synskarpa.add(obs);
            }
        }

        populateWithSynskarpa(syn, synskarpa);

        return syn;
    }

    /**
     * Takes a list of observations related to synskarpa and populates a syn object with the information found.
     * 
     * @param syn
     *            The {@link Syn} object to populate with data
     * @param synskarpa
     *            A list of synskarpe-related {@link Observation}s
     */
    private void populateWithSynskarpa(Syn syn, List<Observation> synskarpa) {

        Double hogerUtan = null;
        Double hogerMed = null;
        Double vansterUtan = null;
        Double vansterMed = null;
        Double binUtan = null;
        Double binMed = null;

        boolean hogerKontaktlins = false;
        boolean vansterKontaktlins = false;

        for (Observation o : synskarpa) {
            if (o.getObservationskod().getCode().equals(ObservationsKod.EJ_KORRIGERAD_SYNSKARPA.getCode())
                    && o.getLateralitet().getCode().equals(LateralitetsKod.HOGER.getCode())) {
                hogerUtan = o.getVarde().get(0).getQuantity();

            } else if (o.getObservationskod().getCode().equals(ObservationsKod.KORRIGERAD_SYNSKARPA.getCode())
                    && o.getLateralitet().getCode().equals(LateralitetsKod.HOGER.getCode())) {
                hogerMed = o.getVarde().get(0).getQuantity();

            } else if (o.getObservationskod().getCode().equals(ObservationsKod.EJ_KORRIGERAD_SYNSKARPA.getCode())
                    && o.getLateralitet().getCode().equals(LateralitetsKod.VANSTER.getCode())) {
                vansterUtan = o.getVarde().get(0).getQuantity();

            } else if (o.getObservationskod().getCode().equals(ObservationsKod.KORRIGERAD_SYNSKARPA.getCode())
                    && o.getLateralitet().getCode().equals(LateralitetsKod.VANSTER.getCode())) {
                vansterMed = o.getVarde().get(0).getQuantity();

            } else if (o.getObservationskod().getCode().equals(ObservationsKod.EJ_KORRIGERAD_SYNSKARPA.getCode())
                    && o.getLateralitet().getCode().equals(LateralitetsKod.BINOKULART.getCode())) {
                binUtan = o.getVarde().get(0).getQuantity();

            } else if (o.getObservationskod().getCode().equals(ObservationsKod.KORRIGERAD_SYNSKARPA.getCode())
                    && o.getLateralitet().getCode().equals(LateralitetsKod.BINOKULART.getCode())) {
                binMed = o.getVarde().get(0).getQuantity();

            } else if (o.getObservationskod().getCode().equals(ObservationsKod.KONTAKTLINSER.getCode())
                    && o.getLateralitet().getCode().equals(LateralitetsKod.HOGER.getCode())) {
                hogerKontaktlins = o.getForekomst();
            } else if (o.getObservationskod().getCode().equals(ObservationsKod.KONTAKTLINSER.getCode())
                    && o.getLateralitet().getCode().equals(LateralitetsKod.VANSTER.getCode())) {
                vansterKontaktlins = o.getForekomst();
            }
        }

        syn.setSynskarpaHoger(hogerUtan, hogerMed, hogerKontaktlins);
        syn.setSynskarpaHoger(hogerUtan, hogerMed, hogerKontaktlins);
        syn.setSynskarpaVanster(vansterUtan, vansterMed, vansterKontaktlins);
        syn.setSynskarpaBinokulart(binUtan, binMed);

    }

    private Vardenhet convertToIntVardenhet(se.inera.certificate.model.Vardenhet extVardenhet)
            throws ConverterException {

        LOG.trace("Converting vardenhet");

        if (extVardenhet == null) {
            throw new ConverterException("External Vardenhet is null, can not convert");
        }

        Vardenhet intVardenhet = new Vardenhet();

        intVardenhet.setEnhetsid(InternalModelConverterUtils.getExtensionFromId(extVardenhet.getId()));
        intVardenhet.setEnhetsnamn(extVardenhet.getNamn());
        intVardenhet.setPostadress(extVardenhet.getPostadress());
        intVardenhet.setPostnummer(extVardenhet.getPostnummer());
        intVardenhet.setPostort(extVardenhet.getPostort());
        intVardenhet.setTelefonnummer(extVardenhet.getTelefonnummer());
        intVardenhet.setEpost(extVardenhet.getEpost());

        Vardgivare intVardgivare = convertToIntVardgivare(extVardenhet.getVardgivare());
        intVardenhet.setVardgivare(intVardgivare);

        return intVardenhet;
    }

    private Vardgivare convertToIntVardgivare(se.inera.certificate.model.Vardgivare extVardgivare)
            throws ConverterException {

        LOG.trace("Converting vardgivare");

        if (extVardgivare == null) {
            throw new ConverterException("External vardgivare is null, can not convert");
        }

        Vardgivare intVardgivare = new Vardgivare();

        intVardgivare.setVardgivarid(InternalModelConverterUtils.getExtensionFromId(extVardgivare.getId()));
        intVardgivare.setVardgivarnamn(extVardgivare.getNamn());

        return intVardgivare;
    }

    private HoSPersonal convertToIntHoSPersonal(HosPersonal extHoSPersonal) throws ConverterException {

        LOG.trace("Converting HoSPersonal");

        if (extHoSPersonal == null) {
            throw new ConverterException("External HoSPersonal is null, can not convert");
        }

        HoSPersonal intHoSPersonal = new HoSPersonal();

        intHoSPersonal.setPersonid(InternalModelConverterUtils.getExtensionFromId(extHoSPersonal.getId()));
        intHoSPersonal.setFullstandigtNamn(extHoSPersonal.getNamn());
        intHoSPersonal.setBefattning(extHoSPersonal.getBefattning());

        Vardenhet intVardenhet = convertToIntVardenhet(extHoSPersonal.getVardenhet());
        intHoSPersonal.setVardenhet(intVardenhet);

        return intHoSPersonal;
    }

    private Patient convertToIntPatient(se.inera.certificate.model.Patient extPatient) throws ConverterException {

        LOG.trace("Converting patient");

        if (extPatient == null) {
            throw new ConverterException("No Patient found to convert");
        }

        Patient intPatient = new Patient();

        intPatient.setPersonid(InternalModelConverterUtils.getExtensionFromId(extPatient.getId()));

        intPatient.setEfternamn(extPatient.getEfternamn());

        String forNamn = StringUtils.join(extPatient.getFornamn(), " ");
        intPatient.setFornamn(forNamn);

        String fullstandigtNamn = forNamn.concat(" ").concat(extPatient.getEfternamn());
        intPatient.setFullstandigtNamn(fullstandigtNamn);

        intPatient.setPostadress(extPatient.getPostadress());
        intPatient.setPostnummer(extPatient.getPostnummer());
        intPatient.setPostort(extPatient.getPostort());

        return intPatient;
    }

    /**
     * Returns an Observation based on the specified Kod, or <code>null</code> if none where found.
     * 
     * @param observationskod
     *            Find an observation with this {@link Kod}
     * @return an {@link Observation} if it is found, or null otherwise
     */
    public Observation getObservationWithKod(Kod observationskod) {
        for (Observation observation : observationer) {
            if (observationskod.equals(observation.getObservationskod())) {
                return observation;
            }
        }

        return null;
    }

    /**
     * Returns an Aktivitet based on the specified Kod, or <code>null</code> if none where found.
     * 
     * @param aktivitetskod
     *            Find an aktivitet with this {@link Kod}
     * @return an {@link Aktivitet} if it is found, or null otherwise
     */
    public Aktivitet getAktivitetWithKod(Kod aktivitetskod) {
        for (Aktivitet aktivitet : aktiviteter) {
            if (aktivitetskod.equals(aktivitet.getAktivitetskod())) {
                return aktivitet;
            }
        }

        return null;
    }

}
