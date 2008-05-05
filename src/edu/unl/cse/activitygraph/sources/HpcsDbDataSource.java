package edu.unl.cse.activitygraph.sources;

import java.sql.Timestamp;
import java.util.ArrayList;

import edu.unl.cse.activitygraph.Point;
import edu.unl.cse.activitygraph.SeriesGroup;
import edu.unl.cse.activitygraph.dialogs.HpcsLoaderProgressDialog;
import edu.unl.cse.activitygraph.dialogs.StudentChooserDialog;
import edu.unl.cse.activitygraph.hpcs.AssignmentUpdaterServices;
import edu.unl.cse.activitygraph.hpcs.AssignmentUpdaterServicesImplementation;
import edu.unl.cse.activitygraph.hpcs.Compile;
import edu.unl.cse.activitygraph.hpcs.DatabaseConnectionException;
import edu.unl.cse.activitygraph.hpcs.EditorEvent;
import edu.unl.cse.activitygraph.hpcs.Run;
import edu.unl.cse.activitygraph.hpcs.ShellEvent;
import edu.unl.cse.activitygraph.hpcs.Sourcefile;


public class HpcsDbDataSource extends GenericDataSource {
	
    private AssignmentUpdaterServices appServices;
	private boolean isEmpty = true;
	
	// For now, this will spawn a dialog on its own when you open it
	public HpcsDbDataSource(String url, String username, String password) throws DatabaseConnectionException {
		appServices = new AssignmentUpdaterServicesImplementation(url,username,password);

		StudentChooserDialog chooserDlg = makeStudentChooserDialog();
        chooserDlg.setVisible(true);
        if(chooserDlg.userClickedOk()) {
        	//this.appServices.loadDataFromDB(dlg.getAccountId(), dlg.getClassId());
        	new HpcsLoaderProgressDialog(appServices,chooserDlg.getAccountId(),chooserDlg.getClassId());
        	
        	this.populateListsofData();
        }
        
	}
	

	private StudentChooserDialog makeStudentChooserDialog() {
		return new StudentChooserDialog(appServices);
	}
	
	/**
	 * Populates the various lists that are ultimately used by ActivityGraph
	 * for visualization 
	 *
	 */
	private void populateListsofData() {
		this.seriesGroups = new ArrayList<SeriesGroup>();
        SeriesGroup compiles = new SeriesGroup("compiles");
        this.seriesGroups.add(compiles);
        
        System.out.println("Processing " + appServices.getCompiles().size() + " compiles");
        
        for(Compile compile : appServices.getCompiles()) {
        	for(Sourcefile sourcefile : compile.getSourceFiles()) {
        		Timestamp ts = compile.getTimestamp();
        		this.updateFirstEvent(ts);
        		this.updateLastEvent(ts);
        		compiles.getAddSeries(sourcefile.getFilename()).addEvent(new Point(ts,compile.getCommand()));
        		this.setNotEmpty();
        	}
        	
        }
        
        SeriesGroup other = new SeriesGroup("other");
        this.seriesGroups.add(other);
        for(Run run : appServices.getRuns()) {
        	Timestamp ts = run.getTimestamp();
        	other.getAddSeries("run").addEvent(new Point(ts, run.getCommand()));
    		this.setNotEmpty();
    		this.updateFirstEvent(ts);
    		this.updateLastEvent(ts);        	
        }
        
        for(ShellEvent shell : appServices.getShellEvents()) {
        	Timestamp ts = shell.getTimestamp();
        	other.getAddSeries("shell").addEvent(new Point(ts, shell.command));
    		this.setNotEmpty();
    		this.updateFirstEvent(ts);
    		this.updateLastEvent(ts);        	
        }
        
        for(EditorEvent edit : appServices.getEditorEvents()) {
        	Timestamp ts = edit.getTimestamp();
        	other.getAddSeries("edit").addEvent(new Point(ts, edit.file));
    		this.setNotEmpty();
    		this.updateFirstEvent(ts);
    		this.updateLastEvent(ts);        	
        }
		
	}

	public boolean isEmpty() {
		return this.isEmpty;
	}
	
	private void setNotEmpty() {
		this.isEmpty = false;
	}

}
