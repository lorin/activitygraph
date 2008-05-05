package edu.unl.cse.activitygraph;


import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ShiftCalculatorTest {
	
	Calendar cal;
	private int paddingMinutes;
	private int bigGapMinutes;
	Date D1,D2,D3,D4,D5,D6,D7,D8,D9,D10;
	
	LinkedList<ShiftCalculator.WorkInterval> workIntervals; 
	
	ShiftCalculator scSingleInterval,scTwoIntervals,closeScTwoInterval,threeIntervals,fiveIntervals;

	@Before
	public void setUp() throws Exception {
		bigGapMinutes = 50;
		paddingMinutes = 15;
       //D1 D2 D5 D6 D3 D4 D7 D8
	   //D1 2006,9,30,5,45    D2 2006,9,30,6,00   D5 2006,10,1,7,01  D6 2006,10,1,7,30 
	   //D3 2006,10,1,8,30    D4 2006,10,1,9,00   D7                 D8
		cal = Calendar.getInstance();
		cal.set(2006,9,30,5,45,00);
		D1=cal.getTime();
		cal.set(2006,9,30,6,00,00);
		D2=cal.getTime();
		cal.set(2006,10,1,8,30,00);
		D3=cal.getTime();
		cal.set(2006,10,1,9,00,00);
		D4=cal.getTime();
		cal.set(2006,10,1,7,01,00);
		D5 = cal.getTime();
		cal.set(2006,10,1,7,30,00);
		D6 = cal.getTime();
		cal.set(2006,10,1,14,00,00);
		D7 = cal.getTime();
		cal.set(2006,10,1,15,00,00);
		D8 = cal.getTime();
		cal.set(2006,10,1,16,00,00);
		D9 = cal.getTime();
		cal.set(2006,10,1,17,00,00);
		D10 = cal.getTime();
		

		workIntervals =  new LinkedList<ShiftCalculator.WorkInterval>();
		scSingleInterval = new ShiftCalculator(bigGapMinutes,paddingMinutes);
		scSingleInterval.addEvent(new Interval(D1,D2));
		
		scTwoIntervals = new ShiftCalculator(bigGapMinutes,paddingMinutes);
		scTwoIntervals.addEvent(new Interval(D1,D2));
		scTwoIntervals.addEvent(new Interval(D3,D4));
		
		workIntervals =  new LinkedList<ShiftCalculator.WorkInterval>();
		closeScTwoInterval = new ShiftCalculator(bigGapMinutes,paddingMinutes);
		closeScTwoInterval.addEvent(new Interval(D5,D6));
		closeScTwoInterval.addEvent(new Interval(D3,D4));
		
		workIntervals =  new LinkedList<ShiftCalculator.WorkInterval>();
		threeIntervals = new ShiftCalculator(bigGapMinutes,paddingMinutes);
		threeIntervals.addEvent(new Interval(D1,D2));
		threeIntervals.addEvent(new Interval(D5,D6));
		threeIntervals.addEvent(new Interval(D3,D4));
		
		workIntervals =  new LinkedList<ShiftCalculator.WorkInterval>();
		fiveIntervals = new ShiftCalculator(bigGapMinutes,paddingMinutes);
		fiveIntervals.addEvent(new Interval(D1,D2));
		fiveIntervals.addEvent(new Interval(D5,D6));
		fiveIntervals.addEvent(new Interval(D3,D4));
		fiveIntervals.addEvent(new Interval(D7,D8));
		fiveIntervals.addEvent(new Interval(D9,D10));
		
	}

	@After
	public void tearDown() throws Exception {
	}
	
	
	/**
	 * Add a single point to the ShiftCalculator and check that the
	 * work intervals consist of a single point 
	 */
	@Test
	public void testGetWorkIntervalsSinglePoint() {
		// Create the "correct" output for checking
		workIntervals.add(new ShiftCalculator.WorkInterval(D1,D1));
		
		// Feed the data into ShiftCalculator
		ShiftCalculator sc = new ShiftCalculator(bigGapMinutes,paddingMinutes);
		sc.addEvent(new Point(D1));
		assertEquals(workIntervals,sc.getWorkIntervals());
	}
	

	/**
	 * Add two points
	 */
	@Test 
	public void testGetWorkIntervalsTwoPoints() {
		// Create the "correct" output for checking
		workIntervals.add(new ShiftCalculator.WorkInterval(D1,D2));
		
		// Feed the data into ShiftCalculator		
		ShiftCalculator sc = new ShiftCalculator(bigGapMinutes,paddingMinutes);
		sc.addEvent(new Point(D1));
		sc.addEvent(new Point(D2));
		assertEquals(workIntervals,sc.getWorkIntervals());
		
		
		
	}

	
	/**
	 * Add a single interval to the ShiftCalculator and check that the
	 * work intervals consist of the same interval 
	 */
	@Test
	public void testGetWorkIntervalsSingleInterval() {
		// Create the "correct" output for checking
		workIntervals.add(new ShiftCalculator.WorkInterval(D1,D2));
		
		
		// This is just scSingleInterval		
		assertEquals(workIntervals,scSingleInterval.getWorkIntervals());
	}
	
	/**
	 * Add two intervals to ShiftCalculator
	 */
	@Test
	public void testGetWorkIntervalsTwoIntervals() {
		// Create the "correct" output for checking
		workIntervals.add(new ShiftCalculator.WorkInterval(D1,D2));
		workIntervals.add(new ShiftCalculator.WorkInterval(D3,D4));

		assertEquals(workIntervals,scTwoIntervals.getWorkIntervals());
	
	}	
	
	/**
	 * Add four points and check that we get two work intervals
	 */
	@Test
	public void testGetWorkIntervalsFourPoints() {
		// Create the "correct" output for checking
		workIntervals.add(new ShiftCalculator.WorkInterval(D1,D2));
		workIntervals.add(new ShiftCalculator.WorkInterval(D3,D4));
		
		
		ShiftCalculator sc = new ShiftCalculator(bigGapMinutes,paddingMinutes);
		sc.addEvent(new Point(D1));
		sc.addEvent(new Point(D2));
		sc.addEvent(new Point(D3));
		sc.addEvent(new Point(D4));
		
		assertEquals(workIntervals,sc.getWorkIntervals());
		
	}
	
	
	/**
	 * Add a point before the first interval, less than bigGap.
	 * It should just expand the existing interval
	 */
	@Test
	public void testBeforeFirstIntervalClose() {
		cal.setTime(D1);
		cal.add(Calendar.MINUTE, -(bigGapMinutes-5));
		Date D0 = cal.getTime();
		
		// Create the "correct" output for checking
		workIntervals.add(new ShiftCalculator.WorkInterval(D0,D2));
		
		scSingleInterval.addEvent(new Point(D0));
		
		assertEquals(workIntervals,scSingleInterval.getWorkIntervals());
		
	}
	
	
	
	/**
	 * Add a point before the first interval, less than bigGap.
	 * It should just expand the existing interval
	 */
	@Test
	public void testBeforeFirstIntervalFar() {
		cal.setTime(D1);
		cal.add(Calendar.MINUTE, -(bigGapMinutes+5));
		Date D0 = cal.getTime();
		
		// Create the "correct" output for checking
		workIntervals.add(new ShiftCalculator.WorkInterval(D0,D0));
		workIntervals.add(new ShiftCalculator.WorkInterval(D1,D2));
		
		scSingleInterval.addEvent(new Point(D0));
		
		assertEquals(workIntervals,scSingleInterval.getWorkIntervals());
		
	}
	
	/**
	 * Add a point after the last interval, less than bigGap.
	 * It should just expand the existing interval
	 */
	@Test
	public void testAfterLastIntervalClose() {
		cal.setTime(D2);
		cal.add(Calendar.MINUTE, (bigGapMinutes-5));
		Date D999 = cal.getTime();
		
		// Create the "correct" output for checking
		workIntervals.add(new ShiftCalculator.WorkInterval(D1,D999));

		
		scSingleInterval.addEvent(new Point(D999));

		assertEquals(workIntervals,scSingleInterval.getWorkIntervals());
		
	}
	

	/**
	 * Add a point after the last interval, more than bigGap.
	 * It should create a new interval
	 */
	@Test
	public void testAfterLastIntervalFar() {
		cal.setTime(D2);
		cal.add(Calendar.MINUTE, (bigGapMinutes+5));
		Date D999 = cal.getTime();
		
		// Create the "correct" output for checking
		workIntervals.add(new ShiftCalculator.WorkInterval(D1,D2));
		workIntervals.add(new ShiftCalculator.WorkInterval(D999,D999));

		
		scSingleInterval.addEvent(new Point(D999));

		assertEquals(workIntervals,scSingleInterval.getWorkIntervals());
		
	}
	
	/**
	 * Add a Interval between the two intervals, far from both of them
	 */
	@Test
	public void testBetweenIntervalsFarBoth() {
		cal.set(2006,9,30,18,45,00);
		Date Dp = cal.getTime();
		cal.set(2006,9,30,19,45,00);
		Date Dn = cal.getTime();
		
		// Create the "correct" output for checking
		workIntervals.add(new ShiftCalculator.WorkInterval(D1,D2));
		workIntervals.add(new ShiftCalculator.WorkInterval(Dp,Dn));
		workIntervals.add(new ShiftCalculator.WorkInterval(D3,D4));
		
		scTwoIntervals.addEvent(new Interval(Dp,Dn));
		assertEquals(workIntervals,scTwoIntervals.getWorkIntervals());
	}
	
	/**
	 * Add a point between the two intervals, far from both of them
	 */
	@Test
	public void testPointBetweenIntervalsFarBoth() {
		cal.set(2006,9,30,18,45,00);
		Date Dp = cal.getTime();

		
		// Create the "correct" output for checking
		workIntervals.add(new ShiftCalculator.WorkInterval(D1,D2));
		workIntervals.add(new ShiftCalculator.WorkInterval(Dp,Dp));
		workIntervals.add(new ShiftCalculator.WorkInterval(D3,D4));
		
		scTwoIntervals.addEvent(new Point(Dp));
		assertEquals(workIntervals,scTwoIntervals.getWorkIntervals());
	}

	/**
	 * Add a interval between the two intervals, near the prvious one
	 */
	@Test
	public void testBetweenIntervalsCloseToPre() {
		cal.set(2006,9,30,6,30,00);
		Date Dp = cal.getTime();
		cal.set(2006,9,30,6,59,00);
		Date Dn = cal.getTime();
		
		// Create the "correct" output for checking
		workIntervals.add(new ShiftCalculator.WorkInterval(D1,Dn));
		workIntervals.add(new ShiftCalculator.WorkInterval(D3,D4));
		
		scTwoIntervals.addEvent(new Interval(Dp,Dn));
		assertEquals(workIntervals,scTwoIntervals.getWorkIntervals());
	}
	
	/**
	 * Add a interval between the two intervals, near the next one
	 */
	@Test
	public void testBetweenIntervalsCloseToNext() {
		cal.set(2006,10,1,8,01,00);
		Date Dp = cal.getTime();
		cal.set(2006,10,1,8,10,00);
		Date Dn = cal.getTime();
		// Create the "correct" output for checking
		workIntervals.add(new ShiftCalculator.WorkInterval(D1,D2));
		workIntervals.add(new ShiftCalculator.WorkInterval(Dp,D4));
		
		scTwoIntervals.addEvent(new Interval(Dp,Dn));
		assertEquals(workIntervals,scTwoIntervals.getWorkIntervals());
	}
	
	/**
	 * Add a interval between the two intervals, closr to both
	 */
	@Test
	public void testBetweenIntervalsCloseToPreAndNext() {
		
	
		cal.set(2006,10,1,8,01,00);
		Date Dp = cal.getTime();
		cal.set(2006,10,1,8,10,00);
		Date Dn = cal.getTime();
		// Create the "correct" output for checking
		workIntervals.add(new ShiftCalculator.WorkInterval(D5,D4));
		
		
		closeScTwoInterval.addEvent(new Interval(Dp,Dn));
		assertEquals(workIntervals,closeScTwoInterval.getWorkIntervals());
	}
	
	/**
	 * Add a interval between the two intervals, the interval begin from the edge of previous one
	 */
	@Test
	public void testBetweenIntervalsOntheEdgeofPrev() {
		
	
		cal.set(2006,9,30,6,00,00);
		Date Dp = cal.getTime();
		cal.set(2006,9,30,7,50,00);
		Date Dn = cal.getTime();
		// Create the "correct" output for checking
		workIntervals.add(new ShiftCalculator.WorkInterval(D1,Dn));
		workIntervals.add(new ShiftCalculator.WorkInterval(D3,D4));
		
		
		scTwoIntervals.addEvent(new Interval(Dp,Dn));
		assertEquals(workIntervals,scTwoIntervals.getWorkIntervals());
	}
	
	/**
	 * Add a interval between the two intervals, the interval end at the edge of next one
	 */
	@Test
	public void testBetweenIntervalsOntheEdgeofNext() {
		
	
		cal.set(2006,10,1,8,00,00);
		Date Dp = cal.getTime();
		cal.set(2006,10,1,8,30,00);
		Date Dn = cal.getTime();
		// Create the "correct" output for checking
		workIntervals.add(new ShiftCalculator.WorkInterval(D1,D2));
		workIntervals.add(new ShiftCalculator.WorkInterval(Dp,D4));
		
		
		scTwoIntervals.addEvent(new Interval(Dp,Dn));
		assertEquals(workIntervals,scTwoIntervals.getWorkIntervals());
	}
	
	/**
	 * Add a interval within a work interval
	 */
	@Test
	public void testIntervalWithinWorkinterval() {
		
	
		cal.set(2006,10,1,7,02,00);//7:02
		Date Dp = cal.getTime();
		cal.set(2006,10,1,7,22,00);
		Date Dn = cal.getTime();
		// Create the "correct" output for checking
		workIntervals.add(new ShiftCalculator.WorkInterval(D1,D2));
		workIntervals.add(new ShiftCalculator.WorkInterval(D5,D6));
		workIntervals.add(new ShiftCalculator.WorkInterval(D3,D4));
		
		
		threeIntervals.addEvent(new Interval(Dp,Dn));
		assertEquals(workIntervals,threeIntervals.getWorkIntervals());
	}
	
	/**
	 * Add a interval
	 * start from gap and end in gap
	 * start and end points both are far from other intervals.
	 * cover two intervals
	 */
	@Test
	public void testIntervalStartGapEndGapBothFar() {
	
		cal.set(2006,10,1,6,00,00);
		Date Dp = cal.getTime();
		cal.set(2006,10,1,10,00,00);
		Date Dn = cal.getTime();
		// Create the "correct" output for checking
		workIntervals.add(new ShiftCalculator.WorkInterval(D1,D2));
		workIntervals.add(new ShiftCalculator.WorkInterval(Dp,Dn));
		workIntervals.add(new ShiftCalculator.WorkInterval(D7,D8));
		workIntervals.add(new ShiftCalculator.WorkInterval(D9,D10));
		
		
		fiveIntervals.addEvent(new Interval(Dp,Dn));
		assertEquals(workIntervals,fiveIntervals.getWorkIntervals());
	}

	/**
	 * Add a interval
	 * start from gap and end in gap
	 * start point is close to previouse interval
	 * end point is far from next interval
	 * cover two intervals
	 */
	@Test
	public void testIntervalStartGapEndGapCloseFar() {
	
		cal.set(2006,9,30,6,30,00);
		Date Dp = cal.getTime();
		cal.set(2006,10,1,10,00,00);
		Date Dn = cal.getTime();
		// Create the "correct" output for checking
		workIntervals.add(new ShiftCalculator.WorkInterval(D1,Dn));
		workIntervals.add(new ShiftCalculator.WorkInterval(D7,D8));
		workIntervals.add(new ShiftCalculator.WorkInterval(D9,D10));
		
		
		fiveIntervals.addEvent(new Interval(Dp,Dn));
		assertEquals(workIntervals,fiveIntervals.getWorkIntervals());
	}
	
	
	
	

	
	/**
	 * Add a interval
	 * start from gap and end in gap
	 * start point is far from previouse interval
	 * end point is close to next interval
	 * cover two intervals
	 */
	@Test
	public void testIntervalStartGapEndGapFarClose() {
	
		cal.set(2006,10,1,7,00,00);
		Date Dp = cal.getTime();
		cal.set(2006,10,1,13,30,00);
		Date Dn = cal.getTime();
		// Create the "correct" output for checking
		workIntervals.add(new ShiftCalculator.WorkInterval(D1,D2));
		workIntervals.add(new ShiftCalculator.WorkInterval(Dp,D8));
		workIntervals.add(new ShiftCalculator.WorkInterval(D9,D10));
		
		
		fiveIntervals.addEvent(new Interval(Dp,Dn));
		assertEquals(workIntervals,fiveIntervals.getWorkIntervals());
	}
	
	/**
	 * Add a interval
	 * both end points are close to other intervals <50 minutes
	 * cover two intervals
	 */
	@Test
	public void testIntervalStartGapEndGapBothClose() {
	
		cal.set(2006,9,30,6,30,00);
		Date Dp = cal.getTime();
		cal.set(2006,10,1,13,30,00);
		Date Dn = cal.getTime();
		// Create the "correct" output for checking
		workIntervals.add(new ShiftCalculator.WorkInterval(D1,D8));
		workIntervals.add(new ShiftCalculator.WorkInterval(D9,D10));
		
		
		fiveIntervals.addEvent(new Interval(Dp,Dn));
		assertEquals(workIntervals,fiveIntervals.getWorkIntervals());
	}
	
	
	/**
	 * Add a interval
	 * both end points have standard distance to other intervals 50 minutes
	 * cover two intervals
	 */
	@Test
	public void testIntervalStartGapEndGapBothStandard() {
	
		cal.set(2006,9,30,6,50,00);
		Date Dp = cal.getTime();
		cal.set(2006,10,1,13,10,00);
		Date Dn = cal.getTime();
		// Create the "correct" output for checking
		workIntervals.add(new ShiftCalculator.WorkInterval(D1,D8));
		workIntervals.add(new ShiftCalculator.WorkInterval(D9,D10));
		
		
		fiveIntervals.addEvent(new Interval(Dp,Dn));
		assertEquals(workIntervals,fiveIntervals.getWorkIntervals());
	}

	
	
	/**
	 * Add a interval
	 * start from gap and end in interval
	 *  start are far from previous intervals.
	 *  cover one interval
	 */
	@Test
	public void testIntervalStartGapEndIntervalFar() {
	
		cal.set(2006,10,1,6,00,00);
		Date Dp = cal.getTime();
		cal.set(2006,10,1,8,45,00);
		Date Dn = cal.getTime();
		// Create the "correct" output for checking
		workIntervals.add(new ShiftCalculator.WorkInterval(D1,D2));
		workIntervals.add(new ShiftCalculator.WorkInterval(Dp,D4));
		workIntervals.add(new ShiftCalculator.WorkInterval(D7,D8));
		workIntervals.add(new ShiftCalculator.WorkInterval(D9,D10));
		
		
		fiveIntervals.addEvent(new Interval(Dp,Dn));
		assertEquals(workIntervals,fiveIntervals.getWorkIntervals());
	}
	
	
	/**
	 * Add a interval
	 * start from gap and end in interval
	 *  start point is close to previous intervals
	 *  cover one interval
	 */
	@Test
	public void testIntervalStartGapEndIntervalClose() {
	
		cal.set(2006,9,30,6,30,00);
		Date Dp = cal.getTime();
		cal.set(2006,10,1,8,45,00);
		Date Dn = cal.getTime();
		// Create the "correct" output for checking
		workIntervals.add(new ShiftCalculator.WorkInterval(D1,D4));
		workIntervals.add(new ShiftCalculator.WorkInterval(D7,D8));
		workIntervals.add(new ShiftCalculator.WorkInterval(D9,D10));
		
		
		fiveIntervals.addEvent(new Interval(Dp,Dn));
		assertEquals(workIntervals,fiveIntervals.getWorkIntervals());
	}
	
	/**
	 * Add a interval
	 * start from gap and end in interval
	 *  the distance from start point to previous intervals is the standard 50 minutes
	 *  cover one interval
	 *  this situation should be same as testIntervalStartGapEndIntervalClose
	 */
	@Test
	public void testIntervalStartGapEndIntervalStandard() {
	
		cal.set(2006,9,30,6,50,00);
		Date Dp = cal.getTime();
		cal.set(2006,10,1,8,45,00);
		Date Dn = cal.getTime();
		// Create the "correct" output for checking
		workIntervals.add(new ShiftCalculator.WorkInterval(D1,D4));
		workIntervals.add(new ShiftCalculator.WorkInterval(D7,D8));
		workIntervals.add(new ShiftCalculator.WorkInterval(D9,D10));
		
		
		fiveIntervals.addEvent(new Interval(Dp,Dn));
		assertEquals(workIntervals,fiveIntervals.getWorkIntervals());
	}
	
	/**
	 * Add a interval
	 * start from interval and end in gap
	 * end  point is far from next intervals
	 * cover 1 interval
	 */
	@Test
	public void testIntervalStartIntervalEndGapFar() {
	
		cal.set(2006,10,1,7,15,00);
		Date Dp = cal.getTime();
		cal.set(2006,10,1,10,00,00);
		Date Dn = cal.getTime();
		// Create the "correct" output for checking
		workIntervals.add(new ShiftCalculator.WorkInterval(D1,D2));
		workIntervals.add(new ShiftCalculator.WorkInterval(D5,Dn));
		workIntervals.add(new ShiftCalculator.WorkInterval(D7,D8));
		workIntervals.add(new ShiftCalculator.WorkInterval(D9,D10));
		
		
		fiveIntervals.addEvent(new Interval(Dp,Dn));
		assertEquals(workIntervals,fiveIntervals.getWorkIntervals());
	}
	
	
	/**
	 * Add a interval
	 * start from interval and end in gap
	 * end  point is close to next intervals
	 * cover 1 interval
	 */
	@Test
	public void testIntervalStartIntervalEndGapclose() {
	
		cal.set(2006,10,1,7,15,00);
		Date Dp = cal.getTime();
		cal.set(2006,10,1,13,30,00);
		Date Dn = cal.getTime();
		// Create the "correct" output for checking
		workIntervals.add(new ShiftCalculator.WorkInterval(D1,D2));
		workIntervals.add(new ShiftCalculator.WorkInterval(D5,D8));
		workIntervals.add(new ShiftCalculator.WorkInterval(D9,D10));
		
		
		fiveIntervals.addEvent(new Interval(Dp,Dn));
		assertEquals(workIntervals,fiveIntervals.getWorkIntervals());
	}
	
	
	/**
	 * Add a interval
	 * start from interval and end in gap
	 * the distance of end  point to next intervals is the stanrdard gap.
	 * cover 1 interval
	 * This situation should be the same as testIntervalStartIntervalEndGapclose
	 */
	@Test
	public void testIntervalStartIntervalEndGapStandard() {
	
		cal.set(2006,10,1,7,15,00);
		Date Dp = cal.getTime();
		cal.set(2006,10,1,13,10,00);
		Date Dn = cal.getTime();
		// Create the "correct" output for checking
		workIntervals.add(new ShiftCalculator.WorkInterval(D1,D2));
		workIntervals.add(new ShiftCalculator.WorkInterval(D5,D8));
		workIntervals.add(new ShiftCalculator.WorkInterval(D9,D10));
		
		
		fiveIntervals.addEvent(new Interval(Dp,Dn));
		assertEquals(workIntervals,fiveIntervals.getWorkIntervals());
	}
	
	/**
	 * Add a interval
	 * start from interval and end in interval
	 * cover 1 interval
	 * 
	 */
	@Test
	public void testIntervalStartIntervalEndInterval() {
	
		cal.set(2006,10,1,7,15,00);
		Date Dp = cal.getTime();
		cal.set(2006,10,1,14,30,00);
		Date Dn = cal.getTime();
		// Create the "correct" output for checking
		workIntervals.add(new ShiftCalculator.WorkInterval(D1,D2));
		workIntervals.add(new ShiftCalculator.WorkInterval(D5,D8));
		workIntervals.add(new ShiftCalculator.WorkInterval(D9,D10));
		
		
		fiveIntervals.addEvent(new Interval(Dp,Dn));
		assertEquals(workIntervals,fiveIntervals.getWorkIntervals());
	}
	
	/**
	 * Add a interval
	 * start from front edge of interval and end in another interval end edge
	 * cover 3 interval
	 * 
	 */
	@Test
	public void testIntervalStartIntervalEndIntervalFrontEdgeEndEdge() {
	
		cal.set(2006,10,1,7,01,00);
		Date Dp = cal.getTime();
		cal.set(2006,10,1,15,00,00);
		Date Dn = cal.getTime();
		// Create the "correct" output for checking
		workIntervals.add(new ShiftCalculator.WorkInterval(D1,D2));
		workIntervals.add(new ShiftCalculator.WorkInterval(D5,D8));
		workIntervals.add(new ShiftCalculator.WorkInterval(D9,D10));
		
		
		fiveIntervals.addEvent(new Interval(Dp,Dn));
		assertEquals(workIntervals,fiveIntervals.getWorkIntervals());
	}
	
	/**
	 * Add a interval
	 * start from front edge of interval and end in another interval front edge
	 * cover 3 interval
	 * 
	 */
	@Test
	public void testIntervalStartIntervalEndIntervalFrontEdgeFrontEdge() {
	
		cal.set(2006,10,1,7,01,00);
		Date Dp = cal.getTime();
		cal.set(2006,10,1,16,00,00);
		Date Dn = cal.getTime();
		// Create the "correct" output for checking
		workIntervals.add(new ShiftCalculator.WorkInterval(D1,D2));
		workIntervals.add(new ShiftCalculator.WorkInterval(D5,D10));

		
		
		fiveIntervals.addEvent(new Interval(Dp,Dn));
		assertEquals(workIntervals,fiveIntervals.getWorkIntervals());
	}
	
	
	/**
	 * Add a intervalt already exist in the sequence
	 * start and end both are far from intervals.
	 */
	@Test
	public void testSameIntervalStartGapEndGapBothFar() {
	
		cal.set(2006,10,1,8,30,00);
		Date Dp = cal.getTime();
		cal.set(2006,10,1,9,00,00);
		Date Dn = cal.getTime();
		// Create the "correct" output for checking
		workIntervals.add(new ShiftCalculator.WorkInterval(D1,D2));
		workIntervals.add(new ShiftCalculator.WorkInterval(D5,D6));
		workIntervals.add(new ShiftCalculator.WorkInterval(D3,D4));
		workIntervals.add(new ShiftCalculator.WorkInterval(D7,D8));
		workIntervals.add(new ShiftCalculator.WorkInterval(D9,D10));
		
		
		fiveIntervals.addEvent(new Interval(Dp,Dn));
		assertEquals(workIntervals,fiveIntervals.getWorkIntervals());
	}
	
	
	
}
	