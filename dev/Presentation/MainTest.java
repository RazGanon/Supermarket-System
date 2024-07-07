package Presentation;

import static org.junit.jupiter.api.Assertions.*;

import Data.DataSource;
import Domain.*;
import Presentation.Main;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.util.Scanner;

public class MainTest {
    private static EmployeeController employeeController;
    private static ScheduleController scheduleController;
    private static ConstraintsController constraintsController;
    private Main main;
    private final InputStream originalSystemIn = System.in;
    private final PrintStream originalSystemOut = System.out;
    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;

    @BeforeAll
    public static void setUpClass() {
        employeeController = new EmployeeController();
        constraintsController = new ConstraintsController(null);
        scheduleController = new ScheduleController(employeeController, constraintsController);
    }

    @BeforeEach
    public void setUp() {
        main = new Main(employeeController, scheduleController, constraintsController);
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
        System.setIn(originalSystemIn); // Reset System.in to its original state
    }

    private void provideInput(String data) {
        testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }

    @Test
    public void testMainWithFullSequence() {
//        // Simulate input for the entire sequence of interactions
        String input = "yes\n" +   // Clean data before running
                "1234\n" +  // Enter username
                "1234\n" + // Enter password
                "4\n" + //check if employee already have relevant constraints (manager not allow submission so cant be )
                 "1235\n" +  //employee is not in system so return "Enter employee ID (or type 'exit' to return to the menu):"
                 "exit\n" + //exit
                 "9\n" + //register new employee
                  "shon\n" +   // emp first name
                        "platok\n" +   // emp last name
                        "1235\n" +   // emp id
                        "10000\n" +   // emp salary
                        "1\n" +   // chose full time job
                        "3\n" +   // Chose the third supermarket in the list
                        "1235\n" +  // Enter password
                        "4\n" + //check again if employee have relevant constraints print
                        "1235\n" +//- No relevant constraints found.
                        "14\n"  +//- check the week number -should return (The Week Number is :1)
                        "7\n " + //allow submission - print "Submissions have been allowed."
                        "14\n"  +//- check the week number -should return (The Week Number is :2)
                        "15\n" + //log out
                         "1235\n"+ //log in with new employee
                         "1235\n" + // password of new employee
                         "3\n" + // show me updated constraints - should return No constraints found for employee ID: 1235 for week: 1
                         "1\n" + // make new constraints for employee
                         "11\n" + //enter constraints : *6
                         "11\n" + //enter constraints : *6
                        "11\n" + //enter constraints : *6


                        "101\n"+ //enter constraints : *6 - should return Invalid input. Please enter a valid constraint (11, 10, 01, or 00).
                         "10\n"+ //enter constraints : *6
                         "10\n" + //enter constraints : *6
                         "11\n" + //enter constraints : *6
                         "3\n" + // show me updated constraints - should print Constraints for employee ID: 1235 for week: 1
                         "8\n" + // log out
                          "1234\n" +  // Enter username
                          "1234\n" + // Enter password
                          "4\n" + //check if employee already have relevant constraints (return his constraint)
                            "1235\n" + // Enter emp id

                            "5\n" + //generate schedule
                          "2\n" + //my rules  2 managers for sunday morning ...
                          "2\n" + //my rules  2 employees for sunday evening ...
                          "2\n" + //my rules  2 managers for sunday morning ...
                          "2\n" + //my rules  2 employees for sunday  morning ...
                "2\n" + //my rules  2 managers for monday morning ...
                "2\n" + //my rules  2 employees for monday evening ...
                "2\n" + //my rules  2 managers for monday morning ...
                "2\n" + //my rules  2 employees for monday  morning ...
                "2\n" + //my rules  2 managers for  morning ...
                "2\n" + //my rules  2 employees for  evening ...
                "2\n" + //my rules  2 managers for  morning ...
                "2\n" + //my rules  2 employees for   morning ...
                "2\n" + //my rules  2 managers for  morning ...
                "2\n" + //my rules  2 employees for  evening ...
                "2\n" + //my rules  2 managers for  morning ...
                "2\n" + //my rules  2 employees for   morning ...
                "2\n" + //my rules  2 managers for  morning ...
                "2\n" + //my rules  2 employees for  evening ...
                "2\n" + //my rules  2 managers for  morning ...
                "2\n" + //my rules  2 employees for   morning ...
                "2\n" + //my rules  2 managers for  morning ...
                "2\n" + //my rules  2 employees for  evening ...
                "2\n" + //my rules  2 managers for  morning ...
                "2\n" + //my rules  2 employees for   morning ...
                          "15\n" + //log out
                         "1235\n" +//log in with new employee
                         "1235\n" + // password of new employee
                         "2\n" + //show me current schedule -- print current schedule
                         "9\n" ; // exit from system- return "Exiting from system..."



        provideInput(input);

        // Act
        Main.main(new String[]{});

        // Assert
        String output = testOut.toString();
        System.out.println(output); // Print the output for debugging
        // Assertions
        assertTrue(output.contains("ConstraintsTable table has been cleaned."));
        assertTrue(output.contains("schedule table has been cleaned."));
        assertTrue(output.contains("schedule_shift_employee table has been cleaned."));
        assertTrue(output.contains("WeekFlag table has been cleaned."));
        assertTrue(output.contains("Employee table has been cleaned."));
        assertTrue(output.contains("SuperMarkets table has been cleaned."));
        assertTrue(output.contains("Manager employee has been added to the Employee table"));
        assertTrue(output.contains("Starting data has been restored."));
        assertTrue(output.contains("Enter username:"));
        assertTrue(output.contains("Enter password:"));
        assertTrue(output.contains("Enter employee ID (or type 'exit' to return to the menu):"));
        assertTrue(output.contains("Employee registered successfully."));
        assertTrue(output.contains("Employee with ID: 1235 does not exist."));
        assertTrue(output.contains("The Week Number is: 1"));
        assertTrue(output.contains("No constraints found for employee ID: 1235 and Week: 1"));
        assertTrue(output.contains("Submissions have been allowed."));
        assertTrue(output.contains("The Week Number is: 2"));
        assertTrue(output.contains("No constraints found for employee ID: 1235 for week: 1"));
        assertTrue(output.contains("Invalid input. Please enter a valid constraint (11, 10, 01, or 00)."));
        assertTrue(output.contains("Constraints added for employee ID: 1235"));
        assertTrue(output.contains("Shift requirements set successfully."));
        assertTrue(output.contains("Submissions have been stopped."));
        assertTrue(output.contains("Exiting from system..."));

        // Check if admin is in the system
        Employee employee = EmployeeController.getEmployeeById("1234");
        assertNotNull(employee);
        assertEquals("mor", employee.getFname());
        // Check if new employee is in the system
        Employee employee1 = EmployeeController.getEmployeeById("1235");
        assertNotNull(employee1);
        assertEquals("shon", employee1.getFname());
    }
    @Test
    public void testRegisterNewEmployeeAndLogout() {
        String input = "yes\n" +   // Clean data before running
                "1234\n" +  // Enter username
                "1234\n" + // Enter password
                "9\n" + //register new employee
                "john\n" +   // emp first name
                "platok\n" +   // emp last name
                "5678\n" +   // emp id
                "10000\n" +   // emp salary
                "1\n" +   // chose full time job
                "3\n" +   // Chose the third supermarket in the list
                "5678\n" +  // Enter password
                "16\n"; // exit from system

        provideInput(input);

        // Act
        Main.main(new String[]{});

        // Assert
        String output = testOut.toString();
        System.out.println(output); // Print the output for debugging
        assertTrue(output.contains("Employee registered successfully."));
        assertTrue(output.contains("Exiting from system..."));

        // Check if new employee is in the system
        Employee employee = EmployeeController.getEmployeeById("5678");
        assertNotNull(employee);
        assertEquals("john", employee.getFname());
    }
    @Test
    public void testCheckWeekNumber() {
        String input = "yes\n" +   // Clean data before running
                "1234\n" +  // Enter username
                "1234\n" + // Enter password
                "14\n" + // check the week number
                "16\n" ; // log out

        provideInput(input);

        // Act
        Main.main(new String[]{});

        // Assert
        String output = testOut.toString();
        System.out.println(output); // Print the output for debugging
        assertTrue(output.contains("The Week Number is: 1"));
        assertTrue(output.contains("Exiting from system..."));
    }
    @Test
    public void testGrowWeekNumber() {
        String input = "yes\n" +   // Clean data before running
                "1234\n" +  // Enter username
                "1234\n" + // Enter password
                "7\n" + // allow subimssion to week++
                "14\n" + // check the week number
                "16\n" ; // log out

        provideInput(input);

        // Act
        Main.main(new String[]{});

        // Assert
        String output = testOut.toString();
        System.out.println(output); // Print the output for debugging
        assertTrue(output.contains("The Week Number is: 2"));
        assertTrue(output.contains("Exiting from system..."));
    }

    @Test
    public void testRegisterEmployeeAndCheckConstraints() {
        String input = "yes\n" +   // Clean data before running
                "1234\n" +  // Enter username
                "1234\n" + // Enter password
                "9\n" + // register new employee
                "alex\n" +   // emp first name
                "smith\n" +   // emp last name
                "6789\n" +   // emp id
                "6000\n" +   // emp salary
                "2\n" +   // chose part time job
                "2\n" +   // Chose the second supermarket in the list
                "6789\n"+
                "15\n" + // log out
                "6789\n" + // log in with new employee
                "6789\n" + // password of new employee
                "3\n" + // show me updated constraints
                "9\n"; // exit from system

        provideInput(input);

        // Act
        Main.main(new String[]{});

        // Assert
        String output = testOut.toString();
        System.out.println(output); // Print the output for debugging
        assertTrue(output.contains("Employee registered successfully."));
        assertTrue(output.contains("No constraints found for employee ID: 6789 for week: 1"));
        assertTrue(output.contains("Exiting from system..."));

        // Check if new employee is in the system
        Employee employee = EmployeeController.getEmployeeById("6789");
        assertNotNull(employee);
        assertEquals("alex", employee.getFname());
    }
    @Test
    public void testInvalidConstraintInputWithNewUserFromPreviousTestWhenSubmissionNotAllowed() {
        String input = "no\n" +   // Clean data before running
                "6789\n" +  // Enter username
                "6789\n" + // Enter password
                "1\n" + // make new constraints for employee
                "9\n"; // exit from system

        provideInput(input);

        // Act
        Main.main(new String[]{});

        // Assert
        String output = testOut.toString();
        System.out.println(output); // Print the output for debugging
        assertTrue(output.contains("Submissions are currently not allowed."));

        assertTrue(output.contains("Exiting from system..."));
    }
    @Test

    public void testInvalidConstraintInputWithNewUserFromPreviousTest() {
        String input = "no\n" +   // Clean data before running
                "1234\n" +  // Enter username
                "1234\n" +  // Enter username
                "7\n" + // allow submission
                "15\n" + // admin log out

                "6789\n" +  // Enter username
                "6789\n" + // Enter password
                "1\n" + // make new constraints for employee
                "11\n" + // enter constraints: *6
                "101\n" + // enter constraints: *6 - should return Invalid input. Please enter a valid constraint (11, 10, 01, or 00).
                "10\n" + // enter constraints: *6
                "10\n" + // enter constraints: *6
                "10\n" + // enter constraints: *6
                "10\n" + // enter constraints: *6
                "10\n" + // enter constraints: *6
                "9\n"; // exit from system

        provideInput(input);

        // Act
        Main.main(new String[]{});

        // Assert
        String output = testOut.toString();
        System.out.println(output); // Print the output for debugging
        assertTrue(output.contains("Invalid input. Please enter a valid constraint (11, 10, 01, or 00)."));
        assertTrue(output.contains("Constraints added for employee ID: 6789"));
        assertTrue(output.contains("Exiting from system..."));
    }
    @Test

    public void testChangeEmpRole() {
        String input = "no\n" +   // Clean data before running
                "1234\n" +  // Enter username
                "1234\n" +  // Enter username
                "6\n" + // get employee role by id
                "6789\n" +  // Enter emp id
                "2\n" + // Change role Of emp
                "6789\n" +  // Enter emp id
                "3\n" + // make employee worker
                "16\n"; // exit from system

        provideInput(input);

        // Act
        Main.main(new String[]{});

        // Assert
        String output = testOut.toString();
        System.out.println(output); // Print the output for debugging
        assertTrue(output.contains("Employee"));
        assertTrue(output.contains("Role updated successfully in the database."));
        assertTrue(output.contains("Role updated successfully."));
        assertEquals(employeeController.getRoleEmployeeById("6789"),Role.worker);

        assertTrue(output.contains("Exiting from system..."));
    }

    @Test
    public void cleanDataTest() {
        // Simulate clean data and then fill it
        String input =
                "yes\n" +
                        "yes\n" +
                        "yes\n" +
                        "yes\n" +
                        "yes\n" +
                        "1122\n";

        provideInput(input);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        DataSource.cleanTables(); // clean all the tables except supermarkets table
        DataSource.cleanTable("SuperMarkets"); // clean supermarkets table

        assertTrue(out.toString().contains("SuperMarkets table has been cleaned."));

        DataSource.restoreSuperMarketStartingData();
        employeeController.addAllTAbleEmp();
        SuperMarketController.addAllSuperMarketsToSystem();
        assertTrue(out.toString().contains("Starting data has been restored."));

        // Check if employee is in the system
        Employee employee = EmployeeController.getEmployeeById("1234");
        assertNotNull(employee);
        assertEquals("mor", employee.getFname());
    }


    @Test
    public void testUserExperience() {
        // Simulate clean data and then fill it
        String input =
                "yes\n" +
                        "yes\n" +
                        "yes\n" +
                        "yes\n" +
                        "yes\n" +
                        "1122\n";

        provideInput(input);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        DataSource.cleanTables(); // clean all the tables except supermarkets table
        DataSource.cleanTable("SuperMarkets"); // clean supermarkets table
        DataSource.restoreSuperMarketStartingData();
        employeeController.addAllTAbleEmp();
        SuperMarketController.addAllSuperMarketsToSystem();
        assertTrue(out.toString().contains("Starting data has been restored."));


        // Simulate adding an employee
        String input1 = "shon\n" +   // emp first name
                "platok\n" +   // emp last name
                "1122\n" +   // emp id
                "10000\n" +   // emp salary
                "1\n" +   // chose full time job
                "3\n" +   // Chose the third supermarket in the list
                "1122\n";  // Enter password

        provideInput(input1);

        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));

        // Act
        main.registerEmployee(new Scanner(System.in));

        // Assert
        Employee employee = EmployeeController.getEmployeeById("1122");
        assertNotNull(employee);
        assertEquals("shon", employee.getFname());
    }
}
