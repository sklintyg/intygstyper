package se.inera.certificate.modules.rli.model.converter;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.certificate.modules.rli.model.codes.CodeConverter;
import se.inera.certificate.modules.rli.model.codes.RekommendationsKod;
import se.inera.certificate.modules.rli.model.codes.SjukdomskannedomKod;
import se.inera.certificate.modules.rli.model.internal.mi.Rekommendation;

public class RekommendationPopulator {

    private static final Logger LOG = LoggerFactory.getLogger(RekommendationPopulator.class);

    public Rekommendation createAndPopulateRekommendation(
            se.inera.certificate.modules.rli.model.external.Utlatande extUtlatande) {

        LOG.trace("Creating and poulating Rekommendation");

        Rekommendation intRekommendation = new Rekommendation();

        List<se.inera.certificate.model.Rekommendation> extRekommendationer = extUtlatande
                .getRekommendationer();

        if (extRekommendationer.isEmpty() || extRekommendationer.size() != 1) {
            LOG.error("- Rekommendationer should contain only 1 Rekommendation. Current nbr is {}",
                    extRekommendationer.size());
            return null;
        }

        se.inera.certificate.model.Rekommendation extRekommendation = extRekommendationer
                .get(0);

        RekommendationsKod rekommendationsKod = CodeConverter.fromCode(extRekommendation.getRekommendationskod(), RekommendationsKod.class);
        intRekommendation.setRekommendationskod(rekommendationsKod);

        SjukdomskannedomKod sjukdomsKannedom = CodeConverter.fromCode(extRekommendation.getSjukdomskannedom(), SjukdomskannedomKod.class);
        intRekommendation.setSjukdomskannedom(sjukdomsKannedom);

        intRekommendation.setBeskrivning(extRekommendation.getBeskrivning());

        return intRekommendation;
    }

}
