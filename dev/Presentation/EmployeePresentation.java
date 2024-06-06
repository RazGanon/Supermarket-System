//package Presentation;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//
//public class EmployeePresentation extends Container {
//    private JButton insertUserButton;
//    private JTextField UserName;
//    private JTextField UsereMail;
//    private JPanel mainPanel;
//
//    public EmployeePresentation() {
//        insertUserButton.addActionListener(new ActionListener() {
//
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                UserApplication ua = new UserApplication();
//                ua.insertUser(UserName.getText(), UsereMail.getText());
//            }
//        });
//    }
//    public static void main (String args[]){
//        JFrame frame = new JFrame("Example");
//        frame.setContentPane(new UserPresentation().mainPanel);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.pack();
//        frame.setVisible(true);
//    }
//}
