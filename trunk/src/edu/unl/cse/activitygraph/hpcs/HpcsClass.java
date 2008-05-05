
package edu.unl.cse.activitygraph.hpcs;

public class HpcsClass implements ToolTip{
    
    private int id;
    private String name;
    private String semester;
    private String university;
    private int project_number;
    private String url;
    private String group_name;
    private int professor;
    private String details;
    private boolean endofstudy;
    private boolean background;
    private boolean over;
    private int students_total;
    private int students_consented;
    
    private String professorName;
    
    public HpcsClass(int id, String name, String semester, String university,
            int project_number, String url, String group_name, int professor, String details, boolean endofstudy, boolean background,
            boolean over, int students_total, int students_consented ) {
        
        this.id                 = id;
        this.name               = name;
        this.semester           = semester;
        this.university         = university;
        this.project_number     = project_number;
        this.url                = url;
        this.group_name         = group_name;
        this.professor          = professor;
        this.details            = details;
        this.endofstudy         = endofstudy;
        this.background         = background;
        this.over               = over;
        this.students_total     = students_total;
        this.students_consented = students_consented;
    }
    
    public boolean equals(Object o){
        if (o instanceof HpcsClass){
            HpcsClass tmp = (HpcsClass)o;
            
            if (    this.id==tmp.id &&
                    this.name.equals(tmp.name) &&
                    this.semester.equals(tmp.semester) &&
                    this.university.equals(tmp.university) &&
                    this.project_number == tmp.project_number &&
                    this.url.equals(tmp.url) &&
                    this.group_name.equals(tmp.group_name) &&
                    this.professor == tmp.professor &&
                    this.details.equals(tmp.details) &&
                    this.endofstudy == tmp.endofstudy &&
                    this.background == tmp.background &&
                    this.over == tmp.over &&
                    this.students_total == tmp.students_total &&
                    this.students_consented == tmp.students_consented
                    ){
                return true;
            }else
                return false;
        }else
            return false;
    }
    
    public String toString(){
        String tmp = "id:                    "+id
                     +"\nname:               "+name
                     +"\nsemester:           "+semester
                     +"\nuniversity:         "+university
                     +"\nproject_number:     "+project_number
                     +"\nurl:                "+url
                     +"\ngroup_name:         "+group_name
                     +"\nprofessor:          "+professor
                     +"\nprofessorName:      "+professorName
                     +"\ndetails:            "+details
                     +"\nendofstudy:         "+endofstudy
                     +"\nbackground:         "+background
                     +"\nover:               "+over
                     +"\nstudents_total:     "+students_total
                     +"\nstudents_consented: "+students_consented;
        return tmp;
    }
    
    public String getToolTipText(){
        String tmp = "<html><body>"
                     +"id:                 "+id
                     +"<br>name:               "+name
                     +"<br>semester:           "+semester
                     +"<br>university:         "+university
                     +"<br>project_number:     "+project_number
                     +"<br>url:                "+url
                     +"<br>group_name:         "+group_name
                     +"<br>professor:          "+professor
                     +"<br>professorName:      "+professorName
                     +"<br>details:            "+details
                     +"<br>endofstudy:         "+endofstudy
                     +"<br>background:         "+background
                     +"<br>over:               "+over
                     +"<br>students_total:     "+students_total
                     +"<br>students_consented: "+students_consented
                     +"</body></html>";
        return tmp;
    }
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getSemester() {
        return semester;
    }
    
    public String getUniversity() {
        return university;
    }
    
    public int getProject_number() {
        return project_number;
    }
    
    public String getUrl() {
        return url;
    }
    
    public String getGroup_name() {
        return group_name;
    }
    
    public int getProfessor() {
        return professor;
    }
    
    public String getDetails() {
        return details;
    }
    
    public boolean isEndofstudy() {
        return endofstudy;
    }
    
    public boolean isBackground() {
        return background;
    }
    
    public boolean isOver() {
        return over;
    }
    
    public int getStudents_total() {
        return students_total;
    }
    
    public int getStudents_consented() {
        return students_consented;
    }

    public String getProfessorName() {
        return professorName;
    }

    public void setProfessorName(String professorName) {
        this.professorName = professorName;
    }
    
}
