package se.inera.certificate.modules.fkparent.model.converter;

import org.joda.time.LocalDateTime;

import se.inera.certificate.model.common.internal.*;

/* TODO: this class should be moved to test directory and put in a test-jar. */

public class IntygGrundDataBuilder {

    public static GrundData getGrundData() {
        GrundData grundData = new GrundData();
        grundData.setSigneringsdatum(new LocalDateTime());
        grundData.setSkapadAv(getHosPersonal());
        grundData.setPatient(getPatient());
        return grundData;
    }

    private static Patient getPatient() {
        Patient patient = new Patient();
        patient.setEfternamn("Olsson");
        patient.setFornamn("Olivia");
        patient.setPersonId("19270310-4321");
        patient.setPostadress("Pgatan 2");
        patient.setPostnummer("100 20");
        patient.setPostort("Stadby gärde");
        return patient;
    }

    private static HoSPersonal getHosPersonal() {
        HoSPersonal personal = new HoSPersonal();
        personal.setVardenhet(getVardenhet());
        personal.setForskrivarKod("09874321");
        personal.setFullstandigtNamn("Karl Karlsson");
        personal.setPersonId("19650708-1234");
        return personal;
    }

    private static Vardenhet getVardenhet() {
        Vardenhet vardenhet = new Vardenhet();
        Vardgivare vardgivare = new Vardgivare();
        vardgivare.setVardgivarnamn("VG1");
        vardgivare.setVardgivarid("12345678");
        vardenhet.setVardgivare(vardgivare);
        vardenhet.setTelefonnummer("0812341234");
        vardenhet.setArbetsplatsKod("45312");
        vardenhet.setEnhetsid("123456789");
        vardenhet.setEnhetsnamn("VE1");
        vardenhet.setEpost("ve1@vg1.se");
        vardenhet.setPostadress("Enhetsg. 1");
        vardenhet.setPostnummer("100 10");
        vardenhet.setPostort("Stadby");
        return vardenhet;
    }

}
