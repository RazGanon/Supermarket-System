//package Presentation;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import org.apache.commons.csv.CSVFormat;
//import org.apache.commons.csv.CSVParser;
//import org.apache.commons.csv.CSVRecord;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import Domain.Employee;
//import Service.EmployeeService;
//
//    public class EmployeeController {
//        private EmployeeService employeeService;
//        private ObjectMapper objectMapper;
//
//        public EmployeeController(EmployeeService employeeService) {
//            this.employeeService = employeeService;
//            this.objectMapper = new ObjectMapper();
//        }
//
//        public void processEmployeeCSV(String csvFilePath) {
//            List<Employee> employees = new ArrayList<>();
//            try (FileReader reader = new FileReader(csvFilePath);
//                 CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())) {
//
//                for (CSVRecord csvRecord : csvParser) {
//                    String jsonString = csvRecord.get("json");
//                    Employee employee = objectMapper.readValue(jsonString, Employee.class);
//                    employees.add(employee);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            // Send employees to EmployeeService
//            for (Employee employee : employees) {
//                employeeService.addEmployee(employee);
//            }
//        }
//    }
//
//}
