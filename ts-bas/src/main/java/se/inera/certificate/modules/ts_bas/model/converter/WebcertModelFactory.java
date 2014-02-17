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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import se.inera.certificate.model.Kod;
import se.inera.certificate.modules.ts_bas.model.external.HosPersonal;
import se.inera.certificate.modules.ts_bas.model.codes.BefattningKod;
import se.inera.certificate.modules.ts_bas.model.codes.CodeConverter;
import se.inera.certificate.modules.ts_bas.model.codes.CodeSystem;
import se.inera.certificate.modules.ts_bas.model.internal.HoSPersonal;
import se.inera.certificate.modules.ts_bas.model.internal.Patient;
import se.inera.certificate.modules.ts_bas.model.internal.Utlatande;
import se.inera.certificate.modules.ts_bas.model.internal.Vardenhet;
import se.inera.certificate.modules.ts_bas.model.internal.Vardgivare;
import se.inera.certificate.modules.ts_bas.rest.dto.CreateNewDraftCertificateHolder;
import se.inera.certificate.modules.ts_bas.rest.dto.HoSPerson;

/**
 * Factory for creating a editable model.
 */
public class WebcertModelFactory {

    /**
     * Create a new TS-bas draft pre-populated with the attached data
     * 
     * @param newDraftData
     *            {@link CreateNewDraftCertificateHolder}
     * @return {@link Utlatande} or throws a ConverterException if something
     *         unforeseen happens
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
        patient.setFornamn(patientInfo.getForNamn());
        patient.setEfternamn(patientInfo.getEfterNamn());
        patient.setFullstandigtNamn(StringUtils.join(patientInfo.getForNamn(), " ", patientInfo.getEfterNamn()));
        patient.setPersonid(patientInfo.getPersonNummer());

        // TODO: Address information needs to be sorted out at a later time
        // patient.setPostadress(patientInfo.getPostadress());
        // patient.setPostnummer(patientInfo.getPostnummer());
        // patient.setPostort(patientInfo.getPostort());

        return patient;
    }

    private void populateWithSkapadAv(Utlatande utlatande, HoSPerson skapadAv) throws ConverterException {
        if (skapadAv == null) {
            throw new ConverterException("Got null while trying to populateWithSkapadAv");
        }

        utlatande.setSkapadAv(convertHosPersonalToEdit(skapadAv));
    }

    private HoSPersonal convertHosPersonalToEdit(HoSPerson hosPers) {
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

        return vardenhet;
    }

    private Vardgivare convertVardgivareToEdit(se.inera.certificate.modules.ts_bas.rest.dto.Vardgivare vardgivareDto) {

        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarid(vardgivareDto.getHsaId());
        vardgivare.setVardgivarnamn(vardgivareDto.getNamn());

        return vardgivare;
    }

    private Collection<String> convertKodToString(List<Kod> specialiteter, Class<? extends CodeSystem> type) {
        List<String> intSpecialiteter = new ArrayList<>();
        for (Kod kod : specialiteter) {
            intSpecialiteter.add(CodeConverter.getInternalNameFromKod(kod, type));
        }
        return intSpecialiteter;
    }

    private Vardenhet convertVardenhetToEdit(se.inera.certificate.model.Vardenhet enhet) {
        if (enhet == null) {
            return null;
        }
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setEnhetsid(enhet.getId().getExtension());
        vardenhet.setEnhetsnamn(enhet.getNamn());
        vardenhet.setEpost(enhet.getEpost());
        vardenhet.setPostadress(enhet.getPostadress());
        vardenhet.setPostort(enhet.getPostort());
        vardenhet.setPostnummer(enhet.getPostnummer());
        vardenhet.setTelefonnummer(enhet.getTelefonnummer());
        vardenhet.setVardgivare(convertVardgivareType(enhet.getVardgivare()));

        return vardenhet;
    }

    private Vardgivare convertVardgivareType(se.inera.certificate.model.Vardgivare source) {
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarid(source.getId().getExtension());
        vardgivare.setVardgivarnamn(source.getNamn());
        return vardgivare;
    }
}
