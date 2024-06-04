package Domain;

import Presentation.ConstraintsController;

import java.util.List;
import java.util.Map;

public class Administrator {
    private EmployeeController employeeController;
    private ConstraintsController cC;

    // Constructor
    public Administrator(EmployeeController employeeController, ConstraintsController CC) {
        this.employeeController = employeeController;
        this.cC=CC;
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

//    public void buildWorkArrangment(){
//        Map constraintsMap = cC.getAllConstrains();
//        constraintsMap.forEach();
//
//


//
//
//    }



}
