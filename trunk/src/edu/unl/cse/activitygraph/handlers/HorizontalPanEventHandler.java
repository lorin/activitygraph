package edu.unl.cse.activitygraph.handlers;
import java.awt.geom.Point2D;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.event.PPanEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.util.PDimension;


/**
 * A panner that only moves in the X direction
 */
public class HorizontalPanEventHandler extends PPanEventHandler {
	protected void pan(PInputEvent e) {
		PCamera c = e.getCamera();
		Point2D l = e.getPosition();
		
		if (c.getViewBounds().contains(l)) {
			PDimension d = e.getDelta();
			c.translateView(d.getWidth(), 0);
		}
	}
}
