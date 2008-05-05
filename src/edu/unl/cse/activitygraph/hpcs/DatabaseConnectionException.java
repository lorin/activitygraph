package edu.unl.cse.activitygraph.hpcs;

@SuppressWarnings("serial")
public class DatabaseConnectionException extends Exception {
	
	public String reason;

	public DatabaseConnectionException(String reason) {
		this.reason = reason;
	}

}
