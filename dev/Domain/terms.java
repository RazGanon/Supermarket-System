package Domain;

import java.util.Date;

//Terms of employment for the employee
public class terms {
    private  Date startDate;//start date at the company
    private String JobType;
    private String Wage;
    private String daysOff; //number of days off that left for this employee


    public terms (Date startDate, String JobType, String Wage, String daysOff) {
        this.daysOff=daysOff;
        this.JobType=JobType;
        this.Wage=Wage;
        this.startDate=startDate;

    }
}
