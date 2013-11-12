/**
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import se.inera.certificate.common.v1.AktivitetType;
import se.inera.certificate.common.v1.ObservationType;
import se.inera.certificate.common.v1.PatientType;
import se.inera.certificate.common.v1.RekommendationType;
import se.inera.certificate.common.v1.Utlatande;
import se.inera.certificate.model.Arbetsuppgift;
import se.inera.certificate.model.Id;
import se.inera.certificate.model.Kod;
import se.inera.certificate.model.Observation;
import se.inera.certificate.model.Patient;
import se.inera.certificate.model.PatientRelation;
import se.inera.certificate.model.Rekommendation;
import se.inera.certificate.model.Vardenhet;
import se.inera.certificate.model.Vardgivare;
import se.inera.certificate.modules.rli.model.external.Aktivitet;

public class ExternalToTransportConverterTest {

    private static ExternalToTransportConverter converter;

    @Before
    public void setUp() throws Exception {
        converter = new ExternalToTransportConverter();
    }

    @Test
    public void convertPatientExternalToTransportTest() {
        Patient source = buildPatient();
        PatientType patient = converter.convertPatient(source);

        assertEquals("Testvägen 23", patient.getPostadress());
    }

    @Test
    public void convertObservationExternalToTransportTest() {
        Observation source = buildObservation();
        ObservationType obs = converter.convertObservation(source);

        assertEquals("SNOMED-CT", obs.getObservationskod().getCode());
    }

    @Test
    public void convertAktivitetExternalToTransportTest() {
        Aktivitet source = buildAktivitet();
        AktivitetType akt = converter.convertAktivitet(source);
        assertEquals("AktivitetKod", akt.getAktivitetskod().getCode());
        assertEquals("Enhetsnamn", akt.getUtforsVidEnhet().getEnhetsnamn());
        assertEquals("Vårdenhet", akt.getUtforsVidEnhet().getArbetsplatskod().getExtension());
    }

    @Test
    public void convertRekommendationExternalToTransportTest() {
        Rekommendation source = buildRekommendation();
        RekommendationType rType = converter.convertRekommendation(source);
        assertEquals("Rek-kod", rType.getRekommendationskod().getCode());
        assertEquals("Sjuk-kännedom", rType.getSjukdomskannedom().getCode());
        assertEquals("Rek-beskrivning", rType.getBeskrivning());
    }

    @Test
    public void convertKommentarer() {
        se.inera.certificate.modules.rli.model.external.Utlatande source = new se.inera.certificate.modules.rli.model.external.Utlatande();
        List<String> l = new ArrayList<String>();
        l.add("KOMMENTARER");
        source.getKommentarer().addAll(l);

        Utlatande utl = converter.externalToTransport(source);

        List<String> komList = utl.getKommentars();

        assertEquals("KOMMENTARER", komList.get(0));

    }

    private Rekommendation buildRekommendation() {
        Rekommendation rek = new Rekommendation();
        rek.setRekommendationskod(new Kod("Rek-kod"));
        rek.setSjukdomskannedom(new Kod("Sjuk-kännedom"));
        rek.setBeskrivning("Rek-beskrivning");
        return rek;
    }

    private Patient buildPatient() {
        Patient patient = new Patient();
        patient.setPostadress("Testvägen 23");
        patient.setPostnummer("12345");
        patient.setPostort("Teststaden");
        Arbetsuppgift a = new Arbetsuppgift();
        a.setTypAvArbetsuppgift("Testare");
        patient.getArbetsuppgifter().add(a);

        patient.getFornamn().add("Test");
        patient.getMellannamn().add("von");
        patient.setEfternamn("Testsson");

        PatientRelation patientRelation = new PatientRelation();

        List<String> relationAdr = new ArrayList<String>();
        relationAdr.add("Relationsvägen 1");

        Id relationId = new Id();
        relationId.setExtension("relationID");

        List<String> relationForNamn = new ArrayList<String>();
        relationForNamn.add("RelationNamn");

        List<Kod> relationsTyps = new ArrayList<Kod>();
        relationsTyps.add(new Kod("RelationsTypsKod"));

        patientRelation.setEfternamn("RelationEfterNamn");
        patientRelation.getFornamn().addAll(relationForNamn);
        patientRelation.setPersonId(relationId);
        patientRelation.setRelationskategori(new Kod("relationsKategori"));
        patientRelation.getRelationtyper().addAll(relationsTyps);
        patientRelation.setRelationskategori(new Kod("Gifta"));

        patient.getPatientrelationer().add(patientRelation);

        return patient;
    }

    private Observation buildObservation() {
        Observation observation = new Observation();
        observation.setBeskrivning("Observationsbeskrivning");
        observation.setObservationskod(new Kod("SNOMED-CT"));
        return observation;
    }

    /*
     * private Arrangemang buildArrangemang(){ Arrangemang arr = new Arrangemang(); arr.setArrangemangstyp(new
     * Kod("resa")); arr.setBokningsreferens("bokningsreferens"); arr.setPlats("New York"); return arr; }
     */

    private Vardenhet buildEnhet() {
        Vardenhet enhet = new Vardenhet();
        enhet.setArbetsplatskod(new Id(null, "Vårdenhet"));
        enhet.setId(new Id(null, "enhetsId"));
        enhet.setNamn("Enhetsnamn");

        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setId(new Id(null, "vårdgivarID"));
        vardgivare.setNamn("Vårdgivarnamn");

        enhet.setVardgivare(vardgivare);
        return enhet;
    }

    private Aktivitet buildAktivitet() {
        Aktivitet aktivitet = new Aktivitet();
        aktivitet.setAktivitetskod(new Kod("AktivitetKod"));
        aktivitet.setUtforsVid(buildEnhet());
        return aktivitet;
    }

}
