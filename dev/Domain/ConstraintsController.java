package Domain;
import Data.ConstraintsDao;
import Data.EmployeeDao;

import java.util.*;
public class ConstraintsController {
    private final Map<String, Constraints> constraintsMap = new HashMap<>();
    //this set contains for each Morning Shift - Who is work by ID
    private final Set<String>[] employeeForShiftsMorning = new HashSet[6];
    //this set contains for each Evening Shift - Who is work
    private final Set<String>[] employeeForShiftsEvening = new HashSet[6];
    // flag --> if =0 Employees can submit new constraints , if == 1 they cant
    private  int submission = 0 ;
    private static int weekFlag = 0;
    private final ScheduleController scheduleController;
    private static ConstraintsDao constraintDao=new ConstraintsDao();
    ;
    public ConstraintsController(ScheduleController scheduleController) {
        this.scheduleController = scheduleController;
        for (int i = 0; i < 6; i++) {
            employeeForShiftsMorning[i] = new HashSet<>();
            employeeForShiftsEvening[i] = new HashSet<>();
        }
    }
    public static int[][] buildMatrix() {
        int rows = 2;
        int cols = 6;
        int[][] matrix = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            Arrays.fill(matrix[i], 0);
        }
        return matrix;
    }



    public void addConstraints(String employeeId, Scanner scanner) {
        if (getSubmission() == 1) {
            System.out.println("Submissions are currently not allowed.");
            return;
        }
        Map<Integer, String> userInputConstraints = new HashMap<>();
        for (int day = 0; day < 6; day++) {
            while (true) {
                System.out.println("Enter consitraints for day " + (day + 1) + ": (11 for both, 10 for morning, 01 for evening, 00 for none)");
                String input = scanner.nextLine();
                if (validateInput(input)) {
                    //daocon
                    userInputConstraints.put(day, input);
                    break;
                } else {
                    System.out.println("Invalid input. Please enter a valid constraint (11, 10, 01, or 00).");
                }
            }
        }
        addConstraintsFromUserInput(employeeId, userInputConstraints);

        System.out.println("Constraints added for employee ID: " + employeeId);
    }
    public void addConstraintsFromUserInput(String employeeId, Map<Integer, String> userInputConstraints) {
        //this Func get Employee ID and his Constarint and add it to the map
        if (this.submission != 0 ){
            System.out.println(" Time is over , Can't add constraints right now!");
            return;
        }

        int[][] matrix = buildMatrix();

        for (Map.Entry<Integer, String> entry : userInputConstraints.entrySet()) {
            int day = entry.getKey();
            String input = entry.getValue();

            switch (input) {
                case "11":
                    matrix[0][day] = 1;
                    matrix[1][day] = 1;
                    employeeForShiftsMorning[day].add(employeeId);
                    employeeForShiftsEvening[day].add(employeeId);
                    break;
                case "10":
                    matrix[0][day] = 1;
                    employeeForShiftsMorning[day].add(employeeId);
                    break;
                case "01":
                    matrix[1][day] = 1;
                    employeeForShiftsEvening[day].add(employeeId);
                    break;
                case "00":
                    break;
                default:
                    throw new IllegalArgumentException("Invalid input: " + input);
            }
        }

        Constraints constraints = new Constraints(matrix);
        constraintsMap.put(employeeId, constraints);
        //add function to save user constraints to db
        constraintDao.saveConstraints(employeeId,constraints,weekFlag);
    }

    public int getSubmission(){
        return this.submission;
    }
    private boolean validateInput(String input) {
        return input.equals("11") || input.equals("10") || input.equals("01") || input.equals("00");
    }




    public void setShiftRequirements(Scanner scanner) {
        //first need to check if admin let the employee's eo append their requirements


        for (int day = 0; day < 6; day++) {
            System.out.println("Enter requirements for day " + (day + 1) + ":");
            System.out.print("Morning Managers: ");
            int morningManagers = scanner.nextInt();
            System.out.print("Morning Employees: ");
            int morningEmployees = scanner.nextInt();
            System.out.print("Evening Managers: ");
            int eveningManagers = scanner.nextInt();
            System.out.print("Evening Employees: ");
            int eveningEmployees = scanner.nextInt();
            ShiftRules rules = new ShiftRules(morningManagers, morningEmployees, eveningManagers, eveningEmployees);
            scheduleController.setShiftRequirements(day, rules);
        }
        System.out.println("Shift requirements set successfully.");
    }

    public void generateWeeklySchedule() {
        String schedule = scheduleController.generateWeeklySchedule(getAllConstraints());
        System.out.println(schedule);
    }
    public Map<String, Constraints> getAllConstraints() {
        return constraintsMap;
    }
    public Constraints getMatrixFromID(String id) {
        return constraintsMap.get(id);
    }

    public Constraints getMatrix(String id) {
        return constraintsMap.get(id);
    }
    public void stopSubmission(){
        this.submission = 1 ;
        System.out.println("Submissions have been stopped.");

    }
    public void startSubmission(){
        if (submission!=0) {
            this.submission = 0;
            weekFlag++;
            System.out.println("Submissions have been allowed.");
        }
    }
    public void printListEmpl() {
        System.out.println("\nThe Map Contains constraints: \n\n" + constraintsMap);
    }
    public void printConstraintsForEmployee(String employeeId) {
        Constraints constraints = constraintsMap.get(employeeId);
        if (constraints != null) {
            System.out.println("Constraints for employee ID: " + employeeId);
            printMatrix(constraints.getMatrix());
        } else {
            System.out.println("No constraints found for employee ID: " + employeeId);
        }
    }

    public void printAllConstraints() {
        for (Map.Entry<String, Constraints> entry : constraintsMap.entrySet()) {
            System.out.println("Employee ID: " + entry.getKey());
            printMatrix(entry.getValue().getMatrix());
            System.out.println();
        }
    }
    public void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int value : row) {
                System.out.print(value + "\t");
            }
            System.out.println();
        }
    }

}