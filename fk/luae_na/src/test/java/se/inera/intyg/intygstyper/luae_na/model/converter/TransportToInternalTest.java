/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.intygstyper.luae_na.model.converter;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDate;

import javax.xml.bind.*;
import javax.xml.transform.stream.StreamSource;

import org.junit.Before;
import org.junit.Test;
import org.oclc.purl.dsdl.svrl.SchematronOutputType;

import com.helger.schematron.svrl.SVRLHelper;

import se.inera.intyg.common.support.model.InternalDate;
import se.inera.intyg.intygstyper.fkparent.integration.RegisterCertificateValidator;
import se.inera.intyg.intygstyper.fkparent.model.converter.IntygTestDataBuilder;
import se.inera.intyg.intygstyper.fkparent.model.internal.*;
import se.inera.intyg.intygstyper.luae_na.model.internal.LuaenaUtlatande;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.ObjectFactory;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateType;

public class TransportToInternalTest {

    private ObjectFactory objectFactory;
    private JAXBContext jaxbContext;
    private RegisterCertificateValidator validator = new RegisterCertificateValidator("luae_na.sch");

    @Before
    public void suitSetup() throws JAXBException {
        jaxbContext = JAXBContext.newInstance(RegisterCertificateType.class);
        objectFactory = new ObjectFactory();
    }

    @Test
    public void endToEnd() throws Exception {
        LuaenaUtlatande originalUtlatande = getUtlatande();
        RegisterCertificateType transportCertificate = InternalToTransport.convert(originalUtlatande);
        LuaenaUtlatande convertedIntyg = TransportToInternal.convert(transportCertificate.getIntyg());

        String xml = xmlToString(transportCertificate);
        SchematronOutputType valResult = validator.validateSchematron(new StreamSource(new StringReader(xml)));

        assertTrue(SVRLHelper.getAllFailedAssertions(valResult).size() == 0);
        assertEquals(originalUtlatande, convertedIntyg);
    }

    private String xmlToString(RegisterCertificateType registerCertificate) throws JAXBException {
        StringWriter stringWriter = new StringWriter();
        JAXBElement<RegisterCertificateType> requestElement = objectFactory.createRegisterCertificate(registerCertificate);
        jaxbContext.createMarshaller().marshal(requestElement, stringWriter);
        return stringWriter.toString();
    }

    private static LuaenaUtlatande getUtlatande() {
        LuaenaUtlatande.Builder utlatande = LuaenaUtlatande.builder();
        utlatande.setId("1234567");
        utlatande.setGrundData(IntygTestDataBuilder.getGrundData());
        utlatande.setTextVersion("1.0");
        utlatande.setUndersokningAvPatienten(new InternalDate(LocalDate.now()));
        utlatande.setKannedomOmPatient(new InternalDate(LocalDate.now()));
        utlatande.setUnderlagFinns(true);
        utlatande.setUnderlag(asList(Underlag.create(Underlag.UnderlagsTyp.OVRIGT, new InternalDate(LocalDate.now()), "plats 1"),
                Underlag.create(Underlag.UnderlagsTyp.UNDERLAG_FRAN_ARBETSTERAPEUT, new InternalDate(LocalDate.now().plusWeeks(2)), "plats 2")));
        utlatande.setSjukdomsforlopp("Snabbt");
        utlatande.setDiagnoser(asList((Diagnos.create("S47", "ICD_10_SE", "Klämskada skuldra", "Klämskada skuldra")),
                Diagnos.create("S48", "ICD_10_SE", "Klämskada arm", "Klämskada arm")));
        utlatande.setDiagnosgrund("Ingen som vet");
        utlatande.setNyBedomningDiagnosgrund(true);
        utlatande.setDiagnosForNyBedomning("Diagnos för ny bedömning");
        utlatande.setFunktionsnedsattningIntellektuell("Bra");
        utlatande.setFunktionsnedsattningKommunikation("Tyst");
        utlatande.setFunktionsnedsattningKoncentration("Noll");
        utlatande.setFunktionsnedsattningPsykisk("Lite ledsen");
        utlatande.setFunktionsnedsattningSynHorselTal("Vitt");
        utlatande.setFunktionsnedsattningBalansKoordination("Tyst");
        utlatande.setFunktionsnedsattningAnnan("Kan inte smida");
        utlatande.setAktivitetsbegransning("Väldigt sjuk");
        utlatande.setPagaendeBehandling("Medicin");
        utlatande.setAvslutadBehandling("Gammal medicin");
        utlatande.setPlaneradBehandling("Mer medicin");
        utlatande.setMedicinskaForutsattningarForArbete("Svårt");
        utlatande.setFormagaTrotsBegransning("Dansa");
        utlatande.setOvrigt("Trevlig kille");
        utlatande.setKontaktMedFk(true);
        utlatande.setAnledningTillKontakt("Känner mig ensam");
        utlatande.setTillaggsfragor(asList(Tillaggsfraga.create("9001", "Svar text 1"), Tillaggsfraga.create("9002", "Svar text 2")));
        return utlatande.build();
    }

}
