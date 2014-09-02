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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.inera.certificate.model.Kod;
import se.inera.certificate.model.Patient;
import se.inera.certificate.model.Vardenhet;
import se.inera.certificate.model.Vardgivare;
import se.inera.certificate.model.util.Strings;
import se.inera.certificate.modules.ts_bas.model.codes.UtlatandeKod;
import se.inera.certificate.modules.ts_bas.model.external.Aktivitet;
import se.inera.certificate.modules.ts_bas.model.external.HosPersonal;
import se.inera.certificate.modules.ts_bas.model.external.Observation;
import se.inera.certificate.modules.ts_bas.model.external.ObservationAktivitetRelation;
import se.inera.certificate.modules.ts_bas.model.external.Rekommendation;
import se.inera.certificate.modules.ts_bas.model.external.Vardkontakt;
import se.inera.certificate.ts_bas.iso.v21090.dt.v1.CD;
import se.inera.certificate.ts_bas.model.ext.v1.IntygAvser;
import se.inera.certificate.ts_bas.model.ext.v1.Specialitet;
import se.inera.certificate.ts_bas.model.ext.v1.Varde;
import se.inera.certificate.ts_bas.model.v1.AktivitetType;
import se.inera.certificate.ts_bas.model.v1.EnhetType;
import se.inera.certificate.ts_bas.model.v1.HosPersonalType;
import se.inera.certificate.ts_bas.model.v1.ObservationType;
import se.inera.certificate.ts_bas.model.v1.PatientType;
import se.inera.certificate.ts_bas.model.v1.RekommendationType;
import se.inera.certificate.ts_bas.model.v1.Utlatande;
import se.inera.certificate.ts_bas.model.v1.VardgivareType;
import se.inera.certificate.ts_bas.model.v1.VardkontaktType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ExternalToTransportConverter {

    private static final Logger LOG = LoggerFactory.getLogger(ExternalToTransportConverter.class);

    /**
     * Converts from the external format ({@link se.inera.certificate.modules.ts_bas.model.external.Utlatande}) to the
     * transport format ({@link se.inera.certificate.ts_bas.model.v1.Utlatande}).
     *
     * @param source
     *            {@link se.inera.certificate.modules.ts_bas.model.external.Utlatande} in the external format to be
     *            converted to transport format
     * @return {@link se.inera.certificate.modules.ts_bas.model.external.Utlatande}
     * @throws ConverterException
     */
    public Utlatande convert(se.inera.certificate.modules.ts_bas.model.external.Utlatande source)
            throws ConverterException {
        LOG.trace("Converting external model to transport");

        Utlatande utlatande = new Utlatande();
        utlatande.setPatient(convertPatient(source.getPatient()));
        utlatande.setSigneringsdatum(source.getSigneringsdatum());
        if (!source.getKommentarer().isEmpty()) {
            utlatande.getKommentars().addAll(source.getKommentarer());
        }
        utlatande.setSkapadAv(convertHosPersonal(source.getSkapadAv()));
        utlatande.setSkickatdatum(source.getSkickatdatum());

        utlatande.setTypAvUtlatande(IsoTypeConverter.toUtlatandeTyp(source.getTyp()));
        UtlatandeKod utlatandeKod = UtlatandeKod.getVersionFromTSParams(source.getTsUtgava(), source.getTsVersion());
        utlatande.setUtgava(utlatandeKod.getTsUtgava());
        utlatande.setVersion(utlatandeKod.getTsVersion());

        utlatande.setUtlatandeId(IsoTypeConverter.toUtlatandeId(source.getId()));

        // Just make sure getVardkontakter() doesn't return an empty list before getting an index..
        if (!source.getVardkontakter().isEmpty()) {
            utlatande.setVardkontakt(convertVardkontakt(source.getVardkontakter().get(0)));
        }

        utlatande.getAktivitets().addAll(convertAktiviteter(source.getAktiviteter()));
        utlatande.getObservations().addAll(convertObservationer(source.getObservationer()));
        utlatande.getIntygAvsers().addAll(convertCodes(source.getIntygAvser(), IntygAvser.class));
        utlatande.getRekommendations().addAll(convertRekommendationer(source.getRekommendationer()));
        utlatande.getObservationAktivitetRelations().addAll(
                convertObservationAktivitetRelationer(source.getObservationAktivitetRelationer()));

        return utlatande;
    }

    /**
     * Convert a Collection of ObservationAktivitetRelation to ObservationAktivitetRelationType.
     *
     * @param source
     *            List of {@link ObservationAktivitetRelation}
     * @return List of {@link ObservationAktivitetRelationType}
     * @throws ConverterException
     */
    private Collection<? extends se.inera.certificate.ts_bas.model.ext.v1.ObservationAktivitetRelation> convertObservationAktivitetRelationer(
            List<ObservationAktivitetRelation> source) throws ConverterException {
        if (source == null) {
            throw new ConverterException();
        }
        List<se.inera.certificate.ts_bas.model.ext.v1.ObservationAktivitetRelation> converted = new ArrayList<>();

        for (ObservationAktivitetRelation it : source) {
            converted.add(convertObservationRelation(it));
        }

        return converted;
    }

    /**
     * Converts a single ObservationAktivitetRelation to ObservationAktivitetRelationType.
     *
     * @param source
     *            {@link ObservationAktivitetRelation}
     * @return {@link ObservationAktivitetRelationType}
     */
    private se.inera.certificate.ts_bas.model.ext.v1.ObservationAktivitetRelation convertObservationRelation(ObservationAktivitetRelation source) {
        se.inera.certificate.ts_bas.model.ext.v1.ObservationAktivitetRelation converted = new se.inera.certificate.ts_bas.model.ext.v1.ObservationAktivitetRelation();
        converted.setAktivitetsid(IsoTypeConverter.toRelationId(source.getAktivitetsid()));
        converted.setObservationsid(IsoTypeConverter.toRelationId(source.getObservationsid()));

        return converted;
    }

    /**
     * Convert collection of Rekommendation to collection of RekommendationType.
     *
     * @param source
     *            {@link Rekommendation}
     * @return {@link RekommendationType}
     * @throws ConverterException
     */
    private Collection<? extends RekommendationType> convertRekommendationer(List<Rekommendation> source)
            throws ConverterException {
        if (source == null) {
            throw new ConverterException();
        }
        List<RekommendationType> rekommendationer = new ArrayList<>();
        for (Rekommendation rek : source) {
            rekommendationer.add(convertRekommendation(rek));
        }
        return rekommendationer;
    }

    /**
     * Convert a single Rekommendation to RekommendationType.
     *
     * @param source
     *            {@link Rekommendation}
     * @return {@link RekommendationType}
     * @throws ConverterException
     */
    private RekommendationType convertRekommendation(Rekommendation source) throws ConverterException {
        RekommendationType rekommendation = new RekommendationType();
        if (source.getBeskrivning() != null) {
            rekommendation.setBeskrivning(source.getBeskrivning());
        }

        rekommendation.setRekommendationskod(IsoTypeConverter.toRekommendationKod(source.getRekommendationskod()));

        if (!source.getVarde().isEmpty()) {
            rekommendation.getVardes().addAll(convertCodes(source.getVarde(), Varde.class));
        }
        return rekommendation;
    }

    /**
     * Convert collection of Kod to collection of CD.
     *
     * @param source
     *            List of {@link Kod}
     * @return List of {@link CD}
     * @throws ConverterException
     */
    private <T extends CD> Collection<T> convertCodes(List<Kod> source, Class<T> type) throws ConverterException {
        try {
            if (source == null) {
                throw new ConverterException();
            }
            List<T> codes = new ArrayList<>();
            for (Kod kod : source) {
                codes.add(IsoTypeConverter.toCD(kod, type.newInstance()));
            }
            return codes;

        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Convert a Collection of Observation to Collection of ObservationType.
     *
     * @param source
     *            List of {@link Observation}
     * @return List of {@link ObservationType}
     * @throws ConverterException
     */
    private Collection<? extends ObservationType> convertObservationer(List<Observation> source)
            throws ConverterException {
        if (source == null) {
            throw new ConverterException("Observationer source was null, conversion failing");
        }
        List<ObservationType> observationer = new ArrayList<>();
        for (Observation obs : source) {
            observationer.add(convertObservation(obs));
        }

        return observationer;
    }

    /**
     * Convert a single Observation to ObservationType.
     *
     * @param source
     *            {@link Observation}
     * @return {@link ObservationType}
     */
    private ObservationType convertObservation(Observation source) {
        ObservationType observation = new ObservationType();
        if (source.getBeskrivning() != null) {
            observation.setBeskrivning(source.getBeskrivning());
        }
        if (source.getForekomst() != null) {
            observation.setForekomst(source.getForekomst());
        }
        if (source.getLateralitet() != null) {
            observation.setLateralitet(IsoTypeConverter.toLateralitetKod(source.getLateralitet()));
        }
        if (source.getId() != null) {
            observation.setObservationsId(IsoTypeConverter.toRelationId(source.getId()));
        }
        observation.setObservationskod(IsoTypeConverter.toObservationKod(source.getObservationskod()));

        if (!source.getVarde().isEmpty()) {
            observation.setVarde(IsoTypeConverter.toPQ(source.getVarde().get(0)));
        }

        return observation;
    }

    /**
     * Convert Collection of Aktivitet to Collection of AktivitetType.
     *
     * @param source
     *            List of {@link Aktivitet}
     * @return List of {@link AktivitetType}
     * @throws ConverterException
     */
    private Collection<? extends AktivitetType> convertAktiviteter(List<Aktivitet> source) throws ConverterException {
        if (source == null) {
            throw new ConverterException("Aktivitet source was null, conversion failing");
        }
        List<AktivitetType> aktiviteter = new ArrayList<>();
        for (Aktivitet aktivitet : source) {
            aktiviteter.add(convertAktivitet(aktivitet));
        }
        return aktiviteter;
    }

    /**
     * Convert a single Aktivitet to AktivitetType.
     *
     * @param source
     *            {@link Aktivitet}
     * @return {@link AktivitetType}
     */
    private AktivitetType convertAktivitet(Aktivitet source) {
        AktivitetType aktivitet = new AktivitetType();

        if (source.getId() != null) {
            aktivitet.setAktivitetsId(IsoTypeConverter.toRelationId(source.getId()));
        }

        aktivitet.setAktivitetskod(IsoTypeConverter.toAktivitetKod(source.getAktivitetskod()));

        if (source.getOstruktureradTid() != null) {
            aktivitet.setOstruktureradtid(source.getOstruktureradTid());
        }
        if (source.getBeskrivning() != null) {
            aktivitet.setBeskrivning(source.getBeskrivning());
        }
        if (source.getMetod() != null) {
            aktivitet.setMetod(IsoTypeConverter.toMetodKod(source.getMetod()));
        }
        if (source.getPlats() != null) {
            aktivitet.setPlats(source.getPlats());
        }
        if (source.getForekomst() != null) {
            aktivitet.setForekomst(source.getForekomst());
        }

        return aktivitet;
    }

    /**
     * Convert a Vardkontakt to VardkontaktType.
     *
     * @param source
     *            {@link Vardkontakt}
     * @return {@link VardkontaktType}
     */
    private VardkontaktType convertVardkontakt(Vardkontakt source) {
        VardkontaktType vardkontakt = new VardkontaktType();
        vardkontakt.setIdKontroll(IsoTypeConverter.toIdkontrollKod(source.getIdkontroll()));
        vardkontakt.setVardkontakttyp(IsoTypeConverter.toVardkontaktKod(source.getVardkontakttyp()));

        return vardkontakt;
    }

    /**
     * Convert at Patient to PatientType.
     *
     * @param source
     *            {@link Patient}
     * @return {@link PatientType}
     */
    private PatientType convertPatient(Patient source) {
        if (source == null) {
            LOG.trace("Patient was null, could not convert");
            return null;
        }

        PatientType patient = new PatientType();
        patient.getFornamns().addAll(source.getFornamn());
        if (source.getMellannamn().isEmpty()) {
            patient.setEfternamn(source.getEfternamn());
        } else {
            patient.setEfternamn(Strings.join(" ", source.getMellannamn()) + " " + source.getEfternamn());
        }
        patient.setPersonId(IsoTypeConverter.toPersonId(source.getId()));
        patient.setPostadress(source.getPostadress());
        patient.setPostnummer(source.getPostnummer());
        patient.setPostort(source.getPostort());

        return patient;
    }

    /**
     * Convert a HosPersonal to HosPersonalType.
     *
     * @param source
     *            {@link HosPersonal}
     * @return {@link HosPersonalType}
     * @throws ConverterException
     */
    private HosPersonalType convertHosPersonal(HosPersonal source) throws ConverterException {
        HosPersonalType hosPersonal = new HosPersonalType();
        hosPersonal.getBefattnings().addAll(source.getBefattningar());
        hosPersonal.setEnhet(convertVardenhet(source.getVardenhet()));
        hosPersonal.setFullstandigtNamn(source.getNamn());
        hosPersonal.setPersonalId(IsoTypeConverter.toHsaId(source.getId()));
        hosPersonal.getSpecialitets().addAll(convertCodes(source.getSpecialiteter(), Specialitet.class));

        return hosPersonal;
    }

    /**
     * Convert a Vardenhet to EnhetType.
     *
     * @param source
     *            {@link Vardenhet}
     * @return {@link EnhetType}
     */
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

    /**
     * Convert a Vardgivare to VardgivareType.
     *
     * @param source
     *            {@link Vardgivare}
     * @return {@link VardgivareType}
     */
    private VardgivareType convertVardgivare(Vardgivare source) {
        VardgivareType vardgivare = new VardgivareType();
        vardgivare.setVardgivareId(IsoTypeConverter.toHsaId(source.getId()));
        vardgivare.setVardgivarnamn(source.getNamn());

        return vardgivare;
    }
}
