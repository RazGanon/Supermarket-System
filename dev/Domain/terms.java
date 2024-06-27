package Domain;

import java.time.LocalDate;
import java.util.Date;

//Terms of employment for the employee
public class terms {
    private  LocalDate startDate;//start date at the company
    private String JobType;
    private String Wage;
    private String daysOff; //number of days off that left for this employee

    //constructor
    public terms (LocalDate startDate, String JobType,  String daysOff) {
        this.daysOff=daysOff;
        this.JobType=JobType;
        this.startDate=startDate;

    }

    public LocalDate getStartdate() {
        return this.startDate;
    }

    public String getJobType() {
        return this.JobType;
    }

    public String getDaysoff() {
        return this.daysOff;
    }
}
