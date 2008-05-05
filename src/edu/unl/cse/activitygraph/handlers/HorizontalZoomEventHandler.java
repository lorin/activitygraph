package edu.unl.cse.activitygraph.handlers;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.event.PZoomEventHandler;
import edu.umd.cs.piccolo.util.PAffineTransform;

import edu.umd.cs.piccolo.PCamera;

import java.awt.geom.Point2D;


/**
 * A zoomer that only zooms in the x-direction.
 */
public class HorizontalZoomEventHandler extends PZoomEventHandler {
	
	// This is the same as viewZoomPoint in the parent, but it's private so we need
	// to store it ourselves as well
	private Point2D myViewZoomPoint; 
	
	
	@Override
	protected void dragActivityFirstStep(PInputEvent aEvent) {
		myViewZoomPoint = aEvent.getPosition();
		super.dragActivityFirstStep(aEvent);
	}
	

	@Override
	protected void dragActivityStep(PInputEvent aEvent) {
		PCamera camera = aEvent.getCamera();
		double dx = aEvent.getCanvasPosition().getX() - getMousePressedCanvasPoint().getX();
		double scaleDelta = (1.0 + (0.001 * dx));

		double currentScale = camera.getViewScale();
		double newScale = currentScale * scaleDelta;

		if (newScale < getMinScale()) {
			scaleDelta = getMinScale() / currentScale;
		}
		if ((getMaxScale() > 0) && (newScale > getMaxScale())) {
			scaleDelta = getMaxScale() / currentScale;
		}
		
		PAffineTransform viewTransform = camera.getViewTransformReference();
		double x = myViewZoomPoint.getX();
		double y = myViewZoomPoint.getY();
		double scale = scaleDelta;
        viewTransform.translate(x,y);
        viewTransform.scale(scale,1);
        camera.translateView(-x, -y);
		
	}


}
