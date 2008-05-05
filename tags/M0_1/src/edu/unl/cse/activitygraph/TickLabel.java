package edu.unl.cse.activitygraph;
import java.util.Calendar;

import edu.umd.cs.piccolo.util.PPaintContext;


@SuppressWarnings("serial")
public class TickLabel extends PTextStretched {

	private static final float hourZoomThreshold = 0.1f;
	
	
	private float zoomThreshold;

	
	public static TickLabel createLabel(float x, float y, Calendar cal) {
		// We'll only create labels for hours for now
		//if want  month labels need add a midday item in type ????????
		TickMark.TickType type = TickMark.getTickType(cal);
		if(type==TickMark.TickType.HOUR || type==TickMark.TickType.DAY)
		{
			TickLabel tl = new TickLabel(String.valueOf(cal.get(Calendar.HOUR_OF_DAY)),hourZoomThreshold);
			tl.setX(x-5);
			tl.setY(y);
			return tl;
		}else{
			TickLabel tl = new TickLabel("",0);
			tl.setVisible(false);
			return tl;
		}

	}
	


	public TickLabel(String text, float zoomThreshold) {
		super(text);
		this.zoomThreshold = zoomThreshold;
	}

	@Override
	protected void paint(PPaintContext paintContext) {
		double zoomX = paintContext.getGraphics().getTransform().getScaleX();

		if(zoomX<zoomThreshold) return; // Only plot if the zoom is above the threshold

		super.paint(paintContext);
	}
}

