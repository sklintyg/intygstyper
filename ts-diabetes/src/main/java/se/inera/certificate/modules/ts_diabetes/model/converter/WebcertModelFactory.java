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
package se.inera.certificate.modules.ts_diabetes.model.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.inera.certificate.model.converter.util.ConverterException;
import se.inera.certificate.model.converter.util.WebcertModelFactoryUtil;
import se.inera.certificate.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.certificate.modules.ts_diabetes.model.codes.UtlatandeKod;
import se.inera.certificate.modules.ts_diabetes.model.internal.Utlatande;

/**
 * Factory for creating a editable model.
 */
public class WebcertModelFactory {
    private static final Logger LOG = LoggerFactory.getLogger(WebcertModelFactory.class);

    /**
     * Create a new TS-diabetes draft pre-populated with the attached data.
     *
     * @param newDraftData
     *            {@link CreateNewDraftHolder}
     * @param template
     *            A template to use as a base, or <code>null</code> if an empty internal model should be used.
     *
     * @return {@link Utlatande} or throws a ConverterException if something unforeseen happens
     * @throws ConverterException
     */
    public Utlatande createNewWebcertDraft(CreateNewDraftHolder newDraftData, Utlatande template) throws ConverterException {
        if (template == null) {
            LOG.trace("Creating draft with id {}", newDraftData.getCertificateId());
            template = new Utlatande();

        } else {
            LOG.trace("Creating copy with id {} from {}", newDraftData.getCertificateId(), template.getId());
        }

        template.setId(newDraftData.getCertificateId());

        // This is where we set the concrete tsUtgava and tsVersion of the intyg that is created.
        template.setTyp(UtlatandeKod.getCurrentVersion().name());

        populateWithSkapadAv(template, newDraftData.getSkapadAv());
        populateWithPatientInfo(template, newDraftData.getPatient());

        return template;
    }

    private void populateWithPatientInfo(Utlatande utlatande,
            se.inera.certificate.modules.support.api.dto.Patient patient) throws ConverterException {

        if (patient == null) {
            throw new ConverterException("Got null while trying to populateWithPatientInfo");
        }

        utlatande.getIntygMetadata().setPatient(WebcertModelFactoryUtil.convertPatientToEdit(patient));
    }

    private void populateWithSkapadAv(Utlatande utlatande,
            se.inera.certificate.modules.support.api.dto.HoSPersonal skapadAv) throws ConverterException {
        if (skapadAv == null) {
            throw new ConverterException("Got null while trying to populateWithSkapadAv");
        }

        utlatande.getIntygMetadata().setSkapadAv(WebcertModelFactoryUtil.convertHosPersonalToEdit(skapadAv));
    }
}
