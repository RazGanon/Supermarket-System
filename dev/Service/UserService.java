package Service;

import Domain.Employee;
import Domain.Role;

import java.util.HashMap;
import java.util.Map;

public class UserService {
    private Map<String, Employee> users;

    public UserService() {
        users = new HashMap<>();
    }

    public void addUser(Employee employee) {
        if (employee != null && employee.getId() != null) {
            users.put(employee.getId(), employee);
        }
    }

    public Employee login(String username, String password) {
        Employee user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public boolean isManager(Employee employee) {
        return employee != null && employee.getRole() == Role.Manager;
    }

    public void changeRole(String userId, Role newRole) {
        Employee user = users.get(userId);
        if (user != null) {
            user.setRole(newRole);
        }
    }

    public Map<String, Employee> getAllUsers() {
        return new HashMap<>(users); // Return a copy to prevent external modifications
    }

    public Employee getUserById(String userId) {
        return users.get(userId);
    }

    public void removeUser(String userId) {
        users.remove(userId);
    }
}
