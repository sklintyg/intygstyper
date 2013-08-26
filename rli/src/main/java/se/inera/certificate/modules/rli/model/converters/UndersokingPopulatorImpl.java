package se.inera.certificate.modules.rli.model.converters;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.joda.time.Partial;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.certificate.model.Kod;
import se.inera.certificate.modules.rli.model.codes.AktivitetsKod;
import se.inera.certificate.modules.rli.model.codes.ObservationsKod;
import se.inera.certificate.modules.rli.model.external.Aktivitet;
import se.inera.certificate.modules.rli.model.external.common.Observation;
import se.inera.certificate.modules.rli.model.external.common.PartialDateInterval;
import se.inera.certificate.modules.rli.model.internal.Graviditet;
import se.inera.certificate.modules.rli.model.internal.KomplikationStyrkt;
import se.inera.certificate.modules.rli.model.internal.OrsakAvbokning;
import se.inera.certificate.modules.rli.model.internal.Undersokning;

public class UndersokingPopulatorImpl implements UndersokningPopulator {

    private static final Logger LOG = LoggerFactory.getLogger(UndersokingPopulatorImpl.class);

    /*
     * (non-Javadoc)
     * 
     * @see
     * se.inera.certificate.modules.rli.model.converters.UndersokningPopulator
     * #createAndPopulateUndersokning
     * (se.inera.certificate.modules.rli.model.external.Utlatande)
     */
    @Override
    public Undersokning createAndPopulateUndersokning(
            se.inera.certificate.modules.rli.model.external.Utlatande extUtlatande) {

        LOG.debug("Creating and populating Undersokning");

        Undersokning intUndersokning = new Undersokning();

        populateUndersokningFromObservationer(extUtlatande.getObservationer(), intUndersokning);

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
            intUndersokning.setOrsakForAvbokning(OrsakAvbokning.RESENAR_SJUK);
            return;
        }

        obs = (Observation) CollectionUtils
                .find(observations, new ObservationsKodPredicate(ObservationsKod.GRAVIDITET));

        if (obs != null) {
            intUndersokning.setOrsakForAvbokning(OrsakAvbokning.RESENAR_GRAVID);
            handleGraviditet(obs, intUndersokning);
            return;
        }

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

        PartialDateInterval aktivitetstid = aktivitet.getAktivitetstid();

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
        intUndersokning.setForstaUndersokningDatum(forstaUndersokningDatum);

        KomplikationStyrkt komplikationStyrkt;
        String forstaUndersokningPlats;

        LOG.debug("- Extracting place and complication attestation from the first exam");

        if (firstExam.getUtforsVidEnhet() != null) {
            komplikationStyrkt = KomplikationStyrkt.AV_HOS_PERSONAL;
            forstaUndersokningPlats = firstExam.getUtforsVidEnhet().getEnhetsnamn();
        } else {
            komplikationStyrkt = KomplikationStyrkt.AV_PATIENT;
            forstaUndersokningPlats = firstExam.getPlats();
        }

        LOG.debug("- Place is {} for the first exam", forstaUndersokningPlats);
        intUndersokning.setForstaUndersokningPlats(forstaUndersokningPlats);

        LOG.debug("- Complication attestation is {}", komplikationStyrkt);
        intUndersokning.setKomplikationStyrkt(komplikationStyrkt);
    }

    private void populateCurrentExam(Undersokning intUndersokning, Aktivitet currentExam) {

        LOG.debug("Populating current exam info");

        LOG.debug("- Extracting date for the current exam");
        String undersokningDatum = getExamDateFromAktivitet(currentExam);
        intUndersokning.setUndersokningDatum(undersokningDatum);

        if (currentExam.getUtforsVidEnhet() != null) {
            LOG.debug("- Extracting place for the current exam");
            String undersokningPlats = currentExam.getUtforsVidEnhet().getEnhetsnamn();
            intUndersokning.setUndersokningPlats(undersokningPlats);
        } else {
            LOG.debug("- Place for current exam could not be determined");
        }
    }

    private void handleGraviditet(Observation obs, Undersokning undersokning) {

        LOG.debug("Handling pregnancy observations");

        PartialDateInterval observationsperiod = obs.getObservationsperiod();
        Partial obsPeriodSlut = observationsperiod.getTom();

        String estimatedDeliveryDate = PartialConverter.partialToString(obsPeriodSlut);

        Graviditet pregnancyInfo = new Graviditet();
        pregnancyInfo.setBeraknadForlossningDatum(estimatedDeliveryDate);

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
            Kod obsKod = obs.getObservationskod();

            return (obsKod.getCode().equals(obsKodEnum.getCode()));
        }

    }
}
