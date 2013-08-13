package se.inera.certificate.modules.rli.model.codes;

public enum ArrangemangsTyp implements ICodeSystem {
	
	RESA("420008001","Resa");

	private static String codeSystemName = "SNOMED-CT";
	
	private static String codeSystem = "1.2.752.116.2.1.1.1";
	
	private static String codeSystemVersion = null;
	
	private String code;
	
	private String description;
	
	private ArrangemangsTyp(String code, String desc) {
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

	public static ArrangemangsTyp getFromCode(String code) {
		
		for (ArrangemangsTyp arrKod : values()) {
			if (arrKod.getCode().equals(code)) {
				return arrKod;
			}
		}
		
		return null;
	}
	
}
