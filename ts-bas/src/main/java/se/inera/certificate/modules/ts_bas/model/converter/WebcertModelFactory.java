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
package se.inera.certificate.modules.ts_bas.model.converter;

import org.apache.commons.lang3.StringUtils;

import se.inera.certificate.modules.ts_bas.model.internal.HoSPersonal;
import se.inera.certificate.modules.ts_bas.model.internal.Patient;
import se.inera.certificate.modules.ts_bas.model.internal.Utlatande;
import se.inera.certificate.modules.ts_bas.model.internal.Vardenhet;
import se.inera.certificate.modules.ts_bas.model.internal.Vardgivare;
import se.inera.certificate.modules.ts_bas.rest.dto.CreateNewDraftCertificateHolder;

/**
 * Factory for creating a editable model.
 */
public class WebcertModelFactory {

    /**
     * Create a new TS-bas draft pre-populated with the attached data
     * 
     * @param newDraftData
     *            {@link CreateNewDraftCertificateHolder}
     * @return {@link Utlatande} or throws a ConverterException if something unforeseen happens
     * @throws ConverterException
     */
    public Utlatande createNewWebcertDraft(CreateNewDraftCertificateHolder newDraftData) throws ConverterException {

        Utlatande utlatande = new Utlatande();

        utlatande.setUtlatandeid(newDraftData.getCertificateId());

        populateWithSkapadAv(utlatande, newDraftData.getSkapadAv());

        populateWithPatientInfo(utlatande, newDraftData.getPatientInfo());

        return utlatande;
    }

    private void populateWithPatientInfo(Utlatande utlatande,
            se.inera.certificate.modules.ts_bas.rest.dto.Patient patient) throws ConverterException {

        if (patient == null) {
            throw new ConverterException("Got null while trying to populateWithPatientInfo");
        }

        utlatande.setPatient(convertPatientToEdit(patient));
    }

    private Patient convertPatientToEdit(se.inera.certificate.modules.ts_bas.rest.dto.Patient patientInfo) {
        Patient patient = new Patient();
        patient.setFornamn(patientInfo.getFornamn());
        patient.setEfternamn(patientInfo.getEfternamn());
        patient.setFullstandigtNamn(StringUtils.join(patientInfo.getFornamn(), " ", patientInfo.getEfternamn()));
        patient.setPersonid(patientInfo.getPersonnummer());

        // TODO: Address information needs to be sorted out at a later time
        // patient.setPostadress(patientInfo.getPostadress());
        // patient.setPostnummer(patientInfo.getPostnummer());
        // patient.setPostort(patientInfo.getPostort());

        return patient;
    }

    private void populateWithSkapadAv(Utlatande utlatande,
            se.inera.certificate.modules.ts_bas.rest.dto.HoSPersonal skapadAv) throws ConverterException {
        if (skapadAv == null) {
            throw new ConverterException("Got null while trying to populateWithSkapadAv");
        }

        utlatande.setSkapadAv(convertHosPersonalToEdit(skapadAv));
    }

    private HoSPersonal convertHosPersonalToEdit(se.inera.certificate.modules.ts_bas.rest.dto.HoSPersonal hosPers) {
        HoSPersonal hosPersonal = new HoSPersonal();

        hosPersonal.setPersonid(hosPers.getHsaId());
        hosPersonal.setFullstandigtNamn(hosPers.getNamn());

        if (hosPers.getBefattning() != null) {
            hosPersonal.getBefattningar().add(hosPers.getBefattning());

        }

        if (hosPers.getVardenhet() != null) {
            Vardenhet vardenhet = convertVardenhetToEdit(hosPers.getVardenhet());
            hosPersonal.setVardenhet(vardenhet);
        }

        return hosPersonal;
    }

    private Vardenhet convertVardenhetToEdit(se.inera.certificate.modules.ts_bas.rest.dto.Vardenhet vardenhetDto) {

        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setEnhetsid(vardenhetDto.getHsaId());
        vardenhet.setEnhetsnamn(vardenhetDto.getNamn());
        vardenhet.setVardgivare(convertVardgivareToEdit(vardenhetDto.getVardgivare()));

        // TODO Populate with the address of the Vardenhet
        vardenhet.setPostadress(vardenhetDto.getPostadress());
        vardenhet.setPostort(vardenhetDto.getPostort());
        vardenhet.setPostnummer(vardenhetDto.getPostnummer());
        vardenhet.setTelefonnummer(vardenhetDto.getTelefonnummer());

        return vardenhet;
    }

    private Vardgivare convertVardgivareToEdit(se.inera.certificate.modules.ts_bas.rest.dto.Vardgivare vardgivareDto) {

        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarid(vardgivareDto.getHsaId());
        vardgivare.setVardgivarnamn(vardgivareDto.getNamn());

        return vardgivare;
    }

}
