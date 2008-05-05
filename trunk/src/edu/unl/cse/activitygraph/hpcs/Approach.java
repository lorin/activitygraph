package edu.unl.cse.activitygraph.hpcs;

public class Approach implements ToolTip{
    private int id;
    private String name;
    private String comments;
    private String pattern_sourcecode;
    private boolean serial;
    
    public Approach(int id, String name, String comments, String pattern_sourcecode, boolean serial) {
        this.id                 = id;
        this.name               = name;
        this.comments           = comments;
        this.pattern_sourcecode = pattern_sourcecode;
        this.serial             = serial;
    }
    
    public boolean equals(Object o){
        if (o instanceof Approach){
            Approach tmp = (Approach)o;
            
            if (    this.id == tmp.id &&
                    this.name.equals(tmp.name) &&
                    this.comments.equals(tmp.comments) 
                    //this.pattern_sourcecode.equals(tmp.pattern_sourcecode) &&
                    //this.serial == tmp.serial
                    ){
                return true;
            }else
                return false;
        }else
            return false;
    }
    
    public String toString(){
        String tmp = "id:               "+id
                +"\nname:               "+name
                +"\ncomments:           "+comments
                +"\npattern_sourcecode: "+pattern_sourcecode
                +"\nserial:             "+serial;
        return tmp;
    }
    
    public String getToolTipText(){
        String tmp = "<html><body><table>"
                +"id:                 "+id
                +"<br>name:               "+name
                +"<br>comments:           "+comments
                +"<br>pattern_sourcecode: "+pattern_sourcecode
                +"<br>serial:             "+serial
                +"</body></html>";
        return tmp;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getComments() {
        return comments;
    }

    public String getPattern_sourcecode() {
        return pattern_sourcecode;
    }

    public boolean isSerial() {
        return serial;
    }
    
}
