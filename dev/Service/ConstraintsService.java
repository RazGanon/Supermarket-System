package Service;

import Domain.Constraints;

import java.util.*;

public class ConstraintsService {
        Map<String, Constraints> constraintsMap = new HashMap<>();
        Set<String> employeeForShiftsMorning[] = new Set[6];
        Set<String> employeeForShiftsEvening[] = new Set[6];

        public ConstraintsService() {
            for (int i = 0; i < 6; i++) {
                employeeForShiftsMorning[i] = new HashSet<>();
                employeeForShiftsEvening[i] = new HashSet<>();
            }
        }

        // Create matrix of the shifts and day.
        // Column is the day, 0 - day one, 1 - day 2 ..
        // Row is the shifts - row 0 is morning shift and row 1 is evening shift.
        // Each slot in the matrix has a number: 0 = can't work, 1 = can work.
        public static int[][] buildMatrix() {
            int rows = 2; // Two rows: morning and evening
            int cols = 6; // Six columns: six days

            // Create and initialize the matrix with zeros
            int[][] matrix = new int[rows][cols];
            for (int i = 0; i < rows; i++) {
                Arrays.fill(matrix[i], 0);
            }

            return matrix;
        }

        // This method gets row (shift) and col (days) and puts the binary number 0 if can't work and 1 if can work
        public void addConstraint(String id, int row, int col, int binary) {
            Constraints constraints = constraintsMap.get(id);
            if (constraints != null) {
                constraints.getMatrix()[row][col] = binary;
            } else {
                int[][] matrix = buildMatrix();
                matrix[row][col] = binary;
                constraintsMap.put(id, new Constraints(matrix));
            }
        }

        // Add constraint to employee with id for all the week
        public Constraints getConstraintFromUser() {
            int[][] matrix = buildMatrix();

            // Ask the user to input the ID
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the employee ID: ");
            String employeeId = scanner.nextLine();

            for (int day = 0; day < 6; day++) {
                System.out.println("For day " + (day + 1) + ":");
                System.out.print("Enter your availability (11 for both morning and evening, 10 for only morning, 01 for only evening, 00 for none): ");
                String userInput = scanner.nextLine();

                switch (userInput) {
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
                        // Do nothing as the default is can't work (0)
                        break;
                    default:
                        System.out.println("Invalid input. Please try again.");
                        day--; // Repeat the loop for the same day
                        break;
                }
            }

            Constraints constraints = new Constraints(matrix);
            constraintsMap.put(employeeId, constraints);
            return constraints;
        }

        public Constraints getMatrixFromID(String id) {
            return constraintsMap.get(id);
        }

        // Get the matrix of constraints
        public Constraints getMatrix(String id) {
            return constraintsMap.get(id);
        }

        public Map<String, Constraints> getAllConstraints() {
            return constraintsMap;
        }

        public void printListEmpl() {
            System.out.println("\nThe Map Contains constraints: \n\n" + constraintsMap);
        }

        // Method to print a matrix
        public void printMatrix(int[][] matrix) {
            for (int[] row : matrix) {
                for (int value : row) {
                    System.out.print(value + "\t");
                }
                System.out.println();
            }
        }
    }


