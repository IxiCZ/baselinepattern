package cz.muni.fi.lasaris.cep.drools.bpattern.domain;

/**
 * Class representing login event.
 */
public class LoginEvent extends Event {
	
	private String schema;
	private boolean success;
	private String sourceHost;
	private int sourcePort;
	private String user;
	
	public String getSchema() {
		return schema;
	}
	
	public void setSchema(String schema) {
		this.schema = schema;
	}
	
	public boolean isSuccess() {
		return success;
	}
	
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public String getSourceHost() {
		return sourceHost;
	}
	
	public void setSourceHost(String sourceHost) {
		this.sourceHost = sourceHost;
	}
	
	public int getSourcePort() {
		return sourcePort;
	}
	
	public void setSourcePort(int sourcePort) {
		this.sourcePort = sourcePort;
	}
	
	public String getUser() {
		return user;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public String toString() {
		return "occurenceTime: " + getOccurenceTime() + ", type: " + getType() + ", host: " + getHost() + 
				 ", application: " + getApplication() + ", level: " + getLevel() + "-- schema: " + schema + ", success: " + success + ", sourceHost: " + sourceHost 
				+ ", sourcePort:" + sourcePort + ", user: " + user + "--";
	}
}
