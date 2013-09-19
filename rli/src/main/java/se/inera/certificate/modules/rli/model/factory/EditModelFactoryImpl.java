package se.inera.certificate.modules.rli.model.factory;

import java.util.Map;

import se.inera.certificate.modules.rli.model.edit.Utlatande;

/**
 * Factory for creating a editable model.
 * 
 * @author nikpet
 *
 */
public class EditModelFactoryImpl implements EditModelFactory {

    /* (non-Javadoc)
     * @see se.inera.certificate.modules.rli.model.factory.EditModelFactory#createEditableUtlatande(java.lang.String, java.util.Map)
     */
    @Override
    public Utlatande createEditableUtlatande(String certificateId, Map<String, Object> certificateData) {

        Utlatande utlatande = new Utlatande();

        utlatande.setUtlatandeid(certificateId);

        // TODO: populate utlatande using the data contained in certificateData

        return utlatande;
    }

}
