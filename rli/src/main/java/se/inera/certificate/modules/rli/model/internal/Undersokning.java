package se.inera.certificate.modules.rli.model.internal;

public class Undersokning {

	private String forstaUndersokningDatum;
	
	private String forstaUndersokningPlats;
	
	private String undersokningDatum;
	
	private String undersokningPlats;
	
	private String komplikationStatus;
	
	private String komplikationBeskrivning;
	
	public Undersokning() {
	
	}

	public String getForstaUndersokningDatum() {
		return forstaUndersokningDatum;
	}

	public void setForstaUndersokningDatum(String forstaUndersokningDatum) {
		this.forstaUndersokningDatum = forstaUndersokningDatum;
	}

	public String getForstaUndersokningPlats() {
		return forstaUndersokningPlats;
	}

	public void setForstaUndersokningPlats(String forstaUndersokningPlats) {
		this.forstaUndersokningPlats = forstaUndersokningPlats;
	}

	public String getUndersokningDatum() {
		return undersokningDatum;
	}

	public void setUndersokningDatum(String undersokningDatum) {
		this.undersokningDatum = undersokningDatum;
	}

	public String getUndersokningPlats() {
		return undersokningPlats;
	}

	public void setUndersokningPlats(String undersokningPlats) {
		this.undersokningPlats = undersokningPlats;
	}

	public String getKomplikationStatus() {
		return komplikationStatus;
	}

	public void setKomplikationStatus(String komplikationStatus) {
		this.komplikationStatus = komplikationStatus;
	}

	public String getKomplikationBeskrivning() {
		return komplikationBeskrivning;
	}

	public void setKomplikationBeskrivning(String komplikationBeskrivning) {
		this.komplikationBeskrivning = komplikationBeskrivning;
	}
	
}
