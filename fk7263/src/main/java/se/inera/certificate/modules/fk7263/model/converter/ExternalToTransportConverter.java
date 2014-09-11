package se.inera.certificate.modules.fk7263.model.converter;

import static se.inera.certificate.model.util.Iterables.addAll;
import static se.inera.certificate.modules.fk7263.model.converter.util.IsoTypeConverter.toHsaId;
import static se.inera.certificate.modules.fk7263.model.converter.util.IsoTypeConverter.toUtlatandeId;
import static se.inera.certificate.modules.fk7263.model.converter.util.IsoTypeConverter.toUtlatandeTyp;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;

import se.inera.certificate.fk7263.model.ext.v1.Observationssamband;
import se.inera.certificate.fk7263.model.v1.AktivitetType;
import se.inera.certificate.fk7263.model.v1.ArbetsuppgiftType;
import se.inera.certificate.fk7263.model.v1.DateInterval;
import se.inera.certificate.fk7263.model.v1.EnhetType;
import se.inera.certificate.fk7263.model.v1.HosPersonalType;
import se.inera.certificate.fk7263.model.v1.ObservationType;
import se.inera.certificate.fk7263.model.v1.PatientType;
import se.inera.certificate.fk7263.model.v1.ReferensType;
import se.inera.certificate.fk7263.model.v1.SysselsattningType;
import se.inera.certificate.fk7263.model.v1.Utlatande;
import se.inera.certificate.fk7263.model.v1.VardgivareType;
import se.inera.certificate.fk7263.model.v1.VardkontaktType;
import se.inera.certificate.model.Arbetsuppgift;
import se.inera.certificate.model.HosPersonal;
import se.inera.certificate.model.Kod;
import se.inera.certificate.model.PhysicalQuantity;
import se.inera.certificate.model.Sysselsattning;
import se.inera.certificate.model.Vardenhet;
import se.inera.certificate.model.Vardgivare;
import se.inera.certificate.model.Vardkontakt;
import se.inera.certificate.modules.fk7263.model.converter.util.IsoTypeConverter;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Aktivitet;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Observation;
import se.inera.certificate.modules.fk7263.model.external.Fk7263ObservationsSamband;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Patient;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Referens;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Utlatande;

public final class ExternalToTransportConverter {

    public Utlatande convert(Fk7263Utlatande source) {

        Utlatande utlatande = new Utlatande();

        utlatande.setUtlatandeId(toUtlatandeId(source.getId()));
        utlatande.setTypAvUtlatande(toUtlatandeTyp(source.getTyp()));

        addAll(utlatande.getKommentars(), source.getKommentarer());
        utlatande.setSigneringsdatum(source.getSigneringsdatum());
        utlatande.setSkickatdatum(source.getSkickatdatum());

        utlatande.setPatient(convert(source.getPatient()));
        utlatande.setSkapadAv(convert(source.getSkapadAv()));

        addAll(utlatande.getAktivitets(), convertAktiviteter(source.getAktiviteter()));
        addAll(utlatande.getObservations(), convertObservations(source.getObservationer()));
        addAll(utlatande.getObservationssambands(), convertObservationsSamband(source.getObservationssamband()));
        addAll(utlatande.getVardkontakts(), convertVardkontakter(source.getVardkontakter()));
        addAll(utlatande.getReferens(), convertReferenser(source.getReferenser()));

        return utlatande;
    }

    private List<Observationssamband> convertObservationsSamband(List<Fk7263ObservationsSamband> source) {
        List<Observationssamband> observationssambandList = new ArrayList<Observationssamband>();

        for (Fk7263ObservationsSamband os : source) {
            Observationssamband observationssamband = new Observationssamband();
            observationssamband.setObservationsid1(IsoTypeConverter.toRelationId(os.getObservationsidEtt()));
            observationssamband.setObservationsid2(IsoTypeConverter.toRelationId(os.getObservationsidTva()));
            observationssambandList.add(observationssamband);
        }
        return observationssambandList;
    }

    private List<ReferensType> convertReferenser(List<Fk7263Referens> source) {
        if (source == null) {
            return null;
        }
        List<ReferensType> referenser = new ArrayList<>();
        for (Fk7263Referens referens : source) {
            referenser.add(convert(referens));
        }
        return referenser;
    }

    private ReferensType convert(Fk7263Referens source) {
        if (source == null) {
            return null;
        }
        ReferensType referens = new ReferensType();
        referens.setReferenstyp(IsoTypeConverter.toReferensKod(source.getReferenstyp()));
        referens.setReferensdatum(source.getDatum());
        if (source.getBeskrivning() != null) {
            referens.setBeskrivning(source.getBeskrivning());
        }
        return referens;
    }

    private List<VardkontaktType> convertVardkontakter(List<Vardkontakt> source) {
        if (source == null) {
            return null;
        }
        List<VardkontaktType> vardkontakter = new ArrayList<>();
        for (Vardkontakt vardkontakt : source) {
            vardkontakter.add(convert(vardkontakt));
        }
        return vardkontakter;
    }

    private VardkontaktType convert(Vardkontakt source) {
        VardkontaktType vardkontakt = new VardkontaktType();
        vardkontakt.setVardkontakttyp(IsoTypeConverter.toVardkontaktKod(source.getVardkontakttyp()));

        if (source.getVardkontaktstid() != null) {
            DateInterval vardkontakttid = new DateInterval();
            vardkontakttid.setFrom(source.getVardkontaktstid().getFrom());
            vardkontakttid.setTom(source.getVardkontaktstid().getTom());
            vardkontakt.setVardkontakttid(vardkontakttid);
        }
        return vardkontakt;
    }

    private List<ObservationType> convertObservations(List<Fk7263Observation> source) {
        if (source == null) {
            return null;
        }
        List<ObservationType> observations = new ArrayList<>();
        for (Fk7263Observation observation : source) {
            observations.add(convert(observation));
        }
        return observations;
    }

    private ObservationType convert(Fk7263Observation source) {
        ObservationType observation = new ObservationType();
        observation.setObservationskategori(IsoTypeConverter.toObservationKategoriKod(source.getObservationskategori()));
        observation.setObservationskod(IsoTypeConverter.toObservationKod(source.getObservationskod()));

        if (source.getObservationsperiod() != null) {
            DateInterval interval = new DateInterval();
            interval.setFrom(new LocalDate(source.getObservationsperiod().getFrom()));
            interval.setTom(new LocalDate(source.getObservationsperiod().getTom()));
            observation.setObservationsperiod(interval);
        }

        addAll(observation.getVardes(), convertVarden(source.getVarde()));
        if (notNullOrEmpty(source.getBeskrivning())) {
            observation.setBeskrivning(source.getBeskrivning());
        }

        if (source.getId() != null) {
            observation.setObservationsId(IsoTypeConverter.toRelationId(source.getId()));
        }

        if (notNullOrEmpty(source.getKommentar())) {
            observation.setKommentar(source.getKommentar());
        }

        return observation;
    }

    private static boolean notNullOrEmpty(String string) {
        if (string == null) {
            return false;
        }
        return !string.isEmpty();
    }

    private List<Object> convertVarden(List<Object> source) {
        if (source == null) {
            return null;
        }
        List<Object> varden = new ArrayList<>();

        for (Object varde : source) {
            if (varde instanceof PhysicalQuantity) {
                varden.add(IsoTypeConverter.toPQ((PhysicalQuantity) varde));
            } else if (varde instanceof Kod) {
                varden.add(IsoTypeConverter.toCD((Kod) varde));
            }
        }
        return varden;
    }

    private List<AktivitetType> convertAktiviteter(List<Fk7263Aktivitet> source) {
        if (source == null) {
            return null;
        }
        List<AktivitetType> aktiviteter = new ArrayList<>();

        for (Fk7263Aktivitet aktivitet : source) {
            aktiviteter.add(convert(aktivitet));
        }
        return aktiviteter;
    }

    private AktivitetType convert(Fk7263Aktivitet source) {
        AktivitetType aktivitet = new AktivitetType();
        aktivitet.setAktivitetskod(IsoTypeConverter.toAktivitetKod(source.getAktivitetskod()));
        aktivitet.setBeskrivning(source.getBeskrivning());
        return aktivitet;
    }

    private HosPersonalType convert(HosPersonal source) {
        if (source == null) {
            return null;
        }
        HosPersonalType hosPersonal = new HosPersonalType();
        hosPersonal.setPersonalId(toHsaId(source.getId()));
        hosPersonal.setFullstandigtNamn(source.getNamn());
        hosPersonal.setForskrivarkod(source.getForskrivarkod());
        hosPersonal.setEnhet(convert(source.getVardenhet()));
        return hosPersonal;
    }

    private EnhetType convert(Vardenhet source) {
        if (source == null) {
            return null;
        }
        EnhetType enhet = new EnhetType();
        enhet.setEnhetsId(toHsaId(source.getId()));
        enhet.setArbetsplatskod(IsoTypeConverter.toArbetsplatsKod(source.getArbetsplatskod()));
        enhet.setEnhetsnamn(source.getNamn());
        enhet.setPostadress(source.getPostadress());
        enhet.setPostnummer(source.getPostnummer());
        enhet.setPostort(source.getPostort());
        enhet.setTelefonnummer(source.getTelefonnummer());
        enhet.setEpost(source.getEpost());
        enhet.setVardgivare(convert(source.getVardgivare()));
        return enhet;
    }

    private VardgivareType convert(Vardgivare source) {
        if (source == null) {
            return null;
        }
        VardgivareType vardgivare = new VardgivareType();
        vardgivare.setVardgivareId(toHsaId(source.getId()));
        vardgivare.setVardgivarnamn(source.getNamn());
        return vardgivare;
    }

    private PatientType convert(Fk7263Patient source) {
        if (source == null) {
            return null;
        }
        PatientType patient = new PatientType();
        patient.setPersonId(IsoTypeConverter.toPersonId(source.getId()));

        addAll(patient.getFornamns(), source.getFornamn());
        addAll(patient.getMellannamns(), source.getMellannamn());
        patient.setEfternamn(source.getEfternamn());
        addAll(patient.getSysselsattnings(), convertSysselsattnings(source.getSysselsattningar()));
        addAll(patient.getArbetsuppgifts(), convertArbetsuppgifts(source.getArbetsuppgifter()));
        return patient;
    }

    private List<ArbetsuppgiftType> convertArbetsuppgifts(List<Arbetsuppgift> source) {
        if (source == null) {
            return null;
        }
        List<ArbetsuppgiftType> arbetsuppgifts = new ArrayList<>();
        for (Arbetsuppgift arbetsuppgift : source) {
            arbetsuppgifts.add(convert(arbetsuppgift));
        }
        return arbetsuppgifts;
    }

    private ArbetsuppgiftType convert(Arbetsuppgift source) {
        if (source == null) {
            return null;
        }
        ArbetsuppgiftType arbetsuppgift = new ArbetsuppgiftType();
        arbetsuppgift.setTypAvArbetsuppgift(source.getTypAvArbetsuppgift());
        return arbetsuppgift;
    }

    private List<SysselsattningType> convertSysselsattnings(List<Sysselsattning> source) {
        if (source == null) {
            return null;
        }
        List<SysselsattningType> sysselsattnings = new ArrayList<>();
        for (Sysselsattning sysselsattning : source) {
            sysselsattnings.add(convert(sysselsattning));
        }
        return sysselsattnings;

    }

    private SysselsattningType convert(Sysselsattning source) {
        if (source == null) {
            return null;
        }
        SysselsattningType sysselsattning = new SysselsattningType();
        sysselsattning.setTypAvSysselsattning(IsoTypeConverter.toSysselsattningKod(source.getSysselsattningstyp()));
        return sysselsattning;
    }

}
