package se.inera.certificate.modules.sjukersattning.model.converter;

import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.REFERENSDATUM_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.REFERENSTYP_DELSVAR_ID;
import static se.inera.certificate.modules.fkparent.model.converter.RespConstants.REFERENS_SVAR_ID;

import org.joda.time.LocalDate;
import se.inera.certificate.model.InternalDate;
import se.inera.certificate.model.common.internal.*;
import se.inera.certificate.model.converter.util.ConverterException;
import se.inera.certificate.modules.fkparent.model.converter.RespConstants;
import se.inera.certificate.modules.sjukersattning.model.internal.SjukersattningUtlatande;
import se.inera.certificate.modules.support.api.dto.CertificateMetaData;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.CVType;
import se.riv.clinicalprocess.healthcond.certificate.v2.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v2.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v2.Svar.Delsvar;

import javax.xml.bind.JAXBElement;

public class TransportToInternal {

    public static SjukersattningUtlatande convert(Intyg source) throws ConverterException {
        SjukersattningUtlatande utlatande = new SjukersattningUtlatande();
        utlatande.setId(source.getIntygsId().getRoot());
        utlatande.setGrundData(getGrundData(source));
        setSvar(utlatande, source);
        return utlatande;
    }

    private static void setSvar(SjukersattningUtlatande utlatande, Intyg source) {
        for (Svar svar : source.getSvar()) {
            switch (svar.getId()) {
                case REFERENS_SVAR_ID:
                    InternalDate referensDatum = null;
                    RespConstants.ReferensTyp referensTyp = null;
                    for (Delsvar delsvar : svar.getDelsvar()) {
                        switch (delsvar.getId()) {
                            case REFERENSDATUM_DELSVAR_ID:
                                referensDatum = new InternalDate(getSvarContent(delsvar, String.class));
                                break;
                            case REFERENSTYP_DELSVAR_ID:
                                String referensTypString = getSvarContent(delsvar, CVType.class).getCode();
                                referensTyp = RespConstants.ReferensTyp.byTransport(referensTypString);
                                break;
                        }
                    }

                    switch (referensTyp) {
                        case UNDERSOKNING:
                            utlatande.setUndersokningAvPatienten(referensDatum);
                            break;
                        case TELEFONKONTAKT:
                            utlatande.setTelefonkontaktMedPatienten(referensDatum);
                            break;
                        case JOURNAL:
                            utlatande.setJournaluppgifter(referensDatum);
                            break;

                    }
            }
        }
    }

    private static <T> T getSvarContent(Delsvar delsvar, Class<T> clazz) {
        Object content = delsvar.getContent().get(0);
        if (content instanceof JAXBElement) {
            return ((JAXBElement<T>) content).getValue();
        }
        return (T) content;
    }

    private static GrundData getGrundData(Intyg source) {
        GrundData grundData = new GrundData();
        grundData.setPatient(getPatient(source));
        grundData.setSkapadAv(getSkapadAv(source));
        return grundData;
    }

    private static HoSPersonal getSkapadAv(Intyg source) {
        HoSPersonal personal = new HoSPersonal();
        personal.setPersonId(source.getSkapadAv().getPersonalId().getExtension());
        personal.setFullstandigtNamn(source.getSkapadAv().getFullstandigtNamn());
        personal.setForskrivarKod(source.getSkapadAv().getForskrivarkod());
        personal.setVardenhet(getVardenhet(source));
        return personal;
    }

    private static Vardenhet getVardenhet(Intyg source) {
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setPostort(source.getSkapadAv().getEnhet().getPostort());
        vardenhet.setPostadress(source.getSkapadAv().getEnhet().getPostadress());
        vardenhet.setPostnummer(source.getSkapadAv().getEnhet().getPostnummer());
        vardenhet.setEpost(source.getSkapadAv().getEnhet().getEpost());
        vardenhet.setEnhetsid(source.getSkapadAv().getEnhet().getEnhetsId().getExtension());
        vardenhet.setArbetsplatsKod(source.getSkapadAv().getEnhet().getArbetsplatskod().getExtension());
        vardenhet.setEnhetsnamn(source.getSkapadAv().getEnhet().getEnhetsnamn());
        vardenhet.setTelefonnummer(source.getSkapadAv().getEnhet().getTelefonnummer());
        vardenhet.setVardgivare(getVardgivare(source));

        return vardenhet;
    }

    private static Vardgivare getVardgivare(Intyg source) {
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarid(source.getSkapadAv().getEnhet().getVardgivare().getVardgivareId().getExtension());
        vardgivare.setVardgivarnamn(source.getSkapadAv().getEnhet().getVardgivare().getVardgivarnamn());
        return vardgivare;
    }

    private static Patient getPatient(Intyg source) {
        Patient patient = new Patient();
        patient.setEfternamn(source.getPatient().getEfternamn());
        patient.setFornamn(source.getPatient().getFornamn());
        patient.setMellannamn(source.getPatient().getMellannamn());
        patient.setPostort(source.getPatient().getPostort());
        patient.setPostnummer(source.getPatient().getPostnummer());
        patient.setPostadress(source.getPatient().getPostadress());
        patient.setPersonId(source.getPatient().getPersonId().getExtension());
        return patient;
    }

    public static CertificateMetaData getMetaData(Intyg source) {
        CertificateMetaData metaData = new CertificateMetaData();
        return metaData;
    }

}
