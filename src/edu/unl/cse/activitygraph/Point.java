package edu.unl.cse.activitygraph;

import java.util.Calendar;
import java.util.Date;

import edu.unl.cse.activitygraph.interfaces.ITimedEvent;

/**
 * A Point is a single instant in time.
 */
public class Point implements ITimedEvent {
	private Date time;
	private String note;

	public Point(Date time,String pointNote) {
		this.time = (Date)time.clone();
		this.note=pointNote;
	}

	public Point(Date time) {
		this(time,"");
	}

	public Date getTime() {
		return (Date)this.time.clone();
	}

	public Date getEndTime() {		
		return (Date)this.time.clone();
	}

	public Date getStartTime() {
		return (Date)this.time.clone();
	}
	public String getNote() {
		return this.note;
	}
	
	
	@Override
	public boolean equals(Object arg) {
		if((arg==null) || !(arg instanceof Point)) {
			return false;
		}
		Point other = (Point)arg;
		return this.time.equals(other.time);
	}

	@Override
	public int hashCode() {
		assert false : "hashCode not designed";
		return 42;
	}
	public String toString(){
		Calendar start = Calendar.getInstance();
		start.clear();
		start.setTime(this.time);
		//String date=String.format("%tD", start);
		String startTime=String.format("%tR", start);
		//return date+"[" + startTime + "]";
		return startTime ;
		
	}
	
	public boolean isInterval() {
		return false;
	}

	/**
	 * Return the length of time, in minutes
	 */
	public float getLengthMin() {
		// Points have no elapsed time
		return 0;
	}

}
