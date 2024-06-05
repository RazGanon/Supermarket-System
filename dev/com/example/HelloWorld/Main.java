//package com.example.HelloWorld;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//
//import Domain.*;
//import Service.EmployeeService;
//import Service.ScheduleService;
//import Service.WorkArrangementService;
//
//public class Main {
//    public static void main(String[] args) {
//        // Create an instance of EmployeeController
//        EmployeeService emp = new EmployeeService();
//
//        // Add a supermarket and register employees
//        SuperMarket news = emp.addsupermarket("tel-aviv", "mor");
//        LocalDate startDate = LocalDate.now();
//        String jobType = "manager";
//        String wage = "full";
//        String daysOff = "13";
//        terms t = emp.setTerms(startDate, jobType, wage, daysOff);
//        emp.registerEmployee("mor", "barzilay", "313322380", 10000, t, news);
//
//        SuperMarket news1 = emp.addsupermarket("tel-aviv", "mor");
//        String jobType1 = "worker";
//        String wage1 = "full";
//        String daysOff1 = "13";
//        terms t1 = emp.setTerms(startDate, jobType1, wage1, daysOff1);
//        emp.registerEmployee("niv", "sampson", "208398511", 5000, t1, news1);
//
//        // Print the list of employees
//        emp.printListEmpl();
//
//        // Create an instance of WorkArrangement
//        WorkArangment workArrangement = new WorkArangment();
//
//        // Initialize Schedule
//        LocalTime morningStart = LocalTime.of(8, 0);
//        LocalTime morningEnd = LocalTime.of(12, 0);
//        LocalTime eveningStart = LocalTime.of(13, 0);
//        LocalTime eveningEnd = LocalTime.of(17, 0);
//
//        ScheduleService schedule = new ScheduleService("tel-aviv", startDate, morningStart, morningEnd, eveningStart, eveningEnd, news);
//
//        // Add shifts to work arrangement
//        WorkArrangementService.addDaySchedule(startDate, ShiftType.MORNING, new Shift(news, ShiftType.MORNING, DayShift.MONDAY));
//        WorkArrangementService.addDaySchedule(startDate.plusDays(1), ShiftType.MORNING, new Shift(news, ShiftType.MORNING, DayShift.TUESDAY));
//
//        // Add employee availability
//        schedule.addEmpAvailability("313322380", startDate, ShiftType.MORNING);
//        schedule.addEmpAvailability("208398511", startDate.plusDays(1), ShiftType.MORNING);
//
//        // Get and print day schedules
//        Shift mondayMorningShift = WorkArrangementService.getDaySchedule(startDate, ShiftType.MORNING);
//        Shift tuesdayMorningShift = WorkArrangementService.getDaySchedule(startDate.plusDays(1), ShiftType.MORNING);
//
//        // Print the retrieved shifts
//        System.out.println("Monday Morning Shift: " + mondayMorningShift);
//        System.out.println("Tuesday Morning Shift: " + tuesdayMorningShift);
//
//        // Print the entire schedule
//        schedule.printSchedule();
//    }
//}
