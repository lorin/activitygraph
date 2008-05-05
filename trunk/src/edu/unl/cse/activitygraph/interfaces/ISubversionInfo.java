package edu.unl.cse.activitygraph.interfaces;

public interface ISubversionInfo {
	/**
	 * 
	 * @return URL to subversion repository
	 */
	String getUrl();
	
	/**
	 * @return login to subversion repository 
	 */
	String getLogin();
	
	/**
	 * 
	 * @return password to subversion repository
	 */
	String getPassword();

}
