package edu.unl.cse.activitygraph.nodes;

import java.awt.Color;
import java.util.Date;

import edu.umd.cs.piccolo.PNode;
import edu.unl.cse.activitygraph.Interval;

public class IntervalNode extends PNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -650793299471369935L;
	private Interval interval;
			
	public IntervalNode(Interval interval, float x, float y, float width, float height, Color color) {
		this.interval = interval;
		setBounds(x,y,width,height);
		setPaint(color);
		
	}
	
	public Date getStartTime() {
		return this.interval.getStartTime();
	}


}
