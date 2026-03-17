package com.spec.kit.exam.system.aspect;

import com.spec.kit.exam.system.annotation.DataPermission;
import com.spec.kit.exam.system.entity.User;
import com.spec.kit.exam.system.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DataPermissionAspectTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private DataPermissionAspect dataPermissionAspect;

    private User adminUser;
    private User teacherUser;
    private User studentUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        adminUser = new User();
        adminUser.setId("1");
        adminUser.setRole("ADMIN");
        
        teacherUser = new User();
        teacherUser.setId("2");
        teacherUser.setRole("TEACHER");
        
        studentUser = new User();
        studentUser.setId("3");
        studentUser.setRole("STUDENT");
    }

    @Test
    void testAdminUserNoDataScope() throws Exception {
        when(userService.getCurrentUser()).thenReturn(adminUser);
        
        DataPermission annotation = mock(DataPermission.class);
        when(annotation.deptAlias()).thenReturn("d");
        when(annotation.userAlias()).thenReturn("u");
        when(annotation.permission()).thenReturn("");
        
        Method method = DataPermissionAspect.class.getDeclaredMethod("buildDataScopeSql", User.class, DataPermission.class);
        method.setAccessible(true);
        String result = (String) method.invoke(dataPermissionAspect, adminUser, annotation);
        
        assertEquals("", result);
    }

    @Test
    void testTeacherUserHasDataScope() throws Exception {
        when(userService.getCurrentUser()).thenReturn(teacherUser);
        
        DataPermission annotation = mock(DataPermission.class);
        when(annotation.deptAlias()).thenReturn("d");
        when(annotation.userAlias()).thenReturn("u");
        when(annotation.permission()).thenReturn("");
        
        Method method = DataPermissionAspect.class.getDeclaredMethod("buildDataScopeSql", User.class, DataPermission.class);
        method.setAccessible(true);
        String result = (String) method.invoke(dataPermissionAspect, teacherUser, annotation);
        
        assertTrue(result.contains("AND d.class_id"));
        assertTrue(result.contains("SELECT class_id FROM teacher_class"));
        assertTrue(result.contains("user_id = 2"));
    }

    @Test
    void testStudentUserHasDataScope() throws Exception {
        when(userService.getCurrentUser()).thenReturn(studentUser);
        
        DataPermission annotation = mock(DataPermission.class);
        when(annotation.deptAlias()).thenReturn("d");
        when(annotation.userAlias()).thenReturn("u");
        when(annotation.permission()).thenReturn("");
        
        Method method = DataPermissionAspect.class.getDeclaredMethod("buildDataScopeSql", User.class, DataPermission.class);
        method.setAccessible(true);
        String result = (String) method.invoke(dataPermissionAspect, studentUser, annotation);
        
        assertTrue(result.contains("AND u.id = 3"));
        assertFalse(result.contains("class_id"));
    }
}