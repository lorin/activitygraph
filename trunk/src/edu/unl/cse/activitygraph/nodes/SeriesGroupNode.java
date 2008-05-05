package edu.unl.cse.activitygraph.nodes;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.util.PBounds;
import edu.unl.cse.activitygraph.SeriesGroup;

/**
 * A SeriesGroupNode is a node that represents a {@link SeriesGroup} object.
 *
 */
public class SeriesGroupNode extends PNode {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1593416506774349124L;
	
	private boolean isExpanded = true;
	

	/**
	 * Collapse all of the series nodes into the same line
	 *
	 */
	private void collapse() {					
		float yCenter = this.getCenterLocalY();
		for(Object obj : this.getChildrenReference()) {
			PNode child = (PNode)obj;
			PBounds childBounds = child.getFullBounds();
			double ydist = yCenter - childBounds.y;
			child.animateToPositionScaleRotation(0, ydist, 1, 0, 1000);
		}
	}
	
	/**
	 * Expand the nodes from the same line into separate lines
	 *
	 */
	private void expand() {
		for(Object obj : this.getChildrenReference()) {
			PNode series = (PNode)obj;
			series.animateToPositionScaleRotation(0, 0, 1, 0, 1000);
		}
	}
	
	/**
	 * Toggle between collapsed and expanded view
	 *
	 */
	public void toggle() {
		if(isExpanded) {
			isExpanded = false;
			collapse();
		} else {
			isExpanded = true;
			expand();
		}
	}

	/**
	 * 
	 * @return the y-value of the center of the series group, in local coordinates
	 */
	private float getCenterLocalY() {
		PBounds bounds = this.getFullBounds();
		return (float) (bounds.y + bounds.height / 2);
		
	}
}
