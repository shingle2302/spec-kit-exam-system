package com.spec.kit.exam.system.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class DataEncryptUtilTest {

    @InjectMocks
    private DataEncryptUtil dataEncryptUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(dataEncryptUtil, "encryptionKey", "12345678901234567890123456789012");
    }

    @Test
    void testEncryptAndDecrypt() {
        String plainText = "Hello, World!";
        
        String encrypted = dataEncryptUtil.encrypt(plainText);
        assertNotNull(encrypted);
        assertFalse(encrypted.equals(plainText));
        
        String decrypted = dataEncryptUtil.decrypt(encrypted);
        assertEquals(plainText, decrypted);
    }

    @Test
    void testEncryptDifferentInputs() {
        String input1 = "Test String 1";
        String input2 = "Test String 2";
        
        String encrypted1 = dataEncryptUtil.encrypt(input1);
        String encrypted2 = dataEncryptUtil.encrypt(input2);
        
        assertNotEquals(encrypted1, encrypted2);
        
        assertEquals(input1, dataEncryptUtil.decrypt(encrypted1));
        assertEquals(input2, dataEncryptUtil.decrypt(encrypted2));
    }

    @Test
    void testEncryptEmptyString() {
        String plainText = "";
        
        String encrypted = dataEncryptUtil.encrypt(plainText);
        assertNotNull(encrypted);
        
        String decrypted = dataEncryptUtil.decrypt(encrypted);
        assertEquals(plainText, decrypted);
    }

    @Test
    void testEncryptSpecialCharacters() {
        String plainText = "测试中文!@#$%^&*()";
        
        String encrypted = dataEncryptUtil.encrypt(plainText);
        assertNotNull(encrypted);
        
        String decrypted = dataEncryptUtil.decrypt(encrypted);
        assertEquals(plainText, decrypted);
    }

    @Test
    void testEncryptLongString() {
        StringBuilder longText = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            longText.append("Test");
        }
        
        String plainText = longText.toString();
        String encrypted = dataEncryptUtil.encrypt(plainText);
        assertNotNull(encrypted);
        
        String decrypted = dataEncryptUtil.decrypt(encrypted);
        assertEquals(plainText, decrypted);
    }

    @Test
    void testDecryptInvalidData() {
        String invalidCipherText = "InvalidEncryptedData123";
        
        assertThrows(RuntimeException.class, () -> {
            dataEncryptUtil.decrypt(invalidCipherText);
        });
    }
}