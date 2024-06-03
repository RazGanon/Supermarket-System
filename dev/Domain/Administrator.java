package Domain;

import java.util.List;

public class Administrator {
    private EmployeeController employeeController;

    // Constructor
    public Administrator(EmployeeController employeeController) {
        this.employeeController = employeeController;
    }

    // Method to manage employee roles
    public void manageEmployeeRoles(String employeeId, Role newRole) {
        Employee employee = employeeController.getEmployeeById(employeeId);
        if (employee != null) {
            employee.setRole(newRole);
            System.out.println("Role of employee " + employeeId + " updated to " + newRole.get_Role_Name());
        } else {
            System.out.println("Employee with ID " + employeeId + " not found.");
        }
    }



}
