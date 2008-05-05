package edu.unl.cse.activitygraph.hpcs;

import edu.unl.cse.activitygraph.hpcs.Compile;
import edu.unl.cse.activitygraph.hpcs.HpcsAssignment;
import edu.unl.cse.activitygraph.hpcs.HpcsClass;
import edu.unl.cse.activitygraph.hpcs.StudentAccount;
import edu.unl.cse.activitygraph.interfaces.IStatusUpdater;

import java.util.Vector;

public interface AssignmentUpdaterServices {
    
    //public void setAssignmentUpdaterGuiServices(AssignmentUpdaterGuiServices gui);

    public Vector<HpcsClass> getHpcsClasses();

    public Vector<StudentAccount> getStudentAccounts(int classId);
    
    public void loadDataFromDB(int accountId, int classId, IStatusUpdater display);
    
    //public Vector<Sourcefile> getSourcefiles();
    
    public Vector<String> getSourcefileNames();
    
    public Vector<Compile> getCompiles();
    
    public Vector<HpcsAssignment> getAssignments();
    
    //public Vector<Approach> getApproaches(int assignmentId);
    
    public int updateChangedAssignmentsIntoDB();

	public Vector<Run> getRuns();
	
	public Vector<ShellEvent> getShellEvents();
	
	public Vector<EditorEvent> getEditorEvents();
    
}
