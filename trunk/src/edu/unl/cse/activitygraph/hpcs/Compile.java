package edu.unl.cse.activitygraph.hpcs;

import java.sql.Timestamp;
import java.util.Vector;

public class Compile implements ToolTip{
    
    private int id;
    private int account_id;
    private int assignment_id;
    private int cluster_id;
    private int approach_id;
    private int compile_reason_id;
    private Timestamp timestamp;
    private double compiletime;
    private boolean successful;
    private String command;
    private String description;
    private Vector<Sourcefile> sourceFiles = new Vector<Sourcefile>();
    
    private boolean assignmentModified = false;
    private int initial_assignment_id;
    private int initial_approach_id;
    
    // The compile number for a particular assignment, this is the X-value on a chart
    private int compile_number;
    
    public Compile(int id, int account_id, int assignment_id, int cluster_id, int approach_id, int compile_reason_id,
            Timestamp timestamp, double compiletime, boolean successful, String command, String description) {
        
        this.id                = id;
        this.account_id        = account_id;
        this.assignment_id     = assignment_id;
        this.cluster_id        = cluster_id;
        this.approach_id       = approach_id;
        this.compile_reason_id = compile_reason_id;
        this.timestamp         = timestamp;
        this.compiletime       = compiletime;
        this.successful        = successful;
        this.command           = command;
        this.description       = description;
        
        this.initial_assignment_id = assignment_id;
        this.initial_approach_id   = approach_id;
        
        // Compile number isn't known until the compile is drawn to the chart
        this.compile_number = -1;
    }
    
    public boolean equals(Object o){
        if (o instanceof Compile){
            Compile tmp = (Compile)o;
            
            if (    this.id == tmp.id &&
                    this.account_id == tmp.account_id &&
                    this.assignment_id == tmp.assignment_id &&
                    this.cluster_id == tmp.cluster_id &&
                    this.approach_id == tmp.approach_id &&
                    this.compile_reason_id == tmp.compile_reason_id &&
                    this.timestamp == tmp.timestamp &&
                    this.compiletime == tmp.compiletime &&
                    this.successful == tmp.successful &&
                    this.command.equals(tmp.command) &&
                    this.description.equals(tmp.description)
                    ){
                return true;
            }else
                return false;
        }else
            return false;
    }
    
    public String toString(){
        String tmp =  "number:              " +compile_number
        			 +"\nid:                "+id
                     +"\naccount_id:        "+account_id
                     +"\nassignment_id:     "+assignment_id
                     +"\ncluster_id:        "+cluster_id
                     +"\napproach_id:       "+approach_id
                     +"\ncompile_reason_id: "+compile_reason_id
                     +"\ntimestamp:         "+timestamp
                     +"\ncompiletime:       "+compiletime
                     +"\nsuccessful:        "+successful
                     +"\ncommand:           "+command
                     +"\ndescription:       "+description;
        return tmp;
    }
    
    public String getToolTipText(){
        String tmp = "<html><body>"
        	         +"number:                "+compile_number
                     +"<br>id:                "+id
                     +"<br>account_id:        "+account_id
                     +"<br>assignment_id:     "+assignment_id
                     +"<br>cluster_id:        "+cluster_id
                     +"<br>approach_id:       "+approach_id
                     +"<br>compile_reason_id: "+compile_reason_id
                     +"<br>timestamp:         "+timestamp.toString()+" ("+this.timestamp.getTime()+")"
                     +"<br>compiletime:       "+compiletime
                     +"<br>successful:        "+successful
                     +"<br>command:           "+command
                     +"<br>description:       "+description
//                     +"<br>isSelectedOnScreen: "+selectedOnScreen
                     +"</body></html>";
        return tmp;
    }

    public int getId() {
        return id;
    }

    public int getAccount_id() {
        return account_id;
    }

    public int getAssignment_id() {
        return assignment_id;
    }

    public int getCluster_id() {
        return cluster_id;
    }

    public int getApproach_id() {
        return approach_id;
    }

    public int getCompile_reason_id() {
        return compile_reason_id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public double getCompiletime() {
        return compiletime;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }

    public boolean isAssignmentModified() {
        return assignmentModified;
    }

//    public void setAssignmentModified(boolean assignmentModified) {
//        this.assignmentModified = assignmentModified;
//    }
    
    public void setUpdatedToDB(){
        this.initial_assignment_id = this.assignment_id;
        this.initial_approach_id   = this.approach_id;
        this.assignmentModified    = false;
    }
    
    public void restoreInitialValues(){
        this.assignment_id      = this.initial_assignment_id;
        this.approach_id        = this.initial_approach_id;
        this.assignmentModified = false;
    }

    public void setAssignment_id(int assignment_id) {       
        this.assignment_id = assignment_id;
        
        if ( this.assignment_id != this.initial_assignment_id || this.approach_id != this.initial_approach_id){
            assignmentModified = true;
        }else
            assignmentModified = false;
    }

    public void setApproach_id(int approach_id) {
        this.approach_id = approach_id;
        
        if ( this.assignment_id != this.initial_assignment_id || this.approach_id != this.initial_approach_id){
            assignmentModified = true;
        }else
            assignmentModified = false;
    }
    
    public void setCompileNumber(int compile_number) {
    	this.compile_number = compile_number;
    }
    


    public Vector<Sourcefile> getSourceFiles() {
        return sourceFiles;
    }

}
