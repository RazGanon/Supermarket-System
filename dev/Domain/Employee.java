package Domain;
import java.util.Date;

public class Employee {
    private String fName; //first name of employee
    private String lName; //last  name of employee
    private String id;
    private int salary;
    private terms terms; // Terms of employment for the employee
    private SuperMarket SuperMarketBranch; //the super market branch where the emploee works
    private Role role ;
    private String password;

    // constructors / standard setters / getters
    public Employee (String empfName, String emplName, String empId, int empSalary, terms empTerms, SuperMarket empSuperMarketBranch) {
        fName=empfName;
        lName=emplName;
        id=empId;
        salary=empSalary;
        terms=empTerms;
        SuperMarketBranch=empSuperMarketBranch;
        role= Role.Employee; // by default

    }
    public void setFname (String ifName){
        fName=ifName;
    }
    public void setLname (String ilName){
        fName=ilName;
    }

    public void setSalary (int iSalary){
        salary=iSalary;
    }
    public String getId () {return id;}
    public String getFname () {return fName;}
    public String getLname () {return lName;}
    public terms getTerms () {return terms;}
    public SuperMarket getSuperMarketBranch () {return SuperMarketBranch;}

    public int getSalary () {return salary;}
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return this.password;
    }
}



