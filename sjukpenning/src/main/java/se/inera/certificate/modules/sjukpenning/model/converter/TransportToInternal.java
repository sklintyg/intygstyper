package se.inera.certificate.modules.sjukpenning.model.converter;

import se.inera.certificate.model.converter.util.ConverterException;
import se.inera.certificate.modules.sjukpenning.model.internal.SjukpenningUtlatande;
import se.inera.intygstjanster.fk.services.v1.SjukpenningIntyg;

public class TransportToInternal {
    public static SjukpenningUtlatande convert(SjukpenningIntyg source) throws ConverterException {
        SjukpenningUtlatande utlatande = new SjukpenningUtlatande();
        utlatande.setAktivitetsbegransning(source.getKonsekvenser().getBegransning());
        utlatande.setFunktionsnedsattning(source.getKonsekvenser().getKonsekvens());

        //utlatande.setJournaluppgifter(source.getVardKontakter().getVardkontakt().);
        // TODO
        return null;
    }
}
