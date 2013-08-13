package se.inera.certificate.modules.rli.model.codes;

public enum RekommendationsKod implements ICodeSystem {
	
	REK1("REK1","Jag avråder uttryckligen från resa, då patientens = resenärens tillstånd innebär, att sådan ej kan genomföras utan men."),
	REK2("REK2","Jag avråder ej från resa. Patientens = resenärens tillstånd utgör inget hinder för resa."),
	REK3("REK3","Jag avråder uttryckligen från resa. Komplikationer i graviditeten har uppkommit efter bokning av resan, vilka innebär hinder för resa."),
	REK4("REK4","Jag avråder ej från resa. Graviditeten utgör inget medicinskt hinder för resa."),
	REK5("REK5","Resenären, som är när anhörig till patienten, bör ej genomföra resan. Detta emedan patientens tillstånd är allvarligt."),
	REK6("REK6","Resenären, som är nära anhörig till patienten, bör ej genomföra resan. Detta emedan patientens tillstånd föranleder speciell omvårdnad genom resenärens försorg."),
	REK7("REK7","Jag avråder ej från resa. Patientens = anhörig till resenären, tillstånd utgör inget hinder för resenären att resa.");

	private static String codeSystemName = "kv_rekommendation_intyg";
	
	private static String codeSystem = null;
	
	private static String codeSystemVersion = null;
	
	private String code;
	
	private String description;
	
	private RekommendationsKod(String code, String desc) {
		this.code = code;
		this.description = desc;
	}
	
	@Override
	public String getCode() {
		return this.code;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public String getCodeSystem() {
		return codeSystem;
	}

	@Override
	public String getCodeSystemName() {
		return codeSystemName;
	}

	@Override
	public String getCodeSystemVersion() {
		return codeSystemVersion;
	}

	public static RekommendationsKod getFromCode(String code) {
		
		for (RekommendationsKod rekKod : values()) {
			if (rekKod.getCode().equals(code)) {
				return rekKod;
			}
		}
		
		return null;
	}
	
}
