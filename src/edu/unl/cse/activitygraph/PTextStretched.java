package edu.unl.cse.activitygraph;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolo.util.PAffineTransform;
import edu.umd.cs.piccolo.util.PPaintContext;

@SuppressWarnings("serial")
public class PTextStretched extends PText {

	public PTextStretched(String string) {
		super(string);
	}

	@Override
	protected void paint(PPaintContext paintContext) {
		// Scale the "path" object the opposite of the zoom
		// So, if we zoom in 2X, then we make the object 2X smaller so it's rendered the same size
		
		
		PAffineTransform tfRef = new PAffineTransform(paintContext.getGraphics().getTransform());
		double scaleX = tfRef.getScaleX();
		double scaleY = tfRef.getScaleY();
		
		// Don't paint if the scale goes below 0.25
		if(scaleX<=0.25) return;
		
		PAffineTransform tf = new PAffineTransform();
		

		
		// The translations are needed because the scaling has to be done about the 
		// origin of the data point

		float x = (float) getX();
		float y = (float) getY();
		
		tf.translate(x, y);
		tf.scale(1/scaleX, 1/scaleY);
		tf.translate(-x, -y);
		
		paintContext.getGraphics().transform(tf);


		// Paint the dot
		super.paint(paintContext);
		


		// Undo the transformation
		PAffineTransform tfinv = new PAffineTransform();
		tfinv.translate(x, y);
		tfinv.scale(scaleX, scaleY);
		tfinv.translate(-x, -y);
		paintContext.getGraphics().transform(tfinv);

	}
	

}