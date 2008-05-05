package edu.unl.cse.activitygraph.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * ColorCycler is a utility class that acts like a circular linked list 
 * which contains colors.  
 * 
 * It is useful for assigning data to different series when plotting.
 */
public class ColorCycler {
	
	private ArrayList<Color> colors;
	private Iterator<Color> iter;
	
	public ColorCycler() {
		colors = new ArrayList<Color>();
		colors.add(Color.YELLOW);
		colors.add(Color.GREEN);
		colors.add(Color.RED);
		colors.add(Color.BLUE);
		colors.add(Color.PINK);
		colors.add(Color.MAGENTA);
		colors.add(Color.ORANGE);
		colors.add(Color.CYAN);
		
		
		iter = colors.iterator();
		
	}
	
	/**
	 * 
	 * @return a color
	 */
	public Color getNextColor() {
		if(!iter.hasNext()) {
			iter = colors.iterator();
		}
		return iter.next();
	}

}
