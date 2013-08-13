package se.inera.certificate.modules.rli.model.codes;

public enum TestKod implements ICodeSystem {
	
	CODE_RED("CODE_RED", "Code Red"),
	CODE_BLUE("CODE_BLUE", "Code Blue"),
	CODE_BLACK("", "");
		
	private static String codeSystemName = "ColorCodes";
	
	private static String codeSystem = "CC";
	
	private static String codeSystemVersion = "1.0";
	
	private String code;
	
	private String description;
	
	private TestKod(String code, String description) {
		this.code = code;
		this.description = description;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getDescription() {
		return description;
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

}
