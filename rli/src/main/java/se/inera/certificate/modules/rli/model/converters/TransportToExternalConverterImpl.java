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
package se.inera.certificate.modules.rli.model.converters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import riv.insuranceprocess.healthreporting._2.EnhetType;
import riv.insuranceprocess.healthreporting._2.HosPersonalType;
import riv.insuranceprocess.healthreporting._2.VardgivareType;
import se.inera.certificate.common.v1.AktivitetType;
import se.inera.certificate.common.v1.ObservationType;
import se.inera.certificate.common.v1.PatientRelationType;
import se.inera.certificate.common.v1.PatientType;
import se.inera.certificate.common.v1.RekommendationType;
import se.inera.certificate.common.v1.UtforarrollType;

//Common model
import se.inera.certificate.model.HosPersonal;
import se.inera.certificate.model.Observation;
import se.inera.certificate.model.PartialInterval;
import se.inera.certificate.model.Patient;
import se.inera.certificate.model.PatientRelation;
import se.inera.certificate.model.Rekommendation;
import se.inera.certificate.model.Utforarroll;
import se.inera.certificate.model.Vardenhet;
import se.inera.certificate.model.Vardgivare;

//Specific for RLI
import se.inera.certificate.modules.rli.model.external.Aktivitet;
import se.inera.certificate.modules.rli.model.external.Arrangemang;
import se.inera.certificate.modules.rli.model.external.Utlatande;

/**
 * Converter between transport and external model.
 * 
 * @author erik
 * 
 */
public class TransportToExternalConverterImpl implements TransportToExternalConverter {

    private static final Logger LOG = LoggerFactory.getLogger(TransportToExternalConverterImpl.class);

    public TransportToExternalConverterImpl() {

    }

    /**
     * Converts from the transport format
     * (se.inera.certificate.common.v1.Utlatande) to the external format
     * (se.inera.certificate.modules.rli.model.external.Utlatande).
     * 
     * @param source
     *            Utlatande in the transport format to be converted to external
     *            format
     * @return se.inera.certificate.modules.rli.model.external.Utlatande
     */
    @Override
    public Utlatande transportToExternal(se.inera.certificate.common.v1.Utlatande source) {

        LOG.debug("Starting conversion");

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
     * @param source
     *            List of RekommendationType to be converted
     * @return List of Rekommendation
     */
    List<Rekommendation> convertRekommendationer(List<RekommendationType> source) {
        LOG.debug("Converting rekommendationer");

        List<Rekommendation> rekommendationer = new ArrayList<Rekommendation>();

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
     * @param source
     *            RekommendationType
     * @return Rekommendation, or null if source is null
     */
    private Rekommendation convertRekommendation(RekommendationType source) {
        if (source == null) {
            return null;
        }
        Rekommendation rekommendation = new Rekommendation();

        rekommendation.setBeskrivning(source.getBeskrivning());
        rekommendation.setRekommendationskod(IsoTypeConverter.toKod(source.getRekommendationskod()));
        rekommendation.setSjukdomskannedom(IsoTypeConverter.toKod(source.getSjukdomskannedom()));
        return rekommendation;
    }

    /**
     * Iterates over a list of objects of the type ObservationType and converts
     * them to type Observation.
     * 
     * @param source
     *            List of objects of type ObservationType to be converted
     * @return List of objects of type Observation
     */
    List<Observation> convertObservations(List<ObservationType> source) {

        LOG.debug("Converting observationer");

        List<Observation> observations = new ArrayList<Observation>();
        if (source != null) {
            for (ObservationType ot : source) {
                observations.add(convertObservation(ot));
            }
        }
        return observations;
    }

    /**
     * Converts an object from ObservationType to Observation, this needs more
     * work.
     * 
     * @param source
     *            ObservationType to be converted
     * @return object of type Observation
     */
    private Observation convertObservation(ObservationType source) {
        Observation observation = new Observation();

        observation.setObservationsId(IsoTypeConverter.toId(source.getObservationsId()));

        if (source.getObservationsperiod() != null) {
            observation.setObservationsPeriod(new PartialInterval(source.getObservationsperiod().getFrom(), source
                    .getObservationsperiod().getTom()));
        }
        observation.setObservationsKod(IsoTypeConverter.toKod(source.getObservationskod()));
        observation.setUtforsAv(convertUtforarroll(source.getUtforsAv()));
        return observation;
    }

    /**
     * Converts a list of UtforarrollType to Utforarroll.
     * 
     * @param source
     *            List of UtforarrollType
     * @return List of Utforarroll
     */
    List<Utforarroll> convertUtforarroller(List<UtforarrollType> source) {
        LOG.debug("Converting utforarroller");

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
     * @param source
     *            UtforarrollType
     * @return Utforarroll, or null if source is null
     */
    private Utforarroll convertUtforarroll(UtforarrollType source) {
        if (source == null) {
            return null;
        }
        Utforarroll utforarroll = new Utforarroll();
        utforarroll.setAntasAv(convertHosPersonal(source.getAntasAv()));
        utforarroll.setUtforartyp(IsoTypeConverter.toKod(source.getUtforartyp()));
        return utforarroll;
    }

    /**
     * Convert transport model Arrangemang to external model arrangemang.
     * 
     * @param source
     *            se.inera.certificate.rli.v1.Arrangemang
     * @return import
     *         se.inera.certificate.modules.rli.model.external.Arrangemang
     */
    Arrangemang convertArrangemang(se.inera.certificate.rli.v1.Arrangemang source) {
        LOG.debug("Converting arrangemang");

        if (source == null) {
            return null;
        }
        Arrangemang arrangemang = new Arrangemang();

        arrangemang.setArrangemangstid(new PartialInterval(source.getArrangemangstid().getFrom(), source
                .getArrangemangstid().getTom()));
        arrangemang.setArrangemangstyp(IsoTypeConverter.toKod(source.getArrangemangstyp()));
        arrangemang.setAvbestallningsdatum(source.getAvbestallningsdatum());
        arrangemang.setBokningsdatum(source.getBokningsdatum());
        arrangemang.setBokningsreferens(source.getBokningsreferens());
        arrangemang.setPlats(source.getPlats());
        return arrangemang;
    }

    /**
     * Iterates through a list of AktivitetType and converts each subsequent
     * item from AktivitetType to the Aktivitet that the external model
     * understands.
     * 
     * @param source
     *            the list of AktivitetType's to be converted
     * @return a List containing Aktiviteter
     */
    List<Aktivitet> convertAktiviteter(List<AktivitetType> source) {

        LOG.debug("Converting aktiviteter");

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
     * @param source
     *            the object of AktivitetType to be converted
     * @return Aktivitet
     */
    private Aktivitet convertAktivitet(AktivitetType source) {

        if (source == null) {
            return null;
        }

        Aktivitet aktivitet = new Aktivitet();

        aktivitet.setAktivitetsid(IsoTypeConverter.toId(source.getAktivitetsId()));
        aktivitet.setBeskrivning(source.getBeskrivning());
        aktivitet.setAktivitetskod(IsoTypeConverter.toKod(source.getAktivitetskod()));
        if (source.getAktivitetstid() != null) {
            aktivitet.setAktivitetstid(new PartialInterval(source.getAktivitetstid().getFrom(), source
                    .getAktivitetstid().getTom()));
        }
        aktivitet.setBeskrivning(source.getBeskrivning());
        aktivitet.setUtforsVid(convertEnhet(source.getUtforsVidEnhet()));
        //aktivitet.getUtforsAvs().addAll(convertUtforarroller(source.getUtforsAvs()));

        return aktivitet;
    }

    /**
     * Converts HosPersonalType to HosPersonal, changing isoType II to Id.
     * 
     * @param source
     *            HosPersonalType to be converted
     * @return HosPersonal, or null if source is null
     */
    HosPersonal convertHosPersonal(HosPersonalType source) {

        LOG.debug("Converting HosPersonal");
        if (source == null) {
            return null;
        }

        HosPersonal hosPersonal = new HosPersonal();
        hosPersonal.setId(IsoTypeConverter.toId(source.getPersonalId()));
        hosPersonal.setNamn(source.getFullstandigtNamn());
        hosPersonal.setForskrivarkod(source.getForskrivarkod());
        hosPersonal.setVardenhet(convertEnhet(source.getEnhet()));

        return hosPersonal;
    }

    /**
     * Converts PatientType to Patient.
     * 
     * @param source
     *            PatientType to be converted
     * @return Patient, or null if source is null
     */
    Patient convertPatient(PatientType source) {
        LOG.debug("Converting patient");

        if (source == null) {
            return null;
        }

        Patient patient = new Patient();
        patient.setId(IsoTypeConverter.toId(source.getPersonId()));

        patient.setPostadress(source.getPostadress());
        patient.setPostnummer(source.getPostnummer());
        patient.setPostort(source.getPostort());

        patient.setPatientrelations(convertPatientRelations(source.getPatientRelations()));
        patient.setFornamns(source.getFornamns());
        patient.setMellannamns(source.getMellannamns());
        patient.setEfternamns(Arrays.asList(source.getEfternamn()));

        return patient;
    }

    private List<PatientRelation> convertPatientRelations(List<PatientRelationType> source) {
        List<PatientRelation> patientRelation = new ArrayList<PatientRelation>();
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
        patientRelation.getFornamns().addAll(source.getFornamns());
        patientRelation.setEfternamn(source.getEfternamn());
        patientRelation.getMellannamns().addAll(source.getMellannamns());

        for (iso.v21090.dt.v1.CD cd : source.getRelationTyps()) {
            patientRelation.getRelationTyps().add(IsoTypeConverter.toKod(cd));
        }
        return patientRelation;

    }

    /**
     * Converts EnhetType to Enhet .
     * 
     * @param source
     *            EnhetType to be converted
     * @return Enhet, or null if source is null
     */

    Vardenhet convertEnhet(EnhetType source) {
        LOG.debug("Converting enhet");
        if (source == null) {
            return null;
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
     * @param source
     *            VardgivareType to be converted
     * @return Vardgivare, or null if source is null
     */
    Vardgivare convertVardgivare(VardgivareType source) {

        LOG.debug("Converting vardgivare");
        if (source == null) {
            return null;
        }
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setId(IsoTypeConverter.toId(source.getVardgivareId()));
        vardgivare.setNamn(source.getVardgivarnamn());

        return vardgivare;
    }

}
