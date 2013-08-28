package cz.muni.fi.lasaris.cep.drools.bpattern.outcome;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import cz.muni.fi.lasaris.cep.drools.bpattern.domain.RepeatedLoginEvent;

/**
 * Class with methods to be called from rules. 
 */
public class DistributedAttackReport {

	/**
	 * Reports the distributed attack event.
	 * 
	 * @param rlEvents Repeated login events from which will be created the report 
	 */
	@SuppressWarnings("unchecked")
	public void reportAttack(Collection<RepeatedLoginEvent> rlEvents) {
		//System.out.println("Attack" + rlEvents);
		JSONObject jsonObjectInner = new JSONObject();

		JSONArray hostnames = new JSONArray();
		JSONArray usernames = new JSONArray();
		Set<String> users = new HashSet<String>();
		for (RepeatedLoginEvent rle : rlEvents) {
			hostnames.add( rle.getHostname());
			users.add(rle.getUsername());
		}
		usernames.add(users);
		jsonObjectInner.put("hostnames", hostnames);
		jsonObjectInner.put("hostsNumber", rlEvents.size());
		jsonObjectInner.put("users", usernames);

		System.out.println(jsonObjectInner);
		
		JSONObject complexEvent = new JSONObject();
		complexEvent.put("id", 0);
		complexEvent.put("occurrenceTime", new Date());
		complexEvent.put("hostname", "processing-agent-XX.fi.muni.cz");
		complexEvent.put("entity", "cloud1-group");
		complexEvent.put("type", "cz.muni.fi.ngmon.DISTRIBUOVANY_SL_UTOK");
		complexEvent.put("http:"+"/" + "/ " + "ngmon.fi.muni.cz/v1.0/cplxevents.jsch", jsonObjectInner);
		
		JSONObject result = new JSONObject();
		result.put("ComplexEvent",complexEvent);
	
		
		//System.out.println(result);
		
	}
}
