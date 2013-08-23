package se.inera.certificate.modules.rli.model.converters;

import se.inera.certificate.model.Id;
import se.inera.certificate.model.Kod;

public final class InternalModelConverterUtils {

	private InternalModelConverterUtils() {
		
	}
	
	public static String getValueFromKod(Kod kod) {
		return (kod != null) ? kod.getCode() : null;
	}

	public static String getValueFromId(Id id) {
		return (id != null) ? id.getExtension() : null;
	}
}
