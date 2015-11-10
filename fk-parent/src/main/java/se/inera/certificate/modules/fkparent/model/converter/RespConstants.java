package se.inera.certificate.modules.fkparent.model.converter;

public class RespConstants {
    public static final int UNDERSOKNING_AV_PATIENT = 1;
    public static final int TELEFONKONTAKT = 2;
    public static final int JOURNALUPPGIFTER = 3;

    public static final int ARBETE = 1;
    public static final int ARBETSLOSHET = 2;
    public static final int FORALDRALEDIGHET = 3;
    public static final int STUDIER = 4;
    public static final int ARBETSMARKNADSPROGRAM = 5;

    public static final String CERTIFICATE_CODE_SYSTEM = "f6fb361a-e31d-48b8-8657-99b63912dd9b";
    public static final String REFERENS_CODE_SYSTEM = "KV_FKMU_0001";
    public static final String HSA_CODE_SYSTEM = "1.2.752.129.2.1.4.1";
    public static final String PERSON_ID_CODE_SYSTEM = "1.2.752.129.2.1.3.3";
    public static final String FUNKTIONSOMRADE_CODE_SYSTEM = "KV_FKMU_0XXX";
    public static final String UNDERLAG_CODE_SYSTEM = "KV_FKMU_1XXX";
    public static final String BEHANDLINGSATGARD_CODE_SYSTEM = "TODO-123"; // TODO
    public static final String BEFATTNING_CODE_SYSTEM = "1.2.752.129.2.2.1.4";
    public static final String SPECIALISTKOMPETENS_CODE_SYSTEM = "TODO-321"; // TODO
    public static final String ARBETSPLATSKOD_CODE_SYSTEM = "1.2.752.29.4.71";

    public static final String SMITTA_SVAR_ID = "2";
    public static final String SMITTA_DELSVAR_ID = "2.1";
    public static final String SYSSELSATTNING_SVAR_ID = "1";
    public static final String SYSSELSATTNING_TYP_DELSVAR = "1.1";
    public static final String SYSSELSATTNING_BESKRIVNING_DELSVAR_ID = "1.2";
    public static final String REFERENS_SVAR_ID = "10";
    public static final String REFERENSTYP_DELSVAR_ID = "10.1";
    public static final String OVRIGKANNEDOM_SVAR_ID = "27";
    public static final String OVRIGKANNEDOM_DELSVAR_ID = "27.1";
    public static final String REFERENSDATUM_DELSVAR_ID = "10.2";
    public static final String UNDERLAG_SVAR_ID = "28";
    public static final String UNDERLAG_TYP_DELSVAR_ID = "28.1";
    public static final String UNDERLAG_DATUM_DELSVAR_ID = "28.2";
    public static final String UNDERLAG_BILAGA_DELSVAR_ID = "28.3";
    public static final String HUVUDSAKLIG_ORSAK_SVAR_ID = "3";
    public static final String DIAGNOS_DELSVAR_ID = "3.1";
    public static final String DIAGNOS_BESKRIVNING_DELSVAR_ID = "3.2";
    public static final String YTTERLIGARE_ORSAK_SVAR_ID = "4";
    public static final String YTTERLIGARE_ORSAK_DELSVAR_ID = "4.1";
    public static final String YTTERLIGARE_ORSAK_BESKRIVNING_DELSVAR_ID = "4.2";
    public static final String DIAGNOSTISERING_SVAR_ID = "23";
    public static final String DIAGNOSTISERING_DELSVAR_ID = "23.1";
    public static final String NYBEDOMNING_SVAR_ID = "24";
    public static final String NYBEDOMNING_DELSVAR_ID = "24.1";
    public static final String FUNKTIONSNEDSATTNING_SVAR_ID = "11";
    public static final String FUNKTIONSNEDSATTNING_BESKRIVNING_DELSVAR_ID = "11.1";
    public static final String FUNKTIONSNEDSATTNING_FUNKTIONSOMRADE_DELSVAR_ID = "11.2";
    public static final String AKTIVITETSBEGRANSNING_SVAR_ID = "5";
    public static final String AKTIVITETSBEGRANSNING_DELSVAR_ID = "5.1";
    public static final String PAGAENDEBEHANDLING_SVAR_ID = "12";
    public static final String PAGAENDEBEHANDLING_DELSVAR_ID = "12.1";
    public static final String AVSLUTADBEHANDLING_SVAR_ID = "25";
    public static final String AVSLUTADBEHANDLING_DELSVAR_ID = "25.1";
    public static final String PLANERADBEHANDLING_SVAR_ID = "13";
    public static final String PLANERADBEHANDLING_DELSVAR_ID = "13.1";
    public static final String NEDSATTNING_SVAR_ID = "6";
    public static final String NEDSATTNING_DELSVAR_ID = "6.1";
    public static final String RESSATT_SVAR_ID = "7";
    public static final String RESSATT_DELSVAR_ID = "7.1";
    public static final String REKOMMENDATION_OVERSKRIDER_SVAR_ID = "14";
    public static final String REKOMMENDATION_OVERSKRIDER_DELSVAR_ID = "14.1";
    public static final String AKTIVITETSFORMAGA_SVAR_ID = "16";
    public static final String AKTIVITETSFORMAGA_DELSVAR_ID = "16.1";
    public static final String PROGNOS_SVAR_ID = "17";
    public static final String PROGNOS_DELSVAR_ID = "17.1";
    public static final String OVRIGT_SVAR_ID = "22";
    public static final String OVRIGT_DELSVAR_ID = "22.1";
    public static final String KONTAKT_ONSKAS_SVAR_ID = "21";
    public static final String KONTAKT_ONSKAS_DELSVAR_ID = "21.1";

    public enum ReferensTyp {
        UNDERSOKNING(1), TELEFONKONTAKT(2), JOURNAL(3), UNKNOWN(-1);

        public final int transport;

        ReferensTyp(int transport) {
            this.transport = transport;
        }

        public static ReferensTyp byTransport(String transport) {
            for (ReferensTyp referensTyp : values()) {
                if (referensTyp.transport == Integer.parseInt(transport)) {
                    return referensTyp;
                }
            }
            throw new IllegalArgumentException();
        }
    }

}
