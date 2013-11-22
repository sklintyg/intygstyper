package se.inera.certificate.modules.rli.model.converter;

import static se.inera.certificate.modules.rli.model.codes.HSpersonalKod.HSA_ID;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.certificate.model.HosPersonal;
import se.inera.certificate.model.Id;
import se.inera.certificate.model.Kod;
import se.inera.certificate.model.Observation;
import se.inera.certificate.model.Patient;
import se.inera.certificate.model.Rekommendation;
import se.inera.certificate.model.Utforarroll;
import se.inera.certificate.model.Vardenhet;
import se.inera.certificate.model.Vardgivare;
import se.inera.certificate.modules.rli.model.codes.AktivitetsKod;
import se.inera.certificate.modules.rli.model.codes.CodeConverter;
import se.inera.certificate.modules.rli.model.codes.HSpersonalKod;
import se.inera.certificate.modules.rli.model.codes.ObservationsKod;
import se.inera.certificate.modules.rli.model.codes.UtforarrollKod;
import se.inera.certificate.modules.rli.model.external.Aktivitet;
import se.inera.certificate.modules.rli.model.external.Arrangemang;
import se.inera.certificate.modules.rli.model.external.Utlatande;
import se.inera.certificate.modules.rli.model.internal.wc.HoSPersonal;
import se.inera.certificate.modules.rli.model.internal.wc.KomplikationStyrkt;
import se.inera.certificate.modules.rli.model.internal.wc.OrsakAvbokning;
import se.inera.certificate.modules.rli.model.internal.wc.Undersokning;
import se.inera.certificate.modules.rli.model.internal.wc.Utforare;

public class InternalToExternalConverter {

    private static final Logger LOG = LoggerFactory.getLogger(InternalToExternalConverter.class);

    private static final String PERS_ID_ROOT = "1.2.752.129.2.1.3.1";

    private static final String RLI_INTYGSTYP_CODESYSTEM = "f6fb361a-e31d-48b8-8657-99b63912dd9b";

    public Utlatande convertUtlatandeFromInternalToExternal(
            se.inera.certificate.modules.rli.model.internal.wc.Utlatande source) throws ConverterException {

        LOG.debug("Converting Utlatande '{}' from internal to external", source.getUtlatandeid());

        Utlatande utlatande = new Utlatande();
        utlatande.setId(new Id(source.getUtlatandeidroot(), source.getUtlatandeid()));
        /** Is this valid? */
        utlatande.setTyp(new Kod(RLI_INTYGSTYP_CODESYSTEM, null, null, "RLI"));
        utlatande.setSigneringsdatum(source.getSigneringsdatum());
        utlatande.setSkickatdatum(source.getSkickatdatum());

        utlatande.setSkapadAv(convertHoSPersonal(source.getSkapadAv()));
        utlatande.setPatient(convertPatient(source.getPatient()));
        utlatande.setArrangemang(convertArrangemang(source.getArrangemang()));
        utlatande.getObservationer().addAll(convertObservationer(source.getUndersokning()));
        utlatande.getAktiviteter().addAll(convertAktiviteter(source.getUndersokning()));
        utlatande.getRekommendationer().addAll(convertRekommendationer(source.getRekommendation()));
        utlatande.getKommentarer().addAll(source.getKommentarer());

        return utlatande;

    }

    private List<Rekommendation> convertRekommendationer(
            se.inera.certificate.modules.rli.model.internal.wc.Rekommendation source) {
        Rekommendation rek = new Rekommendation();
        rek.setBeskrivning(source.getBeskrivning());
        rek.setRekommendationskod(CodeConverter.toKod(source.getRekommendationskod()));
        rek.setSjukdomskannedom(CodeConverter.toKod(source.getSjukdomskannedom()));
        return Arrays.asList(rek);
    }

    List<Observation> convertObservationer(Undersokning source) {
        LOG.trace("Converting Observationer");
        List<Observation> obs = new ArrayList<Observation>();

        if (source.getOrsakforavbokning() == OrsakAvbokning.RESENAR_SJUK) {
            Observation o = new Observation();
            o.setObservationskod(CodeConverter.toKod(ObservationsKod.SJUKDOM));
            o.setUtforsAv(convertUtforsAv(source.getUtforsAv()));
            obs.add(o);

        } else if (source.getOrsakforavbokning() == OrsakAvbokning.RESENAR_GRAVID) {
            Observation o1 = new Observation();
            o1.setObservationskod(CodeConverter.toKod(ObservationsKod.KOMPLIKATION_VID_GRAVIDITET));
            o1.setUtforsAv(convertUtforsAv(source.getUtforsAv()));
            obs.add(o1);

            Observation o2 = new Observation();
            o2.setObservationskod(CodeConverter.toKod(ObservationsKod.GRAVIDITET));
            o2.setObservationsperiod(PartialConverter.toPartialInterval(null, source.getGraviditet()
                    .getBeraknatForlossningsdatum()));
            o2.setUtforsAv(convertUtforsAv(source.getUtforsAv()));

            obs.add(o2);
        }
        // TODO: Don't forget ObservationsKod.ANHORIG_SJUK

        return obs;
    }

    private Utforarroll convertUtforsAv(Utforare source) {
        if (source == null) {
            LOG.trace("No utforare found when converting, hence null");
            return null;
        }
        Utforarroll utforsAv = new Utforarroll();

        utforsAv.setUtforartyp(CodeConverter.toKod(source.getUtforartyp()));
        utforsAv.setAntasAv(convertHoSPersonal(source.getAntasAv()));

        return utforsAv;
    }

    List<Aktivitet> convertAktiviteter(Undersokning source) throws ConverterException {
        List<Aktivitet> aktiviteter = new ArrayList<Aktivitet>();

        if (source.getOrsakforavbokning() == OrsakAvbokning.RESENAR_SJUK) {
            buildAktivitetSjuk(aktiviteter, source);
        } else if (source.getOrsakforavbokning() == OrsakAvbokning.RESENAR_GRAVID) {
            buildAktiviteterGravid(aktiviteter, source);
        }
        return aktiviteter;
    }

    /**
     * Create necessary Aktiviteter for a pregnant person.
     * 
     * @param aktiviteter
     *            ArrayList of Aktiviteter
     * @param source
     *            se.inera.certificate.modules.rli.model.internal.Undersokning
     * @throws ConverterException
     */
    private void buildAktiviteterGravid(List<Aktivitet> aktiviteter, Undersokning source) throws ConverterException {
        /** Create first Aktivitet */
        Aktivitet akt1 = new Aktivitet();
        akt1.setAktivitetskod(CodeConverter.toKod(AktivitetsKod.FORSTA_UNDERSOKNING));

        String undersokningsDatum = source.getForstaUndersokningsdatum();
        if (undersokningsDatum != null) {
            akt1.setAktivitetstid(PartialConverter.toPartialInterval(undersokningsDatum, undersokningsDatum));
        }

        /** Add a Plats if we're dealing with ForstaUndersokning */
        if (source.getForstaUndersokningsplats() != null) {
            akt1.setPlats(source.getForstaUndersokningsplats());
        }
        aktiviteter.add(akt1);

        /** Create second Aktivitet */
        Aktivitet akt2 = new Aktivitet();
        akt2.setAktivitetskod(CodeConverter.toKod(AktivitetsKod.KLINISK_UNDERSOKNING));
        if (undersokningsDatum == null) {
            throw new ConverterException("Mandatory date in KLINISK_UNDERSOKNING missing, aborting");
        }
        akt2.setAktivitetstid(PartialConverter.toPartialInterval(undersokningsDatum, undersokningsDatum));

        /** Determine what kind of location to add for second Aktivitet */
        // TODO: This should always be UtforsVid no?

        if (source.getUtforsVid() != null) {
            akt1.setUtforsVid(buildVardenhet(source.getUtforsVid()));
            akt1.getBeskrivsAv().addAll(buildUtforarrollFromKomplikationStyrkt(KomplikationStyrkt.AV_HOS_PERSONAL));
        }
        aktiviteter.add(akt2);

    }

    /**
     * 
     * Create necessary Aktivitet for a sick person
     * 
     * @param aktiviteter
     *            ArrayList of Aktiviteter
     * @param source
     *            se.inera.certificate.modules.rli.model.internal.Undersokning
     * @throws ConverterException
     */
    private void buildAktivitetSjuk(List<Aktivitet> aktiviteter, Undersokning source) throws ConverterException {
        Aktivitet akt = new Aktivitet();
        akt.setAktivitetskod(CodeConverter.toKod(AktivitetsKod.KLINISK_UNDERSOKNING));

        if (source.getUndersokningsdatum() == null) {
            throw new ConverterException("Mandatory date in KLINISK_UNDERSOKNING missing, aborting");
        }

        String undersokningsdatum = source.getUndersokningsdatum();
        akt.setAktivitetstid(PartialConverter.toPartialInterval(undersokningsdatum, undersokningsdatum));

        if (source.getKomplikationstyrkt() == KomplikationStyrkt.AV_PATIENT && source.getUndersokningsplats() != null) {
            akt.setPlats(source.getUndersokningsplats());

        } else if (source.getKomplikationstyrkt() == KomplikationStyrkt.AV_HOS_PERSONAL) {
            akt.setUtforsVid(buildVardenhet(source.getUtforsVid()));
            akt.getBeskrivsAv().addAll(buildUtforarrollFromKomplikationStyrkt(KomplikationStyrkt.AV_HOS_PERSONAL));

        }
        aktiviteter.add(akt);

    }

    private List<Utforarroll> buildUtforarrollFromKomplikationStyrkt(KomplikationStyrkt kS) throws ConverterException {
        List<Utforarroll> list = new ArrayList<Utforarroll>();
        Utforarroll utf = new Utforarroll();
        utf.setUtforartyp(buildUtforarKodFromKomplikationStyrkt(kS));
        return list;
    }

    private Kod buildUtforarKodFromKomplikationStyrkt(KomplikationStyrkt kS) throws ConverterException {
        switch (kS) {
        case AV_HOS_PERSONAL:
            return CodeConverter.toKod(UtforarrollKod.AV_HOS_PERSONAL);
        case AV_PATIENT:
            return CodeConverter.toKod(UtforarrollKod.AV_PATIENT);
        }

        throw new ConverterException("Unknown komplikation: " + kS);
    }

    private Vardenhet buildVardenhet(se.inera.certificate.modules.rli.model.internal.wc.Vardenhet source) {
        if (source == null) {
            LOG.trace("Internal vardenhet was null");
            return null;
        }
        // TODO Add HSA-ID for Vardenhet to internal model (and other stuff we want to be able to re-create)
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setNamn(source.getEnhetsnamn());
        vardenhet.setEpost(source.getEpost());
        vardenhet.setId(new Id(HSpersonalKod.HSA_ID.getCode(), source.getEnhetsid()));
        vardenhet.setPostadress(source.getPostadress());
        vardenhet.setPostnummer(source.getPostnummer());
        vardenhet.setPostort(source.getPostort());
        vardenhet.setTelefonnummer(source.getTelefonnummer());
        vardenhet.setVardgivare(convertVardgivare(source.getVardgivare()));
        return vardenhet;
    }

    HosPersonal convertHoSPersonal(HoSPersonal source) {
        HosPersonal hsp = new HosPersonal();
        hsp.setNamn(source.getFullstandigtNamn());
        hsp.setId(new Id(HSA_ID.getCodeSystem(), source.getPersonid()));
        hsp.setVardenhet(convertVardenhet(source.getVardenhet()));
        return hsp;
    }

    Vardenhet convertVardenhet(se.inera.certificate.modules.rli.model.internal.wc.Vardenhet source) {
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setEpost(source.getEpost());
        vardenhet.setId(new Id(HSA_ID.getCodeSystem(), source.getEnhetsid()));
        vardenhet.setNamn(source.getEnhetsnamn());
        vardenhet.setPostadress(source.getPostadress());
        vardenhet.setPostnummer(source.getPostnummer());
        vardenhet.setPostort(source.getPostort());
        vardenhet.setTelefonnummer(source.getTelefonnummer());
        vardenhet.setVardgivare(convertVardgivare(source.getVardgivare()));

        return vardenhet;
    }

    Vardgivare convertVardgivare(se.inera.certificate.modules.rli.model.internal.wc.Vardgivare source) {
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setId(new Id(HSA_ID.getCodeSystem(), source.getVardgivarid()));
        vardgivare.setNamn(source.getVardgivarnamn());
        return vardgivare;
    }

    Patient convertPatient(se.inera.certificate.modules.rli.model.internal.wc.Patient source) {
        Patient patient = new Patient();
        patient.setEfternamn(source.getEfternamn());
        patient.getFornamn().add(source.getFornamn());
        patient.setId(new Id(PERS_ID_ROOT, source.getPersonid()));
        patient.setPostadress(source.getPostadress());
        patient.setPostnummer(source.getPostnummer());
        patient.setPostort(source.getPostort());

        return patient;
    }

    Arrangemang convertArrangemang(se.inera.certificate.modules.rli.model.internal.wc.Arrangemang source) {
        Arrangemang arr = new Arrangemang();

        arr.setArrangemangstid(PartialConverter.toPartialInterval(source.getArrangemangsdatum(),
                source.getArrangemangslutdatum()));
        arr.setArrangemangstyp(CodeConverter.toKod(source.getArrangemangstyp()));
        arr.setAvbestallningsdatum(PartialConverter.stringToPartial(source.getAvbestallningsdatum()));
        arr.setBokningsdatum(PartialConverter.stringToPartial(source.getBokningsdatum()));
        arr.setBokningsreferens(source.getBokningsreferens());
        arr.setPlats(source.getPlats());

        return arr;
    }
}
