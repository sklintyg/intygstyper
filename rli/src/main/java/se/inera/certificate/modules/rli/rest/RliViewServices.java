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
package se.inera.certificate.modules.rli.rest;

import java.util.Arrays;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.joda.time.LocalDateTime;

import se.inera.certificate.modules.rli.model.codes.ArrangemangsTyp;
import se.inera.certificate.modules.rli.model.codes.RekommendationsKod;
import se.inera.certificate.modules.rli.model.codes.SjukdomsKannedom;
import se.inera.certificate.modules.rli.model.internal.Arrangemang;
import se.inera.certificate.modules.rli.model.internal.Graviditet;
import se.inera.certificate.modules.rli.model.internal.HoSPersonal;
import se.inera.certificate.modules.rli.model.internal.KomplikationStyrkt;
import se.inera.certificate.modules.rli.model.internal.OrsakAvbokning;
import se.inera.certificate.modules.rli.model.internal.Patient;
import se.inera.certificate.modules.rli.model.internal.Rekommendation;
import se.inera.certificate.modules.rli.model.internal.Undersokning;
import se.inera.certificate.modules.rli.model.internal.Utlatande;
import se.inera.certificate.modules.rli.model.internal.Vardenhet;
import se.inera.certificate.modules.rli.model.internal.Vardgivare;
/**
 * Mock class for testing purposes
 * @author erik
 *
 */
@Path("/view")
public class RliViewServices {

    @GET
    @Path("/utlatande/{utlatande-id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Utlatande getUtlatande(@PathParam("utlatande-id") String utlatandeId) {
        if (utlatandeId.equals("sjuk")) {
            return buildSjukUtlatande();
        } else if (utlatandeId.equals("gravid")) {
            return buildGravidUtlatande();
        } else {
            // Maybe write a default too?
            return buildSjukUtlatande();
        }
    }

    private Patient buildPatient() {
        Patient pat = new Patient();
        pat.setFornamn("Test");
        pat.setEfternamn("Testsson");
        pat.setPostadress("Teststigen 1");
        pat.setPostnummer("123 45");
        pat.setPostort("Teststaden");
        pat.setPersonid("19121212-1212");
        pat.setFullstandigtNamn("Test Testsson");
        return pat;
    }

    private Arrangemang buildArrangemang() {
        Arrangemang arr = new Arrangemang();
        arr.setArrangemangsdatum("2013-08-22");
        arr.setArrangemangstyp(ArrangemangsTyp.RESA);
        arr.setAvbestallningsdatum("2013-08");
        arr.setPlats("Tegucigalpa");
        arr.setBokningsreferens("12345678-90");
        arr.setBokningsdatum("2013-01-01");
        return arr;
    }

    private HoSPersonal buildHosPers() {
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarid("vardgivareId");
        vardgivare.setVardgivarnamn("Testvårdgivaren");

        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setVardgivare(vardgivare);
        vardenhet.setEnhetsid("1234-567");
        vardenhet.setEnhetsnamn("Tolvberga vårdcentral");
        vardenhet.setPostadress("Tolvstigen 12");
        vardenhet.setPostnummer("12345");
        vardenhet.setPostort("Tolvberga");
        vardenhet.setTelefonnummer("012-345678");
        vardenhet.setEpost("ingen@alls.se");

        HoSPersonal hosPers = new HoSPersonal();
        hosPers.setFullstandigtNamn("Doktor Alban");
        hosPers.setPersonid("19101010-1010");
        hosPers.setVardenhet(vardenhet);
        return hosPers;
    }

    private Utlatande buildGravidUtlatande() {

        Utlatande utlatande = new Utlatande();

        utlatande.setPatient(buildPatient());

        utlatande.setUtlatandeid("f6fb361a-e31d-48b8-8657-99b63912dd9b");
        LocalDateTime signeringsDatum = new LocalDateTime(2013, 8, 12, 11, 25);
        utlatande.setSigneringsdatum(signeringsDatum);
        LocalDateTime skickatDatum = new LocalDateTime(2013, 8, 12, 11, 25, 30);
        utlatande.setSkickatdatum(skickatDatum);
        utlatande.setTypAvUtlatande("RLI");
        utlatande.setKommentarer(Arrays.asList("Övriga upplysningar"));

        utlatande.setArrangemang(buildArrangemang());

        utlatande.setSkapadAv(buildHosPers());

        Rekommendation rekommendation = new Rekommendation();
        rekommendation.setRekommendationskod(RekommendationsKod.REK3);
        rekommendation.setSjukdomskannedom(SjukdomsKannedom.SJK4);
        rekommendation
                .setBeskrivning("Pga diverse medicinska orsaker beräknas förlossningen äga rum tidigare än väntat, vilket var omöjligt att veta i ett tidigare skede");
        utlatande.setRekommendation(rekommendation);

        Undersokning undersokning = new Undersokning();
        undersokning.setOrsakforavbokning(OrsakAvbokning.RESENAR_GRAVID);
        undersokning.setUndersokningsdatum("2013-08-12");
        undersokning.setUndersokningsplats("Tolvberga vårdcentral");
        undersokning.setKomplikationstyrkt(KomplikationStyrkt.AV_HOS_PERSONAL);
        undersokning.setForstaUndersokningsdatum("2010-01");
        undersokning.setForstaUndersokningsdatum("Trestadens lasarett");

        Graviditet graviditet = new Graviditet();
        graviditet.setBeraknatForlossningsdatum("2013-08-14");
        undersokning.setGraviditet(graviditet);

        utlatande.setUndersokning(undersokning);

        return utlatande;

    }

    private Utlatande buildSjukUtlatande() {

        Utlatande utlatande = new Utlatande();

        utlatande.setPatient(buildPatient());

        utlatande.setUtlatandeid("f6fb361a-e31d-48b8-8657-99b63912dd9b");
        LocalDateTime signeringsDatum = new LocalDateTime(2013, 8, 12, 11, 25);
        utlatande.setSigneringsdatum(signeringsDatum);
        LocalDateTime skickatDatum = new LocalDateTime(2013, 8, 12, 11, 25, 30);
        utlatande.setSkickatdatum(skickatDatum);
        utlatande.setTypAvUtlatande("RLI");
        utlatande.setKommentarer(Arrays.asList("Övriga upplysningar"));

        utlatande.setArrangemang(buildArrangemang());

        utlatande.setSkapadAv(buildHosPers());

        Rekommendation rekommendation = new Rekommendation();
        rekommendation.setRekommendationskod(RekommendationsKod.REK1);
        rekommendation.setSjukdomskannedom(SjukdomsKannedom.SJK2);
        utlatande.setRekommendation(rekommendation);

        Undersokning undersokning = new Undersokning();
        undersokning.setOrsakforavbokning(OrsakAvbokning.RESENAR_SJUK);
        undersokning.setUndersokningsdatum("2013-08-12");
        undersokning.setUndersokningsplats("Tolvberga vårdcentral");
        undersokning.setKomplikationstyrkt(KomplikationStyrkt.AV_PATIENT);
        undersokning.setForstaUndersokningsdatum("2010-01");
        undersokning.setForstaUndersokningsdatum("Trestadens lasarett");
        utlatande.setUndersokning(undersokning);

        return utlatande;
    }
}
