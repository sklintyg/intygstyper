package se.inera.intyg.intygstyper.fk7263.model.converter;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.common.support.model.InternalLocalDateInterval;
import se.inera.intyg.common.support.model.LocalDateInterval;
import se.inera.intyg.intygstyper.fk7263.model.internal.Utlatande;

/**
 * Created by eriklupander on 15-04-08.
 */
@RunWith(MockitoJUnitRunner.class)
public class ArbetsformagaToGiltighetTest {

    private LocalDate fromDate1;
    private LocalDate toDate1;
    private LocalDate fromDate2;
    private LocalDate toDate2;
    private LocalDate fromDate3;
    private LocalDate toDate3;
    private Utlatande utlatandeWithInvalidDate;

    @Before
    public void setupDates() {
        fromDate1 = LocalDate.parse("2015-04-08");
        toDate1 = LocalDate.parse("2015-04-18");
        fromDate2 = LocalDate.parse("2015-04-19");
        toDate2 = LocalDate.parse("2015-04-29");
        fromDate3 = LocalDate.parse("2015-04-30");
        toDate3 = LocalDate.parse("2015-05-08");
    }

    @Test
    public void testReadIntervalFromOneArbetsformaga() {
        LocalDateInterval giltighet = ArbetsformagaToGiltighet.getGiltighetFromUtlatande(getUtlatandeWithSingleNedsattning());
        assertTrue(giltighet.getFrom().compareTo(fromDate1) == 0);
        assertTrue(giltighet.getTom().compareTo(toDate1) == 0);
    }

    @Test
    public void testReadIntervalFromSeveralArbetsformagor() {
        LocalDateInterval giltighet = ArbetsformagaToGiltighet.getGiltighetFromUtlatande(getUtlatandeWithMultipleNedsattningar());
        assertTrue(giltighet.getFrom().compareTo(fromDate1) == 0);
        assertTrue(giltighet.getTom().compareTo(toDate3) == 0);
    }

    @Test
    public void testReadIntervalReturnsNullOnInvalidDatePresent() {
        LocalDateInterval giltighet = ArbetsformagaToGiltighet.getGiltighetFromUtlatande(getUtlatandeWithInvalidDate());
        assertNull(giltighet);
    }

    private Utlatande getUtlatandeWithSingleNedsattning() {
        Utlatande utlatande = new Utlatande();
        utlatande.setNedsattMed100(new InternalLocalDateInterval(new InternalDate(fromDate1), new InternalDate(toDate1)));
        return utlatande;
    }

    private Utlatande getUtlatandeWithMultipleNedsattningar() {
        Utlatande utlatande = new Utlatande();
        utlatande.setNedsattMed100(new InternalLocalDateInterval(new InternalDate(fromDate1), new InternalDate(toDate1)));
        utlatande.setNedsattMed75(new InternalLocalDateInterval(new InternalDate(fromDate2), new InternalDate(toDate2)));
        utlatande.setNedsattMed50(new InternalLocalDateInterval(new InternalDate(fromDate3), new InternalDate(toDate3)));
        return utlatande;
    }

    private Utlatande getUtlatandeWithInvalidDate() {
        Utlatande utlatande = new Utlatande();
        InternalLocalDateInterval internalLocalDateInterval = new InternalLocalDateInterval("2015-08-01", "2015-08-44");
        utlatande.setNedsattMed25(internalLocalDateInterval);
        return utlatande;
    }
}
