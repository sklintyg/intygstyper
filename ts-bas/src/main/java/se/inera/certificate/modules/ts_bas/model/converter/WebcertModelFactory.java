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

    private void populateWithPatientInfo(Utlatande utlatande, se.inera.certificate.model.Patient patient)
            throws ConverterException {

        if (patient == null) {
            throw new ConverterException("Got null while trying to populateWithPatientInfo");
        }

        utlatande.setPatient(convertPatientToEdit(patient));
    }

    private Patient convertPatientToEdit(se.inera.certificate.model.Patient patientInfo) {
        Patient patient = new Patient();
        patient.setFornamn(StringUtils.join(patientInfo.getFornamn(), " "));
        patient.setEfternamn(patientInfo.getEfternamn());
        patient.setFullstandigtNamn(patientInfo.getFullstandigtNamn());
        patient.setPersonid(patientInfo.getId().getExtension());
        
        // TODO: Address information needs to be sorted out at a later time
        //patient.setPostadress(patientInfo.getPostadress());
        //patient.setPostnummer(patientInfo.getPostnummer());
        //patient.setPostort(patientInfo.getPostort());

        return patient;
    }

    private void populateWithSkapadAv(Utlatande utlatande, HosPersonal skapadAv) throws ConverterException {
        if (skapadAv == null) {
            throw new ConverterException("Got null while trying to populateWithSkapadAv");
        }

        utlatande.setSkapadAv(convertHosPersonalToEdit(skapadAv));
    }

    private HoSPersonal convertHosPersonalToEdit(HosPersonal hosPersType) {
        HoSPersonal hosPersonal = new HoSPersonal();

        hosPersonal.setPersonid(hosPersType.getId().getExtension());
        hosPersonal.setFullstandigtNamn(hosPersType.getNamn());

        if (hosPersType.getBefattning() != null) {
            hosPersonal.getBefattningar().addAll(convertKodToString(hosPersType.getBefattningar(), BefattningKod.class));
            
        }

        hosPersonal.setVardenhet(convertVardenhetToEdit(hosPersType.getVardenhet()));

        return hosPersonal;
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
