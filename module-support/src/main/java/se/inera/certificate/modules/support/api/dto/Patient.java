package se.inera.certificate.modules.support.api.dto;

import org.apache.commons.lang3.StringUtils;

import static org.springframework.util.Assert.hasText;

public class Patient {

    private final String fornamn;

    private final String mellannamn;

    private final String efternamn;

    private final String personnummer;

    private final String postadress;

    private final String postnummer;

    private final String postort;

    public Patient(String fornamn, String mellannamn, String efternamn, String personnummer, String postadress, String postnummer,
            String postort) {
        hasText(fornamn, "'fornamn' must not be empty");
        hasText(efternamn, "'efternamn' must not be empty");
        hasText(personnummer, "'personnummer' must not be empty");
        this.fornamn = fornamn;
        this.mellannamn = mellannamn;
        this.efternamn = efternamn;
        this.personnummer = personnummer;
        this.postadress = postadress;
        this.postnummer = postnummer;
        this.postort = postort;
    }

    public String getFornamn() {
        return fornamn;
    }

    public String getMellannamn() {
        return mellannamn;
    }

    public String getEfternamn() {
        return efternamn;
    }

    public String getPersonnummer() {
        return personnummer;
    }

    public String getPostadress() {
        return postadress;
    }

    public String getPostnummer() {
        return postnummer;
    }

    public String getPostort() {
        return postort;
    }

    public String getFullstandigtNamn() {
        if (StringUtils.isBlank(mellannamn)) {
            return fornamn + " " + efternamn;
        } else {
            return fornamn + " " + mellannamn + " " + efternamn;
        }
    }
}
