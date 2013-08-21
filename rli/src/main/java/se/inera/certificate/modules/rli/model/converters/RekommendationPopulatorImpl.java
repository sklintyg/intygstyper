package se.inera.certificate.modules.rli.model.converters;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.certificate.modules.rli.model.codes.RekommendationsKod;
import se.inera.certificate.modules.rli.model.codes.SjukdomsKannedom;
import se.inera.certificate.modules.rli.model.internal.Rekommendation;

public class RekommendationPopulatorImpl implements RekommendationPopulator {
	
	private static Logger LOG = LoggerFactory.getLogger(RekommendationPopulatorImpl.class);
	
	/* (non-Javadoc)
	 * @see se.inera.certificate.modules.rli.model.converters.RekommendationPopulator#createAndPopulateRekommendation(se.inera.certificate.modules.rli.model.external.Utlatande)
	 */
	@Override
	public Rekommendation createAndPopulateRekommendation(
			se.inera.certificate.modules.rli.model.external.Utlatande extUtlatande) {
		
		LOG.debug("Creating and poulating Rekommendation");
		
		Rekommendation intRekommendation = new Rekommendation();

		List<se.inera.certificate.modules.rli.model.external.common.Rekommendation> extRekommendationer = extUtlatande.getRekommendationer();
		
		if (extRekommendationer.isEmpty() || extRekommendationer.size() != 1) {
			LOG.error("Rekommendationer should contain only 1 Rekommendation!");
			return null;
		}
		
		se.inera.certificate.modules.rli.model.external.common.Rekommendation extRekommendation = extRekommendationer.get(0);
				
		String extRekommendationsKod = InternalModelConverterUtils.getValueFromKod(extRekommendation.getRekommendationskod());
		intRekommendation.setRekommendationsKod(RekommendationsKod.getFromCode(extRekommendationsKod));
		
		String extSjukKod = InternalModelConverterUtils.getValueFromKod(extRekommendation.getSjukdomskannedom());
		intRekommendation.setSjukdomsKannedom(SjukdomsKannedom.getFromCode(extSjukKod));
		
		intRekommendation.setBeskrivning(extRekommendation.getBeskrivning());
		
		return intRekommendation;
	}

}
