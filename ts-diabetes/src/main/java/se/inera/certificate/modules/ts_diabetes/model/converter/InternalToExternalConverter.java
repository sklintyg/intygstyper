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
package se.inera.certificate.modules.ts_diabetes.model.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.certificate.model.Id;
import se.inera.certificate.model.Kod;
import se.inera.certificate.model.PartialInterval;
import se.inera.certificate.model.Patient;
import se.inera.certificate.model.PhysicalQuantity;
import se.inera.certificate.model.Vardenhet;
import se.inera.certificate.model.Vardgivare;
import se.inera.certificate.modules.ts_diabetes.model.codes.AktivitetKod;
import se.inera.certificate.modules.ts_diabetes.model.codes.BefattningKod;
import se.inera.certificate.modules.ts_diabetes.model.codes.BilagaKod;
import se.inera.certificate.modules.ts_diabetes.model.codes.CodeConverter;
import se.inera.certificate.modules.ts_diabetes.model.codes.CodeSystem;
import se.inera.certificate.modules.ts_diabetes.model.codes.HSpersonalKod;
import se.inera.certificate.modules.ts_diabetes.model.codes.IdKontrollKod;
import se.inera.certificate.modules.ts_diabetes.model.codes.IntygAvserKod;
import se.inera.certificate.modules.ts_diabetes.model.codes.LateralitetsKod;
import se.inera.certificate.modules.ts_diabetes.model.codes.MetodKod;
import se.inera.certificate.modules.ts_diabetes.model.codes.ObservationsKod;
import se.inera.certificate.modules.ts_diabetes.model.codes.RekommendationVardeKod;
import se.inera.certificate.modules.ts_diabetes.model.codes.RekommendationsKod;
import se.inera.certificate.modules.ts_diabetes.model.codes.UtlatandeKod;
import se.inera.certificate.modules.ts_diabetes.model.codes.VardkontakttypKod;
import se.inera.certificate.modules.ts_diabetes.model.external.Aktivitet;
import se.inera.certificate.modules.ts_diabetes.model.external.Bilaga;
import se.inera.certificate.modules.ts_diabetes.model.external.HosPersonal;
import se.inera.certificate.modules.ts_diabetes.model.external.Observation;
import se.inera.certificate.modules.ts_diabetes.model.external.ObservationAktivitetRelation;
import se.inera.certificate.modules.ts_diabetes.model.external.Rekommendation;
import se.inera.certificate.modules.ts_diabetes.model.external.Utlatande;
import se.inera.certificate.modules.ts_diabetes.model.external.Vardkontakt;
import se.inera.certificate.modules.ts_diabetes.model.internal.BedomningKorkortstyp;
import se.inera.certificate.modules.ts_diabetes.model.internal.HoSPersonal;
import se.inera.certificate.modules.ts_diabetes.model.internal.IntygAvserKategori;
import se.inera.certificate.modules.ts_diabetes.model.internal.Syn;

public class InternalToExternalConverter {

    private static final Logger LOG = LoggerFactory.getLogger(InternalToExternalConverter.class);

    private static final String PERS_ID_ROOT = "1.2.752.129.2.1.3.1";

    // Since these IDs are only unique inside a given Utlatande, they are hereby fixed to following values
    private static final Id SYNFALTSPROVNING_ID = new Id("1.2.752.129.2.1.2.1", "1");
    private static final Id OGATS_RORLIGHET_ID = new Id("1.2.752.129.2.1.2.1", "2");
    private static final Id UTAN_ANMARKNING_ID = new Id("1.2.752.129.2.1.2.1", "3");
    private static final Id DIPLOPI_ID = new Id("1.2.752.129.2.1.2.1", "4");

    /**
     * Takes an internal Utlatande and converts it to the external model
     * 
     * @param source
     *            {@link se.inera.certificate.modules.ts_diabetes.model.internal.Utlatande}
     * 
     * @return {@link Utlatande}, unless the source is null in which case a {@link ConverterException} is thrown
     * 
     * @throws ConverterException
     */
    public Utlatande convert(se.inera.certificate.modules.ts_diabetes.model.internal.Utlatande source)
            throws ConverterException {
        LOG.trace("Converting internal model to external");

        if (source == null) {
            throw new ConverterException("Source utlatande was null, cannot convert");
        }

        Utlatande utlatande = new Utlatande();

        // TODO: Where does the codeSystem come from?! (i.e "1.2.752.129.2.1.2.1")
        utlatande.setId(new Id("1.2.752.129.2.1.2.1", source.getUtlatandeid()));

        utlatande.setPatient(convertToExtPatient(source.getPatient()));
        utlatande.setSigneringsdatum(source.getSigneringsdatum());
        utlatande.setSkapadAv(convertToExtHosPersonal(source.getSkapadAv()));
        utlatande.setSkickatdatum(source.getSkickatdatum());
        utlatande.setTyp(CodeConverter.toKod(UtlatandeKod.TS_DIABETES));

        if (source.getKommentar() != null) {
            utlatande.getKommentarer().add(source.getKommentar());
        }

        // Since Aktiviteter are dependent on there being a Syn object present...
        if (!source.getSyn().getSeparatOgonlakarintyg()) {
            utlatande.getAktiviteter().addAll(buildAktiviteter(source));
            utlatande.getObservationAktivitetRelationer().addAll(buildObsAktRelationer(source));
        }

        utlatande.setBilaga(createBilaga(source.getSyn().getSeparatOgonlakarintyg()));

        utlatande.getObservationer().addAll(buildObservationer(source));
        utlatande.getRekommendationer().addAll(buildRekommendationer(source));

        utlatande.getIntygAvser().addAll(buildIntygAvser(source));
        utlatande.getVardkontakter().addAll(buildVardkontakt(source));

        return utlatande;
    }

    // Since there's only one type of bilaga at this time...
    private Bilaga createBilaga(Boolean forekomst) {
        Bilaga bilaga = new Bilaga();
        bilaga.setBilagetyp(CodeConverter.toKod(BilagaKod.OGONLAKARINTYG));
        bilaga.setForekomst(forekomst);
        return bilaga;
    }

    /**
     * Creates a List of Vardkontakt[er] from and Internal Utlatande
     * 
     * @param source
     *            {@link se.inera.certificate.modules.ts_diabetes.model.internal.Utlatande}
     * @return a List of {@link Vardkontakt}
     */
    private Collection<? extends Vardkontakt> buildVardkontakt(
            se.inera.certificate.modules.ts_diabetes.model.internal.Utlatande source) {

        List<Vardkontakt> vardkontakter = new ArrayList<Vardkontakt>();
        Vardkontakt vardkontakt = new Vardkontakt();
        
        vardkontakt.setIdkontroll(createIdKontrollKod(source.getVardkontakt().getIdkontroll()));
        
        vardkontakt.setVardkontakttyp(CodeConverter.toKod(VardkontakttypKod.MIN_UNDERSOKNING));
        vardkontakter.add(vardkontakt);

        return vardkontakter;
    }

    // TODO: There might be a better way to do this..
    private Kod createIdKontrollKod(String code) {
        return CodeConverter.toKod(IdKontrollKod.valueOf(code));
    }


    /**
     * Creates a List of {@link Kod}[er] concerning what type of permissions this Utlatande concerns
     * 
     * @param source
     *            {@link se.inera.certificate.modules.ts_diabetes.model.internal.Utlatande}
     * @return List of {@link Kod}
     */
    private Collection<? extends Kod> buildIntygAvser(
            se.inera.certificate.modules.ts_diabetes.model.internal.Utlatande source) {
        List<Kod> intygetAvser = new ArrayList<Kod>();
        for (IntygAvserKategori kategori : source.getIntygAvser().getKorkortstyp()) {
            intygetAvser.add(CodeConverter.toKod(IntygAvserKod.valueOf(kategori.name())));
        }

        return intygetAvser;
    }

    /**
     * Creates a List of ObservationAktivitetRelation (this will always consist of the same fixed relations)
     * 
     * @param source
     *            {@link se.inera.certificate.modules.ts_diabetes.model.internal.Utlatande}
     * @return a List of {@link ObservationAktivitetRelation}
     */
    private Collection<? extends ObservationAktivitetRelation> buildObsAktRelationer(
            se.inera.certificate.modules.ts_diabetes.model.internal.Utlatande source) {
        List<ObservationAktivitetRelation> relationer = new ArrayList<ObservationAktivitetRelation>();

        ObservationAktivitetRelation obsAkt1 = new ObservationAktivitetRelation();
        obsAkt1.setAktivitetsid(SYNFALTSPROVNING_ID);
        obsAkt1.setObservationsid(UTAN_ANMARKNING_ID);
        relationer.add(obsAkt1);

        ObservationAktivitetRelation obsAkt2 = new ObservationAktivitetRelation();
        obsAkt2.setAktivitetsid(OGATS_RORLIGHET_ID);
        obsAkt2.setObservationsid(DIPLOPI_ID);
        relationer.add(obsAkt2);

        return relationer;
    }

    /**
     * Creates a List of Rekommendation[er]
     * 
     * @param source
     *            {@link se.inera.certificate.modules.ts_diabetes.model.internal.Utlatande}
     * @return a List of {@link Rekommendation}
     */
    private Collection<? extends Rekommendation> buildRekommendationer(
            se.inera.certificate.modules.ts_diabetes.model.internal.Utlatande source) {
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

        if (source.getBedomning().getLamplighetInnehaBehorighet() != null) {
            Rekommendation lamplighet = new Rekommendation();
            lamplighet.setRekommendationskod(CodeConverter
                    .toKod(RekommendationsKod.LAMPLIGHET_INNEHA_BEHORIGHET_TILL_KORNINGAR_OCH_ARBETSFORMER));
            lamplighet.setBoolean_varde(source.getBedomning().getLamplighetInnehaBehorighet());
            rekommendationer.add(lamplighet);
        }

        return rekommendationer;
    }

    /**
     * Create a List of {@link Observation}[er] from internal model
     * 
     * @param source
     *            {@link se.inera.certificate.modules.ts_diabetes.model.internal.Utlatande}
     * @return a List of {@link Observation}
     */
    private Collection<? extends Observation> buildObservationer(
            se.inera.certificate.modules.ts_diabetes.model.internal.Utlatande source) {
        List<Observation> observationer = new ArrayList<Observation>();

        // Create Diabetes
        if (source.getDiabetes().getDiabetestyp() != null) {
            Observation diabetes = new Observation();

            diabetes.setObservationskod(CodeConverter.toKod(ObservationsKod.valueOf(source.getDiabetes().getDiabetestyp())));
            diabetes.setForekomst(true);

            //We are only interested in from, hence tom is set to null
            if (source.getDiabetes().getObservationsperiod() != null) {
                diabetes.setObservationsperiod(new PartialInterval(PartialConverter.stringToPartial(source.getDiabetes()
                    .getObservationsperiod()), null));
            }
            
            observationer.add(diabetes);
        }

        // Diabetes behandlingar
        if (source.getDiabetes().getInsulin() != null) {
            Observation insulin = new Observation();
            insulin.setObservationskod(CodeConverter.toKod(ObservationsKod.DIABETIKER_INSULINBEHANDLING));
            insulin.setForekomst(source.getDiabetes().getInsulin());
            
            //We are only interested in from, hence tom is set to null
            if (source.getDiabetes().getInsulinBehandlingsperiod() != null) {
                insulin.setObservationsperiod(new PartialInterval(PartialConverter.stringToPartial(source.getDiabetes()
                    .getInsulinBehandlingsperiod()), null));
            }
            
            observationer.add(insulin);
        }

        if (source.getDiabetes().getEndastKost() != null) {
            observationer.add(createBasicObservation(ObservationsKod.DIABETIKER_ENBART_KOST, source.getDiabetes()
                    .getEndastKost()));
        }

        if (source.getDiabetes().getTabletter() != null) {
            observationer.add(createBasicObservation(ObservationsKod.DIABETIKER_TABLETTBEHANDLING, source.getDiabetes()
                    .getTabletter()));
        }

        if (source.getDiabetes().getAnnanBehandling() != null) {
            observationer.add(createObservationWithBeskrivning(ObservationsKod.DIABETIKER_ANNAN_BEHANDLING, source
                    .getDiabetes().getAnnanBehandling(), source.getDiabetes().getAnnanBehandlingBeskrivning()));
        }

        // Hypoglykemi observationer
        if (source.getHypoglykemier().getKunskapOmAtgarder() != null) {
            observationer.add(createBasicObservation(ObservationsKod.KUNSKAP_ATGARD_HYPOGLYKEMI, source
                    .getHypoglykemier().getKunskapOmAtgarder()));
        }

        if (source.getHypoglykemier().getTeckenNedsattHjarnfunktion() != null) {
            observationer.add(createBasicObservation(ObservationsKod.HYPOGLYKEMIER_MED_TECKEN_PA_NEDSATT_HJARNFUNKTION,
                    source.getHypoglykemier().getTeckenNedsattHjarnfunktion()));
        }

        if (source.getHypoglykemier().getSaknarFormagaKannaVarningstecken() != null) {
            observationer.add(createBasicObservation(ObservationsKod.SAKNAR_FORMAGA_KANNA_HYPOGLYKEMI, source
                    .getHypoglykemier().getSaknarFormagaKannaVarningstecken()));
        }

        if (source.getHypoglykemier().getAllvarligForekomst() != null) {
            observationer.add(createObservationWithBeskrivning(ObservationsKod.ALLVARLIG_HYPOGLYKEMI, source
                    .getHypoglykemier().getAllvarligForekomst(), source.getHypoglykemier()
                    .getAllvarligForekomstBeskrivning()));
        }

        if (source.getHypoglykemier().getAllvarligForekomstTrafiken() != null) {
            observationer.add(createObservationWithBeskrivning(ObservationsKod.ALLVARLIG_HYPOGLYKEMI_I_TRAFIKEN, source
                    .getHypoglykemier().getAllvarligForekomstTrafiken(), source.getHypoglykemier()
                    .getAllvarligForekomstTrafikBeskrivning()));
        }

        if (source.getHypoglykemier().getAllvarligForekomstVakenTid() != null) {
            Observation hypoglykemiVakenTid = new Observation();
            hypoglykemiVakenTid.setObservationskod(CodeConverter
                    .toKod(ObservationsKod.ALLVARLIG_HYPOGLYKEMI_VAKET_TILLSTAND));
            hypoglykemiVakenTid.setForekomst(source.getHypoglykemier().getAllvarligForekomstVakenTid());

            hypoglykemiVakenTid.setObservationstid(createLocalDateFromString(source.getHypoglykemier()
                    .getAllvarligForekomstVakenTidObservationstid()));

            observationer.add(hypoglykemiVakenTid);
        }
        // Syn-related observations
        if (source.getSyn() != null) {
            observationer.addAll(createSynRelatedObservations(source.getSyn()));
        }

        return observationer;
    }

    private LocalDate createLocalDateFromString(String timeString) {
        LocalDate localDate = LocalDate.parse(timeString);
        return localDate;
    }

    /**
     * Creates a List with Observation[er] for right, left and both eyes without visual aid (utan korrektion) and with
     * aid (if applicable)
     * 
     * @param syn
     *            {@link Syn} object with data to be used
     * @return a List of {@link Observation}[er]
     */
    private List<Observation> createSynRelatedObservations(Syn syn) {
        List<Observation> obs = new ArrayList<Observation>();

        // Dubbelseende(diplopi)
        if (syn.getDiplopi() != null) {
            obs.add(createObservationWithId(ObservationsKod.DIPLOPI, syn.getDiplopi(), DIPLOPI_ID));
        }

        // Utan anmärkning?
        if (syn.getSynfaltsprovningUtanAnmarkning() != null) {
            Observation utanAnmarkning = new Observation();
            utanAnmarkning.setObservationskod(CodeConverter.toKod(ObservationsKod.SYNFALTSPROVNING_UTAN_ANMARKNING));
            utanAnmarkning.setForekomst(syn.getSynfaltsprovningUtanAnmarkning());
            utanAnmarkning.setId(UTAN_ANMARKNING_ID);
            obs.add(utanAnmarkning);
        }

        if (syn.getHoger() != null) {
            Observation hogerUtan = new Observation();
            hogerUtan.setObservationskod(CodeConverter.toKod(ObservationsKod.EJ_KORRIGERAD_SYNSKARPA));
            hogerUtan.setLateralitet(CodeConverter.toKod(LateralitetsKod.HOGER));
            hogerUtan.getVarde().add(new PhysicalQuantity(syn.getHoger().getUtanKorrektion(), null));
            obs.add(hogerUtan);
        }

        if (syn.getVanster() != null) {
            Observation vansterUtan = new Observation();
            vansterUtan.setObservationskod(CodeConverter.toKod(ObservationsKod.EJ_KORRIGERAD_SYNSKARPA));
            vansterUtan.setLateralitet(CodeConverter.toKod(LateralitetsKod.VANSTER));
            vansterUtan.getVarde().add(new PhysicalQuantity(syn.getVanster().getUtanKorrektion(), null));
            obs.add(vansterUtan);
        }

        if (syn.getBinokulart() != null) {
            Observation binokulartUtan = new Observation();
            binokulartUtan.setObservationskod(CodeConverter.toKod(ObservationsKod.EJ_KORRIGERAD_SYNSKARPA));
            binokulartUtan.setLateralitet(CodeConverter.toKod(LateralitetsKod.BINOKULART));
            binokulartUtan.getVarde().add(new PhysicalQuantity(syn.getBinokulart().getUtanKorrektion(), null));
            obs.add(binokulartUtan);
        }

        // Create optional corrected observations
        if (syn.getHoger() != null && syn.getHoger().getMedKorrektion() != null) {
            Observation hogerMed = new Observation();
            hogerMed.setObservationskod(CodeConverter.toKod(ObservationsKod.KORRIGERAD_SYNSKARPA));
            hogerMed.setLateralitet(CodeConverter.toKod(LateralitetsKod.HOGER));
            hogerMed.getVarde().add(new PhysicalQuantity(syn.getHoger().getMedKorrektion(), null));
            obs.add(hogerMed);
        }
        if (syn.getVanster() != null && syn.getVanster().getMedKorrektion() != null) {
            Observation vansterMed = new Observation();
            vansterMed.setObservationskod(CodeConverter.toKod(ObservationsKod.KORRIGERAD_SYNSKARPA));
            vansterMed.setLateralitet(CodeConverter.toKod(LateralitetsKod.VANSTER));
            vansterMed.getVarde().add(new PhysicalQuantity(syn.getVanster().getMedKorrektion(), null));
            obs.add(vansterMed);
        }
        if (syn.getBinokulart() != null && syn.getBinokulart().getMedKorrektion() != null) {
            Observation binokulartMed = new Observation();
            binokulartMed.setObservationskod(CodeConverter.toKod(ObservationsKod.KORRIGERAD_SYNSKARPA));
            binokulartMed.setLateralitet(CodeConverter.toKod(LateralitetsKod.BINOKULART));
            binokulartMed.getVarde().add(new PhysicalQuantity(syn.getBinokulart().getMedKorrektion(), null));
            obs.add(binokulartMed);
        }

        return obs;
    }

    /**
     * Create Observation with ObservationsKod, forekomst and beskrivning
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
     * Create an Observation with Id, ObservationsKod and Boolean (forekomst)
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
     * Create a "basic" observation i.e an observation consisting of only an ObservationsKod and a boolean
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
     * Create mandatory Aktivitet[er] from internal objects
     * 
     * @param source
     *            {@link se.inera.certificate.modules.ts_diabetes.model.internal.Utlatande}
     * @return List of {@link Aktivitet}
     */
    private Collection<? extends Aktivitet> buildAktiviteter(
            se.inera.certificate.modules.ts_diabetes.model.internal.Utlatande source) {
        List<Aktivitet> aktiviteter = new ArrayList<Aktivitet>();

        // Create Aktivitet[er]

        // Synfältsprövning
        if (source.getSyn().getSynfaltsprovning() != null) {
            Aktivitet synprovning = new Aktivitet();
            synprovning.setAktivitetskod(CodeConverter.toKod(AktivitetKod.SYNFALTSUNDERSOKNING));
            synprovning.setMetod(CodeConverter.toKod(MetodKod.DONDERS_KONFRONTATIONSMETOD));
            synprovning.setId(SYNFALTSPROVNING_ID);
            aktiviteter.add(synprovning);
        }
        // Prövning av ögats rörlighet
        if (source.getSyn().getProvningOgatsRorlighet() != null) {
            Aktivitet ogatsRorlighet = new Aktivitet();
            ogatsRorlighet.setId(OGATS_RORLIGHET_ID);
            ogatsRorlighet.setAktivitetskod(CodeConverter.toKod(AktivitetKod.PROVNING_AV_OGATS_RORLIGHET));
            aktiviteter.add(ogatsRorlighet);
        }

        // Egenkontroll av blodsocker
        if (source.getHypoglykemier().getEgenkontrollBlodsocker() != null) {
            Aktivitet egenkontrollBlodsocker = new Aktivitet();
            egenkontrollBlodsocker.setAktivitetskod(CodeConverter.toKod(AktivitetKod.EGENOVERVAKNING_BLODGLUKOS));
            egenkontrollBlodsocker.setForekomst(source.getHypoglykemier().getEgenkontrollBlodsocker());
            aktiviteter.add(egenkontrollBlodsocker);
        }

        return aktiviteter;
    }

    /**
     * Convert from internal to external Patient
     * 
     * @param source
     *            {@link se.inera.certificate.modules.ts_diabetes.model.internal.Patient}
     * @return external {@link Patient}
     */
    private Patient convertToExtPatient(se.inera.certificate.modules.ts_diabetes.model.internal.Patient source) {
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
     * Convert from internal to external HosPersonal
     * 
     * @param source
     *            internal {@link HoSPersonal}
     * @return external {@link HosPersonal}
     */
    private HosPersonal convertToExtHosPersonal(HoSPersonal source) {
        HosPersonal hosPersonal = new HosPersonal();
        hosPersonal.getBefattningar().addAll(convertStringToCode(BefattningKod.class, source.getBefattningar()));
        hosPersonal.setId(new Id(HSpersonalKod.HSA_ID.getCodeSystem(), source.getPersonid()));
        hosPersonal.setNamn(source.getFullstandigtNamn());
        hosPersonal.setVardenhet(convertToExtVardenhet(source.getVardenhet()));
        // TODO: IMPORTANT! change when specialiteter is implemented in internal model
        hosPersonal.getSpecialiteter();
        return hosPersonal;
    }

    /**
     * Convert a String-representation (i.e the name of the enum constant representing that particular Kod) to a Kod
     * object
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
     * Convert from internal to external Vardenhet
     * 
     * @param source
     *            {@link se.inera.certificate.modules.ts_diabetes.model.internal.Vardenhet}
     * @return external {@link Vardenhet}
     */
    private Vardenhet convertToExtVardenhet(se.inera.certificate.modules.ts_diabetes.model.internal.Vardenhet source) {
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
     * Convert from internal to external Vardenhet
     * 
     * @param source
     *            {@link se.inera.certificate.modules.ts_diabetes.model.internal.Vardgivare}
     * @return external {@link Vardgivare}
     */
    private Vardgivare convertToExtVardgivare(se.inera.certificate.modules.ts_diabetes.model.internal.Vardgivare source) {
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setId(new Id(HSpersonalKod.HSA_ID.getCodeSystem(), source.getVardgivarid()));
        vardgivare.setNamn(source.getVardgivarnamn());
        return vardgivare;
    }

    private boolean isTrue(Boolean bool) {
        return bool != null && bool;
    }

}
