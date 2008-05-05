package edu.unl.cse.activitygraph;
import java.awt.BasicStroke;
import java.awt.Color;
import java.util.Calendar;

import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.util.PAffineTransform;
import edu.umd.cs.piccolo.util.PPaintContext;




@SuppressWarnings("serial")
public class TickMark extends PPath {
		
	private float x,y;
	private float zoomThreshold;
	
	private static final float dayTickHeight = 20;
	private static final float hourTickHeight = 15;
	private static final float halfHourTickHeight = 10;
	private static final float quarterHourTickHeight = 7;
	private static final float fiveMinuteTickHeight = 5;
	private static final float minuteTickHeight = 3; 
	
	private static final float dayZoomThreshold = 0.0f;
	private static final float hourZoomThreshold = 0.1f;
	private static final float halfHourZoomThreshold = 2.0f;
	private static final float quarterHourZoomThreshold = 3.0f;
	private static final float fiveMinuteZoomThreshold = 4.0f;
	private static final float minuteZoomThreshold = 5.0f;
	
	public enum TickType {DAY, HOUR, HALFHOUR, QUARTERHOUR, FIVEMINUTE, MINUTE };
	
	
	
	
	public static TickType getTickType(Calendar cal) {
		int minute = cal.get(Calendar.MINUTE);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		
		if(hour==0 &&  minute==0) {
			return TickType.DAY;
		} else if (hour!=0 && minute==0) {
			return TickType.HOUR;
		} else if (minute==30) {
			return TickType.HALFHOUR;
		} else if (minute % 15==0) {
			return TickType.QUARTERHOUR;
		} else if (minute % 5==0) {
			return TickType.FIVEMINUTE;
		} else {
			return TickType.MINUTE;
		}
	}
	
	
	
	
	public static PPath createTick(float x, float y, Calendar cal) {
		switch(getTickType(cal)) {
		case DAY:
			return createDayTick(x,y);
		case HOUR:
			return createHourTick(x,y);
		default:
			return null;
		/*
		case HALFHOUR:
			return createHalfHourTick(x,y);
		case QUARTERHOUR:
			return createQuarterHourTick(x,y);
		case FIVEMINUTE:
			return createFiveMinuteTick(x,y);
		case MINUTE:
			return createMinuteTick(x,y);
		default: // Should never reach here
			return null;
		*/				
		}

	}

	public static PPath createDayTick(float x, float y) {
		PPath mark = TickMark.createLine(x, y, dayTickHeight,dayZoomThreshold);		
		mark.setStroke(new BasicStroke(2));
		return mark;
	}

	public static PPath createHourTick(float x, float y) {
		return TickMark.createLine(x, y, hourTickHeight, hourZoomThreshold);		
	}
	
	public static PPath createHalfHourTick(float x, float y) {
		return TickMark.createLine(x, y, halfHourTickHeight, halfHourZoomThreshold);		
	}
	
	public static PPath createQuarterHourTick(float x, float y) {
		return TickMark.createLine(x, y, quarterHourTickHeight, quarterHourZoomThreshold);		
	}
	
	public static PPath createFiveMinuteTick(float x, float y) {
		return TickMark.createLine(x, y, fiveMinuteTickHeight, fiveMinuteZoomThreshold);		
	}

	public static PPath createMinuteTick(float x, float y) {
		return TickMark.createLine(x, y, minuteTickHeight, minuteZoomThreshold);
	}
	

	public static TickMark createLine(float x, float y, float height, float zoomThreshold) {
		TickMark result = new TickMark(x,y,height,zoomThreshold);
		//GeneralPath result = new GeneralPath();
	    
		result.moveTo(x, y);
		result.lineTo(x, y-height);
		result.setPaint(Color.white);
		return result;
	}
	
	
	/**
	 * @param x x-value where to put the tick
	 * @param y y-value where to put the tick
	 * @param height height of tick 
	 * @param zoomThreshold threshold below which the tick should no longer be displayed
	 */
	public TickMark(float x, float y, float height, float zoomThreshold) {
		super();
		this.x = x;
		this.y = y;
		this.zoomThreshold = zoomThreshold;
	}
	
	protected void paint(PPaintContext paintContext) {
		
		/**
		 * We want the tick marks to not change in width, and to change in height based
		 * on the zoom. The more zoomed out we are, the smaller the height.
		 * 
		 */
		 		
		PAffineTransform tf = new PAffineTransform();
		
		// The translations are needed because the scaling has to be done about the 
		// origin of the data point
		// This is code borrowed from PAffineTransform.scaleAboutPoint, that can handle 
		// scalings across the different dimensions 
		
		double zoomX = paintContext.getGraphics().getTransform().getScaleX();

		if(zoomX<zoomThreshold) return; // Only plot if the zoom is above the threshold

	 
		
		double scaleX = 1/paintContext.getGraphics().getTransform().getScaleX();
		double scaleY = 1/paintContext.getGraphics().getTransform().getScaleY();
		

		tf.translate(x, y);
		tf.scale(scaleX, scaleY);
		tf.translate(-x, -y);
		
		paintContext.pushTransform(tf);

		// Paint the dot
		super.paint(paintContext);
		
		// Undo the transformation
		paintContext.popTransform(tf);
	}

	

}
