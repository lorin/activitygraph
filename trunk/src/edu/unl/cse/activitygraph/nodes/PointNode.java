package edu.unl.cse.activitygraph.nodes;

import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.util.Date;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.util.PAffineTransform;
import edu.umd.cs.piccolo.util.PPaintContext;
import edu.unl.cse.activitygraph.Point;

/**
 * A node that represents a data point that exists at a single instant in time.
 * 
 * It stays the same regardless of zoom setting.
 *
 *
 */

public class PointNode extends PPath {

	private static final long serialVersionUID = 2012648675640235991L;
	
	private float x,y;

	private Point point;
	
	public PointNode(Point point, Ellipse2D ellipse, float x, float y, Color color) {
		super(ellipse);
		this.point = point;
		this.x = x;
		this.y = y;
		this.setPaint(color);
		this.setStroke(null);
	}

	public static PNode createPointNode(Point point, float x, float y, float radius, Color color) {
		Ellipse2D ellipse = new Ellipse2D.Float();
		ellipse.setFrame(x,y,radius,radius);
		return new PointNode(point, ellipse,x,y,color);
	}
	
	public Date getStartTime() {
		return this.point.getStartTime();
	}
	
	@Override
	protected void paint(PPaintContext paintContext) {
		
		/**
		 * We want to have the data points drawn the same size regardless of the current zoom,
		 * so whatever the zoom is, we undo it before painting
		 */
		 		
		PAffineTransform tf = new PAffineTransform();
		
		// The translations are needed because the scaling has to be done about the 
		// origin of the data point
		// This is code borrowed from PAffineTransform.scaleAboutPoint, that can handle 
		// scalings across the different dimensions 
				
		tf.translate(x, y);
		tf.scale(1/paintContext.getGraphics().getTransform().getScaleX(), 
				 1/paintContext.getGraphics().getTransform().getScaleY());
		tf.translate(-x, -y);
		
		paintContext.pushTransform(tf);

		// Paint the dot
		super.paint(paintContext);
		
		// Undo the transformation
		paintContext.popTransform(tf);
	}

	

}
