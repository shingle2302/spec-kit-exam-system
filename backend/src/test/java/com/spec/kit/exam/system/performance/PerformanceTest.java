package com.spec.kit.exam.system.performance;

import com.spec.kit.exam.system.dto.LoginRequestDTO;
import com.spec.kit.exam.system.entity.User;
import com.spec.kit.exam.system.service.UserService;
import com.spec.kit.exam.system.util.JwtUtilEnhanced;
import com.spec.kit.exam.system.util.DataEncryptUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class PerformanceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtilEnhanced jwtUtil;

    @Autowired
    private DataEncryptUtil dataEncryptUtil;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("perfuser");
        testUser.setPasswordHash("password123");
        testUser.setEmail("perf@example.com");
        testUser.setRole("TEACHER");
        testUser.setPhone("13800138000");
        testUser.setIdCard("110101199001011234");
    }

    @Test
    void testJwtTokenGenerationPerformance() {
        int iterations = 1000;
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < iterations; i++) {
            String token = jwtUtil.generateAccessToken(testUser);
            assertNotNull(token);
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        double avgTime = (double) duration / iterations;

        System.out.println("JWT Token Generation Performance:");
        System.out.println("Total time: " + duration + "ms");
        System.out.println("Average time per token: " + avgTime + "ms");
        System.out.println("Tokens per second: " + (1000 / avgTime));

        assertTrue(avgTime < 10, "Average token generation time should be less than 10ms");
    }

    @Test
    void testJwtTokenParsingPerformance() {
        String token = jwtUtil.generateAccessToken(testUser);
        int iterations = 1000;
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < iterations; i++) {
            var claims = jwtUtil.parseToken(token);
            assertNotNull(claims);
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        double avgTime = (double) duration / iterations;

        System.out.println("JWT Token Parsing Performance:");
        System.out.println("Total time: " + duration + "ms");
        System.out.println("Average time per parse: " + avgTime + "ms");
        System.out.println("Parses per second: " + (1000 / avgTime));

        assertTrue(avgTime < 5, "Average token parsing time should be less than 5ms");
    }

    @Test
    void testDataEncryptionPerformance() {
        String plainText = "This is a test string for encryption performance testing.";
        int iterations = 1000;
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < iterations; i++) {
            String encrypted = dataEncryptUtil.encrypt(plainText);
            String decrypted = dataEncryptUtil.decrypt(encrypted);
            assertEquals(plainText, decrypted);
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        double avgTime = (double) duration / iterations;

        System.out.println("Data Encryption/Decryption Performance:");
        System.out.println("Total time: " + duration + "ms");
        System.out.println("Average time per operation: " + avgTime + "ms");
        System.out.println("Operations per second: " + (1000 / avgTime));

        assertTrue(avgTime < 10, "Average encryption/decryption time should be less than 10ms");
    }

    @Test
    void testConcurrentTokenGeneration() throws InterruptedException {
        int threadCount = 10;
        int iterationsPerThread = 100;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < iterationsPerThread; j++) {
                        String token = jwtUtil.generateAccessToken(testUser);
                        if (token != null && !token.isEmpty()) {
                            successCount.incrementAndGet();
                        } else {
                            failureCount.incrementAndGet();
                        }
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        int totalOperations = threadCount * iterationsPerThread;
        double avgTime = (double) duration / totalOperations;

        System.out.println("Concurrent Token Generation Performance:");
        System.out.println("Thread count: " + threadCount);
        System.out.println("Operations per thread: " + iterationsPerThread);
        System.out.println("Total operations: " + totalOperations);
        System.out.println("Total time: " + duration + "ms");
        System.out.println("Average time per operation: " + avgTime + "ms");
        System.out.println("Success count: " + successCount.get());
        System.out.println("Failure count: " + failureCount.get());

        executor.shutdown();
        assertTrue(successCount.get() == totalOperations, "All operations should succeed");
        assertTrue(avgTime < 15, "Average time should be less than 15ms under concurrent load");
    }

    @Test
    void testDatabaseQueryPerformance() {
        int iterations = 100;
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < iterations; i++) {
            List<User> users = userService.list();
            assertNotNull(users);
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        double avgTime = (double) duration / iterations;

        System.out.println("Database Query Performance:");
        System.out.println("Total time: " + duration + "ms");
        System.out.println("Average time per query: " + avgTime + "ms");
        System.out.println("Queries per second: " + (1000 / avgTime));

        assertTrue(avgTime < 100, "Average query time should be less than 100ms");
    }

    @Test
    void testMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        
        // 执行GC
        System.gc();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();

        // 生成大量token
        List<String> tokens = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            tokens.add(jwtUtil.generateAccessToken(testUser));
        }

        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsed = memoryAfter - memoryBefore;
        double memoryPerToken = (double) memoryUsed / tokens.size();

        System.out.println("Memory Usage Analysis:");
        System.out.println("Memory before: " + (memoryBefore / 1024 / 1024) + " MB");
        System.out.println("Memory after: " + (memoryAfter / 1024 / 1024) + " MB");
        System.out.println("Memory used: " + (memoryUsed / 1024 / 1024) + " MB");
        System.out.println("Memory per token: " + memoryPerToken + " bytes");
        System.out.println("Tokens generated: " + tokens.size());

        assertTrue(memoryPerToken < 1000, "Memory per token should be less than 1KB");
    }

    @Test
    void testLoadSimulation() throws InterruptedException {
        int userCount = 50;
        int operationsPerUser = 20;
        ExecutorService executor = Executors.newFixedThreadPool(userCount);
        CountDownLatch latch = new CountDownLatch(userCount);
        AtomicInteger totalOperations = new AtomicInteger(0);
        AtomicInteger successfulOperations = new AtomicInteger(0);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < userCount; i++) {
            final int userId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < operationsPerUser; j++) {
                        totalOperations.incrementAndGet();
                        
                        // 模拟用户操作
                        User user = new User();
                        user.setUsername("user" + userId);
                        user.setPasswordHash("password123");
                        user.setEmail("user" + userId + "@example.com");
                        user.setRole("STUDENT");
                        
                        String token = jwtUtil.generateAccessToken(user);
                        if (token != null && !token.isEmpty()) {
                            successfulOperations.incrementAndGet();
                        }
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("Load Simulation Results:");
        System.out.println("Simulated users: " + userCount);
        System.out.println("Operations per user: " + operationsPerUser);
        System.out.println("Total operations: " + totalOperations.get());
        System.out.println("Successful operations: " + successfulOperations.get());
        System.out.println("Total time: " + duration + "ms");
        System.out.println("Operations per second: " + (totalOperations.get() * 1000.0 / duration));
        System.out.println("Success rate: " + (successfulOperations.get() * 100.0 / totalOperations.get()) + "%");

        executor.shutdown();
        assertTrue(successfulOperations.get() == totalOperations.get(), "All operations should succeed");
        assertTrue(duration < 10000, "Load test should complete within 10 seconds");
    }
}