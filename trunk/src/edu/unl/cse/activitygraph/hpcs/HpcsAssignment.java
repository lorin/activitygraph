package edu.unl.cse.activitygraph.hpcs;

import java.sql.Timestamp;
import java.util.Vector;

public class HpcsAssignment implements ToolTip{
    
    private int id;
    private int problem_type_id;
    private int class_id;
    private int assigned_number;
    private boolean serial_code_provided;
    private String comments;
    private String validation_input_file;
    private String validation_output_file;
    private String validation_parameters;
    private Timestamp due_date;
    private Timestamp submission_date;
    private Timestamp extension_date;
    private int grade_max;
    private boolean class_assignment;
    
    private Vector<Approach> approaches;
    
    public HpcsAssignment(int id, int problem_type_id, int class_id, int assigned_number, boolean serial_code_provided, String comments,
            String validation_input_file, String validation_output_file, String validation_parameters, Timestamp due_date,
            Timestamp submission_date, Timestamp extension_date, int grade_max, boolean class_assignment) {

        this.id                     = id;
        this.problem_type_id        = problem_type_id;
        this.class_id               = class_id;
        this.assigned_number        = assigned_number;
        this.serial_code_provided   = serial_code_provided;
        this.comments               = comments;
        this.validation_input_file  = validation_input_file;
        this.validation_output_file = validation_output_file;
        this.validation_parameters  = validation_parameters;
        this.due_date               = due_date;
        this.submission_date        = submission_date;
        this.extension_date         = extension_date;
        this.grade_max              = grade_max;
        this.class_assignment       = class_assignment;
    }
    
    public boolean equals(Object o){
        if (o instanceof HpcsAssignment){
            HpcsAssignment tmp = (HpcsAssignment)o;
            
            if (    this.id == tmp.id &&
                    this.problem_type_id == tmp.problem_type_id &&
                    this.class_id == tmp.class_id &&
                    this.assigned_number == tmp.assigned_number &&
                    this.serial_code_provided == tmp.serial_code_provided &&
                    this.comments.equals(tmp.comments) &&
                    this.validation_input_file.equals(tmp.validation_input_file) &&
                    this.validation_output_file.equals(tmp.validation_output_file) &&
                    this.validation_parameters.equals(tmp.validation_parameters) &&
                    this.due_date.equals(tmp.due_date) &&
                    this.submission_date.equals(tmp.submission_date) &&
                    this.extension_date.equals(tmp.extension_date) &&
                    this.grade_max == tmp.grade_max &&
                    this.class_assignment == tmp.class_assignment
                    ){
                return true;
            }else
                return false;
        }else
            return false;
    }
    
    public String toString(){
        String due_date        = null;
        String submission_date = null;
        String extension_date  = null;
        
        if (this.due_date != null)
            due_date = this.due_date.toString();
        if (this.submission_date != null)
            submission_date = this.submission_date.toString();
        if (this.extension_date != null)
            extension_date = this.extension_date.toString();   
            
        
        String tmp = "id:                        "+id
                     +"\nproblem_type_id:        "+problem_type_id
                     +"\nclass_id:               "+class_id
                     +"\nassigned_number:        "+assigned_number
                     +"\nserial_code_provided:   "+serial_code_provided
                     +"\ncomments:               "+comments
                     +"\nvalidation_input_file:  "+validation_input_file
                     +"\nvalidation_output_file: "+validation_output_file
                     +"\nvalidation_parameters:  "+validation_parameters
                     +"\ndue_date:               "+due_date
                     +"\nsubmission_date:        "+submission_date
                     +"\nextension_date:         "+extension_date
                     +"\ngrade_max:              "+grade_max
                     +"\nclass_assignment:       "+class_assignment
                     +"</body></html>"   ;
        return tmp;
    }
    
    public String getToolTipText(){
        String due_date        = null;
        String submission_date = null;
        String extension_date  = null;
        
        if (this.due_date != null)
            due_date = this.due_date.toString();
        if (this.submission_date != null)
            submission_date = this.submission_date.toString();
        if (this.extension_date != null)
            extension_date = this.extension_date.toString();   
            
        
        String tmp = "<html><body>"
                     +"id:                     "+id
                     +"<br>problem_type_id:        "+problem_type_id
                     +"<br>class_id:               "+class_id
                     +"<br>assigned_number:        "+assigned_number
                     +"<br>serial_code_provided:   "+serial_code_provided
                     +"<br>comments:               "+comments
                     +"<br>validation_input_file:  "+validation_input_file
                     +"<br>validation_output_file: "+validation_output_file
                     +"<br>validation_parameters:  "+validation_parameters
                     +"<br>due_date:               "+due_date
                     +"<br>submission_date:        "+submission_date
                     +"<br>extension_date:         "+extension_date
                     +"<br>grade_max:              "+grade_max
                     +"<br>class_assignment:       "+class_assignment
                     +"</body></html>"   ;
        return tmp;
    }

    public int getId() {
        return id;
    }

    public int getProblem_type_id() {
        return problem_type_id;
    }

    public int getClass_id() {
        return class_id;
    }

    public int getAssigned_number() {
        return assigned_number;
    }

    public boolean isSerial_code_provided() {
        return serial_code_provided;
    }

    public String getComments() {
        return comments;
    }

    public String getValidation_input_file() {
        return validation_input_file;
    }

    public String getValidation_output_file() {
        return validation_output_file;
    }

    public String getValidation_parameters() {
        return validation_parameters;
    }

    public Timestamp getDue_date() {
        return due_date;
    }

    public Timestamp getSubmission_date() {
        return submission_date;
    }

    public Timestamp getExtension_date() {
        return extension_date;
    }

    public int getGrade_max() {
        return grade_max;
    }

    public boolean isClass_assignment() {
        return class_assignment;
    }

    public void setApproaches(Vector<Approach> approaches) {
        this.approaches = approaches;
    }
    
    public Vector<Approach> getApproaches() {
        return this.approaches;
    }
    
}
