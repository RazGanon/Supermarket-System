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
    //private static EmployeeDao single_instance = null;
    //private static Map<Integer, Employee> EmpCache = new HashMap<>();

    public EmployeeDao() {
    }

    //public static synchronized EmployeeDao getInstance() {
    //   if (single_instance == null) {
    //      single_instance = new EmployeeDao();
    //}
    // return single_instance;
    //}

    public List<Employee> getAllEmp() {

        //this function insert all the employees from the database to the system
        List<Employee> employees = new ArrayList<>();
        try {
            conn = DataSource.openConnection();
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM Employee");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String id = rs.getString("ID");
                String fname = rs.getString("Fname");
                String lname = rs.getString("lname");
                int salary = rs.getInt("salary");
                String jobType = rs.getString("Job");
                String superMarketAddress = rs.getString("Address");
                String ManagerName = rs.getString("Manager_Name");
                String startdate = rs.getString("hire_date");
                String daysoff = rs.getString("days_off");
                Role role = Role.valueOf(rs.getString("role"));
                String password = rs.getString("password");

                SuperMarket superMarket = SuperMarketDao.getInstance().getSuperMarketByAddress(superMarketAddress);
                terms t = new terms(startdate, jobType, daysoff);
                Employee employee = new Employee(fname, lname, id, salary, t, superMarket);
                employee.setPassword(password);
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
                String startdate = rs.getString("hire_date");
                String jobType = rs.getString("jobType");
                String daysoff = rs.getString("days_off");
                Role role = Role.valueOf(rs.getString("role"));
                String superMarketAddress = rs.getString("Address");

                SuperMarket superMarket = SuperMarketDao.getInstance().getSuperMarketByAddress(superMarketAddress);
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
            String sql = "INSERT INTO Employee (ID, fname, lname, salary,Job, Address, Manager_Name,Role, hire_date, days_off, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, employee.getId());
            statement.setString(2, employee.getFname());
            statement.setString(3, employee.getLname());
            statement.setInt(4, employee.getSalary());
            statement.setString(9, (employee.getTerms().getStartdate()));
            statement.setString(5, employee.getTerms().getJobType());
            statement.setString(8, employee.getRole().toString());
            statement.setString(6, employee.getSuperMarketBranch().getAddress());
            statement.setString(7, employee.getSuperMarketBranch().getManagerName());
            statement.setString(11, employee.getPassword());
            statement.setString(10, employee.getTerms().getDaysoff());

            statement.executeUpdate();

            EmployeeController.addEmployeeToSystem(employee);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataSource.closeConnection();
        }
    }

    public void updateRole(String employeeId, Role newRole) throws SQLException {
        Connection conn = DataSource.getConnection();
        String sql = "UPDATE Employee SET role = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newRole.name());
            stmt.setString(2, employeeId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Failed to update role in the database.", e);
        } finally {
            conn.close();
        }
    }
    public void updateSuperMarket(String employeeId, SuperMarket s) throws SQLException {
        Connection conn = DataSource.getConnection();
        String sql = "UPDATE Employee SET Address = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, s.getAddress());
            stmt.setString(2, employeeId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Failed to update Supermarket in the database.", e);
        } finally {
            conn.close();
        }
    }
    }

