package com.employee.app.repository;

import com.employee.app.config.DataStorageProperties;
import com.employee.app.model.Employee;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Repository
public class JsonEmployeeRepository {

    private final DataStorageProperties properties;
    private final ObjectMapper objectMapper;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private Path employeesFilePath;

    public JsonEmployeeRepository(DataStorageProperties properties, ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    void init() throws IOException {
        Path dataDir = Path.of(properties.dir()).toAbsolutePath().normalize();
        Files.createDirectories(dataDir);
        employeesFilePath = dataDir.resolve(properties.employeesFile());

        if (Files.notExists(employeesFilePath)) {
            org.springframework.core.io.ClassPathResource seed =
                    new org.springframework.core.io.ClassPathResource("data/employees.json");
            Files.copy(seed.getInputStream(), employeesFilePath);
        }
    }

    public List<Employee> findAll() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(readAllUnsafe());
        } finally {
            lock.readLock().unlock();
        }
    }

    public Optional<Employee> findById(Long id) {
        return findAll().stream().filter(employee -> employee.id().equals(id)).findFirst();
    }

    public List<Employee> search(String query) {
        if (query == null || query.isBlank()) {
            return findAll();
        }
        String normalized = query.trim().toLowerCase();
        return findAll().stream()
                .filter(employee -> matches(employee, normalized))
                .toList();
    }

    public Employee save(Employee employee) {
        lock.writeLock().lock();
        try {
            List<Employee> employees = readAllUnsafe();
            Long id = employee.id() != null ? employee.id() : nextId(employees);
            Employee saved = employee.withId(id);

            employees.removeIf(existing -> existing.id().equals(saved.id()));
            employees.add(saved);
            employees.sort(Comparator.comparing(Employee::id));
            writeAllUnsafe(employees);
            return saved;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean deleteById(Long id) {
        lock.writeLock().lock();
        try {
            List<Employee> employees = readAllUnsafe();
            boolean removed = employees.removeIf(employee -> employee.id().equals(id));
            if (removed) {
                writeAllUnsafe(employees);
            }
            return removed;
        } finally {
            lock.writeLock().unlock();
        }
    }

    private boolean matches(Employee employee, String query) {
        return contains(employee.firstName(), query)
                || contains(employee.lastName(), query)
                || contains(employee.email(), query)
                || contains(employee.department(), query)
                || contains(employee.jobTitle(), query);
    }

    private boolean contains(String value, String query) {
        return value != null && value.toLowerCase().contains(query);
    }

    private Long nextId(List<Employee> employees) {
        return employees.stream()
                .map(Employee::id)
                .max(Long::compareTo)
                .map(id -> id + 1)
                .orElse(1L);
    }

    private List<Employee> readAllUnsafe() {
        try {
            return new ArrayList<>(objectMapper.readValue(employeesFilePath.toFile(), new TypeReference<>() {}));
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to read employees from " + employeesFilePath, ex);
        }
    }

    private void writeAllUnsafe(List<Employee> employees) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(employeesFilePath.toFile(), employees);
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to write employees to " + employeesFilePath, ex);
        }
    }
}
