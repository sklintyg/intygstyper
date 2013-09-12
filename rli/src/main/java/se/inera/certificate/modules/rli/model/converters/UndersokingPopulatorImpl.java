package se.inera.certificate.modules.rli.model.converters;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.joda.time.Partial;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.certificate.model.HosPersonal;
import se.inera.certificate.model.Kod;
import se.inera.certificate.model.Observation;
import se.inera.certificate.model.PartialInterval;
import se.inera.certificate.model.Utforarroll;
import se.inera.certificate.modules.rli.model.codes.AktivitetsKod;
import se.inera.certificate.modules.rli.model.codes.ObservationsKod;
import se.inera.certificate.modules.rli.model.external.Aktivitet;

import se.inera.certificate.modules.rli.model.internal.Graviditet;
import se.inera.certificate.modules.rli.model.internal.HoSPersonal;
import se.inera.certificate.modules.rli.model.internal.KomplikationStyrkt;
import se.inera.certificate.modules.rli.model.internal.OrsakAvbokning;
import se.inera.certificate.modules.rli.model.internal.Undersokning;
import se.inera.certificate.modules.rli.model.internal.Utforare;
import se.inera.certificate.modules.rli.model.internal.Vardenhet;
import se.inera.certificate.modules.rli.model.internal.Vardgivare;

public class UndersokingPopulatorImpl implements UndersokningPopulator {

    private static final Logger LOG = LoggerFactory.getLogger(UndersokingPopulatorImpl.class);

    /*
     * (non-Javadoc)
     * 
     * @see se.inera.certificate.modules.rli.model.converters.UndersokningPopulator #createAndPopulateUndersokning
     * (se.inera.certificate.modules.rli.model.external.Utlatande)
     */
    @Override
    public Undersokning createAndPopulateUndersokning(
            se.inera.certificate.modules.rli.model.external.Utlatande extUtlatande) {

        LOG.debug("Creating and populating Undersokning");

        Undersokning intUndersokning = new Undersokning();

        populateUndersokningFromObservationer(extUtlatande.getObservations(), intUndersokning);

        populateUndersokningFromAktiviteter(extUtlatande.getAktiviteter(), intUndersokning);

        return intUndersokning;
    }

    void populateUndersokningFromObservationer(List<Observation> observations, Undersokning intUndersokning) {

        LOG.debug("Populating Undersokning from Observationer");

        if (observations.isEmpty()) {
            LOG.error("No observations found! Can not populate undersokning!");
            return;
        }

        Observation obs = null;

        obs = (Observation) CollectionUtils.find(observations, new ObservationsKodPredicate(ObservationsKod.SJUKDOM));

        if (obs != null) {
            intUndersokning.setOrsakforavbokning(OrsakAvbokning.RESENAR_SJUK);
            if (obs.getUtforsAv() != null) {
                intUndersokning.setUtforsAv(convertToIntUtforare(obs.getUtforsAv()));
            }
            return;
        }

        obs = (Observation) CollectionUtils
                .find(observations, new ObservationsKodPredicate(ObservationsKod.GRAVIDITET));

        if (obs != null) {
            intUndersokning.setOrsakforavbokning(OrsakAvbokning.RESENAR_GRAVID);
            if (obs.getUtforsAv() != null) {
                intUndersokning.setUtforsAv(convertToIntUtforare(obs.getUtforsAv()));
            }
            handleGraviditet(obs, intUndersokning);
            return;
        }

    }

    private Utforare convertToIntUtforare(Utforarroll source) {
        LOG.debug("Converting to internal Utforare");
        Utforare utforsAv = new Utforare();
        utforsAv.setUtforartyp(source.getUtforartyp().getCode());
        if (source.getAntasAv() != null){
            utforsAv.setAntasAv(convertToIntHoSPersonal(source.getAntasAv()));
        }
        return utforsAv;
    }

    private HoSPersonal convertToIntHoSPersonal(HosPersonal extHoSPersonal) {
        HoSPersonal intHoSPersonal = new HoSPersonal();

        intHoSPersonal.setPersonid(InternalModelConverterUtils.getExtensionFromId(extHoSPersonal.getId()));
        intHoSPersonal.setFullstandigtNamn(extHoSPersonal.getNamn());
        // intHoSPersonal.setBefattning(befattning);
        
        Vardenhet intVardenhet = convertToIntVardenhet(extHoSPersonal.getVardenhet());
        intHoSPersonal.setVardenhet(intVardenhet);

        return intHoSPersonal;
    }

    private Vardenhet convertToIntVardenhet(se.inera.certificate.model.Vardenhet source) {
        if (source == null){
            LOG.debug("External vardenhet was null");
            return null;
        }
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setEnhetsid(source.getId().getExtension());
        vardenhet.setEnhetsnamn(source.getNamn());
        vardenhet.setEpost(source.getEpost());
        vardenhet.setPostadress(source.getPostadress());
        vardenhet.setPostnummer(source.getPostnummer());
        vardenhet.setPostort(source.getPostort());
        vardenhet.setTelefonnummer(source.getTelefonnummer());
        vardenhet.setVardgivare(convertToIntVardgivare(source.getVardgivare()));
        return vardenhet;
    }

    private Vardgivare convertToIntVardgivare(se.inera.certificate.model.Vardgivare source) {
        Vardgivare vg = new Vardgivare();
        vg.setVardgivarid(source.getId().getExtension());
        vg.setVardgivarnamn(source.getNamn());
        return vg;
    }

    void populateUndersokningFromAktiviteter(List<Aktivitet> aktiviteter, Undersokning intUndersokning) {

        LOG.debug("Populating Undersokning from Aktiviteter");

        Aktivitet currentExam = null;
        Aktivitet firstExam = null;

        if (aktiviteter == null || aktiviteter.isEmpty()) {
            LOG.debug("No aktiviteter found, can not continue with population");
            return;
        }

        LOG.debug("{} aktiviteter supplied", aktiviteter.size());

        for (Aktivitet akt : aktiviteter) {
            Kod aktivitetskod = akt.getAktivitetskod();
            if (checkAktivitetsKod(aktivitetskod, AktivitetsKod.FORSTA_UNDERSOKNING)) {
                LOG.debug("Found firstExam");
                firstExam = akt;
            } else if (checkAktivitetsKod(aktivitetskod, AktivitetsKod.KLINISK_UNDERSOKNING)) {
                LOG.debug("Found currentExam");
                currentExam = akt;
            }
        }

        populateCurrentExam(intUndersokning, currentExam);

        populateFirstExam(intUndersokning, firstExam);

    }

    private boolean checkAktivitetsKod(Kod kod, AktivitetsKod aktivitetsKod) {
        String kodValue = InternalModelConverterUtils.getValueFromKod(kod);
        return aktivitetsKod.getCode().equals(kodValue);
    }

    private String getExamDateFromAktivitet(Aktivitet aktivitet) {

        PartialInterval aktivitetstid = aktivitet.getAktivitetstid();

        if (aktivitetstid == null) {
            return null;
        }

        if (aktivitetstid.getFrom() != null) {
            return PartialConverter.partialToString(aktivitetstid.getFrom());
        }

        return null;
    }

    private void populateFirstExam(Undersokning intUndersokning, Aktivitet firstExam) {

        LOG.debug("Populating first exam info");

        if (firstExam == null) {
            LOG.debug("- firstExam is null, can not populate first exam info");
            return;
        }

        LOG.debug("- Extracting exam date from the first exam");
        String forstaUndersokningDatum = getExamDateFromAktivitet(firstExam);
        intUndersokning.setForstaUndersokningsdatum(forstaUndersokningDatum);

        KomplikationStyrkt komplikationStyrkt;
        String forstaUndersokningPlats;

        LOG.debug("- Extracting place and complication attestation from the first exam");

        if (firstExam.getUtforsVid() != null) {
            komplikationStyrkt = KomplikationStyrkt.AV_HOS_PERSONAL;
            // forstaUndersokningPlats = firstExam.getUtforsVid().getNamn();
            forstaUndersokningPlats = null;
            intUndersokning.setUtforsVid(convertToIntVardenhet(firstExam.getUtforsVid()));

        } else {
            komplikationStyrkt = KomplikationStyrkt.AV_PATIENT;
            forstaUndersokningPlats = firstExam.getPlats();
        }

        LOG.debug("- Place is {} for the first exam", forstaUndersokningPlats);
        intUndersokning.setForstaUndersokningsplats(forstaUndersokningPlats);

        LOG.debug("- Complication attestation is {}", komplikationStyrkt);
        intUndersokning.setKomplikationstyrkt(komplikationStyrkt);
    }

    private void populateCurrentExam(Undersokning intUndersokning, Aktivitet currentExam) {

        LOG.debug("Populating current exam info");

        LOG.debug("- Extracting date for the current exam");
        String undersokningDatum = getExamDateFromAktivitet(currentExam);
        intUndersokning.setUndersokningsdatum(undersokningDatum);

        if (currentExam.getUtforsVid() != null) {
            LOG.debug("- Extracting place for the current exam");
            String undersokningPlats = currentExam.getUtforsVid().getNamn();
            // intUndersokning.setUndersokningsplats(undersokningPlats);
            intUndersokning.setUtforsVid(convertToIntVardenhet(currentExam.getUtforsVid()));

        } else {
            LOG.debug("- Place for current exam could not be determined");
        }
    }

    private void handleGraviditet(Observation obs, Undersokning undersokning) {

        LOG.debug("Handling pregnancy observations");

        PartialInterval observationsperiod = obs.getObservationsPeriod();
        Partial obsPeriodSlut = observationsperiod.getTom();

        String estimatedDeliveryDate = PartialConverter.partialToString(obsPeriodSlut);

        Graviditet pregnancyInfo = new Graviditet();
        pregnancyInfo.setBeraknatForlossningsdatum(estimatedDeliveryDate);

        undersokning.setGraviditet(pregnancyInfo);
    }

    public class ObservationsKodPredicate implements Predicate {

        private final ObservationsKod obsKodEnum;

        public ObservationsKodPredicate(ObservationsKod obsKodEnum) {
            this.obsKodEnum = obsKodEnum;
        }

        @Override
        public boolean evaluate(Object obj) {

            if (!(obj instanceof Observation)) {
                return false;
            }

            Observation obs = (Observation) obj;
            Kod obsKod = obs.getObservationsKod();

            return (obsKod.getCode().equals(obsKodEnum.getCode()));
        }

    }
}
