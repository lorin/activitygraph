
package edu.unl.cse.activitygraph.hpcs;

public class Sourcefile implements ToolTip{
    
    private int id;
    private int compile_id;
    //private String code;
    private String filename;
    private String filepath;
    private String filetype;
    private int sloc;
    private int prev_sourcefile_id;
    private int lines_added;
    private int lines_deleted;
    
    public Sourcefile(int id, int compile_id, String code, String filename, String filepath, String filetype,
                      int sloc, int prev_sourcefile_id, int lines_added, int lines_deleted) {
        
    this.id                 = id;
    this.compile_id          = compile_id;
    //this.code               = code;
    this.filename           = filename;
    this.filepath           = filepath;
    this.filetype           = filetype;
    this.sloc               = sloc;
    this.prev_sourcefile_id = prev_sourcefile_id;
    this.lines_added        = lines_added;
    this.lines_deleted      = lines_deleted;
        
    }
    
    public boolean equals(Object o){
        if (o instanceof Sourcefile){
            Sourcefile tmp = (Sourcefile)o;
            
            if (    this.id == tmp.id &&
                    this.compile_id == tmp.compile_id &&
                    //this.code.equals(tmp.code) &&
                    this.filename.equals(tmp.filename) &&
                    this.filepath.equals(tmp.filepath) &&
                    this.filetype.equals(tmp.filetype) &&
                    this.sloc == tmp.sloc &&
                    this.prev_sourcefile_id == tmp.prev_sourcefile_id &&
                    this.lines_added == tmp.lines_added &&
                    this.lines_deleted == tmp.lines_deleted
                    ){
                return true;
            }else
                return false;
        }else
            return false;
    }
    
    public String toString(){
        String tmp = "id:                    "+id
                     +"\ncompile_id:         "+compile_id
                     //+"\ncode: "+code
                     +"\nfilename:           "+filename
                     +"\nfilepath:           "+filepath
                     +"\nfiletype:           "+filetype
                     +"\nsloc:               "+sloc
                     +"\nprev_sourcefile_id: "+prev_sourcefile_id
                     +"\nlines_added:        "+lines_added
                     +"\nlines_deleted:      "+lines_deleted;
    
        return tmp;
    }
    
    public String getToolTipText(){
        String tmp = "<html><body>"
                     +"id:                 "+id
                     +"<br>compile_id:         "+compile_id
                     //+"<br>code: "+code
                     +"<br>filename:           "+filename
                     +"<br>filepath:           "+filepath
                     +"<br>filetype:           "+filetype
                     +"<br>sloc:               "+sloc
                     +"<br>prev_sourcefile_id: "+prev_sourcefile_id
                     +"<br>lines_added:        "+lines_added
                     +"<br>lines_deleted:      "+lines_deleted
                     +"</body></html>";
    
        return tmp;
    }

    public int getId() {
        return id;
    }

    public int getCompileId() {
        return compile_id;
    }
    
    /**
     * Returns the sourcecode of this sourcefile as a String. This method will read the sourcecode from the database, because
     * this class does not store sourcecodes. The reason for this decision is "memory consumption".
     * @return the corresponding sourcecode
     **/
    /*
    public String getCode() {
        return AssignmentUpdater.getSourcecode(id);
    }
    */

    public String getFilename() {
        return filename;
    }

    public String getFilepath() {
        return filepath;
    }

    public String getFiletype() {
        return filetype;
    }

    public int getSloc() {
        return sloc;
    }

    public int getPrev_sourcefile_id() {
        return prev_sourcefile_id;
    }

    public int getLines_added() {
        return lines_added;
    }

    public int getLines_deleted() {
        return lines_deleted;
    }
    
}
