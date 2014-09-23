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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.inera.certificate.model.Kod;
import se.inera.certificate.model.util.Strings;
import se.inera.certificate.modules.ts_bas.model.codes.AktivitetKod;
import se.inera.certificate.modules.ts_bas.model.codes.CodeConverter;
import se.inera.certificate.modules.ts_bas.model.codes.CodeSystem;
import se.inera.certificate.modules.ts_bas.model.codes.IdKontrollKod;
import se.inera.certificate.modules.ts_bas.model.codes.IntygAvserKod;
import se.inera.certificate.modules.ts_bas.model.codes.LateralitetsKod;
import se.inera.certificate.modules.ts_bas.model.codes.ObservationsKod;
import se.inera.certificate.modules.ts_bas.model.codes.RekommendationVardeKod;
import se.inera.certificate.modules.ts_bas.model.codes.RekommendationsKod;
import se.inera.certificate.modules.ts_bas.model.codes.SpecialitetKod;
import se.inera.certificate.modules.ts_bas.model.codes.UtlatandeKod;
import se.inera.certificate.modules.ts_bas.model.external.Aktivitet;
import se.inera.certificate.modules.ts_bas.model.external.HosPersonal;
import se.inera.certificate.modules.ts_bas.model.external.Observation;
import se.inera.certificate.modules.ts_bas.model.external.Rekommendation;
import se.inera.certificate.modules.ts_bas.model.internal.Bedomning;
import se.inera.certificate.modules.ts_bas.model.internal.BedomningKorkortstyp;
import se.inera.certificate.modules.ts_bas.model.internal.Diabetes;
import se.inera.certificate.modules.ts_bas.model.internal.Funktionsnedsattning;
import se.inera.certificate.modules.ts_bas.model.internal.HjartKarl;
import se.inera.certificate.modules.ts_bas.model.internal.HoSPersonal;
import se.inera.certificate.modules.ts_bas.model.internal.HorselBalans;
import se.inera.certificate.modules.ts_bas.model.internal.IntygAvser;
import se.inera.certificate.modules.ts_bas.model.internal.IntygAvserKategori;
import se.inera.certificate.modules.ts_bas.model.internal.Kognitivt;
import se.inera.certificate.modules.ts_bas.model.internal.Medicinering;
import se.inera.certificate.modules.ts_bas.model.internal.Medvetandestorning;
import se.inera.certificate.modules.ts_bas.model.internal.NarkotikaLakemedel;
import se.inera.certificate.modules.ts_bas.model.internal.Neurologi;
import se.inera.certificate.modules.ts_bas.model.internal.Njurar;
import se.inera.certificate.modules.ts_bas.model.internal.Patient;
import se.inera.certificate.modules.ts_bas.model.internal.Psykiskt;
import se.inera.certificate.modules.ts_bas.model.internal.Sjukhusvard;
import se.inera.certificate.modules.ts_bas.model.internal.SomnVakenhet;
import se.inera.certificate.modules.ts_bas.model.internal.Syn;
import se.inera.certificate.modules.ts_bas.model.internal.Utlatande;
import se.inera.certificate.modules.ts_bas.model.internal.Utvecklingsstorning;
import se.inera.certificate.modules.ts_bas.model.internal.Vardenhet;
import se.inera.certificate.modules.ts_bas.model.internal.Vardgivare;
import se.inera.certificate.modules.ts_bas.model.internal.Vardkontakt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Converter for converting the external format to the internal view format.
 *
 * @author Erik
 */
public class ExternalToInternalConverterInstance {

    private static final Logger LOG = LoggerFactory.getLogger(ExternalToInternalConverterInstance.class);

    public Utlatande convert(se.inera.certificate.modules.ts_bas.model.external.Utlatande externalModel) throws ConverterException {
        Utlatande intUtlatande = convertUtlatandeFromExternalToInternal(externalModel);
        LOG.trace("Converting external model to internal");
        return intUtlatande;
    }

    private Utlatande convertUtlatandeFromExternalToInternal(
            se.inera.certificate.modules.ts_bas.model.external.Utlatande extUtlatande) throws ConverterException {
        LOG.debug("Converting Utlatande '{}' from external to internal", extUtlatande.getId());

        Utlatande intUtlatande = new Utlatande();

        intUtlatande.setId(InternalModelConverterUtils.getExtensionFromId(extUtlatande.getId()));
        intUtlatande.setTyp(UtlatandeKod.getVersionFromTSParams(extUtlatande.getTsUtgava(), extUtlatande.getTsVersion()).name());
        intUtlatande.setSigneringsdatum(extUtlatande.getSigneringsdatum());
        intUtlatande.setSkickatdatum(extUtlatande.getSkickatdatum());
        intUtlatande.setKommentar(getSingleElement(extUtlatande.getKommentarer()));

        HoSPersonal intHoSPersonal = convertToIntHoSPersonal(extUtlatande.getSkapadAv());
        intUtlatande.setSkapadAv(intHoSPersonal);

        Patient intPatient = convertToIntPatient(extUtlatande.getPatient());
        intUtlatande.setPatient(intPatient);

        setVardkontakt(getSingleElement(extUtlatande.getVardkontakter()), intUtlatande);

        setIntygAvser(extUtlatande.getIntygAvser(), intUtlatande);

        setBedomning(extUtlatande.getRekommendationer(), intUtlatande);

        // Create internal model-specific objects grouping similar attributes together
        setSyn(extUtlatande, intUtlatande);

        setHorselBalans(extUtlatande, intUtlatande);
        setFunktionsnedsattning(extUtlatande, intUtlatande);
        setHjartKarl(extUtlatande, intUtlatande);
        setDiabetes(extUtlatande, intUtlatande);
        setNeurologi(extUtlatande, intUtlatande);
        setMedvetandestorning(extUtlatande, intUtlatande);
        setNjurar(extUtlatande, intUtlatande);
        setKognitivt(extUtlatande, intUtlatande);
        setSomnVakenhet(extUtlatande, intUtlatande);
        setNarkotikaLakemedel(extUtlatande, intUtlatande);
        setPsykiskt(extUtlatande, intUtlatande);
        setUtvecklingsstorning(extUtlatande, intUtlatande);
        setSjukhusvard(extUtlatande, intUtlatande);
        setMedicinering(extUtlatande, intUtlatande);

        return intUtlatande;
    }

    /**
     * Convert a List of Kod into an IntygAvser object.
     *
     * @param intygAvser
     *            a List of {@link Kod}
     * @param intUtlatande
     *            {@link Utlatande}
     */
    private void setIntygAvser(List<Kod> intygAvser, Utlatande intUtlatande) {
        IntygAvser internalIntygAvser = intUtlatande.getIntygAvser();

        for (Kod kod : intygAvser) {
            IntygAvserKod vardeKod = CodeConverter.fromCode(kod, IntygAvserKod.class);
            internalIntygAvser.getKorkortstyp().add(IntygAvserKategori.valueOf(vardeKod.name()));
        }
    }

    /**
     * Convert Vardkontakt from external to internal format.
     *
     * @param source
     *            {@link se.inera.certificate.modules.ts_bas.model.external.Vardkontakt}
     * @param intUtlatande
     *            {@link Utlatande}
     */
    private void setVardkontakt(se.inera.certificate.modules.ts_bas.model.external.Vardkontakt source,
            Utlatande intUtlatande) {
        Vardkontakt vardkontakt = intUtlatande.getVardkontakt();

        vardkontakt.setIdkontroll(CodeConverter.getInternalNameFromKod(source.getIdkontroll(), IdKontrollKod.class));
        vardkontakt.setTyp(source.getVardkontakttyp().getCode());

    }

    private void setBedomning(List<Rekommendation> rekommendationer, Utlatande intUtlatande) {
        Bedomning bedomning = intUtlatande.getBedomning();

        for (Rekommendation rek : rekommendationer) {
            if (CodeConverter.matches(RekommendationsKod.PATIENT_UPPFYLLER_KRAV_FOR, rek.getRekommendationskod())) {
                if (rek.getVarde().contains(CodeConverter.toKod(RekommendationVardeKod.INTE_TA_STALLNING))) {
                    bedomning.setKanInteTaStallning(true);
                } else {
                    for (Kod varde : rek.getVarde()) {
                        RekommendationVardeKod vardeKod = CodeConverter.fromCode(varde, RekommendationVardeKod.class);
                        bedomning.getKorkortstyp().add(BedomningKorkortstyp.valueOf(vardeKod.name()));
                    }
                }

            } else if (CodeConverter.matches(RekommendationsKod.PATIENT_BOR_UNDESOKAS_AV_SPECIALIST,
                    rek.getRekommendationskod())) {
                bedomning.setLakareSpecialKompetens(rek.getBeskrivning());

            }
        }
    }

    /**
     * Create Medicinering object from Observation.
     *
     * @param extUtlatande
     *            {@link se.inera.certificate.modules.ts_bas.model.external.Utlatande}
     * @param intUtlatande
     *            {@link Utlatande}
     */
    private void setMedicinering(se.inera.certificate.modules.ts_bas.model.external.Utlatande extUtlatande,
            Utlatande intUtlatande) {
        Medicinering medicinering = intUtlatande.getMedicinering();

        Observation stadigvarandeMedicinering = getObservationWithKod(extUtlatande.getObservationer(),
                CodeConverter.toKod(ObservationsKod.STADIGVARANDE_MEDICINERING));

        if (stadigvarandeMedicinering != null) {
            medicinering.setStadigvarandeMedicinering(stadigvarandeMedicinering.getForekomst());
            if (stadigvarandeMedicinering.getForekomst()) {
                medicinering.setBeskrivning(stadigvarandeMedicinering.getBeskrivning());
            }
        }
    }

    /**
     * Create Sjukhusvard object from Aktivitet.
     *
     * @param extUtlatande
     *            {@link se.inera.certificate.modules.ts_bas.model.external.Utlatande}
     * @param intUtlatande
     *            {@link Utlatande}
     */
    private void setSjukhusvard(se.inera.certificate.modules.ts_bas.model.external.Utlatande extUtlatande,
            Utlatande intUtlatande) {
        Sjukhusvard sjukhusvard = intUtlatande.getSjukhusvard();

        Aktivitet harVistatsSjukhus = getAktivitetWithKod(extUtlatande.getAktiviteter(), CodeConverter.toKod(AktivitetKod.VARD_PA_SJUKHUS));

        if (harVistatsSjukhus != null) {
            sjukhusvard.setSjukhusEllerLakarkontakt(harVistatsSjukhus.getForekomst());
            if (harVistatsSjukhus.getForekomst()) {

                sjukhusvard.setTidpunkt(harVistatsSjukhus.getOstruktureradTid());
                sjukhusvard.setVardinrattning(harVistatsSjukhus.getPlats());
                sjukhusvard.setAnledning(harVistatsSjukhus.getBeskrivning());
            }
        }
    }

    /**
     * Create Utvecklingsstorning object from Observationer.
     *
     * @param extUtlatande
     *            {@link se.inera.certificate.modules.ts_bas.model.external.Utlatande}
     * @param intUtlatande
     *            {@link Utlatande}
     */
    private void setUtvecklingsstorning(se.inera.certificate.modules.ts_bas.model.external.Utlatande extUtlatande,
            Utlatande intUtlatande) {
        Utvecklingsstorning utvecklingsstorning = intUtlatande.getUtvecklingsstorning();

        Observation psykiskUtvecklingsstorning = getObservationWithKod(extUtlatande.getObservationer(),
                CodeConverter.toKod(ObservationsKod.PSYKISK_UTVECKLINGSSTORNING));
        Observation adhdDampMm = getObservationWithKod(extUtlatande.getObservationer(), CodeConverter.toKod(ObservationsKod.ADHD_DAMP_MM));

        if (psykiskUtvecklingsstorning != null) {
            utvecklingsstorning.setPsykiskUtvecklingsstorning(psykiskUtvecklingsstorning.getForekomst());
        }

        if (adhdDampMm != null) {
            utvecklingsstorning.setHarSyndrom(adhdDampMm.getForekomst());
        }
    }

    /**
     * Create Psykiskt object from Observationer.
     *
     * @param extUtlatande
     *            {@link se.inera.certificate.modules.ts_bas.model.external.Utlatande}
     * @param intUtlatande
     *            {@link Utlatande}
     */
    private void setPsykiskt(se.inera.certificate.modules.ts_bas.model.external.Utlatande extUtlatande,
            Utlatande intUtlatande) {
        Psykiskt psykiskt = intUtlatande.getPsykiskt();

        Observation psykiskSjukdom = getObservationWithKod(extUtlatande.getObservationer(), CodeConverter.toKod(ObservationsKod.PSYKISK_SJUKDOM));

        if (psykiskSjukdom != null) {
            psykiskt.setPsykiskSjukdom(psykiskSjukdom.getForekomst());
        }
    }

    /**
     * Create NarkotikaLakemedel object from Observationer and Aktiviteter.
     *
     * @param extUtlatande
     *            {@link se.inera.certificate.modules.ts_bas.model.external.Utlatande}
     * @param intUtlatande
     *            {@link Utlatande}
     */
    private void setNarkotikaLakemedel(se.inera.certificate.modules.ts_bas.model.external.Utlatande extUtlatande,
            Utlatande intUtlatande) {
        NarkotikaLakemedel narkotikaLakemedel = intUtlatande.getNarkotikaLakemedel();

        Observation teckenPaMissbruk = getObservationWithKod(extUtlatande.getObservationer(), CodeConverter.toKod(ObservationsKod.TECKEN_PA_MISSBRUK));
        Aktivitet vardinsatsMissbruk = getAktivitetWithKod(extUtlatande.getAktiviteter(),
                CodeConverter.toKod(AktivitetKod.VARDINSATS_MISSBRUK_BEROENDE));
        Aktivitet provtagningMissbruk = getAktivitetWithKod(extUtlatande.getAktiviteter(),
                CodeConverter.toKod(AktivitetKod.PROVTAGNING_ALKOHOL_NARKOTIKA));
        Observation lakemedelsanvandning = getObservationWithKod(extUtlatande.getObservationer(),
                CodeConverter.toKod(ObservationsKod.LAKEMEDELSANVANDNING_TRAFIKSAKERHETSRISK));

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
    }

    /**
     * Create SomnVakenhet object from Observationer.
     *
     * @param extUtlatande
     *            {@link se.inera.certificate.modules.ts_bas.model.external.Utlatande}
     * @param intUtlatande
     *            {@link Utlatande}
     */
    private void setSomnVakenhet(se.inera.certificate.modules.ts_bas.model.external.Utlatande extUtlatande,
            Utlatande intUtlatande) {
        SomnVakenhet somnVakenhet = intUtlatande.getSomnVakenhet();

        Observation somnVakenhetsstorning = getObservationWithKod(extUtlatande.getObservationer(),
                CodeConverter.toKod(ObservationsKod.SOMN_VAKENHETSSTORNING));

        if (somnVakenhetsstorning != null) {
            somnVakenhet.setTeckenSomnstorningar(somnVakenhetsstorning.getForekomst());
        }
    }

    /**
     * Create Kognitivt object from Observationer.
     *
     * @param extUtlatande
     *            {@link se.inera.certificate.modules.ts_bas.model.external.Utlatande}
     * @param intUtlatande
     *            {@link Utlatande}
     */
    private void setKognitivt(se.inera.certificate.modules.ts_bas.model.external.Utlatande extUtlatande,
            Utlatande intUtlatande) {
        Kognitivt kognitivt = intUtlatande.getKognitivt();

        Observation sviktandeKognitivFunktion = getObservationWithKod(extUtlatande.getObservationer(),
                CodeConverter.toKod(ObservationsKod.SVIKTANDE_KOGNITIV_FUNKTION));

        if (sviktandeKognitivFunktion != null) {
            kognitivt.setSviktandeKognitivFunktion(sviktandeKognitivFunktion.getForekomst());
        }
    }

    /**
     * Create Njurar object from Observationer.
     *
     * @param extUtlatande
     *            {@link se.inera.certificate.modules.ts_bas.model.external.Utlatande}
     * @param intUtlatande
     *            {@link Utlatande}
     */
    private void setNjurar(se.inera.certificate.modules.ts_bas.model.external.Utlatande extUtlatande,
            Utlatande intUtlatande) {
        Njurar njurar = intUtlatande.getNjurar();

        Observation nedsattNjurfunktion = getObservationWithKod(extUtlatande.getObservationer(),
                CodeConverter.toKod(ObservationsKod.NEDSATT_NJURFUNKTION_TRAFIKSAKERHETSRISK));

        if (nedsattNjurfunktion != null) {
            njurar.setNedsattNjurfunktion(nedsattNjurfunktion.getForekomst());
        }
    }

    /**
     * Create Medvetandestorning object from Observationer.
     *
     * @param extUtlatande
     *            {@link se.inera.certificate.modules.ts_bas.model.external.Utlatande}
     * @param intUtlatande
     *            {@link Utlatande}
     */
    private void setMedvetandestorning(se.inera.certificate.modules.ts_bas.model.external.Utlatande extUtlatande,
            Utlatande intUtlatande) {
        Medvetandestorning medvetandestorning = intUtlatande.getMedvetandestorning();

        Observation harMedvetandestorning = getObservationWithKod(extUtlatande.getObservationer(), CodeConverter.toKod(ObservationsKod.EPILEPSI));

        if (harMedvetandestorning != null) {
            medvetandestorning.setMedvetandestorning(harMedvetandestorning.getForekomst());
            if (harMedvetandestorning.getForekomst()) {
                medvetandestorning.setBeskrivning(harMedvetandestorning.getBeskrivning());
            }
        }

    }

    /**
     * Create Neurologi object from Observationer.
     *
     * @param extUtlatande
     *            {@link se.inera.certificate.modules.ts_bas.model.external.Utlatande}
     * @param intUtlatande
     *            {@link Utlatande}
     */
    private void setNeurologi(se.inera.certificate.modules.ts_bas.model.external.Utlatande extUtlatande,
            Utlatande intUtlatande) {
        Neurologi neurologi = intUtlatande.getNeurologi();

        Observation neuroSjukdom = getObservationWithKod(extUtlatande.getObservationer(),
                CodeConverter.toKod(ObservationsKod.TECKEN_PA_NEUROLOGISK_SJUKDOM));

        if (neuroSjukdom != null) {
            neurologi.setNeurologiskSjukdom(neuroSjukdom.getForekomst());
        }
    }

    /**
     * Create Diabetes object from Observationer.
     *
     * @param extUtlatande
     *            {@link se.inera.certificate.modules.ts_bas.model.external.Utlatande}
     * @param intUtlatande
     *            {@link Utlatande}
     */
    private void setDiabetes(se.inera.certificate.modules.ts_bas.model.external.Utlatande extUtlatande,
            Utlatande intUtlatande) {
        Diabetes diabetes = intUtlatande.getDiabetes();

        List<Observation> observationer = extUtlatande.getObservationer();
        Observation harDiabetes = getObservationWithKod(observationer, CodeConverter.toKod(ObservationsKod.HAR_DIABETES));
        Observation diabetesTyp1 = getObservationWithKod(observationer, CodeConverter.toKod(ObservationsKod.DIABETES_TYP_1));
        Observation diabetesTyp2 = getObservationWithKod(observationer, CodeConverter.toKod(ObservationsKod.DIABETES_TYP_2));
        Observation insulin = getObservationWithKod(observationer, CodeConverter.toKod(ObservationsKod.DIABETIKER_INSULINBEHANDLING));
        Observation tabletter = getObservationWithKod(observationer, CodeConverter.toKod(ObservationsKod.DIABETIKER_TABLETTBEHANDLING));
        Observation kost = getObservationWithKod(observationer, CodeConverter.toKod(ObservationsKod.DIABETIKER_KOSTBEHANDLING));

        if (harDiabetes != null) {
            diabetes.setHarDiabetes(harDiabetes.getForekomst());
            if (harDiabetes.getForekomst()) {
                if (diabetesTyp1 != null) {
                    diabetes.setDiabetesTyp(CodeConverter.getInternalNameFromKod(diabetesTyp1.getObservationskod(),
                            ObservationsKod.class));
                } else if (diabetesTyp2 != null) {
                    diabetes.setDiabetesTyp(CodeConverter.getInternalNameFromKod(diabetesTyp2.getObservationskod(),
                            ObservationsKod.class));
                }
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

    /**
     * Create object encapsulating HjartKarl-related stuff.
     *
     * @param extUtlatande
     *            {@link se.inera.certificate.modules.ts_bas.model.external.Utlatande}
     * @param intUtlatande
     *            {@link Utlatande}
     */
    private void setHjartKarl(se.inera.certificate.modules.ts_bas.model.external.Utlatande extUtlatande,
            Utlatande intUtlatande) {
        HjartKarl hjartKarl = intUtlatande.getHjartKarl();

        Observation hjartKarlSjukdom = getObservationWithKod(extUtlatande.getObservationer(),
                CodeConverter.toKod(ObservationsKod.HJART_KARLSJUKDOM_TRAFIKSAKERHETSRISK));
        Observation hjarnskadaEfterTrauma = getObservationWithKod(extUtlatande.getObservationer(),
                CodeConverter.toKod(ObservationsKod.TECKEN_PA_HJARNSKADA));
        Observation riskStroke = getObservationWithKod(extUtlatande.getObservationer(), CodeConverter.toKod(ObservationsKod.RISKFAKTORER_STROKE));

        if (hjartKarlSjukdom != null) {
            hjartKarl.setHjartKarlSjukdom(hjartKarlSjukdom.getForekomst());
        }

        if (hjarnskadaEfterTrauma != null) {
            hjartKarl.setHjarnskadaEfterTrauma(hjarnskadaEfterTrauma.getForekomst());
        }

        if (riskStroke != null) {
            hjartKarl.setRiskfaktorerStroke(riskStroke.getForekomst());
            if (riskStroke.getForekomst()) {
                hjartKarl.setBeskrivningRiskfaktorer(riskStroke.getBeskrivning());
            }
        }
    }

    /**
     * Create object encapsulating Funktionsnedsattnings-related stuff.
     *
     * @param extUtlatande
     *            {@link se.inera.certificate.modules.ts_bas.model.external.Utlatande}
     * @param intUtlatande
     *            {@link Utlatande}
     */
    private void setFunktionsnedsattning(se.inera.certificate.modules.ts_bas.model.external.Utlatande extUtlatande,
            Utlatande intUtlatande) {

        Funktionsnedsattning funktionsnedsattning = intUtlatande.getFunktionsnedsattning();

        Observation framforaFordon = getObservationWithKod(extUtlatande.getObservationer(),
                CodeConverter.toKod(ObservationsKod.FORSAMRAD_RORLIGHET_FRAMFORA_FORDON));
        Observation hjalpaPassagerare = getObservationWithKod(extUtlatande.getObservationer(),
                CodeConverter.toKod(ObservationsKod.FORSAMRAD_RORLIGHET_HJALPA_PASSAGERARE));

        if (framforaFordon != null) {
            funktionsnedsattning.setFunktionsnedsattning(framforaFordon.getForekomst());
            if (framforaFordon.getForekomst()) {
                funktionsnedsattning.setBeskrivning(framforaFordon.getBeskrivning());
            }
        }

        if (hjalpaPassagerare != null) {
            funktionsnedsattning.setOtillrackligRorelseformaga(hjalpaPassagerare.getForekomst());
        }
    }

    /**
     * Create object encapsulating Horsel and Balans-related stuff.
     *
     * @param extUtlatande
     *            {@link se.inera.certificate.modules.ts_bas.model.external.Utlatande}
     * @param intUtlatande
     *            {@link Utlatande}
     */
    private void setHorselBalans(se.inera.certificate.modules.ts_bas.model.external.Utlatande extUtlatande,
            Utlatande intUtlatande) {
        HorselBalans horselBalans = intUtlatande.getHorselBalans();

        Observation balansrubbning = getObservationWithKod(extUtlatande.getObservationer(), CodeConverter
                .toKod(ObservationsKod.ANFALL_BALANSRUBBNING_YRSEL));
        Observation samtal4M = getObservationWithKod(extUtlatande.getObservationer(), CodeConverter.toKod(ObservationsKod.SVARIGHET_SAMTAL_4M));
        if (balansrubbning != null) {
            horselBalans.setBalansrubbningar(balansrubbning.getForekomst());
        }

        if (samtal4M != null) {
            horselBalans.setSvartUppfattaSamtal4Meter(samtal4M.getForekomst());
        }
    }

    /**
     * Create a {@link Syn} object from {@link Observation}s in
     * {@link se.inera.certificate.modules.ts_bas.model.external.Utlatande}.
     *
     * @param extUtlatande
     *            {@link se.inera.certificate.modules.ts_bas.model.external.Utlatande}
     * @param intUtlatande
     *            {@link Utlatande}
     */
    private void setSyn(se.inera.certificate.modules.ts_bas.model.external.Utlatande extUtlatande,
            Utlatande intUtlatande) {

        Syn syn = intUtlatande.getSyn();

        // Find the Syn-related Aktivitet
        Aktivitet plus8Korretionsgrader = getAktivitetWithKod(extUtlatande.getAktiviteter(), CodeConverter
                .toKod(AktivitetKod.UNDERSOKNING_PLUS8_KORREKTIONSGRAD));

        if (plus8Korretionsgrader != null) {
            syn.setKorrektionsglasensStyrka(plus8Korretionsgrader.getForekomst());
        }

        // Handle Syn related Observationer
        List<Observation> observationer = extUtlatande.getObservationer();
        Observation diplopi = getObservationWithKod(observationer, CodeConverter.toKod(ObservationsKod.DIPLOPI));
        Observation nattblindhet = getObservationWithKod(observationer, CodeConverter.toKod(ObservationsKod.NATTBLINDHET));
        Observation nystagmus = getObservationWithKod(observationer, CodeConverter.toKod(ObservationsKod.NYSTAGMUS_MM));
        Observation progresivSjukdom = getObservationWithKod(observationer, CodeConverter.toKod(ObservationsKod.PROGRESIV_OGONSJUKDOM));
        Observation synfaltsdefekter = getObservationWithKod(observationer, CodeConverter.toKod(ObservationsKod.SYNFALTSDEFEKTER));

        if (diplopi != null) {
            syn.setDiplopi(diplopi.getForekomst());
        }
        if (nattblindhet != null) {
            syn.setNattblindhet(nattblindhet.getForekomst());
        }
        if (nystagmus != null) {
            syn.setNystagmus(nystagmus.getForekomst());
        }
        if (progresivSjukdom != null) {
            syn.setProgressivOgonsjukdom(progresivSjukdom.getForekomst());
        }
        if (synfaltsdefekter != null) {
            syn.setSynfaltsdefekter(synfaltsdefekter.getForekomst());
        }

        // Used to populate Syn with Synskarpa later
        List<Observation> synskarpa = new ArrayList<>();

        // Hmmm does this need to be this way or can getObservationWithKod be used instead?
        for (Observation obs : observationer) {
            Kod kod = obs.getObservationskod();
            if (kod.getCode().equals(ObservationsKod.EJ_KORRIGERAD_SYNSKARPA.getCode())
                    || kod.getCode().equals(ObservationsKod.KORRIGERAD_SYNSKARPA.getCode())
                    || kod.getCode().equals(ObservationsKod.KONTAKTLINSER.getCode())) {
                synskarpa.add(obs);
            }
        }

        populateWithSynskarpa(syn, synskarpa);
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
            if (isObservationAndLateralitet(o, ObservationsKod.EJ_KORRIGERAD_SYNSKARPA, LateralitetsKod.HOGER)) {
                hogerUtan = o.getVarde().get(0).getQuantity();

            } else if (isObservationAndLateralitet(o, ObservationsKod.KORRIGERAD_SYNSKARPA, LateralitetsKod.HOGER)) {
                hogerMed = o.getVarde().get(0).getQuantity();

            } else if (isObservationAndLateralitet(o, ObservationsKod.EJ_KORRIGERAD_SYNSKARPA, LateralitetsKod.VANSTER)) {
                vansterUtan = o.getVarde().get(0).getQuantity();

            } else if (isObservationAndLateralitet(o, ObservationsKod.KORRIGERAD_SYNSKARPA, LateralitetsKod.VANSTER)) {
                vansterMed = o.getVarde().get(0).getQuantity();

            } else if (isObservationAndLateralitet(o, ObservationsKod.EJ_KORRIGERAD_SYNSKARPA,
                    LateralitetsKod.BINOKULART)) {
                binUtan = o.getVarde().get(0).getQuantity();

            } else if (isObservationAndLateralitet(o, ObservationsKod.KORRIGERAD_SYNSKARPA, LateralitetsKod.BINOKULART)) {
                binMed = o.getVarde().get(0).getQuantity();

            } else if (isObservationAndLateralitet(o, ObservationsKod.KONTAKTLINSER, LateralitetsKod.HOGER)) {
                hogerKontaktlins = Boolean.TRUE.equals(o.getForekomst());
            } else if (isObservationAndLateralitet(o, ObservationsKod.KONTAKTLINSER, LateralitetsKod.VANSTER)) {
                vansterKontaktlins = Boolean.TRUE.equals(o.getForekomst());
            }
        }

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

        intHoSPersonal.getBefattningar().addAll(extHoSPersonal.getBefattningar());
        intHoSPersonal.getSpecialiteter().addAll(
                convertKodToString(extHoSPersonal.getSpecialiteter(), SpecialitetKod.class));

        Vardenhet intVardenhet = convertToIntVardenhet(extHoSPersonal.getVardenhet());
        intHoSPersonal.setVardenhet(intVardenhet);

        return intHoSPersonal;
    }

    private Collection<String> convertKodToString(List<Kod> koder, Class<? extends CodeSystem> type) {
        List<String> intKoder = new ArrayList<>();
        for (Kod kod : koder) {
            intKoder.add(CodeConverter.fromCode(kod, type).toString());
        }
        return intKoder;
    }

    private Patient convertToIntPatient(se.inera.certificate.model.Patient extPatient) throws ConverterException {

        LOG.trace("Converting patient");

        if (extPatient == null) {
            throw new ConverterException("No Patient found to convert");
        }

        Patient intPatient = new Patient();

        intPatient.setPersonid(InternalModelConverterUtils.getExtensionFromId(extPatient.getId()));

        intPatient.setFornamn(Strings.join(" ", extPatient.getFornamn()));
        if (!extPatient.getMellannamn().isEmpty()) {
            intPatient.setMellannamn(Strings.join(" ", extPatient.getMellannamn()));
        }
        intPatient.setEfternamn(extPatient.getEfternamn());
        intPatient.setFullstandigtNamn(extPatient.getFullstandigtNamn());

        intPatient.setPostadress(extPatient.getPostadress());
        intPatient.setPostnummer(extPatient.getPostnummer());
        intPatient.setPostort(extPatient.getPostort());

        return intPatient;
    }

    private boolean isObservationAndLateralitet(Observation obs, ObservationsKod observationskod,
            LateralitetsKod lateralitet) {
        if (CodeConverter.matches(observationskod, obs.getObservationskod())) {
            if (CodeConverter.matches(lateralitet, obs.getLateralitet())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns an Observation based on the specified Kod, or <code>null</code> if none where found.
     *
     * @param observationer
     *            observationer
     * @param observationskod
     *            Find an observation with this {@link Kod}
     * @return an {@link Observation} if it is found, or null otherwise
     */
    public Observation getObservationWithKod(List<Observation> observationer, Kod observationskod) {
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
     * @param aktiviteter
     *            aktiviteter
     * @param aktivitetskod
     *            Find an aktivitet with this {@link Kod}
     * @return an {@link Aktivitet} if it is found, or null otherwise
     */
    public Aktivitet getAktivitetWithKod(List<Aktivitet> aktiviteter, Kod aktivitetskod) {
        for (Aktivitet aktivitet : aktiviteter) {
            if (aktivitetskod.equals(aktivitet.getAktivitetskod())) {
                return aktivitet;
            }
        }

        return null;
    }

    /**
     * Get a single object from a collection, return null if collection contains more than one object.
     *
     * @param collection
     *            The collection
     * @return an object
     */
    private <T> T getSingleElement(Collection<T> collection) {
        if (collection.size() != 1) {
            return null;
        }
        return collection.iterator().next();
    }

}
