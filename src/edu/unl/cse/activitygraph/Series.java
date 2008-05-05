package edu.unl.cse.activitygraph;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import edu.umd.cs.piccolo.nodes.PText;
import edu.unl.cse.activitygraph.interfaces.ITimedEvent;

/**
 * Series is a class which represents a series of points or intervals that
 * occur over time.
 * 
 *
 */
public class Series {
	
	private String name;
	List<ITimedEvent> events;
	private Color color;
	private float yLabelPos;
	public PText yLabel;

	
	public Series(String name) {
		this.name = name;
		events = new ArrayList<ITimedEvent>();
	}

	public String getName() {
		return name;
	}

	public List<ITimedEvent> getEvents() {
		return events;
	}

	public void addEvent(ITimedEvent event) {
		events.add(event);
		
	}


	/**
	 * 
	 * @return The length of time occupied by this series, in minutes
	 */
	public float getLengthMin() {
		float total = 0;
		for(ITimedEvent event : this.events) {
			total += event.getLengthMin();
		}
		return total;
	}

	public Color getColor() {
		
		return this.color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public float getyLabelPos() {
		
		return this.yLabelPos;
	}
	
	public void setyLabelPos(float y) {
		this.yLabelPos = y;
	}

	/**
	 * @return the number of events in the series
	 */
	public int getEventCount() {
		
		return this.events.size();
	}
	

}
