package se.inera.certificate.modules.fk7263.model.converter;

import java.util.List;

import static org.joda.time.DateTimeFieldType.dayOfMonth;
import static org.joda.time.DateTimeFieldType.monthOfYear;
import static org.joda.time.DateTimeFieldType.year;

import org.joda.time.LocalDate;
import org.joda.time.Partial;
import se.inera.certificate.model.Aktivitet;
import se.inera.certificate.model.HosPersonal;
import se.inera.certificate.model.LocalDateInterval;
import se.inera.certificate.model.Observation;
import se.inera.certificate.model.PartialInterval;
import se.inera.certificate.model.PhysicalQuantity;
import se.inera.certificate.model.Prognos;
import se.inera.certificate.model.Referens;
import se.inera.certificate.model.Sysselsattning;
import se.inera.certificate.model.Vardenhet;
import se.inera.certificate.model.Vardgivare;
import se.inera.certificate.model.Vardkontakt;
import se.inera.certificate.modules.fk7263.model.codes.Aktivitetskoder;
import se.inera.certificate.modules.fk7263.model.codes.ObservationsKoder;
import se.inera.certificate.modules.fk7263.model.codes.Prognoskoder;
import se.inera.certificate.modules.fk7263.model.codes.Referenstypkoder;
import se.inera.certificate.modules.fk7263.model.codes.Sysselsattningskoder;
import se.inera.certificate.modules.fk7263.model.codes.Vardkontakttypkoder;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Patient;
import se.inera.certificate.modules.fk7263.model.external.Fk7263Utlatande;
import se.inera.certificate.modules.fk7263.model.internal.Fk7263Intyg;
import se.inera.certificate.modules.fk7263.model.internal.Vardperson;

/**
 * @author andreaskaltenbach
 */
public class ExternalToInternalConverter {

    private Fk7263Utlatande source;
    private Fk7263Intyg intyg = new Fk7263Intyg();

    public ExternalToInternalConverter(Fk7263Utlatande source) {
        this.source = source;
    }

    public Fk7263Intyg convert() {

        intyg.setId(source.getId().getRoot());
        intyg.setSkickatDatum(source.getSkickatdatum());
        intyg.setGiltighet(new PartialInterval(source.getValidFromDate(), source.getValidToDate()));

        Aktivitet smittskydd = source.getAktivitet(Aktivitetskoder.AVSTANGNING_ENLIGT_SML_PGA_SMITTA);
        intyg.setAvstangningSmittskydd(smittskydd != null);

        convertDiagnos();
        convertSjukdomsforlopp();
        convertFunktionsnedsattning();
        convertVardkontakter();
        convertReferenser();
        convertAktivitetsbegransning();
        convertAktiviteter();
        convertPatient();
        convertArbetsformaga();
        convertKommentar();
        convertSkapasAv();

        intyg.setSigneringsdatum(source.getSigneringsdatum());

        return intyg;
    }

    private void convertSkapasAv() {
        HosPersonal personal = source.getSkapadAv();
        Vardperson vardperson = new Vardperson();
        if (personal != null) {
            if (personal.getId() != null) {
                vardperson.setHsaId(personal.getId().getExtension());
            }

            vardperson.setNamn(personal.getNamn());
            vardperson.setForskrivarKod(personal.getForskrivarkod());

            Vardenhet enhet = personal.getVardenhet();
            if (enhet != null) {

                if (enhet.getId() != null) {
                    vardperson.setEnhetsId(enhet.getId().getExtension());
                }

                if (enhet.getArbetsplatskod() != null) {
                    vardperson.setArbetsplatsKod(enhet.getArbetsplatskod().getExtension());
                }

                vardperson.setEnhetsnamn(enhet.getNamn());
                vardperson.setPostadress(enhet.getPostadress());
                vardperson.setPostnummer(enhet.getPostnummer());
                vardperson.setPostort(enhet.getPostort());
                vardperson.setTelefonnummer(enhet.getTelefonnummer());
                vardperson.setEpost(enhet.getEpost());

                Vardgivare vardgivare = enhet.getVardgivare();
                if (vardgivare != null) {
                    if (vardgivare.getId() != null) {
                        vardperson.setVardgivarId(vardgivare.getId().getExtension());
                    }
                    vardperson.setVardgivarnamn(vardgivare.getNamn());
                }
            }
        }
        intyg.setVardperson(vardperson);
    }

    private void convertKommentar() {
        if (!source.getKommentarer().isEmpty()) {
            intyg.setKommentar(source.getKommentarer().get(0));
        }
    }

    private LocalDate toLocalDate(Partial partial) {
        if (partial == null) {
            return null;
        }
        return new LocalDate(partial.get(year()), partial.get(monthOfYear()), partial.get(dayOfMonth()));
    }

    private LocalDateInterval toDatumInterval(PartialInterval interval) {
        if (interval == null) {
            return null;
        }
        return new LocalDateInterval(toLocalDate(interval.getFrom()), toLocalDate(interval.getTom()));
    }

    private void convertArbetsformaga() {
        List<Observation> arbetsformagor = source.getObservationsByKod(ObservationsKoder.ARBETSFORMAGA);

        if (!arbetsformagor.isEmpty()) {
            Observation arbetsformaga = arbetsformagor.get(0);
            convertPrognoser(arbetsformaga.getPrognoser());
        }

        for (Observation arbetsformaga : arbetsformagor) {

            if (!arbetsformaga.getVarde().isEmpty()) {
                PartialInterval interval = arbetsformaga.getObservationsperiod();
                PhysicalQuantity quantity = arbetsformaga.getVarde().get(0);

                switch (quantity.getQuantity().toString()) {
                case "75.0":
                    intyg.setNedsattMed25(toDatumInterval(interval));
                    break;
                case "50.0":
                    intyg.setNedsattMed50(toDatumInterval(interval));
                    break;
                case "25.0":
                    intyg.setNedsattMed75(toDatumInterval(interval));
                    break;
                case "0.0":
                    intyg.setNedsattMed100(toDatumInterval(interval));
                    break;
                }
            }
        }

    }

    private void convertPrognoser(List<Prognos> prognoser) {
        if (!prognoser.isEmpty()) {
            intyg.setArbetsformagaPrognos(prognoser.get(0).getBeskrivning());

            for (Prognos prognos : prognoser) {
                if (Prognoskoder.ATERSTALLAS_HELT.equals(prognos.getPrognoskod())) {
                    intyg.setArbetsformataPrognosJa(true);
                } else if (Prognoskoder.ATERSTALLAS_DELVIS.equals(prognos.getPrognoskod())) {
                    intyg.setArbetsformataPrognosJaDelvis(true);
                } else if (Prognoskoder.INTE_ATERSTALLAS.equals(prognos.getPrognoskod())) {
                    intyg.setArbetsformataPrognosNej(true);
                } else if (Prognoskoder.DET_GAR_INTE_ATT_BEDOMA.equals(prognos.getPrognoskod())) {
                    intyg.setArbetsformataPrognosGarInteAttBedoma(true);
                }
            }
        }
    }

    private void convertPatient() {

        Fk7263Patient patient = source.getPatient();
        if(patient == null) {
            return;
        }

        intyg.setPatientNamn(patient.getFullstandigtNamn());
        intyg.setPatientPersonnummer(patient.getId().getExtension());

        for (Sysselsattning sysselsattning : patient.getSysselsattningar()) {
            if (Sysselsattningskoder.NUVARANDE_ARBETE.equals(sysselsattning.getSysselsattningstyp())
                    && !patient.getArbetsuppgifter().isEmpty()) {
                intyg.setNuvarandeArbetsuppgifter(patient.getArbetsuppgifter().get(0).getTypAvArbetsuppgift());
            } else if (Sysselsattningskoder.ARBETSLOSHET.equals(sysselsattning.getSysselsattningstyp())) {
                intyg.setArbetsloshet(true);
            } else if (Sysselsattningskoder.MAMMALEDIG.equals(sysselsattning.getSysselsattningstyp())) {
                intyg.setForaldrarledighet(true);
            } else if (Sysselsattningskoder.PAPPALEDIG.equals(sysselsattning.getSysselsattningstyp())) {
                intyg.setForaldrarledighet(true);
            }
        }

    }

    private void convertAktiviteter() {

        for (Aktivitet aktivitet : source.getAktiviteter()) {

            if (Aktivitetskoder.PATIENTEN_BOR_FA_KONTAKT_MED_ARBETSFORMEDLINGEN.equals(aktivitet.getAktivitetskod())) {
                intyg.setRekommendationKontaktArbetsformedlingen(true);
            } else if (Aktivitetskoder.PATIENTEN_BOR_FA_KONTAKT_MED_FORETAGSHALSOVARDEN.equals(aktivitet
                    .getAktivitetskod())) {
                intyg.setRekommendationKontaktForetagshalsovarden(true);
            } else if (Aktivitetskoder.OVRIGT.equals(aktivitet.getAktivitetskod())) {
                intyg.setRekommendationOvrigt(aktivitet.getBeskrivning());
            } else if (Aktivitetskoder.PLANERAD_ELLER_PAGAENDE_BEHANDLING_ELLER_ATGARD_INOM_SJUKVARDEN.equals(aktivitet
                    .getAktivitetskod())) {
                intyg.setAtgardInomSjukvarden(aktivitet.getBeskrivning());
            } else if (Aktivitetskoder.PLANERAD_ELLER_PAGAENDE_ANNAN_ATGARD.equals(aktivitet.getAktivitetskod())) {
                intyg.setAnnanAtgard(aktivitet.getBeskrivning());
            } else if (Aktivitetskoder.ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL
                    .equals(aktivitet.getAktivitetskod())) {
                intyg.setRehabiliteringAktuell(true);
            } else if (Aktivitetskoder.ARBETSLIVSINRIKTAD_REHABILITERING_AR_INTE_AKTUELL.equals(aktivitet
                    .getAktivitetskod())) {
                intyg.setRehabiliteringEjAktuell(true);
            } else if (Aktivitetskoder.GAR_EJ_ATT_BEDOMA_OM_ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL
                    .equals(aktivitet.getAktivitetskod())) {
                intyg.setRehabiliteringGarInteAttBedoma(true);
            } else if (Aktivitetskoder.FORANDRA_RESSATT_TILL_ARBETSPLATSEN_AR_AKTUELLT.equals(aktivitet
                    .getAktivitetskod())) {
                intyg.setRessattTillArbeteAktuellt(true);
            } else if (Aktivitetskoder.FORANDRA_RESSATT_TILL_ARBETSPLATSEN_AR_EJ_AKTUELLT.equals(aktivitet
                    .getAktivitetskod())) {
                intyg.setRessattTillArbeteEjAktuellt(true);
            } else if (Aktivitetskoder.KONTAKT_MED_FK_AR_AKTUELL.equals(aktivitet.getAktivitetskod())) {
                intyg.setKontaktMedFk(true);
            }

        }

    }

    private void convertAktivitetsbegransning() {
        Observation aktivitetsbegransning = source
                .findObservationByKategori(ObservationsKoder.AKTIVITETER_OCH_DELAKTIGHET);
        if (aktivitetsbegransning != null) {
            intyg.setAktivitetsbegransning(aktivitetsbegransning.getBeskrivning());
        }
    }

    private void convertVardkontakter() {
        for (Vardkontakt vardkontakt : source.getVardkontakter()) {
            if (Vardkontakttypkoder.MIN_UNDERSOKNING_AV_PATIENTEN.equals(vardkontakt.getVardkontakttyp())) {
                intyg.setUndersokningAvPatienten(vardkontakt.getVardkontaktstid().getStart());
            } else if (Vardkontakttypkoder.MIN_TELEFONKONTAKT_MED_PATIENTEN.equals(vardkontakt.getVardkontakttyp())) {
                intyg.setTelefonkontaktMedPatienten(vardkontakt.getVardkontaktstid().getStart());
            }
        }
    }

    private void convertReferenser() {
        for (Referens referens : source.getReferenser()) {
            if (Referenstypkoder.JOURNALUPPGIFT.equals(referens.getReferenstyp())) {
                intyg.setJournaluppgifter(referens.getDatum());
            }
            if (Referenstypkoder.ANNAT.equals(referens.getReferenstyp())) {
                intyg.setAnnanReferens(referens.getDatum());
            }
        }
    }

    private void convertFunktionsnedsattning() {
        Observation funktionsnedsattning = source.findObservationByKategori(ObservationsKoder.KROPPSFUNKTIONER);
        if (funktionsnedsattning != null) {
            intyg.setFunktionsnedsattning(funktionsnedsattning.getBeskrivning());
        }
    }

    private void convertSjukdomsforlopp() {
        List<Observation> sjukdomsforlopp = source.getObservationsByKod(ObservationsKoder.FORLOPP);
        if (sjukdomsforlopp != null && !sjukdomsforlopp.isEmpty()) {
            intyg.setSjukdomsforlopp(sjukdomsforlopp.get(0).getBeskrivning());
        }
    }

    private void convertDiagnos() {
        Observation huvudDiagnos = source.findObservationByKategori(ObservationsKoder.DIAGNOS);
        if (huvudDiagnos != null) {
            intyg.setDiagnosKod(huvudDiagnos.getObservationskod().getCode());
            intyg.setDiagnosBeskrivning(huvudDiagnos.getBeskrivning());
        }
    }
}
