package edu.unl.cse.activitygraph.sources;

import java.util.Date;
import java.util.List;

import edu.unl.cse.activitygraph.SeriesGroup;
import edu.unl.cse.activitygraph.interfaces.IDataSource;

/**
 * A class that implements IDataSource which has some convenience fields and methods
 *
 */
public abstract class GenericDataSource implements IDataSource {

	protected List<SeriesGroup> seriesGroups;
	protected Date firstEvent = null;
	protected Date lastEvent = null;

	
	public Date getFirstEventTime() {
		return (Date)this.firstEvent.clone();
	}

	public Date getLastEventTime() {
		return (Date)this.lastEvent.clone();
	}

	public List<SeriesGroup> getSeriesGroups() {
		return this.seriesGroups;
	}

	protected void updateFirstEvent(Date timestamp) {
		if(this.firstEvent==null || timestamp.before(this.firstEvent)) {
			this.firstEvent = (Date)timestamp.clone();
		}
		
	}

	protected void updateLastEvent(Date timestamp) {
		if(this.lastEvent==null || timestamp.after(this.lastEvent)) {
			this.lastEvent = (Date)timestamp.clone();
		}
		
	}

}
