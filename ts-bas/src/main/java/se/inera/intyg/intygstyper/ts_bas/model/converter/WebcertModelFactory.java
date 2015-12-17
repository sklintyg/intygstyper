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
package se.inera.intyg.intygstyper.ts_bas.model.converter;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.intyg.common.support.model.converter.util.ConverterException;
import se.inera.intyg.common.support.model.converter.util.WebcertModelFactoryUtil;
import se.inera.intyg.common.support.modules.support.api.dto.CreateDraftCopyHolder;
import se.inera.intyg.common.support.modules.support.api.dto.CreateNewDraftHolder;
import se.inera.intyg.common.support.modules.support.api.dto.HoSPersonal;
import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;
import se.inera.intyg.intygstyper.ts_bas.model.internal.Utlatande;
import se.inera.intyg.intygstyper.ts_bas.support.TsBasEntryPoint;

/**
 * Factory for creating a editable model.
 */
public class WebcertModelFactory {
    private static final Logger LOG = LoggerFactory.getLogger(WebcertModelFactory.class);

    /**
     * Create a new TS-bas draft pre-populated with the attached data.
     *
     * @param newDraftData
     *            {@link CreateNewDraftHolder}
     * @param template
     *            A template to use as a base, or <code>null</code> if an empty internal model should be used.
     *
     * @return {@link Utlatande}
     *
     * @throws se.inera.intyg.common.support.model.converter.util.ConverterException
     *             if something unforeseen happens
     */
    public Utlatande createNewWebcertDraft(CreateNewDraftHolder newDraftData, Utlatande template) throws ConverterException {
        if (template == null) {
            LOG.trace("Creating draft with id {}", newDraftData.getCertificateId());
            template = new Utlatande();

        } else {
            LOG.trace("Creating copy with id {} from {}", newDraftData.getCertificateId(), template.getId());
        }

        template.setId(newDraftData.getCertificateId());

        template.setTyp(TsBasEntryPoint.MODULE_ID);

        populateWithSkapadAv(template, newDraftData.getSkapadAv());
        populateWithPatientInfo(template, newDraftData.getPatient());

        return template;
    }

    private void populateWithPatientInfo(Utlatande utlatande,
            se.inera.intyg.common.support.modules.support.api.dto.Patient patient) throws ConverterException {

        if (patient == null) {
            throw new ConverterException("Got null while trying to populateWithPatientInfo");
        }

        utlatande.getGrundData().setPatient(WebcertModelFactoryUtil.convertPatientToEdit(patient));
    }

    private void populateWithSkapadAv(Utlatande utlatande,
            se.inera.intyg.common.support.modules.support.api.dto.HoSPersonal skapadAv) throws ConverterException {
        if (skapadAv == null) {
            throw new ConverterException("Got null while trying to populateWithSkapadAv");
        }

        utlatande.getGrundData().setSkapadAv(WebcertModelFactoryUtil.convertHosPersonalToEdit(skapadAv));
    }

    public Utlatande createCopy(CreateDraftCopyHolder copyData, Utlatande template) throws ConverterException {
        LOG.trace("Creating copy with id {} from {}", copyData.getCertificateId(), template.getId());

        populateWithId(template, copyData.getCertificateId());
        populateWithSkapadAv(template, copyData.getSkapadAv());

        if (copyData.hasPatient()) {
            populateWithPatientInfo(template, copyData.getPatient());
        }

        if (copyData.hasNewPersonnummer()) {
            populateWithNewPersonnummer(template, copyData.getNewPersonnummer());
        }

        return template;
    }

    private void populateWithNewPersonnummer(Utlatande template, Personnummer newPersonnummer) {
        template.getGrundData().getPatient().setPersonId(newPersonnummer);
    }

    private void populateWithId(Utlatande utlatande, String utlatandeId) throws ConverterException {

        if (utlatandeId == null) {
            throw new ConverterException("No certificateID found");
        }

        utlatande.setId(utlatandeId);
    }

    public void updateSkapadAv(Utlatande utlatande, HoSPersonal hosPerson, LocalDateTime signeringsdatum) {
        utlatande.getGrundData().getSkapadAv().setPersonId(hosPerson.getHsaId());
        utlatande.getGrundData().getSkapadAv().setFullstandigtNamn(hosPerson.getNamn());
        utlatande.getGrundData().getSkapadAv().setForskrivarKod(hosPerson.getForskrivarkod());
        utlatande.getGrundData().setSigneringsdatum(signeringsdatum);
        utlatande.getGrundData().getSkapadAv().getBefattningar().clear();
        if (hosPerson.getBefattning() != null) {
            utlatande.getGrundData().getSkapadAv().getBefattningar().add(hosPerson.getBefattning());
        }
        utlatande.getGrundData().getSkapadAv().getSpecialiteter().clear();
        if (hosPerson.getSpecialiseringar() != null) {
            utlatande.getGrundData().getSkapadAv().getSpecialiteter().addAll(hosPerson.getSpecialiseringar());
        }
    }
}
