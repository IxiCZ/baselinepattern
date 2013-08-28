package cz.muni.fi.lasaris.cep.drools.bpattern.domain;

/**
 * Class representing general event from monitoring.
 */
public class Event {
	
	private long occurenceTime;
	private String type;
	private String host;
	private String application;
	private int level;
	
	public long getOccurenceTime() {
		return occurenceTime;
	}
	
	public void setOccurenceTime(long occurenceTime) {
		this.occurenceTime = occurenceTime;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getHost() {
		return host;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public String getApplication() {
		return application;
	}
	
	public void setApplication(String application) {
		this.application = application;
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public String toString(){
		return "occurenceTime: " + occurenceTime + ", type: " + type + ", host: " + host + 
				 ", application: " + application + ", level: " + level;
	}
}
