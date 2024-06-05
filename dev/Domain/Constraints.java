package Domain;


public class Constraints

{
    private int[][] ConstraintsMatrix ;
    //constructor
    public  Constraints (int [][] constraintsMatrix){
        this.ConstraintsMatrix = constraintsMatrix;
    }

    //getter
    public int[][] getMatrix(){
        return this.ConstraintsMatrix;
    }



}
