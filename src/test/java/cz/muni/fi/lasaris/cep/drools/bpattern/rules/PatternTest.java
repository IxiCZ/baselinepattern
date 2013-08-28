package cz.muni.fi.lasaris.cep.drools.bpattern.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cz.muni.fi.lasaris.cep.drools.bpattern.domain.LoginEvent;
import cz.muni.fi.lasaris.cep.drools.bpattern.domain.RepeatedLoginEvent;
import cz.muni.fi.lasaris.cep.drools.bpattern.outcome.DistributedAttackReport;

/**
 * Testing rules for baseline pattern.
 */

public class PatternTest {

	private StatefulKnowledgeSession ksession;
	private KnowledgeBase kbase;
	private FiredRulesListener firedRules;

	@Before
	public void setUp() {
		KnowledgeBuilderConfiguration kbconf = KnowledgeBuilderFactory.newKnowledgeBuilderConfiguration();
		kbconf.setProperty("drools.accumulate.function.distributedAttack",
				"cz.muni.fi.lasaris.cep.drools.bpattern.functions.DistributedAttackFunction");

		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder(kbconf);

		kbuilder.add(new ClassPathResource("baseline-pattern.drl", getClass()), ResourceType.DRL);
		Assert.assertFalse(kbuilder.getErrors().toString(), kbuilder.hasErrors());

		KnowledgeBaseConfiguration config = KnowledgeBaseFactory.newKnowledgeBaseConfiguration();
		config.setOption(EventProcessingOption.STREAM);

		kbase = KnowledgeBaseFactory.newKnowledgeBase(config);
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());

		KnowledgeSessionConfiguration conf = KnowledgeBaseFactory.newKnowledgeSessionConfiguration();
		conf.setOption(ClockTypeOption.get("pseudo"));
		ksession = kbase.newStatefulKnowledgeSession(conf, null);
		ksession.setGlobal("daReport", new DistributedAttackReport());

		firedRules = new FiredRulesListener();
		ksession.addEventListener(firedRules);

	}

	@Test
	public void repeatedLogin() {
		String rule = "REPEATED_LOGIN";
		ksession.fireAllRules();
		SessionPseudoClock clock = ksession.getSessionClock();

		for (int i = 1; i < 2500; i++) {
			// System.out.println("inserting time= " +i);
			clock.advanceTime(1, TimeUnit.MILLISECONDS);

			LoginEvent lEvent = new LoginEvent();
			lEvent.setSourceHost("HostName " + i % 2);
			lEvent.setUser("UserName");
			lEvent.setSuccess(false);
			lEvent.setOccurenceTime(i);

			ksession.insert(lEvent);
			ksession.fireAllRules();
		}

		assertEquals(1, firedRules.howManyTimesIsRuleFired(rule));
	}

	@Test
	public void repeatedLoginWindow() {
		String rule = "REPEATED_LOGIN";
		ksession.fireAllRules();
		SessionPseudoClock clock = ksession.getSessionClock();

		for (int i = 1; i <= 1001; i++) {
			clock.advanceTime(60, TimeUnit.MILLISECONDS);
			// System.out.println("inserting time= " + clock.getCurrentTime());

			LoginEvent lEvent = new LoginEvent();
			lEvent.setSourceHost("HostName");
			lEvent.setUser("UserName");
			lEvent.setSuccess(false);

			// window is 60s = 60 000ms => for 1001 events the 60ms separations
			// are enough for the rule not to fire
			lEvent.setOccurenceTime(i * 60);
			lEvent.setHost("HostName");

			ksession.insert(lEvent);
			ksession.fireAllRules();
		}

		assertFalse(firedRules.isRuleFired(rule));

		for (int i = 1; i <= 1001; i++) {
			clock.advanceTime(59, TimeUnit.MILLISECONDS);
			// System.out.println("inserting time= " + clock.getCurrentTime());

			LoginEvent lEvent = new LoginEvent();
			lEvent.setSourceHost("HostName");
			lEvent.setUser("UserName");
			lEvent.setSuccess(false);

			lEvent.setOccurenceTime(i * 59);
			lEvent.setHost("HostName");

			ksession.insert(lEvent);
			ksession.fireAllRules();
		}

		assertTrue(firedRules.isRuleFired(rule));
	}

	@Test
	public void repeatedLoginNotFireMore() {
		String rule = "REPEATED_LOGIN";
		ksession.fireAllRules();
		SessionPseudoClock clock = ksession.getSessionClock();

		for (int i = 1; i <= 1010; i++) {
			clock.advanceTime(40, TimeUnit.MILLISECONDS);
			// System.out.println("inserting time= " + clock.getCurrentTime());

			LoginEvent lEvent = new LoginEvent();
			lEvent.setSourceHost("HostName");
			lEvent.setUser("UserName");
			lEvent.setSuccess(false);

			lEvent.setOccurenceTime(i * 40);
			lEvent.setHost("HostName");

			ksession.insert(lEvent);
			ksession.fireAllRules();
		}

		assertEquals(1, firedRules.howManyTimesIsRuleFired(rule));
	}

	@Test
	public void distributedAtack() {
		String rule = "DISTRIBUTED_DICT_ATTACK";
		ksession.fireAllRules();
		SessionPseudoClock clock = ksession.getSessionClock();

		for (int i = 0; i < 40; i++) {
			clock.advanceTime(10, TimeUnit.SECONDS);

			ksession.insert(new RepeatedLoginEvent("hostname" + i, "username", 1005));
			ksession.fireAllRules();
		}
		assertEquals(3, firedRules.howManyTimesIsRuleFired(rule));
	}

	@Test
	public void distributedAtackSize() {
		String rule = "DISTRIBUTED_DICT_ATTACK";
		ksession.fireAllRules();
		SessionPseudoClock clock = ksession.getSessionClock();

		for (int i = 0; i < 12; i++) {
			// System.out.println("inserting time= " +i);
			clock.advanceTime(1, TimeUnit.MILLISECONDS);

			ksession.insert(new RepeatedLoginEvent("hostname" + i, "username", 2));
			ksession.fireAllRules();
		}

		assertEquals(1, firedRules.howManyTimesIsRuleFired(rule));
	}

	@Test
	public void distributedAtackWindowTooLongPr() {
		String rule = "DISTRIBUTED_DICT_ATTACK";
		ksession.fireAllRules();
		SessionPseudoClock clock = ksession.getSessionClock();

		for (int i = 0; i < 30; i++) {
			// System.out.println("inserting time= " +i);
			clock.advanceTime(1, TimeUnit.MINUTES);

			ksession.insert(new RepeatedLoginEvent("hostname" + i, "username", 2));
			ksession.fireAllRules();
		}

		assertEquals(0, firedRules.howManyTimesIsRuleFired(rule));
	}

	@After
	public void tearDown() {
		if (ksession != null) {
			ksession.dispose();
		} else {
			System.err.println("KSession was null.");
		}
	}
}
