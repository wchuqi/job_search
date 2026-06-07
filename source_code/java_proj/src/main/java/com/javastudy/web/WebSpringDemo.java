package com.javastudy.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

public class WebSpringDemo {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String statusMeaning(int status) {
        return switch (status) {
            case 200 -> "OK";
            case 201 -> "Created";
            case 400 -> "Bad Request";
            case 404 -> "Not Found";
            case 500 -> "Internal Server Error";
            default -> "Other";
        };
    }

    public String toJson(UserResponse user) throws JsonProcessingException {
        return objectMapper.writeValueAsString(user);
    }

    public UserResponse fromJson(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, UserResponse.class);
    }

    public List<String> webConcepts() {
        return List.of("HTTP method", "status code", "JSON", "Servlet", "Filter",
                "Controller", "IoC", "DI", "AOP", "validation", "global exception handler");
    }

    public List<String> transactionFailureReasons() {
        return List.of("self invocation", "method is not public", "wrong propagation",
                "exception does not trigger rollback", "proxy is bypassed");
    }

    public List<String> observabilitySignals() {
        return List.of("log", "metric", "trace", "health check", "profiling", "Actuator");
    }

    public Map<String, String> configLayers() {
        return Map.of("dev", "local defaults", "test", "test fixtures", "prod", "environment variables");
    }

    public record UserRequest(@NotBlank String name) {}
    public record UserResponse(long id, String name) {}

    @Service
    public static class UserService {
        public UserResponse create(UserRequest request) {
            return new UserResponse(1L, request.name());
        }

        public UserResponse find(long id) {
            if (id == 1L) {
                return new UserResponse(1L, "Alice");
            }
            throw new UserNotFoundException(id);
        }
    }

    @RestController
    public static class UserController {
        private final UserService service;

        public UserController(UserService service) {
            this.service = service;
        }

        @PostMapping("/users")
        public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest request) {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
        }

        @GetMapping("/users/{id}")
        public UserResponse find(@PathVariable long id) {
            return service.find(id);
        }
    }

    @ControllerAdvice
    public static class ApiExceptionHandler {
        @ExceptionHandler(UserNotFoundException.class)
        public ResponseEntity<Map<String, Object>> handleNotFound(UserNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "USER_NOT_FOUND", "id", ex.id()));
        }
    }

    public static class UserNotFoundException extends RuntimeException {
        private final long id;

        public UserNotFoundException(long id) {
            super("User not found: " + id);
            this.id = id;
        }

        public long id() {
            return id;
        }
    }
}
