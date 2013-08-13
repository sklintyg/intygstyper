package se.inera.certificate.modules.rli.model.codes;

import org.apache.commons.lang3.StringUtils;

public enum ObservationsKod implements ICodeSystem {
	
	SJUKDOM("39104002", "Sjukdom"),
	GRAVIDITET("289908002", "Gravid");
	
	private static String codeSystemName = "SNOMED-CT";
	
	private static String codeSystem = "1.2.752.116.2.1.1.1";
	
	private static String codeSystemVersion = "";
	
	private String code;
	
	private String description;
	
	private ObservationsKod(String code, String desc) {
		this.code = code;
		this.description = desc;
	}

	public String getCode() {
		return code;
	}

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

	public static ObservationsKod getFromCode(String code) {
		
		if (StringUtils.isBlank(code)) {
			return null;
		}
		
		for (ObservationsKod obsKod : values()) {
			if (obsKod.getCode().equals(code)) {
				return obsKod;
			}
		}
		
		return null;
	}
}
