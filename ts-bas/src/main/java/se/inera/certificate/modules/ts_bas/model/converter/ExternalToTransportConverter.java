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
import se.inera.certificate.model.PartialInterval;
import se.inera.certificate.model.Patient;
import se.inera.certificate.model.Vardenhet;
import se.inera.certificate.model.Vardgivare;
import se.inera.certificate.modules.ts_bas.model.external.Aktivitet;
import se.inera.certificate.modules.ts_bas.model.external.HosPersonal;
import se.inera.certificate.modules.ts_bas.model.external.Observation;
import se.inera.certificate.modules.ts_bas.model.external.Rekommendation;
import se.inera.certificate.modules.ts_bas.model.external.Vardkontakt;
import se.inera.certificate.ts_bas.model.v1.AktivitetType;
import se.inera.certificate.ts_bas.model.v1.EnhetType;
import se.inera.certificate.ts_bas.model.v1.HosPersonalType;
import se.inera.certificate.ts_bas.model.v1.ObservationType;
import se.inera.certificate.ts_bas.model.v1.PartialDateInterval;
import se.inera.certificate.ts_bas.model.v1.PatientType;
import se.inera.certificate.ts_bas.model.v1.RekommendationType;
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
        utlatande.setVardkontakt(convertVardkontakt(source.getVardkontakter().get(0)));
        utlatande.getAktivitets().addAll(convertAktiviteter(source.getAktiviteter()));
        utlatande.getObservations().addAll(convertObservationer(source.getObservationer()));
        utlatande.getIntygAvsers().addAll(convertCodes(source.getIntygAvser()));
        utlatande.getRekommendations().addAll(convertRekommendationer(source.getRekommendationer()));

        return utlatande;
    }

    /**
     * Convert collection of Rekommendation to collection of RekommendationType
     * 
     * @param source
     * @return
     * @throws ConverterException
     */
    private Collection<? extends RekommendationType> convertRekommendationer(List<Rekommendation> source)
            throws ConverterException {
        if (source == null) {
            throw new ConverterException();
        }
        List<RekommendationType> rekommendationer = new ArrayList<RekommendationType>();
        for (Rekommendation rek : source) {
            rekommendationer.add(convertRekommendation(rek));
        }
        return rekommendationer;
    }

    /**
     * Convert a single Rekommendation to RekommendationType
     * 
     * @param source
     * @return
     */
    private RekommendationType convertRekommendation(Rekommendation source) {
        RekommendationType rekommendation = new RekommendationType();
        if (source.getBeskrivning() != null) {
            rekommendation.setBeskrivning(source.getBeskrivning());
        }

        rekommendation.setRekommendationskod(IsoTypeConverter.toCD(source.getRekommendationskod()));

        if (source.getVarde() != null) {
            rekommendation.getVardes().add(IsoTypeConverter.toCD(source.getVarde()));
        }
        return rekommendation;
    }

    /**
     * Convert collection of Kod to collection of CD
     * 
     * @param source
     * @return
     * @throws ConverterException
     */
    private Collection<? extends CD> convertCodes(List<Kod> source) throws ConverterException {
        if (source == null) {
            throw new ConverterException();
        }
        List<CD> codes = new ArrayList<CD>();
        for (Kod kod : source) {
            codes.add(IsoTypeConverter.toCD(kod));
        }
        return codes;
    }

    /**
     * Convert a Collection of Observation to Collection of ObservationType
     * 
     * @param source
     * @return
     * @throws ConverterException
     */
    private Collection<? extends ObservationType> convertObservationer(List<Observation> source)
            throws ConverterException {
        if (source == null) {
            throw new ConverterException("Observationer source was null, conversion failing");
        }
        List<ObservationType> observationer = new ArrayList<ObservationType>();
        for (Observation obs : source) {
            observationer.add(convertObservation(obs));
        }

        return observationer;
    }

    /**
     * Convert a single Observation to ObservationType
     * 
     * @param source
     * @return
     */
    private ObservationType convertObservation(Observation source) {
        ObservationType observation = new ObservationType();
        if (source.getBeskrivning() != null) {
            observation.setBeskrivning(source.getBeskrivning());
        }
        if (source.getForekonst() != null) {
            observation.setForekomst(source.getForekonst());
        }
        if (source.getLateralitet() != null) {
            observation.setLateralitet(IsoTypeConverter.toCD(source.getLateralitet()));
        }
        if (source.getId() != null) {
            observation.setObservationsid(IsoTypeConverter.toII(source.getId()));
        }
        observation.setObservationskod(IsoTypeConverter.toCD(source.getObservationskod()));

        if (!source.getVarde().isEmpty()) {
            observation.setVarde(IsoTypeConverter.toPQ(source.getVarde().get(0)));
        }

        return observation;
    }

    /**
     * Convert Collection of Aktivitet to Collection of AktivitetType
     * 
     * @param source
     * @return
     * @throws ConverterException
     */
    private Collection<? extends AktivitetType> convertAktiviteter(List<Aktivitet> source) throws ConverterException {
        if (source == null) {
            throw new ConverterException("Aktivitet source was null, conversion failing");
        }
        List<AktivitetType> aktiviteter = new ArrayList<AktivitetType>();
        for (Aktivitet aktivitet : source) {
            aktiviteter.add(convertAktivitet(aktivitet));
        }
        return aktiviteter;
    }

    /**
     * Convert a single Aktivitet to AktivitetType
     * 
     * @param source
     *            Aktivitet
     * @return AktivitetType
     */
    private AktivitetType convertAktivitet(Aktivitet source) {
        AktivitetType aktivitet = new AktivitetType();

        if (source.getId() != null) {
            aktivitet.setAktivitetsid(IsoTypeConverter.toII(source.getId()));
        }

        aktivitet.setAktivitetskod(IsoTypeConverter.toCD(source.getAktivitetskod()));
        
        if (source.getAktivitetsstatus() != null) {
            aktivitet.setAktivitetsstatus(IsoTypeConverter.toCD(source.getAktivitetsstatus()));
        }
        
        if (source.getAktivitetstid() != null) {
            aktivitet.setAktivitetstid(convertPartialDateInterval(source.getAktivitetstid()));
        }
        if (source.getBeskrivning() != null) {
            aktivitet.setBeskrivning(source.getBeskrivning());
        }
        if (source.getMetod() != null) {
            aktivitet.setMetod(IsoTypeConverter.toCD(source.getMetod()));
        }
        if (source.getPlats() != null) {
            aktivitet.setPlats(source.getPlats());
        }

        return aktivitet;
    }

    /**
     * Convert PartialInterval to PartialDateInterval
     * 
     * @param source
     * @return
     */
    private PartialDateInterval convertPartialDateInterval(PartialInterval source) {
        if (source == null) {
            LOG.trace("Source PartialDateInterval was null, could not convert");
            return null;
        }
        PartialDateInterval pdi = new PartialDateInterval();
        pdi.setFrom(source.getFrom());
        pdi.setTom(source.getTom());
        return pdi;
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
