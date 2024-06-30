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
    //private static Map<Integer, Employee> EmpCache = new HashMap<>();

    private EmployeeDao() {
    }

    public static synchronized EmployeeDao getInstance() {
        if (single_instance == null) {
            single_instance = new EmployeeDao();
        }
        return single_instance;
    }

    public List<Employee> getAllEmp() {
        //this function insert all the employees from the database to the system
        List<Employee> employees = new ArrayList<>();
        try {
            conn = DataSource.openConnection();
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM Employee");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String fname = rs.getString("fname");
                String lname = rs.getString("lname");
                String id = rs.getString("id");
                int salary = rs.getInt("salary");
                LocalDate startdate = rs.getDate("Date").toLocalDate();
                String jobType = rs.getString("jobType");
                String daysoff = rs.getString("days_off");
                Role role = Role.valueOf(rs.getString("role"));
                int superMarketId = rs.getInt("super_market_id");

                SuperMarket superMarket = SuperMarketDao.getInstance().getSuperMarket(superMarketId);
                terms t = new terms(startdate, jobType, daysoff);
                Employee employee = new Employee(id, fname, lname, salary, t, superMarket);
                employee.setRole(role);

                EmployeeController.addEmployeeToSystem(employee);
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataSource.closeConnection();
        }
        return employees;
    }

    public Employee getEmployee(int id) {
        //check if emp is already in system
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

                SuperMarket superMarket = SuperMarketDao.getInstance().getSuperMarket(superMarketId);
                terms t = new terms(startdate, jobType, daysoff);
                emp = new Employee(id_new, fname, lname, salary, t, superMarket);
                emp.setRole(role);

                EmployeeController.addEmployeeToSystem(emp);
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
            String sql = "INSERT INTO Employee (id, fname, lname, salary, Date, jobType, days_off, super_market_id, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, employee.getId());
            statement.setString(2, employee.getFname());
            statement.setString(3, employee.getLname());
            statement.setInt(4, employee.getSalary());
            statement.setDate(5, java.sql.Date.valueOf(employee.getTerms().getStartdate()));
            statement.setString(6, employee.getTerms().getJobType());
            statement.setString(7, employee.getTerms().getDaysoff());
            statement.setInt(8, employee.getSuperMarketBranch().getId());
            statement.setString(9, employee.getRole().name());
            statement.executeUpdate();

            EmployeeController.addEmployeeToSystem(employee);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataSource.closeConnection();
        }
    }
}
