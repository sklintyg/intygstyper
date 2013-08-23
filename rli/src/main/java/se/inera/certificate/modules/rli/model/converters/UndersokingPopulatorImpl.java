package se.inera.certificate.modules.rli.model.converters;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.joda.time.Partial;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.certificate.model.Kod;
import se.inera.certificate.modules.rli.model.codes.ObservationsKod;
import se.inera.certificate.modules.rli.model.external.Aktivitet;
import se.inera.certificate.modules.rli.model.external.common.Observation;
import se.inera.certificate.modules.rli.model.external.common.PartialDateInterval;
import se.inera.certificate.modules.rli.model.internal.Graviditet;
import se.inera.certificate.modules.rli.model.internal.KomplikationStyrkt;
import se.inera.certificate.modules.rli.model.internal.OrsakAvbokning;
import se.inera.certificate.modules.rli.model.internal.Undersokning;

public class UndersokingPopulatorImpl implements UndersokningPopulator {

    private static Logger LOG = LoggerFactory.getLogger(UndersokingPopulatorImpl.class);

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
            LOG.debug("No aktiviteter found");
            return;
        }

        if (aktiviteter.size() != 2) {
            LOG.debug("There must be 2 aktiviteter");
            return;
        }

        Aktivitet ak1 = aktiviteter.get(0);
        Aktivitet ak2 = aktiviteter.get(1);

        Partial ak1Date = getExamDateFromAktivitet(ak1);
        Partial ak2Date = getExamDateFromAktivitet(ak2);

        if (ak1Date.size() == ak2Date.size()) {
            if (ak1Date.isAfter(ak2Date)) {
                currentExam = ak1;
                firstExam = ak2;
            } else {
                currentExam = ak2;
                firstExam = ak1;
            }
        } else if (ak1Date.size() > ak2Date.size()) {
            currentExam = ak1;
            firstExam = ak2;
        } else {
            currentExam = ak2;
            firstExam = ak1;
        }

        populateCurrentExam(intUndersokning, currentExam);

        populateFirstExam(intUndersokning, firstExam);

    }

    private Partial getExamDateFromAktivitet(Aktivitet aktivitet) {

        PartialDateInterval aktivitetstid = aktivitet.getAktivitetstid();

        if (aktivitetstid == null) {
            return null;
        }

        if (aktivitetstid.getFrom() != null) {
            return aktivitetstid.getFrom();
        }

        return null;
    }

    private void populateFirstExam(Undersokning intUndersokning, Aktivitet firstExam) {
        Partial firstExamDate = getExamDateFromAktivitet(firstExam);
        String forstaUndersokningDatum = PartialConverter.partialToString(firstExamDate);
        intUndersokning.setForstaUndersokningDatum(forstaUndersokningDatum);

        KomplikationStyrkt komplikationStyrkt;
        String forstaUndersokningPlats;

        if (firstExam.getUtforsVidEnhet() != null) {
            komplikationStyrkt = KomplikationStyrkt.AV_HOS_PERSONAL;
            forstaUndersokningPlats = firstExam.getUtforsVidEnhet().getEnhetsnamn();
        } else {
            komplikationStyrkt = KomplikationStyrkt.AV_PATIENT;
            forstaUndersokningPlats = firstExam.getBeskrivning();
        }

        intUndersokning.setForstaUndersokningPlats(forstaUndersokningPlats);
        intUndersokning.setKomplikationStyrkt(komplikationStyrkt);
    }

    private void populateCurrentExam(Undersokning intUndersokning, Aktivitet currentExam) {

        Partial partial = getExamDateFromAktivitet(currentExam);
        String undersokningDatum = PartialConverter.partialToString(partial);
        intUndersokning.setUndersokningDatum(undersokningDatum);

        String undersokningPlats = currentExam.getUtforsVidEnhet().getEnhetsnamn();
        intUndersokning.setUndersokningPlats(undersokningPlats);
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

        private ObservationsKod obsKodEnum;

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
