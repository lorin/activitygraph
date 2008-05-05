package edu.unl.cse.activitygraph.hpcs;


public abstract class ComparableTimedEvent implements TimedEvent<ComparableTimedEvent> {


	public int compareTo(ComparableTimedEvent evt) {
		return this.getTimestamp().compareTo(evt.getTimestamp());
	}

}
