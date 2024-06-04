package Domain;


public class DaySchedule {
    EmployeeController emp ;
    private final String day;
    private Node head; // Head of the linked list for regular employees
    private Employee manager; // Manager assigned to the shift

    // Constructor
    public DaySchedule(String day) {
        this.day = day;
        this.head = null;
        this.manager = null;
    }

    // Method to add an employee to the linked list
    public void addEmployee(Employee employee) {
        if (emp.isManager(employee)) { // Check if the employee is a Manager
            if (manager == null) {
                this.manager = employee;
            } else {
                throw new IllegalArgumentException("Only one manager allowed per shift");
            }
        } else { // Regular employee
            Node newNode = new Node(employee) {
            };
            if (head == null) {
                head = newNode;
            } else {
                Node current = head;
                while (current.getNext() != null) {
                    current = current.getNext();
                }
                current.setNext(newNode);
            }        }
    }

    // Method to get the manager for the shift
    public Employee getManager() {
        return manager;
    }

    // Method to get the linked list of employees for a day (optional)
    public Node getEmployees() {
        return head;
    }
}
