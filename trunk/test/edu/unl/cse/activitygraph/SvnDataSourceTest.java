package edu.unl.cse.activitygraph;

import static org.junit.Assert.*;

import org.junit.Test;
import org.tmatesoft.svn.core.SVNException;

import edu.unl.cse.activitygraph.sources.SVNDataSource;


public class SvnDataSourceTest {
	@Test
	public void testIsNonEmpty() throws SVNException {
		SVNDataSource svnds = new SVNDataSource("http://activitygraph.googlecode.com/svn/");
		assertFalse(svnds.isEmpty());
	}
	
	
	// This test is currently commented out because we don't have a publicly accessible 
	// empty svn directory. You can create your own by doing "svnadmin creaate /tmp/tester"
	// and then uncomment the code below, if you like.
	/*
	@Test
	public void testIsEmpty() throws SVNException {
		SVNDataSource svnds = new SVNDataSource("file:///tmp/tester");
		assertTrue(svnds.isEmpty());
	}
	*/

}
