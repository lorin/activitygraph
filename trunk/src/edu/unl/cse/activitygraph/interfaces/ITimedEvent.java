package edu.unl.cse.activitygraph.interfaces;

import java.util.Date;

/**
 * A timed event, either a point (which occurs at only one point in time), or an interval.
 */
public interface ITimedEvent {

	/**
	 * 
	 * @return True if the event occurs over an interval of time, false otherwise
	 */
	boolean isInterval();

	Date getStartTime();
	Date getEndTime();
	String getNote();
	
	/**
	 * 
	 * @return The length of the timed event, in minutes. 
	 * 
	 * It may be zero if the event occurs at a single instant in time 
	 */
	float getLengthMin();

}
