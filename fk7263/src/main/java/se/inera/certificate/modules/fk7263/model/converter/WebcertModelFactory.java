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
package se.inera.certificate.modules.fk7263.model.converter;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.certificate.model.common.internal.Vardenhet;
import se.inera.certificate.model.common.internal.Vardgivare;
import se.inera.certificate.model.converter.util.ConverterException;
import se.inera.certificate.model.converter.util.WebcertModelFactoryUtil;
import se.inera.certificate.modules.fk7263.model.internal.Fk7263Intyg;
import se.inera.certificate.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.certificate.modules.support.api.dto.HoSPersonal;
import se.inera.certificate.modules.support.api.dto.Patient;

/**
 * Factory for creating a editable model.
 */
public class WebcertModelFactory {
    private static final Logger LOG = LoggerFactory.getLogger(WebcertModelFactory.class);

    /**
     * Create a new FK7263 draft pre-populated with the attached data.
     *
     * @param newDraftData
     *            {@link CreateNewDraftHolder}
     * @param template
     *            A template to use as a base, or <code>null</code> if an empty internal model should be used.
     *
     * @return {@link Fk7263Intyg} or throws a ConverterException if something unforeseen happens
     * @throws ConverterException
     */
    public Fk7263Intyg createNewWebcertDraft(CreateNewDraftHolder newDraftData, Fk7263Intyg template) throws ConverterException {
        if (template == null) {
            LOG.trace("Creating draft with id {}", newDraftData.getCertificateId());

            template = new Fk7263Intyg();
            template.setArbetsformataPrognosGarInteAttBedoma(false);
            template.setArbetsformataPrognosJa(false);
            template.setArbetsformataPrognosJaDelvis(false);
            template.setArbetsformataPrognosNej(false);
            template.setNuvarandeArbete(true);
            template.setArbetsloshet(false);

            template.setAvstangningSmittskydd(false);
            template.setForaldrarledighet(false);
            template.setKontaktMedFk(false);
            template.setRehabiliteringAktuell(false);
            template.setRehabiliteringAktuell(false);
            template.setRehabiliteringEjAktuell(false);
            template.setRehabiliteringGarInteAttBedoma(false);
            template.setRekommendationKontaktArbetsformedlingen(false);
            template.setRekommendationKontaktForetagshalsovarden(false);
            template.setRessattTillArbeteAktuellt(false);
            template.setRessattTillArbeteEjAktuellt(false);

        } else {
            LOG.trace("Creating copy with id {} from {}", newDraftData.getCertificateId(), template.getId());
        }

        if (newDraftData.getCertificateId() == null) {
            throw new ConverterException("No certificateID found");
        }

        template.setId(newDraftData.getCertificateId());

        populateWithSkapadAv(template, newDraftData.getSkapadAv());
        populateWithPatientInfo(template, newDraftData.getPatient());

        return template;
    }

    private void populateWithPatientInfo(Fk7263Intyg utlatande, Patient patient) throws ConverterException {
        if (patient == null) {
            throw new ConverterException("Got null while trying to populateWithPatientInfo");
        }

        utlatande.getIntygMetadata().setPatient(WebcertModelFactoryUtil.convertPatientToEdit(patient));
    }

    private void populateWithSkapadAv(Fk7263Intyg utlatande, se.inera.certificate.modules.support.api.dto.HoSPersonal hoSPersonal)
            throws ConverterException {
        if (hoSPersonal == null) {
            throw new ConverterException("Got null while trying to populateWithSkapadAv");
        }

        utlatande.getIntygMetadata().setSkapadAv(WebcertModelFactoryUtil.convertHosPersonalToEdit(hoSPersonal));
    }

    public void updateSkapadAv(Fk7263Intyg utlatande, HoSPersonal hosPerson, LocalDateTime signeringsdatum) {
        utlatande.getIntygMetadata().getSkapadAv().setPersonId(hosPerson.getHsaId());
        utlatande.getIntygMetadata().getSkapadAv().setFullstandigtNamn(hosPerson.getNamn());
        utlatande.getIntygMetadata().getSkapadAv().setForskrivarKod(hosPerson.getForskrivarkod());
        utlatande.getIntygMetadata().setSigneringsdatum(signeringsdatum);
    }
}
