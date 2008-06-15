package edu.unl.cse.activitygraph.util;

import java.util.TimeZone;

/**
 * A singleton class that allows client to set the time zone of the data 
 * as well as retrieve it.
 * 
 * This is useful because the data may be in a different time zone from
 * the local one.
 */
public class DataTimeZone {
	static private TimeZone timeZone = TimeZone.getDefault();
	
	static public TimeZone getTimeZone() {
		return timeZone;
	}
	
	/**
	 * Set the time zone using a string ID. For example:  America/New_York
	 * For further examples, see http://www.statoids.com/tus.html
	 * 
	 * @param id The string ID that describes the time zone.
	 * 
	 */
	static public void setByString(String id) {
		timeZone = TimeZone.getTimeZone(id);
	}
}
