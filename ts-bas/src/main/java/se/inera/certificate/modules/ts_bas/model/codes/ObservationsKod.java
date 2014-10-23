package se.inera.certificate.modules.ts_bas.model.codes;

import static se.inera.certificate.modules.ts_bas.model.codes.Kodverk.ICD_10;
import static se.inera.certificate.modules.ts_bas.model.codes.Kodverk.OBSERVATIONER;
import static se.inera.certificate.modules.ts_bas.model.codes.Kodverk.SNOMED_CT;
import se.inera.certificate.model.Kod;
import se.inera.certificate.model.common.codes.CodeConverter;
import se.inera.certificate.model.common.codes.CodeSystem;

public enum ObservationsKod implements CodeSystem {

    /** Synfältsdefekter (H53.4). */
    SYNFALTSDEFEKTER("H53.4", "Synfältsdefekter", ICD_10),

    /** Diplopi (H53.2). */
    DIPLOPI("H53.2", "Diplopi", ICD_10),

    /** Nystagmus och andra oregelbundna ögonrörelser (H55.9). */
    NYSTAGMUS_MM("H55.9", "Nystagmus och andra oregelbundna ögonrörelser", ICD_10),

    /** Nattblindhet (H53.6). */
    NATTBLINDHET("H53.6", "Nattblindhet", ICD_10),

    /** Progressiv ögonsjukdom (OBS1). */
    PROGRESIV_OGONSJUKDOM("OBS1", "Progressiv ögonsjukdom", OBSERVATIONER),

    /** Ej korrigerad synskärpa (420050001). */
    EJ_KORRIGERAD_SYNSKARPA("420050001", "Ej korrigerad synskärpa", SNOMED_CT),

    /** Korrigerad synskärpa (397535007). */
    KORRIGERAD_SYNSKARPA("397535007", "Korrigerad synskärpa", SNOMED_CT),

    /** Synskärpa, med glasögon (408333000). */
    SYNSKARPA_GLASOGON("408333000", "Synskärpa, med glasögon", SNOMED_CT),

    /** Kontaktlinser (285049007). */
    KONTAKTLINSER("285049007", "Kontaktlinser", SNOMED_CT),

    /** Överraskande anfall av balansrubbningningar eller yrsel (OBS2). */
    ANFALL_BALANSRUBBNING_YRSEL("OBS2", "Överraskande anfall av balansrubbningningar eller yrsel", OBSERVATIONER),

    /** Svårigheter att uppfatta vanlig samtalsstämma på fyta meters avstånd (OBS3). */
    SVARIGHET_SAMTAL_4M("OBS3", "Svårigheter att uppfatta vanlig samtalsstämma på fyta meters avstånd", OBSERVATIONER),

    /**
     * Sjukdom eller funktionsnedsättning som påverkar rörligheten så att fordon inte kan köras på ett trafiksäkert sätt
     * (OBS4).
     */
    FORSAMRAD_RORLIGHET_FRAMFORA_FORDON(
            "OBS4",
            "Sjukdom eller funktionsnedsättning som påverkar rörligheten och som medför att fordon inte kan köras på ett trafiksäkert sätt",
            OBSERVATIONER),

    /** Otillräcklig rörelseförmågan för att kunna hjälpa passagerare in och ut ur fordonet samt med bilbälte (OBS5). */
    FORSAMRAD_RORLIGHET_HJALPA_PASSAGERARE("OBS5",
            "Otillräcklig rörelseförmågan för att kunna hjälpa passagerare in och ut ur fordonet samt med bilbälte",
            OBSERVATIONER),

    /** "Hjärt- och kärlsjukdom som innebär en trafiksäkerhetsrisk (OBS6). */
    HJART_KARLSJUKDOM_TRAFIKSAKERHETSRISK(
            "OBS6",
            "Hjärt- och kärlsjukdom som kan medföra en påtaglig risk för att hjärnans funktioner akut försämras eller som i övrigt innebär en trafiksäkerhetsrisk",
            OBSERVATIONER),

    /**
     * Riskfaktorer för stroke (tidigare stroke eller TIA, förhöjt blodtryck, förmaksflimmer eller kärlmissbildning)
     * (OBS7).
     */
    RISKFAKTORER_STROKE(
            "OBS7",
            "Riskfaktorer för stroke (tidigare stroke eller TIA, förhöjt blodtryck, förmaksflimmer eller kärlmissbildning)",
            OBSERVATIONER),

    /** Tecken på hjärnskada efter trauma, stroke eller anna sjukdom i centrala nervsystemet (OBS8). */
    TECKEN_PA_HJARNSKADA("OBS8",
            "Tecken på hjärnskada efter trauma, stroke eller anna sjukdom i centrala nervsystemet", OBSERVATIONER),

    /** Har diabetes? (73211009). */
    HAR_DIABETES("73211009", "Har diabetes", SNOMED_CT),

    /** Diabetes mellitus typ 1 (E10). */
    DIABETES_TYP_1("E10", "Diabetes mellitus typ 1", ICD_10),

    /** Diabetes mellitus typ 2 (E11). */
    DIABETES_TYP_2("E11", "Diabetes mellitus typ 2", ICD_10),

    /** Diabetiker på Kostbehandling (OBS9). */
    DIABETIKER_KOSTBEHANDLING("OBS9", "Diabetiker på kostbehandling", OBSERVATIONER),

    /** Diabetiker på tablettbehandling (170746002). */
    DIABETIKER_TABLETTBEHANDLING("170746002", "Diabetiker på tablettbehandling", SNOMED_CT),

    /** Diabetiker på enbart kostbehandling (170745003). */
    DIABETIKER_ENBART_KOST("170745003", "Diabetiker på enbart kostbehandling", SNOMED_CT),

    /** Diabetiker på insulinbehandling (170747006). */
    DIABETIKER_INSULINBEHANDLING("170747006", "Diabetiker på insulinbehandling", SNOMED_CT),

    /** Tecken på neurologisk sjukdom (407624006). */
    TECKEN_PA_NEUROLOGISK_SJUKDOM("407624006", "Tecken på neurologisk sjukdom", SNOMED_CT),

    /** Epilepsi, ospecificerad (G40.9). */
    EPILEPSI("G40.9", "Epilepsi, ospecificerad", ICD_10),

    /** Nedsatt njurfunktion som kan innebära trafiksäkerhetsrisk (OBS11). */
    NEDSATT_NJURFUNKTION_TRAFIKSAKERHETSRISK("OBS11", "Nedsatt njurfunktion som kan innebära trafiksäkerhetsrisk",
            OBSERVATIONER),

    /** Tecken på sviktande kognitiv funktion (OBS12). */
    SVIKTANDE_KOGNITIV_FUNKTION("OBS12", "Tecken på sviktande kognitiv funktion", OBSERVATIONER),

    /** Tecken på sömn- eller vakenhetsstörning (OBS13). */
    SOMN_VAKENHETSSTORNING("OBS13", "Tecken på sömn- eller vakenhetsstörning", OBSERVATIONER),

    /** Tecken på missbruk av alkohol, narkotika eller läkemedel (OBS14). */
    TECKEN_PA_MISSBRUK("OBS14", "Tecken på missbruk av alkohol, narkotika eller läkemedel", OBSERVATIONER),

    /** Regelbundet läkarordinerat bruk av läkemedel som kan innebära en trafiksäkerhetsrisk (OBS15). */
    LAKEMEDELSANVANDNING_TRAFIKSAKERHETSRISK("OBS15",
            "Regelbundet läkarordinerat bruk av läkemedel som kan innebära en trafiksäkerhetsrisk", OBSERVATIONER),

    /**
     * psykisk sjukdom eller störning, till exempel schizofreni, annan psykos eller bipolär (manodepressiv) sjukdom
     * (OBS16).
     */
    PSYKISK_SJUKDOM(
            "OBS16",
            "psykisk sjukdom eller störning, till exempel schizofreni, annan psykos eller bipolär (manodepressiv) sjukdom",
            OBSERVATIONER),

    /** Förekomst avADHD, ADD, DAMP, Aspergers syndrom eller Tourettes syndrom (OBS17). */
    ADHD_DAMP_MM("OBS17", "Förekomst avADHD, ADD, DAMP, Aspergers syndrom eller Tourettes syndrom", OBSERVATIONER),

    /** Psykisk utvecklingsstörning (129104009). */
    PSYKISK_UTVECKLINGSSTORNING("129104009", "Psykisk utvecklingsstörning", SNOMED_CT),

    /** Förekomst av stadigvarande medicinering (OBS18). */
    STADIGVARANDE_MEDICINERING("OBS18", "Förekomst av stadigvarande medicinering", OBSERVATIONER);

    private final String code;

    private final String description;

    private final String codeSystem;

    private final String codeSystemName;

    private final String codeSystemVersion;

    private ObservationsKod(String code, String description, Kodverk kodverk) {
        this.code = code;
        this.description = description;
        this.codeSystem = kodverk.getCodeSystem();
        this.codeSystemName = kodverk.getCodeSystemName();
        this.codeSystemVersion = kodverk.getCodeSystemVersion();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCode() {
        return code;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCodeSystem() {
        return codeSystem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCodeSystemName() {
        return codeSystemName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCodeSystemVersion() {
        return codeSystemVersion;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean matches(Kod kod) {
        return CodeConverter.matches(this, kod);
    }
}
