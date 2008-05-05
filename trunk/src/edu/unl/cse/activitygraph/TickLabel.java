package edu.unl.cse.activitygraph;
import java.util.Calendar;


import edu.umd.cs.piccolo.util.PPaintContext;


@SuppressWarnings("serial")
public class TickLabel extends PTextStretched {

	private static final float hourZoomThreshold = 0.1f;
	private static TickLabel tl;
	
	private float zoomThreshold;
	private String tooltip = null; 


	public static TickLabel createLabel(float x, float y, Calendar cal) {
		// We'll only create labels for hours for now
		//if want  month labels need add a midday item in type ????????
		
		TickMark.TickType type = TickMark.getTickType(cal);
		if(type==TickMark.TickType.HOUR ||type==TickMark.TickType.DAY)
		{
			String date = String.format("%tD %tR",cal.getTime(), cal.getTime());
			tl = new TickLabel(String.valueOf(cal.get(Calendar.HOUR_OF_DAY)),hourZoomThreshold,x,y,date);
			
			return tl;
		}		

		else{
			TickLabel tl = new TickLabel("",0, x, y, null);
			tl.setVisible(false);
			return tl;
		}

	}
	
	/**
	 * Center the text at the x,y coordinate specified
	 * @param x x-coordinate
	 * @param y y-coordinate
	 */
	private void centerAt(float x, float y) {
		/*
		 * Adjust the bounding box (x,y,w,h) -> (x',y',w',h') as follows:
		 * 
		 * x' = x - w
		 * y' = y
		 * w' = 2w
		 * h' = h
		 * 
		 */
		this.setJustification(javax.swing.JLabel.CENTER_ALIGNMENT);
		this.setConstrainWidthToTextWidth(false);
		this.setX(x - this.getWidth());
		this.setY(y);
		this.setWidth(this.getWidth()*2);
				
	}

	public TickLabel(String text, float zoomThreshold, float x, float y, String tooltip) {
		super(text, x, y);
		this.zoomThreshold = zoomThreshold;
		this.tooltip = tooltip;
		this.centerAt(x,y);
		this.addAttribute("tooltip",tooltip);
	}

	@Override
	protected void paint(PPaintContext paintContext) {
		double zoomX = paintContext.getGraphics().getTransform().getScaleX();
		//Don't paint if the scale goes below 0.25
		if(zoomX<0.25f) return;
		if(zoomX<zoomThreshold) return; // Only plot if the zoom is above the threshold		
		super.paint(paintContext);
	}

	@Override
	public void setVisible(boolean isVisible) {
		super.setVisible(isVisible);
		if(isVisible) {
			// show the tooltip
			this.addAttribute("tooltip", tooltip);
		} else {
			// hide the tooltip
			this.addAttribute("tooltip", null);
		}
	}


}

