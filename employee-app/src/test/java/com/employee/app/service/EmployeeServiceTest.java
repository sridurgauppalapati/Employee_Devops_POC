package com.employee.app.service;

import com.employee.app.model.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class EmployeeServiceTest {

    @TempDir
    static Path tempDir;

    @DynamicPropertySource
    static void overrideDataDir(DynamicPropertyRegistry registry) {
        registry.add("app.data.dir", () -> tempDir.toString());
    }

    @Autowired
    private EmployeeService employeeService;

    @Test
    void shouldSearchEmployeesByDepartment() {
        employeeService.save(new Employee(null, "Alice", "Brown", "alice@example.com", "Finance", "Analyst"));

        var results = employeeService.search("Finance");

        assertEquals(1, results.size());
        assertEquals("Alice", results.getFirst().firstName());
    }

    @Test
    void shouldReturnAllEmployeesWhenSearchQueryIsBlank() {
        assertTrue(employeeService.search("").size() >= 2);
    }
}
