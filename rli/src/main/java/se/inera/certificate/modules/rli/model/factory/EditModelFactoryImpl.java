package se.inera.certificate.modules.rli.model.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.certificate.model.HosPersonal;
import se.inera.certificate.modules.rli.model.edit.HoSPersonal;
import se.inera.certificate.modules.rli.model.edit.Utlatande;
import se.inera.certificate.modules.rli.model.edit.Vardenhet;
import se.inera.certificate.modules.rli.model.edit.Vardgivare;
import se.inera.certificate.modules.rli.rest.dto.CreateNewDraftCertificateHolder;

/**
 * Factory for creating a editable model.
 * 
 * @author nikpet
 * 
 */
public class EditModelFactoryImpl implements EditModelFactory {

    private static final Logger LOG = LoggerFactory.getLogger(EditModelFactoryImpl.class);

    /*
     * (non-Javadoc)
     * 
     * @see se.inera.certificate.modules.rli.model.factory.EditModelFactory#createEditableUtlatande(java.lang.String,
     * java.util.Map)
     */
    @Override
    public Utlatande createEditableUtlatande(CreateNewDraftCertificateHolder newDraftData) {

        Utlatande utlatande = new Utlatande();

        utlatande.setUtlatandeid(newDraftData.getCertificateId());

        populateWithSkapadAv(utlatande, newDraftData.getSkapadAv());
        // populateWithPatientInfo(utlatande, newDraftData.getPatientInfo());

        return utlatande;
    }

    private void populateWithSkapadAv(Utlatande utlatande, HosPersonal skapadAv) {
        if (skapadAv == null) {
            LOG.error("skapadAv was null");
            return;
        }

        utlatande.setSkapadAv(convertToEdit(skapadAv));
    }

    private HoSPersonal convertToEdit(HosPersonal hosPersType) {
        HoSPersonal hosPersonal = new HoSPersonal();

        hosPersonal.setPersonid(hosPersType.getId().getExtension());
        hosPersonal.setFullstandigtNamn(hosPersType.getNamn());

        if (hosPersType.getBefattning() != null) {
            hosPersonal.setBefattning(hosPersType.getBefattning());
        }

        hosPersonal.setVardenhet(convertEnhetType(hosPersType.getVardenhet()));

        return hosPersonal;
    }

    private Vardenhet convertEnhetType(se.inera.certificate.model.Vardenhet enhet) {
        Vardenhet vardenhet = new Vardenhet();
        vardenhet.setEnhetsid(enhet.getId().getExtension());
        vardenhet.setEnhetsnamn(enhet.getNamn());
        vardenhet.setEpost(enhet.getEpost());
        vardenhet.setPostadress(enhet.getPostadress());
        vardenhet.setPostort(enhet.getPostort());
        vardenhet.setPostnummer(enhet.getPostnummer());
        vardenhet.setTelefonnummer(enhet.getTelefonnummer());
        vardenhet.setVardgivare(convertVardgivareType(enhet.getVardgivare()));

        return vardenhet;
    }

    private Vardgivare convertVardgivareType(se.inera.certificate.model.Vardgivare source) {
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarid(source.getId().getExtension());
        vardgivare.setVardgivarnamn(source.getNamn());
        return vardgivare;
    }
}
