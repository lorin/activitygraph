package edu.unl.cse.activitygraph;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class GetShiftMinutesTest {
	Interval intvl;
	Interval intvl2,intvl3;
	ShiftCalculator shiCalculator;
	Calendar cal;
	Date D1,D2,D3,D4,D5,D6;
	private int paddingMinutes;
	private int bigGapMinutes;
	private float delta=.01f;

	@Before
	public void setUp(){
		
		bigGapMinutes = 50;
		paddingMinutes = 15;
		
		cal = Calendar.getInstance();
		cal.set(2006,8,30,5,45,00);
		D1=cal.getTime();
		cal.set(2006,8,30,6,00,00);
		D2=cal.getTime();
		cal.set(2006,8,30,12,15,00);
		D3=cal.getTime();
		cal.set(2006,8,30,12,30,00);
		D4=cal.getTime();
		cal.set(2006,9,1,8,15,00);
		D5=cal.getTime();
		cal.set(2006,9,1,9,30,00);
		D6=cal.getTime();


		String note="getShiftMinutesTest";
		
		intvl = new Interval(D1,D2,note);
		intvl2 = new Interval(D3,D4,note);
		intvl3 = new Interval(D5,D6,note);
		shiCalculator = new ShiftCalculator(bigGapMinutes,paddingMinutes);
		shiCalculator.addEvent(intvl);
		shiCalculator.addEvent(intvl2);
		shiCalculator.addEvent(intvl3);
		
	}
	@After
	public void tearDown() throws Exception {
	}
	@Test
	public void testGetShiftMinutes()
	{	
		cal = Calendar.getInstance();
		cal.set(2006,8,30,12,20,00);
		Date Dt1 = cal.getTime();
		
		cal.set(2006,9,1,9,15,00);
		Date Dt2 = cal.getTime();
		
		//test case1 when data is within first work interval
		assertEquals(0.0f,shiCalculator.getShiftMinutes(D1),delta);
		assertEquals(0.0f,shiCalculator.getShiftMinutes(D2),delta);
		//test case 2 *date* is within an interval other than first
		assertEquals(360.0f,shiCalculator.getShiftMinutes(Dt1),delta);
		assertEquals(1530.0f,shiCalculator.getShiftMinutes(Dt2),delta);
		
	}
	
	@Test(expected=ShiftCalculator.ShiftCalculatorEmptyException.class)
	public void testGetShiftMinutesEmpty() {
		ShiftCalculator sc = new ShiftCalculator(bigGapMinutes,paddingMinutes);
		@SuppressWarnings("unused")
		float shiftMinutes = sc.getShiftMinutes(cal.getTime());
	}
	

}

