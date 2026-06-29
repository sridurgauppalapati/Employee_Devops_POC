package com.employee.app.service;

import com.employee.app.model.Employee;
import com.employee.app.repository.JsonEmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final JsonEmployeeRepository repository;

    public EmployeeService(JsonEmployeeRepository repository) {
        this.repository = repository;
    }

    public List<Employee> findAll() {
        return repository.findAll();
    }

    public Optional<Employee> findById(Long id) {
        return repository.findById(id);
    }

    public List<Employee> search(String query) {
        return repository.search(query);
    }

    public Employee save(Employee employee) {
        return repository.save(employee);
    }

    public boolean delete(Long id) {
        return repository.deleteById(id);
    }
}
