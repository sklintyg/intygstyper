package se.inera.certificate.modules.sjukersattning.model.converter;

import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.*;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.joda.time.LocalDateTime;

import se.inera.certificate.model.common.internal.HoSPersonal;
import se.inera.certificate.model.common.internal.Vardenhet;
import se.inera.certificate.model.converter.util.ConverterException;
import se.inera.certificate.modules.sjukersattning.model.internal.*;
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
        skapadAv.setForskrivarkod(sourceSkapadAv.getForskrivarKod());
        skapadAv.setEnhet(getEnhet(sourceSkapadAv.getVardenhet()));
        for (String sourceBefattning : sourceSkapadAv.getBefattningar()) {
            Befattning befattning = new Befattning();
            befattning.setCodeSystem(BEFATTNING_CODE_SYSTEM);
            befattning.setCode(sourceBefattning);
            skapadAv.getBefattning().add(befattning);
        }
        for (String sourceKompetens : sourceSkapadAv.getSpecialiteter()) {
            Specialistkompetens kompetens = new Specialistkompetens();
            kompetens.setCode(sourceKompetens);
            skapadAv.getSpecialistkompetens().add(kompetens);
        }
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
        vardenhet.setEpost(sourceVardenhet.getEpost());
        vardenhet.setVardgivare(getVardgivare(sourceVardenhet.getVardgivare()));
        vardenhet.setArbetsplatskod(getArbetsplatsKod(sourceVardenhet.getArbetsplatsKod()));
        return vardenhet;
    }

    private static ArbetsplatsKod getArbetsplatsKod(String sourceArbetsplatsKod) {
        ArbetsplatsKod arbetsplatsKod = new ArbetsplatsKod();
        arbetsplatsKod.setRoot(ARBETSPLATSKOD_CODE_SYSTEM);
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
        personId.setExtension(sourcePatient.getPersonId().getPersonnummer().replaceAll("-", ""));
        patient.setPersonId(personId);
        patient.setPostadress(sourcePatient.getPostadress());
        patient.setPostnummer(sourcePatient.getPostnummer());
        patient.setPostort(sourcePatient.getPostort());
        return patient;
    }

    private static IntygId getIntygsId(SjukersattningUtlatande source) {
        IntygId intygId = new IntygId();
        intygId.setRoot(source.getGrundData().getSkapadAv().getVardenhet().getEnhetsid());
        intygId.setExtension(source.getId());
        return intygId;
    }

    private static TypAvIntyg getTypAvIntyg(SjukersattningUtlatande source) {
        TypAvIntyg typAvIntyg = new TypAvIntyg();
        typAvIntyg.setCode(source.getTyp());
        typAvIntyg.setCodeSystem(CERTIFICATE_CODE_SYSTEM);
        typAvIntyg.setDisplayName(source.getTyp());
        return typAvIntyg;
    }

    private static List<Svar> getSvar(SjukersattningUtlatande source) {
        List<Svar> svars = new ArrayList<>();

        if (source.getUndersokningAvPatienten() != null) {
            svars.add(aSvar(REFERENS_SVAR_ID).
                    withDelsvar(REFERENSTYP_DELSVAR_ID, aCV(REFERENS_CODE_SYSTEM, Integer.toString(UNDERSOKNING_AV_PATIENT))).
                    withDelsvar(REFERENSDATUM_DELSVAR_ID, source.getUndersokningAvPatienten().asLocalDate().toString()).build());
        }
        if (source.getTelefonkontaktMedPatienten() != null) {
            svars.add(aSvar(REFERENS_SVAR_ID).
                    withDelsvar(REFERENSTYP_DELSVAR_ID, aCV(REFERENS_CODE_SYSTEM, Integer.toString(TELEFONKONTAKT))).
                    withDelsvar(REFERENSDATUM_DELSVAR_ID, source.getTelefonkontaktMedPatienten().asLocalDate().toString()).build());
        }
        if (source.getJournaluppgifter() != null) {
            svars.add(aSvar(REFERENS_SVAR_ID).
                    withDelsvar(REFERENSTYP_DELSVAR_ID, aCV(REFERENS_CODE_SYSTEM, Integer.toString(JOURNALUPPGIFTER))).
                    withDelsvar(REFERENSDATUM_DELSVAR_ID, source.getJournaluppgifter().asLocalDate().toString()).build());
        }
        if (source.getKannedomOmPatient() != null) {
            svars.add(aSvar(OVRIGKANNEDOM_SVAR_ID).
                    withDelsvar(OVRIGKANNEDOM_DELSVAR_ID, source.getKannedomOmPatient().asLocalDate().toString()).build());
        }

        for (Underlag underlag : source.getUnderlag()) {
            svars.add(
                    aSvar(UNDERLAG_SVAR_ID).withDelsvar(UNDERLAG_TYP_DELSVAR_ID,
                            aCV(UNDERLAG_CODE_SYSTEM, Integer.toString(underlag.getTyp().getId()))).
                            withDelsvar(UNDERLAG_DATUM_DELSVAR_ID,
                                    underlag.getDatum() != null ? underlag.getDatum().asLocalDate().toString() : null).
                            withDelsvar(UNDERLAG_BILAGA_DELSVAR_ID,
                                    underlag.getBilaga() != null ? underlag.getBilaga().toString() : null).build());

        }

        for (int i = 0; i < source.getDiagnoser().size(); i++) {
            Diagnos diagnos = source.getDiagnoser().get(i);
            if (i == 0) {
                svars.add(aSvar(HUVUDSAKLIG_ORSAK_SVAR_ID).
                        withDelsvar(DIAGNOS_DELSVAR_ID, aCV(diagnos.getDiagnosKodSystem(), diagnos.getDiagnosKod())).
                        withDelsvar(DIAGNOS_BESKRIVNING_DELSVAR_ID, diagnos.getDiagnosBeskrivning()).build());
            } else {
                svars.add(aSvar(YTTERLIGARE_ORSAK_SVAR_ID).withDelsvar(YTTERLIGARE_ORSAK_DELSVAR_ID,
                        aCV(diagnos.getDiagnosKodSystem(), diagnos.getDiagnosKod())).
                        withDelsvar(YTTERLIGARE_ORSAK_BESKRIVNING_DELSVAR_ID, diagnos.getDiagnosBeskrivning()).build());
            }

        }

        svars.add(aSvar(DIAGNOSTISERING_SVAR_ID).
                withDelsvar(DIAGNOSTISERING_DELSVAR_ID, source.getDiagnostisering()).build());
        if (source.getNyBedomningDiagnos() != null) {
            svars.add(aSvar(NYBEDOMNING_SVAR_ID).
                    withDelsvar(NYBEDOMNING_DELSVAR_ID, source.getNyBedomningDiagnos().toString()).build());
        }

        for (BehandlingsAtgard atgard : source.getAtgarder()) {
            svars.add(aSvar(YTTERLIGARE_ORSAK_SVAR_ID).withDelsvar(YTTERLIGARE_ORSAK_DELSVAR_ID,
                    aCV(atgard.getAtgardsKodSystem(), atgard.getAtgardsKod())).
                    withDelsvar(YTTERLIGARE_ORSAK_BESKRIVNING_DELSVAR_ID, atgard.getAtgardsBeskrivning()).build());
        }
        for (Funktionsnedsattning funktionsnedsattning : source.getFunktionsnedsattningar()) {
            svars.add(aSvar(FUNKTIONSNEDSATTNING_SVAR_ID).
                    withDelsvar(FUNKTIONSNEDSATTNING_BESKRIVNING_DELSVAR_ID, funktionsnedsattning.getBeskrivning()).
                    withDelsvar(FUNKTIONSNEDSATTNING_FUNKTIONSOMRADE_DELSVAR_ID,
                            aCV(FUNKTIONSOMRADE_CODE_SYSTEM, Integer.toString(funktionsnedsattning.getFunktionsomrade().getId()))).build());
        }
        svars.add(aSvar(AKTIVITETSBEGRANSNING_SVAR_ID).withDelsvar(AKTIVITETSBEGRANSNING_DELSVAR_ID, source.getAktivitetsbegransning()).build());

        if (source.getPagaendeBehandling() != null) {
            svars.add(aSvar(PAGAENDEBEHANDLING_SVAR_ID).withDelsvar(PAGAENDEBEHANDLING_DELSVAR_ID, source.getPagaendeBehandling()).build());
        }

        if (source.getAvslutadBehandling() != null) {
            svars.add(aSvar(AVSLUTADBEHANDLING_SVAR_ID).withDelsvar(AVSLUTADBEHANDLING_DELSVAR_ID, source.getAvslutadBehandling()).build());
        }

        if (source.getPlaneradBehandling() != null) {
            svars.add(aSvar(PLANERADBEHANDLING_SVAR_ID).withDelsvar(PLANERADBEHANDLING_DELSVAR_ID, source.getPlaneradBehandling()).build());
        }

        svars.add(aSvar(AKTIVITETSFORMAGA_SVAR_ID).withDelsvar(AKTIVITETSFORMAGA_DELSVAR_ID, source.getAktivitetsFormaga()).build());

        svars.add(aSvar(PROGNOS_SVAR_ID).withDelsvar(PROGNOS_DELSVAR_ID, source.getPrognos()).build());

        if (source.getOvrigt() != null) {
            svars.add(aSvar(OVRIGT_SVAR_ID).withDelsvar(OVRIGT_DELSVAR_ID, source.getOvrigt()).build());
        }

        if (source.getKontaktMedFk() != null) {
            svars.add(aSvar(KONTAKT_ONSKAS_SVAR_ID).
                    withDelsvar(KONTAKT_ONSKAS_DELSVAR_ID, source.getKontaktMedFk().toString()).
                    withDelsvar(ANLEDNING_TILL_KONTAKT_DELSVAR_ID, source.getAnledningTillKontakt()).build());
        }

        return svars;
    }

    private static HsaId anHsaId(String id) {
        HsaId hsaId = new HsaId();
        hsaId.setRoot(HSA_CODE_SYSTEM);
        hsaId.setExtension(id);
        return hsaId;
    }

    private static JAXBElement<CVType> aCV(String codeSystem, String code) {
        CVType cv = new CVType();
        cv.setCodeSystem(codeSystem);
        cv.setCode(code);
        return new JAXBElement<>(new QName("urn:riv:clinicalprocess:healthcond:certificate:types:2", "cv"), CVType.class, null, cv);
    }

    private static SvarBuilder aSvar(String id) {
        return new SvarBuilder(id);
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

}
