package edu.unl.cse.activitygraph;


import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.unl.cse.activitygraph.Point;

public class PointTest {

	Calendar cal ;
	Point pt;

	
	@Before
	public void setUp() throws Exception {
		this.cal = Calendar.getInstance();
		this.cal.set(2007,6,20,14,15,15);
		this.pt = new Point(cal.getTime(),cal.getTime().toString());
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testEquals() {
		this.cal.set(2007,6,20,14,15,15);
		assertEquals(new Point(this.cal.getTime(),this.cal.getTime().toString()),this.pt);
	}
	
	@Test
	public void testIsInterval() {
		assertFalse(pt.isInterval());
	}

}
