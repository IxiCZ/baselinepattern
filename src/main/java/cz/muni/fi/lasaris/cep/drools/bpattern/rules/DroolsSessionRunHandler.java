package cz.muni.fi.lasaris.cep.drools.bpattern.rules;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderConfiguration;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.conf.EventProcessingOption;
import org.drools.io.impl.ClassPathResource;
import org.drools.runtime.KnowledgeSessionConfiguration;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.conf.ClockTypeOption;
import org.drools.time.SessionPseudoClock;

import cz.muni.fi.lasaris.cep.drools.bpattern.domain.Event;
import cz.muni.fi.lasaris.cep.drools.bpattern.outcome.DistributedAttackReport;

public class DroolsSessionRunHandler {

	private StatefulKnowledgeSession ksession;
	private KnowledgeBase kbase;

	/**
	 * Inserts the given events into the session and fires the rules.
	 * 
	 * @param events
	 *            to be inserted into the session
	 */
	public void run(List<Event> events) {
		setUp();

		ksession.fireAllRules();
		SessionPseudoClock clock = ksession.getSessionClock();

		long prevTime = 0;

		long startTime = System.currentTimeMillis();
		System.out.println("Started inserting events:");
		//
		for (Event e : events) {
			clock.advanceTime(e.getOccurenceTime() - prevTime, TimeUnit.MILLISECONDS);
			ksession.insert(e);

			prevTime = e.getOccurenceTime();
			ksession.fireAllRules();

		}
		System.out.println("Finished: " + (System.currentTimeMillis() - startTime));

		tearDown();
	}

	/**
	 * Prepares the session from rules.
	 */
	private void setUp() {
		KnowledgeBuilderConfiguration kbconf = KnowledgeBuilderFactory.newKnowledgeBuilderConfiguration();
		kbconf.setProperty("drools.accumulate.function.distributedAttack",
				"cz.muni.fi.lasaris.cep.drools.bpattern.functions.DistributedAttackFunction");

		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder(kbconf);

		kbuilder.add(new ClassPathResource("baseline-pattern.drl", getClass()), ResourceType.DRL);

		KnowledgeBaseConfiguration config = KnowledgeBaseFactory.newKnowledgeBaseConfiguration();
		config.setOption(EventProcessingOption.STREAM);

		kbase = KnowledgeBaseFactory.newKnowledgeBase(config);
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());

		KnowledgeSessionConfiguration conf = KnowledgeBaseFactory.newKnowledgeSessionConfiguration();
		conf.setOption(ClockTypeOption.get("pseudo"));
		ksession = kbase.newStatefulKnowledgeSession(conf, null);
		ksession.setGlobal("daReport", new DistributedAttackReport());
	}

	/**
	 * Disposes of the session.
	 */
	private void tearDown() {
		if (ksession != null) {
			ksession.dispose();
		} else {
			System.err.println("KSession was null.");
		}
	}

}
