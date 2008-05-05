
package edu.unl.cse.activitygraph.hpcs;

import java.sql.Timestamp;

public class Run extends ComparableTimedEvent {    
    
    private int id;
    private int accountId;
    private int assignmentId;
    private int approachId;
    private int clusterId;
    private String type;
    private Timestamp timestamp;
    private double runtime;
    private String path;
    private boolean successful;
    private String command;
    private int processors;
    
    public Run(int id, int accountId, int assignmentId, int approachId, int clusterId, String type, Timestamp timestamp,
                double runtime, String path, boolean successful, String command, int processors
    ) {
        
        this.id           = id;
        this.accountId    = accountId;
        this.assignmentId = assignmentId;
        this.approachId   = approachId;
        this.clusterId    = clusterId;
        this.type         = type;
        this.timestamp    = timestamp;
        this.runtime      = runtime;
        this.path         = path;
        this.successful   = successful;
        this.command      = command;
        this.processors   = processors;
        
    }

    public int getId() {
        return id;
    }

    public int getAccountId() {
        return accountId;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public int getApproachId() {
        return approachId;
    }

    public int getClusterId() {
        return clusterId;
    }

    public String getType() {
        return type;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public double getRuntime() {
        return runtime;
    }

    public String getPath() {
        return path;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public String getCommand() {
        return command;
    }

    public int getProcessors() {
        return processors;
    }
    
}
