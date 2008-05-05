package edu.unl.cse.activitygraph;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.unl.cse.activitygraph.Interval;
import edu.unl.cse.activitygraph.Point;
import edu.unl.cse.activitygraph.Series;
import edu.unl.cse.activitygraph.interfaces.ITimedEvent;

public class SeriesTest {
	
	Series series;
	Calendar cal;

	@Before
	public void setUp() throws Exception {
		series = new Series("Test Series");
		cal = Calendar.getInstance();
		cal.set(2007,6,20,12,0,0);
		Date start = cal.getTime();
		cal.set(2007,6,30,13,0,0);
		Date end = cal.getTime();
		String note="SeriesTest";
		Interval interval = new Interval(start,end,note);
		
		series.addEvent(interval);
		
		cal.set(2007,8,4,13,14,15);
		Point point = new Point(cal.getTime(),cal.getTime().toString());
		
		series.addEvent(point);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testName() {
		assertEquals("Test Series",series.getName());
	}
	
	@Test
	public void testEvents() {
		ArrayList<ITimedEvent> events = new ArrayList<ITimedEvent>();
		cal.set(2007,6,20,12,0,0);
		Date start = cal.getTime();
		cal.set(2007,6,30,13,0,0);
		Date end = cal.getTime();
		String note="SeriesTest";
		Interval interval = new Interval(start,end,note);
		events.add(interval);
		cal.set(2007,8,4,13,14,15);
		Point point = new Point(cal.getTime(),cal.getTime().toString());
		events.add(point);
		
		assertEquals(events.size(),this.series.getEvents().size());
		for(int i=0;i<events.size();++i) {
			assertEquals(events.get(i),this.series.getEvents().get(i));
		}		
	}
	
	

}
