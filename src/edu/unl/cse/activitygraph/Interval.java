package edu.unl.cse.activitygraph;

import java.util.Calendar;
import java.util.Date;

/**
 * A time interval.
 *
 */
public class Interval extends AbstractTimedEvent {
	private Date startTime;
	private Date endTime;
	private String note;
	
	
	@SuppressWarnings("serial")
	static class InvalidIntervalException extends RuntimeException {

		public InvalidIntervalException() {
			super();
		}
		
	}
	

	public Interval(Date startTime, Date endTime,String intervalNote) {
		if((startTime==null) || (endTime==null) || endTime.before(startTime))  {
			throw new InvalidIntervalException();
		}
		this.startTime = (Date)startTime.clone();
		this.endTime = (Date)endTime.clone();
		this.note=intervalNote;
	}

	public Interval(Date startTime, Date endTime) {
		this(startTime,endTime,"");
	}

	public Date getEndTime() {
		return (Date)this.endTime.clone();
	}

	public Date getStartTime() {
		return (Date)this.startTime.clone();
	}
	public String getNote() {
		return this.note;
	}
	
	
	@Override
	public boolean equals(Object arg) {
		if((arg==null) || !(arg instanceof Interval)) {
			return false;
		}
		Interval other = (Interval)arg;
		return 	(this.startTime.equals(other.startTime)) &&
				(this.endTime.equals(other.endTime));
	}
	
	@Override
	public int hashCode() {
		assert false : "hashCode not designed";
		return 42;
	}

	@Override
	public String toString() {
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		start.clear();
		start.setTime(this.startTime);
		//String date=String.format("%tD", start);
		String startTime=String.format("%tR", start);
		end.clear();
		end.setTime(this.endTime);
		String endTime=String.format("%tR", end);
		return startTime + "," + endTime;
		//return date+"[" + startTime + "," + endTime + "]";
	}

	public boolean isInterval() {
		return true;
	}

	public float getLengthMin() {
		return (getEndTime().getTime() - getStartTime().getTime()) / (1000.0f*60.0f); 
	}
	
	

}
