package se.inera.certificate.modules.rli.model.codes;

import org.apache.commons.lang3.StringUtils;

import se.inera.certificate.model.Kod;

/**
 * Util for converting an enum implementing ICodeSystem to a Kod object.
 * 
 * @author nikpet
 *
 */
public final class CodeConverter {

	private CodeConverter() {
		
	}
		
	public static Kod toKod(ICodeSystem codeEnum) {
		
		if (codeEnum == null) {
			return null;
		}
		
		if (StringUtils.isBlank(codeEnum.getCode())) {
			throw new IllegalArgumentException("The provided enum does not contain any code values");
		}
		
		Kod kod = new Kod(codeEnum.getCode());
		
		if (StringUtils.isNotBlank(codeEnum.getCodeSystemName())) {
			kod.setCodeSystemName(codeEnum.getCodeSystemName());
		}
		
		if (StringUtils.isNotBlank(codeEnum.getCodeSystem())) {
			kod.setCodeSystem(codeEnum.getCodeSystem());
		}
		
		if (StringUtils.isNotBlank(codeEnum.getCodeSystemVersion())) {
			kod.setCodeSystemVersion(codeEnum.getCodeSystemVersion());
		}
		
		return kod;
	}
	
}
