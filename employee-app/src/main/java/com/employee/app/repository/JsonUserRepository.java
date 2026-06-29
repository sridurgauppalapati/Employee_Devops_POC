package com.employee.app.repository;

import com.employee.app.config.DataStorageProperties;
import com.employee.app.model.UserAccount;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Repository
public class JsonUserRepository {

    private final DataStorageProperties properties;
    private final ObjectMapper objectMapper;
    private Path usersFilePath;

    public JsonUserRepository(DataStorageProperties properties, ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    void init() throws IOException {
        Path dataDir = Path.of(properties.dir()).toAbsolutePath().normalize();
        Files.createDirectories(dataDir);
        usersFilePath = dataDir.resolve(properties.usersFile());

        if (Files.notExists(usersFilePath)) {
            ClassPathResource seed = new ClassPathResource("data/users.json");
            Files.copy(seed.getInputStream(), usersFilePath);
        }
    }

    public Optional<UserAccount> findByUsername(String username) {
        return readAll().stream()
                .filter(user -> user.username().equalsIgnoreCase(username))
                .findFirst();
    }

    private List<UserAccount> readAll() {
        try {
            return objectMapper.readValue(usersFilePath.toFile(), new TypeReference<>() {});
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to read users from " + usersFilePath, ex);
        }
    }
}
