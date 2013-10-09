package se.inera.certificate.modules.rli.model.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import se.inera.certificate.modules.rli.model.edit.Utlatande;

import com.fasterxml.jackson.databind.ObjectMapper;

public class EditModelFactoryTest {
    EditModelFactoryImpl factory;
    ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        factory = new EditModelFactoryImpl();
        mapper = new ObjectMapper();
    }

    @Test
    public void testCreateEditableUtlatande() {

        Map<String, Object> initialData = new HashMap<String, Object>();
        Map<String, Serializable> skapadAv = new HashMap<String, Serializable>();

        Map<String, String> vardgivare = new HashMap<String, String>();
        vardgivare.put("vardgivarid", "vardgivare_test");
        vardgivare.put("vardgivarnamn", "Testvårdgivaren");

        HashMap<String, Object> vardenhet = new HashMap<String, Object>();
        vardenhet.put("enhetsid", "testenhet");
        vardenhet.put("enhetsnamn", "testnamn");
        vardenhet.put("postadress", "Testvägen12");
        vardenhet.put("postnummer", "12334");
        vardenhet.put("postort", "Tolvberga");
        vardenhet.put("telefonnummer", "08-1337");
        vardenhet.put("epost", "test@test.com");
        vardenhet.put("vardgivare", vardgivare);

        skapadAv.put("personid", "personid");
        skapadAv.put("fullstandigtNamn", "personnamn");
        skapadAv.put("vardenhet", vardenhet);

        initialData.put("skapadAv", skapadAv);

        Utlatande utlatande = factory.createEditableUtlatande("testid", initialData);

        assertNotNull(utlatande);

        // vardenhet
        assertEquals("testenhet", utlatande.getSkapadAv().getVardenhet().getEnhetsid());
        assertEquals("testnamn", utlatande.getSkapadAv().getVardenhet().getEnhetsnamn());
        assertEquals("Testvägen12", utlatande.getSkapadAv().getVardenhet().getPostadress());
        assertEquals("12334", utlatande.getSkapadAv().getVardenhet().getPostnummer());
        assertEquals("Tolvberga", utlatande.getSkapadAv().getVardenhet().getPostort());
        assertEquals("08-1337", utlatande.getSkapadAv().getVardenhet().getTelefonnummer());
        assertEquals("test@test.com", utlatande.getSkapadAv().getVardenhet().getEpost());
        // vardgivare
        assertEquals("Testvårdgivaren", utlatande.getSkapadAv().getVardenhet().getVardgivare().getVardgivarnamn());
        assertEquals("vardgivare_test", utlatande.getSkapadAv().getVardenhet().getVardgivare().getVardgivarid());
        // skapad av
        assertEquals("personid", utlatande.getSkapadAv().getPersonid());
        assertEquals("personnamn", utlatande.getSkapadAv().getFullstandigtNamn());
    }

}
