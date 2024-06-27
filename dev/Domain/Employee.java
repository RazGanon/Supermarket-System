package Domain;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Employee {
    private String fName; //first name of employee
    private String lName; //last  name of employee
    private String id;
    private int salary;
    private terms terms; // Terms of employment for the employee
    private SuperMarket SuperMarketBranch; //the super market branch where the emploee works
    private Role role ;
    private String password;
    private Constraints empcons;
    private List<Schedule> pastSchedules;
    private StringBuilder MySchedule;

    // constructors / standard setters / getters
    public Employee (String empfName, String emplName, String empId, int empSalary, terms empTerms, SuperMarket empSuperMarketBranch) {
        fName=empfName;
        lName=emplName;
        id=empId;
        salary=empSalary;
        terms=empTerms;
        SuperMarketBranch=empSuperMarketBranch;
        role= Role.Employee; // by default
        //this.pastSchedules = new ArrayList<>();


    }

    public void setFname (String ifName){
        fName=ifName;
    }
    public void setLname (String ilName){
        lName=ilName;
    }

    public void setSalary (int iSalary){
        salary=iSalary;
    }
    public String getId () {return id;}
    public void setId (String id) {this.id =  id;}

    public String getFname () {return fName;}
    public String getLname () {return lName;}
    public terms getTerms () {return terms;}
    public void setTerms (terms t ){this.terms = t;}
    public SuperMarket getSuperMarketBranch () {return SuperMarketBranch;}

    public int getSalary () {return salary;}
    public Role getRole() {
        return role;
    }
    public Constraints getConstraint() {
        return this.empcons;
    }

    public void setSuperMarket(SuperMarket s) {
    this.SuperMarketBranch =s;


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
    public List<Schedule> getPastSchedules() {
        return pastSchedules;
    }
    public void addSchedule(Schedule schedule) {
        this.pastSchedules.add(schedule);
    }
}



