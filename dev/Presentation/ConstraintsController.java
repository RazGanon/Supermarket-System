package Presentation;
import Domain.Constraints.*;

import java.util.Objects;
import java.util.Scanner;
public class ConstraintsController {
    String id;


    //crate matrix of the shifts and day .
    //column is thd day , 0 -day one , 1-day 2 ..
    //row is the shifts - row 0 is morning shift and row 1 is evening shift.
    //Each slot in the matrix have a number . 0 = cant work , 1 = can work.
    public static int[][] buildMatrix() {
        // Define the matrix dimensions
        int rows = 2; // Two rows
        int cols = 6; // Six columns

        // Create the matrix
        int[][] matrix = new int[rows][cols];

        // Populate the matrix with zeros
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = 0;
            }
        }

        return matrix;
    }

    int[][] matrix_constraint = buildMatrix();


    public ConstraintsController(String id, int[][] matrix_constraint) {

        this.id = id;
        this.matrix_constraint = matrix_constraint;

    }
    //defult constractor if the emplouee dont have constraint yet
    public ConstraintsController(String id ) {

        this.id = id;
        this.matrix_constraint = buildMatrix();

    }
    public int[][] getMatrix_constraint ( ){
        return this.matrix_constraint;
    }


    //this method get row(day) and col (shift) and put the binary number 0 if cant work and 1 if can work
    public void addConstraint(String id,int row, int col, int binary) {
        this.matrix_constraint[row][col] = binary;
        this.id=id;
    }

    public void getConstraintFromUser() {
        // Ask the user to input the ID
        System.out.print("Enter the employee ID: ");
        Scanner scanner = new Scanner(System.in);
        String employeeId = scanner.nextLine(); // Read the input as a string
        //this is for sunday
        System.out.print("for Sunday if you can work morning and evening press 11" +
                "if you cant work press 00 " +
                "if you can work only morning press 10 "
                 + " if you can work only evening pres 01");
        String userInput_sunday = scanner.nextLine();
        for (int day = 0; day < 6; day++) {
            System.out.println("For day " + (day + 1) + ":");
            System.out.print("Enter your availability (11 for both morning and evening, 10 for only morning, 01 for only evening, 00 for none): ");
            String userInput = scanner.nextLine(); // Read the input as a string

            // Process the user input based on the constraints
            if (userInput.equals("11")) {
                //System.out.println("You can work both morning and evening on day " + (day + 1));
                // Add constraints for both morning and evening shifts
                this.addConstraint(id,day,0,1);
                this.addConstraint(id,day,1,1);

            }  if (userInput.equals("10")) {
                //System.out.println("You can work only morning on day " + (day + 1));

                // Add constraints for only morning shift
                this.addConstraint(id,day,0,1);
            }  if (userInput.equals("01")) {
                //System.out.println("You can work only evening on day " + (day + 1));
                // Add constraints for only evening shift
                this.addConstraint(id,day,1,1);
            }  if (userInput.equals("00")) {
                //System.out.println("You can't work on day " + (day + 1));
                // No constraints needed
            } else {
                System.out.println("Invalid input. Please try again.");
                day--; // Repeat the loop for the same day
            }
        }// Read the input as a string
        /*
        if (Objects.equals(userInput_sunday, "11"))
        {
            this.addConstraint(employeeId ,0,0,1);
            this.addConstraint(employeeId,0,1,1);
        }
        else if (Objects.equals(userInput_sunday, "10"))
        {
            this.addConstraint(employeeId ,0,0,1);
        }
        else if (Objects.equals(userInput_sunday, "01"))
        {
            this.addConstraint(employeeId, 0,1,1);
        }

        //for monday
        System.out.print("for Sunday if you can work morning and evening press 11" +
                "if you cant work press 00 " +
                "if you can work only morning press 10 "
                + " if you can work only evening pres 01");
        String userInputaymondy = scanner.nextLine(); // Read the input as a string
        if (Objects.equals(userInputaymondy, "11"))
        {
            this.addConstraint(employeeId ,0,1,1);
            this.addConstraint(employeeId,1,1,1);
        }
        else if (Objects.equals(userInputaymondy, "10"))
        {
            this.addConstraint(employeeId ,0,1,1);
        }
        else if (Objects.equals(userInputaymondy, "01"))
        {
            this.addConstraint(employeeId, 1,1,1);
        }
        /
         */





    }

    // Method to print a matrix
    public  void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + "\t");
            }
            System.out.println();
        }
    }
}
