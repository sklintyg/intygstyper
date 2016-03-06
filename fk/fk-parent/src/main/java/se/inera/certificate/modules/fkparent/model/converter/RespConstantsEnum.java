package se.inera.certificate.modules.fkparent.model.converter;

public enum RespConstantsEnum {
    GRUNDFORMU_UNDERSOKNING_AV_PATIENT_SVAR_JSON_ID("1", "undersokningAvPatienten"),
    GRUNDFORMU_JOURNALUPPGIFTER_SVAR_JSON_ID("1", "journaluppgifter"),
    GRUNDFORMU_ANHORIGS_BESKRIVNING_SVAR_JSON_ID("1", "anhorigsBeskrivningAvPatienten"), 
    GRUNDFORMU_ANNAT_SVAR_JSON_ID("1", "annatGrundForMU"), 
    
    GRUNDFORMEDICINSKTUNDERLAG_TYP_DELSVAR_JSON_ID("1.1", null),
    GRUNDFORMEDICINSKTUNDERLAG_DATUM_DELSVAR_JSON_ID("1.2", null),
    GRUNDFORMEDICINSKTUNDERLAG_ANNANBESKRIVNING_DELSVAR_JSON_ID("1.2", null),
    
    KANNEDOM_SVAR_JSON_ID("2", "kannedomOmPatient"),
    KANNEDOM_DELSVAR_JSON_ID("2.1", null), 
    
    UNDERLAGFINNS_SVAR_JSON_ID("3", "underlagFinns"), 
    UNDERLAGFINNS_DELSVAR_JSON_ID("3.1", null), 
    
    UNDERLAG_SVAR_JSON_ID("4", "underlag"), 
    UNDERLAG_TYP_DELSVAR_JSON_ID("4.1", null), 
    UNDERLAG_DATUM_DELSVAR_JSON_ID("4.2", null),
    UNDERLAG_HAMTAS_FRAN_DELSVAR_JSON_ID("4.3", null), 
    
    SJUKDOMSFORLOPP_SVAR_JSON_ID("5", "sjukdomsforlopp"),
    SJUKDOMSFORLOPP_DELSVAR_JSON_ID("5.1", null),
    
    DIAGNOS_SVAR_JSON_ID("6", "diagnoser"),
    DIAGNOS_BESKRIVNING_DELSVAR_JSON_ID("6.1", null), 
    DIAGNOS_DELSVAR_JSON_ID("6.2", null), 
    
    DIAGNOSGRUND_SVAR_JSON_ID("7", "diagnosgrund"),
    DIAGNOSGRUND_DELSVAR_JSON_ID("7.1", null), 
    DIAGNOSGRUND_NYBEDOMNING_DELSVAR_JSON_ID("7.2", null), 
    
    FUNKTIONSNEDSATTNING_INTELLEKTUELL_SVAR_JSON_ID("8", "funktionsnedsattningIntellektuell"), 
    FUNKTIONSNEDSATTNING_INTELLEKTUELL_DELSVAR_JSON_ID("8.1", null), 
    
    FUNKTIONSNEDSATTNING_KOMMUNIKATION_SVAR_JSON_ID("9", "funktionsnedsattningKommunikation"), 
    FUNKTIONSNEDSATTNING_KOMMUNIKATION_DELSVAR_JSON_ID("9.1", null), 
    
    FUNKTIONSNEDSATTNING_KONCENTRATION_SVAR_JSON_ID("10", "funktionsnedsattningKoncentration"), 
    FUNKTIONSNEDSATTNING_KONCENTRATION_DELSVAR_JSON_ID("10.1", null), 
    
    FUNKTIONSNEDSATTNING_PSYKISK_SVAR_JSON_ID  ("11", "funktionsnedsattningPsykisk"),
    FUNKTIONSNEDSATTNING_PSYKISK_DELSVAR_JSON_ID ( "11.1", null),
    
    FUNKTIONSNEDSATTNING_SYNHORSELTAL_SVAR_JSON_ID ( "12", "funktionsnedsattningSynHorselTal"),
    FUNKTIONSNEDSATTNING_SYNHORSELTAL_DELSVAR_JSON_ID ( "12.1", null),
    
    FUNKTIONSNEDSATTNING_BALANSKOORDINATION_SVAR_JSON_ID ( "13", "funktionsnedsattningBalansKoordination"),
    FUNKTIONSNEDSATTNING_BALANSKOORDINATION_DELSVAR_JSON_ID ( "13.1", null),
    
    FUNKTIONSNEDSATTNING_ANNAN_SVAR_JSON_ID ( "14", "funktionsnedsattningAnnan"),
    FUNKTIONSNEDSATTNING_ANNAN_DELSVAR_JSON_ID ( "14.1", null),
    
    AKTIVITETSBEGRANSNING_SVAR_JSON_ID ( "17", "aktivitetsbegransning"),
    AKTIVITETSBEGRANSNING_DELSVAR_JSON_ID ( "17.1", null),
    
    AVSLUTADBEHANDLING_SVAR_JSON_ID ( "18", "avslutadBehandling"),
    AVSLUTADBEHANDLING_DELSVAR_JSON_ID ( "18.1", null),
    
    PAGAENDEBEHANDLING_SVAR_JSON_ID ( "19", "pagaendeBehandling"),
    PAGAENDEBEHANDLING_DELSVAR_JSON_ID ( "19.1", null),
    
    PLANERADBEHANDLING_SVAR_JSON_ID ( "20", "planeradBehandling"),
    PLANERADBEHANDLING_DELSVAR_JSON_ID ( "20.1", null),
    
    SUBSTANSINTAG_SVAR_JSON_ID ( "21", "substansintag"),
    SUBSTANSINTAG_DELSVAR_JSON_ID ( "21.1", null),
    
    MEDICINSKAFORUTSATTNINGARFORARBETE_SVAR_JSON_ID ( "22", "medicinskaForutsattningarForArbete"),
    MEDICINSKAFORUTSATTNINGARFORARBETE_DELSVAR_JSON_ID ( "22.1", null),
    
    AKTIVITETSFORMAGA_SVAR_JSON_ID ( "23", "aktivitetsFormaga"),
    FORMAGA_TROTS_BEGRANSNING_SVAR_JSON_ID ( "23", "formagaTrotsBegransning"),
    AKTIVITETSFORMAGA_DELSVAR_JSON_ID ( "23.1", null),
    FORMAGA_TROTS_BEGRANSNING_DELSVAR_JSON_ID ( "23.1", "formagaTrotsBegransning"),
    
    OVRIGT_SVAR_JSON_ID ( "25", "ovrigt"),
    OVRIGT_DELSVAR_JSON_ID ( "25.1", "ovrigt"),
    
    KONTAKT_ONSKAS_SVAR_JSON_ID ( "26", "kontaktMedFk"),
    KONTAKT_ONSKAS_DELSVAR_JSON_ID ( "26.1", "kontaktMedFk"),
    ANLEDNING_TILL_KONTAKT_DELSVAR_JSON_ID ( "26.2", "anledningTillKontakt"),
    
    TYP_AV_SYSSELSATTNING_SVAR_JSON_ID ( "28", "sysselsattning"),
    TYP_AV_SYSSELSATTNING_DELSVAR_JSON_ID ( "28.1", "sysselsattning"),
     
    NUVARANDE_ARBETE_SVAR_JSON_ID ( "29", "nuvarandeArbete"),
    NUVARANDE_ARBETE_DELSVAR_JSON_ID ( "29.1", "nuvarandeArbete"),
    
    ARBETSMARKNADSPOLITISKT_PROGRAM_SVAR_JSON_ID ( "30", "arbetsmarknadspolitisktProgram"),
    ARBETSMARKNADSPOLITISKT_PROGRAM_DELSVAR_JSON_ID ( "30.1", null),
    
    BEHOV_AV_SJUKSKRIVNING_SVAR_JSON_ID ( "32", "sjukskrivningar"),
    BEHOV_AV_SJUKSKRIVNING_NIVA_DELSVARSVAR_JSON_ID ( "32.1", null),
    BEHOV_AV_SJUKSKRIVNING_PERIOD_DELSVARSVAR_JSON_ID ( "32.2", null),
    
    ARBETSTIDSFORLAGGNING_SVAR_JSON_ID ( "33", "arbetstidsforlaggning"),
    ARBETSTIDSFORLAGGNING_OM_DELSVAR_JSON_ID ( "33.1", "arbetstidsforlaggning"), 
    ARBETSTIDSFORLAGGNING_MOTIVERING_SVAR_JSON_ID ( "33.2", "arbetstidsforlaggningMotivering"),
    
    ARBETSRESOR_SVAR_JSON_ID ( "34", "arbetsresor"),
    ARBETSRESOR_OM_DELSVAR_JSON_ID ( "34.1", "arbetsresor"),
    
    FUNKTIONSNEDSATTNING_SVAR_JSON_ID ( "35", "funktionsnedsattning"),
    FUNKTIONSNEDSATTNING_DELSVAR_JSON_ID ( "35.1", null),
    
    FORSAKRINGSMEDICINSKT_BESLUTSSTOD_SVAR_JSON_ID ( "37", "forsakringsmedicinsktBeslutsstod"),
    FORSAKRINGSMEDICINSKT_BESLUTSSTOD_DELSVAR_JSON_ID ( "37.1", null),
     
    PROGNOS_SVAR_JSON_ID ( "39", "prognos"),
    PROGNOS_BESKRIVNING_DELSVAR_JSON_ID ( "39.1", null),
    PROGNOS_FORTYDLIGANDE_DELSVAR_JSON_ID ( "39.2", null),
     
    ARBETSLIVSINRIKTADE_ATGARDER_SVAR_JSON_ID ( "40", "arbetslivsinriktadeAtgarder"),
    ARBETSLIVSINRIKTADE_ATGARDER_VAL_DELSVAR_JSON_ID ( "40.1", null),
    ARBETSLIVSINRIKTADE_ATGARDER_AKTUELLT_BESKRIVNING_DELSVAR_JSON_ID ( "40.2", "arbetslivsinriktadeAtgarderAktuelltBeskrivning"),
    ARBETSLIVSINRIKTADE_ATGARDER_EJ_AKTUELLT_BESKRIVNING_DELSVAR_JSON_ID ( "40.3", "arbetslivsinriktadeAtgarderEjAktuelltBeskrivning");
    
    
    
    public final String id;
    public final String namn;
    
    public static final String TEST = "";
    public static final String[] TEST1 = {"1", "2"};

    RespConstantsEnum(String frageId, String frageNamn) {
        this.id = frageId;
        this.namn = frageNamn;
    }
}
