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

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.Partial;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.certificate.model.PartialInterval;
import se.inera.certificate.modules.rli.model.codes.AktivitetsKod;
import se.inera.certificate.modules.rli.model.codes.ArrangemangsKod;
import se.inera.certificate.modules.rli.model.codes.CodeConverter;
import se.inera.certificate.modules.rli.model.codes.ObservationsKod;
import se.inera.certificate.modules.rli.model.codes.RekommendationsKod;
import se.inera.certificate.modules.rli.model.codes.SjukdomskannedomKod;
import se.inera.certificate.modules.rli.model.external.Aktivitet;
import se.inera.certificate.modules.rli.model.external.HosPersonal;
import se.inera.certificate.modules.rli.model.external.Observation;
import se.inera.certificate.modules.rli.model.external.Utforarroll;
import se.inera.certificate.modules.rli.model.internal.mi.Arrangemang;
import se.inera.certificate.modules.rli.model.internal.mi.Graviditet;
import se.inera.certificate.modules.rli.model.internal.mi.HoSPersonal;
import se.inera.certificate.modules.rli.model.internal.mi.KomplikationStyrkt;
import se.inera.certificate.modules.rli.model.internal.mi.OrsakAvbokning;
import se.inera.certificate.modules.rli.model.internal.mi.Patient;
import se.inera.certificate.modules.rli.model.internal.mi.Rekommendation;
import se.inera.certificate.modules.rli.model.internal.mi.Undersokning;
import se.inera.certificate.modules.rli.model.internal.mi.Utforare;
import se.inera.certificate.modules.rli.model.internal.mi.Utlatande;
import se.inera.certificate.modules.rli.model.internal.mi.Vardenhet;
import se.inera.certificate.modules.rli.model.internal.mi.Vardgivare;

/**
 * Converter for converting the external format to the internal view format.
 * 
 * 
 * @author Niklas Pettersson, R2M
 * 
 */
public class ExternalToInternalConverter {

    private static final Logger LOG = LoggerFactory.getLogger(ExternalToInternalConverter.class);

    public Utlatande convert(se.inera.certificate.modules.rli.model.external.Utlatande externalUtlatande)
            throws ConverterException {
        Utlatande intUtlatande = convertUtlatandeFromExternalToInternal(externalUtlatande);
        LOG.trace("Converting external model to internal");
        return intUtlatande;
    }

    private Utlatande convertUtlatandeFromExternalToInternal(
            se.inera.certificate.modules.rli.model.external.Utlatande extUtlatande) throws ConverterException {
        LOG.debug("Converting Utlatande '{}' from external to internal", extUtlatande.getId());

        Utlatande intUtlatande = new Utlatande();

        intUtlatande.setUtlatandeid(InternalModelConverterUtils.getExtensionFromId(extUtlatande.getId()));
        intUtlatande.setTypAvUtlatande(InternalModelConverterUtils.getValueFromKod(extUtlatande.getTyp()));
        intUtlatande.setSigneringsdatum(extUtlatande.getSigneringsdatum());
        intUtlatande.setSkickatdatum(extUtlatande.getSkickatdatum());
        intUtlatande.setKommentarer(extUtlatande.getKommentarer());

        HoSPersonal intHoSPersonal = convertToIntHoSPersonal(extUtlatande.getSkapadAv());
        intUtlatande.setSkapadAv(intHoSPersonal);

        Patient intPatient = convertToIntPatient(extUtlatande.getPatient());
        intUtlatande.setPatient(intPatient);

        Arrangemang intArrangemang = convertToIntArrangemang(extUtlatande.getArrangemang());
        intUtlatande.setArrangemang(intArrangemang);

        Undersokning intUndersokning = createUndersokning(extUtlatande);
        intUtlatande.setUndersokning(intUndersokning);

        Rekommendation intRekommendation = createRekommendation(extUtlatande);
        intUtlatande.setRekommendation(intRekommendation);

        return intUtlatande;
    }

    private Vardenhet convertToIntVardenhet(se.inera.certificate.model.Vardenhet extVardenhet)
            throws ConverterException {

        LOG.trace("Converting vardenhet");

        if (extVardenhet == null) {
            throw new ConverterException("External Vardenhet is null, can not convert");
        }

        Vardenhet intVardenhet = new Vardenhet();

        intVardenhet.setEnhetsid(InternalModelConverterUtils.getExtensionFromId(extVardenhet.getId()));
        intVardenhet.setEnhetsnamn(extVardenhet.getNamn());
        intVardenhet.setPostadress(extVardenhet.getPostadress());
        intVardenhet.setPostnummer(extVardenhet.getPostnummer());
        intVardenhet.setPostort(extVardenhet.getPostort());
        intVardenhet.setTelefonnummer(extVardenhet.getTelefonnummer());
        intVardenhet.setEpost(extVardenhet.getEpost());

        Vardgivare intVardgivare = convertToIntVardgivare(extVardenhet.getVardgivare());
        intVardenhet.setVardgivare(intVardgivare);

        return intVardenhet;
    }

    private Vardgivare convertToIntVardgivare(se.inera.certificate.model.Vardgivare extVardgivare)
            throws ConverterException {

        LOG.trace("Converting vardgivare");

        if (extVardgivare == null) {
            throw new ConverterException("External vardgivare is null, can not convert");
        }

        Vardgivare intVardgivare = new Vardgivare();

        intVardgivare.setVardgivarid(InternalModelConverterUtils.getExtensionFromId(extVardgivare.getId()));
        intVardgivare.setVardgivarnamn(extVardgivare.getNamn());

        return intVardgivare;
    }

    private HoSPersonal convertToIntHoSPersonal(HosPersonal extHoSPersonal) throws ConverterException {

        LOG.trace("Converting HoSPersonal");

        if (extHoSPersonal == null) {
            throw new ConverterException("External HoSPersonal is null, can not convert");
        }

        HoSPersonal intHoSPersonal = new HoSPersonal();

        intHoSPersonal.setPersonid(InternalModelConverterUtils.getExtensionFromId(extHoSPersonal.getId()));
        intHoSPersonal.setFullstandigtNamn(extHoSPersonal.getNamn());
        intHoSPersonal.setBefattning(extHoSPersonal.getBefattning());

        Vardenhet intVardenhet = convertToIntVardenhet(extHoSPersonal.getVardenhet());
        intHoSPersonal.setVardenhet(intVardenhet);

        return intHoSPersonal;
    }

    private Patient convertToIntPatient(se.inera.certificate.model.Patient extPatient) throws ConverterException {

        LOG.trace("Converting patient");

        if (extPatient == null) {
            throw new ConverterException("No Patient found to convert");
        }

        Patient intPatient = new Patient();

        intPatient.setPersonid(InternalModelConverterUtils.getExtensionFromId(extPatient.getId()));

        intPatient.setEfternamn(extPatient.getEfternamn());

        String forNamn = StringUtils.join(extPatient.getFornamn(), " ");
        intPatient.setFornamn(forNamn);

        String fullstandigtNamn = forNamn.concat(" ").concat(extPatient.getEfternamn());
        intPatient.setFullstandigtNamn(fullstandigtNamn);

        intPatient.setPostadress(extPatient.getPostadress());
        intPatient.setPostnummer(extPatient.getPostnummer());
        intPatient.setPostort(extPatient.getPostort());

        return intPatient;
    }

    private Arrangemang convertToIntArrangemang(se.inera.certificate.modules.rli.model.external.Arrangemang extArr)
            throws ConverterException {

        LOG.trace("Converting arrangemang");

        if (extArr == null) {
            throw new ConverterException("No arrangemang found to convert");
        }

        Arrangemang intArr = new Arrangemang();

        intArr.setPlats(extArr.getPlats());

        ArrangemangsKod arrTyp = CodeConverter.fromCode(extArr.getArrangemangstyp(), ArrangemangsKod.class);
        intArr.setArrangemangstyp(arrTyp);

        intArr.setBokningsreferens(extArr.getBokningsreferens());

        Partial extBokningsDatum = extArr.getBokningsdatum();
        intArr.setBokningsdatum(PartialConverter.partialToString(extBokningsDatum));

        Partial extAvbestDatum = extArr.getAvbestallningsdatum();
        intArr.setAvbestallningsdatum(PartialConverter.partialToString(extAvbestDatum));

        PartialInterval arrangemangsTid = extArr.getArrangemangstid();

        if (arrangemangsTid != null) {
            intArr.setArrangemangsdatum(PartialConverter.partialToString(arrangemangsTid.getFrom()));
            String tom = (arrangemangsTid.getTom() != null) ? PartialConverter
                    .partialToString(arrangemangsTid.getTom()) : PartialConverter.partialToString(arrangemangsTid
                    .getFrom());
            intArr.setArrangemangslutdatum(tom);
        }

        return intArr;
    }

    private Utforare convertToIntUtforare(Utforarroll source) throws ConverterException {
        LOG.trace("Converting to internal Utforare");
        Utforare utforsAv = new Utforare();
        utforsAv.setUtforartyp(source.getUtforartyp().getCode());
        if (source.getAntasAv() != null) {
            utforsAv.setAntasAv(convertToIntHoSPersonal(source.getAntasAv()));
        }
        return utforsAv;
    }

    private Rekommendation createRekommendation(se.inera.certificate.modules.rli.model.external.Utlatande extUtlatande)
            throws ConverterException {
        LOG.trace("Creating and poulating Rekommendation");

        Rekommendation intRekommendation = new Rekommendation();

        if (extUtlatande.getRekommendationer().size() != 1) {
            throw new ConverterException("- Rekommendationer should contain only 1 Rekommendation");
        }

        se.inera.certificate.model.Rekommendation extRekommendation = extUtlatande.getRekommendationer().get(0);

        intRekommendation.setRekommendationskod(CodeConverter.fromCode(extRekommendation.getRekommendationskod(),
                RekommendationsKod.class));
        intRekommendation.setSjukdomskannedom(CodeConverter.fromCode(extRekommendation.getSjukdomskannedom(),
                SjukdomskannedomKod.class));
        intRekommendation.setBeskrivning(extRekommendation.getBeskrivning());

        return intRekommendation;
    }

    private Undersokning createUndersokning(se.inera.certificate.modules.rli.model.external.Utlatande extUtlatande)
            throws ConverterException {
        LOG.trace("Creating and populating Undersokning");

        Undersokning intUndersokning = new Undersokning();

        populateUndersokningFromObservationer(extUtlatande.getObservationer(), intUndersokning);
        populateUndersokningFromAktiviteter(extUtlatande.getAktiviteter(), intUndersokning);

        return intUndersokning;
    }

    private void populateUndersokningFromObservationer(List<Observation> observationer, Undersokning intUndersokning)
            throws ConverterException {
        LOG.trace("Populating Undersokning from Observationer");

        if (observationer.isEmpty()) {
            throw new ConverterException("No observations found! Can not populate undersokning!");
        }

        for (Observation observation : observationer) {
            if (ObservationsKod.SJUKDOM.matches(observation.getObservationskod())) {
                intUndersokning.setOrsakforavbokning(OrsakAvbokning.RESENAR_SJUK);
                if (observation.getUtforsAv() != null) {
                    intUndersokning.setUtforsAv(convertToIntUtforare(observation.getUtforsAv()));
                }

            } else if (ObservationsKod.GRAVIDITET.matches(observation.getObservationskod())) {

                intUndersokning.setOrsakforavbokning(OrsakAvbokning.RESENAR_GRAVID);
                if (observation.getUtforsAv() != null) {
                    intUndersokning.setUtforsAv(convertToIntUtforare(observation.getUtforsAv()));
                }

                String estimatedDeliveryDate = PartialConverter.partialToString(observation.getObservationsperiod()
                        .getTom());
                Graviditet pregnancyInfo = new Graviditet();
                pregnancyInfo.setBeraknatForlossningsdatum(estimatedDeliveryDate);
                intUndersokning.setGraviditet(pregnancyInfo);
            }
        }
    }

    private void populateUndersokningFromAktiviteter(List<Aktivitet> aktiviteter, Undersokning intUndersokning)
            throws ConverterException {
        LOG.trace("Populating Undersokning from Aktiviteter");

        if (aktiviteter.isEmpty()) {
            throw new ConverterException("No aktiviteter found, can not continue with population");
        }

        for (Aktivitet aktivitet : aktiviteter) {
            if (AktivitetsKod.FORSTA_UNDERSOKNING.matches(aktivitet.getAktivitetskod())) {
                populateFirstExam(intUndersokning, aktivitet);

            } else if (AktivitetsKod.KLINISK_UNDERSOKNING.matches(aktivitet.getAktivitetskod())) {
                populateCurrentExam(intUndersokning, aktivitet);
            }
        }
    }

    private void populateFirstExam(Undersokning intUndersokning, Aktivitet firstExam) throws ConverterException {
        if (firstExam == null) {
            throw new ConverterException("- firstExam is null, can not populate first exam info");
        }

        String forstaUndersokningDatum = getExamDateFromAktivitet(firstExam);
        intUndersokning.setForstaUndersokningsdatum(forstaUndersokningDatum);

        KomplikationStyrkt komplikationStyrkt;
        String forstaUndersokningPlats;

        if (firstExam.getUtforsVid() != null) {
            komplikationStyrkt = KomplikationStyrkt.AV_HOS_PERSONAL;
            forstaUndersokningPlats = firstExam.getUtforsVid().getNamn();
            intUndersokning.setUtforsVid(convertToIntVardenhet(firstExam.getUtforsVid()));

        } else {
            komplikationStyrkt = KomplikationStyrkt.AV_PATIENT;
            forstaUndersokningPlats = firstExam.getPlats();
        }

        intUndersokning.setForstaUndersokningsplats(forstaUndersokningPlats);
        intUndersokning.setKomplikationstyrkt(komplikationStyrkt);
    }

    private void populateCurrentExam(Undersokning intUndersokning, Aktivitet currentExam) throws ConverterException {
        if (currentExam == null) {
            throw new ConverterException("- currentExam is null, can not populate current exam info");
        }

        String undersokningDatum = getExamDateFromAktivitet(currentExam);
        intUndersokning.setUndersokningsdatum(undersokningDatum);

        if (currentExam.getUtforsVid() != null) {
            String undersokningPlats = currentExam.getUtforsVid().getNamn();
            intUndersokning.setUndersokningsplats(undersokningPlats);
            intUndersokning.setUtforsVid(convertToIntVardenhet(currentExam.getUtforsVid()));

        } else {
            throw new ConverterException("- Place for current exam could not be determined");
        }
    }

    private String getExamDateFromAktivitet(Aktivitet aktivitet) {

        PartialInterval aktivitetstid = aktivitet.getAktivitetstid();

        if (aktivitetstid == null) {
            return null;
        }

        if (aktivitetstid.getFrom() != null) {
            return PartialConverter.partialToString(aktivitetstid.getFrom());
        }

        return null;
    }
}
