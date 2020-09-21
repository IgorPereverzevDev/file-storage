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
    void isValid() {
        String[] arr = {"abc", "name", "desc", "2020-09-20 14:54:00.121"};
        assertTrue(Validator.isValid(arr));
    }

    @Test
    void isValidFail() {
        String[] arr = {"", "name", "desc", "2020-09-20 14:54:00.121"};
        assertFalse(Validator.isValid(arr));
    }


}