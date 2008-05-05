package edu.unl.cse.activitygraph.hpcs;


import java.sql.Timestamp;

public class EditorEvent extends ComparableTimedEvent {
    
    public int accountId;
    public Timestamp timestamp;
    public String file;
    public String editorEventToolId;
    public String editorEventTypeId;
    
    /**
     *Creates a new instance of HstEditorEvent 
     */
    public EditorEvent(int accountId, Timestamp timestamp, String file, String editorEventToolId, String editorEventTypeId ) {
        this.accountId = accountId;
        this.editorEventToolId = editorEventToolId;
        this.editorEventTypeId = editorEventTypeId;
        this.file = file;
        this.timestamp = timestamp;
    }
    
    public boolean equals(Object o){
        if (o instanceof EditorEvent){
            EditorEvent tmp = (EditorEvent)o;
            if (    this.accountId==tmp.accountId &&
                    this.editorEventToolId.equals(tmp.editorEventToolId) &&
                    this.editorEventTypeId.equals(tmp.editorEventTypeId) && 
                    this.file.equals(tmp.file) &&
                    this.timestamp.equals(tmp.timestamp) 
                ){
                return true;
            }else
                return false;
        }else
            return false;
    }
    
    public String toString(){
        return ("accountId = "+accountId+", editorEventToolId = "+editorEventToolId+", editorEventTypeId = "+editorEventTypeId+", file = "+file+", timestamp = "+timestamp);
    }

	public Timestamp getTimestamp() {
		return timestamp;
	}
}
