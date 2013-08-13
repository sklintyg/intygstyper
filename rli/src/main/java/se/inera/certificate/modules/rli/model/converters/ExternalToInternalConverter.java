package se.inera.certificate.modules.rli.model.converters;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.Partial;

import se.inera.certificate.common.v1.PartialDateInterval;
import se.inera.certificate.model.Id;
import se.inera.certificate.model.Kod;
import se.inera.certificate.modules.rli.model.internal.Arrangemang;
import se.inera.certificate.modules.rli.model.internal.Patient;
import se.inera.certificate.modules.rli.model.internal.Status;
import se.inera.certificate.modules.rli.model.internal.Tillstand;
import se.inera.certificate.modules.rli.model.internal.Undersokning;
import se.inera.certificate.modules.rli.model.internal.Utlatande;

public class ExternalToInternalConverter {

	public ExternalToInternalConverter() {

	}

	public Utlatande fromExternalToInternal(
			se.inera.certificate.modules.rli.model.external.Utlatande extUtlatande) {

		Utlatande intUtlatande = new Utlatande();

		intUtlatande.setUtlatandeId(getValueFromId(extUtlatande.getId()));
		intUtlatande.setTypAvUtlatande(getValueFromKod(extUtlatande.getTyp()));

		intUtlatande.setSigneringsDatum(extUtlatande.getSigneringsDatum());
		intUtlatande.setSkickatDatum(extUtlatande.getSkickatDatum());

		// TODO: Finns inte i extern model
		// intUtlatande.setGiltighetsPeriodStart(null);
		// intUtlatande.setGiltighetsPeriodSlut(null);

		intUtlatande.setKommentarer(extUtlatande.getKommentars());
		
		Arrangemang intArrangemang = convertToIntArrangemang(extUtlatande
				.getArrangemang());
		intUtlatande.setArrangemang(intArrangemang);

		Patient intPatient = convertToIntPatient(extUtlatande.getPatient());
		intUtlatande.setPatient(intPatient);

		Tillstand extTillstand = convertToIntTillstand(extUtlatande);
		intUtlatande.setTillstand(extTillstand);

		Undersokning intUndersokning = convertToIntUndersokning(extUtlatande);
		intUtlatande.setUndersokning(intUndersokning);
		
		List<Status> intStatuses = convertToIntStatuses(extUtlatande.getStatus());
		intUtlatande.setStatus(intStatuses);

		return intUtlatande;
	}

	private List<Status> convertToIntStatuses(
			List<se.inera.certificate.model.Status> extStatuses) {
				
		List<Status> intStatuses = new ArrayList<Status>();
		
		if (extStatuses == null) {
			return intStatuses;
		}
		
		Status intStatus;
		
		for (se.inera.certificate.model.Status extStatus : extStatuses) {
			intStatus = new Status();
			intStatus.setType(extStatus.getType().name());
			intStatus.setTimestamp(extStatus.getTimestamp());
			intStatus.setTarget(extStatus.getTarget());
		}
				
		return intStatuses;
	}

	private Undersokning convertToIntUndersokning(
			se.inera.certificate.modules.rli.model.external.Utlatande extUtlatande) {
		// TODO Auto-generated method stub
		return null;
	}

	private Tillstand convertToIntTillstand(
			se.inera.certificate.modules.rli.model.external.Utlatande extUtlatande) {
		// TODO Auto-generated method stub
		return null;
	}

	private Patient convertToIntPatient(
			se.inera.certificate.model.Patient extPatient) {
		
		if (extPatient == null) {
			return null;
		}
		
		Patient intPatient = new Patient();
		
		intPatient.setPersonId(getValueFromId(extPatient.getId()));
		
		String efterNamn = StringUtils.join(extPatient.getEfternamns(), " ");
		intPatient.setEfterNamn(efterNamn);
		
		String forNamn = StringUtils.join(extPatient.getFornamns(), " ");
		intPatient.setForNamn(forNamn);
				
		String fullstandigtNamn = forNamn.concat(" ").concat(efterNamn);
		intPatient.setFullstandigtNamn(fullstandigtNamn);
		
		
		return intPatient;
	}

	private Arrangemang convertToIntArrangemang(
			se.inera.certificate.modules.rli.model.external.Arrangemang extArr) {
		
		if (extArr == null) {
			return null;
		}
		
		Arrangemang intArr = new Arrangemang();
		
		intArr.setPlats(extArr.getPlats());
		intArr.setArrangemangsTyp(getValueFromKod(extArr.getArrangemangstyp()));
		intArr.setBokningsReferens(getValueFromKod(extArr.getBokningsreferens()));
		
//		Partial extBokningsDatum = extArr.getBokningsdatum();
//		Partial extAvbestDatum = extArr.getAvbestallningsdatum();
//		PartialDateInterval extArrangemangsTid = extArr.getArrangemangstid();
			
		
		return intArr;
	}

	private static String getValueFromKod(Kod kod) {
		return (kod != null) ? kod.getCode() : null;
	}

	private static String getValueFromId(Id id) {
		return (id != null) ? id.getExtension() : null;
	}
		
}
