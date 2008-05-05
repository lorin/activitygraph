package edu.unl.cse.activitygraph.hpcs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import edu.unl.cse.activitygraph.interfaces.IStatusUpdater;

public class AssignmentUpdaterServicesImplementation implements
		AssignmentUpdaterServices {
	
    private Vector<HpcsClass> classes;              //= new Vector();
    private Vector<Compile> compiles;               //= new Vector();
    private Vector<String> sourcefileNames;         //= new Vector();
    private Vector<HpcsAssignment> assignments;     //= new Vector();
    private Vector<Run> runs;
    private Vector<ShellEvent> shellEvents;
    private Vector<EditorEvent> editorEvents;
	private Statement stmt;
	private String query;
	private ResultSet result;
    
    private static Connection dbConn;
    
    /**
     *The JDBC driver name (PostgreSQL)
     */
    private static final String DRIVER_NAME = "org.postgresql.Driver";
    /**
     *The JDBC url prefix for connecting to a PostgreSQL database
     */
    private static final String JDBC_URL_PREFIX = "jdbc:postgresql://";

    
    public AssignmentUpdaterServicesImplementation(String url, String username, String password) throws DatabaseConnectionException {
    	connectToDatabase(url,username,password);
    	this.loadClassesFromDB();
    }
    
    private void loadClassesFromDB() {
        classes = new Vector<HpcsClass>();
        try{
            stmt   = dbConn.createStatement();
            query  = "SELECT * FROM class ORDER BY id";
            result = stmt.executeQuery(query);
            ResultSet resultProf;
            
            //save data to buffer
            HpcsClass tmpCls;
            while(result.next()){
                tmpCls = new HpcsClass(result.getInt("id"),
                        result.getString("name"),
                        result.getString("semester"),
                        result.getString("university"),
                        result.getInt("project_number"),
                        result.getString("url"),
                        result.getString("group_name"),
                        result.getInt("professor"),
                        result.getString("details"),
                        result.getBoolean("endofstudy"),
                        result.getBoolean("background"),
                        result.getBoolean("over"),
                        result.getInt("students_total"),
                        result.getInt("students_consented")
                        );
                
                //get the professor name of this class
                stmt   = dbConn.createStatement();
                query  = "SELECT firstname, lastname FROM account WHERE id="+tmpCls.getProfessor()+" LIMIT 1";
                resultProf = stmt.executeQuery(query);
                while(resultProf.next()){
                    tmpCls.setProfessorName(resultProf.getString("firstname")+" "+resultProf.getString("lastname"));
                }
                
                classes.add(tmpCls);
                
            }
            
        }catch(Exception e){
            System.out.println("Exception in AssignmentUpdater.readClassesFromDB()");
            e.printStackTrace();
        }
	}

	private void connectToDatabase(String url, String username, String password) throws DatabaseConnectionException {
        System.out.print("Load the JDBC driver.");
        try{
            Class.forName(DRIVER_NAME);
        }catch(ClassNotFoundException e){
        	throw new DatabaseConnectionException("JDBC driver \""+DRIVER_NAME+"\" could not be loaded.");
        }
        try {
            dbConn     = DriverManager.getConnection(JDBC_URL_PREFIX+url, username, password);
        } catch (SQLException e) {
        	throw new DatabaseConnectionException(e.getMessage());
        }
    }



	public Vector<HpcsAssignment> getAssignments() {
		return assignments;
	}

	public Vector<Compile> getCompiles() {
		return compiles;
	}

	public Vector<HpcsClass> getHpcsClasses() {
		return classes;
	}

	public Vector<String> getSourcefileNames() {
		return sourcefileNames;
	}

	public Vector<StudentAccount> getStudentAccounts(int classId) {
        Vector<StudentAccount> studentsOfClass = new Vector<StudentAccount>();
        try{
        	stmt= dbConn.createStatement();
            
            String query = 	"SELECT * FROM account  WHERE class_id="+classId +" ORDER BY id";
            ResultSet result = stmt.executeQuery(query);
            
            while(result.next()){
                studentsOfClass.add(new StudentAccount(
                        result.getInt("id"),
                        result.getInt("class_id"),
                        result.getString("email"),
                        result.getString("firstname"),
                        result.getString("lastname"),
                        result.getString("hackystat_key"),
                        result.getString("pin"),
                        result.getInt("role_id"),
                        result.getString("accountname"),
                        result.getString("timezone"),
                        result.getString("anonym")
                        )
                        );
                
            }
            return studentsOfClass;
        }catch(Exception e){
            System.out.println("Exception in AssignmentUpdaterServicesImplementation.getStudentAccounts(int classId)");
            e.printStackTrace();
            return null;
        }
	}

	public void loadDataFromDB(int accountId, int classId,IStatusUpdater display) {
		int steps = 8;
        loadCompilesFromDB(accountId,display);
        display.updateTotal(1*100/steps);
        loadSourceFilesFromDB(display);
        display.updateTotal(2*100/steps);
        determineSourcefileNames(display);
        display.updateTotal(3*100/steps);
        loadAssignmentsFromDB(classId,display);
        display.updateTotal(4*100/steps);
        loadApproachesFromDB(display);
        display.updateTotal(5*100/steps);
        loadRunsFromDB(accountId,display);
        display.updateTotal(6*100/steps);
        loadEditorEventsFromDB(accountId,display);
        display.updateTotal(7*100/steps);
        loadShellEventsFromDB(accountId,display);
        display.updateTotal(8*100/steps);
	}

	private void loadShellEventsFromDB(int accountId, IStatusUpdater display) {
		this.shellEvents = this.loadShellEvents(accountId,display);
		
	}

	private void loadEditorEventsFromDB(int accountId, IStatusUpdater display) {
		this.editorEvents = this.loadEditorEvents(accountId,display);
		
	}

	private void loadRunsFromDB(int accountId, IStatusUpdater display) {
		this.runs = this.loadRuns(accountId,display);
		
	}

	private void determineSourcefileNames(IStatusUpdater display) {
        sourcefileNames = new Vector<String>();
        for (Compile comp : this.compiles){
            for(Sourcefile srcfile : comp.getSourceFiles()){
                if (!sourcefileNames.contains(srcfile.getFilename())){
                    sourcefileNames.add(srcfile.getFilename());
                }
            }
        }
        sourcefileNames.add("without sourcefile");		
	}

	private void loadApproachesFromDB(IStatusUpdater display) {
        for (HpcsAssignment ass : this.assignments){
            Vector<Approach> approaches = getApproaches(ass.getId());   //reads from DB
            ass.setApproaches(approaches);
        }		
	}

	private Vector<Approach> getApproaches(int assignmentId) {
        Vector<Approach> approaches = new Vector<Approach>();
        
        try{
            stmt = dbConn.createStatement();
            
            query = "SELECT approach_id FROM assignment_approach WHERE assignment_id="+assignmentId;
            query = "SELECT * FROM approach WHERE id IN ("+query+") ORDER BY id";
            result = stmt.executeQuery(query);
            
            Approach tmp;
            while(result.next()){
                tmp = new Approach(result.getInt("id"),
                        result.getString("name"),
                        result.getString("comments"),
                        result.getString("pattern_sourcecode"),
                        result.getBoolean("serial")
                        );
                
                approaches.add(tmp);
            }
            return approaches;
        }catch(Exception e){
            System.out.println("Exception in AssignmentUpdater.getApproaches(int assignmentId)");
            System.out.println("query: "+query);
            e.printStackTrace();
            return approaches;
        }
	}

	private void loadAssignmentsFromDB(int classId, IStatusUpdater display) {
        assignments = new Vector<HpcsAssignment>();
        
        try{
            
            stmt= dbConn.createStatement();
            String query = "SELECT * FROM assignment WHERE class_id="+ classId;
            result = stmt.executeQuery(query);
            
            while(result.next()){
                
                assignments.add(new HpcsAssignment(
                        result.getInt("id"),
                        result.getInt("problem_type_id"),
                        result.getInt("class_id"),
                        result.getInt("assigned_number"),
                        result.getBoolean("serial_code_provided"),
                        result.getString("comments"),
                        result.getString("validation_input_file"),
                        result.getString("validation_output_file"),
                        result.getString("validation_parameters"),
                        result.getTimestamp("due_date"),
                        result.getTimestamp("submission_date"),
                        result.getTimestamp("extension_date"),
                        result.getInt("grade_max"),
                        result.getBoolean("class_assignment")
                        )
                        );
            }
        } catch (Exception e){
            System.out.println("Exception in AssignmentUpdater.loadAssignmentsFromDB()");
            e.printStackTrace();
        }		
	}

	private void loadSourceFilesFromDB(IStatusUpdater display) {
		display.updateText("Loading source files");
		System.out.println("loadSourceFilesFromDB");
        Sourcefile srcFile;
        System.out.println(compiles.size() + " compiles to process");
        int i=0;
        int size = compiles.size();
        for (Compile comp : compiles){
        	display.updateCurrent(100*i / size);
        	i++;
        	//System.out.println("Compile " + i++);
            try{
                stmt= dbConn.createStatement();
                
                String query = "SELECT id, compile_id, filename, filepath, filetype, sloc, prev_sourcefile_id, lines_added, lines_deleted "
                        +"FROM sourcefile WHERE compile_id="+comp.getId();
                result = stmt.executeQuery(query);
                Vector<Sourcefile> srcFiles = comp.getSourceFiles();
                while(result.next()){
                    
                    srcFile = new Sourcefile(
                            result.getInt("id"),
                            result.getInt("compile_id"),
                            "",                                //new String(result.getBytes("code")),
                            result.getString("filename"),
                            result.getString("filepath"),
                            result.getString("filetype"),
                            result.getInt("sloc"),
                            result.getInt("prev_sourcefile_id"),
                            result.getInt("lines_added"),
                            result.getInt("lines_deleted")
                            );
                    srcFiles.add(srcFile);
                    //sourcefiles.add(srcFile );
                    
                }
            } catch (Exception e){
                System.out.println("Exception in AssignmentUpdater.loadSourceFileInformationFromDB(int compileId)");
                e.printStackTrace();
            }
        }		
	}

	private void loadCompilesFromDB(int accountID, IStatusUpdater display) {
		display.updateText("Loading compiles");
        compiles = new Vector<Compile>();
        try{
            stmt= dbConn.createStatement();
            query = 	"SELECT * FROM compile  WHERE account_id="+accountID+" ORDER BY timestamp";
            result = stmt.executeQuery(query);
            while(result.next()){
                
                compiles.add(new Compile(
                        result.getInt("id"),
                        result.getInt("account_id"),
                        result.getInt("assignment_id"),
                        result.getInt("cluster_id"),
                        result.getInt("approach_id"),
                        result.getInt("compile_reason_id"),
                        result.getTimestamp("timestamp"),
                        result.getDouble("compiletime"),
                        result.getBoolean("successful"),
                        result.getString("command"),
                        result.getString("description")
                        )
                        );
                
            }
        } catch (Exception e){
            System.out.println("Exception in AssignmentUpdater.loadCompilesFromDB(int accountID)");
            e.printStackTrace();
        }		
	}

	public int updateChangedAssignmentsIntoDB() {
		throw new RuntimeException("updateChangedAssignmentsIntoDB");
	}
	
    private Vector<Run> loadRuns(int accountId, IStatusUpdater display){
    	display.updateText("Loading runs");
        ResultSet tmpRs;
        String query;
        Statement stmt;
        Vector<Run> tmpVect;
        try {
            if (dbConn == null || dbConn.isClosed() ){
                return new Vector<Run>();
            }
            stmt = dbConn.createStatement();
            query = "SELECT id, account_id, assignment_id, approach_id, " +
                    " cluster_id, type, timestamp, runtime, path," +
                    " successful, command, processors FROM run " +
                    "WHERE account_id='"+accountId+ "'";
            //System.out.println(query);
            tmpRs = stmt.executeQuery(query);
            tmpVect = new Vector<Run>();
            while(tmpRs.next()){
                tmpVect.add(new Run(
                        tmpRs.getInt("id"),
                        accountId,
                        tmpRs.getInt("assignment_id"),
                        tmpRs.getInt("approach_id"),
                        tmpRs.getInt("cluster_id"),
                        tmpRs.getString("type"),
                        tmpRs.getTimestamp("timestamp"),
                        tmpRs.getDouble("runtime"),
                        tmpRs.getString("path"),
                        tmpRs.getBoolean("successful"),
                        tmpRs.getString("command"),
                        tmpRs.getInt("processors")   ));
            }
            
            tmpRs.close();
            tmpRs = null;
            return tmpVect;
        }catch (Exception e){
            e.printStackTrace();
        }
        
        return null;
    }
    
    private Vector<ShellEvent> loadShellEvents(int accountId, IStatusUpdater display){
        display.updateText("Loading shell events");
        ResultSet tmpRs;
        String query;
        Statement stmt;
        Vector<ShellEvent> tmpVect;
        try {
            if (dbConn == null || dbConn.isClosed() ){
                return new Vector<ShellEvent>();
            }
            stmt = dbConn.createStatement();
            query = "SELECT timestamp, command, arguments, hostname_id FROM shell_event WHERE account_id="+accountId;
            tmpRs = stmt.executeQuery(query);
            tmpVect = new Vector<ShellEvent>();
            while(tmpRs.next()){
                tmpVect.add(new ShellEvent(
                        accountId,
                        tmpRs.getTimestamp("timestamp"),
                        tmpRs.getString("command"),
                        tmpRs.getString("arguments"),
                        tmpRs.getString("hostname_id")   ));
            }
            tmpRs.close();
            tmpRs = null;
            return tmpVect;
        }catch (Exception e){
            e.printStackTrace();
        }
        
        return null;
    }
    
    private Vector<EditorEvent> loadEditorEvents(int accountId, IStatusUpdater display){
    	display.updateText("Loading editor events");
        ResultSet tmpRs;
        String query;
        Statement stmt;
        Vector<EditorEvent> tmpVect;
        try {
            if (dbConn == null || dbConn.isClosed() ){
                return new Vector<EditorEvent>();
            }
            stmt = dbConn.createStatement();
            query = "SELECT editor_event_tool_id, editor_event_type_id, timestamp, file FROM editor_event WHERE account_id='"+accountId+"'";
            //System.out.println(query);
            tmpRs = stmt.executeQuery(query);
            tmpVect = new Vector<EditorEvent>();
            while(tmpRs.next()){
                tmpVect.add(new EditorEvent(
                        accountId,
                        tmpRs.getTimestamp("timestamp"),
                        tmpRs.getString("file"),
                        tmpRs.getString("editor_event_tool_id"),
                        tmpRs.getString("editor_event_type_id")   ));
            }
            tmpRs.close();
            tmpRs = null;
            return tmpVect;
        }catch (Exception e){
            e.printStackTrace();
        }
        
        return null;
    }

	public Vector<Run> getRuns() {
		return runs;
	}

	public Vector<EditorEvent> getEditorEvents() {
		return editorEvents;
	}

	public Vector<ShellEvent> getShellEvents() {
		return shellEvents;
	}



}
