package edu.unl.cse.activitygraph.interfaces;


public interface IStatusUpdater {
	/**
	 * Update status information for a long running job
	 * @param status a string describing the status
	 * @param shortProgress progress from 0 to 100 for an intermediate task 
	 * @param longProgress progress from 0 to 100 on the entire task
	 */
	public void updateStatus(String status, int shortProgress, int longProgress);
	
	public void updateText(String text);
	
	public void updateCurrent(int current);
	
	public void updateTotal(int total);


}
