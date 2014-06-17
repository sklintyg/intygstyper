package se.inera.certificate.modules.fk7263.model.converter;

import static se.inera.certificate.model.util.Iterables.addAll;
import static se.inera.certificate.modules.fk7263.model.converter.util.IsoTypeConverter.toCD;
import static se.inera.certificate.modules.fk7263.model.converter.util.IsoTypeConverter.toII;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;

import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.AktivitetType;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.Aktivitetskod;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.ArbetsformagaNedsattningType;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.ArbetsformagaType;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.ArbetsuppgiftType;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.BedomtTillstandType;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.FunktionstillstandType;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.Lakarutlatande;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.MedicinsktTillstandType;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.Nedsattningsgrad;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.Prognosangivelse;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.ReferensType;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.Referenstyp;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.SysselsattningType;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.TypAvFunktionstillstand;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.TypAvSysselsattning;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.VardkontaktType;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.mu7263.v3.Vardkontakttyp;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.registermedicalcertificate.v3.RegisterMedicalCertificate;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.v2.EnhetType;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.v2.HosPersonalType;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.v2.PatientType;
import se.inera.certificate.fk7263.insuranceprocess.healthreporting.v2.VardgivareType;
import se.inera.certificate.model.HosPersonal;
import se.inera.certificate.model.Kod;
import se.inera.certificate.model.Referens;
import se.inera.certificate.model.Sysselsattning;
import se.inera.certificate.model.Vardenhet;
import se.inera.certificate.model.Vardgivare;
import se.inera.certificate.model.Vardkontakt;
import se.inera.certificate.model.util.Strings;
import se.inera.certificate.modules.fk7263.model.codes.Aktivitetskoder;
import se.inera.certificate.modules.fk7263.model.codes.ObservationsKoder;
import se.inera.certificate.modules.fk7263.model.codes.Prognoskoder;
import se.inera.certificate.modules.fk7263.model.codes.Referenstypkoder;
import se.inera.certificate.modules.fk7263.model.codes.Sysselsattningskoder;
import se.inera.certificate.modules.fk7263.model.codes.Vardkontakttypkoder;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Aktivitet;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Observation;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Patient;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Prognos;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Utlatande;

public final class ExternalToTransportFk7263LegacyConverter {

    private static final String FK7263 = "Läkarintyg enligt 3 kap, 8 § lagen (1962:381) om allmän försäkring";
    public static final int FORMOGA_3_4 = 75;
    public static final int FORMOGA_1_2 = 50;
    public static final int FORMOGA_1_4 = 25;

    private ExternalToTransportFk7263LegacyConverter() {
    }

    public static RegisterMedicalCertificate getJaxbObject(Fk7263Utlatande utlatande) {
        RegisterMedicalCertificate register = new RegisterMedicalCertificate();
        register.setLakarutlatande(new Lakarutlatande());
        if (utlatande.getId() != null) {
            register.getLakarutlatande().setLakarutlatandeId(
                    toII(utlatande.getId()).getExtension() == null ? utlatande.getId().getRoot() : utlatande.getId().getExtension());
        }
        register.getLakarutlatande().setTypAvUtlatande(FK7263);

        if (utlatande.getKommentarer() != null && !utlatande.getKommentarer().isEmpty()) {
            register.getLakarutlatande().setKommentar(utlatande.getKommentarer().get(0));
        }

        register.getLakarutlatande().setSigneringsdatum(utlatande.getSigneringsdatum());
        register.getLakarutlatande().setSkickatDatum(utlatande.getSkickatdatum());
        register.getLakarutlatande().setPatient(toJaxb(utlatande.getPatient()));
        register.getLakarutlatande().setSkapadAvHosPersonal(toJaxb(utlatande.getSkapadAv()));

        Fk7263Observation sjukdomsforlopp = utlatande.findObservationByKod(ObservationsKoder.FORLOPP);
        if (sjukdomsforlopp != null) {
            register.getLakarutlatande().setBedomtTillstand(sjukdomsforloppToJaxb(sjukdomsforlopp.getBeskrivning()));
        }

        Fk7263Observation diagnos = utlatande.findObservationByKategori(ObservationsKoder.DIAGNOS);
        if (diagnos != null) {
            register.getLakarutlatande().setMedicinsktTillstand(toMedicinsktTillstand(diagnos));
        }

        // Add collections with wrapped nullchecks method
        addAll(register.getLakarutlatande().getAktivitets(), convert(utlatande.getAktiviteter()));
        addAll(register.getLakarutlatande().getReferens(), convertReferenser(utlatande.getReferenser()));
        addAll(register.getLakarutlatande().getVardkontakts(), convertVardkontakter(utlatande.getVardkontakter()));

        Fk7263Observation kroppsfunktion = utlatande.findObservationByKategori(ObservationsKoder.KROPPSFUNKTIONER);
        if (kroppsfunktion != null) {
            register.getLakarutlatande().getFunktionstillstands()
                    .add(toFunktionstillstand(kroppsfunktion, TypAvFunktionstillstand.KROPPSFUNKTION));
        }

        Fk7263Observation aktivitet = utlatande.findObservationByKategori(ObservationsKoder.AKTIVITETER_OCH_DELAKTIGHET);
        if (aktivitet != null) {

            // add arbetsformaga to aktivitetsbegransing
            FunktionstillstandType aktivitetsbegransing = toFunktionstillstand(aktivitet,
                    TypAvFunktionstillstand.AKTIVITET);
            aktivitetsbegransing.setArbetsformaga(toArbetsformaga(utlatande));

            register.getLakarutlatande().getFunktionstillstands().add(aktivitetsbegransing);

        } else if (utlatande.getAktivitet(Aktivitetskoder.AVSTANGNING_ENLIGT_SML_PGA_SMITTA) != null) {
            // If no ObservationsKoder.AKTIVITETER_OCH_DELAKTIGHET is found,
            // create FunktionstillstandType and populate it with Arbetsformaga to not loose information
            FunktionstillstandType aktivitetsbegransing = new FunktionstillstandType();
            aktivitetsbegransing.setTypAvFunktionstillstand(TypAvFunktionstillstand.AKTIVITET);
            aktivitetsbegransing.setArbetsformaga(toArbetsformaga(utlatande));
            register.getLakarutlatande().getFunktionstillstands().add(aktivitetsbegransing);
        }
        return register;
    }

    private static FunktionstillstandType toFunktionstillstand(Fk7263Observation observation,
            TypAvFunktionstillstand typAvFunktionstillstand) {
        FunktionstillstandType funktionstillstandType = new FunktionstillstandType();
        funktionstillstandType.setTypAvFunktionstillstand(typAvFunktionstillstand);
        funktionstillstandType.setBeskrivning(EmptyStringUtil.unescapeEmptyString(observation.getBeskrivning()));
        return funktionstillstandType;
    }

    private static ArbetsformagaType toArbetsformaga(Fk7263Utlatande utlatande) {

        ArbetsformagaType arbetsformagaType = new ArbetsformagaType();

        List<Fk7263Observation> arbetsformagas = utlatande.getObservationsByKod(ObservationsKoder.ARBETSFORMAGA);

        if (!arbetsformagas.isEmpty()) {
            Fk7263Observation firstObservation = arbetsformagas.get(0);
            if (firstObservation.getPrognoser() != null && !firstObservation.getPrognoser().isEmpty()) {
                Fk7263Prognos prognos = firstObservation.getPrognoser().get(0);

                String beskrivning = prognos.getBeskrivning();
                arbetsformagaType.setMotivering(beskrivning);

                Kod prognosKod = prognos.getPrognoskod();
                if (prognosKod != null) {
                    String fk7263String = Prognoskoder.MAP_TO_FK7263.get(prognosKod);
                    arbetsformagaType.setPrognosangivelse(Prognosangivelse.fromValue(fk7263String));
                }
            }
        }

        if (utlatande.getPatient() != null) {

            Fk7263Patient patient = utlatande.getPatient();

            // attach arbetsuppgift if available
            if (patient.getArbetsuppgifter() != null && !patient.getArbetsuppgifter().isEmpty()) {
                ArbetsuppgiftType arbetsuppgift = new ArbetsuppgiftType();
                arbetsuppgift.setTypAvArbetsuppgift(patient.getArbetsuppgifter().get(0).getTypAvArbetsuppgift());
                arbetsformagaType.setArbetsuppgift(arbetsuppgift);
            }

            if (patient.getSysselsattningar() != null) {
                arbetsformagaType.getSysselsattnings().addAll(convertSysselsattnings(patient.getSysselsattningar()));
            }
        }

        for (Fk7263Observation arbetsformaga : arbetsformagas) {
            ArbetsformagaNedsattningType nedsattningType = new ArbetsformagaNedsattningType();

            if (arbetsformaga.getVarde() != null && !arbetsformaga.getVarde().isEmpty()) {
                switch (arbetsformaga.getVarde().get(0).getQuantity().intValue()) {
                case FORMOGA_3_4: // 75% Arbetsformaga
                    nedsattningType.setNedsattningsgrad(Nedsattningsgrad.NEDSATT_MED_1_4);
                    break;
                case FORMOGA_1_2: // 50% Arbetsformaga
                    nedsattningType.setNedsattningsgrad(Nedsattningsgrad.NEDSATT_MED_1_2);
                    break;
                case FORMOGA_1_4: // 25% Arbetsformaga
                    nedsattningType.setNedsattningsgrad(Nedsattningsgrad.NEDSATT_MED_3_4);
                    break;
                case 0: // 0% Arbetsformaga
                    nedsattningType.setNedsattningsgrad(Nedsattningsgrad.HELT_NEDSATT);
                    break;
                default:
                    throw new IllegalStateException("Wrong nedsättningsgrad "
                            + arbetsformaga.getVarde().get(0).getQuantity());
                }
            }

            if (arbetsformaga.getObservationsperiod() != null) {
                nedsattningType.setVaraktighetFrom(new LocalDate(arbetsformaga.getObservationsperiod().getFrom()));
                nedsattningType.setVaraktighetTom(new LocalDate(arbetsformaga.getObservationsperiod().getTom()));
            }

            arbetsformagaType.getArbetsformagaNedsattnings().add(nedsattningType);
        }
        return arbetsformagaType;
    }

    private static List<SysselsattningType> convertSysselsattnings(List<Sysselsattning> source) {
        List<SysselsattningType> sysselsattningTypes = new ArrayList<>();
        for (Sysselsattning sysselsattning : source) {
            SysselsattningType sysselsattningType = convert(sysselsattning);
            if (sysselsattningType != null) {
                sysselsattningTypes.add(sysselsattningType);
            }
        }
        return sysselsattningTypes;
    }

    private static SysselsattningType convert(Sysselsattning source) {

        if (source == null || source.getSysselsattningstyp() == null) {
            return null;
        }

        SysselsattningType sysselsattningTyp = null;
        try {
            Kod sysselsattning = source.getSysselsattningstyp();
            String fk7263String = Sysselsattningskoder.MAP_TO_FK7263.get(sysselsattning);
            if (fk7263String != null) {
                sysselsattningTyp = new SysselsattningType();
                sysselsattningTyp.setTypAvSysselsattning(TypAvSysselsattning.fromValue(fk7263String));
            }
        } catch (IllegalArgumentException ex) {
            // unknown typAvSysselsattning that is not relevant in the Fk7263 context
            return null;
        }

        return sysselsattningTyp;
    }

    private static List<VardkontaktType> convertVardkontakter(List<Vardkontakt> source) {
        if (source == null) {
            return null;
        }

        List<VardkontaktType> vardkontaktTypes = new ArrayList<>();
        for (Vardkontakt vardkontakt : source) {
            VardkontaktType vardkontaktType = toVardkontakt(vardkontakt);
            if (vardkontaktType != null) {
                vardkontaktTypes.add(vardkontaktType);
            }
        }
        return vardkontaktTypes;
    }

    private static VardkontaktType toVardkontakt(Vardkontakt source) {
        if (source == null || source.getVardkontakttyp() == null) {
            return null;
        }

        VardkontaktType vardkontaktType = null;

        try {
            Kod vardkontakttyp = source.getVardkontakttyp();
            String fk7263String = Vardkontakttypkoder.MAP_TO_FK7263.get(vardkontakttyp);
            if (fk7263String != null) {
                vardkontaktType = new VardkontaktType();
                vardkontaktType.setVardkontaktstid(source.getVardkontaktstid().getFrom());
                vardkontaktType.setVardkontakttyp(Vardkontakttyp.fromValue(fk7263String));
            }
        } catch (IllegalArgumentException ex) {
            // unknown vardkontakttyp that is not relevant in the Fk7263 context
            return null;
        }

        return vardkontaktType;
    }

    private static List<ReferensType> convertReferenser(List<Referens> source) {
        if (source == null) {
            return null;
        }

        List<ReferensType> referensTypes = new ArrayList<>();
        for (Referens referens : source) {
            ReferensType referensType = toReferens(referens);
            if (referensType != null) {
                referensTypes.add(referensType);
            }
        }
        return referensTypes;
    }

    private static ReferensType toReferens(Referens source) {
        if (source == null || source.getReferenstyp() == null) {
            return null;
        }

        ReferensType referensType = null;

        try {
            Kod referenstyp = source.getReferenstyp();
            String fk7363String = Referenstypkoder.MAP_TO_FK7263.get(referenstyp);
            if (fk7363String != null) {
                referensType = new ReferensType();
                referensType.setDatum(source.getDatum());
                referensType.setReferenstyp(Referenstyp.fromValue(fk7363String));
            }
        } catch (IllegalArgumentException ex) {
            // unknown referenstyp that is not relevant in the Fk7263 context
            return null;
        }

        return referensType;
    }

    private static List<AktivitetType> convert(List<Fk7263Aktivitet> source) {
        if (source == null) {
            return null;
        }

        List<AktivitetType> aktivitets = new ArrayList<>();
        for (Fk7263Aktivitet aktivitet : source) {
            AktivitetType aktivitetType = convert(aktivitet);
            if (aktivitetType != null) {
                aktivitets.add(aktivitetType);
            }
        }
        return aktivitets;
    }

    private static AktivitetType convert(Fk7263Aktivitet source) {
        if (source == null || source.getAktivitetskod() == null) {
            return null;
        }

        AktivitetType aktivitet = null;
        try {
            Kod aktivitetskod = source.getAktivitetskod();
            String asfk7263String = Aktivitetskoder.MAP_TO_FK_7263.get(aktivitetskod);
            if (asfk7263String != null) {
                aktivitet = new AktivitetType();
                aktivitet.setBeskrivning(source.getBeskrivning());
                aktivitet.setAktivitetskod(Aktivitetskod.fromValue(asfk7263String));
            }
        } catch (IllegalArgumentException ex) {
            // unknown aktivitetkod that is not relevant in the Fk7263 context
            return null;
        }

        return aktivitet;
    }

    private static MedicinsktTillstandType toMedicinsktTillstand(Fk7263Observation diagnos) {
        MedicinsktTillstandType tillstand = new MedicinsktTillstandType();
        tillstand.setBeskrivning(diagnos.getBeskrivning());
        tillstand.setTillstandskod(toCD(diagnos.getObservationskod()));
        return tillstand;
    }

    private static BedomtTillstandType sjukdomsforloppToJaxb(String source) {
        BedomtTillstandType tillstand = new BedomtTillstandType();
        tillstand.setBeskrivning(source);
        return tillstand;
    }

    private static EnhetType toJaxb(Vardenhet source) {
        if (source == null) {
            return null;
        }

        EnhetType enhet = new EnhetType();
        enhet.setEnhetsnamn(source.getNamn());
        enhet.setPostadress(source.getPostadress());
        enhet.setPostnummer(source.getPostnummer());
        enhet.setPostort(source.getPostort());
        enhet.setTelefonnummer(source.getTelefonnummer());
        enhet.setEpost(source.getEpost());

        enhet.setArbetsplatskod(toII(source.getArbetsplatskod()));
        enhet.setEnhetsId(toII(source.getId()));

        enhet.setVardgivare(toJaxb(source.getVardgivare()));
        return enhet;
    }

    private static VardgivareType toJaxb(Vardgivare source) {
        if (source == null) {
            return null;
        }

        VardgivareType vardgivare = new VardgivareType();
        vardgivare.setVardgivarnamn(source.getNamn());
        vardgivare.setVardgivareId(toII(source.getId()));
        return vardgivare;
    }

    private static HosPersonalType toJaxb(HosPersonal skapadAv) {
        HosPersonalType personal = new HosPersonalType();
        personal.setForskrivarkod(skapadAv.getForskrivarkod());
        personal.setFullstandigtNamn(skapadAv.getNamn());
        personal.setPersonalId(toII(skapadAv.getId()));

        personal.setEnhet(toJaxb(skapadAv.getVardenhet()));

        return personal;
    }

    private static PatientType toJaxb(Fk7263Patient source) {
        PatientType patient = new PatientType();
        patient.setFullstandigtNamn(extractFullstandigtNamn(source));
        patient.setPersonId(toII(source.getId()));
        return patient;
    }

    private static String extractFullstandigtNamn(Fk7263Patient source) {
        List<String> names = new ArrayList<>();

        if (source.getFornamn() != null) {
            names.addAll(source.getFornamn());
        }

        if (source.getMellannamn() != null) {
            names.addAll(source.getMellannamn());
        }

        names.add(source.getEfternamn());

        return Strings.join(" ", names);
    }
}
