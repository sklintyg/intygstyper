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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.inera.certificate.modules.fk7263.model.internal.Fk7263Intyg;
import se.inera.certificate.modules.fk7263.model.internal.Vardperson;
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
     * @param newDraftData {@link CreateNewDraftHolder}
     * @return {@link Fk7263Intyg} or throws a ConverterException if something unforeseen happens
     * @throws ConverterException
     */
    public Fk7263Intyg createNewWebcertDraft(CreateNewDraftHolder newDraftData) throws ConverterException {

        if (newDraftData.getCertificateId() == null) {
            throw new ConverterException("No certificateID found");
        }

        Fk7263Intyg utlatande = new Fk7263Intyg();

        utlatande.setArbetsformataPrognosGarInteAttBedoma(false);
        utlatande.setArbetsformataPrognosJa(false);
        utlatande.setArbetsformataPrognosJaDelvis(false);
        utlatande.setArbetsformataPrognosNej(false);
        utlatande.setArbetsloshet(false);

        utlatande.setAvstangningSmittskydd(false);
        utlatande.setForaldrarledighet(false);
        utlatande.setKontaktMedFk(false);
        utlatande.setRehabiliteringAktuell(false);
        utlatande.setRehabiliteringAktuell(false);
        utlatande.setRehabiliteringEjAktuell(false);
        utlatande.setRehabiliteringGarInteAttBedoma(false);
        utlatande.setRekommendationKontaktArbetsformedlingen(false);
        utlatande.setRekommendationKontaktForetagshalsovarden(false);
        utlatande.setRessattTillArbeteAktuellt(false);
        utlatande.setRessattTillArbeteEjAktuellt(false);

        utlatande.setId(newDraftData.getCertificateId());

        populateWithSkapadAv(utlatande, newDraftData.getSkapadAv());

        populateWithPatientInfo(utlatande, newDraftData.getPatient());

        return utlatande;
    }

    /**
     * Create a new TS-bas draft pre-populated with the attached data.
     *
     * @param newDraftData
     *            {@link CreateNewDraftHolder}
     * @return {@link se.inera.certificate.modules.fk7263.model.internal.Fk7263Intyg} or throws a ConverterException if something unforeseen happens
     * @throws ConverterException
     */
    public Fk7263Intyg createNewWebcertDraftFromTemplate(CreateNewDraftHolder newDraftData, Fk7263Intyg template) throws ConverterException {
        LOG.trace("Creating copy with id {} from {}", newDraftData.getCertificateId(), template.getId());

        template.setId(newDraftData.getCertificateId());

        populateWithSkapadAv(template, newDraftData.getSkapadAv());
        populateWithPatientInfo(template, newDraftData.getPatient());

        return template;
    }

    private void populateWithPatientInfo(Fk7263Intyg utlatande,
                                         Patient patient) throws ConverterException {

        if (patient == null) {
            throw new ConverterException("Got null while trying to populateWithPatientInfo");
        }

        utlatande.setPatientNamn(patient.getFullstandigtNamn());
        utlatande.setPatientPersonnummer(patient.getPersonnummer());
    }

    private void populateWithSkapadAv(Fk7263Intyg utlatande, se.inera.certificate.modules.support.api.dto.HoSPersonal hoSPersonal)
            throws ConverterException {
        if (hoSPersonal == null) {
            throw new ConverterException("Got null while trying to populateWithSkapadAv");
        }

        utlatande.setVardperson(convertHosPersonalToEdit(hoSPersonal));
    }

    private Vardperson convertHosPersonalToEdit(se.inera.certificate.modules.support.api.dto.HoSPersonal hoSPersonal) throws ConverterException {
        Vardperson vardperson = new Vardperson();
        vardperson.setNamn(hoSPersonal.getNamn());
        vardperson.setHsaId(hoSPersonal.getHsaId());
        vardperson.setForskrivarKod(hoSPersonal.getForskrivarkod());

        vardperson.setEnhetsId(hoSPersonal.getVardenhet().getHsaId());
        vardperson.setEnhetsnamn(hoSPersonal.getVardenhet().getNamn());

        vardperson.setPostadress(hoSPersonal.getVardenhet().getPostadress());
        vardperson.setPostnummer(hoSPersonal.getVardenhet().getPostnummer());
        vardperson.setPostort(hoSPersonal.getVardenhet().getPostort());
        vardperson.setTelefonnummer(hoSPersonal.getVardenhet().getTelefonnummer());
        vardperson.setEpost(hoSPersonal.getVardenhet().getEpost());
        vardperson.setArbetsplatsKod(hoSPersonal.getVardenhet().getArbetsplatskod());

        vardperson.setVardgivarId(hoSPersonal.getVardenhet().getVardgivare().getHsaId());
        vardperson.setVardgivarnamn(hoSPersonal.getVardenhet().getVardgivare().getNamn());

        return vardperson;
    }

    public void updateSkapadAv(Fk7263Intyg utlatande, HoSPersonal hosPerson) {
        utlatande.getVardperson().setHsaId(hosPerson.getHsaId());
        utlatande.getVardperson().setNamn(hosPerson.getNamn());
        utlatande.getVardperson().setForskrivarKod(hosPerson.getForskrivarkod());
    }
}
