package se.inera.certificate.modules.rli.model.external.common;

import java.util.List;

public class Aktivitet {

	private Kod aktivitetskod;
	private PartialDateInterval aktivitetstid;
	private String beskrivning;
	private Kod aktivitetsstatus;
	private String motivering;
	private String syfte;
	private Enhet utforsVidEnhet;
	private List<Utforarroll> utforsAvs;
	private Omfattning omfattning;

	public Kod getAktivitetskod() {
		return aktivitetskod;
	}

	public void setAktivitetskod(Kod aktivitetskod) {
		this.aktivitetskod = aktivitetskod;
	}

	public PartialDateInterval getAktivitetstid() {
		return aktivitetstid;
	}

	public void setAktivitetstid(PartialDateInterval aktivitetstid) {
		this.aktivitetstid = aktivitetstid;
	}

	public String getBeskrivning() {
		return beskrivning;
	}

	public void setBeskrivning(String beskrivning) {
		this.beskrivning = beskrivning;
	}

	public Kod getAktivitetsstatus() {
		return aktivitetsstatus;
	}

	public void setAktivitetsstatus(Kod aktivitetsstatus) {
		this.aktivitetsstatus = aktivitetsstatus;
	}

	public String getMotivering() {
		return motivering;
	}

	public void setMotivering(String motivering) {
		this.motivering = motivering;
	}

	public String getSyfte() {
		return syfte;
	}

	public void setSyfte(String syfte) {
		this.syfte = syfte;
	}

	public Enhet getUtforsVidEnhet() {
		return utforsVidEnhet;
	}

	public void setUtforsVidEnhet(Enhet utforsVidEnhet) {
		this.utforsVidEnhet = utforsVidEnhet;
	}

	public List<Utforarroll> getUtforsAvs() {
		return utforsAvs;
	}

	public void setUtforsAvs(List<Utforarroll> utforsAvs) {
		this.utforsAvs = utforsAvs;
	}

	public Omfattning getOmfattning() {
		return omfattning;
	}

	public void setOmfattning(Omfattning omfattning) {
		this.omfattning = omfattning;
	}
}
