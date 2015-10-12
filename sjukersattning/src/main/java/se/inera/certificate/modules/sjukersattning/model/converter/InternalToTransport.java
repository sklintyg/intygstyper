package se.inera.certificate.modules.sjukersattning.model.converter;

import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.*;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import se.inera.certificate.model.InternalLocalDateInterval;
import se.inera.certificate.model.common.internal.HoSPersonal;
import se.inera.certificate.model.common.internal.Vardenhet;
import se.inera.certificate.model.converter.util.ConverterException;
import se.inera.certificate.modules.sjukersattning.model.internal.SjukersattningUtlatande;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateType;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.*;
import se.riv.clinicalprocess.healthcond.certificate.v2.*;
import se.riv.clinicalprocess.healthcond.certificate.v2.Svar.Delsvar;

public class InternalToTransport {

    public static RegisterCertificateType convert(SjukersattningUtlatande source) throws ConverterException {
        if (source == null) {
            throw new ConverterException("Source utlatande was null, cannot convert");
        }

        RegisterCertificateType sjukersattningType = new RegisterCertificateType();
        sjukersattningType.setIntyg(getIntyg(source));
        return sjukersattningType;
    }

    private static Intyg getIntyg(SjukersattningUtlatande source) {
        Intyg intyg = new Intyg();
        intyg.setTyp(getTypAvIntyg(source));
        intyg.setIntygsId(getIntygsId(source));
        intyg.setVersion("1"); // TODO
        intyg.setSigneringstidpunkt(source.getGrundData().getSigneringsdatum());
        intyg.setSkickatTidpunkt(new LocalDateTime());
        intyg.setSkapadAv(getSkapadAv(source));
        intyg.setPatient(getPatient(source.getGrundData().getPatient()));
        intyg.getSvar().addAll(getSvar(source));
        return intyg;
    }

    private static HosPersonal getSkapadAv(SjukersattningUtlatande source) {
        HoSPersonal sourceSkapadAv = source.getGrundData().getSkapadAv();
        HosPersonal skapadAv = new HosPersonal();
        skapadAv.setPersonalId(anHsaId(sourceSkapadAv.getPersonId()));
        skapadAv.setFullstandigtNamn(sourceSkapadAv.getFullstandigtNamn());
        skapadAv.setEnhet(getEnhet(sourceSkapadAv.getVardenhet()));
        return skapadAv;
    }

    private static Enhet getEnhet(Vardenhet sourceVardenhet) {
        Enhet vardenhet = new Enhet();
        vardenhet.setEnhetsId(anHsaId(sourceVardenhet.getEnhetsid()));
        vardenhet.setEnhetsnamn(sourceVardenhet.getEnhetsnamn());
        vardenhet.setPostnummer(sourceVardenhet.getPostnummer());
        vardenhet.setPostadress(sourceVardenhet.getPostadress());
        vardenhet.setPostort(sourceVardenhet.getPostort());
        vardenhet.setTelefonnummer(sourceVardenhet.getTelefonnummer());
        vardenhet.setVardgivare(getVardgivare(sourceVardenhet.getVardgivare()));
        vardenhet.setArbetsplatskod(getArbetsplatsKod(sourceVardenhet.getArbetsplatsKod()));
        return vardenhet;
    }

    private static ArbetsplatsKod getArbetsplatsKod(String sourceArbetsplatsKod) {
        ArbetsplatsKod arbetsplatsKod = new ArbetsplatsKod();
        arbetsplatsKod.setExtension(sourceArbetsplatsKod);
        return arbetsplatsKod;
    }

    private static Vardgivare getVardgivare(se.inera.certificate.model.common.internal.Vardgivare sourceVardgivare) {
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivareId(anHsaId(sourceVardgivare.getVardgivarid()));
        vardgivare.setVardgivarnamn(sourceVardgivare.getVardgivarnamn());
        return vardgivare;
    }

    private static Patient getPatient(se.inera.certificate.model.common.internal.Patient sourcePatient) {
        Patient patient = new se.riv.clinicalprocess.healthcond.certificate.v2.Patient();
        patient.setEfternamn(sourcePatient.getEfternamn());
        patient.setFornamn(sourcePatient.getFornamn());
        patient.setMellannamn(sourcePatient.getMellannamn());
        PersonId personId = new PersonId();
        personId.setRoot(PERSON_ID_CODE_SYSTEM);
        personId.setExtension(sourcePatient.getPersonId().replaceAll("-", ""));
        patient.setPersonId(personId);
        patient.setPostadress(sourcePatient.getPostadress());
        patient.setPostnummer(sourcePatient.getPostnummer());
        patient.setPostort(sourcePatient.getPostadress());
        return patient;
    }

    private static IntygId getIntygsId(SjukersattningUtlatande source) {
        IntygId intygId = new IntygId();
        intygId.setRoot(source.getId());
        return intygId;
    }

    private static TypAvIntyg getTypAvIntyg(SjukersattningUtlatande source) {
        TypAvIntyg typAvIntyg = new TypAvIntyg();
        typAvIntyg.setCodeSystem(CERTIFICATE_CODE_SYSTEM);
        typAvIntyg.setCodeSystemName(INTYP_TYP_CODE_SYSTEM);
        typAvIntyg.setCode(source.getTyp());
        return typAvIntyg;
    }

    private static List<Svar> getSvar(SjukersattningUtlatande source) {
        List<Svar> svars = new ArrayList<>();

        if (source.getUndersokningAvPatienten() != null) {
            svars.add(aSvar(REFERENS_SVAR_ID).
                    withDelsvar(REFERENSTYP_DELSVAR_ID, aCV(REFERENS_CODE_SYSTEM, Integer.toString(UNDERSOKNING_AV_PATIENT), null)).
                    withDelsvar(REFERENSDATUM_DELSVAR_ID, source.getUndersokningAvPatienten().asLocalDate().toString()).build());
        }
        if (source.getTelefonkontaktMedPatienten() != null) {
            svars.add(aSvar(REFERENS_SVAR_ID).
                    withDelsvar(REFERENSTYP_DELSVAR_ID, aCV(REFERENS_CODE_SYSTEM, Integer.toString(TELEFONKONTAKT), null)).
                    withDelsvar(REFERENSDATUM_DELSVAR_ID, source.getTelefonkontaktMedPatienten().asLocalDate().toString()).build());
        }
        if (source.getJournaluppgifter() != null) {
            svars.add(aSvar(REFERENS_SVAR_ID).
                    withDelsvar(REFERENSTYP_DELSVAR_ID, aCV(REFERENS_CODE_SYSTEM, Integer.toString(JOURNALUPPGIFTER), null)).
                    withDelsvar(REFERENSDATUM_DELSVAR_ID, source.getJournaluppgifter().asLocalDate().toString()).build());
        }
        if (source.getKannedomOmPatient() != null) {
            svars.add(aSvar(OVRIGKANNEDOM_SVAR_ID).
                    withDelsvar(OVRIGKANNEDOM_DELSVAR_ID, source.getKannedomOmPatient().asLocalDate().toString()).build());
        }

        svars.add(aSvar(HUVUDSAKLIG_ORSAK_SVAR_ID).
                withDelsvar(DIAGNOS_DELSVAR_ID, aCV(DIAGNOS_CODE_SYSTEM, source.getDiagnosKod1(), source.getDiagnosBeskrivning1())).
                withDelsvar(DIAGNOS_BESKRIVNING_DELSVAR_ID, source.getDiagnosYtterligareBeskrivning1()).build());

        if (source.getDiagnosKod2() != null) {
            svars.add(aSvar(YTTERLIGARE_ORSAK_SVAR_ID).withDelsvar(YTTERLIGARE_ORSAK_DELSVAR_ID,
                    aCV(DIAGNOS_CODE_SYSTEM, source.getDiagnosKod2(), source.getDiagnosBeskrivning2())).
                    withDelsvar(YTTERLIGARE_ORSAK_BESKRIVNING_DELSVAR_ID, source.getDiagnosYtterligareBeskrivning2()).build());
        }
        if (source.getDiagnosKod3() != null) {
            svars.add(aSvar(YTTERLIGARE_ORSAK_SVAR_ID).withDelsvar(YTTERLIGARE_ORSAK_DELSVAR_ID,
                    aCV(DIAGNOS_CODE_SYSTEM, source.getDiagnosKod3(), source.getDiagnosBeskrivning3())).
                    withDelsvar(YTTERLIGARE_ORSAK_BESKRIVNING_DELSVAR_ID, source.getDiagnosYtterligareBeskrivning3()).build());
        }

        svars.add(aSvar(DIAGNOSTISERING_SVAR_ID).
                withDelsvar(DIAGNOSTISERING_DELSVAR_ID, source.getDiagnostisering()).build());
        svars.add(aSvar(NYBEDOMNING_SVAR_ID).
                withDelsvar(NYBEDOMNING_DELSVAR_ID, Boolean.toString(source.isNyBedomningDiagnos())).build());

        // TODO: Åtgärd som orsak till nedsatt arbetsförmåga ska in här.

        // TODO: Funktionsområde
        svars.add(aSvar(FUNKTIONSNEDSATTNING_SVAR_ID).withDelsvar(FUNKTIONSNEDSATTNING_DELSVAR_ID, source.getFunktionsnedsattning()).
                withDelsvar(FUNKTIONSNEDSATTNING_FUNKTIONSOMRADE_DELSVAR_ID, aCV(FUNKTIONSOMRADE_CODE_SYSTEM, "TODO", null)).build());

        svars.add(aSvar(AKTIVITETSBEGRANSNING_SVAR_ID).withDelsvar(AKTIVITETSBEGRANSNING_DELSVAR_ID, source.getAktivitetsbegransning()).build());

        svars.add(aSvar(PAGAENDEBEHANDLING_SVAR_ID).withDelsvar(PAGAENDEBEHANDLING_DELSVAR_ID, source.getPagaendeBehandling()).build());

        svars.add(aSvar(AVSLUTADBEHANDLING_SVAR_ID).withDelsvar(AVSLUTADBEHANDLING_DELSVAR_ID, source.getAvslutadBehandling()).build());

        svars.add(aSvar(PLANERADBEHANDLING_SVAR_ID).withDelsvar(PLANERADBEHANDLING_DELSVAR_ID, source.getPlaneradBehandling()).build());

        svars.add(aSvar(AKTIVITETSFORMAGA_SVAR_ID).withDelsvar(AKTIVITETSFORMAGA_DELSVAR_ID, source.getVadPatientenKanGora()).build());

        svars.add(aSvar(PROGNOS_SVAR_ID).withDelsvar(PROGNOS_DELSVAR_ID, source.getPrognosNarPatientKanAterga()).build());

        // Arbetslivsinriktade åtgärder svars.add(aSvar("18").withDelsvar("18.1", source.))

        svars.add(aSvar(OVRIGT_SVAR_ID).withDelsvar(OVRIGT_DELSVAR_ID, source.getKommentar()).build());

        svars.add(aSvar(KONTAKT_ONSKAS_SVAR_ID).withDelsvar(KONTAKT_ONSKAS_DELSVAR_ID, Boolean.toString(source.isKontaktMedFk())).build());

        return svars;
    }

    private static HsaId anHsaId(String id) {
        HsaId hsaId = new HsaId();
        hsaId.setRoot(HSA_CODE_SYSTEM);
        hsaId.setExtension(id);
        return hsaId;
    }

    private static JAXBElement<TimePeriodType> aPeriod(InternalLocalDateInterval nedsattning) {
        TimePeriodType period = new TimePeriodType();
        period.setStart(nedsattning.fromAsLocalDate().toLocalDateTime(LocalTime.MIDNIGHT));
        period.setEnd(nedsattning.tomAsLocalDate().toLocalDateTime(LocalTime.MIDNIGHT));
        return new JAXBElement<TimePeriodType>(new QName("urn:riv:clinicalprocess:healthcond:certificate:types:2", "timePeriod"),
                TimePeriodType.class, null, period);
    }

    private static JAXBElement<CVType> aCV(String codeSystem, String code, String displayName) {
        CVType cv = new CVType();
        cv.setCodeSystem(codeSystem);
        cv.setCode(code);
        cv.setDisplayName(displayName);
        return new JAXBElement<CVType>(new QName("urn:riv:clinicalprocess:healthcond:certificate:types:2", "cv"), CVType.class, null, cv);
    }

    private static class SvarBuilder {
        private String id;
        private List<Delsvar> delSvars = new ArrayList<>();

        public SvarBuilder(String id) {
            this.id = id;
        }

        public Svar build() {
            Svar svar = new Svar();
            svar.setId(id);
            svar.getDelsvar().addAll(delSvars);
            return svar;
        }

        public SvarBuilder withDelsvar(String delsvarsId, Object content) {
            Delsvar delsvar = new Delsvar();
            delsvar.setId(delsvarsId);
            delsvar.getContent().add(content);
            delSvars.add(delsvar);
            return this;
        }
    }

    private static SvarBuilder aSvar(String id) {
        return new SvarBuilder(id);
    }

}
