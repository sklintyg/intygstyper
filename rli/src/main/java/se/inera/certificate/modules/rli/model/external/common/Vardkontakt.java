package se.inera.certificate.modules.rli.model.external.common;

public class Vardkontakt {

	private Kod vardkontakttyp;
	private DateInterval vardkontakttid;

	public Kod getVardkontakttyp() {
		return vardkontakttyp;
	}

	public void setVardkontakttyp(Kod vardkontakttyp) {
		this.vardkontakttyp = vardkontakttyp;
	}

	public DateInterval getVardkontakttid() {
		return vardkontakttid;
	}

	public void setVardkontakttid(DateInterval vardkontakttid) {
		this.vardkontakttid = vardkontakttid;
	}
}
