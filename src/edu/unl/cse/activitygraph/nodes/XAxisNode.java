package edu.unl.cse.activitygraph.nodes;
import java.util.Calendar;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.unl.cse.activitygraph.TickMark;
import edu.unl.cse.activitygraph.util.CoordMapper;

@SuppressWarnings("serial")
public class XAxisNode extends PNode {
	private static final int resolutionMinutes = 60; // Tick mark resolution in minutes  
	
	public XAxisNode(CoordMapper mapper, Calendar last, float y)
	{
		Calendar endpoint = makeEndpoint(last);
		this.addChild(makeLine(mapper.timeToX(endpoint.getTime())));
		makeTicks(mapper.getRefCalendar(),endpoint, y);
		
	}
	
	/**
	 * Calculate the end point of the x-axis, using the last event.
	 * 
	 * This is done by rounding the last event up to the nearest hour
	 * 
	 * @param last The last recorded event
	 * @return the end point for the x-axis, which ends on the hour after <em>last</em>
	 */
	private static Calendar makeEndpoint(Calendar last) {
		Calendar endpoint = (Calendar) last.clone();
		// Fast forward end point up to the next hour if it's not already at an hour
		if (endpoint.get(Calendar.MINUTE) != 0) {
			endpoint.add(Calendar.MINUTE, 60 - last.get(Calendar.MINUTE));
		}
		return endpoint;
	}
	/**
	 * 
	 * @param xlast  the last x-value
	 * @return       a node representing the line 
	 */
	private static PNode makeLine(float xlast) {
		return PPath.createLine(0,0,xlast,0);
	}

	/**
	 * Make the ticks and labels
	 * @param first	  The calendar date corresponding to x=0
	 * @param last	  The calendar date corresponding to the last time
	 */
	private void makeTicks(Calendar first, Calendar last, float y) {
		Calendar cal;
		float x;
		for(cal=(Calendar)first.clone(),x=0;
		    cal.before(last) || cal.equals(last);
		    cal.add(Calendar.MINUTE, resolutionMinutes), x+=resolutionMinutes)
		{
			PNode node = createTickAndLabel(cal,x, y);
			this.addChild(node);
		}
	}

	private PNode createTickAndLabel(Calendar cal, float x, float y) {
		
		//int day = cal.get(Calendar.DAY_OF_WEEK);
		int minute = cal.get(Calendar.MINUTE);
		//int hour = cal.get(Calendar.HOUR_OF_DAY);
		
		PNode node = null;

		if(minute==0) {
			node = new HourTickMarkAndLabel(x,y);
		}
		return node;
	}
	
	class TickMarkAndLabel extends PNode {
		public TickMarkAndLabel(float x, float y, float height, float zoomThreshold ) {
			TickMark mark = new TickMark(x,y,height, zoomThreshold);
			mark.addChild(this);
		}
		
	};
	
	class HourTickMarkAndLabel extends TickMarkAndLabel{
		private static final float height = 20.0f;
		private static final float zoomThreshold = 0.0f; //0.25f;

		public HourTickMarkAndLabel(float x, float y) {
			super(x,y,height,zoomThreshold);
		}

	}
			
}
