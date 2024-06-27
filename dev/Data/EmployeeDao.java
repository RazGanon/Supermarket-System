package Data;

import Domain.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeDao {
    private Connection conn;
    private static EmployeeDao single_instance = null;
    private static Map<Integer, Employee> EmpCache = new HashMap<>();

    private EmployeeDao() {
    }

    public static synchronized EmployeeDao getInstance() {
        if (single_instance == null) {
            single_instance = new EmployeeDao();
        }
        return single_instance;
    }

    public List<Employee> getAllEmp() {
        List<Employee> employees = new ArrayList<>();
        try {
            conn = DataSource.openConnection();
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM Employee");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String fname = rs.getString("fname");
                String lname = rs.getString("lname"); // Fixed lname to fetch correctly
                String id = rs.getString("id");
                int salary = rs.getInt("salary");
                LocalDate startdate = rs.getDate("Date").toLocalDate();
                String jobType = rs.getString("jobType");
                String daysoff = rs.getString("days_off");
                Role role = Role.valueOf(rs.getString("role"));
                int superMarketId = rs.getInt("super_market_id");

                // Use SuperMarketDao to get the SuperMarket by ID
                SuperMarket superMarket = SuperMarketDao.getInstance().getSuperMarket(superMarketId);
                //set the terms
                terms t = new terms(startdate, jobType, daysoff);
                Employee employee = new Employee(id, fname, lname, salary, t, superMarket);
                employee.setRole(role);
                //add employee to the system for cache
                EmployeeController.addEmployeeToSystem(employee);

                //EmpCache.put(Integer.valueOf(id), employee); // Cache the employee
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataSource.closeConnection();
        }
        return employees;
    }

    public Employee getEmployee(int id) {
        Employee emp = EmployeeController.getEmployeeById(String.valueOf(id));
        if (emp != null) {
            return emp;
        }
        try {
            conn = DataSource.openConnection();
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM Employee WHERE id = ?");
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String fname = rs.getString("fname");
                String lname = rs.getString("lname");
                String id_new = rs.getString("id");
                int salary = rs.getInt("salary");
                LocalDate startdate = rs.getDate("Date").toLocalDate();
                String jobType = rs.getString("jobType");
                String daysoff = rs.getString("days_off");
                Role role = Role.valueOf(rs.getString("role"));
                int superMarketId = rs.getInt("super_market_id");

                // Use SuperMarketDao to get the SuperMarket by ID
                SuperMarket superMarket = SuperMarketDao.getInstance().getSuperMarket(superMarketId);
                terms t = new terms(startdate, jobType, daysoff);
                emp = new Employee(id_new, fname, lname, salary, t, superMarket);
                emp.setRole(role);

                //EmpCache.put(Integer.valueOf(id), employee); // Cache the employee
                EmployeeController.addEmployeeToSystem(emp);

                //EmpCache.put(id, emp); // Cache the employee
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataSource.closeConnection();
        }
        return emp;
    }

    public void registerEmployee(Employee employee) {
        try {
            conn = DataSource.openConnection();
            String sql = "INSERT INTO Employee (fname, lname, salary, Date, jobType, days_off, super_market_id, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, employee.getFname());
            statement.setString(2, employee.getLname());
            statement.setInt(3, employee.getSalary());
            statement.setDate(4, java.sql.Date.valueOf(employee.getTerms().getStartdate()));
            statement.setString(5, employee.getTerms().getJobType());
            statement.setString(6, employee.getTerms().getDaysoff());
            statement.setInt(7, employee.getSuperMarketBranch().getId());
            statement.setString(8, employee.getRole().name());
            statement.executeUpdate();

            // Cache the new employee
            //EmpCache.put(Integer.valueOf(id), employee); // Cache the employee
            EmployeeController.addEmployeeToSystem(employee);

            //EmpCache.put(Integer.valueOf(employee.getId()), employee);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataSource.closeConnection();
        }
    }
}
