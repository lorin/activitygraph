
package edu.unl.cse.activitygraph.hpcs;

import java.sql.Timestamp;

public class ShellEvent extends ComparableTimedEvent {
    
    public int accountId;
    public Timestamp timestamp;
    public String command;
    public String arguments;
    public String hostnameId;
    
    /**
     *Creates a new instance of HstShellEvent 
     */
    public ShellEvent(int accountId, Timestamp timestamp, String command, String arguments, String hostnameId ) {
        this.accountId  = accountId;
        this.command    = command;
        this.arguments  = arguments;
        this.hostnameId = hostnameId;
        this.timestamp  = timestamp;
    }
    
    public boolean equals(Object o){
        if (o instanceof ShellEvent){
            ShellEvent tmp = (ShellEvent)o;
            if (    this.accountId==tmp.accountId &&
                    this.command.equals(tmp.command) && 
                    this.arguments.equals(tmp.arguments) &&
                    this.hostnameId.equals(tmp.hostnameId) &&
                    this.timestamp.equals(tmp.timestamp)
                ){
                return true;
            }else
                return false;
        }else
            return false;
    }
    
    public String toString(){
        return ("accountId='"+accountId+"', timestamp='"+timestamp+"', command='"+command+"', arguments='"+arguments+"', hostnameId='"+hostnameId+"'");
    }

	public Timestamp getTimestamp() {
		return timestamp;
	}    
}
