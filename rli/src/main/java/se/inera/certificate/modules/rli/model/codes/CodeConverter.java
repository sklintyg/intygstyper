/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Certificate Modules (http://code.google.com/p/inera-certificate-modules).
 *
 * Inera Certificate Modules is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Inera Certificate Modules is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
