package se.inera.certificate.modules.rli.model.internal;


public class Undersokning {

	private String forstaUndersokningDatum;
	
	private String forstaUndersokningPlats;
	
	private String undersokningDatum;
	
	private String undersokningPlats;
	
	private KomplikationStyrkt komplikationStyrkt;
		
	private Graviditet graviditet;
	
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

	public KomplikationStyrkt getKomplikationStyrkt() {
		return komplikationStyrkt;
	}

	public void setKomplikationStyrkt(KomplikationStyrkt komplikationStyrkt) {
		this.komplikationStyrkt = komplikationStyrkt;
	}

	public Graviditet getGraviditet() {
		return graviditet;
	}

	public void setGraviditet(Graviditet graviditet) {
		this.graviditet = graviditet;
	}

	public String getKomplikationBeskrivning() {
		return komplikationBeskrivning;
	}

	public void setKomplikationBeskrivning(String komplikationBeskrivning) {
		this.komplikationBeskrivning = komplikationBeskrivning;
	}
	
}
