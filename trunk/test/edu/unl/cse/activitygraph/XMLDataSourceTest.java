package edu.unl.cse.activitygraph;

import static org.junit.Assert.*;

import java.awt.Color;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import nu.xom.ParsingException;
import nu.xom.ValidityException;

import org.junit.Before;
import org.junit.Test;

import edu.unl.cse.activitygraph.interfaces.ITimedEvent;
import edu.unl.cse.activitygraph.sources.XMLDataSource;
import edu.unl.cse.activitygraph.sources.XMLDataSource.InvalidColorException;


public class XMLDataSourceTest {
	
	private XMLDataSource xmlds;
	List<SeriesGroup> groups;
	
	SimpleDateFormat formatter;
	
	@Before
	public void setUp() throws Exception {
		String pattern= "yyyy-MM-dd HH:mm";
		formatter = new SimpleDateFormat(pattern); 

	}

	/**
	 * Should throw an exception because root node is not ActivityGraph
	 * @throws InvalidColorException 
	 *
	 */
	@Test(expected=ValidityException.class)
	public void testValidityException() throws IOException,ParsingException,ValidityException, InvalidColorException{
		String xml = "<SeriesGroup name='foo' />";
		xmlds = new XMLDataSource(new StringReader(xml));
		
	}
	
	@Test(expected=InvalidColorException.class)
	public void testInvalidColorException() throws InvalidColorException {
		XMLDataSource.stringToColor("invalid color");
	}
	
	@Test
	public void testColors() throws InvalidColorException {
		assertEquals(Color.black,XMLDataSource.stringToColor("BLACK"));
		assertEquals(Color.black,XMLDataSource.stringToColor("Black"));
		assertEquals(Color.black,XMLDataSource.stringToColor("black"));
		assertEquals(Color.blue,XMLDataSource.stringToColor("blue"));
		assertEquals(Color.cyan,XMLDataSource.stringToColor("cyan"));
		assertEquals(Color.darkGray,XMLDataSource.stringToColor("darkGray"));
		assertEquals(Color.darkGray,XMLDataSource.stringToColor("dark_Gray"));
		assertEquals(Color.gray,XMLDataSource.stringToColor("gray"));
		assertEquals(Color.green,XMLDataSource.stringToColor("green"));
		assertEquals(Color.lightGray,XMLDataSource.stringToColor("lightgray"));
		assertEquals(Color.lightGray,XMLDataSource.stringToColor("light_gray"));
		assertEquals(Color.magenta,XMLDataSource.stringToColor("magenta"));
		assertEquals(Color.orange,XMLDataSource.stringToColor("orange"));
		assertEquals(Color.pink,XMLDataSource.stringToColor("pink"));
		assertEquals(Color.red,XMLDataSource.stringToColor("red"));
		assertEquals(Color.white,XMLDataSource.stringToColor("white"));
		assertEquals(Color.yellow,XMLDataSource.stringToColor("yellow"));
	}
		
	@Test
	public void testSingleSeriesGroup() throws IOException,ParsingException,ValidityException, InvalidColorException{
		String xml = "<ActivityGraph>" +
				"<SeriesGroup name='foo'>" +
				"<Series name='bar' />" +
				"</SeriesGroup>" + 
				"</ActivityGraph>";
		xmlds = new XMLDataSource(new StringReader(xml));
		groups = xmlds.getSeriesGroups();
		assertEquals(1,groups.size());
		
		SeriesGroup group = groups.get(0);
		assertEquals("foo",group.getName());
	}

	/**
	 * Ignore empty series groups
	 * @throws InvalidColorException 
	 */
	@Test
	public void testEmptySeriesGroup() throws IOException,ParsingException,ValidityException, InvalidColorException{
		String xml = "<ActivityGraph>" +
				"<SeriesGroup name='foo' />" +
				"</ActivityGraph>";
		xmlds = new XMLDataSource(new StringReader(xml));
		groups = xmlds.getSeriesGroups();
		assertEquals(0,groups.size());
	}

	
	@Test
	public void testSingleSeries() throws IOException,ParsingException,ValidityException, InvalidColorException{
		String xml = "<ActivityGraph>\n" + 
				"		<SeriesGroup name=\'foo\'>\n" + 
				"			<Series name=\'bar\' />\n" + 
				"		</SeriesGroup>\n" + 
				"</ActivityGraph>";
		xmlds = new XMLDataSource(new StringReader(xml));
		groups = xmlds.getSeriesGroups();
		assertEquals(1,groups.size());
		SeriesGroup group = groups.get(0);
		assertEquals("foo", group.getName());
		assertEquals(1,group.getSeries().size());
		Series series = group.getSeries().get(0);
		assertEquals("bar",series.getName());
	}
	
	
	
	@Test
	public void testSingleIntervalSeries() throws IOException,ParsingException,ValidityException, InvalidColorException{
		/*
		  start: 2007-09-01 15:53:00 CDT
		  end:   2007-09-01 18:40:00 CDT
		 */
		String tz = "America/Chicago";
		String xml = "<ActivityGraph timezone='" + tz + "'>" +
				"<SeriesGroup name='main'>" +
				"<Series name='thinking'>" + 
				"<Interval start='1188679980' end='1188690000' />" +
				"</Series>" + 
				"</SeriesGroup>" +
				"</ActivityGraph>";
		xmlds = new XMLDataSource(new StringReader(xml));
		Series series = xmlds.getSeriesGroups().get(0).getSeries().get(0);
		List<ITimedEvent> events = series.getEvents();
		assertEquals(1,events.size());
		ITimedEvent event = events.get(0);
		formatter.setTimeZone(TimeZone.getTimeZone(tz));
		assertEquals("2007-09-01 15:53", formatter.format(event.getStartTime()));
		assertEquals("2007-09-01 18:40", formatter.format(event.getEndTime()));

	}
	
	@Test
	public void testSinglePointSeries() throws IOException,ParsingException,ValidityException, InvalidColorException{
		String tz = "America/Chicago";
		String xml = "<ActivityGraph timezone='" + tz + "'>" +
		"<SeriesGroup name='events'>" +
		"<Series name='compile'>" + 
		"<Point time='1188679980' />" +
		"</Series>" + 
		"</SeriesGroup>" +
		"</ActivityGraph>";

		xmlds = new XMLDataSource(new StringReader(xml));
		Series series = xmlds.getSeriesGroups().get(0).getSeries().get(0);
		List<ITimedEvent> events = series.getEvents();
		assertEquals(1,events.size());
		ITimedEvent event = events.get(0);
		formatter.setTimeZone(TimeZone.getTimeZone(tz));
		assertEquals("2007-09-01 15:53", formatter.format(event.getStartTime()));
		assertEquals("2007-09-01 15:53", formatter.format(event.getEndTime()));
		
	}
	
	@Test
	public void testEpochStringToDate() {
		// 2007-09-01 18:40:00 CDT
		String s = "1188690000";
		String tz = "America/Chicago";
		Date date = XMLDataSource.epochStringToDate(s);
		formatter.setTimeZone(TimeZone.getTimeZone(tz));
		assertEquals("2007-09-01 18:40",formatter.format(date));
		
	}
	
	@Test
	public void testGetFirstEventTime() throws IOException,ParsingException,ValidityException, InvalidColorException {
		/*
		  start: 2007-09-01 15:53:00 CDT
		  end:   2007-09-01 18:40:00 CDT
		 */
		String tz = "America/Chicago";
		String xml = "<ActivityGraph timezone='" + tz + "'>" +
		"<SeriesGroup name='main'>" +
		"<Series name='thinking'>" + 
		"<Interval start='1188679980' end='1188690000' />" +
		"</Series>" + 
		"</SeriesGroup>" +
		"</ActivityGraph>";
		xmlds = new XMLDataSource(new StringReader(xml));
		formatter.setTimeZone(TimeZone.getTimeZone(tz));
		assertEquals("2007-09-01 15:53", formatter.format(xmlds.getFirstEventTime()));
	}
	
	@Test
	public void testGetLastEventTime() throws IOException,ParsingException,ValidityException, InvalidColorException {
		/*
		  start: 2007-09-01 15:53:00 CDT
		  end:   2007-09-01 18:40:00 CDT
		 */
		String tz = "America/Chicago";
		String xml = "<ActivityGraph timezone='" + tz + "'>" +
		"<SeriesGroup name='main'>" +
		"<Series name='thinking'>" + 
		"<Interval start='1188679980' end='1188690000' />" +
		"</Series>" + 
		"</SeriesGroup>" +
		"</ActivityGraph>";
		xmlds = new XMLDataSource(new StringReader(xml));
		formatter.setTimeZone(TimeZone.getTimeZone(tz));
		assertEquals("2007-09-01 18:40", formatter.format(xmlds.getLastEventTime()));
	}
	
	@Test
	public void testIsEmpty() throws IOException,ParsingException,ValidityException, InvalidColorException
	{
		String xml="<ActivityGraph />";
		xmlds = new XMLDataSource(new StringReader(xml));
		assertTrue(xmlds.isEmpty());
	}
	
	@Test
	public void testIsNonEmpty() throws IOException,ParsingException,ValidityException, InvalidColorException
	{
		String xml = "<ActivityGraph>" +
		"<SeriesGroup name='main'>" +
		"<Series name='thinking'>" + 
		"<Interval start='1188679980' end='1188690000' />" +
		"</Series>" + 
		"</SeriesGroup>" +
		"</ActivityGraph>";
		xmlds = new XMLDataSource(new StringReader(xml));
		assertFalse(xmlds.isEmpty());
	}

}
