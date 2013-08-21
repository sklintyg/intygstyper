package se.inera.certificate.modules.rli.model.converters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;
import org.junit.Before;
import org.junit.Test;

import se.inera.certificate.modules.rli.model.codes.AktivitetsKod;
import se.inera.certificate.modules.rli.model.codes.ObservationsKod;
import se.inera.certificate.modules.rli.model.external.common.Aktivitet;
import se.inera.certificate.modules.rli.model.external.common.Enhet;
import se.inera.certificate.modules.rli.model.external.common.Kod;
import se.inera.certificate.modules.rli.model.external.common.Observation;
import se.inera.certificate.modules.rli.model.external.common.PartialDateInterval;
import se.inera.certificate.modules.rli.model.internal.KomplikationStyrkt;
import se.inera.certificate.modules.rli.model.internal.OrsakAvbokning;
import se.inera.certificate.modules.rli.model.internal.Undersokning;

/**
 * Unit test for the UndersokningPopulatorImpl class.
 * 
 * @author nikpet
 *
 */
public class UndersokingPopulatorTest {

    private UndersokingPopulatorImpl converter;

    @Before
    public void setUp() {
        this.converter = new UndersokingPopulatorImpl();
    }

    @Test
    public void testPopulateUndersokningRekommendationMedSjukdom() {

        Observation sjukObs = constructObservation(ObservationsKod.SJUKDOM, 2013, 2, 2);

        List<Observation> observationer = Arrays.asList(sjukObs);

        Undersokning undersokning = new Undersokning();

        converter.populateUndersokningFromObservationer(observationer, undersokning);

        assertNotNull(undersokning);
        assertEquals(OrsakAvbokning.RESENAR_SJUK, undersokning.getOrsakForAvbokning());
    }

    @Test
    public void testPopulateUndersokningRekommendationMedGravid() {

        Observation sjukObs = constructObservation(ObservationsKod.GRAVIDITET, 2013, 2, 2);

        sjukObs.getObservationsperiod().setTom(TestUtils.constructPartial(2013, 12, 24));

        List<Observation> observationer = new ArrayList<Observation>();
        observationer.add(sjukObs);

        Undersokning undersokning = new Undersokning();

        converter.populateUndersokningFromObservationer(observationer, undersokning);

        assertNotNull(undersokning);
        assertEquals(OrsakAvbokning.RESENAR_GRAVID, undersokning.getOrsakForAvbokning());
        assertNotNull(undersokning.getGraviditet());
        assertEquals("2013-12-24", undersokning.getGraviditet().getBeraknadForlossningDatum());
    }

    @Test
    public void testpopulateFromAktiviteter1() {

        Aktivitet firstExam = constructAktivitet(2012, 5, 0);
        firstExam.setBeskrivning("Sjukhus A");

        Aktivitet currentExam = constructAktivitet(2013, 6, 12);
        Enhet enhet = constructEnhet("Sjukhus B");
        currentExam.setUtforsVidEnhet(enhet);

        List<Aktivitet> aktiviteter = Arrays.asList(currentExam, firstExam);

        Undersokning intUndersokning = new Undersokning();
        converter.populateUndersokningFromAktiviteter(aktiviteter, intUndersokning);

        assertNotNull(intUndersokning);
        assertEquals(intUndersokning.getForstaUndersokningDatum(), "2012-05");
        assertEquals(intUndersokning.getForstaUndersokningPlats(), "Sjukhus A");
        assertEquals(intUndersokning.getKomplikationStyrkt(), KomplikationStyrkt.AV_PATIENT);

        assertEquals(intUndersokning.getUndersokningDatum(), "2013-06-12");
        assertEquals(intUndersokning.getUndersokningPlats(), "Sjukhus B");
    }

    @Test
    public void testpopulateFromAktiviteter2() {

        Aktivitet firstExam = constructAktivitet(2012, 5, 23);
        Enhet sjukhusEnhetA = constructEnhet("Sjukhus A");
        firstExam.setUtforsVidEnhet(sjukhusEnhetA);

        Aktivitet currentExam = constructAktivitet(2013, 6, 12);
        Enhet sjukhusEnhetX = constructEnhet("Sjukhus X");
        currentExam.setUtforsVidEnhet(sjukhusEnhetX);

        List<Aktivitet> aktiviteter = Arrays.asList(firstExam, currentExam);

        Undersokning intUndersokning = new Undersokning();
        converter.populateUndersokningFromAktiviteter(aktiviteter, intUndersokning);

        assertNotNull(intUndersokning);
        assertEquals(intUndersokning.getForstaUndersokningDatum(), "2012-05-23");
        assertEquals(intUndersokning.getForstaUndersokningPlats(), "Sjukhus A");
        assertEquals(intUndersokning.getKomplikationStyrkt(), KomplikationStyrkt.AV_HOS_PERSONAL);

        assertEquals(intUndersokning.getUndersokningDatum(), "2013-06-12");
        assertEquals(intUndersokning.getUndersokningPlats(), "Sjukhus X");
    }

    private Observation constructObservation(ObservationsKod obsKod, int year, int month, int day) {

        Observation obs = new Observation();

        PartialDateInterval obsPeriod = new PartialDateInterval(null, null);
        obs.setObservationsperiod(obsPeriod);

        obs.setObservationskod(new Kod(obsKod.getCode()));
        obs.setObservationstid(TestUtils.constructPartial(year, month, day));

        return obs;
    }

    private Enhet constructEnhet(String enhetsNamn) {
        Enhet enhet = new Enhet();
        enhet.setEnhetsnamn(enhetsNamn);
        return enhet;
    }

    private Aktivitet constructAktivitet(int year, int month, int day) {

        Partial from = TestUtils.constructPartial(year, month, day);
        PartialDateInterval atid = new PartialDateInterval(from, null);

        Aktivitet a = new Aktivitet();
        a.setAktivitetskod(new Kod(AktivitetsKod.KLINISK_UNDERSOKNING.getCode()));
        a.setAktivitetstid(atid);

        return a;
    }

}
