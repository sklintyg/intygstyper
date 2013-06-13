package se.inera.certificate.modules.fk7263.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Iterables.find;
import static se.inera.certificate.integration.v1.Referenstyp.ANNAT;
import static se.inera.certificate.integration.v1.Referenstyp.JOURNALUPPGIFTER;
import static se.inera.certificate.integration.v1.Sysselsattning.NUVARANDE_ARBETE;
import static se.inera.certificate.integration.v1.Vardkontakttyp.MIN_TELEFONKONTAKT_MED_PATIENTEN;
import static se.inera.certificate.integration.v1.Vardkontakttyp.MIN_UNDERSOKNING_AV_PATIENTEN;

import com.google.common.base.Predicate;
import se.inera.certificate.integration.v1.AktivitetType;
import se.inera.certificate.integration.v1.Aktivitetskod;
import se.inera.certificate.integration.v1.ArbetsformagaType;
import se.inera.certificate.integration.v1.FunktionsnedsattningType;
import se.inera.certificate.integration.v1.Lakarutlatande;
import se.inera.certificate.integration.v1.PatientType;
import se.inera.certificate.integration.v1.Prognosangivelse;
import se.inera.certificate.integration.v1.ReferensType;
import se.inera.certificate.integration.v1.Referenstyp;
import se.inera.certificate.integration.v1.VardkontaktType;
import se.inera.certificate.integration.v1.Vardkontakttyp;

/**
 * @author andreaskaltenbach
 */
public class LakarutlatandeValidator {

    private Lakarutlatande lakarutlatande;

    private List<String> validationErrors = new ArrayList<>();

    public static final String PERSON_NUMBER_REGEX = "[0-9]{8}[-+][0-9]{4}";

    public LakarutlatandeValidator(Lakarutlatande lakarutlatande) {
        this.lakarutlatande = lakarutlatande;
    }

    public List<String> validate() {

        validatePatient(lakarutlatande.getPatient());
        validateNonSmittskydd();

        validateRessatt();
        validateKommentar();

        return validationErrors;
    }

    private void validatePatient(PatientType patient) {
        // Check format of patient id (has to be a valid personnummer)
        String personNumber = patient.getId();
        if (personNumber == null || !Pattern.matches(PERSON_NUMBER_REGEX, personNumber)) {
            validationErrors.add("Wrong format for person-id! Valid format is YYYYMMDD-XXXX or YYYYMMDD+XXXX.");
        }
    }


    private AktivitetType findAktivitetWithCode(final Aktivitetskod aktivitetskod) {
        return find(lakarutlatande.getAktivitets(), new Predicate<AktivitetType>() {
            @Override
            public boolean apply(AktivitetType aktivitet) {
                return aktivitet.getAktivitetskod() == aktivitetskod;
            }
        }, null);
    }

    private VardkontaktType findVardkontaktTyp(final Vardkontakttyp vardkontaktTyp) {

        return find(lakarutlatande.getVardkontakts(), new Predicate<VardkontaktType>() {
            @Override
            public boolean apply(VardkontaktType vardkontakt) {
                return vardkontakt.getVardkontakttyp() == vardkontaktTyp;

            }
        }, null);
    }

    private ReferensType findReferensTyp(final Referenstyp referenstyp) {

        return find(lakarutlatande.getReferens(), new Predicate<ReferensType>() {
            @Override
            public boolean apply(ReferensType referens) {
                return referens.getReferenstyp() == referenstyp;
            }
        }, null);
    }

    private void validateNonSmittskydd() {

        // Many fields are optional if smittskydd is checked, if not set validate these below
        boolean inSmittskydd = findAktivitetWithCode(Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA) != null;

        if (inSmittskydd) {
            return;
        }

        // Fält 2 - Check that we got a bedomtTillstand element
        if (lakarutlatande.getBedomtTillstand() == null) {
            validationErrors.add("No bedomtTillstand found!");
            return;
        }


        // Fält 2 - Bedomt tillstånd kod - mandatory
        if (isNullOrEmpty(lakarutlatande.getBedomtTillstand().getTillstandskod())) {
            validationErrors.add("No tillstandskod in bedomtTillstand found!");
        }

        validateFunktionsnedsattning();
        validateArbetsformaga();
    }

    private void validateFunktionsnedsattning() {

        // Fält 4 - vänster Check that we got a funktionsnedsattning element
        FunktionsnedsattningType funktionsnedsattning = lakarutlatande.getFunktionsnedsattning();
        if (funktionsnedsattning == null) {
            validationErrors.add("No funktionsnedsattning element found!");
            return;
        }

        // Fält 4 - höger översta kryssrutan
        VardkontaktType inUndersokning = findVardkontaktTyp(MIN_UNDERSOKNING_AV_PATIENTEN);

        // Fält 4 - höger näst översta kryssrutan
        VardkontaktType telefonkontakt = findVardkontaktTyp(MIN_TELEFONKONTAKT_MED_PATIENTEN);

        // Fält 4 - höger näst nedersta kryssrutan
        ReferensType journal = findReferensTyp(JOURNALUPPGIFTER);

        // Fält 4 - höger nedersta kryssrutan
        ReferensType inAnnat = findReferensTyp(ANNAT);

        // Fält 4 - höger Check that we at least got one field set
        if (inUndersokning == null && telefonkontakt == null && journal == null && inAnnat == null) {
            validationErrors.add("No vardkontakt or referens element found ! At least one must be set!");
            return;
        }
    }

    private void validateArbetsformaga() {

        // Fält 8a - arbetsformoga
        ArbetsformagaType arbetsformaga = lakarutlatande.getAktivitetsbegransning().getArbetsformaga();

        // Fält 8a
        boolean nuvarandeArbete = arbetsformaga.getSysselsattnings().contains(NUVARANDE_ARBETE);

        // Fält 8a - Check that we got a arbetsuppgift element if arbete is set
        if (nuvarandeArbete && isNullOrEmpty(arbetsformaga.getArbetsuppgift())) {
            validationErrors.add("No arbetsuppgift found when arbete set in field 8a!.");
            return;
        }
    }

    private void validateRessatt() {
        // Fält 11 - optional
        AktivitetType inForandratRessatt = findAktivitetWithCode(Aktivitetskod.FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_AKTUELLT);
        AktivitetType inEjForandratRessatt = findAktivitetWithCode(Aktivitetskod.FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_EJ_AKTUELLT);

        // Fält 11 - If set only one should be set
        if (inForandratRessatt != null && inEjForandratRessatt != null) {
            validationErrors.add("Only one forandrat ressatt could be set for field 11.");
        }
    }

    private void validateKommentar() {

        // Fält 13 - Upplysningar - optional
        // If field 4 annat satt or field 10 går ej att bedömma is set then
        // field 13 should contain data.
        String kommentar = lakarutlatande.getKommentar();
        ReferensType annat = findReferensTyp(Referenstyp.ANNAT);

        boolean garEjAttBedomma = lakarutlatande.getAktivitetsbegransning().getArbetsformaga().getPrognosangivelse() == Prognosangivelse.DET_GAR_INTE_ATT_BEDOMMA;

        if ((annat != null || garEjAttBedomma) && (isNullOrEmpty(kommentar))) {
            validationErrors.add("Upplysningar should contain data as field 4 or fields 10 is checked.");
        }
    }
}
