package edu.unl.cse.activitygraph.hpcs;

/**
 * A captured event with an associated timestamp
 * 
 * @author Lorin Hochstein
 *
 */
public interface TimedEvent<T> extends Comparable<T> {

	java.sql.Timestamp getTimestamp();

}
