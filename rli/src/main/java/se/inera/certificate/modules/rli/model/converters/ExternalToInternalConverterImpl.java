package se.inera.certificate.modules.rli.model.converters;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.certificate.model.HosPersonal;
import se.inera.certificate.model.Id;
import se.inera.certificate.model.Kod;
import se.inera.certificate.modules.rli.model.codes.ArrangemangsTyp;
import se.inera.certificate.modules.rli.model.internal.Arrangemang;
import se.inera.certificate.modules.rli.model.internal.HoSPersonal;
import se.inera.certificate.modules.rli.model.internal.Patient;
import se.inera.certificate.modules.rli.model.internal.Status;
import se.inera.certificate.modules.rli.model.internal.Tillstand;
import se.inera.certificate.modules.rli.model.internal.Undersokning;
import se.inera.certificate.modules.rli.model.internal.Utfardare;
import se.inera.certificate.modules.rli.model.internal.Utlatande;
import se.inera.certificate.modules.rli.model.internal.Vardenhet;
import se.inera.certificate.modules.rli.model.internal.Vardgivare;

/**
 * Converter for converting the external format to the internal view format.
 * 
 * 
 * @author Niklas Pettersson, R2M
 *
 */
public class ExternalToInternalConverterImpl implements ExternalToInternalConverter {
	
	private static final Logger LOG = LoggerFactory.getLogger(ExternalToInternalConverterImpl.class);

	private UndersokingRekommendationConverter undersokingRekommendationConverter;
	
	public ExternalToInternalConverterImpl() {

	}

	/* (non-Javadoc)
	 * @see se.inera.certificate.modules.rli.model.converters.ExternalToInternalConverter#fromExternalToInternal(se.inera.certificate.modules.rli.model.external.Utlatande)
	 */
	@Override
	public Utlatande fromExternalToInternal(
			se.inera.certificate.modules.rli.model.external.Utlatande extUtlatande) {

		LOG.debug("Starting conversion");
		
		Utlatande intUtlatande = new Utlatande();

		intUtlatande.setUtlatandeId(getValueFromId(extUtlatande.getId()));
		intUtlatande.setTypAvUtlatande(getValueFromKod(extUtlatande.getTyp()));

		intUtlatande.setSigneringsDatum(extUtlatande.getSigneringsDatum());
		intUtlatande.setSkickatDatum(extUtlatande.getSkickatDatum());

		// TODO: Finns inte i extern model
		// intUtlatande.setGiltighetsPeriodStart(null);
		// intUtlatande.setGiltighetsPeriodSlut(null);
		
		intUtlatande.setKommentarer(extUtlatande.getKommentars());
		
		List<Status> intStatuses = convertToIntStatuses(extUtlatande.getStatus());
		intUtlatande.setStatus(intStatuses);
		
		Utfardare intUtfardare = convertToIntUtfardare(extUtlatande.getSkapadAv());
		intUtlatande.setUtfardare(intUtfardare );

		Patient intPatient = convertToIntPatient(extUtlatande.getPatient());
		intUtlatande.setPatient(intPatient);
		
		Arrangemang intArrangemang = convertToIntArrangemang(extUtlatande
				.getArrangemang());
		intUtlatande.setArrangemang(intArrangemang);

		populateUndersokingRekommendation(extUtlatande, intUtlatande);

		return intUtlatande;
	}

	private void populateUndersokingRekommendation(
			se.inera.certificate.modules.rli.model.external.Utlatande extUtlatande,
			Utlatande intUtlatande) {
		undersokingRekommendationConverter.populateUndersokningRekommendation(extUtlatande, intUtlatande);
	}

	Utfardare convertToIntUtfardare(HosPersonal extHoSPersonal) {
		
		LOG.debug("Converting utfardare");
		
		if (extHoSPersonal == null) {
			LOG.debug("External HoSPersonal is null, can not convert");
			return null;
		}
		
		Utfardare intUtfardare = new Utfardare();
		
		HoSPersonal intHoSPersonal = convertToIntHoSPersonal(extHoSPersonal);
		intUtfardare.setHosPersonal(intHoSPersonal); 
				
		Vardenhet intVardenhet = convertToIntVardenhet(extHoSPersonal.getVardenhet());
		intUtfardare.setVardenhet(intVardenhet);
		
		return intUtfardare;
	}

	Vardenhet convertToIntVardenhet(se.inera.certificate.model.Vardenhet extVardenhet) {
		
		LOG.debug("Converting vardenhet");
		
		if (extVardenhet == null) {
			LOG.debug("External Vardenhet is null, can not convert");
			return null;
		}
		
		Vardenhet intVardenhet = new Vardenhet();
		
		intVardenhet.setEnhetsId(getValueFromId(extVardenhet.getId()));
		intVardenhet.setEnhetsNamn(extVardenhet.getNamn());
		intVardenhet.setPostAddress(extVardenhet.getPostadress());
		intVardenhet.setPostNummer(extVardenhet.getPostnummer());
		intVardenhet.setPostOrt(extVardenhet.getPostort());
		intVardenhet.setTelefonNummer(extVardenhet.getTelefonnummer());
		intVardenhet.setePost(extVardenhet.getEpost());
		
		Vardgivare intVardgivare = convertToIntVardgivare(extVardenhet.getVardgivare());
		intVardenhet.setVardgivare(intVardgivare );
				
		return intVardenhet;
	}

	Vardgivare convertToIntVardgivare(se.inera.certificate.model.Vardgivare extVardgivare) {

		LOG.debug("Converting vardgivare");
		
		if (extVardgivare == null) {
			LOG.debug("External vardgivare is null, can not convert");
			return null;
		}
		
		Vardgivare intVardgivare = new Vardgivare();
		
		intVardgivare.setVardgivarId(getValueFromId(extVardgivare.getId()));
		intVardgivare.setVardgivarNamn(extVardgivare.getNamn());
		
		return intVardgivare;
	}

	HoSPersonal convertToIntHoSPersonal(HosPersonal extHoSPersonal) {
		
		LOG.debug("Converting HoSPersonal");
		
		if (extHoSPersonal == null) {
			LOG.debug("External HoSPersonal is null, can not convert");
			return null;
		}
		
		HoSPersonal intHoSPersonal = new HoSPersonal();
		
		intHoSPersonal.setPersonId(getValueFromId(extHoSPersonal.getId()));
		intHoSPersonal.setFullstandigtNamn(extHoSPersonal.getNamn());
		//intHoSPersonal.setBefattning(befattning);
		
		return intHoSPersonal;
	}

	List<Status> convertToIntStatuses(
			List<se.inera.certificate.model.Status> extStatuses) {
		
		LOG.debug("Converting statuses");
		
		List<Status> intStatuses = new ArrayList<Status>();
		
		if (extStatuses == null || extStatuses.isEmpty()) {
			LOG.debug("No statuses found to convert");
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

	Undersokning convertToIntUndersokning(
			se.inera.certificate.modules.rli.model.external.Utlatande extUtlatande) {
		// TODO Auto-generated method stub
		return null;
	}

	Patient convertToIntPatient(
			se.inera.certificate.model.Patient extPatient) {
		
		LOG.debug("Converting patient");
		
		if (extPatient == null) {
			LOG.debug("No Patient found to convert");
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

	Arrangemang convertToIntArrangemang(
			se.inera.certificate.modules.rli.model.external.Arrangemang extArr) {
		
		LOG.debug("Converting arrangemang");
		
		if (extArr == null) {
			LOG.debug("No arrangemang found to convert");
			return null;
		}
		
		Arrangemang intArr = new Arrangemang();
		
		intArr.setPlats(extArr.getPlats());
		
		String arrTypCode = getValueFromKod(extArr.getArrangemangstyp());
		ArrangemangsTyp arrTyp = ArrangemangsTyp.getFromCode(arrTypCode);
		intArr.setArrangemangsTyp(arrTyp);
		
		intArr.setBokningsReferens(extArr.getBokningsreferens());
		
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
