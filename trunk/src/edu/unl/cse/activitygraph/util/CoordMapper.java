package edu.unl.cse.activitygraph.util;

import java.util.Calendar;
import java.util.Date;

/**
 * CoordMapper translates from a time instant to an x-axis coordinate.
 * It is used for determining where to plot time data.
 * 
 * CoordMapper is designed so that time(x=0) is always on an hour
 */
public class CoordMapper {
	
	Date refTime;
	Calendar refCal;
	static float msInMinute = 1000.0f * 60.0f;

	public CoordMapper(Date firstEventTime) {
		/*
		 * The reference time is the time of the first event, rounded down
		 * to the nearest hour. 
		 * 
		 * So, if the first event happens at 9:35, the reference time is 9:00
		 */
		refCal = Calendar.getInstance();
		refCal.setTime(firstEventTime);
		refCal.set(Calendar.MINUTE,0);
		refCal.set(Calendar.SECOND,0);
		refCal.set(Calendar.MILLISECOND,0);
		this.refTime = refCal.getTime();
	}

	/**
	 * 
	 * @param time a time instant
	 * @return the x-value corresponding to the specified time
	 */
	public float timeToX(Date time) {	
		return (time.getTime() - this.refTime.getTime()) / msInMinute;
	}
	
	/**
	 * 
	 * @return the calendar object that corresponds to x=0
	 */
	public Calendar getRefCalendar() {
		return refCal;
	}

	/**
	 * 
	 * @param x an x-value
	 * @return the time corresponding to the x-value
	 */
	public Date xToTime(float x) {
		Calendar cal = Calendar.getInstance();
		long ms = (long) (((double)x)*msInMinute + this.refTime.getTime());
		cal.setTimeInMillis(ms);
		return cal.getTime();
	}
}
