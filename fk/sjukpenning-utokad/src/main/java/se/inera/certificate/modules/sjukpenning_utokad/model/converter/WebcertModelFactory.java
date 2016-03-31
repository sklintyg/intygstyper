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

package se.inera.certificate.modules.sjukpenning_utokad.model.converter;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import se.inera.certificate.modules.sjukpenning_utokad.model.internal.SjukpenningUtokadUtlatande;
import se.inera.certificate.modules.sjukpenning_utokad.model.internal.SjukpenningUtokadUtlatande.Builder;
import se.inera.certificate.modules.sjukpenning_utokad.support.SjukpenningUtokadEntryPoint;
import se.inera.intyg.common.services.texts.IntygTextsService;
import se.inera.intyg.common.support.model.common.internal.GrundData;
import se.inera.intyg.common.support.model.common.internal.Relation;
import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.model.converter.util.WebcertModelFactoryUtil;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.intyg.common.support.modules.support.api.dto.HoSPersonal;
import se.inera.intyg.common.support.modules.support.api.dto.Patient;
import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;

/**
 * Factory for creating an editable model.
 */
public class WebcertModelFactory {
    private static final Logger LOG = LoggerFactory.getLogger(WebcertModelFactory.class);

    @Autowired(required = false)
    private IntygTextsService intygTexts;

    /**
     * Create a new sjukpenning-utokad draft pre-populated with the attached data.
     *
     * @param newDraftData
     *            {@link CreateNewDraftHolder}
     * @return {@link SjukpenningUtokadUtlatande} or throws a ConverterException if something unforeseen happens
     * @throws ConverterException
     */
    public SjukpenningUtokadUtlatande createNewWebcertDraft(CreateNewDraftHolder newDraftData) throws ConverterException {

        LOG.trace("Creating draft with id {}", newDraftData.getCertificateId());

        Builder template = SjukpenningUtokadUtlatande.builder();
        GrundData grundData = new GrundData();

        populateWithId(template, newDraftData.getCertificateId());
        populateWithSkapadAv(grundData, newDraftData.getSkapadAv());
        populateWithPatientInfo(grundData, newDraftData.getPatient());

        // Default to latest version available of intyg
        template.setTextVersion(intygTexts.getLatestVersion(SjukpenningUtokadEntryPoint.MODULE_ID));

        return template.setGrundData(grundData).build();
    }

    public SjukpenningUtokadUtlatande createCopy(CreateDraftCopyHolder copyData, SjukpenningUtokadUtlatande template) throws ConverterException {

        LOG.trace("Creating copy with id {} from {}", copyData.getCertificateId(), template.getId());

        SjukpenningUtokadUtlatande.Builder templateBuilder = template.toBuilder();
        GrundData grundData = template.getGrundData();

        populateWithId(templateBuilder, copyData.getCertificateId());
        populateWithSkapadAv(grundData, copyData.getSkapadAv());
        populateWithRelation(grundData, copyData.getRelation());

        if (copyData.hasPatient()) {
            populateWithPatientInfo(grundData, copyData.getPatient());
        }

        if (copyData.hasNewPersonnummer()) {
            populateWithNewPersonnummer(grundData, copyData.getNewPersonnummer());
        }

        resetDataInCopy(grundData);

        return templateBuilder.build();
    }

    private void populateWithId(Builder utlatande, String utlatandeId) throws ConverterException {
        if (utlatandeId == null) {
            throw new ConverterException("No certificateID found");
        }
        utlatande.setId(utlatandeId);
    }

    private void populateWithNewPersonnummer(GrundData grundData, Personnummer newPersonnummer) {
        grundData.getPatient().setPersonId(newPersonnummer);
    }

    private void populateWithPatientInfo(GrundData grundData, Patient patient) throws ConverterException {
        if (patient == null) {
            throw new ConverterException("Got null while trying to populateWithPatientInfo");
        }
        grundData.setPatient(WebcertModelFactoryUtil.convertPatientToEdit(patient));
    }

    private void populateWithSkapadAv(GrundData grundData, HoSPersonal hoSPersonal) throws ConverterException {
        if (hoSPersonal == null) {
            throw new ConverterException("Got null while trying to populateWithSkapadAv");
        }
        grundData.setSkapadAv(WebcertModelFactoryUtil.convertHosPersonalToEdit(hoSPersonal));
    }

    private void populateWithRelation(GrundData grundData, Relation relation) {
        if (relation != null) {
            grundData.setRelation(relation);
        } else {
            grundData.setRelation(null);
        }
    }

    private void resetDataInCopy(GrundData grundData) {
        grundData.setSigneringsdatum(null);
    }

    public void updateSkapadAv(SjukpenningUtokadUtlatande utlatande, HoSPersonal hosPerson, LocalDateTime signeringsdatum) {
        utlatande.getGrundData().getSkapadAv().setPersonId(hosPerson.getHsaId());
        utlatande.getGrundData().getSkapadAv().setFullstandigtNamn(hosPerson.getNamn());
        utlatande.getGrundData().getSkapadAv().setForskrivarKod(hosPerson.getForskrivarkod());
        utlatande.getGrundData().setSigneringsdatum(signeringsdatum);
    }
}
