package com.exam.system.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdministratorModelTest {

    private Administrator administrator;

    @BeforeEach
    void setUp() {
        administrator = new Administrator();
    }

    @Test
    void testAdministratorCreation() {
        assertNotNull(administrator);
    }

    @Test
    void testAdminLevelAccessors() {
        Administrator.AdminLevel adminLevel = Administrator.AdminLevel.SYSTEM;
        administrator.setAdminLevel(adminLevel);
        assertEquals(adminLevel, administrator.getAdminLevel());
    }

    @Test
    void testUserAccessors() {
        User user = new User();
        administrator.setUser(user);
        assertEquals(user, administrator.getUser());
    }

    @Test
    void testAdministratorConstructor() {
        Administrator newAdmin = new Administrator();
        assertNotNull(newAdmin);
    }

    @Test
    void testAdminLevelEnumValues() {
        Administrator.AdminLevel[] levels = Administrator.AdminLevel.values();
        assertEquals(3, levels.length);
        assertTrue(java.util.Arrays.asList(levels).contains(Administrator.AdminLevel.SYSTEM));
        assertTrue(java.util.Arrays.asList(levels).contains(Administrator.AdminLevel.SCHOOL));
        assertTrue(java.util.Arrays.asList(levels).contains(Administrator.AdminLevel.DEPARTMENT));
    }
}