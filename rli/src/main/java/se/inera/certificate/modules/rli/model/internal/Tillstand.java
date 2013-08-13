package se.inera.certificate.modules.rli.model.internal;

public class Tillstand {

	private String orsakForAvbokning;
	
	private Graviditet graviditet;
	
	public Tillstand() {
	
	}

	public String getOrsakForAvbokning() {
		return orsakForAvbokning;
	}

	public void setOrsakForAvbokning(String orsakForAvbokning) {
		this.orsakForAvbokning = orsakForAvbokning;
	}

	public Graviditet getGraviditet() {
		return graviditet;
	}

	public void setGraviditet(Graviditet graviditet) {
		this.graviditet = graviditet;
	}
	
}
