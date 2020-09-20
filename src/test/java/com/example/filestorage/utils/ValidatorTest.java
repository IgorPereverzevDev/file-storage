package com.example.filestorage.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidatorTest {

    @Test
    void isValidLine() {
        assertTrue(Validator.isValidLine("\t\t\t\tabc"));
    }

    @Test
    void isInValidLine() {
        assertFalse(Validator.isValidLine("\t\t\t\t"));
    }

    @Test
    void isValidName() {
        assertTrue(Validator.isValidName("abc"));
    }

    @Test
    void isInValidName() {
        assertFalse(Validator.isValidName("123"));
    }

    @Test
    void isValidId() {
        assertTrue(Validator.isValidId("123"));
    }

    @Test
    void isInValidId() {
        assertFalse(Validator.isValidId("asbcds!!,,,"));
    }

    @Test
    void isValidTimeStamp() {
        assertTrue(Validator.isValidTimeStamp("2020-09-20 14:54:00.121"));
    }

    @Test
    void isInValidTimeStamp() {
        assertFalse(Validator.isValidTimeStamp("aaaaa020-09-20 14:54:00.121!!!!"));
    }
}