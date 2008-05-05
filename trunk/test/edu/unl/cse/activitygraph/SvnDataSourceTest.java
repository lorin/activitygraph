package edu.unl.cse.activitygraph;

import static org.junit.Assert.*;

import org.junit.Test;
import org.tmatesoft.svn.core.SVNException;

import edu.unl.cse.activitygraph.sources.SVNDataSource;


public class SvnDataSourceTest {
	@Test
	public void testIsNonEmpty() throws SVNException {
		SVNDataSource svnds = new SVNDataSource("http://cse.unl.edu:8080/svn/ActivityGraph");
		assertFalse(svnds.isEmpty());
	}
	
	@Test
	public void testIsEmpty() throws SVNException {
		SVNDataSource svnds = new SVNDataSource("http://cse.unl.edu:8080/svn/empty");
		assertTrue(svnds.isEmpty());
	}

}
