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
package se.inera.certificate.modules.rli.model.converters;

import static org.junit.Assert.assertEquals;
import iso.v21090.dt.v1.CD;
import iso.v21090.dt.v1.II;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;
import org.junit.Before;
import org.junit.Test;

import riv.insuranceprocess.healthreporting._2.EnhetType;
import riv.insuranceprocess.healthreporting._2.HosPersonalType;
import riv.insuranceprocess.healthreporting._2.VardgivareType;
import se.inera.certificate.common.v1.ObservationType;
import se.inera.certificate.common.v1.PartialDateInterval;
import se.inera.certificate.common.v1.PatientType;
import se.inera.certificate.common.v1.UtforarrollType;
import se.inera.certificate.modules.rli.model.external.Arrangemang;
import se.inera.certificate.modules.rli.model.external.Utlatande;
import se.inera.certificate.modules.rli.model.external.common.HosPersonal;
import se.inera.certificate.modules.rli.model.external.common.Observation;
import se.inera.certificate.modules.rli.model.external.common.Patient;

/**
 * Test class for TransportToExternal, contains methods for setting up Utlatande using both the transport model and the
 * external model, and populating each with mock data
 * 
 * @author erik
 * 
 */

public class TransportToExternalConverterTest {

    private TransportToExternalConverterImpl converter;

    private ObservationType buildObservationType() {
        CD cd = new CD();
        cd.setCode("testCode");

        PartialDateInterval pdi = new PartialDateInterval();

        pdi.setFrom(new Partial().with(DateTimeFieldType.dayOfWeek(), 5).with(DateTimeFieldType.hourOfDay(), 12)
                .with(DateTimeFieldType.minuteOfHour(), 20));

        pdi.setTom(new Partial().with(DateTimeFieldType.dayOfWeek(), 6).with(DateTimeFieldType.hourOfDay(), 12)
                .with(DateTimeFieldType.minuteOfHour(), 20));

        ObservationType observation = new ObservationType();
        observation.setObservationskod(cd);
        observation.setObservationsperiod(pdi);

        UtforarrollType utforare = new UtforarrollType();
        utforare.setUtforartyp(cd);
        HosPersonalType hpt = new HosPersonalType();
        hpt.setFullstandigtNamn("Utforar Utforarsson");
        utforare.setAntasAv(hpt);

        observation.setUtforsAv(utforare);
        return observation;
    }

    private PatientType buildPatientType() {
        PatientType pt = new PatientType();
        II patientID = new II();
        patientID.setIdentifierName("patientID");
        patientID.setExtension("patientID");
        pt.setPersonId(patientID);
        pt.setPostadress("Testgatan 23");
        pt.setPostnummer("12345");
        pt.setPostort("Teststaden");
        return pt;
    }

    private se.inera.certificate.rli.v1.Arrangemang buildArrangemang() {

        se.inera.certificate.rli.v1.Arrangemang arr = new se.inera.certificate.rli.v1.Arrangemang();

        PartialDateInterval pdi = new PartialDateInterval();

        pdi.setFrom(new Partial().with(DateTimeFieldType.dayOfWeek(), 5).with(DateTimeFieldType.hourOfDay(), 12)
                .with(DateTimeFieldType.minuteOfHour(), 20));

        pdi.setTom(new Partial().with(DateTimeFieldType.dayOfWeek(), 6).with(DateTimeFieldType.hourOfDay(), 12)
                .with(DateTimeFieldType.minuteOfHour(), 20));
        arr.setArrangemangstid(pdi);

        CD cd = new CD();
        cd.setCode("testCode");
        arr.setArrangemangstyp(cd);

        arr.setBokningsreferens("bokningsReferens");
        arr.setPlats("arrangemangsPlats");

        arr.setAvbestallningsdatum(new Partial().with(DateTimeFieldType.dayOfMonth(), 2)
                .with(DateTimeFieldType.year(), 2002).with(DateTimeFieldType.monthOfYear(), 11));

        arr.setBokningsdatum(new Partial().with(DateTimeFieldType.dayOfMonth(), 1).with(DateTimeFieldType.year(), 2002)
                .with(DateTimeFieldType.monthOfYear(), 12));

        return arr;
    }

    private HosPersonalType buildHosPersonalType() {
        HosPersonalType hsp = new HosPersonalType();
        hsp.setEnhet(buildEnhetType());
        hsp.setForskrivarkod("Förskrivarkod");
        hsp.setFullstandigtNamn("Fullständigt namn");

        II persID = new II();
        persID.setExtension("HosPersonalID");

        hsp.setPersonalId(persID);

        return hsp;
    }

    private EnhetType buildEnhetType() {
        EnhetType enhet = new EnhetType();

        II ap = new II();
        ap.setExtension("AP-kod");
        II enhetID = new II();
        enhetID.setExtension("EnhetID");

        enhet.setArbetsplatskod(ap);
        enhet.setEnhetsId(enhetID);
        enhet.setEnhetsnamn("Enhetsnamn");
        enhet.setVardgivare(buildVardgivareType());

        return enhet;
    }

    private VardgivareType buildVardgivareType() {
        VardgivareType vardgivare = new VardgivareType();
        II id = new II();
        id.setExtension("VardgivarID");
        vardgivare.setVardgivareId(id);
        vardgivare.setVardgivarnamn("Vardgivarnamn");

        return vardgivare;
    }

    private se.inera.certificate.common.v1.Utlatande buildUtlatandeWithoutObservation() {
        se.inera.certificate.common.v1.Utlatande utlatande = new se.inera.certificate.common.v1.Utlatande();

        utlatande.setArrangemang(buildArrangemang());
        utlatande.setPatient(buildPatientType());
        utlatande.setSkapadAv(buildHosPersonalType());

        CD typ = new CD();
        typ.setCode("SNOMED-CT");
        II id = new II();
        id.setExtension("UtlåtandeID");

        utlatande.setTypAvUtlatande(typ);
        utlatande.setUtlatandeId(id);
        return utlatande;
    }

    @Before
    public void setUp() {
        // Initiate converter
        converter = new TransportToExternalConverterImpl();

    }

    @Test
    public void testConvertUtlatandeWithoutObservation() {
        se.inera.certificate.common.v1.Utlatande source = buildUtlatandeWithoutObservation();
        Utlatande utlatande = converter.transportToExternal(source);

        assertEquals(new ArrayList<String>(), utlatande.getObservationer());
    }

    @Test
    public void testConvertPatient() {
        PatientType transportPatient = buildPatientType();

        Patient externalPatient = converter.convertPatient(transportPatient);

        assertEquals(transportPatient.getPersonId().getExtension(), externalPatient.getPersonId().getExtension());

        assertEquals(transportPatient.getPostadress(), externalPatient.getPostadress());

    }

    @Test
    public void testConvertSkapadAv() {

        HosPersonalType transportSkapadAv = new HosPersonalType();
        transportSkapadAv.setFullstandigtNamn("Skapad af Skapadson");

        HosPersonal externalSkapadAv = converter.convertHosPersonal(transportSkapadAv);

        assertEquals(transportSkapadAv.getFullstandigtNamn(), externalSkapadAv.getFullstandigtNamn());

        assertEquals(transportSkapadAv.getEnhet(), externalSkapadAv.getEnhet());

        assertEquals(transportSkapadAv.getForskrivarkod(), externalSkapadAv.getForskrivarkod());

    }

    @Test
    public void testConvertArrangemang() {
        se.inera.certificate.rli.v1.Arrangemang transportArr = buildArrangemang();
        Arrangemang externalArr = converter.convertArrangemang(transportArr);

        assertEquals(transportArr.getBokningsreferens(), externalArr.getBokningsreferens());

        assertEquals(transportArr.getPlats(), externalArr.getPlats());

        assertEquals(transportArr.getArrangemangstid().getFrom(), externalArr.getArrangemangstid().getFrom());

        assertEquals(transportArr.getArrangemangstid().getTom(), externalArr.getArrangemangstid().getTom());

        assertEquals(transportArr.getArrangemangstyp().getCode().toString(), externalArr.getArrangemangstyp().getCode()
                .toString());

        assertEquals(transportArr.getAvbestallningsdatum(), externalArr.getAvbestallningsdatum());

        assertEquals(transportArr.getBokningsdatum(), externalArr.getBokningsdatum());
    }

    @Test
    public void testConvertObservation() {
        ObservationType transportObs = buildObservationType();
        List<ObservationType> transportList = new ArrayList<>();
        List<Observation> externalList = new ArrayList<>();

        transportList.add(transportObs);
        externalList = converter.convertObservations(transportList);
        Observation externalObs = externalList.get(0);

        assertEquals(transportObs.getObservationsperiod().getFrom(), externalObs.getObservationsperiod().getFrom());

        assertEquals(transportObs.getObservationsperiod().getTom(), externalObs.getObservationsperiod().getTom());

        assertEquals(transportObs.getUtforsAv().getAntasAv().getFullstandigtNamn(), externalObs.getUtforsAv()
                .getAntasAv().getFullstandigtNamn());
    }

}
