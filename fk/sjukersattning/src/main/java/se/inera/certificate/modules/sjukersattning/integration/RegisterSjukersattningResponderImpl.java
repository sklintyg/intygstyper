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

package se.inera.certificate.modules.sjukersattning.integration;

import java.io.StringReader;
import java.io.StringWriter;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;

import org.oclc.purl.dsdl.svrl.SchematronOutputType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.inera.certificate.modules.sjukersattning.model.converter.TransportToInternal;
import se.inera.certificate.modules.sjukersattning.model.converter.util.ConverterUtil;
import se.inera.certificate.modules.sjukersattning.model.internal.SjukersattningUtlatande;
import se.inera.certificate.modules.sjukersattning.rest.SjukersattningModuleApi;
import se.inera.intyg.common.support.integration.module.exception.CertificateAlreadyExistsException;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.modules.support.api.CertificateHolder;
import se.inera.intyg.common.support.validate.CertificateValidationException;
import se.inera.intyg.common.util.logging.LogMarkers;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.ObjectFactory;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateResponseType;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.v2.ErrorIdType;

import com.google.common.base.Throwables;
import com.helger.schematron.svrl.SVRLWriter;

public class RegisterSjukersattningResponderImpl implements RegisterCertificateResponderInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterSjukersattningResponderImpl.class);

    private ObjectFactory objectFactory;
    private JAXBContext jaxbContext;

    private RegisterSjukersattningValidator validator = new RegisterSjukersattningValidator("sjukersattning.sch");

    @Autowired
    private SjukersattningModuleApi moduleApi;

    @Autowired
    private ConverterUtil converterUtil;

    @PostConstruct
    public void initializeJaxbContext() throws JAXBException {
        jaxbContext = JAXBContext.newInstance(RegisterCertificateType.class);
        objectFactory = new ObjectFactory();
    }

    @Override
    public RegisterCertificateResponseType registerCertificate(String logicalAddress, RegisterCertificateType registerCertificate) {
        RegisterCertificateResponseType response = new RegisterCertificateResponseType();

        try {
            String xml = xmlToString(registerCertificate);

            SchematronOutputType valResult = validator.validateSchematron(new StreamSource(new StringReader(xml)));
            System.out.println(SVRLWriter.createXMLString(valResult));

            SjukersattningUtlatande utlatande = TransportToInternal.convert(registerCertificate.getIntyg());
            CertificateHolder certificateHolder = converterUtil.toCertificateHolder(utlatande);
            certificateHolder.setOriginalCertificate(xml);

            moduleApi.getModuleContainer().certificateReceived(certificateHolder);

            response.setResult(ResultUtil.okResult());

        } catch (CertificateAlreadyExistsException e) {
            response.setResult(ResultUtil.infoResult("Certificate already exists"));
            String certificateId = registerCertificate.getIntyg().getIntygsId().getExtension();
            String issuedBy = registerCertificate.getIntyg().getSkapadAv().getEnhet().getEnhetsId().getExtension();
            LOGGER.warn(LogMarkers.VALIDATION, "Validation warning for intyg " + certificateId + " issued by " + issuedBy
                    + ": Certificate already exists - ignored.");

        } catch (CertificateValidationException | ConverterException e) {
            response.setResult(ResultUtil.errorResult(ErrorIdType.VALIDATION_ERROR, e.getMessage()));
            LOGGER.error(LogMarkers.VALIDATION, e.getMessage());

        } catch (JAXBException e) {
            LOGGER.error("JAXB error in Webservice: ", e);
            Throwables.propagate(e);

        } catch (Exception e) {
            LOGGER.error("Error in Webservice: ", e);
            Throwables.propagate(e);
        }

        return response;
    }

    private String xmlToString(RegisterCertificateType registerCertificate) throws JAXBException {
        StringWriter stringWriter = new StringWriter();
        JAXBElement<RegisterCertificateType> requestElement = objectFactory.createRegisterCertificate(registerCertificate);
        jaxbContext.createMarshaller().marshal(requestElement, stringWriter);
        return stringWriter.toString();
    }

}
