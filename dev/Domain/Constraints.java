package Domain;

public class Constraints {

    private int[][] constraintsMatrix;

    // Constructor
    public Constraints(int[][] constraintsMatrix) {
        if (constraintsMatrix == null || constraintsMatrix.length == 0) {
            throw new IllegalArgumentException("Constraints matrix cannot be null or empty");
        }
        this.constraintsMatrix = constraintsMatrix;
    }

    // Getter
    public int[][] getMatrix() {
        return this.constraintsMatrix;
    }
    public boolean canWork(int day, int shiftType) {
        return constraintsMatrix[shiftType][day] == 1;
    }

    // Method to print the constraint matrix
    public void printConstraint() {
        for (int[] row : constraintsMatrix) {
            for (int value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }
}
