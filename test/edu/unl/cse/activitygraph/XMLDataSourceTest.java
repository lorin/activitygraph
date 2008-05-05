package edu.unl.cse.activitygraph;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import nu.xom.ParsingException;
import nu.xom.ValidityException;

import org.junit.Before;
import org.junit.Test;

import edu.unl.cse.activitygraph.interfaces.ITimedEvent;
import edu.unl.cse.activitygraph.sources.XMLDataSource;


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
	 *
	 */
	@Test(expected=ValidityException.class)
	public void testValidityException() throws IOException,ParsingException,ValidityException{
		String xml = "<SeriesGroup name='foo' />";
		xmlds = new XMLDataSource(new StringReader(xml));
		
	}
	
	@Test
	public void testSingleSeriesGroup() throws IOException,ParsingException,ValidityException{
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
	 */
	@Test
	public void testEmptySeriesGroup() throws IOException,ParsingException,ValidityException{
		String xml = "<ActivityGraph>" +
				"<SeriesGroup name='foo' />" +
				"</ActivityGraph>";
		xmlds = new XMLDataSource(new StringReader(xml));
		groups = xmlds.getSeriesGroups();
		assertEquals(0,groups.size());
	}

	
	@Test
	public void testSingleSeries() throws IOException,ParsingException,ValidityException{
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
	public void testSingleIntervalSeries() throws IOException,ParsingException,ValidityException{
		/*
		  start: 2007-09-01 15:53:00 CDT
		  end:   2007-09-01 18:40:00 CDT
		 */
		String xml = "<ActivityGraph>" +
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

		assertEquals("2007-09-01 15:53", formatter.format(event.getStartTime()));
		assertEquals("2007-09-01 18:40", formatter.format(event.getEndTime()));

	}
	
	@Test
	public void testSinglePointSeries() throws IOException,ParsingException,ValidityException{
		String xml = "<ActivityGraph>" +
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
		assertEquals("2007-09-01 15:53", formatter.format(event.getStartTime()));
		assertEquals("2007-09-01 15:53", formatter.format(event.getEndTime()));
		
	}
	
	@Test
	public void testEpochStringToDate() {
		// 2007-09-01 18:40:00 CDT
		String s = "1188690000";
		Date date = XMLDataSource.epochStringToDate(s);

		assertEquals("2007-09-01 18:40",formatter.format(date));
		
	}
	
	@Test
	public void testGetFirstEventTime() throws IOException,ParsingException,ValidityException {
		/*
		  start: 2007-09-01 15:53:00 CDT
		  end:   2007-09-01 18:40:00 CDT
		 */
		String xml = "<ActivityGraph>" +
		"<SeriesGroup name='main'>" +
		"<Series name='thinking'>" + 
		"<Interval start='1188679980' end='1188690000' />" +
		"</Series>" + 
		"</SeriesGroup>" +
		"</ActivityGraph>";
		xmlds = new XMLDataSource(new StringReader(xml));
		assertEquals("2007-09-01 15:53", formatter.format(xmlds.getFirstEventTime()));
	}
	
	@Test
	public void testGetLastEventTime() throws IOException,ParsingException,ValidityException {
		/*
		  start: 2007-09-01 15:53:00 CDT
		  end:   2007-09-01 18:40:00 CDT
		 */
		String xml = "<ActivityGraph>" +
		"<SeriesGroup name='main'>" +
		"<Series name='thinking'>" + 
		"<Interval start='1188679980' end='1188690000' />" +
		"</Series>" + 
		"</SeriesGroup>" +
		"</ActivityGraph>";
		xmlds = new XMLDataSource(new StringReader(xml));
		assertEquals("2007-09-01 18:40", formatter.format(xmlds.getLastEventTime()));
	}
	
	@Test
	public void testIsEmpty() throws IOException,ParsingException,ValidityException
	{
		String xml="<ActivityGraph />";
		xmlds = new XMLDataSource(new StringReader(xml));
		assertTrue(xmlds.isEmpty());
	}
	
	@Test
	public void testIsNonEmpty() throws IOException,ParsingException,ValidityException
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
