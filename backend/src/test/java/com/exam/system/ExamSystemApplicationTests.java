package com.exam.system;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ExamSystemApplicationTests {

    @Test
    void contextLoads() {
        // This test ensures that the Spring context loads successfully
        assertNotNull("Application context loaded successfully");
    }
}