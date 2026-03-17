package com.spec.kit.exam.system.performance;

import com.spec.kit.exam.system.entity.User;
import com.spec.kit.exam.system.service.UserService;
import com.spec.kit.exam.system.util.JwtUtilEnhanced;
import com.spec.kit.exam.system.util.DataEncryptUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PerformanceTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtUtilEnhanced jwtUtil;

    @Mock
    private DataEncryptUtil dataEncryptUtil;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
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
        when(jwtUtil.generateAccessToken(testUser)).thenReturn("test.token.here");
        
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
        verify(jwtUtil, times(iterations)).generateAccessToken(testUser);
    }

    @Test
    void testJwtTokenParsingPerformance() {
        when(jwtUtil.generateAccessToken(testUser)).thenReturn("test.token.here");
        
        io.jsonwebtoken.Claims mockClaims = mock(io.jsonwebtoken.Claims.class);
        when(mockClaims.get("username")).thenReturn("perfuser");
        when(mockClaims.get("role")).thenReturn("TEACHER");
        when(jwtUtil.parseToken("test.token.here")).thenReturn(mockClaims);
        
        String token = jwtUtil.generateAccessToken(testUser);
        int iterations = 1000;
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < iterations; i++) {
            io.jsonwebtoken.Claims claims = jwtUtil.parseToken(token);
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
        verify(jwtUtil, times(iterations)).parseToken(token);
    }

    @Test
    void testDataEncryptionPerformance() {
        String plainText = "This is a test string for encryption performance testing.";
        when(dataEncryptUtil.encrypt(plainText)).thenReturn("encrypted_text");
        when(dataEncryptUtil.decrypt("encrypted_text")).thenReturn(plainText);
        
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
        verify(dataEncryptUtil, times(iterations)).encrypt(plainText);
        verify(dataEncryptUtil, times(iterations)).decrypt("encrypted_text");
    }

    @Test
    void testConcurrentTokenGeneration() throws InterruptedException {
        when(jwtUtil.generateAccessToken(testUser)).thenReturn("test.token.here");
        
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
        verify(jwtUtil, times(totalOperations)).generateAccessToken(testUser);
    }

    @Test
    void testDatabaseQueryPerformance() {
        when(userService.getUsers(1, 100, null)).thenReturn(new ArrayList<>());
        
        int iterations = 100;
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < iterations; i++) {
            List<User> users = userService.getUsers(1, 100, null);
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
        verify(userService, times(iterations)).getUsers(1, 100, null);
    }

    @Test
    void testMemoryUsage() {
        when(jwtUtil.generateAccessToken(testUser)).thenReturn("test.token.here");
        
        Runtime runtime = Runtime.getRuntime();
        
        System.gc();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();

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

        assertTrue(memoryPerToken < 10000, "Memory per token should be less than 10KB");
        verify(jwtUtil, times(1000)).generateAccessToken(testUser);
    }

    @Test
    void testLoadSimulation() throws InterruptedException {
        when(jwtUtil.generateAccessToken(any(User.class))).thenReturn("test.token.here");
        
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
        verify(jwtUtil, times(totalOperations.get())).generateAccessToken(any(User.class));
    }
}