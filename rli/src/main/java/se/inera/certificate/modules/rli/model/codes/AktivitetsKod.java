package se.inera.certificate.modules.rli.model.codes;

public enum AktivitetsKod implements ICodeSystem {

	KLINISK_UNDERSOKNING("AV020","klinisk undersökning UNS","KVÅ"),
	OMVARDNADSATGARD("9632001","Omvårdnadsåtgärd","SNOMED-CT");
	
	private String code;
	private String description;
	private String codeSystemName;
	
	
	private AktivitetsKod(String code, String description, String codeSystemName) {
		this.code = code;
		this.description = description;
		this.codeSystemName = codeSystemName;
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
		return null;
	}

	@Override
	public String getCodeSystemName() {
		return this.codeSystemName;
	}

	@Override
	public String getCodeSystemVersion() {
		return null;
	}
	
}
