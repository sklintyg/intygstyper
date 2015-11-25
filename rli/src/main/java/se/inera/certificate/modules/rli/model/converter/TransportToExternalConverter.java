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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.inera.intyg.common.support.model.PartialInterval;
import se.inera.intyg.common.support.model.PatientRelation;
import se.inera.intyg.common.support.model.Rekommendation;
import se.inera.intyg.common.support.model.Vardgivare;
import se.inera.certificate.modules.rli.model.external.Aktivitet;
import se.inera.certificate.modules.rli.model.external.Arrangemang;
import se.inera.certificate.modules.rli.model.external.HosPersonal;
import se.inera.certificate.modules.rli.model.external.Observation;
import se.inera.certificate.modules.rli.model.external.Patient;
import se.inera.certificate.modules.rli.model.external.Utforarroll;
import se.inera.certificate.modules.rli.model.external.Utlatande;
import se.inera.certificate.modules.rli.model.external.Vardenhet;
import se.inera.certificate.rli.model.v1.AktivitetType;
import se.inera.certificate.rli.model.v1.EnhetType;
import se.inera.certificate.rli.model.v1.HosPersonalType;
import se.inera.certificate.rli.model.v1.ObservationType;
import se.inera.certificate.rli.model.v1.PatientRelationType;
import se.inera.certificate.rli.model.v1.PatientType;
import se.inera.certificate.rli.model.v1.RekommendationType;
import se.inera.certificate.rli.model.v1.UtforarrollType;
import se.inera.certificate.rli.model.v1.VardgivareType;

import java.util.ArrayList;
import java.util.List;

/**
 * Converter between transport and external model.
 *
 * @author erik
 */
public class TransportToExternalConverter {

    private static final Logger LOG = LoggerFactory.getLogger(TransportToExternalConverter.class);

    /**
     * Converts from the transport format (se.inera.intyg.common.support.common.v1.Utlatande) to the external format
     * (se.inera.certificate.modules.rli.model.external.Utlatande).
     *
     * @param source Utlatande in the transport format to be converted to external format
     * @return se.inera.certificate.modules.rli.model.external.Utlatande
     * @throws ConverterException
     */
    public Utlatande convert(se.inera.certificate.rli.model.v1.Utlatande source) throws ConverterException {

        LOG.debug("Converting Utlatande '{}' from transport to external", source.getUtlatandeId());

        Utlatande externalModel = new Utlatande();

        externalModel.setId(IsoTypeConverter.toId(source.getUtlatandeId()));

        externalModel.setTyp(IsoTypeConverter.toKod(source.getTypAvUtlatande()));

        externalModel.getKommentarer().addAll(source.getKommentars());

        externalModel.setSigneringsdatum(source.getSigneringsdatum());

        externalModel.setSkickatdatum(source.getSkickatdatum());

        // Conversions from here on

        externalModel.setPatient(convertPatient(source.getPatient()));

        externalModel.setSkapadAv(convertHosPersonal(source.getSkapadAv()));

        externalModel.getAktiviteter().addAll(convertAktiviteter(source.getAktivitets()));

        externalModel.getObservationer().addAll(convertObservations(source.getObservations()));

        externalModel.setArrangemang(convertArrangemang(source.getArrangemang()));

        externalModel.getRekommendationer().addAll(convertRekommendationer(source.getRekommendations()));

        return externalModel;
    }

    /**
     * Convert List of RekommendationType to List of Rekommendation.
     *
     * @param source List of RekommendationType to be converted
     * @return List of Rekommendation
     * @throws ConverterException
     */
    private List<Rekommendation> convertRekommendationer(List<RekommendationType> source) throws ConverterException {
        LOG.trace("Converting rekommendationer");

        List<Rekommendation> rekommendationer = new ArrayList<>();

        if (source != null) {
            for (RekommendationType toConvert : source) {
                rekommendationer.add(convertRekommendation(toConvert));
            }
        }
        return rekommendationer;
    }

    /**
     * Convert RekommendationType to Rekommendation.
     *
     * @param source RekommendationType
     * @return Rekommendation, or null if source is null
     * @throws ConverterException
     */
    private Rekommendation convertRekommendation(RekommendationType source) throws ConverterException {
        if (source == null) {
            throw new ConverterException("RekommendationType was null, cannot convert");
        }
        Rekommendation rekommendation = new Rekommendation();

        rekommendation.setBeskrivning(source.getBeskrivning());
        rekommendation.setRekommendationskod(IsoTypeConverter.toKod(source.getRekommendationskod()));
        rekommendation.setSjukdomskannedom(IsoTypeConverter.toKod(source.getSjukdomskannedom()));
        return rekommendation;
    }

    /**
     * Iterates over a list of objects of the type ObservationType and converts them to type Observation.
     *
     * @param source List of objects of type ObservationType to be converted
     * @return List of objects of type Observation
     * @throws ConverterException
     */
    private List<Observation> convertObservations(List<ObservationType> source) throws ConverterException {

        LOG.trace("Converting observationer");

        List<Observation> observations = new ArrayList<>();
        if (source != null) {
            for (ObservationType ot : source) {
                observations.add(convertObservation(ot));
            }
        }
        return observations;
    }

    /**
     * Converts an object from ObservationType to Observation, this needs more work.
     *
     * @param source ObservationType to be converted
     * @return object of type Observation
     * @throws ConverterException
     */
    private Observation convertObservation(ObservationType source) throws ConverterException {
        if (source.getObservationskod() == null) {
            throw new ConverterException("Observationskod was null");
        }

        Observation observation = new Observation();

        if (source.getObservationsperiod() != null) {
            observation.setObservationsperiod(new PartialInterval(source.getObservationsperiod().getFrom(), source
                    .getObservationsperiod().getTom()));
        }

        observation.setObservationskod(IsoTypeConverter.toKod(source.getObservationskod()));

        if (source.getUtforsAv() != null) {
            observation.setUtforsAv(convertUtforarroll(source.getUtforsAv()));
        }
        return observation;
    }

    /**
     * Converts a list of UtforarrollType to Utforarroll.
     *
     * @param source List of UtforarrollType
     * @return List of Utforarroll
     * @throws ConverterException
     */
    private List<Utforarroll> convertUtforarroller(List<UtforarrollType> source) throws ConverterException {
        LOG.trace("Converting utforarroller");

        List<Utforarroll> utforsAvs = new ArrayList<>();
        if (source != null) {
            for (UtforarrollType toConvert : source) {
                utforsAvs.add(convertUtforarroll(toConvert));
            }
        }
        return utforsAvs;
    }

    /**
     * Convert UtforarrollType to Utforarroll.
     *
     * @param source UtforarrollType
     * @return Utforarroll, or null if source is null
     * @throws ConverterException
     */
    private Utforarroll convertUtforarroll(UtforarrollType source) throws ConverterException {
        if (source == null) {
            throw new ConverterException("Utforarroll was null");
        }

        Utforarroll utforarroll = new Utforarroll();

        if (source.getAntasAv() != null) {
            utforarroll.setAntasAv(convertHosPersonal(source.getAntasAv()));
        }

        utforarroll.setUtforartyp(IsoTypeConverter.toKod(source.getUtforartyp()));
        return utforarroll;
    }

    /**
     * Convert transport model Arrangemang to external model arrangemang.
     *
     * @param source se.inera.certificate.rli.v1.Arrangemang
     * @return import se.inera.certificate.modules.rli.model.external.Arrangemang
     * @throws ConverterException
     */
    private Arrangemang convertArrangemang(se.inera.certificate.rli.model.ext.v1.Arrangemang source)
            throws ConverterException {
        LOG.trace("Converting arrangemang");

        if (source == null) {
            throw new ConverterException("Arrangemang was null");
        }
        Arrangemang arrangemang = new Arrangemang();

        arrangemang.setArrangemangstid(new PartialInterval(source.getArrangemangstid().getFrom(), source
                .getArrangemangstid().getTom()));

        arrangemang.setArrangemangstyp(IsoTypeConverter.toKod(source.getArrangemangstyp()));

        if (source.getAvbestallningsdatum() != null) {
            arrangemang.setAvbestallningsdatum(source.getAvbestallningsdatum());
        }

        arrangemang.setBokningsdatum(source.getBokningsdatum());

        if (source.getBokningsreferens() != null) {
            arrangemang.setBokningsreferens(source.getBokningsreferens());
        }

        arrangemang.setPlats(source.getPlats());
        return arrangemang;
    }

    /**
     * Iterates through a list of AktivitetType and converts each subsequent item from AktivitetType to the Aktivitet
     * that the external model understands.
     *
     * @param source the list of AktivitetType's to be converted
     * @return a List containing Aktiviteter
     * @throws ConverterException
     */
    private List<Aktivitet> convertAktiviteter(List<AktivitetType> source) throws ConverterException {

        LOG.trace("Converting aktiviteter");

        List<Aktivitet> aktiviteter = new ArrayList<>();
        if (source != null) {
            for (AktivitetType aktivitet : source) {
                aktiviteter.add(convertAktivitet(aktivitet));
            }
        }
        return aktiviteter;
    }

    /**
     * Converts an object of AktivitetType to Aktivitet.
     *
     * @param source the object of AktivitetType to be converted
     * @return Aktivitet
     * @throws ConverterException
     */
    private Aktivitet convertAktivitet(AktivitetType source) throws ConverterException {

        if (source == null) {
            throw new ConverterException("Aktivitet was null");
        }

        Aktivitet aktivitet = new Aktivitet();

        aktivitet.setAktivitetskod(IsoTypeConverter.toKod(source.getAktivitetskod()));

        if (source.getAktivitetstid() != null) {
            aktivitet.setAktivitetstid(new PartialInterval(source.getAktivitetstid().getFrom(), source
                    .getAktivitetstid().getTom()));
        }
        if (source.getBeskrivning() != null) {
            aktivitet.setBeskrivning(source.getBeskrivning());
        }
        if (source.getUtforsVidEnhet() != null) {
            aktivitet.setUtforsVid(convertEnhet(source.getUtforsVidEnhet()));
        }
        if (source.getPlats() != null) {
            aktivitet.setPlats(source.getPlats());
        }

        if (!source.getBeskrivsAvs().isEmpty()) {
            aktivitet.getBeskrivsAv().addAll(convertUtforarroller(source.getBeskrivsAvs()));
        }
        return aktivitet;
    }

    /**
     * Converts HosPersonalType to HosPersonal, changing isoType II to Id.
     *
     * @param source HosPersonalType to be converted
     * @return HosPersonal, or null if source is null
     * @throws ConverterException
     */
    private HosPersonal convertHosPersonal(HosPersonalType source) throws ConverterException {

        LOG.trace("Converting HosPersonal");
        if (source == null) {
            throw new ConverterException();
        }

        HosPersonal hosPersonal = new HosPersonal();

        hosPersonal.setId(IsoTypeConverter.toId(source.getPersonalId()));
        hosPersonal.setNamn(source.getFullstandigtNamn());

        if (source.getForskrivarkod() != null) {
            hosPersonal.setForskrivarkod(source.getForskrivarkod());
        }

        if (source.getBefattning() != null) {
            hosPersonal.getBefattningar().add(source.getBefattning());
        }

        hosPersonal.setVardenhet(convertEnhet(source.getEnhet()));

        return hosPersonal;
    }

    /**
     * Converts PatientType to Patient.
     *
     * @param source PatientType to be converted
     * @return Patient, or null if source is null
     * @throws ConverterException
     */
    private Patient convertPatient(PatientType source) throws ConverterException {
        LOG.trace("Converting patient");

        if (source == null) {
            throw new ConverterException();
        }

        Patient patient = new Patient();
        patient.setId(IsoTypeConverter.toId(source.getPersonId()));

        patient.setPostadress(source.getPostadress());
        patient.setPostnummer(source.getPostnummer());
        patient.setPostort(source.getPostort());

        patient.getPatientrelationer().addAll(convertPatientRelations(source.getPatientRelations()));
        patient.getFornamn().addAll(source.getFornamns());
        patient.setEfternamn(source.getEfternamn());

        return patient;
    }

    private List<PatientRelation> convertPatientRelations(List<PatientRelationType> source) {
        List<PatientRelation> patientRelation = new ArrayList<>();
        if (source != null) {
            for (PatientRelationType prt : source) {
                patientRelation.add(convertPatientRelation(prt));
            }
        }
        return patientRelation;

    }

    private PatientRelation convertPatientRelation(PatientRelationType source) {
        PatientRelation patientRelation = new PatientRelation();

        patientRelation.setPersonId(IsoTypeConverter.toId(source.getPersonId()));
        patientRelation.setRelationskategori(IsoTypeConverter.toKod(source.getRelationskategori()));
        patientRelation.setPostadress(source.getPostadress());
        patientRelation.setPostnummer(source.getPostnummer());
        patientRelation.setPostort(source.getPostort());
        patientRelation.getFornamn().addAll(source.getFornamns());
        patientRelation.getMellannamn().addAll(source.getMellannamns());
        patientRelation.setEfternamn(source.getEfternamn());

        for (iso.v21090.dt.v1.CD cd : source.getRelationTyps()) {
            patientRelation.getRelationtyper().add(IsoTypeConverter.toKod(cd));
        }
        return patientRelation;

    }

    /**
     * Converts EnhetType to Enhet .
     *
     * @param source EnhetType to be converted
     * @return Enhet, or null if source is null
     * @throws ConverterException
     */

    private Vardenhet convertEnhet(EnhetType source) throws ConverterException {
        LOG.trace("Converting enhet");
        if (source == null) {
            throw new ConverterException();
        }
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setId(IsoTypeConverter.toId(source.getEnhetsId()));
        vardenhet.setNamn(source.getEnhetsnamn());
        vardenhet.setArbetsplatskod(IsoTypeConverter.toId(source.getArbetsplatskod()));
        vardenhet.setPostadress(source.getPostadress());
        vardenhet.setPostnummer(source.getPostnummer());
        vardenhet.setPostort(source.getPostort());
        vardenhet.setEpost(source.getEpost());
        vardenhet.setTelefonnummer(source.getTelefonnummer());
        vardenhet.setVardgivare(convertVardgivare(source.getVardgivare()));

        return vardenhet;
    }

    /**
     * Converts VardgivareType to Vardgivare.
     *
     * @param source VardgivareType to be converted
     * @return Vardgivare, or null if source is null
     * @throws ConverterException
     */
    private Vardgivare convertVardgivare(VardgivareType source) throws ConverterException {

        LOG.trace("Converting vardgivare");
        if (source == null) {
            throw new ConverterException();
        }
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setId(IsoTypeConverter.toId(source.getVardgivareId()));
        vardgivare.setNamn(source.getVardgivarnamn());

        return vardgivare;
    }
}
