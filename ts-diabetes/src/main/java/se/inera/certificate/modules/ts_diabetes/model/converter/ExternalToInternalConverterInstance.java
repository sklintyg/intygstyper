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
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.certificate.model.HosPersonal;
import se.inera.certificate.model.Kod;
import se.inera.certificate.modules.ts_diabetes.model.codes.AktivitetKod;
import se.inera.certificate.modules.ts_diabetes.model.codes.CodeConverter;
import se.inera.certificate.modules.ts_diabetes.model.codes.IntygAvserKod;
import se.inera.certificate.modules.ts_diabetes.model.codes.LateralitetsKod;
import se.inera.certificate.modules.ts_diabetes.model.codes.ObservationsKod;
import se.inera.certificate.modules.ts_diabetes.model.codes.RekommendationVardeKod;
import se.inera.certificate.modules.ts_diabetes.model.codes.RekommendationsKod;
import se.inera.certificate.modules.ts_diabetes.model.external.Aktivitet;
import se.inera.certificate.modules.ts_diabetes.model.external.Observation;
import se.inera.certificate.modules.ts_diabetes.model.external.Rekommendation;
import se.inera.certificate.modules.ts_diabetes.model.internal.Bedomning;
import se.inera.certificate.modules.ts_diabetes.model.internal.BedomningKorkortstyp;
import se.inera.certificate.modules.ts_diabetes.model.internal.Diabetes;
import se.inera.certificate.modules.ts_diabetes.model.internal.HoSPersonal;
import se.inera.certificate.modules.ts_diabetes.model.internal.Hypoglykemier;
import se.inera.certificate.modules.ts_diabetes.model.internal.IntygAvser;
import se.inera.certificate.modules.ts_diabetes.model.internal.IntygAvserKategori;
import se.inera.certificate.modules.ts_diabetes.model.internal.Patient;
import se.inera.certificate.modules.ts_diabetes.model.internal.Syn;
import se.inera.certificate.modules.ts_diabetes.model.internal.Utlatande;
import se.inera.certificate.modules.ts_diabetes.model.internal.Vardenhet;
import se.inera.certificate.modules.ts_diabetes.model.internal.Vardgivare;
import se.inera.certificate.modules.ts_diabetes.model.internal.Vardkontakt;
import se.inera.certificate.modules.ts_diabetes.rest.dto.CertificateContentHolder;

/**
 * Converter for converting the external format to the internal view format.
 * 
 * @author Erik
 * 
 */
public class ExternalToInternalConverterInstance {

    private static final Logger LOG = LoggerFactory.getLogger(ExternalToInternalConverterInstance.class);

    private List<Observation> observationer;

    private List<Aktivitet> aktiviteter;

    public Utlatande convert(CertificateContentHolder certificateContentHolder) throws ConverterException {
        se.inera.certificate.modules.ts_diabetes.model.external.Utlatande extUtlatande = certificateContentHolder
                .getCertificateContent();

        Utlatande intUtlatande = convertUtlatandeFromExternalToInternal(extUtlatande);
        LOG.trace("Converting external model to internal");
        return intUtlatande;
    }

    private Utlatande convertUtlatandeFromExternalToInternal(
            se.inera.certificate.modules.ts_diabetes.model.external.Utlatande extUtlatande) throws ConverterException {
        LOG.debug("Converting Utlatande '{}' from external to internal", extUtlatande.getId());

        this.observationer = extUtlatande.getObservationer();
        this.aktiviteter = extUtlatande.getAktiviteter();

        Utlatande intUtlatande = new Utlatande();

        intUtlatande.setUtlatandeid(InternalModelConverterUtils.getExtensionFromId(extUtlatande.getId()));
        intUtlatande.setTypAvUtlatande(InternalModelConverterUtils.getValueFromKod(extUtlatande.getTyp()));
        intUtlatande.setSigneringsdatum(extUtlatande.getSigneringsdatum());
        intUtlatande.setSkickatdatum(extUtlatande.getSkickatdatum());
        intUtlatande.setKommentarer(extUtlatande.getKommentarer().size() == 0 ? null : extUtlatande.getKommentarer()
                .get(0));

        HoSPersonal intHoSPersonal = convertToIntHoSPersonal(extUtlatande.getSkapadAv());
        intUtlatande.setSkapadAv(intHoSPersonal);

        Patient intPatient = convertToIntPatient(extUtlatande.getPatient());
        intUtlatande.setPatient(intPatient);

        intUtlatande.setVardkontakt(convertToIntVardkontakt(extUtlatande.getVardkontakter().get(0)));

        intUtlatande.setIntygAvser(convertToIntIntygAvser(extUtlatande.getIntygAvser()));

        // Create internal model-specific objects grouping similar attributes together
        intUtlatande.setSyn(createSyn(extUtlatande));
        intUtlatande.setDiabetes(createDiabetes(extUtlatande));
        intUtlatande.setHypoglykemier(createHypoglykemier(extUtlatande));

        intUtlatande.setBedomning(createBedomning(extUtlatande));

        return intUtlatande;
    }

    /**
     * Create Hypoglykemier object from external Utlatande
     * 
     * @param extUtlatande
     *            {@link se.inera.certificate.modules.ts_diabetes.model.external.Utlatande}
     * @return {@link Hypoglykemier}
     * @throws ConverterException
     */
    private Hypoglykemier createHypoglykemier(
            se.inera.certificate.modules.ts_diabetes.model.external.Utlatande extUtlatande) throws ConverterException {
        Hypoglykemier hypo = new Hypoglykemier();

        Observation kunskap = getObservationWithKod(CodeConverter.toKod(ObservationsKod.KUNSKAP_ATGARD_HYPOGLYKEMI));
        Observation teckenNedsattHjarnfunktion = getObservationWithKod(CodeConverter
                .toKod(ObservationsKod.HYPOGLYKEMIER_MED_TECKEN_PA_NEDSATT_HJARNFUNKTION));
        
        Aktivitet blodsocker = getAktivitetWithKod(CodeConverter.toKod(AktivitetKod.EGENOVERVAKNING_BLODGLUKOS));

        // Mandatory observations, check for null, if not proceed and set corresponding booleans
        if (kunskap == null) {
            throw new ConverterException("Missing observation OBS19");
        }
        if (teckenNedsattHjarnfunktion == null) {
            throw new ConverterException("Missing observation OBS20");
        }
        hypo.setTeckenNedsattHjarnfunktion(teckenNedsattHjarnfunktion.getForekomst());
        hypo.setKunskapOmAtgarder(kunskap.getForekomst());

        // Then proceed to convert optional Observations
        Observation saknarFormaga = getObservationWithKod(CodeConverter
                .toKod(ObservationsKod.SAKNAR_FORMAGA_KANNA_HYPOGLYKEMI));
        Observation allvarligHypoglykemi = getObservationWithKod(CodeConverter
                .toKod(ObservationsKod.ALLVARLIG_HYPOGLYKEMI));
        Observation allvarligHypoglykemiTrafik = getObservationWithKod(CodeConverter
                .toKod(ObservationsKod.ALLVARLIG_HYPOGLYKEMI_I_TRAFIKEN));
        Observation allvarligHypoglykemiVaken = getObservationWithKod(CodeConverter
                .toKod(ObservationsKod.ALLVARLIG_HYPOGLYKEMI_VAKET_TILLSTAND));

        // Corresponding Observation in comments for clarity
        // Patienten saknar förmåga att känna varningstecken på hypoglykemi
        if (saknarFormaga != null) {
            hypo.setSaknarFormagaKannaVarningstecken(saknarFormaga.getForekomst());
        }

        // Förekomst av allvarlig hypoglykemi under det senaste året
        if (allvarligHypoglykemi != null) {
            hypo.setAllvarligForekomst(allvarligHypoglykemi.getForekomst());
            hypo.setAllvarligForekomstBeskrivning(allvarligHypoglykemi.getBeskrivning());
        }

        // Förekomst av allvarlig hypoglykemi i trafiken under senaste året
        if (allvarligHypoglykemiTrafik != null) {
            hypo.setAllvarligForekomstTrafiken(allvarligHypoglykemiTrafik.getForekomst());

            if (allvarligHypoglykemiTrafik.getForekomst()) {
                hypo.setAllvarligForekomstTrafikBeskrivning(allvarligHypoglykemiTrafik.getBeskrivning());
            }
        }

        // Förekomst av allvarlig hypoglykemi i vaket tillstånd under senaste året
        if (allvarligHypoglykemiVaken != null) {
            hypo.setAllvarligForekomstVakenTid(allvarligHypoglykemiVaken.getForekomst());

            if (allvarligHypoglykemiVaken.getForekomst()) {
                hypo.setAllvarligForekomstVakenTidObservationstid(allvarligHypoglykemiVaken.getOstruktureradTid());
            }
        }
        
        if (blodsocker != null) {
            hypo.setEgenkontrollBlodsocker(blodsocker.getForekomst());
        }
        return hypo;
    }

    /**
     * Convert a List of Kod into an IntygAvser object
     * 
     * @param source
     *            a List of {@link Kod}
     * @return {@link IntygAvser}
     */
    private IntygAvser convertToIntIntygAvser(List<Kod> source) {
        IntygAvser intygAvser = new IntygAvser();

        for (Kod kod : source) {
            IntygAvserKod vardeKod = CodeConverter.fromCode(kod, IntygAvserKod.class);
            intygAvser.getKorkortstyp().add(IntygAvserKategori.valueOf(vardeKod.name()));
        }

        return intygAvser;
    }

    /**
     * Convert Vardkontakt from external to internal format
     * 
     * @param source
     *            {@link se.inera.certificate.modules.ts_diabetes.model.external.Vardkontakt}
     * @return {@link Vardkontakt}
     */
    private Vardkontakt convertToIntVardkontakt(
            se.inera.certificate.modules.ts_diabetes.model.external.Vardkontakt source) {
        Vardkontakt vardkontakt = new Vardkontakt();

        vardkontakt.setIdkontroll(source.getIdkontroll().getCode());
        vardkontakt.setTyp(source.getVardkontakttyp().getCode());

        return vardkontakt;
    }

    /**
     * Create a Bedomning object from a List of Rekommendation[er]
     * 
     * @param extUtlatande
     *            {@link se.inera.certificate.modules.ts_diabetes.model.external.Utlatande}
     * @return {@link Bedomning}
     */
    private Bedomning createBedomning(se.inera.certificate.modules.ts_diabetes.model.external.Utlatande extUtlatande) {
        Bedomning bedomning = new Bedomning();

        for (Rekommendation rek : extUtlatande.getRekommendationer()) {
            
            if (CodeConverter.matches(RekommendationsKod.PATIENT_UPPFYLLER_KRAV_FOR, rek.getRekommendationskod())) {
                if (rek.getVarde().contains(CodeConverter.toKod(RekommendationVardeKod.INTE_TA_STALLNING))) {
                    bedomning.setKanInteTaStallning(true);
                } else {
                    for (Kod varde : rek.getVarde()) {
                        RekommendationVardeKod vardeKod = CodeConverter.fromCode(varde, RekommendationVardeKod.class);
                        bedomning.getKorkortstyp().add(BedomningKorkortstyp.valueOf(vardeKod.name()));
                    }
                }

            } else if(CodeConverter.matches(RekommendationsKod.LAMPLIGHET_INNEHA_BEHORIGHET_TILL_KORNINGAR_OCH_ARBETSFORMER,
                    rek.getRekommendationskod())) {
                bedomning.setLamplighetInnehaBehorighet(rek.getBoolean_varde());
            
            } else if (CodeConverter.matches(RekommendationsKod.PATIENT_BOR_UNDESOKAS_AV_SPECIALIST,
            
                    rek.getRekommendationskod())) {
                bedomning.setLakareSpecialKompetens(rek.getBeskrivning());

            }
        }
        return bedomning;
    }

    /**
     * Create Diabetes object from Observationer
     * 
     * @param extUtlatande
     *            {@link se.inera.certificate.modules.ts_diabetes.model.external.Utlatande}
     * @return {@link Diabetes} object
     * @throws ConverterException
     */
    private Diabetes createDiabetes(se.inera.certificate.modules.ts_diabetes.model.external.Utlatande extUtlatande)
            throws ConverterException {
        Observation diabetesTyp1 = getObservationWithKod(CodeConverter.toKod(ObservationsKod.DIABETES_TYP_1));
        Observation diabetesTyp2 = getObservationWithKod(CodeConverter.toKod(ObservationsKod.DIABETES_TYP_2));

        if (diabetesTyp1 == null && diabetesTyp2 == null) {
            throw new ConverterException("One of Diabetes Typ1 or Diabetes Typ2 must be specified");
        }

        Diabetes diabetes = new Diabetes();
        Observation insulin = getObservationWithKod(CodeConverter.toKod(ObservationsKod.DIABETIKER_INSULINBEHANDLING));
        Observation tabletter = getObservationWithKod(CodeConverter.toKod(ObservationsKod.DIABETIKER_TABLETTBEHANDLING));
        Observation kost = getObservationWithKod(CodeConverter.toKod(ObservationsKod.DIABETIKER_ENBART_KOST));
        Observation annan = getObservationWithKod(CodeConverter.toKod(ObservationsKod.DIABETIKER_ANNAN_BEHANDLING));

        if (diabetesTyp1 != null) {
            if (diabetesTyp1.getForekomst()) {
                diabetes.setDiabetestyp("E10");
                diabetes.setObservationsperiod(diabetesTyp1.getOstruktureradTid());
            }
        }

        if (diabetesTyp2 != null) {
            if (diabetesTyp2.getForekomst()) {
                diabetes.setDiabetestyp("E11");
                diabetes.setObservationsperiod(diabetesTyp2.getOstruktureradTid());
            }
        }
        
        if (insulin != null) {
            diabetes.setInsulin(insulin.getForekomst());
            if (insulin.getForekomst()) {
                diabetes.setInsulinBehandlingsperiod(insulin.getOstruktureradTid());
            }
        }

        if (tabletter != null) {
            diabetes.setTabletter(insulin.getForekomst());
        }

        if (kost != null) {
            diabetes.setEndastKost(kost.getForekomst());
        }

        if (annan != null) {
            diabetes.setAnnanBehandling(annan.getForekomst());
            if (annan.getForekomst()) {
                diabetes.setAnnanBehandlingBeskrivning(annan.getBeskrivning());
            }
        }

        return diabetes;
    }

    /**
     * Create a {@link Syn} object from {@link Observation}s in
     * {@link se.inera.certificate.modules.ts_diabetes.model.external.Utlatande}
     * 
     * @param extUtlatande
     *            {@link se.inera.certificate.modules.ts_diabetes.model.external.Utlatande}
     */
    private Syn createSyn(se.inera.certificate.modules.ts_diabetes.model.external.Utlatande extUtlatande) {

        Syn syn = new Syn();

        Aktivitet synfaltsprovning = getAktivitetWithKod(CodeConverter.toKod(AktivitetKod.SYNFALTSUNDERSOKNING));
        Aktivitet provningOgatsRorlighet = getAktivitetWithKod(CodeConverter
                .toKod(AktivitetKod.PROVNING_AV_OGATS_RORLIGHET));
        
        if (synfaltsprovning != null) {
            syn.setSynfaltsprovning(true);
        }

        if (provningOgatsRorlighet != null) {
            syn.setProvningOgatsRorlighet(true);
        }

        if (extUtlatande.getBilaga() != null) {
            syn.setSeparatOgonlakarintyg(true);
        }

        // Handle Syn related Observationer
        Observation diplopi = getObservationWithKod(CodeConverter.toKod(ObservationsKod.DIPLOPI));
        Observation utanAnmarkning = getObservationWithKod(CodeConverter
                .toKod(ObservationsKod.SYNFALTSPROVNING_UTAN_ANMARKNING));

        if (diplopi != null) {
            syn.setDiplopi(diplopi.getForekomst());
        }

        if (utanAnmarkning != null) {
            syn.setSynfaltsprovningUtanAnmarkning(utanAnmarkning.getForekomst());
        }

        // Used to populate Syn with Synskarpa later
        List<Observation> synskarpa = new ArrayList<Observation>();

        // Hmmm does this need to be this way or can getObservationWithKod be used instead?
        for (Observation obs : extUtlatande.getObservationer()) {
            Kod kod = obs.getObservationskod();
            if (kod.getCode().equals(ObservationsKod.EJ_KORRIGERAD_SYNSKARPA.getCode())
                    || kod.getCode().equals(ObservationsKod.KORRIGERAD_SYNSKARPA.getCode())) {
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

            }
        }

        syn.setHoger(hogerUtan, hogerMed);
        syn.setVanster(vansterUtan, vansterMed);
        syn.setBinokulart(binUtan, binMed);

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
