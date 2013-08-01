package se.inera.certificate.modules.fk7263.model;

import static se.inera.certificate.model.util.Iterables.find;

import se.inera.certificate.model.Aktivitet;
import se.inera.certificate.model.Kod;
import se.inera.certificate.model.Observation;
import se.inera.certificate.model.Utlatande;
import se.inera.certificate.model.Vardenhet;
import se.inera.certificate.model.codes.ObservationsKoder;
import se.inera.certificate.model.util.Predicate;

/**
 * @author andreaskaltenbach
 */
public class Fk7263Intyg extends Utlatande {

    private static final Kod Aktivitet_Arbetslivsinriktad_rehabilitering_ar_aktuell = new Kod("Arbetslivsinriktad_rehabilitering_ar_aktuell");
    private static final Kod Aktivitet_Arbetslivsinriktad_rehabilitering_ar_ej_aktuell = new Kod("Arbetslivsinriktad_rehabilitering_ar_ej_aktuell");
    private static final Kod Aktivitet_Gar_ej_att_bedomma_om_arbetslivsinriktad_rehabilitering_ar_aktuell = new Kod("Gar_ej_att_bedomma_om_arbetslivsinriktad_rehabilitering_ar_aktuell");
    public static final Kod Aktivitet_Forandrat_ressatt_till_arbetsplatsen_ar_aktuellt = new Kod("Forandrat_ressatt_till_arbetsplatsen_ar_aktuellt");
    public static final Kod Aktivitet_Forandrat_ressatt_till_arbetsplatsen_ar_ej_aktuellt = new Kod("Forandrat_ressatt_till_arbetsplatsen_ar_ej_aktuellt");
    private static final Kod Aktivitet_Planerad_eller_pagaende_behandling_eller_atgard_inom_sjukvarden = new Kod("Planerad_eller_pagaende_behandling_eller_atgard_inom_sjukvarden");
    private static final Kod Aktivitet_Planerad_eller_pagaende_annan_atgard = new Kod("Planerad_eller_pagaende_annan_atgard");
    private static final Kod Aktivitet_Kontakt_med_Forsakringskassan_ar_aktuell = new Kod("Kontakt_med_Forsakringskassan_ar_aktuell");
    private static final Kod Aktivitet_Patienten_behover_fa_kontakt_med_foretagshalsovarden = new Kod("Patienten_behover_fa_kontakt_med_foretagshalsovarden");
    private static final Kod Aktivitet_Avstangning_enligt_SmL_pga_smitta = new Kod("Avstangning_enligt_SmL_pga_smitta");
    private static final Kod Aktivitet_Ovrigt = new Kod("Ovrigt");
    private static final Kod Aktivitet_Patienten_behover_fa_kontakt_med_Arbetsformedlingen = new Kod("Patienten_behover_fa_kontakt_med_Arbetsformedlingen");

    private static final Kod Prognos_Aterstallas_helt = new Kod("Aterstallas_helt");
    private static final Kod Prognos_Aterstallas_delvis = new Kod("Aterstallas_delvis");
    private static final Kod Prognos_Inte_aterstallas = new Kod("Inte_aterstallas");
    public static final Kod Prognos_Det_gar_inte_att_bedomma = new Kod("Det_gar_inte_att_bedomma");

    public static final Kod Sysselsattning_Nuvarande_arbete = new Kod("Nuvarande_arbete");
    private static final Kod Sysselsattning_Arbetsloshet = new Kod("Arbetsloshet");
    private static final Kod Sysselsattning_Foraldraledighet = new Kod("Foraldraledighet");

    public static final Kod Nedsattningsgrad_Helt_nedsatt = new Kod("Helt_nedsatt");
    public static final Kod Nedsattningsgrad_Nedsatt_med_3_4 = new Kod("Nedsatt_med_3/4");
    public static final Kod Nedsattningsgrad_Nedsatt_med_1_2 = new Kod("Nedsatt_med_1/2");
    public static final Kod Nedsattningsgrad_Nedsatt_med_1_4 = new Kod("Nedsatt_med_1/4");

    public static final Kod Referens_Journaluppgifter = new Kod("Journaluppgifter");
    public static final Kod Referens_Annat = new Kod("Annat");

    public static final Kod Vardkontakt_Min_undersokning_av_patienten = new Kod("Min_undersokning_av_patienten");
    public static final Kod Vardkontakt_Min_telefonkontakt_med_patienten = new Kod("Min_telefonkontakt_med_patienten");

    public static final String DATE_PATTERN = "yyyy-MM-dd";

    public String getNamnfortydligandeOchAdress() {
        if (getSkapadAv() == null || getSkapadAv().getVardenhet() == null) {
            return "";
        }

        Vardenhet enhet = getSkapadAv().getVardenhet();
        String nameAndAddress = getSkapadAv().getNamn() + "\n"
                + enhet.getNamn()
                + "\n" + enhet.getPostadress()
                + "\n" + enhet.getPostnummer()
                + " " + enhet.getPostort() + "\n"
                + enhet.getTelefonnummer();
        return nameAndAddress;
    }

    public String getSigneringsDatumAsString() {
        return getSigneringsDatum().toString(DATE_PATTERN);
    }

    public String getRekommenderarOvrigtText() {
        return getAktivitetsText(Aktivitet_Ovrigt);
    }

    private String getAktivitetsText(Kod aktivitetskod) {
        Aktivitet activity = getAktivitet(aktivitetskod);
        if (activity != null) {
            return activity.getBeskrivning();
        } else {
            return null;
        }
    }

    public String getAktivitetsnedsattningBeskrivning() {
        Observation aktivitetsbegransning = getAktivitetsbegransning();
        return (aktivitetsbegransning != null) ? aktivitetsbegransning.getBeskrivning() : null;
    }

    public String getFunktionsnedsattningBeskrivning() {
        Observation funktionsnedsattning = getFunktionsnedsattning();
        return (funktionsnedsattning != null) ? funktionsnedsattning.getBeskrivning() : null;
    }

    public Observation getFunktionsnedsattning() {
        return getObservationByKategori(ObservationsKoder.KROPPSFUNKTION);
    }

    public Observation getMedicinsktTillstand() {
        return getObservationByKategori(ObservationsKoder.MEDICINSKT_TILLSTAND);
    }

    public Observation getAktivitetsbegransning() {
        return getObservationByKategori(ObservationsKoder.AKTIVITET);
    }

    public Observation getArbetsformagaAktivitetsbegransning() {
        return getObservationByKategori(ObservationsKoder.ARBETSFORMAGA);
    }

    public boolean isPrognosDelvisAterstallning() {
        return Prognos_Aterstallas_delvis.equals(getPrognosKod());
    }

    public boolean isPrognosEjAterstallning() {
        return Prognos_Inte_aterstallas.equals(getPrognosKod());
    }

    public boolean isPrognosFullAterstallning() {
        return Prognos_Aterstallas_helt.equals(getPrognosKod());
    }

    public boolean isPrognosAterstallningGarEjBedomma() {
        return Prognos_Det_gar_inte_att_bedomma.equals(getPrognosKod());
    }

    public String getPrognosText() {
        Observation arbetsformaga = getArbetsformaga();
        if (arbetsformaga != null) {
            return arbetsformaga.getBeskrivning();
        }
        return null;
    }

    private Kod getPrognosKod() {

        Observation arbetsformaga = getArbetsformaga();
        if (arbetsformaga != null) {
            return arbetsformaga.getObservationsKod();
        }
        return null;
    }

    public Observation getNedsattning(final Double nedsattningsgrad) {
        return find(getObservationsByKategori(ObservationsKoder.NEDSATTNING), new Predicate<Observation>() {
            @Override
            public boolean apply(Observation nedsattning) {
                return nedsattning.getVarde() != null && !nedsattning.getVarde().isEmpty() && nedsattningsgrad.equals(nedsattning.getVarde().get(0).getQuantity());
            }
        }, null);
    }

    public boolean isArbetsformagaIForhallandeTillArbetsloshet() {
        return containsSysselsattningKod(Sysselsattning_Arbetsloshet);
    }

    public boolean isArbetsformagaIForhallandeTillForaldraledighet() {
        return containsSysselsattningKod(Sysselsattning_Foraldraledighet);
    }

    public boolean isArbetsformagaIForhallandeTillNuvarandeArbete() {
        return containsSysselsattningKod(Sysselsattning_Nuvarande_arbete);
    }

    private boolean containsSysselsattningKod(Kod sysselsattningKod) {
        for (se.inera.certificate.model.Sysselsattning sysselsattning : getPatient().getSysselsattnings()) {
            if (sysselsattningKod.equals(sysselsattning.getSysselsattningsTyp())) {
                return true;
            }
        }
        return false;
    }

    public Observation getArbetsformaga() {
        return getObservationByKategori(ObservationsKoder.ARBETSFORMAGA);
    }

    public Aktivitet getForandratRessattAktuellt() {
        return getAktivitet(Aktivitet_Forandrat_ressatt_till_arbetsplatsen_ar_aktuellt);
    }

    public Aktivitet getForandratRessattEjAktuellt() {
        return getAktivitet(Aktivitet_Forandrat_ressatt_till_arbetsplatsen_ar_ej_aktuellt);
    }

    public Aktivitet getKontaktMedForsakringskassanAktuell() {
        return getAktivitet(Aktivitet_Kontakt_med_Forsakringskassan_ar_aktuell);
    }

    public Aktivitet getArbetsinriktadRehabiliteringAktuell() {
        return getAktivitet(Aktivitet_Arbetslivsinriktad_rehabilitering_ar_aktuell);
    }

    public Aktivitet getArbetsinriktadRehabiliteringEjAktuell() {
        return getAktivitet(Aktivitet_Arbetslivsinriktad_rehabilitering_ar_ej_aktuell);
    }

    public Aktivitet getArbetsinriktadRehabiliteringEjBedombar() {
        return getAktivitet(Aktivitet_Gar_ej_att_bedomma_om_arbetslivsinriktad_rehabilitering_ar_aktuell);
    }

    public Aktivitet getAvstangningEnligtSmittskyddslagen() {
        return getAktivitet(Aktivitet_Avstangning_enligt_SmL_pga_smitta);
    }

    public Aktivitet getRekommenderarKontaktMedArbetsformedlingen() {
        return getAktivitet(Aktivitet_Patienten_behover_fa_kontakt_med_Arbetsformedlingen);
    }

    public Aktivitet getRekommenderarKontaktMedForetagshalsovarden() {
        return getAktivitet(Aktivitet_Patienten_behover_fa_kontakt_med_foretagshalsovarden);
    }

    public Aktivitet getRekommenderarOvrigt() {
        return getAktivitet(Aktivitet_Ovrigt);
    }

    public Aktivitet getAtgardInomSjukvarden() {
        return getAktivitet(Aktivitet_Planerad_eller_pagaende_behandling_eller_atgard_inom_sjukvarden);
    }

    public Aktivitet getAnnanAtgard() {
        return getAktivitet(Aktivitet_Planerad_eller_pagaende_annan_atgard);
    }
}
