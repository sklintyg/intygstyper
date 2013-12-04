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

import static se.inera.certificate.modules.rli.model.codes.HSpersonalKod.HSA_ID;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.certificate.model.HosPersonal;
import se.inera.certificate.model.Id;
import se.inera.certificate.model.Kod;
import se.inera.certificate.model.Observation;
import se.inera.certificate.model.Patient;
import se.inera.certificate.model.Rekommendation;
import se.inera.certificate.model.Utforarroll;
import se.inera.certificate.model.Vardenhet;
import se.inera.certificate.model.Vardgivare;
import se.inera.certificate.modules.rli.model.codes.AktivitetsKod;
import se.inera.certificate.modules.rli.model.codes.CodeConverter;
import se.inera.certificate.modules.rli.model.codes.HSpersonalKod;
import se.inera.certificate.modules.rli.model.codes.ObservationsKod;
import se.inera.certificate.modules.rli.model.codes.UtforarrollKod;
import se.inera.certificate.modules.rli.model.codes.UtlatandeKod;
import se.inera.certificate.modules.rli.model.external.Aktivitet;
import se.inera.certificate.modules.rli.model.external.Arrangemang;
import se.inera.certificate.modules.rli.model.external.Utlatande;
import se.inera.certificate.modules.rli.model.internal.wc.HoSPersonal;
import se.inera.certificate.modules.rli.model.internal.wc.OrsakAvbokning;
import se.inera.certificate.modules.rli.model.internal.wc.Undersokning;
import se.inera.certificate.modules.rli.model.internal.wc.Utforare;

public class InternalToExternalConverter {

    private static final Logger LOG = LoggerFactory.getLogger(InternalToExternalConverter.class);

    private static final String PERS_ID_ROOT = "1.2.752.129.2.1.3.1";

    public Utlatande convertUtlatandeFromInternalToExternal(
            se.inera.certificate.modules.rli.model.internal.wc.Utlatande source) throws ConverterException {

        LOG.debug("Converting Utlatande '{}' from internal to external", source.getUtlatandeid());

        Utlatande utlatande = new Utlatande();
        utlatande.setId(new Id(source.getUtlatandeidroot(), source.getUtlatandeid()));
        utlatande.setTyp(CodeConverter.toKod(UtlatandeKod.IVAR));
        utlatande.setSigneringsdatum(source.getSigneringsdatum());
        utlatande.setSkickatdatum(source.getSkickatdatum());

        utlatande.setSkapadAv(convertHoSPersonal(source.getSkapadAv()));
        utlatande.setPatient(convertPatient(source.getPatient()));
        utlatande.setArrangemang(convertArrangemang(source.getArrangemang()));
        utlatande.getObservationer().addAll(convertObservationer(source.getUndersokning()));
        utlatande.getAktiviteter().addAll(convertAktiviteter(source.getUndersokning()));
        utlatande.getRekommendationer().addAll(convertRekommendationer(source.getRekommendation()));
        utlatande.getKommentarer().addAll(source.getKommentarer());

        return utlatande;

    }

    private List<Rekommendation> convertRekommendationer(
            se.inera.certificate.modules.rli.model.internal.wc.Rekommendation source) {
        Rekommendation rek = new Rekommendation();
        rek.setBeskrivning(source.getBeskrivning());
        rek.setRekommendationskod(CodeConverter.toKod(source.getRekommendationskod()));
        rek.setSjukdomskannedom(CodeConverter.toKod(source.getSjukdomskannedom()));
        return Arrays.asList(rek);
    }

    List<Observation> convertObservationer(Undersokning source) {
        LOG.trace("Converting Observationer");
        List<Observation> obs = new ArrayList<Observation>();

        if (source.getOrsakforavbokning() == OrsakAvbokning.RESENAR_SJUK) {
            Observation o = new Observation();
            o.setObservationskod(CodeConverter.toKod(ObservationsKod.SJUKDOM));
            o.setUtforsAv(convertUtforsAv(source.getUtforsAv()));
            obs.add(o);

        }
        if (source.getOrsakforavbokning() == OrsakAvbokning.RESENAR_GRAVID) {
            Observation o1 = new Observation();
            o1.setObservationskod(CodeConverter.toKod(ObservationsKod.KOMPLIKATION_VID_GRAVIDITET));
            o1.setUtforsAv(convertUtforsAv(source.getUtforsAv()));
            obs.add(o1);

            Observation o2 = new Observation();
            o2.setObservationskod(CodeConverter.toKod(ObservationsKod.GRAVIDITET));
            o2.setObservationsperiod(PartialConverter.toPartialInterval(null, source.getGraviditet()
                    .getBeraknatForlossningsdatum()));
            o2.setUtforsAv(convertUtforsAv(source.getUtforsAv()));

            obs.add(o2);
        }
        // TODO: Don't forget ObservationsKod.ANHORIG_SJUK

        return obs;
    }

    private Utforarroll convertUtforsAv(Utforare source) {
        if (source == null) {
            LOG.trace("No utforare found when converting, hence null");
            return null;
        }
        Utforarroll utforsAv = new Utforarroll();

        utforsAv.setUtforartyp(CodeConverter.toKod(source.getUtforartyp()));
        utforsAv.setAntasAv(convertHoSPersonal(source.getAntasAv()));

        return utforsAv;
    }

    List<Aktivitet> convertAktiviteter(Undersokning source) throws ConverterException {
        List<Aktivitet> aktiviteter = new ArrayList<Aktivitet>();
        buildAktiviteter(aktiviteter, source);
        return aktiviteter;
    }

    /**
     * Create necessary Aktiviteter for a pregnant person.
     * 
     * @param aktiviteter
     *            ArrayList of Aktiviteter
     * @param undersokning
     *            se.inera.certificate.modules.rli.model.internal.Undersokning
     * @throws ConverterException
     */
    private void buildAktiviteterGravid(List<Aktivitet> aktiviteter, Undersokning undersokning)
            throws ConverterException {

        /** Create first Aktivitet */
        Aktivitet akt1 = new Aktivitet();
        akt1.setAktivitetskod(CodeConverter.toKod(AktivitetsKod.FORSTA_UNDERSOKNING));

        String undersokningsDatum = undersokning.getForstaUndersokningsdatum();
        if (undersokningsDatum != null) {
            akt1.setAktivitetstid(PartialConverter.toPartialInterval(undersokningsDatum, undersokningsDatum));
        }

        if (undersokning.getForstaUndersokningsplats() != null) {
            akt1.setPlats(undersokning.getForstaUndersokningsplats());
        }

        aktiviteter.add(akt1);

        /** Create second Aktivitet */
        String undersokningsDatum2 = undersokning.getUndersokningsdatum();

        if (undersokningsDatum2 == null) {
            throw new ConverterException("Mandatory date in KLINISK_UNDERSOKNING missing, aborting");
        }

        Aktivitet akt2 = new Aktivitet();

        akt2.setAktivitetskod(CodeConverter.toKod(AktivitetsKod.KLINISK_UNDERSOKNING));

        akt2.setAktivitetstid(PartialConverter.toPartialInterval(undersokningsDatum2, undersokningsDatum2));

        /** Determine what kind of location to add for second Aktivitet */
        // TODO: This should always be UtforsVid no?

        if (undersokning.getUtforsVid() != null) {
            akt1.setUtforsVid(buildVardenhet(undersokning.getUtforsVid()));
            akt1.getBeskrivsAv().add(buildBeskrivsAv(undersokning));
        }
        aktiviteter.add(akt2);

    }

    /**
     * 
     * Create necessary Aktiviteter
     * 
     * @param aktiviteter
     *            ArrayList of Aktiviteter
     * @param undersokning
     *            se.inera.certificate.modules.rli.model.internal.Undersokning
     * @throws ConverterException
     */
    private void buildAktiviteter(List<Aktivitet> aktiviteter, Undersokning undersokning) throws ConverterException {
        if (undersokning.getUndersokningsdatum() == null) {
            throw new ConverterException("Mandatory date in KLINISK_UNDERSOKNING missing, aborting");
        }

        if (undersokning.getForstaUndersokningsdatum() != null && undersokning.getForstaUndersokningsplats() != null) {

            Aktivitet akt1 = new Aktivitet();

            akt1.setAktivitetskod(CodeConverter.toKod(AktivitetsKod.FORSTA_UNDERSOKNING));

            akt1.setAktivitetstid(PartialConverter.toPartialInterval(undersokning.getForstaUndersokningsdatum(),
                    undersokning.getForstaUndersokningsdatum()));

            akt1.setPlats(undersokning.getForstaUndersokningsplats());

            akt1.getBeskrivsAv().add(buildBeskrivsAv(undersokning));

            aktiviteter.add(akt1);
        }

        Aktivitet akt2 = new Aktivitet();

        akt2.setAktivitetskod(CodeConverter.toKod(AktivitetsKod.KLINISK_UNDERSOKNING));

        String undersokningsdatum = undersokning.getUndersokningsdatum();

        akt2.setAktivitetstid(PartialConverter.toPartialInterval(undersokningsdatum, undersokningsdatum));

        akt2.setUtforsVid(buildVardenhet(undersokning.getUtforsVid()));

        akt2.getBeskrivsAv().add(buildBeskrivsAv(undersokning));

        aktiviteter.add(akt2);

    }

    private Utforarroll buildBeskrivsAv(Undersokning undersokning) {
        Utforarroll utf = new Utforarroll();
        utf.setUtforartyp(CodeConverter.toKod(UtforarrollKod.valueOf(undersokning.getKomplikationstyrkt())));

        if (undersokning.getUtforsAv() != null) {
            utf.setAntasAv(convertHoSPersonal(undersokning.getUtforsAv().getAntasAv()));
        }

        return utf;
    }

    private Vardenhet buildVardenhet(se.inera.certificate.modules.rli.model.internal.wc.Vardenhet source) {
        if (source == null) {
            LOG.trace("Internal vardenhet was null");
            return null;
        }
        // TODO Add HSA-ID for Vardenhet to internal model (and other stuff we want to be able to re-create)
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setNamn(source.getEnhetsnamn());
        vardenhet.setEpost(source.getEpost());
        vardenhet.setId(new Id(HSA_ID.getCode(), source.getEnhetsid()));
        vardenhet.setPostadress(source.getPostadress());
        vardenhet.setPostnummer(source.getPostnummer());
        vardenhet.setPostort(source.getPostort());
        vardenhet.setTelefonnummer(source.getTelefonnummer());
        vardenhet.setVardgivare(convertVardgivare(source.getVardgivare()));
        return vardenhet;
    }

    HosPersonal convertHoSPersonal(HoSPersonal source) {
        HosPersonal hsp = new HosPersonal();
        hsp.setNamn(source.getFullstandigtNamn());
        hsp.setId(new Id(HSA_ID.getCodeSystem(), source.getPersonid()));
        hsp.setVardenhet(convertVardenhet(source.getVardenhet()));
        return hsp;
    }

    Vardenhet convertVardenhet(se.inera.certificate.modules.rli.model.internal.wc.Vardenhet source) {
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setEpost(source.getEpost());
        vardenhet.setId(new Id(HSA_ID.getCodeSystem(), source.getEnhetsid()));
        vardenhet.setNamn(source.getEnhetsnamn());
        vardenhet.setPostadress(source.getPostadress());
        vardenhet.setPostnummer(source.getPostnummer());
        vardenhet.setPostort(source.getPostort());
        vardenhet.setTelefonnummer(source.getTelefonnummer());
        vardenhet.setVardgivare(convertVardgivare(source.getVardgivare()));

        return vardenhet;
    }

    Vardgivare convertVardgivare(se.inera.certificate.modules.rli.model.internal.wc.Vardgivare source) {
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setId(new Id(HSA_ID.getCodeSystem(), source.getVardgivarid()));
        vardgivare.setNamn(source.getVardgivarnamn());
        return vardgivare;
    }

    Patient convertPatient(se.inera.certificate.modules.rli.model.internal.wc.Patient source) {
        Patient patient = new Patient();
        patient.setEfternamn(source.getEfternamn());
        patient.getFornamn().add(source.getFornamn());
        patient.setId(new Id(PERS_ID_ROOT, source.getPersonid()));
        patient.setPostadress(source.getPostadress());
        patient.setPostnummer(source.getPostnummer());
        patient.setPostort(source.getPostort());

        return patient;
    }

    Arrangemang convertArrangemang(se.inera.certificate.modules.rli.model.internal.wc.Arrangemang source) {
        Arrangemang arr = new Arrangemang();

        arr.setArrangemangstid(PartialConverter.toPartialInterval(source.getArrangemangsdatum(),
                source.getArrangemangslutdatum()));
        arr.setArrangemangstyp(CodeConverter.toKod(source.getArrangemangstyp()));
        arr.setAvbestallningsdatum(PartialConverter.stringToPartial(source.getAvbestallningsdatum()));
        arr.setBokningsdatum(PartialConverter.stringToPartial(source.getBokningsdatum()));
        arr.setBokningsreferens(source.getBokningsreferens());
        arr.setPlats(source.getPlats());

        return arr;
    }
}
