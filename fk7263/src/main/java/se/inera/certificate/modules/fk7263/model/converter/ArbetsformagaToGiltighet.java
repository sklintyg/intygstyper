package se.inera.certificate.modules.fk7263.model.converter;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;

import se.inera.certificate.model.InternalDate;
import se.inera.certificate.model.InternalLocalDateInterval;
import se.inera.certificate.model.LocalDateInterval;
import se.inera.certificate.modules.fk7263.model.internal.Utlatande;

/**
 * Created by erik on 15-04-08.
 */
public final class ArbetsformagaToGiltighet {

    private ArbetsformagaToGiltighet() {

    }

    /**
     * Uses the NedsattMedNN information on the Utlatande to build a LocalDateInterval with the earliest fromDate to the
     * latest toDate.
     *
     * @param utlatande
     * @return
     *      Aa LocalDateInterval with the earliest fromDate to the
     *      latest toDate
     */
    public static LocalDateInterval getGiltighetFromUtlatande(Utlatande utlatande) {
        return new LocalDateInterval(getValidFromDate(utlatande), getValidToDate(utlatande));
    }

    private static LocalDate getValidToDate(Utlatande utlatande) {
        LocalDate toDate = null;
        List<InternalDate> toDates = new ArrayList<>();
        addTomDate(utlatande.getNedsattMed100(), toDates);
        addTomDate(utlatande.getNedsattMed75(), toDates);
        addTomDate(utlatande.getNedsattMed50(), toDates);
        addTomDate(utlatande.getNedsattMed25(), toDates);

        for (InternalDate internalDate : toDates) {
            if (toDate == null || internalDate.asLocalDate().isAfter(toDate)) {
                toDate = internalDate.asLocalDate();
            }
        }
        return toDate;
    }

    private static LocalDate getValidFromDate(Utlatande utlatande) {
        LocalDate fromDate = null;

        List<InternalDate> fromDates = new ArrayList<>();
        addFromDate(utlatande.getNedsattMed100(), fromDates);
        addFromDate(utlatande.getNedsattMed75(), fromDates);
        addFromDate(utlatande.getNedsattMed50(), fromDates);
        addFromDate(utlatande.getNedsattMed25(), fromDates);

        for (InternalDate internalDate : fromDates) {
            if (fromDate == null || internalDate.asLocalDate().isBefore(fromDate)) {
                fromDate = internalDate.asLocalDate();
            }
        }
        return fromDate;
    }

    private static void addTomDate(InternalLocalDateInterval interval, List<InternalDate> dates) {
        if (interval != null && interval.getTom() != null) {
            dates.add(interval.getTom());
        }
    }

    private static void addFromDate(InternalLocalDateInterval interval, List<InternalDate> dates) {
        if (interval != null && interval.getFrom() != null) {
            dates.add(interval.getFrom());
        }
    }

}
