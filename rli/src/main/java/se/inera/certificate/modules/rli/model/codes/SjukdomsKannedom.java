package se.inera.certificate.modules.rli.model.codes;

public enum SjukdomsKannedom implements ICodeSystem {
	
	SJUK1("SJK1","Sjukdomen/komplikationen var okänd vid bokningstillfälllet."),
	SJUK2("SJK2","Patientens sjukdom/komplikation är kronisk, var känd vid bokningstillfället och utgjorde då inget hinder för resan. Tillståndet för nu förvärrats på ett sätt som vid bokningstillfället ej var möjligt att förutse."),
	SJUK3("SJK3","Patientens sjukdom/komplikation är kronisk, var känd vid bokningstillfället och utgjorde då inget hinder för resan. Tillståndet för nu förvärrats på ett sätt som vid bokningstillfället var möjligt att förutse."),
	SJUK4("SJK4","Inget av ovanstående är tillämpligt.");

	private static String codeSystemName = "kv_sjukdomskännedom_intyg";
	
	private static String codeSystem = "";
	
	private static String codeSystemVersion = "";
	
	private String code;
	
	private String description;
	
	private SjukdomsKannedom(String code, String desc) {
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

	public static SjukdomsKannedom getFromCode(String code) {
		
		for (SjukdomsKannedom sjkKod : values()) {
			if (sjkKod.getCode().equals(code)) {
				return sjkKod;
			}
		}
		
		return null;
	}
	
}
