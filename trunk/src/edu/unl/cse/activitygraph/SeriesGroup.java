package edu.unl.cse.activitygraph;

import java.util.ArrayList;
import java.util.List;



/**
 * SeriesGroup is a collection of Series objects that are grouped together
 * because they are related in some way.
 *
 */
public class SeriesGroup {
	
	private String name;
	private List<Series> seriesList;

	/**
	 * Some operations are invalid if a SeriesGroup object is empty 
	 * (i.e., contains no series). this exception is thrown in cases
	 * where a method expects a non-empty SeriesGroup.
	 *
	 */
	public static class SeriesGroupEmptyException extends Exception {
		
		public SeriesGroup emptySeriesGroup;

		public SeriesGroupEmptyException(SeriesGroup seriesGroup) {
			this.emptySeriesGroup=seriesGroup;
		}

		private static final long serialVersionUID = 8371677275998105965L;

	}

	public SeriesGroup(String name) {
		this.name = name;
		this.seriesList = new ArrayList<Series>();
		
		
	}



	public String getName() {
		return name;
	}
	
	/**
	 * Check for an empty SeriesGroups
	 * 
	 * @return true if the object contains no series, false otherwise
	 */
	public boolean isEmpty() {
		return this.seriesList.isEmpty();
	}
	
	/**
	 * Add a new series to the group 
	 * 
	 * @param name Name of the series
	 * @return a reference to the newly created object
	 */
	public Series addSeries(String name) {
		Series series = new Series(name);
		seriesList.add(series);
		return series;
	}
	
	/**
	 * Retrieve a series by name, adding it if it doesn't exist
	 * @return the series
	 */
	public Series getAddSeries(String name) {
		for(Series series : this.seriesList) {
			if(series.getName().equals(name)) {
				return series;
			}
		}
		return this.addSeries(name);
	}
	

	public List<Series> getSeries() {
		return seriesList;
	}
	


	public float getLengthMin() {
		float total = 0;
		for(Series series : seriesList) {
			total += series.getLengthMin();
		}
		return total;
	}

	/**
	 * 
	 * @return the number of events in all series in the group
	 */
	public int getGroupEventCount() {
		int total = 0;
		for(Series series : seriesList) {
			total += series.getEventCount();
		}
		return total;
	}
	
	public float getYmean() {
		float total = 0;
		int i=0;
		for(Series series : seriesList) {
			total += series.getyLabelPos();
			i+=1;
		}
		return total/i;
	}

}
