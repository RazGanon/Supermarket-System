package Presentation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

public class MainTest {

    private final InputStream originalSystemIn = System.in;
    private final PrintStream originalSystemOut = System.out;
    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;

    @BeforeEach
    public void setUp() {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
        System.setIn(originalSystemIn);  // Reset System.in to its original state
    }

    private void provideInput(String data) {
        testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }

    @Test
    public void testMainWithFullSequence() {
        // Simulate input for the entire sequence of interactions
        String input = "yes\n" +   // Clean data before running
                "yes\n" +   // Clean ConstraintsTable
                "yes\n" +   // Clean schedule
                "yes\n" +   // Clean schedule_shift_employee
                "yes\n" +   // Clean Employee
                "yes\n" +   // Clean WeekFlag
                "1234\n" +  // Enter username
                "1234\n" +  // Enter password
                "15\n";     // Exit (assuming there's a prompt asking to exit)
        provideInput(input);

        // Act
        Main.main(new String[]{});

        // Assert
        String output = testOut.toString();
        System.out.println(output); // Print the output for debugging
        assertTrue(output.contains("Do you want to clean data before running? (yes/no)"), "Initial prompt should be displayed");
        assertTrue(output.contains("Do you want to clean ConstraintsTable table? (yes/no)"), "Prompt for cleaning ConstraintsTable should be displayed");
        assertTrue(output.contains("Do you want to clean schedule table? (yes/no)"), "Prompt for cleaning schedule should be displayed");
        assertTrue(output.contains("Do you want to clean schedule_shift_employee table? (yes/no)"), "Prompt for cleaning schedule_shift_employee should be displayed");
        assertTrue(output.contains("Do you want to clean Employee table? (yes/no)"), "Prompt for cleaning Employee should be displayed");
        assertTrue(output.contains("Do you want to clean WeekFlag table? (yes/no)"), "Prompt for cleaning WeekFlag should be displayed");
        assertTrue(output.contains("Enter username:"), "Prompt for username should be displayed");
        assertTrue(output.contains("Enter password:"), "Prompt for password should be displayed");
        // Add more assertions as needed to verify the state after these interactions
    }

}
