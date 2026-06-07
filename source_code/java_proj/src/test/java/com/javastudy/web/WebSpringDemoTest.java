package com.javastudy.web;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WebSpringDemoTest {

    private final WebSpringDemo demo = new WebSpringDemo();

    @Test
    void httpJsonAndConceptsAreCovered() throws Exception {
        assertEquals("Created", demo.statusMeaning(201));
        assertEquals("Other", demo.statusMeaning(302));
        String json = demo.toJson(new WebSpringDemo.UserResponse(1L, "Alice"));
        assertEquals("Alice", demo.fromJson(json).name());
        assertTrue(demo.webConcepts().contains("IoC"));
        assertTrue(demo.transactionFailureReasons().contains("self invocation"));
        assertTrue(demo.observabilitySignals().contains("Actuator"));
        assertEquals("environment variables", demo.configLayers().get("prod"));
    }

    @Test
    void controllerServiceAndGlobalExceptionHandlerWorkStandalone() {
        WebSpringDemo.UserService service = new WebSpringDemo.UserService();
        WebSpringDemo.UserController controller = new WebSpringDemo.UserController(service);
        ResponseEntity<WebSpringDemo.UserResponse> created =
                controller.create(new WebSpringDemo.UserRequest("Bob"));
        assertEquals(HttpStatus.CREATED, created.getStatusCode());
        assertEquals("Bob", created.getBody().name());
        assertEquals("Alice", controller.find(1L).name());

        WebSpringDemo.ApiExceptionHandler handler = new WebSpringDemo.ApiExceptionHandler();
        ResponseEntity<Map<String, Object>> error =
                handler.handleNotFound(new WebSpringDemo.UserNotFoundException(9L));
        assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
        assertEquals(9L, error.getBody().get("id"));
    }
}
