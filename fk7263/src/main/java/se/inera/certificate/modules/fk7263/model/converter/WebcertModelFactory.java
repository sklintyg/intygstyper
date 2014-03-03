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

import org.apache.commons.lang3.StringUtils;

import se.inera.certificate.modules.fk7263.model.internal.Fk7263Intyg;
import se.inera.certificate.modules.fk7263.model.internal.Vardperson;
import se.inera.certificate.modules.fk7263.rest.dto.CreateNewDraftCertificateHolder;
import se.inera.certificate.modules.fk7263.rest.dto.HoSPersonal;

/**
 * Factory for creating a editable model.
 */
public class WebcertModelFactory {
    /**
     * Create a new FK7263 draft pre-populated with the attached data
     * 
     * @param newDraftData
     *            {@link CreateNewDraftCertificateHolder}
     * @return {@link Utlatande} or throws a ConverterException if something unforeseen happens
     * @throws ConverterException
     */
    public Fk7263Intyg createNewWebcertDraft(CreateNewDraftCertificateHolder newDraftData) throws ConverterException {

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

        populateWithPatientInfo(utlatande, newDraftData.getPatientInfo());

        return utlatande;
    }

    private void populateWithPatientInfo(Fk7263Intyg utlatande,
            se.inera.certificate.modules.fk7263.rest.dto.Patient patient) throws ConverterException {

        if (patient == null) {
            throw new ConverterException("Got null while trying to populateWithPatientInfo");
        }

        utlatande.setPatientNamn(StringUtils.join(patient.getFornamn(), " ", patient.getEfternamn()));
        utlatande.setPatientPersonnummer(patient.getPersonnummer());
    }

    private void populateWithSkapadAv(Fk7263Intyg utlatande,
            se.inera.certificate.modules.fk7263.rest.dto.HoSPersonal skapadAv) throws ConverterException {
        if (skapadAv == null) {
            throw new ConverterException("Got null while trying to populateWithSkapadAv");
        }

        utlatande.setVardperson(convertHosPersonalToEdit(skapadAv));
    }

    private Vardperson convertHosPersonalToEdit(HoSPersonal skapadAv) throws ConverterException {
        Vardperson vardperson = new Vardperson();
        vardperson.setNamn(skapadAv.getNamn());
        vardperson.setHsaId(skapadAv.getHsaId());
        vardperson.setForskrivarKod(skapadAv.getForskrivarkod());

        if (skapadAv.getVardenhet() != null) {
            vardperson.setEnhetsId(skapadAv.getVardenhet().getHsaId());
            vardperson.setEnhetsnamn(skapadAv.getVardenhet().getNamn());

            vardperson.setPostadress(skapadAv.getVardenhet().getPostadress());
            vardperson.setPostnummer(skapadAv.getVardenhet().getPostnummer());
            vardperson.setPostort(skapadAv.getVardenhet().getPostort());
            vardperson.setTelefonnummer(skapadAv.getVardenhet().getTelefonnummer());
        }

        if (skapadAv.getVardenhet() != null && skapadAv.getVardenhet().getVardgivare() != null) {
            vardperson.setVardgivarId(skapadAv.getVardenhet().getVardgivare().getHsaId());
            vardperson.setVardgivarnamn(skapadAv.getVardenhet().getVardgivare().getNamn());
        }

        return vardperson;
    }

}
