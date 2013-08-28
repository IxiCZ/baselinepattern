package cz.muni.fi.lasaris.cep.drools.bpattern.json;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import cz.muni.fi.lasaris.cep.drools.bpattern.domain.DEvent;
import cz.muni.fi.lasaris.cep.drools.bpattern.domain.Event;
import cz.muni.fi.lasaris.cep.drools.bpattern.domain.LoginEvent;

/**
 * Class loading the events from file.
 */
public class LogFileReader {

	private List<Event> events = new ArrayList<Event>();

	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}
	
	/**
	 * Return list of events from given file name. 
	 * 
     * @param fileName name of file from which events will be loaded
	 * @return events loaded from file
	 */
	public List<Event> readEventsFromLogFile(String fileName){
		FileInputStream is = null;
		try {
			is = new FileInputStream(fileName);
		} catch (FileNotFoundException e) {
			System.err.println("File with logs not found.");
			e.printStackTrace();
		}

		if (is != null) {
			try {
				long startTime = System.currentTimeMillis();
				
				System.out.println("Started reading log file.");
				read(is);
				System.out.println("Finished reading log file after: " + (System.currentTimeMillis() - startTime));
				
				return events;
			} catch (ParseException e) {
				System.err.println("Wrong log format.");
				e.printStackTrace();
			} catch (IOException e) {
				System.err.println("Problem with reading file.");
				e.printStackTrace();
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					System.err.println("Problem with closing file.");
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * Loads the events from the input stream.
	 * 
	 * @param is input stream from which the events are to be loaded
	 * @throws IOException	not correct format
	 * @throws ParseException not correct format
	 */
	private void read(InputStream is) throws ParseException, IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));

		//long startTime = System.currentTimeMillis();
		//int lines = 0;
	
		String line = reader.readLine();
		while (line != null) {
			if (line.contains("http://www.ssh.org/events/daemon.jsch")) {
				events.add(jsonDaemonParse(line));
			} else if (line.contains("http://www.fi.muni.cz/events/namespace.jsch")) {
				events.add(jsonNamespaceParse(line));
			}
			// lines++;
			line = reader.readLine();
		}
		
		//System.out.println("L events: " + lines);
		//System.out.println("loginEvents: " + events.size());
		//System.out.println(System.currentTimeMillis() - startTime);
		
		/*for(Event e: events){
			System.out.println(e);
		}*/
	}

	/**
	 * Creates login event from json string.
	 * 
	 * @param line string from which the login event is created
	 * @return login event created from given json string
	 * @throws ParseException the format is wrong
	 */
	private LoginEvent jsonDaemonParse(String line) throws ParseException {
		JSONParser parser = new JSONParser();

		Object obj = parser.parse(line);

		JSONObject jsonObject = (JSONObject) obj;
		JSONObject jsonObjectEvent = (JSONObject) jsonObject.get("Event");

		LoginEvent event = new LoginEvent();
		event.setOccurenceTime((Long) jsonObjectEvent.get("occurrenceTime"));
		event.setType((String) jsonObjectEvent.get("type"));
		event.setHost((String) jsonObjectEvent.get("host"));
		event.setApplication((String) jsonObjectEvent.get("application"));
		event.setLevel(((Long) jsonObjectEvent.get("level")).intValue());

		JSONObject jsonObjectInnerData = (JSONObject) jsonObjectEvent.get("_");
		event.setSchema((String) jsonObjectInnerData.get("schema"));
		event.setSuccess((Boolean) jsonObjectInnerData.get("success"));
		event.setSourceHost((String) jsonObjectInnerData.get("sourceHost"));
		event.setSourcePort(((Long) jsonObjectInnerData.get("sourcePort")).intValue());
		event.setUser((String) jsonObjectInnerData.get("user"));

		
		return event;
	}
	
	/**
	 * Creates dummy event from json string.
	 * 
	 * @param line string from which the dummy event is created
	 * @return dummy event created from given json string
	 * @throws ParseException the format is wrong
	 */
	private DEvent jsonNamespaceParse(String line) throws ParseException {
		JSONParser parser = new JSONParser();

		Object obj = parser.parse(line);

		JSONObject jsonObject = (JSONObject) obj;
		JSONObject jsonObjectEvent = (JSONObject) jsonObject.get("Event");

		DEvent event = new DEvent();
		event.setOccurenceTime((Long) jsonObjectEvent.get("occurrenceTime"));
		event.setType((String) jsonObjectEvent.get("type"));
		event.setHost((String) jsonObjectEvent.get("host"));
		event.setApplication((String) jsonObjectEvent.get("application"));
		event.setLevel(((Long) jsonObjectEvent.get("level")).intValue());

		JSONObject jsonObjectInnerData = (JSONObject) jsonObjectEvent.get("_");
		event.setSchema((String) jsonObjectInnerData.get("schema"));
		event.setValue1((String) jsonObjectInnerData.get("value1"));
		event.setValue2(((Long) jsonObjectInnerData.get("value2")).intValue());
		
		return event;
	}

	
}
