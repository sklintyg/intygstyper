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

import iso.v21090.dt.v1.CD;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.certificate.model.Kod;
import se.inera.certificate.model.Patient;
import se.inera.certificate.model.Vardenhet;
import se.inera.certificate.model.Vardgivare;
import se.inera.certificate.modules.ts_bas.model.external.HosPersonal;
import se.inera.certificate.modules.ts_bas.model.external.Vardkontakt;
import se.inera.certificate.ts_bas.model.v1.EnhetType;
import se.inera.certificate.ts_bas.model.v1.HosPersonalType;
import se.inera.certificate.ts_bas.model.v1.PatientType;
import se.inera.certificate.ts_bas.model.v1.Utlatande;
import se.inera.certificate.ts_bas.model.v1.VardgivareType;
import se.inera.certificate.ts_bas.model.v1.VardkontaktType;

public class ExternalToTransportConverter {

    private static final Logger LOG = LoggerFactory.getLogger(ExternalToTransportConverter.class);

    public Utlatande convert(se.inera.certificate.modules.ts_bas.model.external.Utlatande source)
            throws ConverterException {
        LOG.trace("Converting external model to transport");

        Utlatande utlatande = new Utlatande();
        utlatande.setPatient(convertPatient(source.getPatient()));
        utlatande.setSigneringsdatum(source.getSigneringsdatum());
        utlatande.setSkapadAv(convertHosPersonal(source.getSkapadAv()));
        utlatande.setSkickatdatum(source.getSkickatdatum());
        utlatande.setTypAvUtlatande(IsoTypeConverter.toUtlatandeTyp(source.getTyp()));
        utlatande.setUtlatandeId(IsoTypeConverter.toUtlatandeId(source.getId()));
        utlatande.setVardkontakt(convertVardkontakt(source.getVardkontakt()));

        return utlatande;
    }

    private VardkontaktType convertVardkontakt(Vardkontakt source) {
        VardkontaktType vardkontakt = new VardkontaktType();
        vardkontakt.setIdKontroll(IsoTypeConverter.toCD(source.getIdkontroll()));
        vardkontakt.setVardkontakttyp(IsoTypeConverter.toCD(source.getVardkontakttyp()));
        
        return vardkontakt;
    }

    private PatientType convertPatient(Patient source) {
        PatientType patient = new PatientType();
        patient.getFornamns().addAll(source.getFornamn());
        patient.setEfternamn(source.getEfternamn());
        patient.setPersonId(IsoTypeConverter.toPersonId(source.getId()));
        patient.setPostadress(source.getPostadress());
        patient.setPostnummer(source.getPostnummer());
        patient.setPostort(source.getPostort());

        return patient;
    }

    private HosPersonalType convertHosPersonal(HosPersonal source) {
        HosPersonalType hosPersonal = new HosPersonalType();
        hosPersonal.setBefattning(source.getBefattning());
        hosPersonal.setEnhet(convertVardenhet(source.getVardenhet()));
        hosPersonal.setFullstandigtNamn(source.getNamn());
        hosPersonal.setPersonalId(IsoTypeConverter.toHsaId(source.getId()));
        hosPersonal.getSpecialitets().addAll(convertKodToCD(source.getSpecialiteter()));

        return hosPersonal;
    }

    private Collection<? extends CD> convertKodToCD(List<Kod> source) {
        List<CD> koder = new ArrayList<CD>();
        for (Kod kod : source) {
            koder.add(IsoTypeConverter.toCD(kod));
        }
        return koder;
    }

    private EnhetType convertVardenhet(Vardenhet source) {
        EnhetType enhet = new EnhetType();
        enhet.setEnhetsId(IsoTypeConverter.toHsaId(source.getId()));
        enhet.setEnhetsnamn(source.getNamn());
        enhet.setPostadress(source.getPostadress());
        enhet.setPostnummer(source.getPostnummer());
        enhet.setPostort(source.getPostort());
        enhet.setTelefonnummer(source.getTelefonnummer());
        enhet.setVardgivare(convertVardgivare(source.getVardgivare()));

        return enhet;
    }

    private VardgivareType convertVardgivare(Vardgivare source) {
        VardgivareType vardgivare = new VardgivareType();
        vardgivare.setVardgivareId(IsoTypeConverter.toHsaId(source.getId()));
        vardgivare.setVardgivarnamn(source.getNamn());

        return vardgivare;
    }
}
