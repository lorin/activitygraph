package edu.unl.cse.activitygraph.sources;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;
 

import edu.unl.cse.activitygraph.Point;
import edu.unl.cse.activitygraph.SeriesGroup;
import edu.unl.cse.activitygraph.interfaces.ISubversionInfo;

/**
 * Data source that gets its information from a Subversion repository
 */
public class SVNDataSource extends GenericDataSource {

	private boolean isEmpty = true;


	/**
	 * 
	 * Access a repository using URL alone (will login as anonymous/anonymous)
	 * 
	 * @param url a subversion URL
	 * @throws SVNException 
	 */
	public SVNDataSource(String url) throws SVNException {
		this(url,1,-1,"anonymous","anonymous");
	}
	
	public SVNDataSource(ISubversionInfo info) throws SVNException {
		this(info.getUrl(),1,-1,info.getLogin(),info.getPassword());
	}
	
	public SVNDataSource(String url, long startRevision, long endRevision) throws SVNException {
		this(url,startRevision,endRevision,"anonymous","anonymous");
	}
	
	public SVNDataSource(String url, String name, String password) throws SVNException {
		this(url,1,-1,name,password);
		
	}

	
	@SuppressWarnings("unchecked")
	public SVNDataSource(String url, long startRevision, long endRevision, String name, String password) throws SVNException {
		
        /*
         * Initializes the library (it must be done before ever using the
         * library itself)
         */
        setupLibrary();
        
        SVNRepository repository = null;

    	/*
         * Creates an instance of SVNRepository to work with the repository.
         * All user's requests to the repository are relative to the
         * repository location used to create this SVNRepository.
         * SVNURL is a wrapper for URL strings that refer to repository locations.
         */
        repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(url));
        
        /*
         * User's authentication information (name/password) is provided via  an 
         * ISVNAuthenticationManager  instance.  SVNWCUtil  creates  a   default 
         * authentication manager given user's name and password.
         * 
         * Default authentication manager first attempts to use provided user name 
         * and password and then falls back to the credentials stored in the 
         * default Subversion credentials storage that is located in Subversion 
         * configuration area. If you'd like to use provided user name and password 
         * only you may use BasicAuthenticationManager class instead of default 
         * authentication manager:
         * 
         *  authManager = new BasicAuthenticationsManager(userName, userPassword);
         *  
         * You may also skip this point - anonymous access will be used. 
         */
        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(name, password);
        repository.setAuthenticationManager(authManager);
 
        /*
         * Gets the latest revision number of the repository
         */
        if(endRevision==-1) {
            endRevision = repository.getLatestRevision();
        }
 
        Collection logEntries = null;
        /*
         * Collects SVNLogEntry objects for all revisions in the range
         * defined by its start and end points [startRevision, endRevision].
         * For each revision commit information is represented by
         * SVNLogEntry.
         * 
         * the 1st parameter (targetPaths - an array of path strings) is set
         * when restricting the [startRevision, endRevision] range to only
         * those revisions when the paths in targetPaths were changed.
         * 
         * the 2nd parameter if non-null - is a user's Collection that will
         * be filled up with found SVNLogEntry objects; it's just another
         * way to reach the scope.
         * 
         * startRevision, endRevision - to define a range of revisions you are
         * interested in; by default in this program - startRevision=0, endRevision=
         * the latest (HEAD) revision of the repository.
         * 
         * the 5th parameter - a boolean flag changedPath - if true then for
         * each revision a corresponding SVNLogEntry will contain a map of
         * all paths which were changed in that revision.
         * 
         * the 6th parameter - a boolean flag strictNode - if false and a
         * changed path is a copy (branch) of an existing one in the repository
         * then the history for its origin will be traversed; it means the 
         * history of changes of the target URL (and all that there's in that 
         * URL) will include the history of the origin path(s).
         * Otherwise if strictNode is true then the origin path history won't be
         * included.
         * 
         * The return value is a Collection filled up with SVNLogEntry Objects.
         */
        try {
	        logEntries = repository.log(new String[] {""}, null,
	                startRevision, endRevision, true, true);
	 
	        
	        this.seriesGroups = new ArrayList<SeriesGroup>();
	        SeriesGroup developers = new SeriesGroup("developers");
	        this.seriesGroups.add(developers);
	        
	        // Here, we need to know the names of the developers and add them
	        
	        for (Iterator<SVNLogEntry> entries = logEntries.iterator(); entries.hasNext();) {
	        	
	            /*
	             * gets a next SVNLogEntry
	             */
	            SVNLogEntry logEntry = entries.next();
	
	            // Don't parse entries when the author is null
	            if(logEntry.getAuthor()!=null) {
	            	setNotEmpty();
	        	    developers.getAddSeries(logEntry.getAuthor()).addEvent(new Point(logEntry.getDate(),logEntry.getMessage()));
	        	    super.updateFirstEvent(logEntry.getDate());           
	        	    super.updateLastEvent(logEntry.getDate());
	    	}
	            
	            //System.out.println("---------------------------------------------");
	            /*
	             * gets the revision number
	             */
	            //System.out.println("revision: " + logEntry.getRevision());
	            /*
	             * gets the author of the changes made in that revision
	             */
	            //System.out.println("author: " + logEntry.getAuthor());
	            /*
	             * gets the time moment when the changes were committed
	             */
	            //System.out.println("date: " + logEntry.getDate());
	            /*
	             * gets the commit log message
	             */
	            //System.out.println("log message: " + logEntry.getMessage());
	            /*
	             * displaying all paths that were changed in that revision; cahnged
	             * path information is represented by SVNLogEntryPath.
	             */
	            // The code below isn't currently used
	            //if (logEntry.getChangedPaths().size() > 0) {
	                //System.out.println();
	                //System.out.println("changed paths:");
	                /*
	                 * keys are changed paths
	                 */
	                //Set changedPathsSet = logEntry.getChangedPaths().keySet();
	 
	                // We don't retrieve changed paths right now
	                //for (Iterator changedPaths = changedPathsSet.iterator(); changedPaths.hasNext();) {
	                    /*
	                     * obtains a next SVNLogEntryPath
	                     */
	                    //SVNLogEntryPath entryPath = (SVNLogEntryPath) logEntry.getChangedPaths().get(changedPaths.next());
	                    /*
	                     * SVNLogEntryPath.getPath returns the changed path itself;
	                     * 
	                     * SVNLogEntryPath.getType returns a charecter describing
	                     * how the path was changed ('A' - added, 'D' - deleted or
	                     * 'M' - modified);
	                     * 
	                     * If the path was copied from another one (branched) then
	                     * SVNLogEntryPath.getCopyPath &
	                     * SVNLogEntryPath.getCopyRevision tells where it was copied
	                     * from and what revision the origin path was at.
	                     */
	                    /*System.out.println(" "
	                            + entryPath.getType()
	                            + "	"
	                            + entryPath.getPath()
	                            + ((entryPath.getCopyPath() != null) ? " (from "
	                                    + entryPath.getCopyPath() + " revision "
	                                    + entryPath.getCopyRevision() + ")" : ""));*/
	                //}
	           // }
	        }
        } catch(SVNException ex) {
        	// No-op for now
        }
	}

	public boolean isEmpty() {
		return this.isEmpty;
	}
	
	private void setNotEmpty() {
		this.isEmpty = false;
	}

	
    /*
     * Initializes the library to work with a repository via 
     * different protocols.
     */
    private static void setupLibrary() {
        /*
         * For using over http:// and https://
         */
        DAVRepositoryFactory.setup();
        /*
         * For using over svn:// and svn+xxx://
         */
        SVNRepositoryFactoryImpl.setup();
        
        /*
         * For using over file:///
         */
        FSRepositoryFactory.setup();
    }


}
