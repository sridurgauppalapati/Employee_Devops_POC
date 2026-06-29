package com.employee.app.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record Employee(
        Long id,
        @NotBlank @Size(max = 50) String firstName,
        @NotBlank @Size(max = 50) String lastName,
        @NotBlank @Email @Size(max = 100) String email,
        @NotBlank @Size(max = 50) String department,
        @NotBlank @Size(max = 80) String jobTitle) {

    public Employee withId(Long newId) {
        return new Employee(newId, firstName, lastName, email, department, jobTitle);
    }
}
