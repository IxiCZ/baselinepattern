
package cz.muni.fi.lasaris.cep.drools.bpattern.functions;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import cz.muni.fi.lasaris.cep.drools.bpattern.domain.RepeatedLoginEvent;

/**
 * Drools accumulate function returning the collection of repeated login events with unique hostname.
 */
public class DistributedAttackFunction implements org.drools.base.accumulators.AccumulateFunction {

	protected static class MaxData implements Externalizable {

		public Map<String, RepeatedLoginEvent> rlEvents = new HashMap<String, RepeatedLoginEvent>();

		public MaxData() {
		}

		@SuppressWarnings("unchecked")
		public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
			rlEvents = (Map<String, RepeatedLoginEvent>) in.readObject();
		}

		public void writeExternal(ObjectOutput out) throws IOException {
			out.writeObject(rlEvents);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.drools.base.accumulators.AccumulateFunction#createContext()
	 */
	public Serializable createContext() {
		return new MaxData();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.drools.base.accumulators.AccumulateFunction#init(java.lang.Object)
	 */
	public void init(Serializable context) throws Exception {
		MaxData data = (MaxData) context;
		data.rlEvents.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.drools.base.accumulators.AccumulateFunction#accumulate(java.lang.
	 * Object, java.lang.Object)
	 */
	public void accumulate(Serializable context, Object value) {
		MaxData data = (MaxData) context;
		RepeatedLoginEvent event = (RepeatedLoginEvent) value;
		if (!data.rlEvents.containsKey(event.getHostname())) {
			data.rlEvents.put(event.getHostname(), event);
		}

	}

	public void reverse(Serializable context, Object value) throws Exception {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.drools.base.accumulators.AccumulateFunction#getResult(java.lang.Object
	 * )
	 */
	public Object getResult(Serializable context) throws Exception {
		MaxData data = (MaxData) context;
		return data.rlEvents.values();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.drools.base.accumulators.AccumulateFunction#supportsReverse()
	 */
	public boolean supportsReverse() {
		return false;
	}

	public Class<?> getResultType() {
		// TODO Auto-generated method stub
		return null;
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub

	}

	public void writeExternal(ObjectOutput out) throws IOException {
		// TODO Auto-generated method stub

	}
}