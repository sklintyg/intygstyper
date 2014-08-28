package se.inera.certificate.modules.fk7263.model.converter;

import static se.inera.certificate.modules.fk7263.model.converter.util.IsoTypeConverter.toId;
import static se.inera.certificate.modules.fk7263.model.converter.util.IsoTypeConverter.toKod;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.joda.time.Partial;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.certificate.fk7263.iso.v21090.dt.v1.CD;
import se.inera.certificate.fk7263.iso.v21090.dt.v1.PQ;
import se.inera.certificate.fk7263.model.v1.AktivitetType;
import se.inera.certificate.fk7263.model.v1.ArbetsuppgiftType;
import se.inera.certificate.fk7263.model.v1.EnhetType;
import se.inera.certificate.fk7263.model.v1.HosPersonalType;
import se.inera.certificate.fk7263.model.v1.ObservationType;
import se.inera.certificate.fk7263.model.v1.PatientType;
import se.inera.certificate.fk7263.model.v1.ReferensType;
import se.inera.certificate.fk7263.model.v1.SysselsattningType;
import se.inera.certificate.fk7263.model.v1.VardgivareType;
import se.inera.certificate.fk7263.model.v1.VardkontaktType;
import se.inera.certificate.model.Arbetsuppgift;
import se.inera.certificate.model.LocalDateInterval;
import se.inera.certificate.model.PartialInterval;
import se.inera.certificate.model.Sysselsattning;
import se.inera.certificate.model.Vardgivare;
import se.inera.certificate.model.Vardkontakt;
import se.inera.certificate.modules.fk7263.model.converter.util.IsoTypeConverter;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Aktivitet;
import se.inera.certificate.modules.fk7263.model.external.Fk7263HosPersonal;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Observation;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Patient;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Referens;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Utlatande;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Vardenhet;

/**
 * @author marced
 */
public final class TransportToExternalConverter {

    private static final Logger LOG = LoggerFactory.getLogger(TransportToExternalConverter.class);

    private TransportToExternalConverter() {
    }

    public static Fk7263Utlatande convert(se.inera.certificate.fk7263.model.v1.Utlatande source) throws ConverterException {
        Fk7263Utlatande fk7263utlatande = new Fk7263Utlatande();

        LOG.trace("Starting conversion from Transport to External");

        fk7263utlatande.setId(toId(source.getUtlatandeId()));
        fk7263utlatande.setTyp(toKod(source.getTypAvUtlatande()));
        fk7263utlatande.getKommentarer().addAll(source.getKommentars());
        fk7263utlatande.setSigneringsdatum(source.getSigneringsdatum());
        fk7263utlatande.setSkickatdatum(source.getSkickatdatum());

        fk7263utlatande.setPatient(convert(source.getPatient()));

        fk7263utlatande.setSkapadAv(convert(source.getSkapadAv()));

        fk7263utlatande.getObservationer().addAll(convertObservations(source.getObservations()));

        fk7263utlatande.getAktiviteter().addAll(convertAktiviteter(source.getAktivitets()));
        fk7263utlatande.getReferenser().addAll(convertReferenser(source.getReferens()));
        fk7263utlatande.getVardkontakter().addAll(convertVardkontakter(source.getVardkontakts()));

        return fk7263utlatande;
    }

    private static List<Sysselsattning> convert(List<SysselsattningType> source) {
        List<Sysselsattning> sysselsattnings = new ArrayList<>();
        for (SysselsattningType sysselsattning : source) {
            sysselsattnings.add(convert(sysselsattning));
        }
        return sysselsattnings;
    }

    private static Sysselsattning convert(SysselsattningType source) {
        Sysselsattning sysselsattning = new Sysselsattning();
        sysselsattning.setSysselsattningstyp(IsoTypeConverter.toKod(source.getTypAvSysselsattning()));
        return sysselsattning;
    }

    private static List<Fk7263Observation> convertObservations(List<ObservationType> source) throws ConverterException {
        if (source == null) {
            return null;
        }

        List<Fk7263Observation> observations = new ArrayList<>();
        for (ObservationType observationType : source) {
            Fk7263Observation observation = convert(observationType);
            if (observation != null) {
                observations.add(observation);
            }
        }
        return observations;
    }

    private static Fk7263Observation convert(ObservationType source) throws ConverterException {

        Fk7263Observation observation = new Fk7263Observation();

        observation.setObservationskategori(IsoTypeConverter.toKod(source.getObservationskategori()));
        observation.setObservationskod(IsoTypeConverter.toKod(source.getObservationskod()));

        if (source.getObservationsperiod() != null) {
            Partial from = source.getObservationsperiod().getFrom() == null ? null : new Partial(source.getObservationsperiod().getFrom());
            Partial tom = source.getObservationsperiod().getTom() == null ? null : new Partial(source.getObservationsperiod().getTom());
            PartialInterval observationsPeriod = new PartialInterval(from, tom);
            observation.setObservationsperiod(observationsPeriod);
        }

        // observation.getVarde().addAll(convertVarde(source.getVardes()));

        observation.setBeskrivning(source.getBeskrivning());
        if (source.getVardes() != null && !source.getVardes().isEmpty()) {
            try {
                for (Object varde : source.getVardes()) {
                    Object vardeObject = JAXBUtils.resolveAnyType(varde, CD.class);
                    if (vardeObject instanceof CD && ((CD) vardeObject).getCode() != null) {
                        observation.getVarde().add(IsoTypeConverter.toKod((CD) vardeObject));
                    } else {
                        vardeObject = JAXBUtils.resolveAnyType(varde, PQ.class);
                        if (vardeObject instanceof PQ) {
                            observation.getVarde().add(IsoTypeConverter.toPhysicalQuantity((PQ) vardeObject));
                        }
                    }
                }
            } catch (JAXBException e) {
                throw new ConverterException("Could not convert DOM element to object type", e);
            }
        }

        if (notNullOrEmpty(source.getKommentar())) {
            observation.setKommentar(source.getKommentar());
        }

        return observation;
    }

    private static boolean notNullOrEmpty(String kommentar) {
        if (kommentar == null) {
            return false;
        }
        return !kommentar.isEmpty();
    }

    private static List<Vardkontakt> convertVardkontakter(List<VardkontaktType> source) {
        if (source == null) {
            return null;
        }

        List<Vardkontakt> vardkontakter = new ArrayList<>();
        for (VardkontaktType vardkontakt : source) {
            vardkontakter.add(convert(vardkontakt));
        }
        return vardkontakter;
    }

    private static Vardkontakt convert(VardkontaktType source) {

        Vardkontakt vardkontakt = new Vardkontakt();
        vardkontakt.setVardkontakttyp(IsoTypeConverter.toKod(source.getVardkontakttyp()));
        if (source.getVardkontakttid() != null) {
            LocalDateInterval vardkontaktTid = new LocalDateInterval(source.getVardkontakttid().getFrom(), source
                    .getVardkontakttid().getTom());
            vardkontakt.setVardkontaktstid(vardkontaktTid);
        }
        return vardkontakt;
    }

    private static List<Fk7263Referens> convertReferenser(List<ReferensType> source) {
        if (source == null) {
            return null;
        }

        List<Fk7263Referens> referenser = new ArrayList<>();
        for (ReferensType referens : source) {
            referenser.add(convert(referens));
        }
        return referenser;
    }

    private static Fk7263Referens convert(ReferensType source) {
        Fk7263Referens referens = new Fk7263Referens();
        referens.setReferenstyp(IsoTypeConverter.toKod(source.getReferenstyp()));
        referens.setDatum(source.getReferensdatum());
        return referens;
    }

    private static List<Fk7263Aktivitet> convertAktiviteter(List<AktivitetType> source) {
        if (source == null) {
            return null;
        }

        List<Fk7263Aktivitet> aktiviteter = new ArrayList<>();
        for (AktivitetType aktivitet : source) {
            aktiviteter.add(convert(aktivitet));
        }
        return aktiviteter;
    }

    private static Fk7263Aktivitet convert(AktivitetType source) {
        Fk7263Aktivitet aktivitet = new Fk7263Aktivitet();
        aktivitet.setBeskrivning(source.getBeskrivning());
        aktivitet.setAktivitetskod(IsoTypeConverter.toKod(source.getAktivitetskod()));
        return aktivitet;
    }

    private static Fk7263HosPersonal convert(HosPersonalType source) {

        Fk7263HosPersonal hosPersonal = new Fk7263HosPersonal();
        hosPersonal.setId(IsoTypeConverter.toId(source.getPersonalId()));
        hosPersonal.setNamn(source.getFullstandigtNamn());
        hosPersonal.setForskrivarkod(source.getForskrivarkod());

        hosPersonal.setVardenhet(convert(source.getEnhet()));

        return hosPersonal;
    }

    private static Fk7263Patient convert(PatientType source) {

        Fk7263Patient patient = new Fk7263Patient();
        patient.setId(toId(source.getPersonId()));

        patient.getFornamn().addAll(source.getFornamns());
        patient.getMellannamn().addAll(source.getMellannamns());
        patient.setEfternamn(source.getEfternamn());

        patient.getSysselsattningar().addAll(convert(source.getSysselsattnings()));

        if (!source.getArbetsuppgifts().isEmpty()) {
            List<Arbetsuppgift> arbetsuppgifts = new ArrayList<>();
            for (ArbetsuppgiftType sourceArbetsuppgift : source.getArbetsuppgifts()) {
                Arbetsuppgift arbetsuppgift = new Arbetsuppgift();
                arbetsuppgift.setTypAvArbetsuppgift(sourceArbetsuppgift.getTypAvArbetsuppgift());
                arbetsuppgifts.add(arbetsuppgift);
            }
            patient.getArbetsuppgifter().addAll(arbetsuppgifts);
        }

        return patient;
    }

    private static Fk7263Vardenhet convert(EnhetType source) {

        Fk7263Vardenhet vardenhet = new Fk7263Vardenhet();
        vardenhet.setId(IsoTypeConverter.toId(source.getEnhetsId()));
        vardenhet.setNamn(source.getEnhetsnamn());
        vardenhet.setArbetsplatskod(toId(source.getArbetsplatskod()));
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
        vardgivare.setId(toId(source.getVardgivareId()));
        vardgivare.setNamn(source.getVardgivarnamn());
        return vardgivare;
    }
}
