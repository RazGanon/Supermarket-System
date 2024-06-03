package Domain;
<<<<<<< HEAD

public class Employee {
}
=======
import java.util.Date;

public class Employee {
    private String fName; //first name of employee
    private String lName; //last  name of employee
    private String id;
    private int salary;
    private terms terms; // Terms of employment for the employee
    private SuperMarket SuperMarketBranch; //the super market branch where the emploee works

    // constructors / standard setters / getters
    public Employee (String empfName, String emplName, String empId, int empSalary, terms empTerms, SuperMarket empSuperMarketBranch) {
        fName=empfName;
        lName=emplName;
        id=empId;
        salary=empSalary;
        terms=empTerms;
        SuperMarketBranch=empSuperMarketBranch;
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
    public int getSalary () {return salary;}


}



>>>>>>> origin/208398511_313322380
