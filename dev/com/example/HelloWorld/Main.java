package com.example.HelloWorld;
import java.time.LocalDate;
import java.util.Date;
import java.util.Scanner;

import Domain.*;
import Presentation.ConstraintsController;

public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        //System.out.printf("Hello World!");

//        ConstraintsController m = new ConstraintsController("123");
//        int [][]ma =m.getMatrix_constraint();
//        m.printMatrix(m.getMatrix_constraint());
//        m.getConstraintFromUser();
//        m.printMatrix(m.getMatrix_constraint());
        EmployeeController emp = new EmployeeController();
        Date s= new Date();
        SuperMarket news = emp.addsupermarket("tel-aviv","mor");
        String jobtype= "manager";
        String wage = " full ";
        String daysoff = "13";
        terms t = emp.setTerms(s,jobtype,wage,daysoff);
        emp.registerEmployee("mor","barzilay","313322380",10000,t ,news );

        Date s1= new Date();
        SuperMarket news1 = emp.addsupermarket("tel-aviv","mor");
        String jobtype1= "worker";
        String wage1 = " full ";
        String daysoff1 = "13";
        terms t1 = emp.setTerms(s1,jobtype1,wage1,daysoff1);
        emp.registerEmployee("niv","sampson","208398511",5000,t1 ,news1 );
        emp.printListEmpl();

        WorkArangment workArrangement = new WorkArangment();
        DaySchedule mondaySchedule = workArrangement.getDaySchedule("Monday");
        DaySchedule TuesdaySchedule = workArrangement.getDaySchedule("Tuesday");

        workArrangement.addDaySchedule("Monday", mondaySchedule);
        workArrangement.addDaySchedule("Tuesday", TuesdaySchedule);

// Access or iterate through schedules
        workArrangement.iterateThroughSchedules();

    }






}