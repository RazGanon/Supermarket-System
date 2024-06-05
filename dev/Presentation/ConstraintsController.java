package Presentation;
import Domain.Constraints;
import Domain.Constraints.*;
import com.example.HelloWorld.Main;

import java.util.*;
import java.util.Map;
public class ConstraintsController {
    Map <String  , Constraints > Contrains_map=new HashMap<>();
    Set<String> EmployeeForShifts_1_morning = new  HashSet<>() ;
    Set<String> EmployeeForShifts_1_evening = new  HashSet<>() ;
    Set<String> EmployeeForShifts_2_morning = new  HashSet<>() ;
    Set<String> EmployeeForShifts_2_evening = new  HashSet<>() ;
    Set<String> EmployeeForShifts_3_morning  = new  HashSet<>() ;
    Set<String> EmployeeForShifts_3_evening = new  HashSet<>() ;
    Set<String> EmployeeForShifts_4_morning = new  HashSet<>() ;
    Set<String> EmployeeForShifts_4_evening = new  HashSet<>() ;
    Set<String> EmployeeForShifts_5_morning  = new  HashSet<>() ;
    Set<String> EmployeeForShifts_5_evening = new  HashSet<>() ;
    Set<String> EmployeeForShifts_6_morning  = new  HashSet<>() ;
    Set<String> EmployeeForShifts_6_evening = new  HashSet<>() ;



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
    //int [][] matrix = buildMatrix();
    //Constraints constraints=new Constraints(matrix) ;



    public ConstraintsController() {

    }





    //this method get row(day) and col (shift) and put the binary number 0 if cant work and 1 if can work
    public void addConstraint(String id,int row, int col, int binary) {





    }


    //add constraint to employee with id for all the week

    public Constraints getConstraintFromUser() {
        int [][] matrix = buildMatrix();
        //Constraints constraints=new Constraints(matrix) ;


        // Ask the user to input the ID
        System.out.print("Enter the employee ID: ");
        Scanner scanner = new Scanner(System.in);
        String employeeId = scanner.nextLine(); // Read the input as a string

        for (int day = 0; day < 6; day++) {
            System.out.println("For day " + (day + 1) + ":");
            System.out.print("Enter your availability (11 for both morning and evening, 10 for only morning, 01 for only evening, 00 for none): ");
            String userInput = scanner.nextLine(); // Read the input as a string

            // Process the user input based on the constraints
            if (userInput.equals("11")) {
                //System.out.println("You can work both morning and evening on day " + (day + 1));
                // Add constraints for both morning and evening shifts
                matrix[0][day]=1;
                matrix[1][day]=1;
                if(day==0){
                    EmployeeForShifts_1_morning.add(employeeId);
                    EmployeeForShifts_1_evening.add(employeeId);
                }
                if(day==1){
                    EmployeeForShifts_2_morning.add(employeeId);
                    EmployeeForShifts_2_evening.add(employeeId);
                }
                if(day==2){
                    EmployeeForShifts_3_morning.add(employeeId);
                    EmployeeForShifts_3_evening.add(employeeId);
                }
                if(day==3){
                    EmployeeForShifts_4_morning.add(employeeId);
                    EmployeeForShifts_4_evening.add(employeeId);
                }
                if(day==4){
                    EmployeeForShifts_5_morning.add(employeeId);
                    EmployeeForShifts_5_evening.add(employeeId);
                }
                if(day==5){
                    EmployeeForShifts_6_morning.add(employeeId);
                    EmployeeForShifts_6_evening.add(employeeId);
                }

                //EmployeeForShifts.add(employeeId);//set of shift in day 1 shift mornning
                //this.addConstraint(employeeId,0,day,1);
                //this.addConstraint(employeeId,1,day,1);

            }   if (userInput.equals("10")) {
                //System.out.println("You can work only morning on day " + (day + 1));
                // Add constraints for only morning shift
                matrix[0][day]=1;
                if(day==0){
                    EmployeeForShifts_1_morning.add(employeeId);

                }
                if(day==1){
                    EmployeeForShifts_2_morning.add(employeeId);

                }
                if(day==2){
                    EmployeeForShifts_3_morning.add(employeeId);

                }
                if(day==3){
                    EmployeeForShifts_4_morning.add(employeeId);

                }
                if(day==4){
                    EmployeeForShifts_5_morning.add(employeeId);

                }
                if(day==5){
                    EmployeeForShifts_6_morning.add(employeeId);

                }

                //this.addConstraint(employeeId,0,day,1);
            }  if (userInput.equals("01")) {
                //System.out.println("You can work only evening on day " + (day + 1));
                // Add constraints for only evening shift
                matrix[1][day]=1;
                if(day==0){
                    EmployeeForShifts_1_evening.add(employeeId);

                }
                if(day==1){
                    EmployeeForShifts_2_evening.add(employeeId);

                }
                if(day==2){
                    EmployeeForShifts_3_evening.add(employeeId);

                }
                if(day==3){
                    EmployeeForShifts_4_evening.add(employeeId);

                }
                if(day==4){
                    EmployeeForShifts_5_evening.add(employeeId);

                }
                if(day==5){
                    EmployeeForShifts_6_evening.add(employeeId);

                }
                //this.addConstraint(employeeId ,1,day,1);
            }
            if (!(userInput.equals("01") || userInput.equals("10") ||userInput.equals("11") ||userInput.equals("00"))) {
                System.out.println("Invalid input. Please try again.");
                day--; // Repeat the loop for the same day

            }
        }
        Constraints constraints=new Constraints(matrix) ;
        Contrains_map.put(employeeId,constraints);
        return getMatrixFromID(employeeId);









    }
    public Constraints getMatrixFromID (String id ){
        return Contrains_map.get(id);
    }
    //get matrix of contrains
    public Constraints  get_matrix (String id ){

        return Contrains_map.get(id);
    }
    public Map getAllConstrains (){
        return Contrains_map;
    }
    public void printListEmpl(){
        System.out.println("\nThe Map Contains constrains: \n\n" + Contrains_map);


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
