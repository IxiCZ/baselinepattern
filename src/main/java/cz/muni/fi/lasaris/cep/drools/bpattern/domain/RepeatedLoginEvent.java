package cz.muni.fi.lasaris.cep.drools.bpattern.domain;

/**
 * Class representing repeated login event.
 */
public class RepeatedLoginEvent {

	private String hostname;
	private String username;
	private int attempts;

	public RepeatedLoginEvent() {
		super();
	}

	public RepeatedLoginEvent(String hostname, String username, int attempts) {
		super();
		this.hostname = hostname;
		this.username = username;
		this.attempts = attempts;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public int getAttempts() {
		return attempts;
	}

	public void setAttempts(int attempts) {
		this.attempts = attempts;
	}

	public String toString() {
		return "RepeatedLoginEvent - hostname: " + hostname + ", username: " + username + ", attempts: " + attempts;
	}
}
