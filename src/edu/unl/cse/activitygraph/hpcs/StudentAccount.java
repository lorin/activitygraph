
package edu.unl.cse.activitygraph.hpcs;

public class StudentAccount implements ToolTip{
    
    private int id;
    private int class_id;
    private String email;
    private String firstname;
    private String lastname;
    private String hackystat_key;
    private String pin;
    private int role_id;
    private String accountname;
    private String timezone;
    private String anonym;
    
    public StudentAccount(int id, int class_id, String email, String firstname, String lastname, String hackystat_key,
            String pin, int role_id, String accountname, String timezone, String anonym) {
        
        this.id            = id;
        this.class_id      = class_id;
        this.email         = email;
        this.firstname     = firstname;
        this.lastname      = lastname;
        this.hackystat_key = hackystat_key;
        this.pin           = pin;
        this.role_id       = role_id;
        this.accountname   = accountname;
        this.timezone      = timezone;
        this.anonym        = anonym;
        
    }
    
    public boolean equals(Object o){
        if (o instanceof StudentAccount){
            StudentAccount tmp = (StudentAccount)o;
            
            if (    this.id == tmp.id &&
                    this.class_id == tmp.class_id &&
                    this.email.equals(tmp.email) &&
                    this.firstname.equals(tmp.firstname) &&
                    this.lastname.equals(tmp.lastname) &&
                    this.hackystat_key.equals(tmp.hackystat_key) &&
                    this.pin.equals(tmp.pin) &&
                    this.role_id == tmp.role_id &&
                    this.accountname.equals(tmp.accountname) &&
                    this.timezone.equals(tmp.timezone) &&
                    this.anonym.equals(tmp.anonym)
                    ){
                return true;
            }else
                return false;
        }else
            return false;
    }
    
    public String toString(){
        String tmp = "id:              "+id
                    +"\nclass_id:      "+class_id
                    +"\nemail:         "+email
                    +"\nfirstname:     "+firstname
                    +"\nlastname:      "+lastname
                    +"\nhackystat_key: "+hackystat_key
                    +"\npin:           "+pin
                    +"\nrole_id:       "+role_id
                    +"\naccountname:   "+accountname
                    +"\ntimezone:      "+timezone
                    +"\nanonym:        "+anonym;
        return tmp;
    }
    
    public String getToolTipText(){
        String tmp = "<html><body>"
                    +"id:            "+id
                    +"<br>class_id:      "+class_id
                    +"<br>email:         "+email
                    +"<br>firstname:     "+firstname
                    +"<br>lastname:      "+lastname
                    +"<br>hackystat_key: "+hackystat_key
                    +"<br>pin:           "+pin
                    +"<br>role_id:       "+role_id
                    +"<br>accountname:   "+accountname
                    +"<br>timezone:      "+timezone
                    +"<br>anonym:        "+anonym
                    +"</body></html>";
        return tmp;
    }
    
    public int getId() {
        return id;
    }
    
    public int getClass_id() {
        return class_id;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getFirstname() {
        return firstname;
    }
    
    public String getLastname() {
        return lastname;
    }
    
    public String getHackystat_key() {
        return hackystat_key;
    }
    
    public String getPin() {
        return pin;
    }
    
    public int getRole_id() {
        return role_id;
    }
    
    public String getAccountname() {
        return accountname;
    }
    
    public String getTimezone() {
        return timezone;
    }
    
    public String getAnonym() {
        return this.anonym;
    }
    
}
