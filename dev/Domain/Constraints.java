package Domain;
import java.util.ArrayList;



public class Constraints

{
    private int[][] ConstraintsMatrix ;
    public  Constraints (int [][] constraintsMatrix){
        this.ConstraintsMatrix = constraintsMatrix;
    }

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

}
