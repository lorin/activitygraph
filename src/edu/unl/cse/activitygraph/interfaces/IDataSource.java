package edu.unl.cse.activitygraph.interfaces;

import java.util.Date;
import java.util.List;

import edu.unl.cse.activitygraph.SeriesGroup;

/**
 * 
 * An IDataSource is an interface for data sources that contains timed data 
 * to be plotted by ActivityGraph.
 *
 */
public interface IDataSource {
	
	List<SeriesGroup> getSeriesGroups();
	
	/**
	 * 
	 * @return The time of the first event
	 */
	Date getFirstEventTime();

	/**
	 * 
	 * @return The time of the last event
	 */
	Date getLastEventTime();

	boolean isEmpty();

}
