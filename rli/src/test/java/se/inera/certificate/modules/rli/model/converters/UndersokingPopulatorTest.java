package se.inera.certificate.modules.rli.model.converters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joda.time.LocalDateTime;
import org.joda.time.Partial;
import org.junit.Before;
import org.junit.Test;

import se.inera.certificate.model.Kod;
import se.inera.certificate.model.Observation;
import se.inera.certificate.model.PartialInterval;
import se.inera.certificate.model.Vardenhet;
import se.inera.certificate.modules.rli.model.codes.AktivitetsKod;
import se.inera.certificate.modules.rli.model.codes.ObservationsKod;
import se.inera.certificate.modules.rli.model.external.Aktivitet;
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
        assertEquals(OrsakAvbokning.RESENAR_SJUK, undersokning.getOrsakforavbokning());
    }

    @Test
    public void testPopulateUndersokningRekommendationMedGravid() {

        Observation sjukObs = constructObservation(ObservationsKod.GRAVIDITET, 2013, 2, 2);

        sjukObs.getObservationsPeriod().setTom(TestUtils.constructPartial(2013, 12, 24));

        List<Observation> observationer = new ArrayList<Observation>();
        observationer.add(sjukObs);

        Undersokning undersokning = new Undersokning();

        converter.populateUndersokningFromObservationer(observationer, undersokning);

        assertNotNull(undersokning);
        assertEquals(OrsakAvbokning.RESENAR_GRAVID, undersokning.getOrsakforavbokning());
        assertNotNull(undersokning.getGraviditet());
        assertEquals("2013-12-24", undersokning.getGraviditet().getBeraknadforlossningsdatum());
    }

    @Test
    public void testpopulateFromAktiviteter1() {

        Aktivitet firstExam = constructAktivitet(AktivitetsKod.FORSTA_UNDERSOKNING, 2012, 5, 0);
        firstExam.setPlats("Sjukhus A");

        Aktivitet currentExam = constructAktivitet(AktivitetsKod.KLINISK_UNDERSOKNING, 2013, 6, 12);
        Vardenhet enhet = constructEnhet("Sjukhus B");
        currentExam.setUtforsVid(enhet);

        List<Aktivitet> aktiviteter = Arrays.asList(currentExam, firstExam);

        Undersokning intUndersokning = new Undersokning();
        converter.populateUndersokningFromAktiviteter(aktiviteter, intUndersokning);

        assertNotNull(intUndersokning);
        assertEquals("2012-05", intUndersokning.getForstaundersokningsdatum());
        assertEquals("Sjukhus A", intUndersokning.getForstaundersokningsplats());
        assertEquals(KomplikationStyrkt.AV_PATIENT, intUndersokning.getKomplikationstyrkt());

        assertEquals("2013-06-12", intUndersokning.getUndersokningsdatum());
        assertEquals("Sjukhus B", intUndersokning.getUndersokningsplats());
    }

    @Test
    public void testpopulateFromAktiviteter2() {

        Aktivitet currentExam = constructAktivitet(AktivitetsKod.KLINISK_UNDERSOKNING, 2013, 6, 12);
        Vardenhet sjukhusEnhetX = constructEnhet("Sjukhus X");
        currentExam.setUtforsVid(sjukhusEnhetX);

        List<Aktivitet> aktiviteter = Arrays.asList(currentExam);

        Undersokning intUndersokning = new Undersokning();
        converter.populateUndersokningFromAktiviteter(aktiviteter, intUndersokning);

        assertNotNull(intUndersokning);
        assertEquals("2013-06-12", intUndersokning.getUndersokningsdatum());
        assertEquals("Sjukhus X", intUndersokning.getUndersokningsplats());
        //assertEquals(KomplikationStyrkt.AV_HOS_PERSONAL, intUndersokning.getKomplikationStyrkt());
        assertNull(intUndersokning.getForstaundersokningsdatum());
        assertNull(intUndersokning.getForstaundersokningsplats());
    }

    private Observation constructObservation(ObservationsKod obsKod, int year, int month, int day) {

        Observation obs = new Observation();

        PartialInterval obsPeriod = new PartialInterval(null, null);
        obs.setObservationsPeriod(obsPeriod);

        obs.setObservationsKod(new Kod(obsKod.getCode()));
        obs.setObservationsTid(LocalDateTime.now());

        return obs;
    }

    private Vardenhet constructEnhet(String enhetsNamn) {
        Vardenhet enhet = new Vardenhet();
        enhet.setNamn(enhetsNamn);
        return enhet;
    }

    private Aktivitet constructAktivitet(AktivitetsKod aktivitetsKod, int year, int month, int day) {

        Partial from = TestUtils.constructPartial(year, month, day);
        PartialInterval atid = new PartialInterval(from, null);

        Aktivitet a = new Aktivitet();
        a.setAktivitetskod(new Kod(aktivitetsKod.getCode()));
        a.setAktivitetstid(atid);

        return a;
    }

}
