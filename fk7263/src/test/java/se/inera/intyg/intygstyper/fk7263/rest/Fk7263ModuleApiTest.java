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

package se.inera.intyg.intygstyper.fk7263.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.intyg.common.support.common.enumerations.Recipients.FK;
import static se.inera.intyg.common.support.modules.converter.InternalConverterUtil.aDatePeriod;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.*;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3.wsaddressing10.AttributedURIType;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.databind.ObjectMapper;

import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificate.rivtabp20.v3.RegisterMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;
import se.inera.intyg.common.schemas.insuranceprocess.healthreporting.utils.ResultOfCallUtil;
import se.inera.intyg.common.support.modules.support.api.dto.*;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.util.integration.integration.json.CustomObjectMapper;
import se.inera.intyg.intygstyper.fk7263.model.converter.InternalToTransport;
import se.inera.intyg.intygstyper.fk7263.model.internal.Utlatande;
import se.inera.intyg.intygstyper.fk7263.utils.ResourceConverterUtils;
import se.inera.intyg.intygstyper.fk7263.utils.ScenarioNotFoundException;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.IntygId;
import se.riv.clinicalprocess.healthcond.certificate.v2.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v2.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v2.Svar.Delsvar;

/**
 * @author andreaskaltenbach
 */

@ContextConfiguration(locations = ("/fk7263-test-config.xml"))
@RunWith(SpringJUnit4ClassRunner.class)
public class Fk7263ModuleApiTest {

    public static final String TESTFILE_UTLATANDE         = "Fk7263ModuleApiTest/utlatande.json";
    public static final String TESTFILE_UTLATANDE_MINIMAL = "Fk7263ModuleApiTest/utlatande-minimal.json";
    public static final String TESTFILE_UTLATANDE_FAIL    = "Fk7263ModuleApiTest/utlatande-fail.json";

    private RegisterMedicalCertificateResponderInterface registerMedicalCertificateClient;

    @Autowired
    private Fk7263ModuleApi fk7263ModuleApi;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setUpMocks() {
        registerMedicalCertificateClient = Mockito.mock(RegisterMedicalCertificateResponderInterface.class);
        fk7263ModuleApi.setRegisterMedicalCertificateClient(registerMedicalCertificateClient);
    }

    @Test
    public void testPdfFileName() {
        Utlatande intyg = new Utlatande();
        se.inera.intyg.common.support.model.common.internal.Patient patient = new se.inera.intyg.common.support.model.common.internal.Patient();
        patient.setPersonId(new Personnummer("19121212-1212"));
        intyg.getGrundData().setPatient(patient);
        // TODO Create a proper test when model has been updated.
        // assertEquals("lakarutlatande_19121212-1212_20110124-20110331.pdf", fk7263ModuleApi.pdfFileName(intyg));
    }

    @Test
    public void updateChangesHosPersonalInfo() throws IOException, ModuleException {
        Utlatande utlatande = getUtlatandeFromFile();

        InternalModelHolder holder = createInternalHolder(utlatande);
        Vardgivare vardgivare = new Vardgivare("vardgivarId", "vardgivarNamn");
        Vardenhet vardenhet = new Vardenhet("enhetId", "enhetNamn", "", "", "", "", "", "", vardgivare);
        HoSPersonal hosPerson = new HoSPersonal("nyId", "nyNamn", "nyForskrivarkod", "nyBefattning", null, vardenhet);
        LocalDateTime signingDate = LocalDateTime.parse("2014-08-01");
        InternalModelResponse updatedHolder = fk7263ModuleApi.updateBeforeSigning(holder, hosPerson, signingDate);
        Utlatande updatedIntyg = objectMapper.readValue(updatedHolder.getInternalModel(), Utlatande.class);

        assertEquals(signingDate, updatedIntyg.getGrundData().getSigneringsdatum());
        assertEquals("nyId", updatedIntyg.getGrundData().getSkapadAv().getPersonId());
        assertEquals("nyNamn", updatedIntyg.getGrundData().getSkapadAv().getFullstandigtNamn());
        assertEquals("nyForskrivarkod", updatedIntyg.getGrundData().getSkapadAv().getForskrivarKod());
        assertEquals(utlatande.getGrundData().getSkapadAv().getVardenhet().getEnhetsnamn(), updatedIntyg.getGrundData().getSkapadAv().getVardenhet()
                .getEnhetsnamn());
    }

    @Test
    public void copyContainsOriginalData() throws IOException, ModuleException {
        Utlatande utlatande = getUtlatandeFromFile();

        Patient patient = new Patient("Kalle", null, "Kula", new Personnummer("19121212-1212"), null, null, null);
        CreateDraftCopyHolder copyHolder = createDraftCopyHolder(patient);

        InternalModelResponse holder = fk7263ModuleApi.createNewInternalFromTemplate(copyHolder, createInternalHolder(utlatande));

        assertNotNull(holder);
        Utlatande creatededUtlatande = ResourceConverterUtils.toInternal(holder.getInternalModel());
        assertEquals("2011-03-07", creatededUtlatande.getNedsattMed50().getFrom().getDate());
        assertEquals("Kalle", creatededUtlatande.getGrundData().getPatient().getFornamn());
        assertEquals("Kula", creatededUtlatande.getGrundData().getPatient().getEfternamn());
    }

    @Test
    public void copyContainsOriginalPersondetails() throws IOException, ModuleException {
        Utlatande utlatande = getUtlatandeFromFile();

        // create copyholder without Patient in it
        CreateDraftCopyHolder copyHolder = createDraftCopyHolder(null);

        InternalModelResponse holder = fk7263ModuleApi.createNewInternalFromTemplate(copyHolder, createInternalHolder(utlatande));

        assertNotNull(holder);
        Utlatande creatededUtlatande = ResourceConverterUtils.toInternal(holder.getInternalModel());
        assertEquals("Test Testorsson", creatededUtlatande.getGrundData().getPatient().getEfternamn());
    }

    @Test
    public void copyContainsNewPersonnummer() throws IOException, ModuleException {

        Personnummer newSSN = new Personnummer("19121212-1414");

        Utlatande utlatande = getUtlatandeFromFile();

        Patient patient = new Patient("Kalle", null, "Kula", new Personnummer("19121212-1212"), null, null, null);
        CreateDraftCopyHolder copyHolder = createDraftCopyHolder(patient);
        copyHolder.setNewPersonnummer(newSSN);

        InternalModelResponse holder = fk7263ModuleApi.createNewInternalFromTemplate(copyHolder, createInternalHolder(utlatande));
        assertNotNull(holder);

        Utlatande creatededUtlatande = ResourceConverterUtils.toInternal(holder.getInternalModel());
        assertEquals("Kalle", creatededUtlatande.getGrundData().getPatient().getFornamn());
        assertEquals("Kula", creatededUtlatande.getGrundData().getPatient().getEfternamn());
        assertEquals(newSSN, creatededUtlatande.getGrundData().getPatient().getPersonId());
    }

    @Test
    public void testSendCertificateWhenRecipientIsOtherThanFk() throws Exception {
        InternalModelHolder internalModel = new InternalModelHolder(FileUtils.readFileToString(new ClassPathResource(
                TESTFILE_UTLATANDE).getFile()));

        AttributedURIType address = new AttributedURIType();
        address.setValue("logicalAddress");

        RegisterMedicalCertificateResponseType response = new RegisterMedicalCertificateResponseType();
        response.setResult(ResultOfCallUtil.okResult());

        // Wen
        when(registerMedicalCertificateClient.registerMedicalCertificate(
                any(AttributedURIType.class), any(RegisterMedicalCertificateType.class))).thenReturn(response);

        // Then
        fk7263ModuleApi.sendCertificateToRecipient(internalModel, "logicalAddress", null);

        // Verify
        verify(registerMedicalCertificateClient).registerMedicalCertificate(eq(address), Mockito.any(RegisterMedicalCertificateType.class));
    }

    @Test
    public void testSendFullCertificateWhenRecipientIsFk() throws Exception {

        // Given
        InternalModelHolder internalModel = new InternalModelHolder(FileUtils.readFileToString(new ClassPathResource(
                TESTFILE_UTLATANDE).getFile()));

        AttributedURIType address = new AttributedURIType();
        address.setValue("logicalAddress");

        RegisterMedicalCertificateResponseType response = new RegisterMedicalCertificateResponseType();
        response.setResult(ResultOfCallUtil.okResult());

        // When
        when(registerMedicalCertificateClient.registerMedicalCertificate(
                any(AttributedURIType.class), any(RegisterMedicalCertificateType.class))).thenReturn(response);

        // Then
        fk7263ModuleApi.sendCertificateToRecipient(internalModel, "logicalAddress", FK.toString());

        // Verify
        verify(registerMedicalCertificateClient).registerMedicalCertificate(Mockito.eq(address), any(RegisterMedicalCertificateType.class));
    }

    @Test
    public void testSendMinimalCertificateWhenRecipientIsFk() throws Exception {

        // Given
        InternalModelHolder internalModel = new InternalModelHolder(FileUtils.readFileToString(new ClassPathResource(
                TESTFILE_UTLATANDE_MINIMAL).getFile()));

        AttributedURIType address = new AttributedURIType();
        address.setValue("logicalAddress");

        RegisterMedicalCertificateResponseType response = new RegisterMedicalCertificateResponseType();
        response.setResult(ResultOfCallUtil.okResult());

        // When
        when(registerMedicalCertificateClient.registerMedicalCertificate(
                any(AttributedURIType.class), any(RegisterMedicalCertificateType.class))).thenReturn(response);

        // Then
        fk7263ModuleApi.sendCertificateToRecipient(internalModel, "logicalAddress", FK.toString());

        // Verify
        verify(registerMedicalCertificateClient).registerMedicalCertificate(eq(address), any(RegisterMedicalCertificateType.class));
    }

    @Test(expected = ModuleException.class)
    public void whenFkIsRecipientAndBadCertificateThenThrowException() throws Exception {

        // Given
        InternalModelHolder internalModel = new InternalModelHolder(FileUtils.readFileToString(new ClassPathResource(
                TESTFILE_UTLATANDE_FAIL).getFile()));

        AttributedURIType address = new AttributedURIType();
        address.setValue("logicalAddress");

        // Then
        fk7263ModuleApi.sendCertificateToRecipient(internalModel, "logicalAddress", FK.toString());
    }

    @Test
    public void whenFkIsRecipientThenSetCodeSystemToICD10() throws Exception {
        Utlatande utlatande = getUtlatandeFromFile();
        RegisterMedicalCertificateType request = InternalToTransport.getJaxbObject(utlatande);

        request = fk7263ModuleApi.whenFkIsRecipientThenSetCodeSystemToICD10(request);

        String expected = Fk7263ModuleApi.CODESYSTEMNAME_ICD10;
        String actual = request.getLakarutlatande().getMedicinsktTillstand().getTillstandskod().getCodeSystemName();
        assertEquals(expected, actual);
    }

    @Test(expected = ModuleException.class)
    public void whenFkIsRecipientAndNotSmittskyddAndNoMedicinsktTillstandThenThrowException() throws Exception {
        Utlatande utlatande = getUtlatandeFromFile();
        RegisterMedicalCertificateType request = InternalToTransport.getJaxbObject(utlatande);

        request.getLakarutlatande().setMedicinsktTillstand(null);

        fk7263ModuleApi.whenFkIsRecipientThenSetCodeSystemToICD10(request);
    }

    @Test(expected = ModuleException.class)
    public void whenFkIsRecipientAndNotSmittskyddAndNoTillstandskodThenThrowException() throws Exception {
        Utlatande utlatande = getUtlatandeFromFile();
        RegisterMedicalCertificateType request = InternalToTransport.getJaxbObject(utlatande);

        request.getLakarutlatande().getMedicinsktTillstand().setTillstandskod(null);

        fk7263ModuleApi.whenFkIsRecipientThenSetCodeSystemToICD10(request);
    }

    @Test
    public void testMarshall() throws ScenarioNotFoundException, ModuleException, IOException, SAXException {
        Utlatande internal = objectMapper.readValue(new ClassPathResource("Fk7263ModuleApiTest/utlatande.json").getFile(), Utlatande.class);
        StringWriter writer = new StringWriter();
        try {
            objectMapper.writeValue(writer, internal);
        } catch (IOException e) {
            throw new ModuleException("Failed to serialize internal model", e);
        }
        String xml = writer.toString();
        String actual = fk7263ModuleApi.marshall(xml);
        String expected = FileUtils.readFileToString(new ClassPathResource("Fk7263ModuleApiTest/utlatande.xml").getFile());

        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreAttributeOrder(true);
        Diff diff = XMLUnit.compareXML(expected, actual);
        diff.overrideDifferenceListener(new NamespacePrefixNameIgnoringListener());
        diff.overrideElementQualifier(new ElementNameAndTextQualifier());
        Assert.assertTrue(diff.toString(), diff.similar());
    }

    @Test
    public void getAdditionalInfoOneTimePeriodTest() throws ModuleException {
        final String fromString = "2015-12-12";
        final String toString = "2016-03-02";
        LocalDate from = LocalDate.parse(fromString);
        LocalDate to = LocalDate.parse(toString);
        Intyg intyg = new Intyg();
        intyg.setIntygsId(new IntygId());
        intyg.getIntygsId().setExtension("intygsId");
        Svar s = new Svar();
        s.setId("32");
        Delsvar delsvar = new Delsvar();
        delsvar.setId("32.2");
        delsvar.getContent().add(aDatePeriod(from, to));
        s.getDelsvar().add(delsvar);
        intyg.getSvar().add(s);

        String result = fk7263ModuleApi.getAdditionalInfo(intyg);

        assertEquals(fromString + " - " + toString, result);
    }

    @Test
    public void getAdditionalInfoMultiplePeriodsTest() throws ModuleException {
        final String fromString = "2015-12-12";
        final String middleDate1 = "2015-12-13";
        final String middleDate2 = "2015-12-14";
        final String middleDate3 = "2015-12-15";
        final String middleDate4 = "2015-12-16";
        final String toString = "2016-03-02";
        LocalDate from = LocalDate.parse(fromString);
        LocalDate to = LocalDate.parse(toString);
        Intyg intyg = new Intyg();
        intyg.setIntygsId(new IntygId());
        intyg.getIntygsId().setExtension("intygsId");
        Svar s = new Svar();
        s.setId("32");
        Delsvar delsvar = new Delsvar();
        delsvar.setId("32.2");
        delsvar.getContent().add(aDatePeriod(LocalDate.parse(middleDate2), LocalDate.parse(middleDate3)));
        s.getDelsvar().add(delsvar);
        Svar s2 = new Svar();
        s2.setId("32");
        Delsvar delsvar2 = new Delsvar();
        delsvar2.setId("32.2");
        delsvar2.getContent().add(aDatePeriod(LocalDate.parse(middleDate4), to));
        s2.getDelsvar().add(delsvar2);
        Svar s3 = new Svar();
        s3.setId("32");
        Delsvar delsvar3 = new Delsvar();
        delsvar3.setId("32.2");
        delsvar3.getContent().add(aDatePeriod(from, LocalDate.parse(middleDate1)));
        s3.getDelsvar().add(delsvar3);
        intyg.getSvar().add(s);
        intyg.getSvar().add(s2);
        intyg.getSvar().add(s3);

        String result = fk7263ModuleApi.getAdditionalInfo(intyg);

        assertEquals(fromString + " - " + toString, result);
    }

    @Test
    public void getAdditionalInfoSvarNotFoundTest() throws ModuleException {
        final String fromString = "2015-12-12";
        final String toString = "2016-03-02";
        LocalDate from = LocalDate.parse(fromString);
        LocalDate to = LocalDate.parse(toString);
        Intyg intyg = new Intyg();
        intyg.setIntygsId(new IntygId());
        intyg.getIntygsId().setExtension("intygsId");
        Svar s = new Svar();
        s.setId("30"); // wrong SvarId
        Delsvar delsvar = new Delsvar();
        delsvar.setId("32.2");
        delsvar.getContent().add(aDatePeriod(from, to));
        s.getDelsvar().add(delsvar);
        intyg.getSvar().add(s);

        String result = fk7263ModuleApi.getAdditionalInfo(intyg);

        assertNull(result);
    }

    @Test
    public void getAdditionalInfoDelSvarNotFoundTest() throws ModuleException {
        final String fromString = "2015-12-12";
        final String toString = "2016-03-02";
        LocalDate from = LocalDate.parse(fromString);
        LocalDate to = LocalDate.parse(toString);
        Intyg intyg = new Intyg();
        intyg.setIntygsId(new IntygId());
        intyg.getIntygsId().setExtension("intygsId");
        Svar s = new Svar();
        s.setId("32");
        Delsvar delsvar = new Delsvar();
        delsvar.setId("32.1"); // wrong delsvarId
        delsvar.getContent().add(aDatePeriod(from, to));
        s.getDelsvar().add(delsvar);
        intyg.getSvar().add(s);

        String result = fk7263ModuleApi.getAdditionalInfo(intyg);

        assertNull(result);
    }

    private class NamespacePrefixNameIgnoringListener implements DifferenceListener {
        @Override
        public int differenceFound(Difference difference) {
            if (DifferenceConstants.NAMESPACE_PREFIX_ID == difference.getId()) {
                // differences in namespace prefix IDs are ok (eg. 'ns1' vs 'ns2'), as long as the namespace URI is the
                // same
                return RETURN_IGNORE_DIFFERENCE_NODES_IDENTICAL;
            } else {
                return RETURN_ACCEPT_DIFFERENCE;
            }
        }

        @Override
        public void skippedComparison(Node control, Node test) {
        }
    }

    private Utlatande getUtlatandeFromFile() throws IOException {
        return new CustomObjectMapper().readValue(new ClassPathResource(
                TESTFILE_UTLATANDE).getFile(), Utlatande.class);
    }

    private String toJsonString(Utlatande utlatande) throws ModuleException {
        StringWriter writer = new StringWriter();
        try {
            objectMapper.writeValue(writer, utlatande);
        } catch (IOException e) {
            throw new ModuleException("Failed to serialize internal model", e);
        }
        return writer.toString();
    }

    private CreateDraftCopyHolder createDraftCopyHolder(Patient patient) {
        Vardgivare vardgivare = new Vardgivare("hsaId0", "vardgivare");
        Vardenhet vardenhet = new Vardenhet("hsaId1", "namn", null, null, null, null, null, null, vardgivare);
        HoSPersonal hosPersonal = new HoSPersonal("Id1", "Grodan Boll", "forskrivarkod", "befattning", null, vardenhet);

        CreateDraftCopyHolder holder = new CreateDraftCopyHolder("Id1", hosPersonal);

        if (patient != null) {
            holder.setPatient(patient);
        }

        return holder;
    }

    private InternalModelHolder createInternalHolder(Utlatande utlatande) throws ModuleException {
        return new InternalModelHolder(toJsonString(utlatande));
    }

}
