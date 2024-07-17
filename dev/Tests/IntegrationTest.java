package Tests;
import Data.DataSource;
import Domain.Driver;
import Presentation.Main;
import Presentation.MainController;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import DomainEmployees.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class IntegrationTest {
    private Main main;
    private Gson gson;
    private MainController  mainController;
    private EmployeeController employeeController;

    @Before
    public void setUp() throws Exception {
        this.main = new Main();
        gson = new Gson();
        this.mainController = MainController.getInstance();
        this.employeeController = new EmployeeController();
        employeeController.addAllTAbleEmp();


    }
    @Test
    public void addDriverTest(){

        String input = "C\nShoshi\ncohen\n1122\ntrue\n12200\n1220012\n1\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        main.addDriver(new Scanner(System.in));
        Driver driver = mainController.getDriverById(1122);
        List<Employee> employees = employeeController.getAllEmployees();
        Employee employee = EmployeeController.getEmployeeById("1122");
        assertNotNull(driver);
        assertEquals("Shoshi cohen", driver.getDriverName());
        assertTrue(out.toString().contains("Driver added successfully."));
        assertTrue(employees.contains(employee));

    }
    @Test
    public void removeDriverTest(){
        String input = "1122\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Employee employee = EmployeeController.getEmployeeById("1122");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        main.removeDriver(new Scanner(System.in));
        List<Employee> employees = employeeController.getAllEmployees();
        assertTrue(out.toString().contains("Employee with ID: " + "1122" + " has been deleted successfully."));
        assertFalse(employees.contains(employee));
    }
    @Test
    public void addDriverExists(){
        employeeController.addAllTAbleEmp();
        String input = "C\nArie\nRon\n234234\ntrue\n12200\narierrrr\n1\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        main.addDriver(new Scanner(System.in));
        assertTrue(out.toString().contains("Employee with ID: " + "234234" + " already exists."));
    }


}
