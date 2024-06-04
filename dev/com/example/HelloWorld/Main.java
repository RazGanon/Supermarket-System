package com.example.HelloWorld;
import java.util.Scanner;
import Presentation.ConstraintsController;

public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        //System.out.printf("Hello World!");

        ConstraintsController m = new ConstraintsController("123");
        int [][]ma =m.getMatrix_constraint();
        m.printMatrix(m.getMatrix_constraint());
        m.getConstraintFromUser();
        m.printMatrix(m.getMatrix_constraint());



    }






}