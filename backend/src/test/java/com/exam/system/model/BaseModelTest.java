package com.exam.system.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BaseModelTest {

    private TestModelForBaseModel testModel;

    // Test class that extends BaseModel
    static class TestModelForBaseModel extends BaseModel {
        public TestModelForBaseModel() {
            super();
        }
    }

    @BeforeEach
    void setUp() {
        testModel = new TestModelForBaseModel();
    }

    @Test
    void testBaseModelCreation() {
        assertNotNull(testModel);
    }

    @Test
    void testIdAccessors() {
        testModel.setId(1L);
        assertEquals(1L, testModel.getId());
    }

    @Test
    void testCreatorAccessors() {
        String creator = "testuser";
        testModel.setCreator(creator);
        assertEquals(creator, testModel.getCreator());
    }

    @Test
    void testCreateTimeAccessors() {
        LocalDateTime now = LocalDateTime.now();
        testModel.setCreateTime(now);
        assertEquals(now, testModel.getCreateTime());
    }

    @Test
    void testUpdaterAccessors() {
        String updater = "testuser";
        testModel.setUpdater(updater);
        assertEquals(updater, testModel.getUpdater());
    }

    @Test
    void testUpdateTimeAccessors() {
        LocalDateTime now = LocalDateTime.now();
        testModel.setUpdateTime(now);
        assertEquals(now, testModel.getUpdateTime());
    }

    @Test
    void testDeletedAccessors() {
        testModel.setDeleted(1);
        assertEquals(Integer.valueOf(1), testModel.getDeleted());
    }

    @Test
    void testBaseModelConstructor() {
        TestModelForBaseModel model = new TestModelForBaseModel();
        assertNotNull(model);
        assertEquals(Integer.valueOf(0), model.getDeleted());
    }
}