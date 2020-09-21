package com.example.filestorage.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    private static Pattern pattern;
    private static Matcher matcher;


    public static boolean isValidLine(String line) {
        return !line.replaceAll("[\\n\\t ]", "").isEmpty();
    }

    public static boolean isValid(String[] data) {
        return Validator.isValidField(data[0]) && Validator.isValidField(data[1]) && Validator.isValidTimeStamp(data[3]);
    }

    private static boolean isValidField(String field) {
        pattern = Pattern.compile("^[a-z0-9]", Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(field);
        return matcher.find();
    }

    public static boolean isValidTimeStamp(String timestamp) {
        pattern = Pattern.compile("(^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3})$");
        matcher = pattern.matcher(timestamp);
        return matcher.find();
    }
}
