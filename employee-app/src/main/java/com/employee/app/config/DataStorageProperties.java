package com.employee.app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.data")
public record DataStorageProperties(String dir, String usersFile, String employeesFile) {
}
