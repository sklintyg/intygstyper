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
package se.inera.certificate.modules.rli.model.converter;

import org.joda.time.LocalDate;
import se.inera.certificate.modules.rli.model.internal.wc.Undersokning;
import se.inera.certificate.modules.rli.model.internal.wc.Utlatande;
import se.inera.certificate.modules.rli.model.internal.wc.Vardenhet;
import se.inera.certificate.modules.rli.model.internal.wc.Vardgivare;
import se.inera.certificate.modules.support.api.dto.CreateNewDraftHolder;

/**
 * Factory for creating a editable model.
 *
 * @author nikpet
 *
 */
public class WebcertModelFactory {

    public Utlatande createNewWebcertDraft(CreateNewDraftHolder newDraftData) throws ConverterException {

        Utlatande utlatande = new Utlatande();

        utlatande.setUtlatandeid(newDraftData.getCertificateId());

        populateWithSkapadAv(utlatande, newDraftData.getSkapadAv());

        populateWithPatientInfo(utlatande, newDraftData.getPatient());

        populateWithAktivitetKliniskUndersokning(utlatande, newDraftData.getSkapadAv().getVardenhet());

        return utlatande;
    }

    private void populateWithPatientInfo(Utlatande utlatande,
            se.inera.certificate.modules.support.api.dto.Patient patient) throws ConverterException {

        if (patient == null) {
            throw new ConverterException("Got null while trying to populateWithPatientInfo");
        }

        utlatande.setPatient(convertPatientToEdit(patient));
    }

    private se.inera.certificate.modules.rli.model.internal.wc.Patient convertPatientToEdit(
            se.inera.certificate.modules.support.api.dto.Patient patientInfo) {
        se.inera.certificate.modules.rli.model.internal.wc.Patient patient = new se.inera.certificate.modules.rli.model.internal.wc.Patient();
        patient.setFornamn(patientInfo.getFornamn());
        patient.setMellannamn(patientInfo.getMellannamn());
        patient.setEfternamn(patientInfo.getEfternamn());
        patient.setFullstandigtNamn(patientInfo.getFullstandigtNamn());
        patient.setPersonid(patientInfo.getPersonnummer());
        patient.setPostadress(patientInfo.getPostadress());
        patient.setPostnummer(patientInfo.getPostnummer());
        patient.setPostort(patientInfo.getPostort());

        return patient;
    }

    private void populateWithAktivitetKliniskUndersokning(Utlatande utlatande,
            se.inera.certificate.modules.support.api.dto.Vardenhet vardenhet) throws ConverterException {

        if (vardenhet == null) {
            throw new ConverterException("Got null while trying to convert Vardenhet");
        }
        utlatande.setUndersokning(createKliniskUndersokning(convertVardenhetToEdit(vardenhet)));
    }

    private Undersokning createKliniskUndersokning(Vardenhet vardenhet) {
        Undersokning undersokning = new Undersokning();
        undersokning.setUtforsVid(vardenhet);

        /** Create a new LocalDate for when the actual examination is taking place */
        undersokning.setUndersokningsdatum(LocalDate.now().toString());

        return undersokning;
    }

    private void populateWithSkapadAv(Utlatande utlatande,
            se.inera.certificate.modules.support.api.dto.HoSPersonal skapadAv) throws ConverterException {
        if (skapadAv == null) {
            throw new ConverterException("Got null while trying to populateWithSkapadAv");
        }

        utlatande.setSkapadAv(convertHosPersonalToEdit(skapadAv));
    }

    private se.inera.certificate.modules.rli.model.internal.wc.HoSPersonal convertHosPersonalToEdit(
            se.inera.certificate.modules.support.api.dto.HoSPersonal hosPersType) {

        se.inera.certificate.modules.rli.model.internal.wc.HoSPersonal hosPersonal = new se.inera.certificate.modules.rli.model.internal.wc.HoSPersonal();

        hosPersonal.setPersonid(hosPersType.getHsaId());
        hosPersonal.setFullstandigtNamn(hosPersType.getNamn());

        if (hosPersType.getBefattning() != null) {
            hosPersonal.setBefattning(hosPersType.getBefattning());
        }

        hosPersonal.setVardenhet(convertVardenhetToEdit(hosPersType.getVardenhet()));

        return hosPersonal;
    }

    private Vardenhet convertVardenhetToEdit(se.inera.certificate.modules.support.api.dto.Vardenhet enhet) {
        if (enhet == null) {
            return null;
        }
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setEnhetsid(enhet.getHsaId());
        vardenhet.setEnhetsnamn(enhet.getNamn());
        vardenhet.setEpost(enhet.getEpost());
        vardenhet.setPostadress(enhet.getPostadress());
        vardenhet.setPostort(enhet.getPostort());
        vardenhet.setPostnummer(enhet.getPostnummer());
        vardenhet.setTelefonnummer(enhet.getTelefonnummer());
        vardenhet.setVardgivare(convertVardgivareType(enhet.getVardgivare()));

        return vardenhet;
    }

    private Vardgivare convertVardgivareType(se.inera.certificate.modules.support.api.dto.Vardgivare source) {
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarid(source.getHsaId());
        vardgivare.setVardgivarnamn(source.getNamn());
        return vardgivare;
    }
}
