package cz.muni.fi.lasaris.cep.drools.bpattern.rules;

import java.util.HashMap;
import java.util.Map;

import org.drools.event.rule.ActivationCancelledEvent;
import org.drools.event.rule.ActivationCreatedEvent;
import org.drools.event.rule.AfterActivationFiredEvent;
import org.drools.event.rule.AgendaEventListener;
import org.drools.event.rule.AgendaGroupPoppedEvent;
import org.drools.event.rule.AgendaGroupPushedEvent;
import org.drools.event.rule.BeforeActivationFiredEvent;
import org.drools.event.rule.RuleFlowGroupActivatedEvent;
import org.drools.event.rule.RuleFlowGroupDeactivatedEvent;

public class FiredRulesListener implements AgendaEventListener {

	private Map<String, Integer> firedRules = new HashMap<String, Integer>();

	public boolean isRuleFired(String rule) {
		return firedRules.containsKey(rule);
	}

	public int howManyTimesIsRuleFired(String rule) {
		if (!firedRules.containsKey(rule)) {
			return 0;
		}
		return firedRules.get(rule);
	}

	public void clear() {
		firedRules.clear();
	}

	public void afterActivationFired(AfterActivationFiredEvent event) {
		String rule = event.getActivation().getRule().getName();
		if (firedRules.containsKey(rule)) {
			firedRules.put(rule, firedRules.get(rule) + 1);
		} else {
			firedRules.put(rule, 1);
		}

	}

	public void activationCreated(ActivationCreatedEvent event) {
		// TODO Auto-generated method stub

	}

	public void activationCancelled(ActivationCancelledEvent event) {
		// TODO Auto-generated method stub

	}

	public void afterRuleFlowGroupActivated(RuleFlowGroupActivatedEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void afterRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void agendaGroupPopped(AgendaGroupPoppedEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void agendaGroupPushed(AgendaGroupPushedEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void beforeActivationFired(BeforeActivationFiredEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void beforeRuleFlowGroupActivated(RuleFlowGroupActivatedEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void beforeRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent arg0) {
		// TODO Auto-generated method stub
		
	}


}
