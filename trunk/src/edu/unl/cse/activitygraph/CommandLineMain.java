package edu.unl.cse.activitygraph;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.tmatesoft.svn.core.SVNException;

import edu.unl.cse.activitygraph.sources.SVNDataSource;
import edu.unl.cse.activitygraph.sources.XMLDataSource;

/**
 * Main simply contains a main function that is intended to be
 * used for the deployed version of ActivityGraph
 *
 */
public class CommandLineMain {
	

	/**
	 * Main function
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length!=1) {
			printUsage();
			return;
		}
		String source = args[0];
		
		if(isUrl(source)) {
			String url = source;
			try {
				new ActivityGraph(new SVNDataSource(url));
			} catch(SVNException e) {
				System.err.println("Problem accessing SVN repository: " + url + "\n");
				e.printStackTrace(System.err);
			}
		} else 
		{
			String fname = source;
			try {
				new ActivityGraph(new XMLDataSource(fname));
			} catch(nu.xom.ParsingException e) {
				System.err.println("Problem parsing the XML file: " + fname + "\n");
				e.printStackTrace(System.err);
			} catch(XMLDataSource.InvalidColorException e) {
				System.out.println("Color specified in XML file is not valid: " + e.badColor);
			} catch(FileNotFoundException e) {
				System.err.println("File not found: " + fname + "\n");
			} catch(IOException e) {
				System.err.println("Problem reading the XML file: " + fname + "\n");
				e.printStackTrace(System.err);
			}
		}
	}
	
	private static boolean isUrl(String source) {
		try {
			new URL(source);
		} catch (MalformedURLException e) {
			return false;
		}
		return true;
	}
	private static void printUsage() {
		System.out.println("ActivityGraph");
		System.out.println("Usage: java -jar ActivityGraph.jar DATASOURCE");
		System.out.println("");
		System.out.println(" A data source may be any of the following:");
		System.out.println(" - path to an XML file (e.g. data/data.xml)");
		System.out.println(" - URL of a subversion repository that allows anonymous access");
		System.out.println("   e.g. ");
		
	}

}
