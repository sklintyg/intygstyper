package se.inera.certificate.modules.rli.model.internal;

import org.joda.time.LocalDateTime;

public class Status {
	
	private String type;

	private String target;
	
	private LocalDateTime timestamp;
	
	public Status() {
	
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

}
