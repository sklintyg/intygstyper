package se.inera.certificate.modules.fk7263.model.converter;

import iso.v21090.dt.v1.PQ;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.inera.certificate.common.v1.AktivitetType;
import se.inera.certificate.common.v1.ArbetsuppgiftType;
import se.inera.certificate.common.v1.ObservationType;
import se.inera.certificate.common.v1.PatientType;
import se.inera.certificate.common.v1.PrognosType;
import se.inera.certificate.common.v1.ReferensType;
import se.inera.certificate.common.v1.SysselsattningType;
import se.inera.certificate.common.v1.VardkontaktType;
import se.inera.certificate.integration.converter.util.IsoTypeConverter;
import se.inera.certificate.model.Aktivitet;
import se.inera.certificate.model.Arbetsuppgift;
import se.inera.certificate.model.HosPersonal;
import se.inera.certificate.model.LocalDateInterval;
import se.inera.certificate.model.Observation;
import se.inera.certificate.model.PartialInterval;
import se.inera.certificate.model.Patient;
import se.inera.certificate.model.PhysicalQuantity;
import se.inera.certificate.model.Prognos;
import se.inera.certificate.model.Referens;
import se.inera.certificate.model.Sysselsattning;
import se.inera.certificate.model.Vardenhet;
import se.inera.certificate.model.Vardgivare;
import se.inera.certificate.model.Vardkontakt;
import se.inera.certificate.modules.fk7263.model.Fk7263Intyg;
import se.inera.ifv.insuranceprocess.healthreporting.v2.EnhetType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.HosPersonalType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.VardgivareType;

import java.util.ArrayList;
import java.util.List;


/**
 * @author marced
 *
 */
public final class UtlatandeJaxbToFk7263IntygConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(UtlatandeJaxbToFk7263IntygConverter.class);

      private UtlatandeJaxbToFk7263IntygConverter() {
    }

    /**
     * Converts a JAXB {@link se.inera.certificate.common.v1.Utlatande} to a {@link se.inera.certificate.modules.fk7263.model.Fk7263Intyg}.
     */
    public static Fk7263Intyg convert(se.inera.certificate.common.v1.Utlatande source) {
        Fk7263Intyg fk7263Intyg = new Fk7263Intyg();

        fk7263Intyg.setId(IsoTypeConverter.toId(source.getUtlatandeId()));
        fk7263Intyg.setTyp(IsoTypeConverter.toKod(source.getTypAvUtlatande()));
        fk7263Intyg.setKommentars(source.getKommentars());
        fk7263Intyg.setSigneringsDatum(source.getSigneringsdatum());
        fk7263Intyg.setSkickatDatum(source.getSkickatdatum());

        fk7263Intyg.setPatient(convert(source.getPatient()));

        fk7263Intyg.setSkapadAv(convert(source.getSkapadAv()));

        fk7263Intyg.setObservations(convertObservations(source.getObservations()));

        fk7263Intyg.setAktiviteter(convertAktiviteter(source.getAktivitets()));
        fk7263Intyg.setReferenser(convertReferenser(source.getReferens()));
        fk7263Intyg.setVardkontakter(convertVardkontakter(source.getVardkontakts()));

        return fk7263Intyg;
    }


    private static List<Observation> convertObservations(List<ObservationType> source) {
        if (source == null) return null;
        
        List<Observation> observations = new ArrayList<>();
        for (ObservationType observationType : source) {
            Observation observation = convert(observationType);
            if (observation != null) {
                observations.add(observation);
            }
        }
        return observations;
    }

    private static Observation convert(ObservationType source) {
        
        Observation observation = new Observation();

        observation.setObservationsKategori(IsoTypeConverter.toKod(source.getObservationskategori()));
        observation.setObservationsKod(IsoTypeConverter.toKod(source.getObservationskod()));

        if (source.getObservationsperiod() != null) {
            PartialInterval observationsPeriod = new PartialInterval(source.getObservationsperiod().getFrom(), source.getObservationsperiod().getTom());
            observation.setObservationsPeriod(observationsPeriod);
        }


        observation.setVarde(convertVarde(source.getVardes()));

        observation.setPrognos(convert(source.getPrognos()));
        observation.setBeskrivning(source.getBeskrivning());

        return observation;
    }

    private static List<PhysicalQuantity> convertVarde(List<PQ> source) {
        
        List<PhysicalQuantity> vardes = new ArrayList<>();
        for (PQ varde : source) {
            vardes.add(new PhysicalQuantity(varde.getValue(), varde.getUnit()));
        }
        return vardes;
    }

    private static Prognos convert(PrognosType source) {
        if(source == null) return null;

        Prognos prognos = new Prognos();
        prognos.setPrognosKod(IsoTypeConverter.toKod(source.getPrognoskod()));
        prognos.setBeskrivning(source.getBeskrivning());
        return prognos;
    }

    private static List<Vardkontakt> convertVardkontakter(List<VardkontaktType> source) {
        if (source == null) return null;
        
        List<Vardkontakt> vardkontakter = new ArrayList<>();
        for (VardkontaktType vardkontakt : source) {
            vardkontakter.add(convert(vardkontakt));
        }
        return vardkontakter;
    }

    private static Vardkontakt convert(VardkontaktType source) {
        
        Vardkontakt vardkontakt = new Vardkontakt();
        vardkontakt.setVardkontakttyp(IsoTypeConverter.toKod(source.getVardkontakttyp()));

        LocalDateInterval vardkontaktTid = new LocalDateInterval(source.getVardkontakttid().getFrom(), source.getVardkontakttid().getTom());
        vardkontakt.setVardkontaktstid(vardkontaktTid);

        return vardkontakt;
    }

    private static List<Referens> convertReferenser(List<ReferensType> source) {
        if (source == null) return null;
        
        List<Referens> referenser = new ArrayList<>();
        for (ReferensType referens : source) {
            referenser.add(convert(referens));
        }
        return referenser;
    }

    private static Referens convert(ReferensType source) {
        Referens referens = new Referens();
        referens.setReferenstyp(IsoTypeConverter.toKod(source.getReferenstyp()));
        referens.setDatum(source.getReferensdatum());
        return referens;
    }

    private static List<Aktivitet> convertAktiviteter(List<AktivitetType> source) {
        if (source == null) return null;
        
        List<Aktivitet> aktiviteter = new ArrayList<>();
        for (AktivitetType aktivitet : source) {
            aktiviteter.add(convert(aktivitet));
        }
        return aktiviteter;
    }

    private static Aktivitet convert(AktivitetType source) {
        Aktivitet aktivitet = new Aktivitet();
        aktivitet.setBeskrivning(source.getBeskrivning());
        aktivitet.setAktivitetskod(IsoTypeConverter.toKod(source.getAktivitetskod()));
        return aktivitet;
    }

    private static HosPersonal convert(HosPersonalType source) {

        HosPersonal hosPersonal = new HosPersonal();
        hosPersonal.setId(IsoTypeConverter.toId(source.getPersonalId()));
        hosPersonal.setNamn(source.getFullstandigtNamn());
        hosPersonal.setForskrivarkod(source.getForskrivarkod());

        hosPersonal.setVardenhet(convert(source.getEnhet()));

        return hosPersonal;
    }

    private static Patient convert(PatientType source) {

        Patient patient = new Patient();
        patient.setId(IsoTypeConverter.toId(source.getPersonId()));

        if (!source.getFornamns().isEmpty()) {
            patient.setFornamns(source.getFornamns());
        }
        if (!source.getEfternamns().isEmpty()) {
            patient.setEfternamns(source.getEfternamns());
        }
        if (!source.getMellannamns().isEmpty()) {
            patient.setMellannamns(source.getMellannamns());
        }

        patient.setSysselsattnings(convert(source.getSysselsattnings()));

        if (!source.getArbetsuppgifts().isEmpty()) {
            List<Arbetsuppgift> arbetsuppgifts = new ArrayList<>();
            for (ArbetsuppgiftType sourceArbetsuppgift : source.getArbetsuppgifts()) {
                Arbetsuppgift arbetsuppgift = new Arbetsuppgift();
                arbetsuppgift.setTypAvArbetsuppgift(sourceArbetsuppgift.getTypAvArbetsuppgift());
                arbetsuppgifts.add(arbetsuppgift);
            }
            patient.setArbetsuppgifts(arbetsuppgifts);
        }

        return patient;
    }

    private static List<Sysselsattning> convert(List<SysselsattningType> source) {
        if (source == null) return null;
        
        List<Sysselsattning> sysselsattnings = new ArrayList<>();
        for (SysselsattningType sysselsattning : source) {
            sysselsattnings.add(convert(sysselsattning));
        }
        return sysselsattnings;
    }

    private static Sysselsattning convert(SysselsattningType source) {
        Sysselsattning sysselsattning = new Sysselsattning();
        sysselsattning.setSysselsattningsTyp(IsoTypeConverter.toKod(source.getTypAvSysselsattning()));
        sysselsattning.setDatum(source.getDatum());
        return sysselsattning;
    }

    private static Vardenhet convert(EnhetType source) {

        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setId(IsoTypeConverter.toId(source.getEnhetsId()));
        vardenhet.setNamn(source.getEnhetsnamn());
        vardenhet.setArbetsplatskod(IsoTypeConverter.toId(source.getArbetsplatskod()));
        vardenhet.setPostadress(source.getPostadress());
        vardenhet.setPostnummer(source.getPostnummer());
        vardenhet.setPostort(source.getPostort());
        vardenhet.setEpost(source.getEpost());
        vardenhet.setTelefonnummer(source.getTelefonnummer());
        vardenhet.setVardgivare(convert(source.getVardgivare()));
        return vardenhet;
    }

    private static Vardgivare convert(VardgivareType source) {

        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setId(IsoTypeConverter.toId(source.getVardgivareId()));
        vardgivare.setNamn(source.getVardgivarnamn());
        return vardgivare;
    }
}
