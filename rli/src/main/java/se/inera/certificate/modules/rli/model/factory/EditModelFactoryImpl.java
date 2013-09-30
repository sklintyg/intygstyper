package se.inera.certificate.modules.rli.model.factory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.inera.certificate.model.HosPersonal;
import se.inera.certificate.modules.rli.model.converters.InternalModelConverterUtils;
import se.inera.certificate.modules.rli.model.edit.HoSPersonal;
import se.inera.certificate.modules.rli.model.edit.Utlatande;
import se.inera.certificate.modules.rli.model.edit.Vardenhet;
import se.inera.certificate.modules.rli.model.edit.Vardgivare;

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
    public Utlatande createEditableUtlatande(String certificateId, Map<String, Object> certificateData) {

        Utlatande utlatande = new Utlatande();

        utlatande.setUtlatandeid(certificateId);

        for (Entry<String, Object> entry : certificateData.entrySet()) {
            switch (entry.getKey()) {
            case "skapadAv":
                populateWithSkapadAv(utlatande, entry.getValue());
                break;

            default:
                LOG.warn("Unknown type of certificate data '{}'", entry.getKey());
                break;
            }
        }

        return utlatande;
    }

    private void populateWithSkapadAv(Utlatande utlatande, Object source) {
        if (source == null) {
            return;
        }
        Map<String, Object> sourceMap = null;
        
        if (source instanceof Map<?, ?>){
            sourceMap = (Map<String, Object>) source;
        } 
        if (sourceMap == null){
            return;
        }
        HoSPersonal editHoSPersonal = new HoSPersonal();

        //Go through the map in skapadAv and populate editHoSPersonal 
        for (Entry<String, Object> entry : sourceMap.entrySet()){
            switch(entry.getKey()){
            case "personid":
                editHoSPersonal.setPersonid((String) entry.getValue());
                break;
                
            case "fullstandigtNamn":
                editHoSPersonal.setFullstandigtNamn((String) entry.getValue());
                break;
            
            case "vardenhet":
                if (entry.getValue().getClass() == LinkedHashMap.class) {
                    editHoSPersonal.setVardenhet(buildVardenhetFromMap( (LinkedHashMap<String, Object>) entry.getValue()));
                }
                break;
            }
        }

        utlatande.setSkapadAv(editHoSPersonal);
    }

    private Vardenhet buildVardenhetFromMap(Map<String, Object> source) {
        Vardenhet vardenhet = new Vardenhet();
        for (Entry<String, Object> entry : source.entrySet()){
            switch(entry.getKey()){
            case "enhetsid":
                vardenhet.setEnhetsid((String) entry.getValue());
                break;
            case "enhetsnamn":
                vardenhet.setEnhetsnamn((String) entry.getValue());
                break;
            case "postadress":
                vardenhet.setPostadress((String) entry.getValue());
                break;
            case "postort":
                vardenhet.setPostort((String) entry.getValue());
                break;
            case "postnummer":
                vardenhet.setPostnummer((String) entry.getValue());
                break;
            case "telefonnummer":
                vardenhet.setTelefonnummer((String) entry.getValue());
                break;
            case "epost":
                vardenhet.setEpost((String) entry.getValue());
                break;
            default:
                LOG.warn("Unknown type of certificate data '{}'", entry.getKey());
                break;
            }
        }
        return vardenhet;
    }

    private Vardenhet convertToEditVardenhet(se.inera.certificate.model.Vardenhet extVardenhet) {
        if (extVardenhet == null) {
            return null;
        }

        Vardenhet editVardenhet = new Vardenhet();

        editVardenhet.setEnhetsid(InternalModelConverterUtils.getExtensionFromId(extVardenhet.getId()));
        editVardenhet.setEnhetsnamn(extVardenhet.getNamn());
        editVardenhet.setPostadress(extVardenhet.getPostadress());
        editVardenhet.setPostnummer(extVardenhet.getPostnummer());
        editVardenhet.setPostort(extVardenhet.getPostort());
        editVardenhet.setTelefonnummer(extVardenhet.getTelefonnummer());
        editVardenhet.setEpost(extVardenhet.getEpost());

        Vardgivare editVardgivare = convertToEditVardgivare(extVardenhet.getVardgivare());
        editVardenhet.setVardgivare(editVardgivare);

        return editVardenhet;
    }

    private Vardgivare convertToEditVardgivare(se.inera.certificate.model.Vardgivare extVardgivare) {
        if (extVardgivare == null) {
            return null;
        }

        Vardgivare editVardgivare = new Vardgivare();

        editVardgivare.setVardgivarid(InternalModelConverterUtils.getExtensionFromId(extVardgivare.getId()));
        editVardgivare.setVardgivarnamn(extVardgivare.getNamn());

        return editVardgivare;
    }
}
