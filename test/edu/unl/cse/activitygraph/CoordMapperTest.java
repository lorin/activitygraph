package edu.unl.cse.activitygraph;


import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.unl.cse.activitygraph.util.CoordMapper;

public class CoordMapperTest {
	
	Calendar cal;
	CoordMapper cm;
	float delta =.01f; // Error for checking floating-point

	@Before
	public void setUp() throws Exception {
		cal = Calendar.getInstance();
		cal.clear();
		cal.set(2007,5,20,12,30,0);
		cm = new CoordMapper(cal.getTime());
		cal.clear();
		
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testTimeToXZero() {
		cal.set(2007,5,20,12,0,0);
		Date ref = cal.getTime();
		assertEquals(0.0f,cm.timeToX(ref),delta);
	}
	
	@Test
	public void testXZeroToTime() {
		cal.set(2007,5,20,12,0,0);
		Date ref = cal.getTime();
		assertEquals(ref,cm.xToTime(0.0f));
	}

	@Test
	public void testTimeToXTwoMinutes() {
		cal.set(2007,5,20,12,2,0);
		Date date = cal.getTime();
		assertEquals(2.0f,cm.timeToX(date),delta);
	}
	
	@Test
	public void testXTwoMinutesToTime() {
		cal.set(2007,5,20,12,2,0);
		Date date = cal.getTime();
		assertEquals(date,cm.xToTime(2.0f));
	}
}

