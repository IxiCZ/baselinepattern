package cz.muni.fi.lasaris.cep.drools.bpattern;

import java.util.List;

import cz.muni.fi.lasaris.cep.drools.bpattern.domain.Event;
import cz.muni.fi.lasaris.cep.drools.bpattern.json.LogFileReader;
import cz.muni.fi.lasaris.cep.drools.bpattern.rules.DroolsSessionRunHandler;

/**
 * Runs baseline pattern on log file.	
 */
public class App {
	public static void main(String[] args) {
		LogFileReader log = new LogFileReader();
		List<Event> events = log.readEventsFromLogFile("jel.log");
		
		DroolsSessionRunHandler drools = new DroolsSessionRunHandler();
		drools.run(events);
	}
}
