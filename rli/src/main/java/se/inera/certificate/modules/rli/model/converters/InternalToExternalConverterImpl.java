package se.inera.certificate.modules.rli.model.converters;

import static se.inera.certificate.modules.rli.model.codes.HSpersonalTyp.HSA_ID;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.certificate.model.HosPersonal;
import se.inera.certificate.model.Id;
import se.inera.certificate.model.Kod;
import se.inera.certificate.model.Observation;
import se.inera.certificate.model.PartialInterval;
import se.inera.certificate.model.Patient;
import se.inera.certificate.model.Rekommendation;
import se.inera.certificate.model.Utforarroll;
import se.inera.certificate.model.Vardenhet;
import se.inera.certificate.model.Vardgivare;
import se.inera.certificate.modules.rli.model.codes.AktivitetsKod;
import se.inera.certificate.modules.rli.model.codes.HSpersonalTyp;
import se.inera.certificate.modules.rli.model.codes.ObservationsKod;
import se.inera.certificate.modules.rli.model.codes.RekommendationsKod;
import se.inera.certificate.modules.rli.model.codes.SjukdomsKannedom;
import se.inera.certificate.modules.rli.model.codes.UtforarTypKod;
import se.inera.certificate.modules.rli.model.external.Aktivitet;
import se.inera.certificate.modules.rli.model.external.Arrangemang;
import se.inera.certificate.modules.rli.model.external.Utlatande;

import se.inera.certificate.modules.rli.model.edit.HoSPersonal;
import se.inera.certificate.modules.rli.model.edit.KomplikationStyrkt;
import se.inera.certificate.modules.rli.model.edit.OrsakAvbokning;
import se.inera.certificate.modules.rli.model.edit.Undersokning;
import se.inera.certificate.modules.rli.model.edit.Utforare;

public class InternalToExternalConverterImpl implements InternalToExternalConverter {

    private static final Logger LOG = LoggerFactory.getLogger(InternalToExternalConverterImpl.class);

    private static final String PERS_ID_ROOT = "1.2.752.129.2.1.3.1";

    private static final String RLI_INTYGSTYP_CODESYSTEM = "f6fb361a-e31d-48b8-8657-99b63912dd9b";

    public InternalToExternalConverterImpl() {
        // TODO Auto-generated constructor stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * se.inera.certificate.modules.rli.model.converters.InternalToExternalConverter#convertUtlatandeFromInternalToExternal
     * (se.inera.certificate.modules.rli.model.internal.Utlatande)
     */
    @Override
    public Utlatande convertUtlatandeFromInternalToExternal(
            se.inera.certificate.modules.rli.model.edit.Utlatande source) {

        LOG.debug("Converting Utlatande '{}' from internal to external", source.getUtlatandeid());

        Utlatande utlatande = new Utlatande();

        utlatande.setId(new Id(source.getUtlatandeidroot(), source.getUtlatandeid()));

        /** Is this valid? */
        utlatande.setTyp(new Kod(RLI_INTYGSTYP_CODESYSTEM, null, null, "RLI"));

        utlatande.setSigneringsdatum(source.getSigneringsdatum());

        utlatande.setSkapadAv(convertHoSPersonal(source.getSkapadAv()));

        utlatande.setSkickatdatum(source.getSkickatdatum());

        utlatande.setPatient(convertPatient(source.getPatient()));

        utlatande.setArrangemang(convertArrangemang(source.getArrangemang()));

        utlatande.getObservationer().addAll(convertObservationer(source.getUndersokning()));

        utlatande.getAktiviteter().addAll(convertAktiviteter(source.getUndersokning()));

        utlatande.getRekommendationer().addAll(convertRekommendationer(source.getRekommendation()));

        utlatande.getKommentarer().addAll(source.getKommentarer());

        return utlatande;

    }

    private List<Rekommendation> convertRekommendationer(
            se.inera.certificate.modules.rli.model.edit.Rekommendation source) {
        Rekommendation rek = new Rekommendation();
        rek.setBeskrivning(source.getBeskrivning());
        rek.setRekommendationskod(buildRekommendationsKod(source.getRekommendationskod()));
        rek.setSjukdomskannedom(buildSjukdomskannedomsKod(source.getSjukdomskannedom()));
        return Arrays.asList(rek);
    }

    private Kod buildSjukdomskannedomsKod(SjukdomsKannedom sk) {
        return new Kod(sk.getCodeSystem(), sk.getCodeSystemName(), sk.getCodeSystemVersion(), sk.getCode());
    }

    private Kod buildRekommendationsKod(RekommendationsKod rk) {
        return new Kod(rk.getCodeSystem(), rk.getCodeSystemName(), rk.getCodeSystemVersion(), rk.getCode());
    }

    List<Observation> convertObservationer(Undersokning source) {
        LOG.trace("Converting Observationer");
        List<Observation> obs = new ArrayList<Observation>();

        if (source.getOrsakforavbokning() == OrsakAvbokning.RESENAR_SJUK) {
            Observation o = new Observation();

            o.setObservationskod(buildObservationsKod(ObservationsKod.SJUKDOM));

            o.setUtforsAv(convertUtforsAv(source.getUtforsAv()));
            obs.add(o);

        } else if (source.getOrsakforavbokning() == OrsakAvbokning.RESENAR_GRAVID) {
            Observation o1 = new Observation();
            o1.setObservationskod(buildObservationsKod(ObservationsKod.KOMPLIKATION_VID_GRAVIDITET));
            o1.setUtforsAv(convertUtforsAv(source.getUtforsAv()));
            obs.add(o1);

            Observation o2 = new Observation();
            o2.setObservationskod(buildObservationsKod(ObservationsKod.GRAVIDITET));

            PartialInterval period = new PartialInterval();
            period.setTom(PartialConverter.stringToPartial(source.getGraviditet().getBeraknatForlossningsdatum()));
            o2.setObservationsperiod(period);
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

        utforsAv.setUtforartyp(buildKodFromUtforartyp(source.getUtforartyp()));
        utforsAv.setAntasAv(convertHoSPersonal(source.getAntasAv()));

        return utforsAv;
    }

    private Kod buildKodFromUtforartyp(String typ) {
        Kod kod = null;

        // TODO: This should of course use the ENUM somehow..
        if (typ.equals(UtforarTypKod.AV_HOS_PERSONAL.getCode())) {
            kod = new Kod(UtforarTypKod.AV_HOS_PERSONAL.getCodeSystem(),
                    UtforarTypKod.AV_HOS_PERSONAL.getCodeSystemName(),
                    UtforarTypKod.AV_HOS_PERSONAL.getCodeSystemVersion(), UtforarTypKod.AV_HOS_PERSONAL.getCode());
        } else if (typ.equals(UtforarTypKod.AV_PATIENT.getCode())) {
            kod = new Kod(UtforarTypKod.AV_PATIENT.getCodeSystem(), UtforarTypKod.AV_PATIENT.getCodeSystemName(),
                    UtforarTypKod.AV_PATIENT.getCodeSystemVersion(), UtforarTypKod.AV_PATIENT.getCode());
        }
        return kod;
    }

    private Kod buildObservationsKod(ObservationsKod obsKod) {
        return new Kod(obsKod.getCodeSystem(), obsKod.getCodeSystemName(), obsKod.getCodeSystemVersion(),
                obsKod.getCode());
    }

    List<Aktivitet> convertAktiviteter(Undersokning source) {
        List<Aktivitet> aktiviteter = new ArrayList<Aktivitet>();

        if (source.getOrsakforavbokning() == OrsakAvbokning.RESENAR_SJUK) {
            buildAktivitetSjuk(aktiviteter, source);
        } else if (source.getOrsakforavbokning() == OrsakAvbokning.RESENAR_GRAVID) {
            buildAktiviteterGravid(aktiviteter, source);
        }
        return aktiviteter;
    }

    /**
     * 
     * Create necessary Aktiviteter for a pregnant person
     * 
     * @param aktiviteter
     *            ArrayList of Aktiviteter
     * @param source
     *            se.inera.certificate.modules.rli.model.internal.Undersokning
     */
    private void buildAktiviteterGravid(List<Aktivitet> aktiviteter, Undersokning source) {
        /** Create first Aktivitet */
        Aktivitet akt1 = new Aktivitet();
        akt1.setAktivitetskod(buildAktivitetsKod(AktivitetsKod.FORSTA_UNDERSOKNING));

        PartialInterval period = new PartialInterval();
        if (source.getForstaUndersokningsdatum() != null) {
            period.setFrom(PartialConverter.stringToPartial(source.getForstaUndersokningsdatum()));
            period.setTom(PartialConverter.stringToPartial(source.getForstaUndersokningsdatum()));
            akt1.setAktivitetstid(period);
        }

        /** Determine what kind of location to add for first Aktivitet */
        // TODO: Decide if the first exam should Plats ALWAYS or if this depends on other stuff

        // if (source.getKomplikationstyrkt() == KomplikationStyrkt.AV_PATIENT
        // && source.getForstaUndersokningsplats() != null) {
        //
        // akt1.setPlats(source.getForstaUndersokningsplats());
        //
        // } else if (source.getKomplikationstyrkt() == KomplikationStyrkt.AV_HOS_PERSONAL
        // && source.getForstaUndersokningsplats() != null) {
        //
        // akt1.setUtforsVid(buildVardenhet(source.getUtforsVid()));
        // akt1.getBeskrivsAv().addAll(buildUtforarrollFromKomplikationStyrkt(KomplikationStyrkt.AV_HOS_PERSONAL));
        // }
        if (source.getForstaUndersokningsplats() != null) {
            akt1.setPlats(source.getForstaUndersokningsplats());
        }
        aktiviteter.add(akt1);

        /** Create second Aktivitet */
        Aktivitet akt2 = new Aktivitet();
        akt2.setAktivitetskod(buildAktivitetsKod(AktivitetsKod.KLINISK_UNDERSOKNING));
        if (source.getUndersokningsdatum() == null) {
            LOG.warn("Mandatory date in KLINISK_UNDERSOKNING missing, aborting");
            return;
        }
        PartialInterval period2 = new PartialInterval();
        period2.setFrom(PartialConverter.stringToPartial(source.getUndersokningsdatum()));
        period2.setTom(PartialConverter.stringToPartial(source.getUndersokningsdatum()));
        akt2.setAktivitetstid(period2);

        /** Determine what kind of location to add for second Aktivitet */

        // if (source.getKomplikationstyrkt() == KomplikationStyrkt.AV_PATIENT
        // && source.getUndersokningsplats() != null) {
        //
        // akt1.setPlats(source.getUndersokningsplats());
        //
        // } else if (source.getKomplikationstyrkt() == KomplikationStyrkt.AV_HOS_PERSONAL
        // && source.getUndersokningsplats() != null) {
        //
        // akt1.setUtforsVid(buildVardenhet(source.getUtforsVid()));
        // akt1.getBeskrivsAv().addAll(buildUtforarrollFromKomplikationStyrkt(KomplikationStyrkt.AV_HOS_PERSONAL));
        // }
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
     */
    private void buildAktivitetSjuk(List<Aktivitet> aktiviteter, Undersokning source) {
        Aktivitet akt = new Aktivitet();
        akt.setAktivitetskod(buildAktivitetsKod(AktivitetsKod.KLINISK_UNDERSOKNING));

        if (source.getUndersokningsdatum() == null) {
            LOG.warn("Mandatory date in KLINISK_UNDERSOKNING missing, aborting");
            return;
        }

        PartialInterval period2 = new PartialInterval();
        period2.setFrom(PartialConverter.stringToPartial(source.getUndersokningsdatum()));
        period2.setTom(PartialConverter.stringToPartial(source.getUndersokningsdatum()));
        akt.setAktivitetstid(period2);

        if (source.getKomplikationstyrkt() == KomplikationStyrkt.AV_PATIENT && source.getUndersokningsplats() != null) {
            akt.setPlats(source.getUndersokningsplats());

        } else if (source.getKomplikationstyrkt() == KomplikationStyrkt.AV_HOS_PERSONAL) {
            akt.setUtforsVid(buildVardenhet(source.getUtforsVid()));
            akt.getBeskrivsAv().addAll(buildUtforarrollFromKomplikationStyrkt(KomplikationStyrkt.AV_HOS_PERSONAL));

        }
        aktiviteter.add(akt);

    }

    private List<Utforarroll> buildUtforarrollFromKomplikationStyrkt(KomplikationStyrkt kS) {
        List<Utforarroll> list = new ArrayList<Utforarroll>();
        Utforarroll utf = new Utforarroll();
        utf.setUtforartyp(buildUtforarKodFromKomplikationStyrkt(kS));
        return list;
    }

    private Kod buildUtforarKodFromKomplikationStyrkt(KomplikationStyrkt kS) {
        Kod kod = null;
        switch (kS) {
        case AV_HOS_PERSONAL:
            kod = new Kod(UtforarTypKod.AV_HOS_PERSONAL.getCodeSystem(),
                    UtforarTypKod.AV_HOS_PERSONAL.getCodeSystemName(),
                    UtforarTypKod.AV_HOS_PERSONAL.getCodeSystemVersion(), UtforarTypKod.AV_HOS_PERSONAL.getCode());
        case AV_PATIENT:
            kod = new Kod(UtforarTypKod.AV_PATIENT.getCodeSystem(), UtforarTypKod.AV_PATIENT.getCodeSystemName(),
                    UtforarTypKod.AV_PATIENT.getCodeSystemVersion(), UtforarTypKod.AV_PATIENT.getCode());
        }
        return kod;
    }

    private Vardenhet buildVardenhet(se.inera.certificate.modules.rli.model.edit.Vardenhet source) {
        if (source == null) {
            LOG.trace("Internal vardenhet was null");
            return null;
        }
        // TODO Add HSA-ID for Vardenhet to internal model (and other stuff we want to be able to re-create)
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setNamn(source.getEnhetsnamn());
        vardenhet.setEpost(source.getEpost());
        vardenhet.setId(new Id(HSpersonalTyp.HSA_ID.getCode(), source.getEnhetsid()));
        vardenhet.setPostadress(source.getPostadress());
        vardenhet.setPostnummer(source.getPostnummer());
        vardenhet.setPostort(source.getPostort());
        vardenhet.setTelefonnummer(source.getTelefonnummer());
        vardenhet.setVardgivare(convertVardgivare(source.getVardgivare()));
        return vardenhet;
    }

    private Kod buildAktivitetsKod(AktivitetsKod aktKod) {
        return new Kod(aktKod.getCodeSystem(), aktKod.getCodeSystemName(), aktKod.getCodeSystemVersion(),
                aktKod.getCode());
    }

    HosPersonal convertHoSPersonal(HoSPersonal source) {
        HosPersonal hsp = new HosPersonal();
        hsp.setNamn(source.getFullstandigtNamn());
        hsp.setId(new Id(HSA_ID.getCodeSystem(), source.getPersonid()));
        hsp.setVardenhet(convertVardenhet(source.getVardenhet()));
        return hsp;
    }

    Vardenhet convertVardenhet(se.inera.certificate.modules.rli.model.edit.Vardenhet source) {
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

    Vardgivare convertVardgivare(se.inera.certificate.modules.rli.model.edit.Vardgivare source) {
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setId(new Id(HSA_ID.getCodeSystem(), source.getVardgivarid()));
        vardgivare.setNamn(source.getVardgivarnamn());
        return vardgivare;
    }

    Patient convertPatient(se.inera.certificate.modules.rli.model.edit.Patient source) {
        Patient patient = new Patient();
        patient.setEfternamn(source.getEfternamn());
        patient.getFornamn().add(source.getFornamn());
        patient.getMellannamn().add(source.getMellannamn());
        patient.setId(new Id(PERS_ID_ROOT, source.getPersonid()));
        patient.setPostadress(source.getPostadress());
        patient.setPostnummer(source.getPostnummer());
        patient.setPostort(source.getPostort());

        return patient;
    }

    Arrangemang convertArrangemang(se.inera.certificate.modules.rli.model.edit.Arrangemang source) {
        Arrangemang arr = new Arrangemang();
        PartialInterval arrTid = new PartialInterval();
        arrTid.setFrom(PartialConverter.stringToPartial(source.getArrangemangsdatum()));
        arrTid.setTom(PartialConverter.stringToPartial(source.getArrangemangslutdatum()));

        arr.setArrangemangstid(arrTid);
        arr.setArrangemangstyp(new Kod(source.getArrangemangstyp().getCodeSystem(), source.getArrangemangstyp()
                .getCodeSystemName(), source.getArrangemangstyp().getCodeSystemVersion(), source.getArrangemangstyp()
                .getCode()));
        arr.setAvbestallningsdatum(PartialConverter.stringToPartial(source.getAvbestallningsdatum()));
        arr.setBokningsdatum(PartialConverter.stringToPartial(source.getBokningsdatum()));
        arr.setBokningsreferens(source.getBokningsreferens());
        arr.setPlats(source.getPlats());

        return arr;
    }
}
