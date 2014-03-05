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
package se.inera.certificate.modules.ts_diabetes.model.converter;

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
import se.inera.certificate.modules.ts_diabetes.model.codes.CodeConverter;
import se.inera.certificate.modules.ts_diabetes.model.codes.UtlatandeKod;
import se.inera.certificate.modules.ts_diabetes.model.converter.ConverterException;
import se.inera.certificate.modules.ts_diabetes.model.converter.IsoTypeConverter;
import se.inera.certificate.modules.ts_diabetes.model.external.Aktivitet;
import se.inera.certificate.modules.ts_diabetes.model.external.Bilaga;
import se.inera.certificate.modules.ts_diabetes.model.external.HosPersonal;
import se.inera.certificate.modules.ts_diabetes.model.external.Observation;
import se.inera.certificate.modules.ts_diabetes.model.external.ObservationAktivitetRelation;
import se.inera.certificate.modules.ts_diabetes.model.external.Rekommendation;
import se.inera.certificate.modules.ts_diabetes.model.external.Utlatande;
import se.inera.certificate.modules.ts_diabetes.model.external.Vardkontakt;
import se.inera.certificate.ts_diabetes.model.v1.AktivitetType;
import se.inera.certificate.ts_diabetes.model.v1.BilagaType;
import se.inera.certificate.ts_diabetes.model.v1.EnhetType;
import se.inera.certificate.ts_diabetes.model.v1.HosPersonalType;
import se.inera.certificate.ts_diabetes.model.v1.ObservationAktivitetRelationType;
import se.inera.certificate.ts_diabetes.model.v1.ObservationType;
import se.inera.certificate.ts_diabetes.model.v1.PatientType;
import se.inera.certificate.ts_diabetes.model.v1.RekommendationType;
import se.inera.certificate.ts_diabetes.model.v1.VardgivareType;
import se.inera.certificate.ts_diabetes.model.v1.VardkontaktType;

/**
 * Converter between transport and external model.
 */
public class TransportToExternalConverter {

    private static final Logger LOG = LoggerFactory.getLogger(TransportToExternalConverter.class);

    /**
     * Converts from the transport format (se.inera.certificate.common.v1.Utlatande) to the external format
     * (se.inera.certificate.modules.ts_diabetes.model.external.Utlatande).
     * 
     * @param source
     *            {@link Utlatande} in the transport format to be converted to external format
     * @return {@link se.inera.certificate.modules.ts_diabetes.model.external.Utlatande}
     * @throws ConverterException
     */
    public Utlatande convert(se.inera.certificate.ts_diabetes.model.v1.Utlatande source) throws ConverterException {
        LOG.trace("Converting transport model to external");

        if (source == null) {
            throw new ConverterException("Source Utlatande was null, cannot convert");
        }
        Utlatande utlatande = new Utlatande();
        utlatande.setId(IsoTypeConverter.toId(source.getUtlatandeId()));

        // Validate and set Typ
        Kod typAvUtlatande = IsoTypeConverter.toKod(source.getTypAvUtlatande());
        UtlatandeKod utlatandeKod = CodeConverter.fromCode(typAvUtlatande, UtlatandeKod.class);
        try {
            utlatandeKod.assertVersion(source.getUtgava(), source.getVersion());
        } catch (IllegalArgumentException e) {
            throw new ConverterException(e.getMessage());
        }

        utlatande.setTyp(typAvUtlatande);
        utlatande.getKommentarer().addAll(source.getKommentars());
        utlatande.setPatient(convertPatient(source.getPatient()));
        utlatande.setSigneringsdatum(source.getSigneringsdatum());
        utlatande.setSkapadAv(convertHosPersonal(source.getSkapadAv()));
        utlatande.setSkickatdatum(source.getSkickatdatum());
        utlatande.getIntygAvser().addAll(convertCDtoKod(source.getIntygAvsers()));
        utlatande.getObservationer().addAll(convertObservationer(source.getObservations()));
        utlatande.getRekommendationer().addAll(convertRekommendationer(source.getRekommendations()));

        if (source.getAktivitets() != null) {
            utlatande.getAktiviteter().addAll(convertAktiviteter(source.getAktivitets()));
        }

        utlatande.getVardkontakter().add(convertVardkontakt(source.getVardkontakt()));
        utlatande.getObservationAktivitetRelationer().addAll(
                convertObservationAktivitetRelationer(source.getObservationAktivitetRelations()));

        utlatande.setBilaga(convertToExtBilaga(source.getBilaga()));

        return utlatande;
    }

    private Bilaga convertToExtBilaga(BilagaType source) throws ConverterException {
        if (source == null) {
            throw new ConverterException("Missing bilaga");
        }
        Bilaga extBilaga = new Bilaga();
        extBilaga.setBilagetyp(IsoTypeConverter.toKod(source.getBilagetyp()));
        extBilaga.setForekomst(source.isForekomst());

        return extBilaga;
    }

    /**
     * Convert a collection of ObservationAktivitetRelationTypes to ObservationAktivitetRelations
     * 
     * @param source
     *            List of {@link ObservationAktivitetRelationType}
     * @return List of {@link ObservationAktivitetRelation}
     * @throws ConverterException
     */
    private Collection<? extends ObservationAktivitetRelation> convertObservationAktivitetRelationer(
            List<ObservationAktivitetRelationType> source) throws ConverterException {
        if (source == null) {
            throw new ConverterException();
        }
        List<ObservationAktivitetRelation> converted = new ArrayList<ObservationAktivitetRelation>();

        for (ObservationAktivitetRelationType it : source) {
            converted.add(convertObservationRelation(it));
        }

        return converted;
    }

    /**
     * Convert a single ObservationAktivitetRelationType to ObservationAktivitetRelation
     * 
     * @param source
     *            {@link ObservationAktivitetRelationType}
     * @return {@link ObservationAktivitetRelation}
     */
    private ObservationAktivitetRelation convertObservationRelation(ObservationAktivitetRelationType source) {
        ObservationAktivitetRelation converted = new ObservationAktivitetRelation();
        converted.setAktivitetsid(IsoTypeConverter.toId(source.getAktivitetsid()));
        converted.setObservationsid(IsoTypeConverter.toId(source.getObservationsid()));

        return converted;
    }

    /**
     * Converts a list of RekommendationType to Rekommendation
     * 
     * @param source
     *            {@link RekommendationType}
     * @return
     * @throws ConverterException
     */
    private Collection<? extends Rekommendation> convertRekommendationer(List<RekommendationType> source)
            throws ConverterException {
        if (source == null) {
            throw new ConverterException();
        }
        List<Rekommendation> rekommendationer = new ArrayList<Rekommendation>();
        for (RekommendationType rek : source) {
            rekommendationer.add(convertRekommendation(rek));
        }
        return rekommendationer;
    }

    /**
     * Convert a RekommendationType to Rekommendation
     * 
     * @param source
     *            {@link RekommendationType}
     * @return {@link Rekommendation}
     */
    private Rekommendation convertRekommendation(RekommendationType source) {
        Rekommendation rekommendation = new Rekommendation();
        if (source.getBeskrivning() != null) {
            rekommendation.setBeskrivning(source.getBeskrivning());
        }

        rekommendation.setRekommendationskod(IsoTypeConverter.toKod(source.getRekommendationskod()));

        if (!source.getKorkortsbehorighets().isEmpty()) {
            rekommendation.getVarde().addAll(convertListOfKod(source.getKorkortsbehorighets()));
        }

        if (source.isVarde() != null) {
            rekommendation.setBoolean_varde(source.isVarde());
        }

        return rekommendation;
    }

    /**
     * Convert a VardkontaktType to Vardkontakt
     * 
     * @param source
     *            {@link VardkontaktType}
     * @return {@link Vardkontakt}
     */
    private Vardkontakt convertVardkontakt(VardkontaktType source) {
        Vardkontakt vardkontakt = new Vardkontakt();
        vardkontakt.setIdkontroll(IsoTypeConverter.toKod(source.getIdKontroll()));
        vardkontakt.setVardkontakttyp(IsoTypeConverter.toKod(source.getVardkontakttyp()));

        return vardkontakt;
    }

    /**
     * Convert a collection of AktivitetType to collection of Aktivitet
     * 
     * @param source
     *            List of {@link AktivitetType}
     * @return List of {@link Aktivitet}
     * @throws ConverterException
     */
    private Collection<? extends Aktivitet> convertAktiviteter(List<AktivitetType> source) throws ConverterException {
        List<Aktivitet> aktiviteter = new ArrayList<Aktivitet>();
        for (AktivitetType akt : source) {
            aktiviteter.add(convertAktivitet(akt));
        }

        return aktiviteter;
    }

    /**
     * Convert a single AktivitetType to Aktivitet
     * 
     * @param source
     *            {@link AktivitetType}
     * @return {@link Aktivitet}
     */
    private Aktivitet convertAktivitet(AktivitetType source) {
        Aktivitet aktivitet = new Aktivitet();

        aktivitet.setAktivitetskod(IsoTypeConverter.toKod(source.getAktivitetskod()));

        if (source.getOstruktureradtid() != null) {
            aktivitet.setOstruktureradTid(source.getOstruktureradtid());
        }

        if (source.getBeskrivning() != null) {
            aktivitet.setBeskrivning(source.getBeskrivning());
        }

        if (source.getAktivitetsstatus() != null) {
            aktivitet.setAktivitetsstatus(IsoTypeConverter.toKod(source.getAktivitetsstatus()));
        }

        if (source.getAktivitetsid() != null) {
            aktivitet.setId(IsoTypeConverter.toId(source.getAktivitetsid()));
        }

        if (source.getMetod() != null) {
            aktivitet.setMetod(IsoTypeConverter.toKod(source.getMetod()));
        }

        if (source.getPlats() != null) {
            aktivitet.setPlats(source.getPlats());
        }

        if (source.isForekomst() != null) {
            aktivitet.setForekomst(source.isForekomst());
        }

        return aktivitet;
    }

    /**
     * Converts a collection of ObservationType to a collection of Observation
     * 
     * @param source
     *            List of {@link ObservationType}
     * @return List of {@link Observation}
     * @throws ConverterException
     */

    private Collection<? extends Observation> convertObservationer(List<ObservationType> source)
            throws ConverterException {
        if (source == null) {
            throw new ConverterException("No observations found, conversion stopped");
        }

        List<Observation> observationer = new ArrayList<Observation>();
        for (ObservationType obs : source) {
            observationer.add(convertObservation(obs));
        }

        return observationer;
    }

    /**
     * Convert a single ObservationType to Observation
     * 
     * @param source
     *            {@link ObservationType}
     * @return {@link Observation}
     */
    private Observation convertObservation(ObservationType source) {
        Observation observation = new Observation();

        if (source.getBeskrivning() != null) {
            observation.setBeskrivning(source.getBeskrivning());
        }

        if (source.isForekomst() != null) {
            observation.setForekomst(source.isForekomst());
        }

        if (source.getObservationsid() != null) {
            observation.setId(IsoTypeConverter.toId(source.getObservationsid()));
        }

        observation.setObservationskod(IsoTypeConverter.toKod(source.getObservationskod()));

        if (source.getLateralitet() != null) {
            observation.setLateralitet(IsoTypeConverter.toKod(source.getLateralitet()));
        }

        if (source.getVarde() != null) {
            observation.getVarde().add(IsoTypeConverter.toPhysicalQuantity(source.getVarde()));
        }

        if (source.getObservationsperiod() != null) {
            PartialInterval partialInterval = new PartialInterval();
            partialInterval.setFrom(source.getObservationsperiod().getFrom());
            observation.setObservationsperiod(partialInterval);
        }

        if (source.getObservationstid() != null) {
            observation.setObservationstid(source.getObservationstid());
        }

        return observation;
    }

    /**
     * Convert from List of CD to List of Kod
     * 
     * @param source
     *            List of {@link CD}
     * @return List of {@link Kod}
     */
    private List<Kod> convertCDtoKod(List<CD> source) {
        List<Kod> converted = new ArrayList<Kod>();
        for (CD cd : source) {
            converted.add(IsoTypeConverter.toKod(cd));
        }
        return converted;
    }

    /**
     * Convert HosPersonalType to HosPersonal
     * 
     * @param source
     *            {@link HosPersonalType}
     * @return {@link HosPersonal}
     * @throws ConverterException
     */
    private HosPersonal convertHosPersonal(HosPersonalType source) throws ConverterException {
        if (source == null) {
            throw new ConverterException("HosPersonal missing");
        }
        HosPersonal skapadAv = new HosPersonal();
        skapadAv.getBefattningar().addAll(convertCDtoKod(source.getBefattnings()));
        skapadAv.setId(IsoTypeConverter.toId(source.getPersonalId()));
        skapadAv.setNamn(source.getFullstandigtNamn());
        skapadAv.setVardenhet(convertVardenhet(source.getEnhet()));
        skapadAv.getSpecialiteter().addAll(convertCDtoKod(source.getSpecialitets()));

        return skapadAv;
    }

    /**
     * Convert EnhetType to Vardenhet
     * 
     * @param source
     *            {@link EnhetType}
     * @return {@link Vardenhet}
     * @throws ConverterException
     */
    private Vardenhet convertVardenhet(EnhetType source) throws ConverterException {
        if (source == null) {
            throw new ConverterException("Vardenhet missing");
        }

        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setId(IsoTypeConverter.toId(source.getEnhetsId()));
        vardenhet.setNamn(source.getEnhetsnamn());
        vardenhet.setPostadress(source.getPostadress());
        vardenhet.setPostnummer(source.getPostnummer());
        vardenhet.setPostort(source.getPostort());
        vardenhet.setTelefonnummer(source.getTelefonnummer());
        vardenhet.setVardgivare(convertVardgivare(source.getVardgivare()));

        return vardenhet;
    }

    /**
     * Convert VardgivareType to Vardgivare
     * 
     * @param source
     *            {@link VardgivareType}
     * @return {@link Vardgivare}
     * @throws ConverterException
     */
    private Vardgivare convertVardgivare(VardgivareType source) throws ConverterException {
        if (source == null) {
            throw new ConverterException("Vardgivare missing");
        }
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setId(IsoTypeConverter.toId(source.getVardgivareId()));
        vardgivare.setNamn(source.getVardgivarnamn());

        return vardgivare;
    }

    /**
     * Convert PatientType to Patient
     * 
     * @param source
     *            {@link PatientType}
     * @return {@link Patient}
     * @throws ConverterException
     */
    private Patient convertPatient(PatientType source) throws ConverterException {
        if (source == null) {
            throw new ConverterException("Missing patient");
        }
        Patient patient = new Patient();
        patient.setEfternamn(source.getEfternamn());
        patient.getFornamn().addAll(source.getFornamns());
        patient.setId(IsoTypeConverter.toId(source.getPersonId()));
        patient.setPostadress(source.getPostadress());
        patient.setPostnummer(source.getPostnummer());
        patient.setPostort(source.getPostort());

        return patient;
    }

    /**
     * Utility method for converting a List of CD to a List of Kod
     * 
     * @param cds
     *            List of {@link CD}
     * @return a List of {@link Kod}
     */
    private List<Kod> convertListOfKod(List<CD> cds) {
        List<Kod> koder = new ArrayList<Kod>();
        for (CD cd : cds) {
            koder.add(IsoTypeConverter.toKod(cd));
        }
        return koder;
    }
}
