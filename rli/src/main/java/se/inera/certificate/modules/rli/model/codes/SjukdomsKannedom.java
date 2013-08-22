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

public enum SjukdomsKannedom implements ICodeSystem {
	
	SJUK1("SJK1","Sjukdomen/komplikationen var okänd vid bokningstillfälllet."),
	SJUK2("SJK2","Patientens sjukdom/komplikation är kronisk, var känd vid bokningstillfället och utgjorde då inget hinder för resan. Tillståndet för nu förvärrats på ett sätt som vid bokningstillfället ej var möjligt att förutse."),
	SJUK3("SJK3","Patientens sjukdom/komplikation är kronisk, var känd vid bokningstillfället och utgjorde då inget hinder för resan. Tillståndet för nu förvärrats på ett sätt som vid bokningstillfället var möjligt att förutse."),
	SJUK4("SJK4","Inget av ovanstående är tillämpligt.");

	private static String codeSystemName = "kv_sjukdomskännedom_intyg";
	
	private static String codeSystem = "f3a556c4-d54b-4f67-8496-d5259df70493";
	
	private static String codeSystemVersion = null;
	
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
		
		if (StringUtils.isBlank(code)) {
			return null;
		}
		
		for (SjukdomsKannedom sjkKod : values()) {
			if (sjkKod.getCode().equals(code)) {
				return sjkKod;
			}
		}
		
		return null;
	}
	
}
