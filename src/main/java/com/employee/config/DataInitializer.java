package com.employee.config;

import com.employee.model.Employee;
import com.employee.repository.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedEmployees(EmployeeRepository employeeRepository) {
        return args -> {
            if (employeeRepository.count() > 0) {
                return;
            }

            employeeRepository.save(buildEmployee("John", "Doe", "john.doe@example.com", "Engineering", "Software Engineer"));
            employeeRepository.save(buildEmployee("Jane", "Smith", "jane.smith@example.com", "HR", "HR Manager"));
            employeeRepository.save(buildEmployee("Alice", "Johnson", "alice.johnson@example.com", "Finance", "Analyst"));
        };
    }

    private Employee buildEmployee(String firstName, String lastName, String email,
                                   String department, String jobTitle) {
        Employee employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setEmail(email);
        employee.setDepartment(department);
        employee.setJobTitle(jobTitle);
        return employee;
    }
}
