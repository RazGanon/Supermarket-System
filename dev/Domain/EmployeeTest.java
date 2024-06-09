package Domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Domain.Employee;
import Domain.terms;
import Domain.SuperMarket;

import java.time.LocalDate;

public class EmployeeTest {

    private Employee employee;
    private SuperMarket superMarket;
    private terms employeeTerms;

    @BeforeEach
    void setUp() {
        superMarket = new SuperMarket("TestSuperMarket","John");
        employeeTerms = new terms(LocalDate.now(),"Full-Time","0");
        employee = new Employee("John", "Doe", "123", 50000, employeeTerms, superMarket);
    }

    @Test
    void testGetFirstName() {
        assertEquals("John", employee.getFname());
    }
    @Test
    void testSetFirstName() {
        employee.setFname("Jane");
        assertEquals("Jane", employee.getFname());
    }

    @Test
    void testGetLastName() {
        assertEquals("Doe", employee.getLname());
    }

    @Test
    void testSetLastName() {
        employee.setLname("Smith");
        assertEquals("Smith", employee.getLname());
    }

    @Test
    void testGetId() {
        assertEquals("123", employee.getId());
    }

    @Test
    void testSetId() {
        employee.setId("124");
        assertEquals("124", employee.getId());
    }

    @Test
    void testGetSalary() {
        assertEquals(50000, employee.getSalary());
    }

    @Test
    void testSetSalary() {
        employee.setSalary(60000);
        assertEquals(60000, employee.getSalary());
    }

    @Test
    void testGetTerms() {
        assertEquals(employeeTerms, employee.getTerms());
    }

    @Test
    void testSetTerms() {
        terms newTerms = new terms(LocalDate.now(),"Part-Time","10");
        employee.setTerms(newTerms);
        assertEquals(newTerms, employee.getTerms());
    }

    @Test
    void testGetSuperMarket() {
        assertEquals(superMarket, employee.getSuperMarketBranch());
    }

    @Test
    void testSetSuperMarket() {
        SuperMarket newSuperMarket = new SuperMarket("NewSuperMarket","mor");
        employee.setSuperMarket(newSuperMarket);
        assertEquals(newSuperMarket, employee.getSuperMarketBranch());

    }
}