#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
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
package ${package}.${artifactId}.model.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joda.time.Partial;
import org.junit.Before;
import org.junit.Test;

import se.inera.certificate.model.Id;
import se.inera.certificate.model.Observation;
import se.inera.certificate.model.PartialInterval;
import se.inera.certificate.model.Vardenhet;
import se.inera.certificate.model.Vardgivare;
import ${package}.${artifactId}.model.codes.AktivitetsKod;
import ${package}.${artifactId}.model.codes.CodeConverter;
import ${package}.${artifactId}.model.codes.ObservationsKod;
import ${package}.${artifactId}.model.external.Aktivitet;
import ${package}.${artifactId}.model.internal.mi.KomplikationStyrkt;
import ${package}.${artifactId}.model.internal.mi.OrsakAvbokning;
import ${package}.${artifactId}.model.internal.mi.Undersokning;

/**
 * Unit test for the UndersokningPopulatorImpl class.
 * 
 * @author nikpet
 * 
 */
public class UndersokingPopulatorTest {

    private ExternalToInternalConverter converter;

    @Before
    public void setUp() {
        this.converter = new ExternalToInternalConverter();
    }

    @Test
    public void testPopulateUndersokningRekommendationMedSjukdom() throws Exception {

        Observation sjukObs = constructObservation(ObservationsKod.SJUKDOM);

        List<Observation> observationer = Arrays.asList(sjukObs);

        Undersokning undersokning = new Undersokning();

        converter.populateUndersokningFromObservationer(observationer, undersokning);

        assertNotNull(undersokning);
        assertEquals(OrsakAvbokning.RESENAR_SJUK, undersokning.getOrsakforavbokning());
    }

    @Test
    public void testPopulateUndersokningRekommendationMedGravid() throws Exception {

        Observation sjukObs = constructObservation(ObservationsKod.GRAVIDITET);

        sjukObs.getObservationsperiod().setTom(TestUtils.constructPartial(2013, 12, 24));

        List<Observation> observationer = new ArrayList<Observation>();
        observationer.add(sjukObs);

        Undersokning undersokning = new Undersokning();

        converter.populateUndersokningFromObservationer(observationer, undersokning);

        assertNotNull(undersokning);
        assertEquals(OrsakAvbokning.RESENAR_GRAVID, undersokning.getOrsakforavbokning());
        assertNotNull(undersokning.getGraviditet());
        assertEquals("2013-12-24", undersokning.getGraviditet().getBeraknatForlossningsdatum());
    }

    @Test
    public void testpopulateFromAktiviteter1() throws Exception {

        Aktivitet firstExam = constructAktivitet(AktivitetsKod.FORSTA_UNDERSOKNING, 2012, 5, 0);
        firstExam.setPlats("Sjukhus A");

        Aktivitet currentExam = constructAktivitet(AktivitetsKod.KLINISK_UNDERSOKNING, 2013, 6, 12);
        Vardenhet enhet = constructEnhet("Sjukhus B");
        currentExam.setUtforsVid(enhet);

        List<Aktivitet> aktiviteter = Arrays.asList(currentExam, firstExam);

        Undersokning intUndersokning = new Undersokning();
        converter.populateUndersokningFromAktiviteter(aktiviteter, intUndersokning);

        assertNotNull(intUndersokning);
        assertEquals("2012-05", intUndersokning.getForstaUndersokningsdatum());
        assertEquals("Sjukhus A", intUndersokning.getForstaUndersokningsplats());
        assertEquals(KomplikationStyrkt.AV_PATIENT, intUndersokning.getKomplikationstyrkt());

        assertEquals("2013-06-12", intUndersokning.getUndersokningsdatum());
        assertEquals("Sjukhus B", intUndersokning.getUtforsVid().getEnhetsnamn());
    }

    @Test
    public void testpopulateFromAktiviteter2() throws Exception {

        Aktivitet currentExam = constructAktivitet(AktivitetsKod.KLINISK_UNDERSOKNING, 2013, 6, 12);
        Vardenhet sjukhusEnhetX = constructEnhet("Sjukhus X");
        currentExam.setUtforsVid(sjukhusEnhetX);

        List<Aktivitet> aktiviteter = Arrays.asList(currentExam);

        Undersokning intUndersokning = new Undersokning();
        converter.populateUndersokningFromAktiviteter(aktiviteter, intUndersokning);

        assertNotNull(intUndersokning);
        assertEquals("2013-06-12", intUndersokning.getUndersokningsdatum());
        assertEquals("Sjukhus X", intUndersokning.getUtforsVid().getEnhetsnamn());
        // assertEquals(KomplikationStyrkt.AV_HOS_PERSONAL, intUndersokning.getKomplikationStyrkt());
        assertNull(intUndersokning.getForstaUndersokningsdatum());
        assertNull(intUndersokning.getForstaUndersokningsplats());
    }

    private Observation constructObservation(ObservationsKod obsKod) {

        Observation obs = new Observation();

        PartialInterval obsPeriod = new PartialInterval(null, null);
        obs.setObservationsperiod(obsPeriod);

        obs.setObservationskod(CodeConverter.toKod(obsKod));

        return obs;
    }

    private Vardenhet constructEnhet(String enhetsNamn) {
        Vardenhet enhet = new Vardenhet();
        enhet.setEpost("enhet@epost.se");
        enhet.setId(new Id("root", "enhetsid"));
        enhet.setNamn(enhetsNamn);
        enhet.setPostadress("enhetsadress");
        enhet.setPostnummer("1337");
        enhet.setPostort("Duvbo");
        enhet.setTelefonnummer("555 555");
        enhet.setVardgivare(constructVardgivare());
        return enhet;
    }

    private Vardgivare constructVardgivare() {
        Vardgivare v = new Vardgivare();
        v.setId(new Id("root", "vardgivarid"));
        /** Why not test some scandinavian chars.. */
        v.setNamn("Vårdgivarnamn");
        return v;
    }

    private Aktivitet constructAktivitet(AktivitetsKod aktivitetsKod, int year, int month, int day) {

        Partial from = TestUtils.constructPartial(year, month, day);
        PartialInterval atid = new PartialInterval(from, null);

        Aktivitet a = new Aktivitet();
        a.setAktivitetskod(CodeConverter.toKod(aktivitetsKod));
        a.setAktivitetstid(atid);

        return a;
    }

}
