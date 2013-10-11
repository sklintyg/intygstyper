package se.inera.certificate.modules.fk7263.model.converter;

import static se.inera.certificate.model.util.Iterables.addAll;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.inera.certificate.fk7263.model.v1.AktivitetType;
import se.inera.certificate.fk7263.model.v1.ArbetsuppgiftType;
import se.inera.certificate.fk7263.model.v1.DateInterval;
import se.inera.certificate.fk7263.model.v1.HosPersonalType;
import se.inera.certificate.fk7263.model.v1.ObservationType;
import se.inera.certificate.fk7263.model.v1.PartialDateInterval;
import se.inera.certificate.fk7263.model.v1.PatientType;
import se.inera.certificate.fk7263.model.v1.PrognosType;
import se.inera.certificate.fk7263.model.v1.ReferensType;
import se.inera.certificate.fk7263.model.v1.SysselsattningType;
import se.inera.certificate.fk7263.model.v1.Utlatande;
import se.inera.certificate.fk7263.model.v1.VardkontaktType;
import se.inera.certificate.model.Aktivitet;
import se.inera.certificate.model.Arbetsuppgift;
import se.inera.certificate.model.HosPersonal;
import se.inera.certificate.model.Observation;
import se.inera.certificate.model.PhysicalQuantity;
import se.inera.certificate.model.Prognos;
import se.inera.certificate.model.Referens;
import se.inera.certificate.model.Sysselsattning;
import se.inera.certificate.model.Vardenhet;
import se.inera.certificate.model.Vardgivare;
import se.inera.certificate.model.Vardkontakt;
import se.inera.certificate.modules.fk7263.model.converter.util.IsoTypeConverter;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Patient;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Utlatande;
import se.inera.ifv.insuranceprocess.healthreporting.v2.EnhetType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.VardgivareType;
import iso.v21090.dt.v1.PQ;

public final class ExternalToTransportConverter {

    private Fk7263Utlatande source;

    public ExternalToTransportConverter(Fk7263Utlatande source) {
        this.source = source;
    }

    public Utlatande convert() {
        Utlatande utlatande = new Utlatande();

        utlatande.setUtlatandeId(IsoTypeConverter.toII(source.getId()));
        utlatande.setTypAvUtlatande(IsoTypeConverter.toCD(source.getTyp()));

        addAll(utlatande.getKommentars(), source.getKommentarer());
        utlatande.setSigneringsdatum(source.getSigneringsdatum());
        utlatande.setSkickatdatum(source.getSkickatdatum());

        utlatande.setPatient(convert(source.getPatient()));
        utlatande.setSkapadAv(convert(source.getSkapadAv()));

        addAll(utlatande.getAktivitets(), convertAktiviteter(source.getAktiviteter()));
        addAll(utlatande.getObservations(), convertObservations(source.getObservationer()));
        addAll(utlatande.getVardkontakts(), convertVardkontakter(source.getVardkontakter()));
        addAll(utlatande.getReferens(), convertReferenser(source.getReferenser()));

        return utlatande;
    }

    private List<ReferensType> convertReferenser(List<Referens> source) {
        if (source == null)
            return null;

        List<ReferensType> referenser = new ArrayList<>();
        for (Referens referens : source) {
            referenser.add(convert(referens));
        }
        return referenser;
    }

    private ReferensType convert(Referens source) {
        if (source == null)
            return null;

        ReferensType referens = new ReferensType();
        referens.setReferenstyp(IsoTypeConverter.toCD(source.getReferenstyp()));
        referens.setReferensdatum(source.getDatum());
        return referens;
    }

    private List<VardkontaktType> convertVardkontakter(List<Vardkontakt> source) {
        if (source == null)
            return null;

        List<VardkontaktType> vardkontakter = new ArrayList<>();
        for (Vardkontakt vardkontakt : source) {
            vardkontakter.add(convert(vardkontakt));
        }
        return vardkontakter;
    }

    private VardkontaktType convert(Vardkontakt source) {
        VardkontaktType vardkontakt = new VardkontaktType();
        vardkontakt.setVardkontakttyp(IsoTypeConverter.toCD(source.getVardkontakttyp()));

        if (source.getVardkontaktstid() != null) {
            DateInterval vardkontakttid = new DateInterval();
            vardkontakttid.setFrom(source.getVardkontaktstid().getStart());
            vardkontakttid.setTom(source.getVardkontaktstid().getEnd());
            vardkontakt.setVardkontakttid(vardkontakttid);
        }
        return vardkontakt;
    }

    private List<ObservationType> convertObservations(List<Observation> source) {
        if (source == null)
            return null;

        List<ObservationType> observations = new ArrayList<>();
        for (Observation observation : source) {
            observations.add(convert(observation));
        }
        return observations;
    }

    private ObservationType convert(Observation source) {
        ObservationType observation = new ObservationType();
        observation.setObservationskategori(IsoTypeConverter.toCD(source.getObservationskategori()));
        observation.setObservationskod(IsoTypeConverter.toCD(source.getObservationskod()));

        if (source.getObservationsperiod() != null) {
            PartialDateInterval interval = new PartialDateInterval();
            interval.setFrom(source.getObservationsperiod().getFrom());
            interval.setTom(source.getObservationsperiod().getTom());
            observation.setObservationsperiod(interval);
        }

        addAll(observation.getVardes(), convertVarden(source.getVarde()));

        observation.setBeskrivning(source.getBeskrivning());
        observation.getPrognos().addAll(convertPrognoser(source.getPrognoser()));

        return observation;
    }

    private List<PQ> convertVarden(List<PhysicalQuantity> source) {
        if (source == null)
            return null;

        List<PQ> varden = new ArrayList<>();

        for (PhysicalQuantity varde : source) {
            varden.add(convert(varde));
        }
        return varden;
    }

    private PQ convert(PhysicalQuantity source) {
        PQ pq = new PQ();
        pq.setUnit(source.getUnit());
        pq.setValue(source.getQuantity());
        return pq;
    }

    private Collection<PrognosType> convertPrognoser(Collection<Prognos> source) {
        List<PrognosType> prognosTypes = new ArrayList<>();
        for (Prognos prognos : source) {
            prognosTypes.add(convert(prognos));
        }
        return prognosTypes;
    }

    private PrognosType convert(Prognos source) {
        if (source == null)
            return null;

        PrognosType prognos = new PrognosType();
        prognos.setPrognoskod(IsoTypeConverter.toCD(source.getPrognoskod()));
        prognos.setBeskrivning(source.getBeskrivning());
        return prognos;
    }

    private List<AktivitetType> convertAktiviteter(List<Aktivitet> source) {
        if (source == null)
            return null;

        List<AktivitetType> aktiviteter = new ArrayList<>();

        for (Aktivitet aktivitet : source) {
            aktiviteter.add(convert(aktivitet));
        }
        return aktiviteter;
    }

    private AktivitetType convert(Aktivitet source) {
        AktivitetType aktivitet = new AktivitetType();
        aktivitet.setAktivitetskod(IsoTypeConverter.toCD(source.getAktivitetskod()));
        aktivitet.setBeskrivning(source.getBeskrivning());
        return aktivitet;
    }

    private HosPersonalType convert(HosPersonal source) {
        if (source == null)
            return null;

        HosPersonalType hosPersonal = new HosPersonalType();
        hosPersonal.setPersonalId(IsoTypeConverter.toII(source.getId()));
        hosPersonal.setFullstandigtNamn(source.getNamn());
        hosPersonal.setForskrivarkod(source.getForskrivarkod());
        hosPersonal.setEnhet(convert(source.getVardenhet()));
        return hosPersonal;
    }

    private EnhetType convert(Vardenhet source) {
        if (source == null)
            return null;

        EnhetType enhet = new EnhetType();
        enhet.setEnhetsId(IsoTypeConverter.toII(source.getId()));
        enhet.setArbetsplatskod(IsoTypeConverter.toII(source.getArbetsplatskod()));
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
        if (source == null)
            return null;

        VardgivareType vardgivare = new VardgivareType();
        vardgivare.setVardgivareId(IsoTypeConverter.toII(source.getId()));
        vardgivare.setVardgivarnamn(source.getNamn());
        return vardgivare;
    }

    private PatientType convert(Fk7263Patient source) {
        if (source == null)
            return null;

        PatientType patient = new PatientType();
        patient.setPersonId(IsoTypeConverter.toII(source.getId()));

        addAll(patient.getFornamns(), source.getFornamn());
        addAll(patient.getMellannamns(), source.getMellannamn());
        patient.setEfternamn(source.getEfternamn());
        addAll(patient.getSysselsattnings(), convertSysselsattnings(source.getSysselsattningar()));
        addAll(patient.getArbetsuppgifts(), convertArbetsuppgifts(source.getArbetsuppgifter()));
        return patient;
    }

    private List<ArbetsuppgiftType> convertArbetsuppgifts(List<Arbetsuppgift> source) {
        if (source == null)
            return null;

        List<ArbetsuppgiftType> arbetsuppgifts = new ArrayList<>();
        for (Arbetsuppgift arbetsuppgift : source) {
            arbetsuppgifts.add(convert(arbetsuppgift));
        }
        return arbetsuppgifts;
    }

    private ArbetsuppgiftType convert(Arbetsuppgift source) {
        if (source == null)
            return null;

        ArbetsuppgiftType arbetsuppgift = new ArbetsuppgiftType();
        arbetsuppgift.setTypAvArbetsuppgift(source.getTypAvArbetsuppgift());
        return arbetsuppgift;
    }

    private List<SysselsattningType> convertSysselsattnings(List<Sysselsattning> source) {
        if (source == null)
            return null;

        List<SysselsattningType> sysselsattnings = new ArrayList<>();
        for (Sysselsattning sysselsattning : source) {
            sysselsattnings.add(convert(sysselsattning));
        }
        return sysselsattnings;

    }

    private SysselsattningType convert(Sysselsattning source) {
        if (source == null)
            return null;

        SysselsattningType sysselsattning = new SysselsattningType();
        sysselsattning.setTypAvSysselsattning(IsoTypeConverter.toCD(source.getSysselsattningstyp()));
        return sysselsattning;
    }

}
