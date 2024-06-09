package Service;

import Domain.Constraints;

import java.util.*;

public class ConstraintsService {
    private final Map<String, Constraints> constraintsMap = new HashMap<>();
    private final Set<String>[] employeeForShiftsMorning = new HashSet[6];
    private final Set<String>[] employeeForShiftsEvening = new HashSet[6];
    private  int submission = 0 ; // if =0 Employees can submit new constraints , if == 1 they cant

    public ConstraintsService() {
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

    public void addConstraintsFromUserInput(String employeeId, Map<Integer, String> userInputConstraints) {
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
        this.submission = 0 ;
        System.out.println("Submissions have been allowed.");

    }

    public int getSubmission(){
        return this.submission;
    }
    public Map<String, Constraints> getAllConstraints() {
        return constraintsMap;
    }

    public void printListEmpl() {
        System.out.println("\nThe Map Contains constraints: \n\n" + constraintsMap);
    }

    public void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int value : row) {
                System.out.print(value + "\t");
            }
            System.out.println();
        }
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
}
