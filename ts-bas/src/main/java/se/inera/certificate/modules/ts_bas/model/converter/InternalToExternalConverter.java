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
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.certificate.model.Id;
import se.inera.certificate.model.Kod;
import se.inera.certificate.model.Patient;
import se.inera.certificate.model.PhysicalQuantity;
import se.inera.certificate.model.Vardgivare;
import se.inera.certificate.modules.ts_bas.model.codes.AktivitetKod;
import se.inera.certificate.modules.ts_bas.model.codes.CodeConverter;
import se.inera.certificate.modules.ts_bas.model.codes.CodeSystem;
import se.inera.certificate.modules.ts_bas.model.codes.HSpersonalKod;
import se.inera.certificate.modules.ts_bas.model.codes.IdKontrollKod;
import se.inera.certificate.modules.ts_bas.model.codes.IntygAvserKod;
import se.inera.certificate.modules.ts_bas.model.codes.LateralitetsKod;
import se.inera.certificate.modules.ts_bas.model.codes.MetodKod;
import se.inera.certificate.modules.ts_bas.model.codes.ObservationsKod;
import se.inera.certificate.modules.ts_bas.model.codes.RekommendationVardeKod;
import se.inera.certificate.modules.ts_bas.model.codes.RekommendationsKod;
import se.inera.certificate.modules.ts_bas.model.codes.SpecialitetKod;
import se.inera.certificate.modules.ts_bas.model.codes.UtlatandeKod;
import se.inera.certificate.modules.ts_bas.model.codes.VardkontakttypKod;
import se.inera.certificate.modules.ts_bas.model.external.Aktivitet;
import se.inera.certificate.modules.ts_bas.model.external.HosPersonal;
import se.inera.certificate.modules.ts_bas.model.external.Observation;
import se.inera.certificate.modules.ts_bas.model.external.ObservationAktivitetRelation;
import se.inera.certificate.modules.ts_bas.model.external.Rekommendation;
import se.inera.certificate.modules.ts_bas.model.external.Utlatande;
import se.inera.certificate.modules.ts_bas.model.external.Vardenhet;
import se.inera.certificate.modules.ts_bas.model.external.Vardkontakt;
import se.inera.certificate.modules.ts_bas.model.internal.BedomningKorkortstyp;
import se.inera.certificate.modules.ts_bas.model.internal.HoSPersonal;
import se.inera.certificate.modules.ts_bas.model.internal.IntygAvserKategori;
import se.inera.certificate.modules.ts_bas.model.internal.Syn;

/**
 * Convert from {@link se.inera.certificate.modules.ts_bas.model.internal.Utlatande} to the external {@link Utlatande}
 * model.
 *
 * @author erik
 *
 */
public class InternalToExternalConverter {

    private static final Logger LOG = LoggerFactory.getLogger(InternalToExternalConverter.class);

    private static final String PERS_ID_ROOT = "1.2.752.129.2.1.3.1";

    // Since these IDs are only unique inside a given Utlatande, they are hereby fixed to following values
    private static final Id SYNFALTSPROVNING_ID = new Id("1.2.752.129.2.1.2.1", "1");
    private static final Id OGATS_RORLIGHET_ID = new Id("1.2.752.129.2.1.2.1", "2");
    private static final Id TECKEN_SYNFALTSDEFEKTER_ID = new Id("1.2.752.129.2.1.2.1", "3");
    private static final Id DIPLOPI_ID = new Id("1.2.752.129.2.1.2.1", "4");

    /**
     * Takes an internal Utlatande and converts it to the external model.
     *
     * @param source
     *            {@link se.inera.certificate.modules.ts_bas.model.internal.Utlatande}
     *
     * @return {@link Utlatande}, unless the source is null in which case a {@link ConverterException} is thrown
     *
     * @throws ConverterException
     */
    public Utlatande convert(se.inera.certificate.modules.ts_bas.model.internal.Utlatande source)
            throws ConverterException {
        LOG.trace("Converting internal model to external");

        if (source == null) {
            throw new ConverterException("Source utlatande was null, cannot convert");
        }

        Utlatande utlatande = new Utlatande();

        utlatande.setId(new Id("1.2.752.129.2.1.2.1", source.getId()));

        utlatande.setPatient(convertToExtPatient(source.getPatient()));
        utlatande.setSigneringsdatum(source.getSigneringsdatum());
        utlatande.setSkapadAv(convertToExtHosPersonal(source.getSkapadAv()));
        utlatande.setSkickatdatum(source.getSkickatdatum());
        utlatande.setTyp(CodeConverter.toKod(UtlatandeKod.TS_BAS_U06_V06));

        if (source.getKommentar() != null) {
            utlatande.getKommentarer().add(source.getKommentar());
        }

        utlatande.getAktiviteter().addAll(buildAktiviteter(source));
        utlatande.getObservationer().addAll(buildObservationer(source));
        utlatande.getRekommendationer().addAll(buildRekommendationer(source));
        utlatande.getObservationAktivitetRelationer().addAll(buildObsAktRelationer(source));
        utlatande.getIntygAvser().addAll(buildIntygAvser(source));
        utlatande.getVardkontakter().addAll(buildVardkontakt(source));

        return utlatande;
    }

    /**
     * Creates a List of Vardkontakt[er] from and Internal Utlatande.
     *
     * @param source
     *            {@link se.inera.certificate.modules.ts_bas.model.internal.Utlatande}
     * @return a List of {@link Vardkontakt}
     */
    private Collection<? extends Vardkontakt> buildVardkontakt(
            se.inera.certificate.modules.ts_bas.model.internal.Utlatande source) {

        List<Vardkontakt> vardkontakter = new ArrayList<Vardkontakt>();

        Vardkontakt vardkontakt = new Vardkontakt();

        vardkontakt.setIdkontroll(createIdKontrollKod(source.getVardkontakt().getIdkontroll()));

        vardkontakt.setVardkontakttyp(CodeConverter.toKod(VardkontakttypKod.MIN_UNDERSOKNING));

        vardkontakter.add(vardkontakt);
        return vardkontakter;
    }

    private Kod createIdKontrollKod(String code) {
        return CodeConverter.toKod(IdKontrollKod.valueOf(code));
    }

    /**
     * Creates a List of {@link Kod}[er] concerning what type of permissions this Utlatande concerns.
     *
     * @param source
     *            {@link se.inera.certificate.modules.ts_bas.model.internal.Utlatande}
     * @return List of {@link Kod}
     */
    private Collection<Kod> buildIntygAvser(
            se.inera.certificate.modules.ts_bas.model.internal.Utlatande source) {

        List<Kod> intygetAvser = new ArrayList<Kod>();
        for (IntygAvserKategori kategori : source.getIntygAvser().getKorkortstyp()) {
            intygetAvser.add(CodeConverter.toKod(IntygAvserKod.valueOf(kategori.name())));
        }
        return intygetAvser;
    }

    /**
     * Creates a List of ObservationAktivitetRelation (this will always consist of the same fixed relations).
     *
     * @param source
     *            {@link se.inera.certificate.modules.ts_bas.model.internal.Utlatande}
     * @return a List of {@link ObservationAktivitetRelation}
     */
    private Collection<ObservationAktivitetRelation> buildObsAktRelationer(
            se.inera.certificate.modules.ts_bas.model.internal.Utlatande source) {
        List<ObservationAktivitetRelation> relationer = new ArrayList<ObservationAktivitetRelation>();

        ObservationAktivitetRelation obsAkt1 = new ObservationAktivitetRelation();
        obsAkt1.setAktivitetsid(SYNFALTSPROVNING_ID);
        obsAkt1.setObservationsid(TECKEN_SYNFALTSDEFEKTER_ID);
        relationer.add(obsAkt1);

        ObservationAktivitetRelation obsAkt2 = new ObservationAktivitetRelation();
        obsAkt2.setAktivitetsid(OGATS_RORLIGHET_ID);
        obsAkt2.setObservationsid(DIPLOPI_ID);
        relationer.add(obsAkt2);

        return relationer;
    }

    /**
     * Creates a List of Rekommendation[er].
     *
     * @param source
     *            {@link se.inera.certificate.modules.ts_bas.model.internal.Utlatande}
     * @return a List of {@link Rekommendation}
     */
    private Collection<Rekommendation> buildRekommendationer(
            se.inera.certificate.modules.ts_bas.model.internal.Utlatande source) {
        List<Rekommendation> rekommendationer = new ArrayList<Rekommendation>();

        Rekommendation uppfyllerKrav = new Rekommendation();

        List<Kod> values = new ArrayList<Kod>();

        // Get all BedomningKorkortsTyp and convert them to Kod, unless KanInteTaStallning is set ..
        if (source.getBedomning().getKanInteTaStallning() == null) {
            for (BedomningKorkortstyp bedomning : source.getBedomning().getKorkortstyp()) {
                values.add(CodeConverter.toKod(RekommendationVardeKod.valueOf(bedomning.name())));
            }
        } else {
            // .. in that case, just add Rekommendation Kan inte ta ställning
            values.add(CodeConverter.toKod(RekommendationVardeKod.INTE_TA_STALLNING));
        }
        uppfyllerKrav.getVarde().addAll(values);
        uppfyllerKrav.setRekommendationskod(CodeConverter.toKod(RekommendationsKod.PATIENT_UPPFYLLER_KRAV_FOR));
        rekommendationer.add(uppfyllerKrav);

        // If LakareSpecialKompetens is set, create Rekommendation for that
        if (source.getBedomning().getLakareSpecialKompetens() != null) {
            if (!source.getBedomning().getLakareSpecialKompetens().isEmpty()) {
                Rekommendation specialist = new Rekommendation();
                specialist.setRekommendationskod(CodeConverter
                        .toKod(RekommendationsKod.PATIENT_BOR_UNDESOKAS_AV_SPECIALIST));
                specialist.setBeskrivning(source.getBedomning().getLakareSpecialKompetens());
                rekommendationer.add(specialist);
            }
        }

        return rekommendationer;
    }

    /**
     * Create a List of {@link Observation}[er] from internal model.
     *
     * @param source
     *            {@link se.inera.certificate.modules.ts_bas.model.internal.Utlatande}
     * @return a List of {@link Observation}
     */
    private Collection<Observation> buildObservationer(
            se.inera.certificate.modules.ts_bas.model.internal.Utlatande source) {
        List<Observation> observationer = new ArrayList<Observation>();

        // Tecken på synfältsdefekter
        observationer.add(createObservationWithId(ObservationsKod.SYNFALTSDEFEKTER, source.getSyn()
                .getSynfaltsdefekter(), TECKEN_SYNFALTSDEFEKTER_ID));

        // Begränsning av seende vid nedsatt belysning
        observationer.add(createBasicObservation(ObservationsKod.NATTBLINDHET, source.getSyn().getNattblindhet()));

        // Progressiv ögonsjukdom
        observationer.add(createBasicObservation(ObservationsKod.PROGRESIV_OGONSJUKDOM, source.getSyn()
                .getProgressivOgonsjukdom()));

        // Dubbelseende(diplopi)
        observationer.add(createObservationWithId(ObservationsKod.DIPLOPI, source.getSyn().getDiplopi(), DIPLOPI_ID));

        // Nystagmus
        observationer.add(createBasicObservation(ObservationsKod.NYSTAGMUS_MM, source.getSyn().getNystagmus()));

        // Syn-related observations (utan korrektion and med korrektion)
        observationer.addAll(createSynRelatedObservations(source.getSyn()));

        // Kontaktlinser
        observationer.addAll(createKontaktlinser(source.getSyn()));

        // Överraskande anfall av balansrubbning eller yrsel
        observationer.add(createBasicObservation(ObservationsKod.ANFALL_BALANSRUBBNING_YRSEL, source.getHorselBalans()
                .getBalansrubbningar()));

        // Sjukdom eller Funktionsnedsättning som påverkar rörligheten
        observationer
                .add(createObservationWithBeskrivning(ObservationsKod.FORSAMRAD_RORLIGHET_FRAMFORA_FORDON, source
                        .getFunktionsnedsattning().getFunktionsnedsattning(), source.getFunktionsnedsattning()
                        .getBeskrivning()));

        // Hjärt- kärlsjukdom som ökar risk för hjärnskada och trafikrisk
        observationer.add(createBasicObservation(ObservationsKod.HJART_KARLSJUKDOM_TRAFIKSAKERHETSRISK, source
                .getHjartKarl().getHjartKarlSjukdom()));

        // Tecken på hjänskada / stroke
        observationer.add(createBasicObservation(ObservationsKod.TECKEN_PA_HJARNSKADA, source.getHjartKarl()
                .getHjarnskadaEfterTrauma()));

        // Riskfaktorer för stroke
        observationer.add(createObservationWithBeskrivning(ObservationsKod.RISKFAKTORER_STROKE, source.getHjartKarl()
                .getRiskfaktorerStroke(), source.getHjartKarl().getBeskrivningRiskfaktorer()));

        // Har diabetes?
        observationer.add(createBasicObservation(ObservationsKod.HAR_DIABETES, source.getDiabetes().getHarDiabetes()));

        // Tecken på neurologisk sjukdom
        observationer.add(createBasicObservation(ObservationsKod.TECKEN_PA_NEUROLOGISK_SJUKDOM, source.getNeurologi()
                .getNeurologiskSjukdom()));

        // Epilepsi eller annan medvetandestörning
        observationer.add(createObservationWithBeskrivning(ObservationsKod.EPILEPSI, source.getMedvetandestorning()
                .getMedvetandestorning(), source.getMedvetandestorning().getBeskrivning()));

        // Nedsatt njurfunktion
        observationer.add(createBasicObservation(ObservationsKod.NEDSATT_NJURFUNKTION_TRAFIKSAKERHETSRISK, source
                .getNjurar().getNedsattNjurfunktion()));

        // Tecken på sviktande kognitiv funktion
        observationer.add(createBasicObservation(ObservationsKod.SVIKTANDE_KOGNITIV_FUNKTION, source.getKognitivt()
                .getSviktandeKognitivFunktion()));

        // Tecken på sömn eller vakenhetsstörningar
        observationer.add(createBasicObservation(ObservationsKod.SOMN_VAKENHETSSTORNING, source.getSomnVakenhet()
                .getTeckenSomnstorningar()));

        // Tecken på missbruk
        observationer.add(createBasicObservation(ObservationsKod.TECKEN_PA_MISSBRUK, source.getNarkotikaLakemedel()
                .getTeckenMissbruk()));

        // Regelbundet ordinerat läkemedelsbruk som kan medföra trafikfara
        observationer.add(createObservationWithBeskrivning(ObservationsKod.LAKEMEDELSANVANDNING_TRAFIKSAKERHETSRISK,
                source.getNarkotikaLakemedel().getLakarordineratLakemedelsbruk(), source.getNarkotikaLakemedel()
                        .getLakemedelOchDos()));

        // Psykisk sjukdom
        observationer.add(createBasicObservation(ObservationsKod.PSYKISK_SJUKDOM, source.getPsykiskt()
                .getPsykiskSjukdom()));

        // Psykisk utvecklingsstörning
        observationer.add(createBasicObservation(ObservationsKod.PSYKISK_UTVECKLINGSSTORNING, source
                .getUtvecklingsstorning().getPsykiskUtvecklingsstorning()));

        // DAMP ADHD Tourettes
        observationer.add(createBasicObservation(ObservationsKod.ADHD_DAMP_MM, source.getUtvecklingsstorning()
                .getHarSyndrom()));

        // Stadigvarande medicinering
        observationer.add(createObservationWithBeskrivning(ObservationsKod.STADIGVARANDE_MEDICINERING, source
                .getMedicinering().getStadigvarandeMedicinering(), source.getMedicinering().getBeskrivning()));

        // END MANDATORY OBSERVATIONs

        // BEGIN Check optional Observations
        // Svårighet uppfatta samtal på 4 meters avstånd
        if (source.getHorselBalans().getSvartUppfattaSamtal4Meter() != null) {
            observationer.add(createBasicObservation(ObservationsKod.SVARIGHET_SAMTAL_4M, source.getHorselBalans()
                    .getSvartUppfattaSamtal4Meter()));
        }

        // Otillräcklig rörelseförmåga att hjälpa passagerare...
        if (source.getFunktionsnedsattning().getOtillrackligRorelseformaga() != null) {

            observationer.add(createBasicObservation(ObservationsKod.FORSAMRAD_RORLIGHET_HJALPA_PASSAGERARE, source
                    .getFunktionsnedsattning().getOtillrackligRorelseformaga()));
        }

        // Diabetes typ
        if (source.getDiabetes().getDiabetesTyp() != null) {
            observationer.add(createBasicObservation(ObservationsKod.valueOf(source.getDiabetes().getDiabetesTyp()),
                    true));
        }

        // Diabetes behandlingar
        if (source.getDiabetes().getInsulin() != null) {
            observationer.add(createBasicObservation(ObservationsKod.DIABETIKER_INSULINBEHANDLING, source.getDiabetes()
                    .getInsulin()));
        }
        if (source.getDiabetes().getKost() != null) {
            observationer.add(createBasicObservation(ObservationsKod.DIABETIKER_KOSTBEHANDLING, source.getDiabetes()
                    .getKost()));
        }
        if (source.getDiabetes().getTabletter() != null) {
            observationer.add(createBasicObservation(ObservationsKod.DIABETIKER_TABLETTBEHANDLING, source.getDiabetes()
                    .getTabletter()));
        }

        return observationer;
    }

    /**
     * Creates a List of Observation for each Kontaktlinser-observation.
     *
     * @param syn
     *            {@link Syn} object with data to be used
     * @return a List of {@link Observation}[er]
     */
    private List<Observation> createKontaktlinser(Syn syn) {
        List<Observation> obs = new ArrayList<Observation>();
        Observation hoger = new Observation();
        Observation vanster = new Observation();

        hoger.setObservationskod(CodeConverter.toKod(ObservationsKod.KONTAKTLINSER));
        hoger.setLateralitet(CodeConverter.toKod(LateralitetsKod.HOGER));
        hoger.setForekomst(syn.getHogerOga().getKontaktlins());

        vanster.setObservationskod(CodeConverter.toKod(ObservationsKod.KONTAKTLINSER));
        vanster.setLateralitet(CodeConverter.toKod(LateralitetsKod.VANSTER));
        vanster.setForekomst(syn.getHogerOga().getKontaktlins());

        obs.add(hoger);
        obs.add(vanster);
        return obs;
    }

    /**
     * Creates a List with Observation[er] for right, left and both eyes without visual aid (utan korrektion) and with
     * aid (if applicable).
     *
     * @param syn
     *            {@link Syn} object with data to be used
     * @return a List of {@link Observation}[er]
     */
    private List<Observation> createSynRelatedObservations(Syn syn) {
        List<Observation> obs = new ArrayList<Observation>();

        // Create mandatory observations without corrective aid
        Observation hogerUtan = new Observation();
        Observation vansterUtan = new Observation();
        Observation binokulartUtan = new Observation();

        hogerUtan.setObservationskod(CodeConverter.toKod(ObservationsKod.EJ_KORRIGERAD_SYNSKARPA));
        hogerUtan.setLateralitet(CodeConverter.toKod(LateralitetsKod.HOGER));
        hogerUtan.getVarde().add(new PhysicalQuantity(syn.getHogerOga().getUtanKorrektion(), null));

        vansterUtan.setObservationskod(CodeConverter.toKod(ObservationsKod.EJ_KORRIGERAD_SYNSKARPA));
        vansterUtan.setLateralitet(CodeConverter.toKod(LateralitetsKod.VANSTER));
        vansterUtan.getVarde().add(new PhysicalQuantity(syn.getHogerOga().getUtanKorrektion(), null));

        binokulartUtan.setObservationskod(CodeConverter.toKod(ObservationsKod.EJ_KORRIGERAD_SYNSKARPA));
        binokulartUtan.setLateralitet(CodeConverter.toKod(LateralitetsKod.BINOKULART));
        binokulartUtan.getVarde().add(new PhysicalQuantity(syn.getHogerOga().getUtanKorrektion(), null));

        obs.add(hogerUtan);
        obs.add(vansterUtan);
        obs.add(binokulartUtan);

        // Create optional corrected observations
        if (syn.getHogerOga().getMedKorrektion() != null) {
            Observation hogerMed = new Observation();
            hogerMed.setObservationskod(CodeConverter.toKod(ObservationsKod.KORRIGERAD_SYNSKARPA));
            hogerMed.setLateralitet(CodeConverter.toKod(LateralitetsKod.HOGER));
            hogerMed.getVarde().add(new PhysicalQuantity(syn.getHogerOga().getMedKorrektion(), null));
            obs.add(hogerMed);
        }
        if (syn.getVansterOga().getMedKorrektion() != null) {
            Observation vansterMed = new Observation();
            vansterMed.setObservationskod(CodeConverter.toKod(ObservationsKod.KORRIGERAD_SYNSKARPA));
            vansterMed.setLateralitet(CodeConverter.toKod(LateralitetsKod.VANSTER));
            vansterMed.getVarde().add(new PhysicalQuantity(syn.getVansterOga().getMedKorrektion(), null));
            obs.add(vansterMed);
        }
        if (syn.getBinokulart().getMedKorrektion() != null) {
            Observation binokulartMed = new Observation();
            binokulartMed.setObservationskod(CodeConverter.toKod(ObservationsKod.KORRIGERAD_SYNSKARPA));
            binokulartMed.setLateralitet(CodeConverter.toKod(LateralitetsKod.BINOKULART));
            binokulartMed.getVarde().add(new PhysicalQuantity(syn.getHogerOga().getMedKorrektion(), null));
            obs.add(binokulartMed);
        }

        return obs;
    }

    /**
     * Create Observation with ObservationsKod, forekomst and beskrivning.
     *
     * @param obsKod
     *            {@link ObservationsKod}
     * @param forekomst
     *            {@link Boolean}
     * @param beskrivning
     *            {@link String}
     * @return An {@link Observation} constructed from the parameters provided
     */
    private Observation createObservationWithBeskrivning(ObservationsKod obsKod, Boolean forekomst, String beskrivning) {
        Observation obs = new Observation();
        obs.setObservationskod(CodeConverter.toKod(obsKod));
        obs.setForekomst(forekomst);
        obs.setBeskrivning(beskrivning);
        return obs;
    }

    /**
     * Create an Observation with Id, ObservationsKod and Boolean (forekomst).
     *
     * @param id
     *            String with the {@link Id} extension
     * @param obsKod
     *            {@link ObservationsKod}
     * @param forekomst
     *            {@link Boolean}
     * @return An {@link Observation} constructed from the parameters provided
     */
    private Observation createObservationWithId(ObservationsKod obsKod, Boolean forekomst, Id id) {
        Observation obs = new Observation();

        obs.setId(id);
        obs.setObservationskod(CodeConverter.toKod(obsKod));
        obs.setForekomst(forekomst);
        return obs;
    }

    /**
     * Create a "basic" observation i.e an observation consisting of only an ObservationsKod and a boolean.
     *
     * @param obsKod
     *            {@link ObservationsKod}
     * @param forekomst
     *            {@link Boolean}
     * @return An {@link Observation} constructed from the parameters provided
     */
    private Observation createBasicObservation(ObservationsKod obsKod, Boolean forekomst) {
        Observation obs = new Observation();
        obs.setObservationskod(CodeConverter.toKod(obsKod));
        obs.setForekomst(forekomst);
        return obs;
    }

    /**
     * Create mandatory Aktivitet[er] from internal objects.
     *
     * @param source
     *            {@link se.inera.certificate.modules.ts_bas.model.internal.Utlatande}
     * @return List of {@link Aktivitet}
     */
    private Collection<Aktivitet> buildAktiviteter(
            se.inera.certificate.modules.ts_bas.model.internal.Utlatande source) {
        List<Aktivitet> aktiviteter = new ArrayList<Aktivitet>();

        // Create mandatory Aktivitet[er]
        // Synfältsprövning
        Aktivitet synprovning = new Aktivitet();
        synprovning.setAktivitetskod(CodeConverter.toKod(AktivitetKod.SYNFALTSUNDERSOKNING));
        synprovning.setMetod(CodeConverter.toKod(MetodKod.DONDERS_KONFRONTATIONSMETOD));
        synprovning.setId(SYNFALTSPROVNING_ID);
        aktiviteter.add(synprovning);

        // Prövning av ögats rörlighet
        Aktivitet ogatsRorlighet = new Aktivitet();
        ogatsRorlighet.setId(OGATS_RORLIGHET_ID);
        ogatsRorlighet.setAktivitetskod(CodeConverter.toKod(AktivitetKod.PROVNING_AV_OGATS_RORLIGHET));
        aktiviteter.add(ogatsRorlighet);

        // Vårdinsats för missbruk etc
        Aktivitet vardMissbruk = new Aktivitet();
        vardMissbruk.setAktivitetskod(CodeConverter.toKod(AktivitetKod.VARDINSATS_MISSBRUK_BEROENDE));
        vardMissbruk.setForekomst(source.getNarkotikaLakemedel().getForemalForVardinsats());
        aktiviteter.add(vardMissbruk);

        // Vård på sjukhus
        Aktivitet vardSjukhus = new Aktivitet();
        vardSjukhus.setAktivitetskod(CodeConverter.toKod(AktivitetKod.VARD_PA_SJUKHUS));
        vardSjukhus.setForekomst(source.getSjukhusvard().getSjukhusEllerLakarkontakt());
        vardSjukhus.setOstruktureradTid(source.getSjukhusvard().getTidpunkt());
        vardSjukhus.setPlats(source.getSjukhusvard().getVardinrattning());
        vardSjukhus.setBeskrivning(source.getSjukhusvard().getAnledning());
        aktiviteter.add(vardSjukhus);

        if (source.getSyn().getKorrektionsglasensStyrka() != null) {
            Aktivitet korrektion8dioptrier = new Aktivitet();
            korrektion8dioptrier.setAktivitetskod(CodeConverter.toKod(AktivitetKod.UNDERSOKNING_PLUS8_KORREKTIONSGRAD));
            korrektion8dioptrier.setForekomst(source.getSyn().getKorrektionsglasensStyrka());
            aktiviteter.add(korrektion8dioptrier);
        }

        if (source.getNarkotikaLakemedel().getProvtagningBehovs() != null) {
            Aktivitet provtagningNark = new Aktivitet();
            provtagningNark.setAktivitetskod(CodeConverter.toKod(AktivitetKod.PROVTAGNING_ALKOHOL_NARKOTIKA));
            provtagningNark.setForekomst(source.getNarkotikaLakemedel().getProvtagningBehovs());
            aktiviteter.add(provtagningNark);
        }

        return aktiviteter;
    }

    /**
     * Convert from internal to external Patient.
     *
     * @param source
     *            {@link se.inera.certificate.modules.ts_bas.model.internal.Patient}
     * @return external {@link Patient}
     */
    private Patient convertToExtPatient(se.inera.certificate.modules.ts_bas.model.internal.Patient source) {
        Patient patient = new Patient();
        patient.setEfternamn(source.getEfternamn());
        patient.getFornamn().add(source.getFornamn());
        patient.setId(new Id(PERS_ID_ROOT, source.getPersonid()));
        patient.setPostadress(source.getPostadress());
        patient.setPostnummer(source.getPostnummer());
        patient.setPostort(source.getPostort());

        return patient;
    }

    /**
     * Convert from internal to external HosPersonal.
     *
     * @param source
     *            internal {@link HoSPersonal}
     * @return external {@link HosPersonal}
     */
    private HosPersonal convertToExtHosPersonal(HoSPersonal source) {
        HosPersonal hosPersonal = new HosPersonal();
        hosPersonal.setId(new Id(HSpersonalKod.HSA_ID.getCodeSystem(), source.getPersonid()));
        hosPersonal.setNamn(source.getFullstandigtNamn());
        hosPersonal.setVardenhet(convertToExtVardenhet(source.getVardenhet()));
        hosPersonal.getBefattningar().addAll(source.getBefattningar());
        hosPersonal.getSpecialiteter().addAll(convertStringToCode(SpecialitetKod.class, source.getSpecialiteter()));

        return hosPersonal;
    }

    /**
     * Convert a String-representation (i.e the name of the enum constant representing that particular Kod) to a Kod
     * object.
     *
     * @param type
     *            the code enum (must extend {@link CodeSystem})
     * @param strings
     *            a list of Strings with the names of enum constants to convert
     * @return a list of {@link Kod}
     */
    private <E extends CodeSystem> Collection<Kod> convertStringToCode(Class<E> type, List<String> strings) {
        List<Kod> koder = new ArrayList<>();
        for (E enumValue : type.getEnumConstants()) {
            if (strings.contains(enumValue.toString())) {
                koder.add(CodeConverter.toKod(enumValue));
            }
        }

        return koder;
    }

    /**
     * Convert from internal to external Vardenhet.
     *
     * @param source
     *            {@link se.inera.certificate.modules.ts_bas.model.internal.Vardenhet}
     * @return external {@link Vardenhet}
     */
    private Vardenhet convertToExtVardenhet(se.inera.certificate.modules.ts_bas.model.internal.Vardenhet source) {
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setId(new Id(HSpersonalKod.HSA_ID.getCodeSystem(), source.getEnhetsid()));
        vardenhet.setNamn(source.getEnhetsnamn());
        vardenhet.setPostadress(source.getPostadress());
        vardenhet.setPostnummer(source.getPostnummer());
        vardenhet.setPostort(source.getPostort());
        vardenhet.setTelefonnummer(source.getTelefonnummer());
        vardenhet.setEpost(source.getEpost());
        vardenhet.setVardgivare(convertToExtVardgivare(source.getVardgivare()));
        return vardenhet;
    }

    /**
     * Convert from internal to external Vardenhet.
     *
     * @param source
     *            {@link se.inera.certificate.modules.ts_bas.model.internal.Vardgivare}
     * @return external {@link Vardgivare}
     */
    private Vardgivare convertToExtVardgivare(se.inera.certificate.modules.ts_bas.model.internal.Vardgivare source) {
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setId(new Id(HSpersonalKod.HSA_ID.getCodeSystem(), source.getVardgivarid()));
        vardgivare.setNamn(source.getVardgivarnamn());
        return vardgivare;
    }

}
