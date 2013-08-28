package cz.muni.fi.lasaris.cep.drools.bpattern.domain;

public class DEvent extends Event{

	private String schema;
	private String value1;
	private int value2;
	
	public String getSchema() {
		return schema;
	}
	
	public void setSchema(String schema) {
		this.schema = schema;
	}
	
	public String getValue1() {
		return value1;
	}
	
	public void setValue1(String value1) {
		this.value1 = value1;
	}
	
	public int getValue2() {
		return value2;
	}
	
	public void setValue2(int value2) {
		this.value2 = value2;
	}
	
	public String toString() {
		return "occurenceTime: " + getOccurenceTime() + ", type: " + getType() + ", host: " + getHost() + 
				", application: " + getApplication() + ", level: " + getLevel() + "-- schema: " + schema + 
				", value1:" + value1 + ", value2: " + value2 + "--";
	}
}
