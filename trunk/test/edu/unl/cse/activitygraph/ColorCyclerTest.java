package edu.unl.cse.activitygraph;


import static org.junit.Assert.assertEquals;

import java.awt.Color;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.unl.cse.activitygraph.util.ColorCycler;

public class ColorCyclerTest {
	
	ColorCycler cycler;

	@Before
	public void setUp() throws Exception {		
		cycler = new ColorCycler();
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testGetFirstColor() {
		assertEquals(Color.YELLOW, cycler.getNextColor());
	}
	
	@Test
	public void testGetSecondColor() {
		cycler.getNextColor();
		assertEquals(Color.GREEN, cycler.getNextColor());
	}
	
	@Test
	public void testGetThirdColor() {
		cycler.getNextColor();
		cycler.getNextColor();
		assertEquals(Color.RED, cycler.getNextColor());		
	}
	
	@Test
	public void testFullCycle() {
		// There are nine colors
		cycler.getNextColor();
		cycler.getNextColor();
		cycler.getNextColor();
		cycler.getNextColor();
		cycler.getNextColor();
		cycler.getNextColor();
		cycler.getNextColor();
		cycler.getNextColor();
		assertEquals(Color.YELLOW, cycler.getNextColor());
		
	}

}
