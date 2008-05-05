package edu.unl.cse.activitygraph;
import java.awt.geom.Ellipse2D;


import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.util.PAffineTransform;
import edu.umd.cs.piccolo.util.PPaintContext;

/**
 * A node that represents a data point.
 * 
 * It stays the same size regardless of zoom setting.
 * 
 * @author Lorin Hochstien
 *
 */

@SuppressWarnings("serial")
public class DataPoint extends PPath {
	
	private float x,y;
	
	public static DataPoint createDataPoint(float x, float y, float width, float height) {
		Ellipse2D ellipse = new Ellipse2D.Float();
		ellipse.setFrame(x, y, width, height);
		return new DataPoint(ellipse,x,y);
		
	}
	
	private DataPoint(Ellipse2D ellipse,float x, float y) {
		super(ellipse);
		this.x = x;
		this.y = y;
		
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
