package edu.unl.cse.activitygraph.sources;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Date;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

import edu.unl.cse.activitygraph.Interval;
import edu.unl.cse.activitygraph.Point;
import edu.unl.cse.activitygraph.Series;
import edu.unl.cse.activitygraph.SeriesGroup;
import edu.unl.cse.activitygraph.util.DataTimeZone;

public class XMLDataSource extends GenericDataSource {
	
	private boolean isEmpty = true;

	public XMLDataSource(String xmlFileName) throws IOException,ParsingException,ValidityException, InvalidColorException {
		this(new FileReader(xmlFileName));
	}
	
	public XMLDataSource(File file) throws FileNotFoundException, IOException, ParsingException,ValidityException, InvalidColorException{
		this(new FileReader(file));
	}
	
	public XMLDataSource(Reader reader) throws IOException,ParsingException,ValidityException, InvalidColorException {
		Builder parser = new Builder();
		Document doc = parser.build(reader);
		this.seriesGroups = new ArrayList<SeriesGroup>();
		this.parseXml(doc);
	}

	private void parseXml(Document doc) throws ValidityException, InvalidColorException{
		
		Element root = doc.getRootElement();
		if(root.getLocalName() != "ActivityGraph") {
			throw new ValidityException("Root node is type: " + root.getLocalName() + ", expected type: ActivityGraph");									
		}
		
		// Check if there's a time zone. If so, set it
		String tzId = root.getAttributeValue("timezone");
		if(tzId!=null) {
			DataTimeZone.setByString(tzId);
		}
		
		Node child;
		Element elt;
		for(int i=0;i<root.getChildCount();++i) {
			child = root.getChild(i);
			if(child instanceof Element) {
				elt = (Element)child;
				if(elt.getLocalName()!="SeriesGroup") {
					throw new ValidityException("Expected node SeriesGroup, got " + elt.getLocalName() + "instead");
				}
				processSeriesGroupElement(elt);
				
			}
		}
		
	}

	private void processSeriesGroupElement(Element groupNode) throws ValidityException, InvalidColorException{
		
		SeriesGroup seriesGroup = new SeriesGroup(groupNode.getAttributeValue("name"));
		// Skip empty series groups
		if(groupNode.getChildCount()==0) { return;}
		
		this.seriesGroups.add(seriesGroup);

		Node child;
		Element elt;
		for(int i=0;i<groupNode.getChildCount();++i) {
			child = groupNode.getChild(i);
			if(child instanceof Element) {
				elt = (Element)child;
				if(elt.getLocalName()!="Series") {
					throw new ValidityException("Expected node Series, got " + elt.getLocalName() + "instead");
				}
				processSeriesElement(elt,seriesGroup);
			}
		}
	}

	private void processSeriesElement(Element seriesNode, SeriesGroup seriesGroup) throws ValidityException, InvalidColorException {
		Series series = seriesGroup.addSeries(seriesNode.getAttributeValue("name"));
		String colorName = seriesNode.getAttributeValue("color");
		if(colorName!=null) {
			Color color = stringToColor(colorName);
			series.setColor(color);
		}
		
		Node child;
		Element elt;
		for(int i=0;i<seriesNode.getChildCount();++i) {
			child = seriesNode.getChild(i);
			if(child instanceof Element) {
				elt = (Element)child;
				processEventElement(elt,series);
			}
		}
		
	}

	private void processEventElement(Element elt, Series series) throws ValidityException{
		this.setNotEmpty();
		String name = elt.getLocalName();
		if(name=="Interval") {
			processIntervalElement(elt, series);
		} else if(name=="Point") {
			processPointElement(elt,series);
		} else {
			throw new ValidityException("Expecting Interval or Point, got: " + name);
		}
	}

	private void processPointElement(Element elt, Series series) {
		String timeString = elt.getAttributeValue("time");
		Date timestamp = epochStringToDate(timeString);
		String text=elt.getAttributeValue("text");
		if (text==null){
			text="";
		}
		String noteAttribute=elt.getAttributeValue("note");
		if(noteAttribute==null){
			noteAttribute="";
		}		
		String note="\n"+text+"\n"+noteAttribute;
		Point point = new Point(timestamp,note);
		series.addEvent(point);
		
		this.updateFirstEvent(timestamp);
		this.updateLastEvent(timestamp);
		
	}

	private void processIntervalElement(Element elt, Series series) {
		String startString = elt.getAttributeValue("start");
		String endString = elt.getAttributeValue("end");
		String text=elt.getAttributeValue("text");
		if(text==null){
			text="";
		}
		String noteAttribute= elt.getAttributeValue("note");
		if(noteAttribute==null){
			noteAttribute="";
		}
		String noteString="\n"+text+"\n"+noteAttribute;
		Date startTimestamp = epochStringToDate(startString);
		Date endTimestamp = epochStringToDate(endString);
		Interval interval = new Interval(startTimestamp, endTimestamp,noteString);
		series.addEvent(interval);
		this.updateFirstEvent(startTimestamp);
		this.updateLastEvent(endTimestamp);
	}

	
	/**
	 * Converts a string representing Unix epoch time (seconds since 1/1/1970 UTC) into a Date object
	 * @param s  a string that has seconds (not miliseconds!) since the epoch
	 * @return a date object
	 */
	public static Date epochStringToDate(String s) {
		return new Date(Long.parseLong(s)*1000);
	}
	
		public boolean isEmpty() {
		return this.isEmpty;
	}
	
	private void setNotEmpty() {
		this.isEmpty = false;
	}
	
	/**
	 * Converts a string that describes a color to the corresponding color.
	 * 
	 * Valid colors: black,blue,cyan,darkGray,gray,green,lightGray, magenta,orange,
	 * pink,red,white,yellow
	 * 
	 * @param s the color as a string
	 * @return
	 */
	public static Color stringToColor(String s) throws InvalidColorException {
		String sCol = s.toLowerCase();
		if(sCol.equals("black"))
			return Color.black;
		else if(sCol.equals("blue"))
			return Color.blue;
		else if(sCol.equals("cyan"))
			return Color.cyan;
		else if(sCol.equals("darkgray"))
			return Color.darkGray;
		else if(sCol.equals("dark_gray"))
			return Color.darkGray;
		else if(sCol.equals("gray"))
			return Color.gray;
		else if(sCol.equals("green"))
			return Color.green;
		else if(sCol.equals("lightgray"))
			return Color.lightGray;
		else if(sCol.equals("light_gray"))
			return Color.lightGray;
		else if(sCol.equals("magenta"))
			return Color.magenta;
		else if(sCol.equals("orange"))
			return Color.orange;
		else if(sCol.equals("pink"))
			return Color.pink;
		else if(sCol.equals("red"))
			return Color.red;
		else if(sCol.equals("white"))
			return Color.white;
		else if(sCol.equals("yellow"))
			return Color.yellow;
		else throw new InvalidColorException(sCol);
		
	}
	
	@SuppressWarnings("serial")
	static public class InvalidColorException extends Exception {
		public String badColor;
		
		public InvalidColorException(String s) {
			this.badColor = s;
		}
	}


}
